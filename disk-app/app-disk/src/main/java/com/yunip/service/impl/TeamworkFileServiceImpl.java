package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.enums.disk.FileType;
import com.yunip.enums.disk.FolderType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.disk.OperateType;
import com.yunip.enums.disk.ValidStatus;
import com.yunip.enums.teamwork.MsgType;
import com.yunip.manage.FileIndexManager;
import com.yunip.manage.FileManager;
import com.yunip.manage.LogManager;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.teamwork.ITeamworkDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameTeamworkNameHelper;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.query.TeamworkFileQuery;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;
import com.yunip.model.teamwork.query.TeamworkFolderQuery;
import com.yunip.service.ITeamworkFileService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.serial.SerialCodeUtil;
import com.yunip.utils.util.StringUtil;

@Service("iTeamworkFileService")
public class TeamworkFileServiceImpl implements ITeamworkFileService {

    @Resource(name = "iTeamworkDao")
    private ITeamworkDao             teamworkDao;

    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao         teamworkFileDao;

    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao       teamworkFolderDao;

    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao  teamworkFileVersionDao;

    @Resource(name = "iFileDao")
    private IFileDao                 fileDao;

    @Resource(name = "iFileVersionDao")
    private IFileVersionDao          fileVersionDao;

    @Resource(name = "iFolderDao")
    private IFolderDao               folderDao;

    @Resource(name = "logManager")
    private LogManager               logManager;

    @Resource(name = "fileIndexManager")
    private FileIndexManager         fileIndexManager;

    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager   teamworkMessageManager;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;

    @Override
    public TeamworkFolder getQuerySubFolder(
            TeamworkFolderQuery teamworkFolderQuery) {
        TeamworkFolder teamworkFolder = new TeamworkFolder();
        //根据query查询类的folderId判断查询方法，若存在则查询此目录下文件，不存在则查询该协作的最上层文件及文件夹
        if (teamworkFolderQuery != null
                && (teamworkFolderQuery.getFolderId() == null || teamworkFolderQuery.getFolderId() == 0)) {
            //查询协作文件首层目录
            teamworkFolderQuery.setFolderId(0);
            teamworkFolder.setId(0);
        }
        else {
            //先查询到指定的目录对象
            teamworkFolder = teamworkFolderDao.selectById(teamworkFolderQuery.getFolderId());
        }
        //获取所有协作文件夹
        TeamworkFolderQuery zTeamworkFolderQuery = new TeamworkFolderQuery();
        if (StringUtils.isNotBlank(teamworkFolderQuery.getQueryName())) {
            zTeamworkFolderQuery.setQueryName(teamworkFolderQuery.getQueryName());
        }
        else {
            zTeamworkFolderQuery.setParentId(teamworkFolderQuery.getFolderId());
        }
        zTeamworkFolderQuery.setTeamworkId(teamworkFolderQuery.getTeamworkId() == null ? 0 : teamworkFolderQuery.getTeamworkId());
        zTeamworkFolderQuery.setOrderIndex(teamworkFolderQuery.getOrderIndex());
        List<TeamworkFolder> zTeamworkFolders = teamworkFolderDao.selectByQuery(zTeamworkFolderQuery);
        //获取所有协作文件
        TeamworkFileQuery zTeamworkFileQuery = new TeamworkFileQuery();
        if (StringUtils.isNotBlank(teamworkFolderQuery.getQueryName())) {
            zTeamworkFileQuery.setQueryName(teamworkFolderQuery.getQueryName());
        }
        else {
            zTeamworkFileQuery.setFolderId(teamworkFolderQuery.getFolderId());
        }
        zTeamworkFileQuery.setTeamworkId(teamworkFolderQuery.getTeamworkId() == null ? 0 : teamworkFolderQuery.getTeamworkId());
        zTeamworkFileQuery.setOrderIndex(teamworkFolderQuery.getOrderIndex());
        List<TeamworkFile> zTeamworkFiles = teamworkFileDao.selectByQuery(zTeamworkFileQuery);
        //放入对象
        teamworkFolder.setTeamworkFolders(zTeamworkFolders);
        teamworkFolder.setTeamworkFiles(zTeamworkFiles);
        //查询协作名
        Teamwork teamwork = teamworkDao.selectById(teamworkFolderQuery.getTeamworkId());
        teamworkFolder.setTeamworkName(teamwork.getTeamworkName());
        teamworkFolder.setIcon(teamwork.getIcon());
        return teamworkFolder;
    }

    @Override
    public List<TeamworkFolder> getParentFolders(int folderId) {
        List<TeamworkFolder> teamworkFolders = new ArrayList<TeamworkFolder>();
        //当前目录
        TeamworkFolder teamworkFolder = teamworkFolderDao.selectById(folderId);
        if (teamworkFolder != null) {
            teamworkFolders.add(teamworkFolder);
        }
        getParentFolder(teamworkFolders, teamworkFolder);
        Collections.reverse(teamworkFolders);
        return teamworkFolders;
    }

    /**
     * 递归查询上级目录
     * @param teamworkFolders
     * @param teamworkFolder  
     * void 
     * @exception
     */
    private void getParentFolder(List<TeamworkFolder> teamworkFolders,
            TeamworkFolder teamworkFolder) {
        if (teamworkFolder != null && teamworkFolder.getParentId() != 0) {
            TeamworkFolder pTeamworkFolder = teamworkFolderDao.selectById(teamworkFolder.getParentId());
            teamworkFolders.add(pTeamworkFolder);
            getParentFolder(teamworkFolders, pTeamworkFolder);
        }
    }

    @Override
    @Transactional
    public int createTeamworkFolder(TeamworkFolder teamworkFolder,
            Employee employee) {
        String parentCode = SystemContant.FIRST_FOLDER_ID;
        teamworkFolder.setEmployeeId(employee.getId());
        teamworkFolder.setCreateAdmin(employee.getEmployeeName());
        teamworkFolder.setCreateTime(new Date());
        teamworkFolder.setFolderName(teamworkFolder.getFolderName().trim());
        teamworkFolder.setValidStatus(ValidStatus.NOMAL.getStatus());
        if (teamworkFolder != null && teamworkFolder.getParentId() != 0) {
            TeamworkFolder pTeamworkFolder = teamworkFolderDao.selectById(teamworkFolder.getParentId());
            //将新增子目录与父目录的employeeId保持一致
            teamworkFolder.setEmployeeId(pTeamworkFolder.getEmployeeId());
            parentCode = pTeamworkFolder.getFolderCode();
        }
        //若存在同名文件则修改名称
        TeamworkFolderQuery teamworkFolderQuery = new TeamworkFolderQuery();
        teamworkFolderQuery.setParentId(teamworkFolder.getParentId());
        teamworkFolderQuery.setTeamworkId(teamworkFolder.getTeamworkId());
        teamworkFolderQuery.setQueryName(teamworkFolder.getFolderName());
        setTeamworkFolderName(teamworkFolder, teamworkFolderQuery);
        //查询该协作当前文件夹的所有子文件夹code
        TeamworkFolderQuery query = new TeamworkFolderQuery();
        query.setTeamworkId(teamworkFolder.getTeamworkId());
        query.setParentId(teamworkFolder.getParentId());
        List<String> folderCodes = teamworkFolderDao.selectByParentId(query);
        if (folderCodes != null && folderCodes.size() >= 9999) {
            throw new MyException(DiskException.NOCREATEFOLDERS);
        }
        //通过util, 获取新创建文件夹的code
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                parentCode);
        teamworkFolder.setFolderCode(folderCode);
        teamworkFolderDao.insert(teamworkFolder);
        //添加协作记录
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessage(
                MsgType.CREATE_FOLDER.getType(), employee.getId(),
                employee.getEmployeeName(), teamworkFolder.getTeamworkId(),
                null, null, teamworkFolder.getId(), null);
        return teamworkMessageId;
    }

    /***
     * 设置同名的文件夹名称
     * @param folder  
     * void 
     * @exception
     */
    private void setFolderName(Folder folder, FolderQuery folderQuery) {
        //存在同名的文件
        List<Folder> folders = folderDao.selectByQuery(folderQuery);
        List<String> olderFolderName = new ArrayList<String>();
        if (folders != null && folders.size() > 0) {
            for (Folder tmFolder : folders) {
                olderFolderName.add(tmFolder.getFolderName());
            }
        }
        String folderName = SerialCodeUtil.getName(folder.getFolderName(),
                olderFolderName);
        folder.setFolderName(folderName);
    }

    /***
     * 设置同名的文件夹名称
     * @param teamworkFolder  
     * @param teamworkFolderQuery  
     * void 
     * @exception
     */
    private void setTeamworkFolderName(TeamworkFolder teamworkFolder,
            TeamworkFolderQuery teamworkFolderQuery) {
        //存在同名的文件夹
        List<TeamworkFolder> teamworkFolders = teamworkFolderDao.selectByQuery(teamworkFolderQuery);
        //所有该目录下文件夹名称
        List<String> oldFolderName = new ArrayList<String>();
        if (teamworkFolders != null && teamworkFolders.size() > 0) {
            for (TeamworkFolder tmFolder : teamworkFolders) {
                oldFolderName.add(tmFolder.getFolderName());
            }
        }
        //调用util重命名相同名称
        String folderName = SerialCodeUtil.getName(
                teamworkFolder.getFolderName(), oldFolderName);
        teamworkFolder.setFolderName(folderName);
    }

    /***
     * 设置同名的文件名称
     * @param folder  
     * void 
     * @exception
     */
    private void setFileName(File file, FileQuery fileQuery) {
        String queryName = StringUtil.getFirstName(fileQuery.getQueryName());
        fileQuery.setQueryName(queryName);
        List<File> files = fileDao.selectByQuery(fileQuery);
        //存在同名的文件
        List<String> oldFileNames = new ArrayList<String>();
        if (files != null && files.size() > 0) {
            for (File tmFile : files) {
                oldFileNames.add(tmFile.getFileName());
            }
        }
        String fileName = SerialCodeUtil.getFileName(file.getFileName(),
                oldFileNames);
        file.setFileName(fileName);
    }

    /**
     * 设置同名的文件名称
     * @param teamworkFile
     * @param teamworkFileQuery  
     * void 
     * @exception
     */
    private void setTeamworkFileName(TeamworkFile teamworkFile,
            TeamworkFileQuery teamworkFileQuery) {
        //存在同名的文件
        List<TeamworkFile> teamworkFiles = teamworkFileDao.selectByQuery(teamworkFileQuery);
        //所有该目录下文件名称
        List<String> oldFileNames = new ArrayList<String>();
        if (teamworkFiles != null && teamworkFiles.size() > 0) {
            for (TeamworkFile tmFile : teamworkFiles) {
                oldFileNames.add(tmFile.getFileName());
            }
        }
        String fileName = SerialCodeUtil.getFileName(
                teamworkFile.getFileName(), oldFileNames);
        teamworkFile.setFileName(fileName);
    }

    @Override
    public boolean checkSameName(RenameHelper renameHelper) {
        if (OpenType.FOLDER.getType() == renameHelper.getOpenType()) {
            //查询同层目录 ，文件夹名是否有同名
            TeamworkFolder teamworkFolder = teamworkFolderDao.selectById(renameHelper.getOpenId());
            TeamworkFolderQuery teamworkFolderQuery = new TeamworkFolderQuery();
            teamworkFolderQuery.setTeamworkId(teamworkFolder.getTeamworkId());
            teamworkFolderQuery.setParentId(teamworkFolder.getParentId());
            teamworkFolderQuery.setFolderName(renameHelper.getName());
            teamworkFolderQuery.setpFolderId(teamworkFolder.getId()); //此属性为排除自身id，sql语句不搜索包含该id的条目
            int count = teamworkFolderDao.selectCountByQuery(teamworkFolderQuery);
            if (count > 0) {
                //存在
                return true;
            }
        }
        else if (OpenType.FILE.getType() == renameHelper.getOpenType()) {
            //查询同层目录 ，文件名是否有同名
            TeamworkFile teamworkFile = teamworkFileDao.selectById(renameHelper.getOpenId());
            TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
            teamworkFileQuery.setTeamworkId(teamworkFile.getTeamworkId());
            teamworkFileQuery.setFolderId(teamworkFile.getFolderId());
            teamworkFileQuery.setFileName(renameHelper.getName());
            teamworkFileQuery.setpFileId(teamworkFile.getId());
            int count = teamworkFileDao.selectCountByQuery(teamworkFileQuery);
            if (count > 0) {
                //存在
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public int rename(RenameHelper renameHelper, Employee employee) {
        int teamworkMessage = 0;
        if (OpenType.FOLDER.getType() == renameHelper.getOpenType()) {
            //文件夹
            TeamworkFolder teamworkFolder = teamworkFolderDao.selectById(renameHelper.getOpenId());
            if (teamworkFolder != null) {
                //修改文件夹名称
                teamworkFolder.setFolderName(renameHelper.getName().trim());
                teamworkFolder.setUpdateAdmin(employee.getEmployeeName());
                //查询同名
                TeamworkFolderQuery teamworkFolderQuery = new TeamworkFolderQuery();
                teamworkFolderQuery.setParentId(teamworkFolder.getParentId());
                teamworkFolderQuery.setTeamworkId(teamworkFolder.getTeamworkId());
                teamworkFolderQuery.setQueryName(teamworkFolder.getFolderName());
                teamworkFolderQuery.setpFolderId(teamworkFolder.getId());//此属性为排除自身id，sql语句不搜索包含该id的条目
                setTeamworkFolderName(teamworkFolder, teamworkFolderQuery);
                teamworkFolderDao.update(teamworkFolder);
            }
            //协作日志
            teamworkMessage = teamworkMessageManager.saveTeamworkMessage(
                    MsgType.RENAME.getType(), employee.getId(),
                    employee.getEmployeeName(), teamworkFolder.getTeamworkId(),
                    null, null, teamworkFolder.getId(), null);
        }
        else if (OpenType.FILE.getType() == renameHelper.getOpenType()) {
            //文件
            TeamworkFile teamworkFile = teamworkFileDao.selectById(renameHelper.getOpenId());
            if (teamworkFile != null) {
                //修改文件名称, 去左空格
                teamworkFile.setFileName(renameHelper.getName().replaceAll(
                        "^[　 ]+", ""));
                FileType fileType = FileType.getFileType(teamworkFile.getFileName());
                teamworkFile.setFileType(fileType.getType());
                teamworkFile.setUpdateAdmin(employee.getEmployeeName());
                //查询同名
                TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
                teamworkFileQuery.setFolderId(teamworkFile.getFolderId());
                teamworkFileQuery.setTeamworkId(teamworkFile.getTeamworkId());
                teamworkFileQuery.setQueryName(teamworkFile.getFileName());
                teamworkFileQuery.setpFileId(teamworkFile.getId());//此属性为排除自身id，sql语句不搜索包含该id的条目
                setTeamworkFileName(teamworkFile, teamworkFileQuery);
                teamworkFileDao.update(teamworkFile);
            }
            //协作日志
            teamworkMessage = teamworkMessageManager.saveTeamworkMessage(
                    MsgType.RENAME.getType(), employee.getId(),
                    employee.getEmployeeName(), teamworkFile.getTeamworkId(),
                    null, teamworkFile.getId(), null, null);
        }
        return teamworkMessage;
    }

    @Override
    @Transactional
    public int delFolderOrFile(TeamworkFolder pTeamworkFolder,
            Employee employee, Employee employeeLog) {
        //勾选删除的文件和文件夹
        List<TeamworkFolder> teamworkFolders = pTeamworkFolder.getTeamworkFolders();
        List<TeamworkFile> teamworkFiles = pTeamworkFolder.getTeamworkFiles();
        //日志文件及文件夹
        List<Integer> fileIds = new ArrayList<Integer>();
        List<Integer> folderIds = new ArrayList<Integer>();
        //文件物理路径待删除集，（final修饰引用地址不能改变）
        final Set<String> deleteFilePaths = new HashSet<String>();
        //传入待删除路径方法
        handleDelete(deleteFilePaths, teamworkFiles, teamworkFolders, fileIds,
                folderIds);
        //添加进协作日志
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessages(MsgType.DELETE.getType(),
                employee.getId(), employee.getEmployeeName(),
                pTeamworkFolder.getTeamworkId(), fileIds, folderIds);
        //遍历删除勾选文件及文件相关联表
        if (teamworkFiles != null && teamworkFiles.size() > 0) {
            for (TeamworkFile teamworkFile : teamworkFiles) {
                //删除文件本身
                teamworkFileDao.delById(teamworkFile.getId());
                //删除文件关联历史版本
                teamworkFileVersionDao.delByFileId(teamworkFile.getId());
            }
        }
        //遍历删除勾选文件夹及其相关表（目录树）
        if (teamworkFolders != null && teamworkFolders.size() > 0) {
            for (TeamworkFolder teamworkFolder : teamworkFolders) {
                teamworkFolder = teamworkFolderDao.selectById(teamworkFolder.getId());
                //删除所有子文件树
                TeamworkFile tmpFile = new TeamworkFile();
                tmpFile.setTeamworkId(teamworkFolder.getTeamworkId());
                tmpFile.setFolderCode(teamworkFolder.getFolderCode());
                teamworkFileDao.delByCode(tmpFile);
                //删除所有子文件的版本信息
                TeamworkFileVersion tmpFileVersion = new TeamworkFileVersion();
                tmpFileVersion.setTeamworkId(teamworkFolder.getTeamworkId());
                tmpFileVersion.setFolderCode(teamworkFolder.getFolderCode());
                teamworkFileVersionDao.delByCode(tmpFileVersion);
                //删除所有子文件夹树
                TeamworkFolder tmpFolder = new TeamworkFolder();
                tmpFolder.setTeamworkId(teamworkFolder.getTeamworkId());
                tmpFolder.setFolderCode(teamworkFolder.getFolderCode());
                teamworkFolderDao.delByCode(tmpFolder);
            }
        }
        //物理路径排查唯一性（需要在表数据已经删除的情况下！否则搜索到本身就要删除的数据）
        fileManager.getOnlyUseFilePath(deleteFilePaths);
        //线程删除物理路径
        fileManager.threadDeleteFile(deleteFilePaths);
        return teamworkMessageId;
    }

    /**
     * 处理所有勾选文件及文件夹的待删除路径
     * @param filePaths
     * @param teamworkFiles
     * @param teamworkFolders  
     * void 
     * @exception
     */
    private void handleDelete(Set<String> filePaths,
            List<TeamworkFile> teamworkFiles,
            List<TeamworkFolder> teamworkFolders, List<Integer> fileIds,
            List<Integer> folderIds) {
        //将勾选的文件的路径及关联历史文件的路径加入到待删除filePaths中
        if (teamworkFiles != null && teamworkFiles.size() > 0) {
            for (TeamworkFile teamworkFile : teamworkFiles) {
                //处理删除文件
                teamworkFile = teamworkFileDao.selectById(teamworkFile.getId());
                handleDeleteFile(filePaths, teamworkFile, fileIds);
            }
        }
        //将勾选的目录下，所有文件的路径及关联历史文件的路径加入到待删除filePaths中
        if (teamworkFolders != null && teamworkFolders.size() > 0) {
            for (TeamworkFolder teamworkFolder : teamworkFolders) {
                //处理删除文件夹
                handleDeleteFolder(filePaths, teamworkFolder.getId(), fileIds,
                        folderIds);
            }
        }
    }

    /**
     * 处理删除文件
     * @param filePaths 待删除物理文件路径集
     * @param teamworkFile  协作文件对象
     * void 
     * @exception
     */
    private void handleDeleteFile(Set<String> filePaths,
            TeamworkFile teamworkFile, List<Integer> fileIds) {
        //日志文件id添加
        fileIds.add(teamworkFile.getId());
        //将文件本身加入删除路径集
        filePaths.add(teamworkFile.getFilePath());
        //将文件对应历史文件加入删除路径集
        TeamworkFileVersionQuery teamworkFileVersionQuery = new TeamworkFileVersionQuery();
        teamworkFileVersionQuery.setFileId(teamworkFile.getId());
        List<TeamworkFileVersion> teamworkFileVersions = teamworkFileVersionDao.selectByQuery(teamworkFileVersionQuery);
        if (teamworkFileVersions != null && teamworkFileVersions.size() > 0) {
            for (TeamworkFileVersion teamworkFileVersion : teamworkFileVersions) {
                filePaths.add(teamworkFileVersion.getFilePath());
            }
        }
    }

    /**
     * 处理删除文件夹
     * @param filePaths 待删除物理文件路径集
     * @param folderId  当前文件夹id（做父文件夹parentId用）
     * void 
     * @exception
     */
    private void handleDeleteFolder(Set<String> filePaths, int folderId,
            List<Integer> fileIds, List<Integer> folderIds) {
        //日志文件夹id添加
        folderIds.add(folderId);
        //获取当前目录下的文件
        TeamworkFileQuery zTeamworkFileQuery = new TeamworkFileQuery();
        zTeamworkFileQuery.setFolderId(folderId);
        List<TeamworkFile> zTeamworkFiles = teamworkFileDao.selectByQuery(zTeamworkFileQuery);
        if (zTeamworkFiles != null && zTeamworkFiles.size() > 0) {
            for (TeamworkFile zTeamworkFile : zTeamworkFiles) {
                //将所有目录文件，加入处理删除文件路径方法
                handleDeleteFile(filePaths, zTeamworkFile, fileIds);
            }
        }
        //获取当前目录下的文件夹
        TeamworkFolderQuery zTeamworkFolderQuery = new TeamworkFolderQuery();
        zTeamworkFolderQuery.setParentId(folderId);
        List<TeamworkFolder> zTeamworkFolders = teamworkFolderDao.selectByQuery(zTeamworkFolderQuery);
        if (zTeamworkFolders != null && zTeamworkFolders.size() > 0) { //递归结束条件
            for (TeamworkFolder zTeamworkFolder : zTeamworkFolders) {
                //递归本身，逐级向下递归所有目录，查询出的子文件夹id作为下次递归的父文件夹id
                handleDeleteFolder(filePaths, zTeamworkFolder.getId(), fileIds,
                        folderIds);
            }
        }
    }

    @Override
    public List<TeamworkFolder> getSubTwoFolders(int folderId,
            Employee employee, String teamworkId) {
        TeamworkFolderQuery folderQuery = new TeamworkFolderQuery();
        folderQuery.setParentId(folderId);
        folderQuery.setTeamworkId(Integer.valueOf(teamworkId));
        List<TeamworkFolder> folders = teamworkFolderDao.selectByQuery(folderQuery);
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder folder : folders) {
                folderQuery.setParentId(folder.getId());
                List<TeamworkFolder> subfolders = teamworkFolderDao.selectByQuery(folderQuery);
                folder.setTeamworkFolders(subfolders);
            }
        }
        return folders;
    }

    @Override
    public SameTeamworkNameHelper getSameName(TeamworkFolder folder,
            int folderId) {
        List<TeamworkFolder> folders = folder.getTeamworkFolders();
        List<TeamworkFile> files = folder.getTeamworkFiles();
        SameTeamworkNameHelper sameName = new SameTeamworkNameHelper();
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder subFolder : folders) {
                subFolder = teamworkFolderDao.selectById(subFolder.getId());
                TeamworkFolderQuery folderQuery = new TeamworkFolderQuery();
                folderQuery.setParentId(folderId);
                folderQuery.setTeamworkId(folder.getTeamworkId());
                folderQuery.setFolderName(subFolder.getFolderName());
                List<TeamworkFolder> list = teamworkFolderDao.selectByQuery(folderQuery);
                if (list != null && list.size() > 0) {
                    TeamworkFolder fold = list.get(0);
                    fold.setUpdateDate(DateUtil.getDateFormat(
                            fold.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getNowFolders().add(fold);
                    subFolder.setUpdateDate(DateUtil.getDateFormat(
                            subFolder.getUpdateTime(),
                            DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getOldFolders().add(subFolder);
                }
            }
        }
        //移动文件
        if (files != null && files.size() > 0) {
            for (TeamworkFile file : files) {
                file = teamworkFileDao.selectById(file.getId());
                TeamworkFileQuery fileQuery = new TeamworkFileQuery();
                fileQuery.setFolderId(folderId);
                fileQuery.setTeamworkId(folder.getTeamworkId());
                fileQuery.setFileName(file.getFileName());
                List<TeamworkFile> list = teamworkFileDao.selectByQuery(fileQuery);
                if (list != null && list.size() > 0) {
                    TeamworkFile fil = list.get(0);
                    fil.setUpdateDate(DateUtil.getDateFormat(
                            fil.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    file.setUpdateDate(DateUtil.getDateFormat(
                            file.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getNowFiles().add(list.get(0));
                    sameName.getOldFiles().add(file);
                }
            }
        }
        return sameName;
    }

    @Override
    public SameTeamworkNameHelper getExportSameName(TeamworkFolder folder,
            int folderId, Employee employee) {
        List<TeamworkFolder> folders = folder.getTeamworkFolders();
        List<TeamworkFile> files = folder.getTeamworkFiles();
        SameTeamworkNameHelper sameName = new SameTeamworkNameHelper();
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder subFolder : folders) {
                subFolder = teamworkFolderDao.selectById(subFolder.getId());
                //查询我的文件夹中，是否同名
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setParentId(folderId);
                folderQuery.setEmployeeId(employee.getId());
                folderQuery.setFolderName(subFolder.getFolderName());
                List<Folder> list = folderDao.selectByQuery(folderQuery);
                if (list != null && list.size() > 0) {
                    Folder folderTmp = list.get(0);
                    TeamworkFolder fold = convertFolderToTeamworkFolder(
                            folderTmp, employee);
                    fold.setUpdateDate(DateUtil.getDateFormat(
                            fold.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getNowFolders().add(fold);
                    subFolder.setUpdateDate(DateUtil.getDateFormat(
                            subFolder.getUpdateTime(),
                            DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getOldFolders().add(subFolder);
                }
            }
        }
        //移动文件
        if (files != null && files.size() > 0) {
            for (TeamworkFile file : files) {
                file = teamworkFileDao.selectById(file.getId());
                //查询我的文件中，是否同名
                FileQuery fileQuery = new FileQuery();
                fileQuery.setFolderId(folderId);
                fileQuery.setEmployeeId(employee.getId());
                fileQuery.setFileName(file.getFileName());
                List<File> list = fileDao.selectByQuery(fileQuery);
                if (list != null && list.size() > 0) {
                    File fileTmp = list.get(0);
                    TeamworkFile fil = convertFileToTeamworkFile(fileTmp);
                    fil.setUpdateDate(DateUtil.getDateFormat(
                            fil.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    file.setUpdateDate(DateUtil.getDateFormat(
                            file.getUpdateTime(), DateUtil.YMDHMS_DATA_MINTUE));
                    sameName.getNowFiles().add(fil);
                    sameName.getOldFiles().add(file);
                }
            }
        }
        return sameName;
    }

    @Override
    @Transactional
    public void moveFolderOrFile(TeamworkFolder folder, int folderId,
            Employee employee, Integer teamworkId) {
        List<TeamworkFolder> folders = folder.getTeamworkFolders();
        List<TeamworkFile> files = folder.getTeamworkFiles();
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder subFolder : folders) {
                subFolder = teamworkFolderDao.selectById(subFolder.getId());
                moveFolder(subFolder, folderId, employee, teamworkId);
                //记录日志
                //                logManager.insertEmpLoyeeLog(subFolder.getId(),
                //                        OpenType.FOLDER, subFolder.getFolderName(),
                //                        ActionType.MOVE, employee);
            }
        }
        //移动文件
        if (files != null && files.size() > 0) {
            for (TeamworkFile file : files) {
                moveFile(file, folderId, employee, teamworkId);
                //记录日志
                //                logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                //                        file.getFileName(), ActionType.MOVE, employee);
            }
        }
    }

    @Override
    @Transactional
    public void moveFolder(TeamworkFolder folder, int folderId,
            Employee employee, Integer teamworkId) {
        TeamworkFolder toFolder = null;
        if (folderId == 0) {
            toFolder = new TeamworkFolder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        else {
            toFolder = teamworkFolderDao.selectById(folderId);
            //设置所属人的姓名(确保移动中不会改变创建人)
            employee.setEmployeeName(toFolder.getCreateAdmin());
        }
        TeamworkFolder oldFolder = getCopyAndMoveFolder(folder, toFolder,
                employee, OperateType.Move.getType(), teamworkId);
        teamworkFolderDao.update(oldFolder);
        //修改子集的文件夹编码和文件中的folderCode
        moveFolderCode(oldFolder.getId(), oldFolder, employee, teamworkId);
    }

    /****
     *  移动和操作的公共方法
     * @param folder     移动或者操作的文件夹ID
     * @param toFolder   移动到的文件夹ID
     * @param employee   操作的员工
     * @param type       行为是复制or移动（复制：1  移动：0）
     * @return  
     * Folder 
     * @exception
     */
    private TeamworkFolder getCopyAndMoveFolder(TeamworkFolder folder,
            TeamworkFolder toFolder, Employee employee, Integer type,
            Integer teamworkId) {
        if (toFolder == null) {
            toFolder = new TeamworkFolder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //复制或者移动文件夹  1.判断是否存在同一个目录下
        TeamworkFolderQuery folderQuery = new TeamworkFolderQuery();
        folderQuery.setParentId(toFolder.getId());
        folderQuery.setTeamworkId(teamworkId);
        folderQuery.setQueryName(folder.getFolderName());
        setTeamworkFolderName(folder, folderQuery);
        folder.setParentId(toFolder.getId());
        //重新生成code
        folder.setFolderCode(toFolder.getFolderCode());
        //查询当前文件夹的子文件夹
        TeamworkFolderQuery query = new TeamworkFolderQuery();
        query.setParentId(toFolder.getId());
        query.setTeamworkId(teamworkId);
        List<String> folderCodes = teamworkFolderDao.selectByParentId(query);
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                toFolder.getFolderCode());
        //若是复制操作，需重定义属性
        if (type == OperateType.Copy.getType()) {
            //重定义文件夹所属人
            folder.setEmployeeId(employee.getId());
        }
        folder.setFolderCode(folderCode);
        folder.setCreateAdmin(employee.getEmployeeName());
        folder.setCreateTime(new Date());
        return folder;
    }

    /***
     * 移动文件夹修改文件夹下的folderCode
     * @param folderId  原来的文件夹ID
     * @param toFolder  新文件夹对象
     * @param employee  操作的员工对象 
     * void 
     * @exception
     */
    private void moveFolderCode(int folderId, TeamworkFolder toFolder,
            Employee employee, Integer teamworkId) {
        //获取所有文件夹
        TeamworkFolderQuery zFolderQuery = new TeamworkFolderQuery();
        zFolderQuery.setParentId(folderId);
        List<TeamworkFolder> folders = teamworkFolderDao.selectByQuery(zFolderQuery);
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder subFolder : folders) {
                int subFolderId = subFolder.getId();
                subFolder.setParentId(toFolder.getId());
                //查询当前文件夹的子文件夹
                TeamworkFolderQuery query = new TeamworkFolderQuery();
                query.setParentId(subFolder.getParentId());
                query.setTeamworkId(teamworkId);
                List<String> folderCodes = teamworkFolderDao.selectByParentId(query);
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                        toFolder.getFolderCode());
                subFolder.setFolderCode(folderCode);
                teamworkFolderDao.update(subFolder);
                moveFolderCode(subFolderId, subFolder, employee, teamworkId);
            }
        }
        //获取所有文件
        TeamworkFileQuery zFileQuery = new TeamworkFileQuery();
        zFileQuery.setFolderId(folderId);
        List<TeamworkFile> files = teamworkFileDao.selectByQuery(zFileQuery);
        if (files != null && files.size() > 0) {
            for (TeamworkFile file : files) {
                file.setUpdateTime(new Date());
                file.setUpdateAdmin(employee.getEmployeeName());
                file.setFolderId(toFolder.getId());
                file.setFolderCode(toFolder.getFolderCode());
                teamworkFileDao.update(file);
            }
        }
    }

    @Override
    @Transactional
    public void moveFile(TeamworkFile file, int folderId, Employee employee,
            Integer teamworkId) {
        file = teamworkFileDao.selectById(file.getId());
        if (folderId != 0) {
            TeamworkFolder toFolder = teamworkFolderDao.selectById(folderId);
            file.setFolderId(toFolder.getId());
            file.setFolderCode(toFolder.getFolderCode());
        }
        else {
            file.setFolderId(0);
            file.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //查询当前文件夹的子文件夹
        TeamworkFileQuery fileQuery = new TeamworkFileQuery();
        fileQuery.setFolderId(folderId);
        fileQuery.setTeamworkId(teamworkId);
        fileQuery.setQueryName(file.getFileName());
        setTeamworkFileName(file, fileQuery);
        teamworkFileDao.update(file);
    }

    @Override
    @Transactional
    public int exportFolderOrFile(TeamworkFolder folder, int folderId,
            Employee employee) {
        List<TeamworkFolder> folders = folder.getTeamworkFolders();
        List<TeamworkFile> files = folder.getTeamworkFiles();
        //判断协作空间是否已满
        List<Long> fileSizeList = new ArrayList<Long>();
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                TeamworkFile fileTmp = teamworkFileDao.selectById(files.get(i).getId());
                fileSizeList.add(fileTmp.getFileSize());
            }
        }
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder subFolder : folders) {
                subFolder = teamworkFolderDao.selectById(subFolder.getId());
                exportFolder(subFolder, folderId, employee,
                        folder.getTeamworkId(), fileSizeList);
            }
        }
        //移动文件
        if (files != null && files.size() > 0) {
            for (TeamworkFile file : files) {
                exportFile(file, folderId, employee);
            }
        }
        //判断个人空间是否已满
        fileManager.checkPersonalSize(fileSizeList, employee);
        
        //协作日志数据
        List<Integer> messageFileIds = new ArrayList<Integer>();
        List<Integer> messageFolderIds = new ArrayList<Integer>();
        teamworkMessageManager.addSelectMessageFileOrFolderIds(messageFileIds, messageFolderIds, folder);
        //协作日志
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessages(MsgType.EXPORT.getType(),
                employee.getId(), employee.getEmployeeName(),
                folder.getTeamworkId(), messageFileIds, messageFolderIds);
        return teamworkMessageId;
    }

    @Override
    @Transactional
    public void exportFile(TeamworkFile teamworkFile, int folderId,
            Employee employee) {
        teamworkFile = teamworkFileDao.selectById(teamworkFile.getId());
        File file = convertTeamworkFileToFile(teamworkFile, employee);
        if (folderId != 0) {
            Folder toFolder = folderDao.selectById(folderId);
            file.setFolderId(toFolder.getId());
            file.setFolderCode(toFolder.getFolderCode());
        }
        else {
            file.setFolderId(0);
            file.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //查询当前文件夹的子文件夹
        FileQuery fileQuery = new FileQuery();
        fileQuery.setEmployeeId(employee.getId());
        fileQuery.setFolderId(folderId);
        fileQuery.setQueryName(teamworkFile.getFileName());
        setFileName(file, fileQuery);
        fileDao.insert(file);
        //新上传文件:文件索引表数据添加
        fileIndexManager.insertFileIndex(file, FileIndexOperType.SAVE.getType());
    }

    @Override
    @Transactional
    public void exportFolder(TeamworkFolder teamworkFolder, int folderId,
            Employee employee, Integer teamworkId, List<Long> fileSizeList) {
        Folder toFolder = null;
        if (folderId == 0) {
            toFolder = new Folder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        else {
            toFolder = folderDao.selectById(folderId);
            //设置所属人的姓名(确保移动中不会改变创建人)
            employee.setEmployeeName(toFolder.getCreateAdmin());
        }
        Folder folder = convertTeamworkFolderToFolder(teamworkFolder, employee);
        Folder oldFolder = getExportFolder(folder, toFolder, employee);
        folderDao.insert(oldFolder);
        //导出文件夹及文件夹下层所有文件与文件夹
        exportFolderCode(teamworkFolder.getId(), oldFolder, employee,
                teamworkId, fileSizeList);
    }

    /****
     *  导出的公共方法
     * @param folder     移动或者操作的文件夹ID
     * @param toFolder   移动到的文件夹ID
     * @param employee   操作的员工
     * @return  
     * Folder 
     * @exception
     */
    private Folder getExportFolder(Folder folder, Folder toFolder,
            Employee employee) {
        if (toFolder == null) {
            toFolder = new Folder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //判断是否存在同一个目录下同名目录
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(toFolder.getId());
        folderQuery.setQueryName(folder.getFolderName());
        folderQuery.setEmployeeId(employee.getId());
        setFolderName(folder, folderQuery);
        folder.setParentId(toFolder.getId());
        //重新生成code
        folder.setFolderCode(toFolder.getFolderCode());
        //查询当前文件夹的子文件夹
        FolderQuery query = new FolderQuery();
        query.setParentId(toFolder.getId());
        query.setEmployeeId(employee.getId());
        List<String> folderCodes = folderDao.selectByParentId(query);
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                toFolder.getFolderCode());
        folder.setFolderCode(folderCode);
        folder.setCreateAdmin(employee.getEmployeeName());
        folder.setEmployeeId(employee.getId());
        folder.setCreateTime(new Date());
        return folder;
    }

    /***
     * 导出文件夹及文件夹下层所有文件与文件夹
     * @param folderId    原来的文件夹ID
     * @param toFolder    新文件夹对象
     * @param employee    操作的员工对象 
     * @param teamworkId  协作ID
     * void 
     * @exception
     */
    private void exportFolderCode(int folderId, Folder toFolder,
            Employee employee, Integer teamworkId, List<Long> fileSizeList) {
        //获取所有文件夹
        TeamworkFolderQuery zFolderQuery = new TeamworkFolderQuery();
        zFolderQuery.setParentId(folderId);
        List<TeamworkFolder> teamworkFolders = teamworkFolderDao.selectByQuery(zFolderQuery);
        if (teamworkFolders != null && teamworkFolders.size() > 0) {
            for (TeamworkFolder subFolder : teamworkFolders) {
                int subFolderId = subFolder.getId();
                subFolder.setParentId(toFolder.getId());
                //查询当前文件夹的子文件夹
                FolderQuery query = new FolderQuery();
                query.setParentId(toFolder.getId());
                query.setEmployeeId(employee.getId());
                List<String> folderCodes = folderDao.selectByParentId(query);
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                        toFolder.getFolderCode());
                Folder folder = convertTeamworkFolderToFolder(subFolder,
                        employee);
                folder.setFolderCode(folderCode);
                folder.setParentId(toFolder.getId());
                folderDao.insert(folder);
                exportFolderCode(subFolderId, folder, employee, teamworkId, fileSizeList);
            }
        }
        //获取所有文件
        TeamworkFileQuery zFileQuery = new TeamworkFileQuery();
        zFileQuery.setFolderId(folderId);
        List<TeamworkFile> teamworkFiles = teamworkFileDao.selectByQuery(zFileQuery);
        if (teamworkFiles != null && teamworkFiles.size() > 0) {
            for (TeamworkFile teamworkFile : teamworkFiles) {
                //将文件夹下的所有文件加入到 监控总空间大小 的集合中
                fileSizeList.add(teamworkFile.getFileSize());
                
                File file = convertTeamworkFileToFile(teamworkFile, employee);
                file.setUpdateTime(new Date());
                file.setUpdateAdmin(employee.getEmployeeName());
                file.setFolderId(toFolder.getId());
                file.setFolderCode(toFolder.getFolderCode());
                fileDao.insert(file);
                //新上传文件:文件索引表数据添加
                fileIndexManager.insertFileIndex(file,
                        FileIndexOperType.SAVE.getType());
            }
        }
    }

    /**
     * File文件 转换 TeamworkFile协作文件
     * @param file
     * @return  
     * TeamworkFile 
     * @exception
     */
    private TeamworkFile convertFileToTeamworkFile(File file) {
        TeamworkFile teamworkFile = new TeamworkFile();
        teamworkFile.setFileName(file.getFileName());
        teamworkFile.setFileType(file.getFileType());
        teamworkFile.setEmployeeId(file.getEmployeeId());
        teamworkFile.setFileSize(file.getFileSize());
        teamworkFile.setFilePath(file.getFilePath());
        return teamworkFile;
    }

    /**
     * Folder文件夹 转换 TeamworkFolder协作文件夹
     * @param folder
     * @param employee
     * @return  
     * TeamworkFolder 
     * @exception
     */
    private TeamworkFolder convertFolderToTeamworkFolder(Folder folder,
            Employee employee) {
        TeamworkFolder teamworkFolder = new TeamworkFolder();
        teamworkFolder.setFolderName(folder.getFolderName());
        teamworkFolder.setFolderType(folder.getFolderType());
        teamworkFolder.setValidStatus(folder.getValidStatus());
        teamworkFolder.setEmployeeId(employee.getId());
        return teamworkFolder;
    }

    /**
     * TeamworkFile协作文件 转换 File文件
     * @param teamworkFile
     * @param employee
     * @return  
     * File 
     * @exception
     */
    private File convertTeamworkFileToFile(TeamworkFile teamworkFile,
            Employee employee) {
        File file = new File();
        file.setFileName(teamworkFile.getFileName());
        file.setFileType(teamworkFile.getFileType());
        file.setEmployeeId(employee.getId());
        file.setFileSize(teamworkFile.getFileSize());
        file.setFilePath(teamworkFile.getFilePath());
        file.setValidStatus(teamworkFile.getValidStatus());
        file.setFileVersion(0);
        file.setCreateTime(new Date());
        file.setCreateAdmin(employee.getEmployeeName());
        file.setUpdateAdmin(employee.getEmployeeName());
        file.setEncryptStatus(teamworkFile.getEncryptStatus());
        file.setEncryptKey(teamworkFile.getEncryptKey());
        file.setServerCode(teamworkFile.getServerCode());
        return file;
    }

    /**
     * TeamworkFolder协作文件夹 转换 Folder文件夹
     * @param teamworkFolder
     * @param employee
     * @return  
     * Folder 
     * @exception
     */
    private Folder convertTeamworkFolderToFolder(TeamworkFolder teamworkFolder,
            Employee employee) {
        Folder folder = new Folder();
        folder.setFolderName(teamworkFolder.getFolderName());
        folder.setFolderType(FolderType.PERSONAL.getType());
        folder.setValidStatus(teamworkFolder.getValidStatus());
        folder.setEmployeeId(employee.getId());
        folder.setCreateTime(new Date());
        folder.setCreateAdmin(employee.getEmployeeName());
        folder.setUpdateAdmin(employee.getEmployeeName());
        return folder;
    }
}
