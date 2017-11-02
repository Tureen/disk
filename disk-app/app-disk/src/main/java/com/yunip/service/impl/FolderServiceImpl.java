package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.DeleteType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.FileDeleteStatus;
import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.enums.disk.FileType;
import com.yunip.enums.disk.FolderType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.disk.OperateType;
import com.yunip.enums.disk.ShareStatus;
import com.yunip.enums.disk.ValidStatus;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.enums.log.ActionType;
import com.yunip.manage.FileIndexManager;
import com.yunip.manage.FileManager;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.manage.LogManager;
import com.yunip.mapper.disk.IAuthorityShareDao;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileDeleteDao;
import com.yunip.mapper.disk.IFileSignDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.disk.IFolderDeleteDao;
import com.yunip.mapper.disk.ITakeCodeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.FileSign;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.FolderDelete;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.query.FileVersionQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.FileRep;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameNameHelper;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.IFolderService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.serial.SerialCodeUtil;
import com.yunip.utils.util.StringUtil;

@Service("iFolderService")
public class FolderServiceImpl implements IFolderService {

    @Resource(name = "iFolderDao")
    private IFolderDao             folderDao;

    @Resource(name = "iFileDao")
    private IFileDao               fileDao;

    @Resource(name = "iFileVersionDao")
    private IFileVersionDao        fileVersionDao;

    @Resource(name = "iAuthorityShareService")
    private IAuthorityShareService authorityShareService;

    @Resource(name = "iAuthorityShareDao")
    private IAuthorityShareDao     authorityShareDao;

    @Resource(name = "iTakeCodeDao")
    private ITakeCodeDao           takeCodeDao;

    @Resource(name = "iFileSignDao")
    private IFileSignDao           fileSignDao;

    @Resource(name = "logManager")
    private LogManager             logManager;

    @Resource(name = "fileManager")
    private FileManager            fileManager;

    @Resource(name = "fileIndexManager")
    private FileIndexManager       fileIndexManager;
    
    @Resource(name = "iFileDeleteDao")
    private IFileDeleteDao         fileDeleteDao;
    
    @Resource(name = "iFolderDeleteDao")
    private IFolderDeleteDao       folderDeleteDao;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;

    @Override
    @Transactional
    public Folder getSubFolder(FolderQuery folderQuery) {
        Folder folder = new Folder();
        if (folderQuery != null
                && (folderQuery.getFolderId() == null || folderQuery.getFolderId() == 0)) {
            //当不穿上级目录时候则是首页目录
            folderQuery.setFolderId(0);
            folderQuery.setFolderCode(SystemContant.FIRST_FOLDER_ID);
            folder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
            folder.setId(0);
        }
        else {
            //放入对象
            folder = folderDao.selectById(folderQuery.getFolderId());
        }
        //获取所有文件夹
        FolderQuery zFolderQuery = new FolderQuery();
        zFolderQuery.setParentId(folderQuery.getFolderId());
        zFolderQuery.setQueryName(folderQuery.getQueryName());
        zFolderQuery.setOrderIndex(folderQuery.getOrderIndex());
        List<Folder> folders = folderDao.selectByQuery(zFolderQuery);
        //获取所有文件
        FileQuery zFileQuery = new FileQuery();
        zFileQuery.setEmployeeId(folderQuery.getEmployeeId());
        zFileQuery.setFolderId(folderQuery.getFolderId());
        zFileQuery.setQueryName(folderQuery.getQueryName());
        zFileQuery.setOrderIndex(folderQuery.getOrderIndex());
        List<File> files = fileDao.selectBySubQuery(zFileQuery);
        //放入对象
        folder.setFiles(files);
        folder.setFolders(folders);
        return folder;
    }

    public Folder getQuerySubFolder(FolderQuery folderQuery, Employee employee) {
        Folder folder = new Folder();
        if (folderQuery != null
                && (folderQuery.getFolderId() == null || folderQuery.getFolderId() == 0)) {
            //当不穿上级目录时候则是首页目录
            folderQuery.setFolderId(0);
            folderQuery.setFolderCode(SystemContant.FIRST_FOLDER_ID);
            folder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
            folder.setId(0);
        }
        else {
            //放入对象
            folder = folderDao.selectById(folderQuery.getFolderId());
        }
        //获取所有文件夹
        FolderQuery zFolderQuery = new FolderQuery();
        if (StringUtils.isNotBlank(folderQuery.getQueryName())) {
            zFolderQuery.setQueryName(folderQuery.getQueryName());
        }
        else {
            zFolderQuery.setParentId(folderQuery.getFolderId());
        }
        zFolderQuery.setEmployeeId(employee.getId());
        zFolderQuery.setFolderType(folderQuery.getFolderType());
        zFolderQuery.setOrderIndex(folderQuery.getOrderIndex());
        List<Folder> folders = folderDao.selectByQuery(zFolderQuery);
        //获取所有文件
        FileQuery zFileQuery = new FileQuery();
        if (StringUtils.isNotBlank(folderQuery.getQueryName())) {
            zFileQuery.setQueryName(folderQuery.getQueryName());
            zFileQuery.setFolderId(null);
        }
        else {
            zFileQuery.setFolderId(folderQuery.getFolderId());
        }
        zFileQuery.setEmployeeId(employee.getId());
        zFileQuery.setOrderIndex(folderQuery.getOrderIndex());
        List<File> files = fileDao.selectBySubSignQuery(zFileQuery);
        //放入对象
        folder.setFiles(files);
        folder.setFolders(folders);
        return folder;
    }

    @Override
    public List<Folder> getParentFolders(int folderId) {
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = folderDao.selectById(folderId);
        if (folder != null) {
            folders.add(folder);
        }
        getParentFolder(folders, folder);
        Collections.reverse(folders);
        return folders;
    }

    public void getParentFolder(List<Folder> folders, Folder folder) {
        if (folder != null && folder.getParentId() != 0) {
            Folder paFolder = folderDao.selectById(folder.getParentId());
            folders.add(paFolder);
            getParentFolder(folders, paFolder);
        }
    }

    @Override
    @Transactional
    public void createPersonalFolder(Folder folder, Employee employee) {
        String parentCode = SystemContant.FIRST_FOLDER_ID;
        if (folder != null && folder.getParentId() != 0) {
            Folder parentFolder = folderDao.selectById(folder.getParentId());
            folder.setEmployeeId(parentFolder.getEmployeeId());
            parentCode = parentFolder.getFolderCode();
        }
        //设置状态为正常
        folder.setValidStatus(ValidStatus.NOMAL.getStatus());
        //设置文件夹类型
        folder.setFolderType(FolderType.PERSONAL.getType());
        //存在同名的文件
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(folder.getParentId());
        folderQuery.setEmployeeId(folder.getEmployeeId());
        folderQuery.setQueryName(folder.getFolderName());
        setFolderName(folder, folderQuery);
        //查询当前文件夹的子文件夹
        FolderQuery query = new FolderQuery();
        query.setEmployeeId(folder.getEmployeeId());
        query.setParentId(folder.getParentId());
        List<String> folderCodes = folderDao.selectByParentId(query);
        if (folderCodes != null && folderCodes.size() >= 9999) {
            throw new MyException(DiskException.NOCREATEFOLDERS);
        }
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                parentCode);
        folder.setFolderCode(folderCode);
        folderDao.insert(folder);
        //添加用户日志
        logManager.insertEmpLoyeeLog(folder.getId(), OpenType.FOLDER,
                folder.getFolderName(), ActionType.CREATE, employee);
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

    @Override
    @Transactional
    public File copyFile(File file, int folderId, Employee employee) {
        File oldFile = fileDao.selectById(file.getId());
        Folder toFolder = folderDao.selectById(folderId);
        if (toFolder == null) {
            toFolder = new Folder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //复制文件  1.判断是否存在同一个目录下
        FileQuery fileQuery = new FileQuery();
        fileQuery.setEmployeeId(employee.getId());
        fileQuery.setFolderId(folderId);
        fileQuery.setQueryName(oldFile.getFileName());
        setFileName(oldFile, fileQuery);
        oldFile.setFolderId(toFolder.getId());
        oldFile.setEmployeeId(employee.getId());
        oldFile.setFolderCode(toFolder.getFolderCode());
        oldFile.setCreateAdmin(employee.getEmployeeName());
        oldFile.setShareStatus(ShareStatus.NONE.getStatus());
        oldFile.setCreateTime(new Date());
        oldFile.setFileVersion(0);
        fileDao.insert(oldFile);
        //复制新文件:文件索引表添加
        fileIndexManager.insertFileIndex(oldFile,
                FileIndexOperType.SAVE.getType());
        //记录日志
        logManager.insertEmpLoyeeLog(oldFile.getFolderId(), OpenType.FILE,
                oldFile.getFileName(), ActionType.COPY, employee);
        return oldFile;
    }

    @Override
    @Transactional
    public void copyFolder(Folder folder, int folderId, Employee employee, List<Long> fileSizeList) {
        Folder toFolder = folderDao.selectById(folderId);
        Folder oldFolder = getCopyAndMoveFolder(folder, toFolder, employee,
                OperateType.Copy.getType());
        if (folderId != 0) {
            oldFolder.setFolderType(toFolder.getFolderType());
        }
        oldFolder.setEmployeeId(employee.getId());
        oldFolder.setShareStatus(ShareStatus.NONE.getStatus());
        folderDao.insert(oldFolder);
        //递归添加内部文件夹和文件
        List<FileRep> fileReps = new ArrayList<FileRep>();
        saveFolderFile(fileReps, folder.getId(), oldFolder, employee, fileSizeList);
        //记录日志
        logManager.insertEmpLoyeeLog(oldFolder.getId(), OpenType.FOLDER,
                oldFolder.getFolderName(), ActionType.COPY, employee);
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
    private Folder getCopyAndMoveFolder(Folder folder, Folder toFolder,
            Employee employee, Integer type) {
        Folder oldFolder = folderDao.selectById(folder.getId());
        if (toFolder == null) {
            toFolder = new Folder();
            toFolder.setId(0);
            toFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //复制或者移动文件夹  1.判断是否存在同一个目录下
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(toFolder.getId());
        folderQuery.setEmployeeId(employee.getId());
        folderQuery.setQueryName(oldFolder.getFolderName());
        setFolderName(oldFolder, folderQuery);
        oldFolder.setParentId(toFolder.getId());
        //重新生成code
        oldFolder.setFolderCode(toFolder.getFolderCode());
        //查询当前文件夹的子文件夹
        FolderQuery query = new FolderQuery();
        query.setEmployeeId(employee.getId());
        query.setParentId(toFolder.getId());
        List<String> folderCodes = folderDao.selectByParentId(query);
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                toFolder.getFolderCode());
        //若是复制操作，需重定义属性
        if (type == OperateType.Copy.getType()) {
            //重定义文件夹所属人
            oldFolder.setEmployeeId(employee.getId());
            //重定义分享状态
            oldFolder.setShareStatus(ShareStatus.NONE.getStatus());
        }
        oldFolder.setFolderCode(folderCode);
        oldFolder.setCreateAdmin(employee.getEmployeeName());
        oldFolder.setCreateTime(new Date());
        return oldFolder;
    }

    /***
     * 递归进行文件夹复制
     * @param fileReps  需要复制的文件路劲对象
     * @param folderId  原来的文件夹ID
     * @param toFolder  新文件夹对象
     * @param employee  操作的员工对象
     * @param type      行为是复制or移动（复制：1  移动：0）
     * void 
     * @exception
     */
    private void saveFolderFile(List<FileRep> fileReps, int folderId,
            Folder toFolder, Employee employee, List<Long> fileSizeList) {
        //获取所有文件夹
        FolderQuery zFolderQuery = new FolderQuery();
        zFolderQuery.setParentId(folderId);
        List<Folder> folders = folderDao.selectByQuery(zFolderQuery);
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                int olderFolderId = subFolder.getId();
                subFolder.setParentId(toFolder.getId());
                //重定义文件夹类型,根据父文件夹类型
                subFolder.setFolderType(toFolder.getFolderType());
                //重定义文件夹所属人
                subFolder.setEmployeeId(employee.getId());
                //重定义文件夹分享状态
                subFolder.setShareStatus(ShareStatus.NONE.getStatus());
                //查询当前文件夹的子文件夹
                FolderQuery query = new FolderQuery();
                query.setEmployeeId(subFolder.getEmployeeId());
                query.setParentId(subFolder.getParentId());
                List<String> folderCodes = folderDao.selectByParentId(query);
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                        toFolder.getFolderCode());
                subFolder.setFolderCode(folderCode);
                subFolder.setEmployeeId(employee.getId());
                subFolder.setShareStatus(ShareStatus.NONE.getStatus());
                folderDao.insert(subFolder);
                saveFolderFile(fileReps, olderFolderId, subFolder, employee, fileSizeList);
            }
        }
        //获取所有文件
        FileQuery zFileQuery = new FileQuery();
        zFileQuery.setFolderId(folderId);
        List<File> files = fileDao.selectBySubQuery(zFileQuery);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                //加入查询disk总空间集合
                fileSizeList.add(file.getFileSize());
                
                file.setCreateAdmin(employee.getEmployeeName());
                file.setFolderId(toFolder.getId());
                file.setFolderCode(toFolder.getFolderCode());
                //重定义文件所属人
                file.setEmployeeId(employee.getId());
                //重定义文件分享状态
                file.setShareStatus(ShareStatus.NONE.getStatus());
                file.setEmployeeId(employee.getId());
                file.setFileVersion(0);
                fileDao.insert(file);
                //复制新文件:文件索引表添加
                fileIndexManager.insertFileIndex(file,
                        FileIndexOperType.SAVE.getType());
                //整理好需要辅助路径的操作
                FileRep fileRep = new FileRep();
                fileRep.setFileId(file.getId());
                fileRep.setPath(file.getFilePath());
                fileReps.add(fileRep);
            }
        }
    }

    @Override
    @Transactional
    public void moveFile(File file, int folderId, Employee employee) {
        file = fileDao.selectById(file.getId());
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
        fileQuery.setQueryName(file.getFileName());
        setFileName(file, fileQuery);
        fileDao.update(file);
    }

    @Override
    @Transactional
    public void moveFolder(Folder folder, int folderId, Employee employee) {
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
        Folder oldFolder = getCopyAndMoveFolder(folder, toFolder, employee,
                OperateType.Move.getType());
        folderDao.update(oldFolder);
        //修改子集的文件夹编码和文件中的folderCode
        moveFolderCode(oldFolder.getId(), oldFolder, employee);
    }

    /***
     * 移动文件夹修改文件夹下的folderCode
     * @param folderId  原来的文件夹ID
     * @param toFolder  新文件夹对象
     * @param employee  操作的员工对象 
     * void 
     * @exception
     */
    private void moveFolderCode(int folderId, Folder toFolder, Employee employee) {
        //获取所有文件夹
        FolderQuery zFolderQuery = new FolderQuery();
        zFolderQuery.setParentId(folderId);
        List<Folder> folders = folderDao.selectByQuery(zFolderQuery);
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                int subFolderId = subFolder.getId();
                subFolder.setParentId(toFolder.getId());
                //查询当前文件夹的子文件夹
                FolderQuery query = new FolderQuery();
                query.setEmployeeId(subFolder.getEmployeeId());
                query.setParentId(subFolder.getParentId());
                List<String> folderCodes = folderDao.selectByParentId(query);
                String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                        toFolder.getFolderCode());
                subFolder.setFolderCode(folderCode);
                folderDao.update(subFolder);
                moveFolderCode(subFolderId, subFolder, employee);
            }
        }
        //获取所有文件
        FileQuery zFileQuery = new FileQuery();
        zFileQuery.setFolderId(folderId);
        List<File> files = fileDao.selectBySubQuery(zFileQuery);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                file.setUpdateTime(new Date());
                file.setUpdateAdmin(employee.getEmployeeName());
                file.setFolderId(toFolder.getId());
                file.setFolderCode(toFolder.getFolderCode());
                fileDao.update(file);
            }
        }
    }

    @Override
    @Transactional
    public void delFolderOrFile(Folder folder, boolean isDel, Employee employee, Employee employeeLog) {
        List<Folder> folders = folder.getFolders();
        List<File> files = folder.getFiles();
        //物理文件路径删除对象集
        final List<FileEntity> fileEntities = new ArrayList<FileEntity>();
        //物理文件版本路径删除对象集
        final List<FileEntity> fileVersionEntities = new ArrayList<FileEntity>();
        //文件索引对象集
        final List<File> fileIndexEntities = new ArrayList<File>();
        //需要删除的文件夹
        if (folders != null && folders.size() > 0) {
            for (Folder fold : folders) {
                delFolder(fileEntities,fileVersionEntities, fileIndexEntities, fold.getId(), isDel,
                        employee,employeeLog);
                //首先,删除当前文件夹
                //folderDao.delById(fold.getId());
            }
        }
        //需要删除的文件
        if (files != null && files.size() > 0) {
            for (File file : files) {
                delFile(fileEntities, fileVersionEntities,fileIndexEntities, file.getId(), isDel,
                        employee,employeeLog);
            }
        }
        final List<Integer> unFileIds = new ArrayList<Integer>();
/*        if (isDel) {
            final int employeeId = employee.getId();
            //删除服务器文件
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (fileIndexEntities.size() > 0) {
                      for (File fileTmp : fileIndexEntities){
                          //根据filePath查询是否重复引用,加入不删除集合
                          getNotOnlyFilePath(unFileIds, fileTmp);
                      }
                      //遍历所有文件，若id不在保留物理路径集合，加入删除集合fileEntities
                      for (File fileTmp : fileIndexEntities){
                          if(!unFileIds.contains(fileTmp.getId())){
                              FileEntity fileEntity = new FileEntity();
                              fileEntity.setFileId("" + fileTmp.getId());
                              fileEntity.setFilePath(fileTmp.getFilePath());
                              fileEntities.add(fileEntity);
                          }
                      }
                      //真实删除
                      delRealFileByFilePath(fileEntities, employeeId);
                    }
                }
            });
            thread.start();
        }else{*/   
        //需删除文件版本对应信息
        final int employeeId = employee.getId();
        //删除服务器文件
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (fileIndexEntities.size() > 0) {
                  for (FileEntity fileEntity : fileVersionEntities){
                      getNotOnlyFileVersionPath(unFileIds, fileEntity);
                  }
                  final List<FileEntity> doFileVersionEntities = new ArrayList<FileEntity>();
                  //遍历所有文件，若id不在保留物理路径集合，加入删除集合fileEntities
                  for (FileEntity fileEntity : fileVersionEntities){
                      if(!unFileIds.contains(Integer.valueOf(fileEntity.getTmpId()))){
                          doFileVersionEntities.add(fileEntity);
                      }
                  }
                  //真实删除
                  delRealFileByFilePath(doFileVersionEntities, employeeId);
                }
            }
        });
        thread.start();
        //文件索引表添加
        Thread threadTwo = new Thread(new Runnable() {

            @Override
            public void run() {
                if (fileIndexEntities.size() > 0) {
                    for (File fileTmp : fileIndexEntities)
                        fileIndexManager.insertFileIndex(fileTmp,
                                FileIndexOperType.DEL.getType());
                }
            }
        });
        threadTwo.start();
    }

    @Override
    public void delFile(List<FileEntity> fileEntities,List<FileEntity> fileVersionEntities,
            List<File> fileIndexEntities, int fileId, boolean isDel,
            Employee employee, Employee employeeLog) {
        File file = fileDao.selectById(fileId);
        if (file != null) {
            if (!employee.getId().equals(file.getEmployeeId())) {
                //进行权限判断
                int auth = authorityShareService.getOpenAuthById(employee,
                        fileId, DeleteType.DIRECT.getType());
                if (auth != AuthorityType.MANAGER.getCode()) {
                    throw new MyException(DiskException.NOPERMISSION,
                            employee.getId());
                }
            }
            //获取唯一引用路径的文件
            getOnlyFilePath(fileEntities, file);
            //将对应文件的历史版本加入到删除集合中
            delFileVersionByFileId(fileVersionEntities, file.getId());
            //数据库文件表及相关表删除
            delFileRelationByFile(fileEntities, fileIndexEntities, file, employee, DeleteType.DIRECT.getType(),employeeLog);
            //记录日志
            logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                    file.getFileName(), ActionType.DELETE, employeeLog);
        }
    }

    @Override
    public void delFolder(List<FileEntity> fileEntities,List<FileEntity> fileVersionEntities,
            List<File> fileIndexEntities, int folderId, boolean isDel,
            Employee employee, Employee employeeLog) {
        Folder folder = folderDao.selectById(folderId);
        if (folder != null) {
            if (!employee.getId().equals(folder.getEmployeeId())) {
                //进行权限判断
                int auth = authorityShareService.getOpenAuthById(employee,
                        folderId, OpenType.FOLDER.getType());
                if (auth != AuthorityType.MANAGER.getCode()) {
                    throw new MyException(DiskException.NOPERMISSION,
                            employee.getId());
                }
            }
            //进行删除，递归遍历
            getFileByFolderId(fileEntities, fileIndexEntities, folderId);
            //查询文件夹下所有子文件的版本文件，并加入到删除对象中
            delFileVersionByFolderId(fileVersionEntities, folder);
            //根据文件夹Code 删除子目录文件
            delFileByCode(folder.getFolderCode(), employee, folderId, employeeLog);
            //根据文件夹Code 删除子目录
            delFolderByCode(folder.getFolderCode(),employee, folderId, employeeLog);
            //根据code删除文件相关表数据
            delFileRelevantByCode(folder.getFolderCode(), employee.getId());
            //根据code删除文件夹相关表数据
            delFolderRelevantByCode(folder.getFolderCode(), employee.getId());
            //删除文件夹相关表
            delFolderRelation(folderId);
            //记录日志
            logManager.insertEmpLoyeeLog(folderId, OpenType.FILE,
                    folder.getFolderName(), ActionType.DELETE, employeeLog);
        }
    }
    
    /**
     * delFolder调用，查询文件夹下所有子文件的版本文件，并加入到删除对象中
     * @param fileVersionEntities
     * @param folderId  
     * void 
     * @exception
     */
    private void delFileVersionByFolderId(List<FileEntity> fileVersionEntities, Folder folder){
        File file = new File();
        file.setEmployeeId(folder.getEmployeeId());
        file.setFolderCode(folder.getFolderCode());
        List<FileVersion> list = fileVersionDao.selectByFolderCode(file);
        if(list != null && list.size() > 0){
            for(FileVersion fileVersion : list){
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileId(""+fileVersion.getFileId());
                fileEntity.setTmpId(""+fileVersion.getId());
                fileEntity.setFilePath(fileVersion.getFilePath());
                fileVersionEntities.add(fileEntity);
            }
        }
    }
    
    /**
     * delFile方法所用，删除文件通过code 
     * @param folderCode
     * @param employeeId  
     * void 
     * @exception
     */
    private void delFileByCode(String folderCode, Employee employee, Integer deleteType, Employee employeeLog){
        //根据code获取所有文件
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFolderCode(folderCode);
        fileQuery.setEmployeeId(employee.getId());
        List<File> files = fileDao.selectByQuery(fileQuery);
        //循环，添加进文件删除信息表
        if(files != null && files.size() > 0){
            for(File file : files){
                addFileDelete(file, employee, deleteType,employeeLog);
            }
        }
        //删除文件表
        File fileTmp = new File();
        fileTmp.setFolderCode(folderCode);
        fileTmp.setEmployeeId(employee.getId());
        fileDao.delByCode(fileTmp);
    }
    
    /**
     * delFolder方法所用，删除文件夹通过code 
     * @param folderCode
     * @param employeeId  
     * void 
     * @exception
     */
    private void delFolderByCode(String folderCode, Employee employee, Integer deleteType, Employee employeeLog){
        //根据code获取所有文件夹（包含第一层文件夹）
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setFolderCode(folderCode);
        folderQuery.setEmployeeId(employee.getId());
        List<Folder> folders = folderDao.selectByQuery(folderQuery);
        //循环，添加进文件夹删除信息表
        if(folders != null && folders.size() > 0){
            for(Folder folder : folders){
                if(folderCode.equals(folder.getFolderCode())){
                    addFolderDelete(folder,employee,DeleteType.DIRECT.getType(),employeeLog);
                }else{
                    addFolderDelete(folder,employee,deleteType,employeeLog);
                }
            }
        }
        //删除文件夹表
        Folder folderTmp = new Folder();
        folderTmp.setFolderCode(folderCode);
        folderTmp.setEmployeeId(employee.getId());
        folderDao.delByCode(folderTmp);
    }
    
    /**
     * 根据文件完整对象写入文件删除信息表
     * @param file  
     * void 
     * @exception
     */
    private void addFileDelete(File file,Employee employee,Integer deleteType, Employee employeeLog){
        FileDelete fileDelete = new FileDelete();
        fileDelete.setOldId(file.getId());
        fileDelete.setFileName(file.getFileName());
        fileDelete.setFileSuffix(file.getFileSuffix());
        fileDelete.setFileType(file.getFileType());
        fileDelete.setEmployeeId(file.getEmployeeId());
        fileDelete.setFileSize(file.getFileSize());
        fileDelete.setFolderId(file.getFolderId());
        fileDelete.setFolderCode(file.getFolderCode());
        fileDelete.setFilePath(file.getFilePath());
        fileDelete.setStatus(FileDeleteStatus.FALSEDELETE.getStatus());
        fileDelete.setDeleteType(deleteType);
        fileDelete.setActionEmployeeId(employeeLog.getId());
        fileDelete.setCreateAdmin(file.getCreateAdmin());
        fileDelete.setCreateTime(file.getCreateTime());
        fileDelete.setUpdateAdmin(employeeLog.getEmployeeName());
        fileDelete.setUpdateTime(new Date());
        fileDelete.setActionEmployeeIp(employeeLog.getLoginIp());
        fileDelete.setEncryptStatus(file.getEncryptStatus());
        fileDelete.setEncryptKey(file.getEncryptKey());
        fileDelete.setServerCode(file.getServerCode());
        //获取文件绝对路径
        StringBuffer absolutePath = new StringBuffer();
        String[] codeArr = splitFolderCode(file.getFolderCode(),OpenType.FILE.getType());
        if(codeArr != null && codeArr.length > 0){
            for(String code : codeArr){
                Folder folderTmp = new Folder();
                folderTmp.setFolderCode(code);
                folderTmp.setEmployeeId(employee.getId());
                List<Folder> folderTmps = folderDao.selectByFolderCode(folderTmp);
                if(folderTmps != null && folderTmps.size() > 0){
                    absolutePath.append(folderTmps.get(0).getFolderName()+"/");
                }
            }
            absolutePath.deleteCharAt(absolutePath.length()-1);
            fileDelete.setAbsolutePath(absolutePath.toString());
        }
        fileDeleteDao.insert(fileDelete);
    }
    
    /**
     * 根据文件夹完整对象写入文件夹删除信息表
     * @param folder  
     * void 
     * @exception
     */
    private void addFolderDelete(Folder folder,Employee employee,Integer deleteType, Employee employeeLog){
        FolderDelete folderDelete = new FolderDelete();
        folderDelete.setOldId(folder.getId());
        folderDelete.setFolderCode(folder.getFolderCode());
        folderDelete.setEmployeeId(folder.getEmployeeId());
        folderDelete.setFolderName(folder.getFolderName());
        folderDelete.setFolderType(folder.getFolderType());
        folderDelete.setParentId(folder.getParentId());
        folderDelete.setStatus(FileDeleteStatus.FALSEDELETE.getStatus());
        folderDelete.setDeleteType(deleteType);
        folderDelete.setActionEmployeeId(employeeLog.getId());
        folderDelete.setCreateAdmin(folder.getCreateAdmin());
        folderDelete.setCreateTime(folder.getCreateTime());
        folderDelete.setUpdateAdmin(employeeLog.getEmployeeName());
        folderDelete.setUpdateTime(new Date());
        folderDelete.setActionEmployeeIp(employeeLog.getLoginIp());
        //获取文件夹绝对路径
        StringBuffer absolutePath = new StringBuffer();
        String[] codeArr = splitFolderCode(folder.getFolderCode(),OpenType.FOLDER.getType());
        if(codeArr != null && codeArr.length > 0){
            for(String code : codeArr){
                Folder folderTmp = new Folder();
                folderTmp.setFolderCode(code);
                folderTmp.setEmployeeId(employee.getId());
                List<Folder> folderTmps = folderDao.selectByFolderCode(folderTmp);
                if(folderTmps != null && folderTmps.size() > 0){
                    absolutePath.append(folderTmps.get(0).getFolderName()+"/");
                }
            }
            absolutePath.deleteCharAt(absolutePath.length()-1);
            folderDelete.setAbsolutePath(absolutePath.toString());
        }
        folderDeleteDao.insert(folderDelete);
    }
    
    /**
     * 拆分文件夹编码，得到该编码所有上级的文件夹编码 
     * @param folderCode type:0.文件 1.文件夹
     * @return  
     * String[] 
     * @exception
     */
    private String[] splitFolderCode(String folderCode,Integer type){
        //首先根据是文件or文件夹去除末尾4位数
        if(OpenType.FOLDER.getType()==type){
            folderCode = folderCode.substring(0, folderCode.length()-4);
        }
        if(folderCode.length()>=4){
            int size = folderCode.length()/4;
            String[] strArr = new String[size];
            StringBuffer strBuf = new StringBuffer();
            for(int i = 0; i < size ; i++){
                strBuf.append(folderCode.substring(i*4,(i+1)*4));
                strArr[i] = strBuf.toString();
            }
            return strArr;
        }else{
            return null;
        }
    }

    /**
     * 文件相关表删除,通过code批量
     * @param folderCode
     * @param employeeId  
     * void 
     * @exception
     */
    private void delFileRelevantByCode(String folderCode, Integer employeeId) {
        //历史版本
        FileVersion fileVersion = new FileVersion();
        fileVersion.setEmployeeId(employeeId);
        fileVersion.setFolderCode(folderCode);
        fileVersionDao.delByCode(fileVersion);
        //标签关联
        FileSign fileSign = new FileSign();
        fileSign.setFolderCode(folderCode);
        fileSign.setEmployeeId(employeeId);
        fileSignDao.delByCode(fileSign);
        //提取码
        TakeCode takeCode = new TakeCode();
        takeCode.setEmployeeId(employeeId);
        takeCode.setFolderCode(folderCode);
        takeCodeDao.delByCode(takeCode);
        //分享
        AuthorityShare authorityShare = new AuthorityShare();
        authorityShare.setFolderCode(folderCode);
        authorityShare.setEmployeeId(employeeId);
        authorityShare.setOpenType(OpenType.FILE.getType());
        authorityShareDao.delByCode(authorityShare);
    }
    
    /**
     * 文件夹相关表删除,通过code批量
     * @param folderCode
     * @param employeeId  
     * void 
     * @exception
     */
    private void delFolderRelevantByCode(String folderCode, Integer employeeId) {
        //分享
        AuthorityShare authorityShare = new AuthorityShare();
        authorityShare.setFolderCode(folderCode);
        authorityShare.setEmployeeId(employeeId);
        authorityShare.setOpenType(OpenType.FOLDER.getType());
        authorityShareDao.delByCode(authorityShare);
    }
    
    /**
     * 根据filePath查询是否重复引用，用来判断是否删除该路径
     * @param file
     * @return  
     * List<FileEntity> 
     * @exception
     */
    public void getOnlyFilePath(List<FileEntity> fileEntities, File file) {
        //查询 看当前文件路径是否存在多个引用
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFilePath(file.getFilePath());
        List<File> tpfiles = fileDao.selectByQuery(fileQuery);
        if (tpfiles != null && tpfiles.size() == 1) {
            //只有只存在一个的时候才删除
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileId("" + file.getId());
            fileEntity.setFilePath(file.getFilePath());
            fileEntities.add(fileEntity);
        }
    }

    /**
     * 根据filePath查询是否重复引用，用来判断是否删除该路径
     * @param file
     * @return  
     * List<FileEntity> 
     * @exception
     */
    public void getNotOnlyFilePath(List<Integer> unFileIds, File file) {
        //查询 看当前文件路径是否存在多个引用
        FileQuery fileQuery = new FileQuery();
        fileQuery.setFilePath(file.getFilePath());
        List<File> tpfiles = fileDao.selectByQuery(fileQuery);
        if (tpfiles != null && tpfiles.size() >= 1 ) {
            //若删除数据库后还存在，加入不删除物理路径集合
            unFileIds.add(file.getId());
        }
    }
    
    /**
     * delOnlyPath调用，根据filePath查询历史文件是否重复引用，用来判断是否删除该路径
     * @param file
     * @return  
     * List<FileEntity> 
     * @exception
     */
    public void getNotOnlyFileVersionPath(List<Integer> unFileIds, FileEntity fileEntity) {
        //查询 看当前文件路径是否存在多个引用
        FileVersionQuery fileVersionQuery = new FileVersionQuery();
        fileVersionQuery.setFilePath(fileEntity.getFilePath());
        List<FileVersion> tpfiles = fileVersionDao.selectByQuery(fileVersionQuery);
        if (tpfiles != null && tpfiles.size() >= 1 ) {
            //若删除数据库后还存在，加入不删除物理路径集合
            unFileIds.add(Integer.valueOf(fileEntity.getTmpId()));
        }
    }

    /**
     * 数据库数据删除（文件夹）
     * @param fileEntities
     * @param file  
     * void 
     * @exception
     */
    @Transactional
    public void delFolderRelation(Integer folderId) {
        //根据文件夹id，删除父文件夹分享
        moveShare(folderId, OpenType.FOLDER.getType());
    }

    /**
     * 数据库数据删除文件(通过删除勾选文件)
     * @param fileEntities
     * @param file  
     * void 
     * @exception
     */
    @Transactional
    public void delFileRelationByFile(List<FileEntity> fileEntities,
            List<File> fileIndexEntities, File file, Employee employee,Integer deleteType,Employee employeeLog) {
        //删除文件对应版本
        fileVersionDao.delByFileId(file.getId());
        //删除数据库数据
        fileDao.delById(file.getId());
        //移除文件夹权限相关表
        moveShare(file.getId(), OpenType.FILE.getType());
        //删除对应的提取码
        takeCodeDao.delByFileId(file.getId());
        //删除对应的标签关联
        fileSignDao.delByFileId(file.getId());
        //添加索引对象序列
        fileIndexEntities.add(file);
        //将此删除文件添加进文件删除信息表
        addFileDelete(file, employee, deleteType,employeeLog);
    }

    /**
     * 服务器删除文件
     * @param fileEntities
     * @return  
     * String 
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
        //System.out.println("请求参数：" + jsonStr);
        //文件服务器返回
        //String result = HttpUtil.post(SystemContant.FILE_SERVICE_URL + EnumFileServiceOperateType.DELETE_FILE.getUrlAddress(),jsonStr, "", "UTF-8");
        List<String> fileServices = fileServiceRouteStrategy.getAllFileServiceAddress();
        for(String url : fileServices){
            HttpUtil.post(url + EnumFileServiceOperateType.DELETE_FILE.getUrlAddress(),jsonStr, "", "UTF-8");
        }
        //System.out.println("返回结果：" + result);
        //若code码不为1000，则抛出异常
    }

    /***
     * 查找文件夹下所有的文件路径
     * @param folderId  原来的文件夹ID
     * @param toFolder  新文件夹对象
     * @param employee  操作的员工对象 
     * void 
     * @exception
     */
    private void getFileByFolderId(List<FileEntity> fileEntities,
            List<File> fileIndexEntities, int folderId) {
        //获取所有文件夹
        FolderQuery zFolderQuery = new FolderQuery();
        zFolderQuery.setParentId(folderId);
        List<Folder> folders = folderDao.selectByQuery(zFolderQuery);
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                //删除文件夹相关表
                //delFolderRelation(folderId);
                //查询当前文件夹的子文件夹
                getFileByFolderId(fileEntities, fileIndexEntities,
                        subFolder.getId());
            }
        }
        //获取所有文件
        FileQuery zFileQuery = new FileQuery();
        zFileQuery.setFolderId(folderId);
        List<File> files = fileDao.selectBySubQuery(zFileQuery);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                //添加索引对象序列
                fileIndexEntities.add(file);
            }
        }
    }

    /**
     * 权限移除
     * @param openId
     * @param openType
     * @return  
     * int 
     * @exception
     */
    public int moveShare(Integer openId, Integer openType) {
        //移除权限
        AuthorityShare authorityShare = new AuthorityShare();
        authorityShare.setOpenId(openId);
        authorityShare.setOpenType(openType);
        return authorityShareDao.delByKeyId(authorityShare);
    }

    /**
     * 将对应文件的历史版本加入到删除集合中
     * @param fileEntities
     * @param fileId  
     * void 
     * @exception
     */
    @Transactional
    public void delFileVersionByFileId(List<FileEntity> fileVersionEntities,
            Integer fileId) {
        //将对应历史文件的路径填入对象集，用于物理删除
        FileVersionQuery fileVersionQuery = new FileVersionQuery();
        fileVersionQuery.setFileId(fileId);
        List<FileVersion> fileVersions = fileVersionDao.selectByQuery(fileVersionQuery);
        if (fileVersions.size() > 0) {
            for (FileVersion fileVersion : fileVersions) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileId("" + fileId);
                fileEntity.setFilePath(fileVersion.getFilePath());
                fileEntity.setTmpId(""+fileVersion.getId());
                fileVersionEntities.add(fileEntity);
            }
        }
    }

    @Override
    @Transactional
    public void copyFolderOrFile(Folder folder, int folderId, Employee employee) {
        List<Folder> folders = folder.getFolders();
        List<File> files = folder.getFiles();
        //判断云盘空间是否已满
        List<Long> fileSizeList = new ArrayList<Long>();
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                File fileTmp = fileDao.selectById(files.get(i).getId());
                fileSizeList.add(fileTmp.getFileSize());
            }
        }
        //复制文件夹
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                copyFolder(subFolder, folderId, employee, fileSizeList);
            }
        }
        //复制文件
        if (files != null && files.size() > 0) {
            for (File subFile : files) {
                copyFile(subFile, folderId, employee);
            }
        }
        //判断云盘空间是否已满
        fileManager.checkPersonalSize(fileSizeList, employee);
    }

    @Override
    @Transactional
    public void moveFolderOrFile(Folder folder, int folderId, Employee employee) {
        List<Folder> folders = folder.getFolders();
        List<File> files = folder.getFiles();
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                subFolder = folderDao.selectById(subFolder.getId());
                moveFolder(subFolder, folderId, employee);
                //记录日志
                logManager.insertEmpLoyeeLog(subFolder.getId(),
                        OpenType.FOLDER, subFolder.getFolderName(),
                        ActionType.MOVE, employee);
            }
        }
        //移动文件
        if (files != null && files.size() > 0) {
            for (File file : files) {
                file = fileDao.selectById(file.getId());
                moveFile(file, folderId, employee);
                //记录日志
                logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                        file.getFileName(), ActionType.MOVE, employee);
            }
        }

    }

    @Override
    public SameNameHelper getSameName(Folder folder, int folderId,
            Employee employee) {
        List<Folder> folders = folder.getFolders();
        List<File> files = folder.getFiles();
        SameNameHelper sameName = new SameNameHelper();
        //移动文件夹
        if (folders != null && folders.size() > 0) {
            for (Folder subFolder : folders) {
                subFolder = folderDao.selectById(subFolder.getId());
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setParentId(folderId);
                folderQuery.setEmployeeId(employee.getId());
                folderQuery.setFolderName(subFolder.getFolderName());
                List<Folder> list = folderDao.selectByQuery(folderQuery);
                if (list != null && list.size() > 0) {
                    Folder fold = list.get(0);
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
            for (File file : files) {
                file = fileDao.selectById(file.getId());
                FileQuery fileQuery = new FileQuery();
                fileQuery.setFolderId(folderId);
                fileQuery.setFileName(file.getFileName());
                fileQuery.setEmployeeId(employee.getId());
                List<File> list = fileDao.selectByQuery(fileQuery);
                if (list != null && list.size() > 0) {
                    File fil = list.get(0);
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
    public List<Folder> getSubTwoFolders(int folderId, Employee employee) {
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(folderId);
        folderQuery.setEmployeeId(employee.getId());
        List<Folder> folders = folderDao.selectByQuery(folderQuery);
        if (folders != null && folders.size() > 0) {
            for (Folder folder : folders) {
                folderQuery.setParentId(folder.getId());
                List<Folder> subfolders = folderDao.selectByQuery(folderQuery);
                folder.setFolders(subfolders);
            }
        }
        return folders;
    }

    @Override
    @Transactional
    public void rename(RenameHelper renameHelper, Employee employee) {
        if (OpenType.FOLDER.getType() == renameHelper.getOpenType()) {
            //文件夹
            Folder folder = folderDao.selectById(renameHelper.getOpenId());
            //日志
            logManager.insertEmpLoyeeLog(folder.getId(), OpenType.FOLDER,
                    folder.getFolderName(), renameHelper.getName(),
                    ActionType.RENAME, employee);
            if (folder != null) {
                folder.setFolderName(renameHelper.getName().trim());
                folder.setUpdateAdmin(employee.getEmployeeName());
                //查询同名
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setParentId(folder.getParentId());
                folderQuery.setEmployeeId(folder.getEmployeeId());
                folderQuery.setQueryName(renameHelper.getName());
                folderQuery.setpFolderId(folder.getId());
                setFolderName(folder, folderQuery);
                folderDao.update(folder);
            }
        }
        else if (OpenType.FILE.getType() == renameHelper.getOpenType()) {
            //文件
            File file = fileDao.selectById(renameHelper.getOpenId());
            //日志
            logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                    file.getFileName(), renameHelper.getName(),
                    ActionType.RENAME, employee);
            if (file != null) {
                //去左空格
                file.setFileName(renameHelper.getName().replaceAll("^[　 ]+", ""));
                FileType fileType = FileType.getFileType(file.getFileName());
                file.setFileType(fileType.getType());
                file.setUpdateAdmin(employee.getEmployeeName());
                //查询同名
                FileQuery fileQuery = new FileQuery();
                fileQuery.setEmployeeId(file.getEmployeeId());
                fileQuery.setFolderId(file.getFolderId());
                fileQuery.setQueryName(renameHelper.getName());
                fileQuery.setpFileId(file.getId());
                setFileName(file, fileQuery);
                fileDao.update(file);
                //重命名文件:文件索引表添加
                fileIndexManager.insertFileIndex(file,
                        FileIndexOperType.UPDATE.getType());
            }
        }
    }

    @Override
    public boolean checkSameName(RenameHelper renameHelper) {
        if (OpenType.FOLDER.getType() == renameHelper.getOpenType()) {
            Folder folder = folderDao.selectById(renameHelper.getOpenId());
            //文件夹
            FolderQuery folderQuery = new FolderQuery();
            folderQuery.setEmployeeId(folder.getEmployeeId());
            folderQuery.setParentId(folder.getParentId());
            folderQuery.setFolderName(renameHelper.getName());
            folderQuery.setpFolderId(folder.getId());
            int count = folderDao.selectCountByQuery(folderQuery);
            if (count > 0) {
                //存在
                return true;
            }
        }
        else if (OpenType.FILE.getType() == renameHelper.getOpenType()) {
            //文件
            File file = fileDao.selectById(renameHelper.getOpenId());
            FileQuery fileQuery = new FileQuery();
            fileQuery.setEmployeeId(file.getEmployeeId());
            fileQuery.setFolderId(file.getFolderId());
            fileQuery.setFileName(renameHelper.getName());
            fileQuery.setpFileId(file.getId());
            int count = fileDao.selectCountByQuery(fileQuery);
            if (count > 0) {
                //存在
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Folder> getFolderInfoListByQuery(FolderQuery folderQuery) {
        return folderDao.selectByQuery(folderQuery);
    }
}
