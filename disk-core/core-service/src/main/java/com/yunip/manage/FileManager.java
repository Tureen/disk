/*
 * 描述：〈文件公共管理数据模块〉
 * 创建人：can.du
 * 创建时间：2016-5-12
 */
package com.yunip.manage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.EncryptStatus;
import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.enums.disk.FileType;
import com.yunip.enums.disk.FolderType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.disk.ShareStatus;
import com.yunip.enums.disk.ShareType;
import com.yunip.enums.disk.UploadType;
import com.yunip.enums.disk.ValidStatus;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.enums.log.ActionType;
import com.yunip.enums.teamwork.MsgType;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IAuthorityShareDao;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileDeleteDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.disk.IFolderDeleteDao;
import com.yunip.mapper.disk.ISignDao;
import com.yunip.mapper.disk.ITakeCodeDao;
import com.yunip.mapper.teamwork.ITeamworkDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.FolderDelete;
import com.yunip.model.disk.Sign;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.query.FileDeleteQuery;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.query.FileVersionQuery;
import com.yunip.model.disk.query.FolderDeleteQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.query.TakeCodeQuery;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.model.teamwork.query.TeamworkFileQuery;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;
import com.yunip.model.teamwork.query.TeamworkFolderQuery;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.serial.SerialCodeUtil;
import com.yunip.utils.util.FileSizeUtil;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 文件公共管理数据模块
 */
@Component("fileManager")
public class FileManager {

    @Resource(name = "iFileDao")
    private IFileDao        fileDao;

    @Resource(name = "iFolderDao")
    private IFolderDao      folderDao;
    
    @Resource(name = "iFileDeleteDao")
    private IFileDeleteDao fileDeleteDao;
    
    @Resource(name = "iFolderDeleteDao")
    private IFolderDeleteDao folderDeleteDao;

    @Resource(name = "iFileVersionDao")
    private IFileVersionDao fileVersionDao;

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao    employeeDao;
    
    @Resource(name = "iAuthorityShareDao")
    private IAuthorityShareDao authorityShareDao;
    
    @Resource(name = "iSignDao")
    private ISignDao signDao;
    
    @Resource(name = "iTakeCodeDao")
    private ITakeCodeDao takeCodeDao;
    
    @Resource(name = "logManager")
    private LogManager logManager;
    
    @Resource(name = "fileIndexManager")
    private FileIndexManager fileIndexManager;
    
    @Resource(name = "iTeamworkDao")
    private ITeamworkDao teamworkDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao teamworkFileDao;
    
    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao teamworkFolderDao;
    
    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao teamworkFileVersionDao;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    
    /***
     * 保存文件操作(调用需要配置权限)
     * @param folderId     文件夹ID
     * @param files        文件对象集合
     * @param uploadType   上传类型
     * void 
     * @exception
     */
    public void saveFiles(int folderId, List<File> files,
            UploadType uploadType, Integer employeeId) {
        if (files != null && files.size() > 0) {
            for (File file : files) {
                saveFile(file, uploadType, employeeId, null, null);
            }
        }
    }

    /***
     * 单个文件保存
     * @param file        文件一定需要包含文件夹ID
     * @param uploadType  上传类型
     * @param employeeId  员工id
     * @param fileVersionNum  限制上传的版本数量
     * @param ip ip地址
     * void 
     * @exception
     */
    @Transactional
    public void saveFile(File file, UploadType uploadType, Integer employeeId ,Integer fileVersionNum, String ip) {
        //TODO 临时处理 start
        FolderQuery query = new FolderQuery();
        query.setFolderId(file.getFolderId());
        Folder folderTmp = null;
        List<Folder> folders = folderDao.selectByQuery(query);
        if(folders != null && folders.size() > 0){
            folderTmp = folders.get(0);
            if(folderTmp.getEmployeeId() != employeeId){
                file.setEmployeeId(folderTmp.getEmployeeId());
            }
        }
        //TODO 临时处理 end
        String folderCode = SystemContant.FIRST_FOLDER_ID;
        if (file.getFolderId() != 0) {
            Folder folder = folderDao.selectById(file.getFolderId());
            folderCode = folder.getFolderCode();
        }
        Employee employee = employeeDao.selectById(file.getEmployeeId());
        
        Employee operateLogEmployee = employee;//上传操作人
        
        file.setFolderCode(folderCode);
        //第一步查询该文件夹下是否存在同名的文件
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFolderId(file.getFolderId());
        fileQuery.setEmployeeId(file.getEmployeeId());
        fileQuery.setFileName(file.getFileName());
        List<File> list = fileDao.selectByQuery(fileQuery);
        if (list != null && list.size() > 0) {
            //存在取第一个 
            File oldFile = list.get(0);
            if (uploadType.equals(UploadType.RENAME)) {
                //进行重命名
                FileQuery newfileQuery = new FileQuery();
                newfileQuery.setFolderId(file.getFolderId());
                newfileQuery.setEmployeeId(file.getEmployeeId());
                setFileName(oldFile, newfileQuery);
            } else {
                //升级版本
                if (uploadType.equals(UploadType.UPVSERSION)) {
                    //根据限制版本个数,决定是否删除旧版本
                    delFileVersion(oldFile,fileVersionNum);
                    addFileVersion(oldFile);
                    //当前版本文件移除:文件索引表数据添加
                    fileIndexManager.insertFileIndex(oldFile, FileIndexOperType.DEL.getType());
                }
                oldFile.setFileSize(file.getFileSize());
                oldFile.setFilePath(file.getFilePath());
                oldFile.setUpdateTime(new Date());
                oldFile.setUpdateAdmin(oldFile.getCreateAdmin());
                setEncryptStatus(oldFile);
                oldFile.setServerCode(SystemContant.SERVER_CODE);
                fileDao.update(oldFile);
                file.setId(oldFile.getId());
                //新上传文件:文件索引表数据添加
                fileIndexManager.insertFileIndex(oldFile, FileIndexOperType.SAVE.getType());
            }
        } else {
            //不存在 新增
            FileType fileType = FileType.getFileType(file.getFileName());
            file.setValidStatus(ValidStatus.NOMAL.getStatus());
            file.setFileType(fileType.getType());
            file.setFileVersion(0);
            //传入的真实employeeId不为null，将其作为创建人参照
            if (employeeId != SystemContant.MANAGER_EMPLOYEE_ID) {
                Employee realEmployee = employeeDao.selectById(employeeId);
                file.setCreateAdmin(realEmployee.getEmployeeName());
                file.setUpdateAdmin(realEmployee.getEmployeeName());
                operateLogEmployee = realEmployee;
            } else {
                file.setCreateAdmin(employee.getEmployeeName());
                file.setUpdateAdmin(employee.getEmployeeName());
            }
            file.setCreateTime(new Date());
            setEncryptStatus(file);
            file.setServerCode(SystemContant.SERVER_CODE);
            fileDao.insert(file);
            //新上传文件:文件索引表数据添加
            fileIndexManager.insertFileIndex(file, FileIndexOperType.SAVE.getType());
        }
        //添加用户日志
        operateLogEmployee.setLoginIp(ip);
        if(folderTmp != null){
            logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FOLDER, file.getFileName(), ActionType.UPLOAD, operateLogEmployee);
        }else{
            logManager.insertEmpLoyeeLog(0, OpenType.FOLDER, file.getFileName(), ActionType.UPLOAD, operateLogEmployee);
        }
    }
    
    /***
     * 单个协作文件保存
     * @param file        文件一定需要包含文件夹ID
     * @param uploadType  上传类型
     * @param employeeId  员工id
     * @param fileVersionNum  限制上传的版本数量
     * @param ip ip地址
     * void 
     * @exception
     */
    @Transactional
    public int saveTeamworkFile(TeamworkFile file, UploadType uploadType, Integer employeeId , Integer fileVersionNum, String ip, Integer zFileId, Integer realUploadType) {
        String folderCode = SystemContant.FIRST_FOLDER_ID;
        if (file.getFolderId() != 0) {
            TeamworkFolder folder = teamworkFolderDao.selectById(file.getFolderId());
            folderCode = folder.getFolderCode();
        }
        file.setFolderCode(folderCode);
        //查询该文件夹下是否存在同名的文件
        TeamworkFileQuery fileQuery = new TeamworkFileQuery();
        fileQuery.setFolderId(file.getFolderId());
        fileQuery.setTeamworkId(file.getTeamworkId());
        fileQuery.setFileName(file.getFileName());
        List<TeamworkFile> list = teamworkFileDao.selectByQuery(fileQuery);
        if (list != null && list.size() > 0) {
            //取第一个
            TeamworkFile oldFile = list.get(0);
            if (uploadType.equals(UploadType.RENAME)) {
                //进行重命名
                TeamworkFileQuery newFileQuery = new TeamworkFileQuery();
                newFileQuery.setFolderId(file.getFolderId());
                newFileQuery.setTeamworkId(file.getTeamworkId());
                setTeamworkFileName(oldFile, newFileQuery);
            } else {
                //升级版本
                if (uploadType.equals(UploadType.UPVSERSION)) {
                    //TODO 根据限制版本个数，决定是否删除旧版本
                    //添加旧文件到版本表中
                    addTeamworkFileVersion(oldFile);
                }
                oldFile.setFileSize(file.getFileSize());
                oldFile.setFilePath(file.getFilePath());
                oldFile.setUpdateAdmin(oldFile.getCreateAdmin());
                oldFile.setUpdateTime(new Date());
                setEncryptStatus(oldFile);
                oldFile.setServerCode(SystemContant.SERVER_CODE);
                teamworkFileDao.update(oldFile);
                file.setId(oldFile.getId());
                file.setUploadType(com.yunip.enums.teamwork.UploadType.OVER_UPVSERSION.getType());
            }
        } else {
            //不存在则新增
            FileType fileType = FileType.getFileType(file.getFileName());
            file.setValidStatus(ValidStatus.NOMAL.getStatus());
            file.setFileType(fileType.getType());
            file.setFileVersion(0);
            setEncryptStatus(file);
            file.setServerCode(SystemContant.SERVER_CODE);
            teamworkFileDao.insert(file);
            file.setUploadType(com.yunip.enums.teamwork.UploadType.INSERT.getType());
        }
        Employee employee = employeeDao.selectById(employeeId);
        Integer zid = null;
        if(zFileId != null){
            TeamworkMessage teamworkMessage = new TeamworkMessage();
            teamworkMessage.setFileId(zFileId);
            teamworkMessage.setMsgType(MsgType.UPLOAD.getType());
            List<TeamworkMessage> teamworkMessages = teamworkMessageManager.getTeamworkMessageByFileId(teamworkMessage);
            if(teamworkMessages != null && teamworkMessages.size() > 0){
                zid = teamworkMessages.get(0).getZid();
            }
        }
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessage(MsgType.UPLOAD.getType(), employeeId, employee.getEmployeeName(), file.getTeamworkId(), null, file.getId(), null, zid);
        return teamworkMessageId;
    }
    
    /**
     * 根据filePath查询是否重复引用，用来判断是否删除该路径
     * @param filePath 文件路径
     */
    public boolean isOnlyFilePath(String filePath) {
        //查询 看当前文件路径是否存在多个引用
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFilePath(filePath);
        List<File> tpfiles = fileDao.selectByQuery(fileQuery);
        if (tpfiles != null && tpfiles.size() <= 1) {
            return true;
        }
        return false;
    }
    
    /**
     * 版本文件删除
     * @param file
     * @param num  允许保留的版本数
     * void 
     * @exception
     */
    public void delFileVersion(File file,Integer num){
        //查询文件总共的版本数
        FileVersionQuery query = new FileVersionQuery();
        query.setFileId(file.getId());
        int count = fileVersionDao.selectCountByQuery(query);
        int index = count - num;
        //对版本数据进行删除(多条)
        if(index>=0){
            for(int i=0;i<=index;i++){
                FileVersion fileVersion = fileVersionDao.selectNewByFileId(file.getId());
                fileVersionDao.delById(fileVersion.getId());
            }
        }
    }

    /***
     * 添加文件版本
     * @param file  
     * void 
     * @exception
     */
    private void addFileVersion(File file){
        FileVersionQuery query = new FileVersionQuery();
        query.setFileId(file.getId());
        query.setPageSize(1);
        query.setDesc(true);
        List<FileVersion> fileVersions = fileVersionDao.selectByQuery(query);
        Integer version = SystemContant.FIRST_VERSION;
        Integer newversion = SystemContant.FIRST_VERSION+1;
        if(!file.getFileVersion().equals(0)){
            version = file.getFileVersion();
        }
        //查询最大版本  创建版本记录
        FileVersion fileVersion = new FileVersion();
        fileVersion.setFileVersion(version);
        fileVersion.setFileName(file.getFileName());
        fileVersion.setFilePath(file.getFilePath());
        fileVersion.setFileSize(file.getFileSize());
        fileVersion.setFileType(file.getFileType());
        fileVersion.setCreateTime(new Date());
        fileVersion.setFileId(file.getId());
        fileVersion.setCreateAdmin(file.getCreateAdmin());
        fileVersion.setCreateTime(new Date());
        fileVersion.setUpdateAdmin(file.getUpdateAdmin());
        fileVersion.setUpdateTime(new Date());
        fileVersion.setValidStatus(ValidStatus.NOMAL.getStatus());
        fileVersion.setFolderCode(file.getFolderCode());
        fileVersion.setEmployeeId(file.getEmployeeId());
        fileVersion.setEncryptStatus(file.getEncryptStatus());
        fileVersion.setEncryptKey(file.getEncryptKey());
        fileVersion.setServerCode(file.getServerCode());
        fileVersionDao.insert(fileVersion);
        //如果当前文件版本比历史版本大1,说明最新,如果不是,说明引用了历史版本
        if(fileVersions.size()==0){
            file.setFileVersion(newversion);
        }else if(file.getFileVersion().equals(fileVersions.get(0).getFileVersion() + 1)){
            file.setFileVersion(fileVersion.getFileVersion() + 1);
        }else{
            file.setFileVersion(fileVersions.get(0).getFileVersion() + 1);
        }
    }
    
    /***
     * 添加协作文件版本
     * @param file  
     * void 
     * @exception
     */
    private void addTeamworkFileVersion(TeamworkFile file){
        TeamworkFileVersionQuery query = new TeamworkFileVersionQuery();
        query.setFileId(file.getId());
        query.setPageSize(1);
        query.setDesc(true);
        List<TeamworkFileVersion> fileVersions = teamworkFileVersionDao.selectByQuery(query);
        Integer version = SystemContant.FIRST_VERSION;
        Integer newversion = SystemContant.FIRST_VERSION + 1;
        if (!file.getFileVersion().equals(0)) {
            version = file.getFileVersion();
        }
        //查询最大版本，创建版本记录
        TeamworkFileVersion fileVersion = new TeamworkFileVersion();
        fileVersion.setTeamworkId(file.getTeamworkId());
        fileVersion.setFileVersion(version);
        fileVersion.setFileName(file.getFileName());
        fileVersion.setFilePath(file.getFilePath());
        fileVersion.setFileSize(file.getFileSize());
        fileVersion.setFileType(file.getFileType());
        fileVersion.setCreateTime(new Date());
        fileVersion.setFileId(file.getId());
        fileVersion.setCreateAdmin(file.getCreateAdmin());
        fileVersion.setCreateTime(new Date());
        fileVersion.setUpdateAdmin(file.getUpdateAdmin());
        fileVersion.setUpdateTime(new Date());
        fileVersion.setValidStatus(ValidStatus.NOMAL.getStatus());
        fileVersion.setFolderCode(file.getFolderCode());
        fileVersion.setEmployeeId(file.getEmployeeId());
        fileVersion.setEncryptStatus(file.getEncryptStatus());
        fileVersion.setEncryptKey(file.getEncryptKey());
        fileVersion.setServerCode(file.getServerCode());
        teamworkFileVersionDao.insert(fileVersion);
        //如果当前文件版本比历史版本大1,说明最新,如果不是,说明引用了历史版本
        if (fileVersions.size() == 0) {
            file.setFileVersion(newversion);
        } else if (file.getFileVersion().equals(fileVersions.get(0).getFileVersion() + 1)){
            file.setFileVersion(fileVersion.getFileVersion() + 1);
        } else {
            file.setFileVersion(fileVersions.get(0).getFileVersion() + 1);
        }
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
    
    /***
     * 设置同名的文件名称
     * @param folder  
     * void 
     * @exception
     */
    private void setTeamworkFileName(TeamworkFile file, TeamworkFileQuery fileQuery) {
        String queryName = StringUtil.getFirstName(fileQuery.getQueryName());
        fileQuery.setQueryName(queryName);
        List<TeamworkFile> files = teamworkFileDao.selectByQuery(fileQuery);
        //存在同名的文件
        List<String> oldFileNames = new ArrayList<String>();
        if (files != null && files.size() > 0) {
            for (TeamworkFile tmFile : files) {
                oldFileNames.add(tmFile.getFileName());
            }
        }
        String fileName = SerialCodeUtil.getFileName(file.getFileName(),
                oldFileNames);
        file.setFileName(fileName);
    }

    /**
     * 根据文件ID查询所有的文件信息
     * @param ids 文件ID数组
     */
    public List<File> getFileByIds(String... ids) {
        List<File> list = new ArrayList<File>();
        for (String id : ids) {
            File file = fileDao.selectById(id);
            if (file != null) {
                list.add(file);
            }
        }
        return list;
    }
    
    /**
     * 根据文件回收表ID查询所有的文件回收信息
     * @param ids 文件ID数组
     */
    public List<FileDelete> getFileDeleteByIds(String... ids) {
        List<FileDelete> list = new ArrayList<FileDelete>();
        for (String id : ids) {
            FileDelete fileDelete = fileDeleteDao.selectById(id);
            if (fileDelete != null) {
                list.add(fileDelete);
            }
        }
        return list;
    }
    
    /**
     * 根据协作文件ID查询所有的协作文件信息
     * @param ids
     * @return  
     * List<TeamworkFile> 
     * @exception
     */
    public List<TeamworkFile> getTeamworkFileByIds(String... ids){
        List<TeamworkFile> list = new ArrayList<TeamworkFile>();
        for (String id : ids) {
            TeamworkFile teamworkFile = teamworkFileDao.selectById(id);
            if (teamworkFile != null) {
               list.add(teamworkFile); 
            }
        }
        return list;
    }
    
    /**
     * 根据文件ID查询的文件信息
     * @param fileId  文件ID 
     */
    public File getFileByFileId(String fileId) {
        return fileDao.selectById(fileId);
    }
    
    /**
     * 根据协作文件ID查询的文件信息
     * @param fileId  协作文件ID 
     */
    public TeamworkFile getTeakworkFileByFileId(String fileId) {
        return teamworkFileDao.selectById(fileId);
    }

    /**
     * 根据目录ID查询目录及目录下的所有目录和文件信息
     * @param folderIds 目录ID数组
     */
    public List<Folder> getFolderByIds(String... folderIds) {
        List<Folder> list = new ArrayList<Folder>();
        for (String folderId : folderIds) {
            Folder folder = folderDao.selectById(folderId);
            //查询文件夹列表
            FolderQuery query = new FolderQuery();
            query.setFolderCode(folder.getFolderCode());
            query.setEmployeeId(folder.getEmployeeId());
            List<Folder> folders = folderDao.selectByQuery(query);
            FileQuery fileQuery = new FileQuery();
            fileQuery.setFolderCode(folder.getFolderCode());
            fileQuery.setEmployeeId(folder.getEmployeeId());
            List<File> files = fileDao.selectByQuery(fileQuery);
            if (folders != null && folders.size() > 0) {
                //进行配对组合
                Map<Integer, List<File>> dataMap = new HashMap<Integer, List<File>>();
                if (files != null && files.size() > 0) {
                    for (File file : files) {
                        List<File> zfiles = null;
                        if (dataMap.containsKey(file.getFolderId())) {
                            zfiles = dataMap.get(file.getFolderId());
                            zfiles.add(file);
                        }
                        else {
                            zfiles = new ArrayList<File>();
                            zfiles.add(file);
                        }
                        dataMap.put(file.getFolderId(), zfiles);
                    }
                }
                //设置文件夹下的文件
                for (Folder zfolder : folders) {
                    List<File> zsfiles = dataMap.get(zfolder.getId());
                    zfolder.setFiles(zsfiles);
                }
                List<File> folderFiles = dataMap.get(folder.getId());
                folder.setFiles(folderFiles);
                //开始构建文件夹树
                setTreeFolder(folder, folders, dataMap);
            }
            else {
                folder.setFolders(folders);
                folder.setFiles(files);
            }
            list.add(folder);
        }
        return list;
    }

    /***
     * 递归构建文件夹
     * @param folder 父目录
     * @param folders 父目录下子目录
     * @param dataMap 文件集合MAP
     * void 
     * @exception
     */
    private void setTreeFolder(Folder folder, List<Folder> folders,
            Map<Integer, List<File>> dataMap) {
        List<Folder> zsfolders = new ArrayList<Folder>();
        for (Folder zfolder : folders) {
            if (zfolder.getParentId().equals(folder.getId())) {
                //TODO
//                List<File> files = dataMap.get(zfolder.getId());
//                zfolder.setFiles(files);
                zsfolders.add(zfolder);
            }
        }
        if (zsfolders.size() > 0) {
            for (Folder zfolder : zsfolders) {
                setTreeFolder(zfolder, folders, dataMap);
            }
        }
        folder.setFolders(zsfolders);
    }
    
    /**
     * 根据回收目录ID查询目录及目录下的所有回收目录和回收文件信息
     * @param folderDeleteIds 回收目录ID数组
     */
    public List<FolderDelete> getFolderDeleteByIds(String... folderDeleteIds) {
        List<FolderDelete> list = new ArrayList<FolderDelete>();
        for (String folderDeleteId : folderDeleteIds) {
            FolderDelete folderDelete = folderDeleteDao.selectById(folderDeleteId);
            //查询文件夹列表
            FolderDeleteQuery folderDeleteQuery = new FolderDeleteQuery();
            folderDeleteQuery.setDeleteType(folderDelete.getOldId());
            List<FolderDelete> folderDeletes = folderDeleteDao.selectAllByQuery(folderDeleteQuery);
            FileDeleteQuery fileDeleteQuery = new FileDeleteQuery();
            fileDeleteQuery.setDeleteType(folderDelete.getOldId());
            List<FileDelete> fileDeletes = fileDeleteDao.selectAllByQuery(fileDeleteQuery);
            if (folderDeletes != null && folderDeletes.size() > 0) {
                //进行配对组合
                Map<Integer, List<FileDelete>> dataMap = new HashMap<Integer, List<FileDelete>>();
                if (fileDeletes != null && fileDeletes.size() > 0) {
                    for (FileDelete fileDelete : fileDeletes) {
                        List<FileDelete> zfileDeletes = null;
                        //将dataMap做成文件夹id对应文件集合的类似树
                        if (dataMap.containsKey(fileDelete.getFolderId())) {
                            zfileDeletes = dataMap.get(fileDelete.getFolderId());
                            zfileDeletes.add(fileDelete);
                        }
                        else {
                            zfileDeletes = new ArrayList<FileDelete>();
                            zfileDeletes.add(fileDelete);
                        }
                        dataMap.put(fileDelete.getFolderId(), zfileDeletes);
                    }
                }
                //设置文件夹下的文件,将map中的文件全部加入对应的上级文件夹中
                for (FolderDelete zfolderDelete : folderDeletes) {
                    List<FileDelete> zfileDeletes = dataMap.get(zfolderDelete.getOldId());
                    zfolderDelete.setFileDeletes(zfileDeletes);
                }
                List<FileDelete> folderDeleteFiles = dataMap.get(folderDelete.getOldId());
                folderDelete.setFileDeletes(folderDeleteFiles);
                //开始构建文件夹树
                setTreeFolderDelete(folderDelete, folderDeletes, dataMap);
            }
            else {
                folderDelete.setFolderDeletes(folderDeletes);
                folderDelete.setFileDeletes(fileDeletes);
            }
            list.add(folderDelete);
        }
        return list;
    }
    
    /***
     * 递归构建文件夹
     * @param folder 父目录
     * @param folders 父目录下子目录
     * @param dataMap 文件集合MAP
     * void 
     * @exception
     */
    private void setTreeFolderDelete(FolderDelete folderDelete, List<FolderDelete> folderDeletes,
            Map<Integer, List<FileDelete>> dataMap) {
        List<FolderDelete> zsfolderDeletes = new ArrayList<FolderDelete>();
        for (FolderDelete zfolderDelete : folderDeletes) {
            if (zfolderDelete.getParentId().equals(folderDelete.getOldId())) {
//                List<FileDelete> fileDeletes = dataMap.get(zfolderDelete.getOldId());
//                zfolderDelete.setFileDeletes(fileDeletes);
                zsfolderDeletes.add(zfolderDelete);
            }
        }
        if (zsfolderDeletes.size() > 0) {
            for (FolderDelete zfolderDelete : zsfolderDeletes) {
                setTreeFolderDelete(zfolderDelete, folderDeletes, dataMap);
            }
        }
        folderDelete.setFolderDeletes(zsfolderDeletes);
    }
    
    /**
     * 根据协作目录ID查询目录及目录下的所有协作目录和协作文件信息
     * @param teamworkFolderIds 协作目录ID数组
     */
    public List<TeamworkFolder> getTeamworkFolderByIds(String... teamworkFolderIds) {
        List<TeamworkFolder> list = new ArrayList<TeamworkFolder>();
        for (String teamworkFolderId : teamworkFolderIds) {
            TeamworkFolder teamworkFolder = teamworkFolderDao.selectById(teamworkFolderId);
            //查询协作文件夹列表
            TeamworkFolderQuery teamworkFolderQuery = new TeamworkFolderQuery();
            teamworkFolderQuery.setFolderCode(teamworkFolder.getFolderCode());
            teamworkFolderQuery.setTeamworkId(teamworkFolder.getTeamworkId());
            List<TeamworkFolder> folders = teamworkFolderDao.selectByQuery(teamworkFolderQuery);
            TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
            teamworkFileQuery.setFolderCode(teamworkFolder.getFolderCode());
            teamworkFileQuery.setTeamworkId(teamworkFolder.getTeamworkId());
            List<TeamworkFile> files = teamworkFileDao.selectByQuery(teamworkFileQuery);
            
            if (folders != null && folders.size() > 0) {
                //进行配对组合
                Map<Integer, List<TeamworkFile>> dataMap = new HashMap<Integer, List<TeamworkFile>>();
                if (files != null && files.size() > 0) {
                    for (TeamworkFile file : files) {
                        List<TeamworkFile> zfiles = null;
                        if (dataMap.containsKey(file.getFolderId())) {
                            zfiles = dataMap.get(file.getFolderId());
                            zfiles.add(file);
                        }
                        else {
                            zfiles = new ArrayList<TeamworkFile>();
                            zfiles.add(file);
                        }
                        dataMap.put(file.getFolderId(), zfiles);
                    }
                }
                //设置文件夹下的文件
                for (TeamworkFolder zfolder : folders) {
                    List<TeamworkFile> zsfiles = dataMap.get(zfolder.getId());
                    zfolder.setTeamworkFiles(zsfiles);
                }
                List<TeamworkFile> folderFiles = dataMap.get(teamworkFolder.getId());
                teamworkFolder.setTeamworkFiles(folderFiles);
                //开始构建文件夹树
                setTreeTeamworkFolder(teamworkFolder, folders, dataMap);
            }
            else {
                teamworkFolder.setTeamworkFolders(folders);
                teamworkFolder.setTeamworkFiles(files);
            }
            list.add(teamworkFolder);
        }
        return list;
    }
    
    /***
     * 递归构建文件夹
     * @param folder 父目录
     * @param folders 父目录下子目录
     * @param dataMap 文件集合MAP
     * void 
     * @exception
     */
    private void setTreeTeamworkFolder(TeamworkFolder folder, List<TeamworkFolder> folders,
            Map<Integer, List<TeamworkFile>> dataMap) {
        List<TeamworkFolder> zsfolders = new ArrayList<TeamworkFolder>();
        for (TeamworkFolder zfolder : folders) {
            if (zfolder.getParentId().equals(folder.getId())) {
                //TODO
//                List<File> files = dataMap.get(zfolder.getId());
//                zfolder.setFiles(files);
                zsfolders.add(zfolder);
            }
        }
        if (zsfolders.size() > 0) {
            for (TeamworkFolder zfolder : zsfolders) {
                setTreeTeamworkFolder(zfolder, folders, dataMap);
            }
        }
        folder.setTeamworkFolders(zsfolders);
    }

    /**
     * 根据目录ID查询该目录信息和目录下一级的文件信息
     * @param folderId 目录ID
     * @param employeeId 员工ID
     */
    public Folder getFolderAndFilesByFolderId(Integer folderId,
            Integer employeeId) {
        Folder folder = new Folder();
        if (folderId != null && folderId == 0) {
            folder.setId(0);
            folder.setFolderName("个人文件");
            folder.setEmployeeId(employeeId);
        }
        else {
            //查询文件夹信息
            FolderQuery query = new FolderQuery();
            query.setFolderId(folderId);
            List<Folder> folders = folderDao.selectByQuery(query);
            if (folders != null && folders.size() > 0) {
                folder = folders.get(0);
            }
        }

        //查询文件夹的文件信息
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFolderId(folderId);
        fileQuery.setEmployeeId(employeeId);
        List<File> files = fileDao.selectByQuery(fileQuery);

        //设置文件夹下的目录
        folder.setFiles(files);
        return folder;
    }
    
    /**
     * 根据协作目录ID查询该目录信息和目录下一级的文件信息
     * @param folderId 目录ID
     * @param employeeId 员工ID
     */
    public TeamworkFolder getTeamworkFolderAndFilesByFolderId(Integer folderId,
            Integer employeeId) {
        TeamworkFolder folder = new TeamworkFolder();
        if (folderId != null && folderId == 0) {
            folder.setId(0);
            folder.setFolderName("协作文件");
        }
        else {
            //查询文件夹信息
            TeamworkFolderQuery query = new TeamworkFolderQuery();
            query.setFolderId(folderId);
            List<TeamworkFolder> folders = teamworkFolderDao.selectByQuery(query);
            if (folders != null && folders.size() > 0) {
                folder = folders.get(0);
            }
        }

        //查询文件夹的文件信息
        TeamworkFileQuery fileQuery = new TeamworkFileQuery();
        fileQuery.setFolderId(folderId);
        List<TeamworkFile> files = teamworkFileDao.selectByQuery(fileQuery);

        //设置文件夹下的目录
        folder.setTeamworkFiles(files);
        return folder;
    }
    
    /***
     * 获取用户已经使用的内存大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    public Long getUseSpace(Integer employeeId){
        //获取文件大小
        Long fileSpace = fileDao.selectSumUseSpace(employeeId);
        //文件版本大小
        Long fileVesionSpace = fileVersionDao.selectSumUseSpace(employeeId);
        return fileSpace + fileVesionSpace;
    }
    
    
    /**
     * 用户合并
     * @param firstEmployee
     * @param secondEmployee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    public int mergeEmployee(Employee firstEmployee,Employee secondEmployee){
        //创建副用户文件夹
            String parentCode = SystemContant.FIRST_FOLDER_ID;
            Folder folder = new Folder();
            folder.setEmployeeId(firstEmployee.getId());
            //设置状态为正常
            folder.setValidStatus(ValidStatus.NOMAL.getStatus());
            //设置文件夹类型
            folder.setFolderType(FolderType.PERSONAL.getType());
            //设置父文件id(最上级)
            folder.setParentId(0);
            //设置文件名
            folder.setFolderName("【"+secondEmployee.getEmployeeName()+"】(合并)");
            //存在同名的文件
            FolderQuery folderQuery = new FolderQuery();
            folderQuery.setEmployeeId(folder.getEmployeeId());
            folderQuery.setQueryName(folder.getFolderName());
            setFolderName(folder, folderQuery);
            //设置文件夹编码
            FolderQuery query = new FolderQuery();
            query.setEmployeeId(folder.getEmployeeId());
            query.setParentId(folder.getParentId());
            List<String> folderCodes = folderDao.selectByParentId(query);
            String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                    parentCode);
            folder.setFolderCode(folderCode);
            folderDao.insert(folder);
        //(文件夹合并)修改副用户最上级的文件夹的paerntId为新文件夹的id;修改副用户所有文件夹的文件夹code:去掉前两个0,前面加上00XX(新建文件夹的code);修改副用户所有文件夹拥有者
            FolderQuery folderPaerntIdQuery = new FolderQuery();
            folderPaerntIdQuery.setEmployeeId(secondEmployee.getId());
            List<Folder> folderPaerntIdList = folderDao.selectByQuery(folderPaerntIdQuery);
            if(folderPaerntIdList != null && folderPaerntIdList.size()>0){
                for(Folder fo : folderPaerntIdList){
                    //文件夹搬迁
                    if(fo.getParentId()==0){
                        fo.setParentId(folder.getId());
                    }
                    fo.setEmployeeId(firstEmployee.getId());
                    //拼接新的文件夹code
                    String newCode = fo.getFolderCode();
                    newCode = folder.getFolderCode() + newCode.substring(2);
                    fo.setFolderCode(newCode);
                    folderDao.update(fo);
                }
            }
         //(文件合并)修改副用户最上级的文件的folderId为新文件的id;修改副用户所有文件的文件code:去掉前两个0,前面加上00XX(新建文件夹的code);修改副用户所有文件拥有者
            FileQuery fileFolderIdQuery = new FileQuery();
            fileFolderIdQuery.setEmployeeId(secondEmployee.getId());
            List<File> fileFolderIdList = fileDao.selectByQuery(fileFolderIdQuery);
            if(fileFolderIdList != null && fileFolderIdList.size()>0){
                for(File fi : fileFolderIdList){
                    //文件搬迁
                    if(fi.getFolderId()==0){
                        fi.setFolderId(folder.getId());
                    }
                    fi.setEmployeeId(firstEmployee.getId());
                    //拼接新的文件夹code
                    String newCode = fi.getFolderCode();
                    newCode = folder.getFolderCode() + newCode.substring(2);
                    fi.setFolderCode(newCode);
                    fileDao.update(fi);
                }
            }
         //被指定分享权限的修改(指定分享给副用户的文件及文件夹)
            AuthorityShareQuery bShareQuery = new AuthorityShareQuery();
            bShareQuery.setShareType(ShareType.EMPLOYEE.getType());
            bShareQuery.setShareEid(""+secondEmployee.getId());
            List<AuthorityShare> bShareList = authorityShareDao.selectByQuery(bShareQuery);
            if(bShareList != null && bShareList.size()>0){
                for(AuthorityShare bshare : bShareList){
                    //判断是否与主用户的被指定共享文件重合
                    AuthorityShareQuery checkBShareQuery = new AuthorityShareQuery();
                    checkBShareQuery.setOpenType(bshare.getOpenType());
                    checkBShareQuery.setOpenId(bshare.getOpenId());
                    checkBShareQuery.setShareType(ShareType.EMPLOYEE.getType());
                    checkBShareQuery.setShareEid(""+firstEmployee.getId());
                    List<AuthorityShare> checkShareList = authorityShareDao.selectByQuery(checkBShareQuery);
                    if(checkShareList.size()>0){
                        //权限冲突,删除权限小的
                        AuthorityShare checkShare = checkShareList.get(0);
                        //若主用户的被分享权限不小于副用户的,保留主的,副的删除
                        if(checkShare.getOperAuth() <= bshare.getOperAuth()){
                            authorityShareDao.delById(bshare.getId());
                            continue;
                        }
                        //否则,删除主用户的
                        else{
                            authorityShareDao.delById(checkShare.getId());
                        }
                    }
                    //修改副用户被分享权限对象
                    bshare.setShareId(""+firstEmployee.getId());
                    authorityShareDao.update(bshare);
                }
            }
         //主动分享权限修改(修改副用户分享出去的权限对象的拥有者employeeId)
            AuthorityShareQuery shareQuery = new AuthorityShareQuery();
            shareQuery.setEmployeeId(secondEmployee.getId());
            List<AuthorityShare> shareList = authorityShareDao.selectByQuery(shareQuery);
            if(shareList != null && shareList.size()>0){
                for(AuthorityShare share : shareList){
                    share.setEmployeeId(firstEmployee.getId());
                    authorityShareDao.update(share);
                }
            }
         //修改副用户标签拥有者
            List<Sign> signList = signDao.selectByEmployeeId(secondEmployee.getId());
            if(signList != null && signList.size()>0){
                for(Sign sign : signList){
                    sign.setEmployeeId(firstEmployee.getId());
                    signDao.update(sign);
                }
            }
         //修改副用户提取码拥有者
            TakeCodeQuery takeCodeQuery = new TakeCodeQuery();
            takeCodeQuery.setEmployeeId(secondEmployee.getId());
            List<TakeCode> takeCodeList = takeCodeDao.selectByQuery(takeCodeQuery);
            if(takeCodeList != null && takeCodeList.size()>0){
                for(TakeCode takeCode : takeCodeList){
                    takeCode.setEmployeeId(firstEmployee.getId());
                    takeCodeDao.update(takeCode);
                }
            }
        return 1;
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
    
    /**
     * 文件信息修改
     * @param file  
     * void 
     * @exception
     */
    @Transactional
    public void updateFile(File file, Employee employee){
        setEncryptStatus(file);
        file.setServerCode(SystemContant.SERVER_CODE);
        fileDao.update(file);
        logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE , file.getFileName(), ActionType.ONLINEEDIT, employee);
        //在线编辑文件:文件索引表添加
        fileIndexManager.insertFileIndex(file.getId(), FileIndexOperType.UPDATE.getType());
    }
    
    /**
     * 获得用户可用空间的大小
     * @param employeeId 员工ID 
     * 返回该员工剩余的可用空间大小(单位：B) 
     */
    public Long getEmployeeUsableDiskSize(Integer employeeId){
        if(SystemContant.MANAGER_EMPLOYEE_ID.equals(employeeId)){
            return Long.MAX_VALUE;
        }
        //个人空间总大小
        Employee employee = employeeDao.selectById(employeeId);
        //设置空间大小：调用空间规则
        spaceRule(employee);
        //MB转化为B
        Long diskMaxSize = Long.valueOf(employee.getPersonalSize()) * 1024 * 1024;
        //云盘已使用空间大小
        Long fileSize = fileDao.selectSumUseSpace(employeeId);
        Long fileVersionSize = fileVersionDao.selectSumUseSpace(employeeId);
        Long diskSize = (fileSize == null ? 0L : fileSize) + (fileVersionSize == null ? 0L : fileVersionSize);
        //返回可用大小
        return diskMaxSize - diskSize;
    } 
    
    /**
     * 获得协作可用空间的大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    public Long getTeamworkUsableDiskSize(Integer teamworkId, Integer employeeId){
        if(employeeId == null){
            Teamwork teamwork = teamworkDao.selectById(teamworkId);
            if(SystemContant.MANAGER_EMPLOYEE_ID.equals(teamwork.getEmployeeId())){
                return Long.MAX_VALUE;
            }
            employeeId = teamwork.getEmployeeId();
        }
        //协作空间总大小
        Employee employee = employeeDao.selectById(employeeId);
        //设置空间大小：调用空间规则
        spaceRule(employee);
        //MB转化为B
        Long teamworkMaxSize = Long.valueOf(employee.getTeamworkSpaceSize()) * 1024 * 1024;
        //云盘已使用空间大小
        Long fileSize = teamworkFileDao.selectSumUseSpace(employeeId);
        Long fileVersionSize = teamworkFileVersionDao.selectSumUseSpace(employeeId);
        Long teamworkSize = (fileSize == null ? 0L : fileSize) + (fileVersionSize == null ? 0L : fileVersionSize);
        //返回可用大小
        return teamworkMaxSize - teamworkSize;
    }
    
    /****
     * 解压文件添加文件夹和文件
     * decompressionFolders
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    @Transactional
    public void decompressionFolders(Folder folder, Employee employee){
        //存在同名的文件夹
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(folder.getParentId());
        folderQuery.setEmployeeId(folder.getEmployeeId());
        folderQuery.setQueryName(folder.getFolderName());
        //改变生成文件夹的名称
        setFolderName(folder, folderQuery);
        //文件下级操作
        String parentCode = SystemContant.FIRST_FOLDER_ID;
        if (folder != null && folder.getParentId() != 0) {
            Folder parentFolder = folderDao.selectById(folder.getParentId());
            folder.setEmployeeId(parentFolder.getEmployeeId());
            parentCode = parentFolder.getFolderCode();
        }
        FolderQuery query = new FolderQuery();
        query.setEmployeeId(folder.getEmployeeId());
        query.setParentId(folder.getParentId());
        List<String> folderCodes = folderDao.selectByParentId(query);
        if (folderCodes != null && folderCodes.size() >= 9999) {
            throw new MyException(DiskException.NOCREATEFOLDERS);
        }
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes, parentCode);
        folder.setFolderCode(folderCode);
        folder.setValidStatus(ValidStatus.NOMAL.getStatus());
        folder.setFolderType(FolderType.PERSONAL.getType());
        folder.setCreateAdmin(employee.getEmployeeName());
        folderDao.insert(folder);
        //判断递归文件夹
        decompressionDgFolders(folder, employee);
        //添加当前目录下的文件
        insertFiles(folder, employee);
    }
    
    /***
     * 解压递归添加文件夹或者文件
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    public void decompressionDgFolders(Folder folder, Employee employee){
        if(folder.getFolders() != null && folder.getFolders().size() > 0){
            for(Folder sonFolder : folder.getFolders()){
                FolderQuery query = new FolderQuery();
                query.setEmployeeId(folder.getEmployeeId());
                query.setParentId(folder.getId());
                List<String> folderCodes = folderDao.selectByParentId(query);
                if (folderCodes != null && folderCodes.size() >= 9999) {
                    throw new MyException(DiskException.NOCREATEFOLDERS);
                }
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes, folder.getFolderCode());
                sonFolder.setFolderCode(folderCode);
                sonFolder.setValidStatus(ValidStatus.NOMAL.getStatus());
                sonFolder.setFolderType(FolderType.PERSONAL.getType());
                sonFolder.setCreateAdmin(employee.getEmployeeName());
                sonFolder.setParentId(folder.getId());
                folderDao.insert(sonFolder);
                //判断递归文件夹
                decompressionDgFolders(sonFolder, employee);
                //添加当前目录下的文件
                insertFiles(sonFolder, employee);
            }
        }
    }
    
    /****
     * 添加文件夹下的文件列表
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    public void insertFiles(Folder folder, Employee employee){
        //循环文件
        if(folder.getFiles() != null && folder.getFiles().size() > 0){
            for(File file : folder.getFiles()){
                file.setFileVersion(0);
                file.setFolderCode(folder.getFolderCode());
                file.setFolderId(folder.getId());
                FileType fileType = FileType.getFileType(file.getFileName());
                file.setFileType(fileType.getType());
                file.setCreateTime(new Date());
                setEncryptStatus(file);
                fileDao.insert(file);
            }
        }
    }
    
    /***
     * 文件夹上传的文件和文件夹相关插入
     * insertFolderAndFile
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    @Transactional
    public void insertFolderAndFile(Folder folder, Folder parentFolder, Employee employee){
        //文件下级操作
        if(parentFolder == null){
            parentFolder = folderDao.selectById(folder.getParentId());
        }
        //存在同名的文件夹
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(folder.getParentId());
        String parentFolderCode = "";
        if(parentFolder != null){
            folderQuery.setEmployeeId(parentFolder.getEmployeeId());
            parentFolderCode = parentFolder.getFolderCode();
            folder.setEmployeeId(parentFolder.getEmployeeId());
        } else {
            folderQuery.setEmployeeId(employee.getId());
            folder.setEmployeeId(employee.getId());
        }
        folderQuery.setFolderName(folder.getFolderName());
        List<Folder> folders = folderDao.selectByQuery(folderQuery);
        if(folders == null || folders.size() == 0){
            FolderQuery query = new FolderQuery();
            query.setEmployeeId(employee.getId());
            query.setParentId(folder.getParentId());
            List<String> folderCodes = folderDao.selectByParentId(query);
            if (folderCodes != null && folderCodes.size() >= 9999) {
                throw new MyException(DiskException.NOCREATEFOLDERS);
            }
            String folderCode = SerialCodeUtil.getFolderCode(folderCodes, parentFolderCode);
            folder.setFolderCode(folderCode);
            folder.setValidStatus(ValidStatus.NOMAL.getStatus());
            folder.setFolderType(FolderType.PERSONAL.getType());
            folder.setCreateAdmin(employee.getEmployeeName());
            folder.setCreateTime(new Date());
            folderDao.insert(folder);
        } else {
            folder.setFolderCode(folders.get(0).getFolderCode());
            folder.setId(folders.get(0).getId());
        }
        //添加子文件夹对象
        if(folder.getFolders() != null && folder.getFolders().size() > 0){
            for(Folder sonFolder : folder.getFolders()){
                sonFolder.setParentId(folder.getId());
                insertFolderAndFile(sonFolder, folder, employee);
            }
        }
        insertUploadFiles(folder, employee);
    }
    
    /****
     * 文件夹上传的文件相关插入(递归)
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    public void insertUploadFiles(Folder folder, Employee employee){
        //循环文件
        if(folder.getFiles() != null && folder.getFiles().size() > 0){
            for(File file : folder.getFiles()){
                //判断是否存在同名文件
                FileQuery fileQuery = new FileQuery();
                fileQuery.setEmployeeId(folder.getEmployeeId());
                fileQuery.setFolderId(folder.getId());
                fileQuery.setQueryName(file.getFileName());
                setFileName(file, fileQuery);
                file.setFolderId(folder.getId());
                file.setEmployeeId(folder.getEmployeeId());
                file.setFolderCode(folder.getFolderCode());
                file.setCreateAdmin(employee.getEmployeeName());
                file.setShareStatus(ShareStatus.NONE.getStatus());
                file.setCreateTime(new Date());
                file.setFileVersion(0);
                file.setFolderCode(folder.getFolderCode());
                FileType fileType = FileType.getFileType(file.getFileName());
                file.setFileType(fileType.getType());
                file.setValidStatus(ValidStatus.NOMAL.getStatus());
                setEncryptStatus(file);
                file.setServerCode(SystemContant.SERVER_CODE);
                fileDao.insert(file);
                //新文件上传:文件索引表添加
                fileIndexManager.insertFileIndex(file, FileIndexOperType.SAVE.getType());
            }
        }
    }
    
    /****
     * 文件夹上传的文件相关插入
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    @Transactional
    public void insertUploadFile(File file, Employee employee){
        //判断是否存在同名文件
        FileQuery fileQuery = new FileQuery();
        fileQuery.setEmployeeId(employee.getId());
        fileQuery.setFolderId(file.getFolderId());
        fileQuery.setQueryName(file.getFileName());
        setFileName(file, fileQuery);
        Folder folder = folderDao.selectById(file.getFolderId());
        file.setFolderCode(folder == null ? "" : folder.getFolderCode());
        file.setEmployeeId(employee.getId());
        file.setCreateAdmin(employee.getEmployeeName());
        file.setShareStatus(ShareStatus.NONE.getStatus());
        file.setCreateTime(new Date());
        file.setFileVersion(0);
        FileType fileType = FileType.getFileType(file.getFileName());
        file.setFileType(fileType.getType());
        file.setValidStatus(ValidStatus.NOMAL.getStatus());
        setEncryptStatus(file);
        file.setServerCode(SystemContant.SERVER_CODE);
        //修改文件拥有人为上级文件夹拥有人
        FolderQuery query = new FolderQuery();
        query.setFolderId(file.getFolderId());
        Folder folderTmp = null;
        List<Folder> folders = folderDao.selectByQuery(query);
        if(folders != null && folders.size() > 0){
            folderTmp = folders.get(0);
            if(folderTmp.getEmployeeId() != employee.getId()){
                file.setEmployeeId(folderTmp.getEmployeeId());
            }
        }
        fileDao.insert(file);
        //新文件上传:文件索引表添加
        fileIndexManager.insertFileIndex(file, FileIndexOperType.SAVE.getType());
    }
    
    /****
     * 文件夹上传的协作文件相关插入
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    @Transactional
    public int insertUploadTeamworkFile(TeamworkFile file, Employee employee, Integer zFileId){
        //判断是否存在同名文件
        TeamworkFileQuery fileQuery = new TeamworkFileQuery();
        fileQuery.setFolderId(file.getFolderId());
        fileQuery.setQueryName(file.getFileName());
        fileQuery.setTeamworkId(file.getTeamworkId());
        setTeamworkFileName(file, fileQuery);
        TeamworkFolder folder = teamworkFolderDao.selectById(file.getFolderId());
        file.setFolderCode(folder == null ? "" : folder.getFolderCode());
        file.setEmployeeId(employee.getId());
        file.setCreateAdmin(employee.getEmployeeName());
        file.setCreateTime(new Date());
        file.setFileVersion(0);
        FileType fileType = FileType.getFileType(file.getFileName());
        file.setFileType(fileType.getType());
        file.setValidStatus(ValidStatus.NOMAL.getStatus());
        setEncryptStatus(file);
        file.setServerCode(SystemContant.SERVER_CODE);
        teamworkFileDao.insert(file);
        
        Integer zid = null;
        if(zFileId != null){
            TeamworkMessage teamworkMessage = new TeamworkMessage();
            teamworkMessage.setFileId(zFileId);
            teamworkMessage.setMsgType(MsgType.UPLOAD.getType());
            List<TeamworkMessage> teamworkMessages = teamworkMessageManager.getTeamworkMessageByFileId(teamworkMessage);
            if(teamworkMessages != null && teamworkMessages.size() > 0){
                zid = teamworkMessages.get(0).getZid();
            }
        }
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessage(MsgType.UPLOAD.getType(), employee.getId(), employee.getEmployeeName(), file.getTeamworkId(), null, file.getId(), null, zid);
        return teamworkMessageId;
    }
    
    /***
     * 文件夹上传的协作文件和协作文件夹相关插入(递归)
     * insertFolderAndFile
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    @Transactional
    public int insertTeamworkFolderAndFile(TeamworkFolder folder, TeamworkFolder parentFolder, Employee employee, Integer zFileId){
        int teamworkMessageId = 0 ;
        //文件下级操作
        if(parentFolder == null){
            parentFolder = teamworkFolderDao.selectById(folder.getParentId());
        }
        //存在同名的文件夹
        TeamworkFolderQuery folderQuery = new TeamworkFolderQuery();
        folderQuery.setParentId(folder.getParentId());
        folderQuery.setTeamworkId(folder.getTeamworkId());
        folderQuery.setFolderName(folder.getFolderName());
        String parentFolderCode = "";
        if(parentFolder != null){
            parentFolderCode = parentFolder.getFolderCode();
        }
        List<TeamworkFolder> folders = teamworkFolderDao.selectByQuery(folderQuery);
        if(folders == null || folders.size() == 0){
            TeamworkFolderQuery query = new TeamworkFolderQuery();
            query.setTeamworkId(folder.getTeamworkId());
            query.setParentId(folder.getParentId());
            List<String> folderCodes = teamworkFolderDao.selectByParentId(query);
            if (folderCodes != null && folderCodes.size() >= 9999) {
                throw new MyException(DiskException.NOCREATEFOLDERS);
            }
            String folderCode = SerialCodeUtil.getFolderCode(folderCodes, parentFolderCode);
            folder.setFolderCode(folderCode);
            folder.setValidStatus(ValidStatus.NOMAL.getStatus());
            folder.setFolderType(FolderType.PERSONAL.getType());
            folder.setCreateAdmin(employee.getEmployeeName());
            folder.setEmployeeId(employee.getId());
            folder.setCreateTime(new Date());
            teamworkFolderDao.insert(folder);
        } else {
            folder.setFolderCode(folders.get(0).getFolderCode());
            folder.setId(folders.get(0).getId());
        }
        //添加子文件夹对象
        if(folder.getTeamworkFolders() != null && folder.getTeamworkFolders().size() > 0){
            for(TeamworkFolder sonFolder : folder.getTeamworkFolders()){
                sonFolder.setParentId(folder.getId());
                sonFolder.setTeamworkId(folder.getTeamworkId());
                int teamworkMessageIdTmp = insertTeamworkFolderAndFile(sonFolder, folder, employee, zFileId);
                if(teamworkMessageIdTmp != 0){
                    teamworkMessageId = teamworkMessageIdTmp;
                }
            }
        }
        int teamworkMessageIdTmp = insertUploadTeamworkFiles(folder, employee, zFileId);
        if(teamworkMessageIdTmp != 0){
            teamworkMessageId = teamworkMessageIdTmp;
        }
        return teamworkMessageId;
    }
    
    /****
     * 文件夹上传的文件相关插入
     * @param folder
     * @param employee  
     * void 
     * @exception
     */
    public int insertUploadTeamworkFiles(TeamworkFolder folder, Employee employee, Integer zFileId){
        int teamworkMessageId = 0;
        //循环文件
        if(folder.getTeamworkFiles() != null && folder.getTeamworkFiles().size() > 0){
            for(TeamworkFile file : folder.getTeamworkFiles()){
                //判断是否存在同名文件
                TeamworkFileQuery fileQuery = new TeamworkFileQuery();
                fileQuery.setTeamworkId(folder.getTeamworkId());
                fileQuery.setFolderId(folder.getId());
                fileQuery.setQueryName(file.getFileName());
                setTeamworkFileName(file, fileQuery);
                file.setFolderId(folder.getId());
                file.setEmployeeId(employee.getId());
                file.setFolderCode(folder.getFolderCode());
                file.setCreateAdmin(employee.getEmployeeName());
                file.setCreateTime(new Date());
                file.setFileVersion(0);
                file.setFolderCode(folder.getFolderCode());
                FileType fileType = FileType.getFileType(file.getFileName());
                file.setFileType(fileType.getType());
                file.setValidStatus(ValidStatus.NOMAL.getStatus());
                setEncryptStatus(file);
                file.setServerCode(SystemContant.SERVER_CODE);
                teamworkFileDao.insert(file);
                
                Integer zid = null;
                if(zFileId != null){
                    TeamworkMessage teamworkMessage = new TeamworkMessage();
                    teamworkMessage.setFileId(zFileId);
                    teamworkMessage.setMsgType(MsgType.UPLOAD.getType());
                    List<TeamworkMessage> teamworkMessages = teamworkMessageManager.getTeamworkMessageByFileId(teamworkMessage);
                    if(teamworkMessages != null && teamworkMessages.size() > 0){
                        zid = teamworkMessages.get(0).getZid();
                    }
                }
                teamworkMessageId = teamworkMessageManager.saveTeamworkMessage(MsgType.UPLOAD.getType(), employee.getId(), employee.getEmployeeName(), file.getTeamworkId(), null, file.getId(), null, zid);
            }
        }
        return teamworkMessageId;
    }
    
    /**
     * 根据目录ID查询该目录下的子目录列表和子文件列表
     * @param folderId 目录ID
     * @param employeeId 员工ID
     */
    public Folder getFoldersAndFilesParentId(Integer folderId, Integer employeeId) {
        Folder folder = new Folder();
        folder.setId(folderId); 
        
        //查询文件夹信息
        FolderQuery query = new FolderQuery();
        query.setFolderId(folderId);
        query.setEmployeeId(employeeId);
        List<Folder> folders = folderDao.selectByQuery(query);
         //设置文件夹下的目录
        folder.setFolders(folders);

        //查询文件夹的文件信息
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFolderId(folderId);
        fileQuery.setEmployeeId(employeeId);
        List<File> files = fileDao.selectByQuery(fileQuery);

        //设置文件夹下的文件
        folder.setFiles(files);
        return folder;
    }
    
    /***
     * 获取最终上传的文件夹ID
     * @param folderId   文件夹ID(当为负数则表示为上传到“来自PC的备份”)
     * @param employee   操作对象
     * @return  
     * Integer 
     * @exception
     */
    public Integer getFinalFolderId(Integer folderId, Employee employee){
        if(folderId < 0){
            //客户端使用
            FolderQuery folderQuery = new FolderQuery();
            folderQuery.setFolderName(SystemContant.INITFOLDERNAME);
            folderQuery.setEmployeeId(employee.getId());
            folderQuery.setParentId(0);
            List<Folder> folders = folderDao.selectByQuery(folderQuery);
            if(folders != null && folders.size() > 0){
                folderId = folders.get(0).getId();
            } else {
                //创建文件夹
                Folder folder = new Folder();
                FolderQuery query = new FolderQuery();
                query.setEmployeeId(employee.getId());
                query.setParentId(folder.getParentId());
                List<String> folderCodes = folderDao.selectByParentId(query);
                if (folderCodes != null && folderCodes.size() >= 9999) {
                    throw new MyException(DiskException.NOCREATEFOLDERS);
                }
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes, "");
                folder.setFolderCode(folderCode);
                folder.setValidStatus(ValidStatus.NOMAL.getStatus());
                folder.setFolderName(SystemContant.INITFOLDERNAME);
                folder.setFolderType(FolderType.PERSONAL.getType());
                folder.setCreateAdmin(employee.getEmployeeName());
                folder.setEmployeeId(employee.getId());
                folder.setCreateTime(new Date());
                folder.setParentId(0);
                folderDao.insert(folder);
                return folder.getId();
            }
        }
        return folderId;
    }
    
    /**
     * 根据FolderCode查询改用户下所有的待加密文件
     * @param folderCode
     * @param employeeId 员工ID
     * @return 返回文件的list集合
     */
    public List<File> getEmployeeFolderAllFilesByFolderCode(String folderCode, Integer employeeId) {
        FileQuery query = new FileQuery();
        query.setFolderCode(folderCode);
        query.setEmployeeId(employeeId);
        query.setEncryptStatus(EncryptStatus.WAITFORENCRYPTION.getStatus());
        return fileDao.selectByQuery(query);
    }
    
    /**
     * 设置文件是否加密状态
     * @param file 文件对象
     */
    public void setEncryptStatus(File file){
        if(isEncrypt(file)){
            file.setEncryptStatus(EncryptStatus.WAITFORENCRYPTION.getStatus());
        }else{
            file.setEncryptStatus(EncryptStatus.UNENCRYPTED.getStatus());
        }
        file.setEncryptKey("");
    }
    
    /**
     * 设置协作文件是否加密状态
     * @param file 文件对象
     */
    public void setEncryptStatus(TeamworkFile file){
        if(isEncrypt(file)){
            file.setEncryptStatus(EncryptStatus.WAITFORENCRYPTION.getStatus());
        }else{
            file.setEncryptStatus(EncryptStatus.UNENCRYPTED.getStatus());
        }
        file.setEncryptKey("");
    }
    
    /**
     * 判断文件类型及大小峰值是否允许加密
     * @param file
     * @return  true:加密   false:不加密
     */
    public boolean isEncrypt(File file){
        //是否开启文件加密
        String isOpenEncryptFile = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENENCRYPTFILE.getKey());
        if((BasicsBool.YES.getBool()).equals(isOpenEncryptFile)){
            String limitFileMaxSize = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ENCRYPTFILEMAXSIZE.getKey());
            String infiniteFileTypes = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.INFINITEENCRYPTFILETYPE.getKey());
            String fileType = FileUtil.getFileNameSuffix(file.getFileName());
            if(!StringUtil.nullOrBlank(infiniteFileTypes)){
                List<String> arrayTmp = Arrays.asList(infiniteFileTypes.split(","));
                if(arrayTmp.contains(fileType)){
                    return true;
                }
            }
            //该文件类型不包含在“无峰值限制的文件类型”中，则进行文件大小判断，决定是否加密
            Long limitFileMaxSizeDouble = StringUtil.nullOrBlank(limitFileMaxSize) ? 0L : Long.parseLong(limitFileMaxSize) * 1024 * 1024;
            return file.getFileSize() < limitFileMaxSizeDouble;
        }
        return false;
    }
    
    /**
     * 判断文件类型及大小峰值是否允许加密
     * @param file
     * @return  true:加密   false:不加密
     */
    public boolean isEncrypt(TeamworkFile file){
        //是否开启文件加密
        String isOpenEncryptFile = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENENCRYPTFILE.getKey());
        if((BasicsBool.YES.getBool()).equals(isOpenEncryptFile)){
            String limitFileMaxSize = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ENCRYPTFILEMAXSIZE.getKey());
            String infiniteFileTypes = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.INFINITEENCRYPTFILETYPE.getKey());
            String fileType = FileUtil.getFileNameSuffix(file.getFileName());
            if(!StringUtil.nullOrBlank(infiniteFileTypes)){
                List<String> arrayTmp = Arrays.asList(infiniteFileTypes.split(","));
                if(arrayTmp.contains(fileType)){
                    return true;
                }
            }
            //该文件类型不包含在“无峰值限制的文件类型”中，则进行文件大小判断，决定是否加密
            Long limitFileMaxSizeDouble = StringUtil.nullOrBlank(limitFileMaxSize) ? 0L : Long.parseLong(limitFileMaxSize) * 1024 * 1024;
            return file.getFileSize() < limitFileMaxSizeDouble;
        }
        return false;
    }
    
    /**
     * 用户空间规则，调用用户任何空间时，先调用此方法
     * @param employee  
     * void 
     * @exception
     */
    public void spaceRule(Employee employee){
        //如果用户总空间未设定，则用默认空间大小代替
        String spaceSize = employee.getSpaceSize();
        if(StringUtil.nullOrBlank(spaceSize)){
            spaceSize = SysConfigHelper.getValue(SystemContant.BASICSCODE, SystemContant.PERSONAL_SPACE);
        }
        //检验是否为数字组成
        if(!StringUtil.isNumber(spaceSize)){
            throw new MyException();
        }
        //如果协作空间未设值，默认为总空间大小的10%
        String teamworkSize = employee.getTeamworkSpaceSize();
        if(StringUtil.nullOrBlank(teamworkSize)){
            Long spaceLSize = Long.valueOf(spaceSize);
            teamworkSize = "" + (spaceLSize / 10);
        }
        //计算个人空间大小：总空间大小 减去 协作空间大小
        String personalSize = "" + (Long.valueOf(spaceSize) - Long.valueOf(teamworkSize));
        //注入employee对象中，供方法取值
        employee.setSpaceSize(spaceSize);
        employee.setPersonalSize(personalSize);
        employee.setTeamworkSpaceSize(teamworkSize);
    }
    
    /**
     * 判断个人空间是否已满
     * @param files
     * @param employee  
     * void 
     * @exception
     */
    public void checkPersonalSize(List<Long> fileSizeList, Employee employee) {
        //可用空间
        Long personalUseSize = getEmployeeUsableDiskSize(employee.getId());
        //所需空间大小
        Long needSize = 0L;
        if (fileSizeList != null && fileSizeList.size() > 0) {
            for (Long fileSize : fileSizeList) {
                needSize += fileSize;
            }
        }
        if (needSize >= personalUseSize) {
            throw new MyException(DiskException.FULL_PERSONAL_SIZE);
        }
    }
    
    /**
     * 判断协作空间是否已满
     * @param files
     * @param employee  
     * void 
     * @exception
     */
    public void checkTeamworkSize(List<Long> fileSizeList, Integer teamworkId) {
        //可用空间
        Long teamworkUseSize = getTeamworkUsableDiskSize(teamworkId, null);
        //所需空间大小
        Long needSize = 0L;
        if (fileSizeList != null && fileSizeList.size() > 0) {
            for (Long fileSize : fileSizeList) {
                needSize += fileSize;
            }
        }
        if (needSize >= teamworkUseSize) {
            throw new MyException(DiskException.FULL_TEAMWORK_SIZE);
        }
    }
    
    /**
     * 排除重复引用路径，得到最终确定删除路径
     * @param filePaths  
     * void 
     * @exception
     */
    public void getOnlyUseFilePath(Set<String> filePaths) {
        //创建对象集，用以保存待删除路径filePaths中，存在重复引用的物理路径
        Set<String> busyFilePaths = new HashSet<String>();
        //遍历
        if (filePaths != null && filePaths.size() > 0) {
            for (String filePath : filePaths) {
                //将待删除路径放入协作文件表中查询重复引用
                TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
                teamworkFileQuery.setFilePath(filePath);
                int teamworkFileCount = teamworkFileDao.selectCountByQuery(teamworkFileQuery);
                //将待删除路径放入协作文件版本表中查询重复引用
                TeamworkFileVersionQuery teamworkFileVersionQuery = new TeamworkFileVersionQuery();
                teamworkFileVersionQuery.setFilePath(filePath);
                int teamworkFileVersionCount = teamworkFileVersionDao.selectCountByQuery(teamworkFileVersionQuery);
                //将待删除路径放入文件表中查询重复引用
                FileQuery fileQuery = new FileQuery();
                fileQuery.setFilePath(filePath);
                int fileCount = fileDao.selectCountByQuery(fileQuery);
                //将待删除路径放入文件版本表中查询重复引用
                FileVersionQuery fileVersionQuery = new FileVersionQuery();
                fileVersionQuery.setFilePath(filePath);
                int fileVersionCount = fileVersionDao.selectCountByQuery(fileVersionQuery);
                //如果查询出一处及一处以上，说明存在重复引用，加入busyFilePaths集中
                if ((teamworkFileCount + teamworkFileVersionCount + fileCount + fileVersionCount) > 0) {
                    busyFilePaths.add(filePath);
                }
            }
        }
        //获取全部重复引用集后，与待删除路径集比较，最终得出确认要删除的物理路径集
        if (busyFilePaths != null && busyFilePaths.size() > 0) {
            for (String busyFilePath : busyFilePaths) {
                if (filePaths.contains(busyFilePath)) {
                    filePaths.remove(busyFilePath);
                }
            }
        }
    }
    
    /**
     * 交互文件服务器，删除真实文件
     * @param fileEntities
     * @param employeeId  
     * void 
     * @exception
     */
    public void delRealFileByFilePath(List<FileEntity> fileEntities,
            Integer employeeId) {
        //文件服务器删除
        FileOperateRequest<FileEntity> temp = new FileOperateRequest<FileEntity>();
        temp.setCmdId(EnumFileServiceOperateType.DELETE_FILE.getCmdId());
        temp.setOperateId("" + employeeId);
        temp.setOperateTime(DateUtil.getDateFormat(new Date(),
                DateUtil.YMDSTRING_DATA));
        temp.setParams(fileEntities);
        temp.setChkValue(FileServiceOperateManagerUtil.getChkValue(temp));
        String jsonStr = JsonUtils.bean2json(temp);
        List<String> fileServices = fileServiceRouteStrategy.getAllFileServiceAddress();
        for (String url : fileServices) {
            //文件服务器返回
            String result = HttpUtil.post(url
                    + EnumFileServiceOperateType.DELETE_FILE.getUrlAddress(),
                    jsonStr, "", "UTF-8");
            System.out.println("返回结果：" + result);
        }
        //若code码不为1000，则抛出异常
    }
    
    public void threadDeleteFile(Set<String> deleteFilePaths){
      //构建文件服务器交互对象list
        final List<FileEntity> fileEntities = new ArrayList<FileEntity>();
        if (deleteFilePaths != null && deleteFilePaths.size() > 0) {
            for (String deleteFilePath : deleteFilePaths) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFilePath(deleteFilePath);
                fileEntity.setFileId("" + 0);
                fileEntities.add(fileEntity);
            }
        }
        //线程删除物理路径
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                delRealFileByFilePath(fileEntities, 1);
            }
        });
        thread.start();
    }
    
    public void spaceInfo(HttpServletRequest request, Employee employee){
        //空间规则
        spaceRule(employee);
        BigDecimal b = null;
        //总空间大小(MB)
        Long spaceSize = Long.valueOf(employee.getSpaceSize());
        
        //个人空间大小(B)
        Long personalSize = Long.valueOf(employee.getPersonalSize()) * 1024 * 1024;
        //协作空间大小(B)
        Long teamworkSize = Long.valueOf(employee.getTeamworkSpaceSize()) * 1024 * 1024;
        
        //可用个人空间(B)
        Long personalUsableSize = getEmployeeUsableDiskSize(employee.getId());
        //可用协作空间(B)
        Long teamworkUsableSize = getTeamworkUsableDiskSize(null, employee.getId());
        //已用个人空间(B)
        Long personalUseSize = personalSize - personalUsableSize;
        //已用协作空间(B)
        Long teamworkUseSize = teamworkSize - teamworkUsableSize;
        
        //个人空间使用比率
        Double personalUseRatio = personalSize == 0 ? 0 : (double)personalUseSize / ((double)personalSize);
        //协作空间使用比率
        Double teamworkUseRatio = teamworkSize == 0 ? 0 : (double)teamworkUseSize / ((double)teamworkSize);
        
        //格式化
        String spaceSizeNum = FileSizeUtil.bytesToSize(spaceSize * 1024 * 1024);
        String personalSizeNum = FileSizeUtil.bytesToSize(personalSize);
        String teamworkSizeNum = FileSizeUtil.bytesToSize(teamworkSize);
        String personalUseSizeNum = FileSizeUtil.bytesToSize(personalUseSize);
        String teamworkUseSizeNum = FileSizeUtil.bytesToSize(teamworkUseSize);
        String personalUsableSizeNum = FileSizeUtil.bytesToSize(personalUsableSize);
        String teamworkUsableSizeNum = FileSizeUtil.bytesToSize(teamworkUsableSize);
        //占用比率处理
        b = new BigDecimal(personalUseRatio * 100);  
        personalUseRatio = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();  
        b = new BigDecimal(teamworkUseRatio * 100);  
        teamworkUseRatio = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();  
        
        //可分配最大协作空间
        Double teamworkMaxSize = (double)spaceSize - ((double)personalUseSize / 1024 / 1024);
        b = new BigDecimal(teamworkMaxSize);
        teamworkMaxSize = b.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
        //需分配最小协作空间
        Double teamworkMinSize = (double)teamworkUseSize / 1024 / 1024;
        b = new BigDecimal(teamworkMinSize);
        teamworkMinSize = b.setScale(0, BigDecimal.ROUND_UP).doubleValue();
        
        request.setAttribute("spaceSize", spaceSize);
        request.setAttribute("spaceSizeB", spaceSize * 1024 * 1024);
        request.setAttribute("personalSize", personalSize / 1024 / 1024);
        request.setAttribute("teamworkSize", teamworkSize / 1024 / 1024);
        request.setAttribute("personalSizeB", personalSize);
        request.setAttribute("teamworkSizeB", teamworkSize);
        request.setAttribute("teamworkMaxSize", teamworkMaxSize);
        request.setAttribute("teamworkMinSize", teamworkMinSize);
        request.setAttribute("spaceSizeNum", spaceSizeNum);
        request.setAttribute("personalSizeNum", personalSizeNum);
        request.setAttribute("teamworkSizeNum", teamworkSizeNum);
        request.setAttribute("personalUseSizeNum", personalUseSizeNum);
        request.setAttribute("teamworkUseSizeNum", teamworkUseSizeNum);
        request.setAttribute("personalUseRatio", personalUseRatio);
        request.setAttribute("teamworkUseRatio", teamworkUseRatio);
        request.setAttribute("personalUsableSize", personalUsableSize);
        request.setAttribute("teamworkUsableSize", teamworkUsableSize);
        request.setAttribute("personalUseSize", personalUseSize);
        request.setAttribute("teamworkUseSize", teamworkUseSize);
        request.setAttribute("personalUsableSizeNum", personalUsableSizeNum);
        request.setAttribute("teamworkUsableSizeNum", teamworkUsableSizeNum);
    }
}
