/*
 * 描述：〈文件删除信息的实现类〉
 * 创建人：ming.zhu
 * 创建时间：2016-11-17
 */
package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.SystemContant;
import com.yunip.enums.disk.FileDeleteStatus;
import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.enums.disk.FolderType;
import com.yunip.enums.disk.ValidStatus;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.enums.log.AdminActionType;
import com.yunip.manage.FileIndexManager;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileDeleteDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.disk.IFolderDeleteDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.FolderDelete;
import com.yunip.model.disk.query.FileDeleteQuery;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.query.FileVersionQuery;
import com.yunip.model.disk.query.FolderDeleteQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.log.AdminLog;
import com.yunip.model.teamwork.query.TeamworkFileQuery;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;
import com.yunip.model.user.Admin;
import com.yunip.service.IAdminLogService;
import com.yunip.service.IFileDeleteService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.serial.SerialCodeUtil;
import com.yunip.utils.util.StringUtil;

@Service("iFileDeleteService")
public class FileDeleteServiceImpl implements IFileDeleteService{
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @Resource(name = "iFolderDao")
    private IFolderDao folderDao;
    
    @Resource(name = "iFileDeleteDao")
    private IFileDeleteDao fileDeleteDao;
    
    @Resource(name = "iFolderDeleteDao")
    private IFolderDeleteDao folderDeleteDao;
    
    @Resource(name = "fileIndexManager")
    private FileIndexManager       fileIndexManager;
    
    @Resource(name = "iFileVersionDao")
    private IFileVersionDao        fileVersionDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao         teamworkFileDao;

    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao       teamworkFolderDao;

    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao  teamworkFileVersionDao;
    
    /**
     * 管理员or员工操作日志 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminLogService")
    private IAdminLogService adminLogService;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    

    @Override
    public FileDeleteQuery getFileDeletesByQuery(FileDeleteQuery query) {
        List<FileDelete> fileDeletes = fileDeleteDao.selectByQuery(query);
        int count = fileDeleteDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(fileDeletes);
        return query;
    }

    @Override
    public FolderDeleteQuery getFolderDeletesByQuery(FolderDeleteQuery query) {
        List<FolderDelete> folderDeletes = folderDeleteDao.selectByQuery(query);
        int count = folderDeleteDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(folderDeletes);
        return query;
    }

    @Override
    public void deleteFile(String idStr,Admin admin,AdminLog adminLog) {
        final List<FileEntity> doFileEntities = new ArrayList<FileEntity>();
        //物理路径删除
        String[] idArr = idStr.split(",");
        if(idArr.length > 0){
            List<FileEntity> fileEntities = new ArrayList<FileEntity>();
            for(String id : idArr){
                FileDelete fileDelete = fileDeleteDao.selectById(Integer.valueOf(id));
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileId("" + fileDelete.getOldId());
                fileEntity.setFilePath(fileDelete.getFilePath());
                fileEntities.add(fileEntity);
                //日志添加
                adminLog.setId(null);
                adminLog.setActionType(AdminActionType.RECOVERYFILEDELETE.getType());
                adminLog.setOperContent("文件删除:"+fileDelete.getFileName());
                adminLogService.addAdminLog(adminLog);
            }
            delOnlyPath(fileEntities,doFileEntities);
            //文件删除信息表 状态修改为“已删除”
            for(String id : idArr){
                FileDelete fileDeleteTmp = new FileDelete();
                fileDeleteTmp.setId(Integer.valueOf(id));
                fileDeleteTmp.setActionAdminId(admin.getId());
                fileDeleteTmp.setStatus(FileDeleteStatus.TRUEDELETE.getStatus());
                fileDeleteDao.update(fileDeleteTmp);
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    delRealFileByFilePath(doFileEntities, 1);
                }
            });
            thread.start();
        }
    }
    
    //查询文件集唯一路径并判断是否删除
    private void delOnlyPath(List<FileEntity> fileEntities,List<FileEntity> doFileEntities){
        if (fileEntities.size() > 0) {
            List<Integer> unFileIds = new ArrayList<Integer>();
            for (FileEntity fileEntity : fileEntities){
                //将待删除路径放入协作文件表中查询重复引用
                TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
                teamworkFileQuery.setFilePath(fileEntity.getFilePath());
                int teamworkFileCount = teamworkFileDao.selectCountByQuery(teamworkFileQuery);
                //将待删除路径放入协作文件版本表中查询重复引用
                TeamworkFileVersionQuery teamworkFileVersionQuery = new TeamworkFileVersionQuery();
                teamworkFileVersionQuery.setFilePath(fileEntity.getFilePath());
                int teamworkFileVersionCount = teamworkFileVersionDao.selectCountByQuery(teamworkFileVersionQuery);
                //将待删除路径放入文件表中查询重复引用
                FileQuery fileQuery = new FileQuery();
                fileQuery.setFilePath(fileEntity.getFilePath());
                int fileCount = fileDao.selectCountByQuery(fileQuery);
                //将待删除路径放入文件版本表中查询重复引用
                FileVersionQuery fileVersionQuery = new FileVersionQuery();
                fileVersionQuery.setFilePath(fileEntity.getFilePath());
                int fileVersionCount = fileVersionDao.selectCountByQuery(fileVersionQuery);
                //如果查询出一处及一处以上，说明存在重复引用，加入busyFilePaths集中
                if ((teamworkFileCount + teamworkFileVersionCount + fileCount + fileVersionCount) > 0) {
                    unFileIds.add(Integer.valueOf(fileEntity.getFileId()));
                }
            }
            //遍历所有文件，若id不在保留物理路径集合，加入删除集合fileEntities
            for (FileEntity fileEntity : fileEntities){
                if(!unFileIds.contains(Integer.valueOf(fileEntity.getFileId()))){
                    doFileEntities.add(fileEntity);
                }
            }
          }
    }
    
    @Override
    @Transactional
    public void deleteFolder(String idStr,Admin admin,AdminLog adminLog) {
        final List<FileEntity> doFileEntities = new ArrayList<FileEntity>();
        String[] idArr = idStr.split(",");
        if(idArr.length > 0){
            //文件夹删除信息表 状态修改为“已删除”
            for(String id : idArr){
                //物理路径删除
                FolderDelete firstFolderDelete = folderDeleteDao.selectById(Integer.valueOf(id));
                List<FileEntity> fileEntities = new ArrayList<FileEntity>();
                FileDeleteQuery fileDeleteQuery = new FileDeleteQuery();
                fileDeleteQuery.setDeleteType(firstFolderDelete.getOldId());
                List<FileDelete> fileDeletes = fileDeleteDao.selectAllByQuery(fileDeleteQuery);
                if(fileDeletes != null && fileDeletes.size() > 0){
                    for(FileDelete fileDelete : fileDeletes){
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFileId("" + fileDelete.getOldId());
                        fileEntity.setFilePath(fileDelete.getFilePath());
                        fileEntities.add(fileEntity);
                    }
                    delOnlyPath(fileEntities,doFileEntities);
                }
                //主文件夹状态修改
                FolderDelete folderDeleteTmp = new FolderDelete();
                folderDeleteTmp.setId(Integer.valueOf(id));
                folderDeleteTmp.setActionAdminId(admin.getId());
                folderDeleteTmp.setStatus(FileDeleteStatus.TRUEDELETE.getStatus());
                folderDeleteDao.update(folderDeleteTmp);
                //文件夹子文件夹状态修改
                FolderDelete pFolderDelete = folderDeleteDao.selectById(Integer.valueOf(id));
                FolderDelete zFolderDelete = new FolderDelete();
                zFolderDelete.setDeleteType(pFolderDelete.getOldId());
                zFolderDelete.setActionAdminId(admin.getId());
                zFolderDelete.setStatus(FileDeleteStatus.TRUEDELETE.getStatus());
                folderDeleteDao.updateByDeleteType(zFolderDelete);
                //文件夹子文件状态修改
                FileDelete zFileDelete = new FileDelete();
                zFileDelete.setDeleteType(pFolderDelete.getOldId());
                zFileDelete.setActionAdminId(admin.getId());
                zFileDelete.setStatus(FileDeleteStatus.TRUEDELETE.getStatus());
                fileDeleteDao.updateByDeleteType(zFileDelete);
                //日志添加
                adminLog.setId(null);
                adminLog.setActionType(AdminActionType.RECOVERYFILEDELETE.getType());
                adminLog.setOperContent("文件夹删除:"+firstFolderDelete.getFolderName());
                adminLogService.addAdminLog(adminLog);
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    delRealFileByFilePath(doFileEntities, 1);
                }
            });
            thread.start();
        }
    }
    
    @Override
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
        for(String url : fileServices){
            //文件服务器返回
            String result = HttpUtil.post(url + EnumFileServiceOperateType.DELETE_FILE.getUrlAddress(), jsonStr, "", "UTF-8");
        }
        //System.out.println("返回结果：" + result);
        //若code码不为1000，则抛出异常
    }

    @Override
    @Transactional
    public void restoreFiles(String idStr, Admin admin,AdminLog adminLog) {
        String[] idArr = idStr.split(",");
        if(idArr.length > 0){
            for(String id : idArr){
                restoreFile(Integer.valueOf(id),admin,adminLog);
            }
        }
    }
    
    @Override
    @Transactional
    public void restoreFolders(String idStr, Admin admin,AdminLog adminLog) {
        String[] idArr = idStr.split(",");
        if(idArr.length > 0){
            for(String id : idArr){
                restoreFolder(Integer.valueOf(id),admin,adminLog);
            }
        }
    }
    
    //restoreFiles调用，单个文件还原
    private void restoreFile(Integer id,Admin admin,AdminLog adminLog){
        FileDelete fileDelete = fileDeleteDao.selectById(id);
        if(fileDelete!=null){
            //获取文件绝对路径下，所有层级目录名，遍历搜索
            String absolutePath = fileDelete.getAbsolutePath()==null?"":fileDelete.getAbsolutePath();
            String[] parentFolderNameArr = absolutePath.split("/");
            Folder parentFolder = new Folder();
            if(parentFolderNameArr.length > 0 && !StringUtil.nullOrBlank(parentFolderNameArr[0])){
                String folderCodeStr = "";
                for(int i = 0 ; i < parentFolderNameArr.length ; i++){
                    Folder folderTmp = new Folder();
                    folderTmp.setFolderName(parentFolderNameArr[i]);
                    folderTmp.setEmployeeId(fileDelete.getEmployeeId());
                    folderTmp.setFolderCode(folderCodeStr);
                    folderTmp.setFolderCodeLength((i+1)*4);//层级为1，则文件夹编码长度为4；层级为2，则文件夹编码长度为8
                    Folder currentFolder = folderDao.selectRestoreByFolderName(folderTmp);
                    //如果有值，则路径文件夹存在于文件夹表中，继续遍历；如果没值，则从此段开始，创建路径（新建文件夹）
                    if(currentFolder != null){
                        folderCodeStr = currentFolder.getFolderCode();
                        parentFolder = currentFolder;
                    }else{
                        //从当前不存在的层级目录开始，逐条创建层级文件夹
                        for(int j = i;j< parentFolderNameArr.length ; j++){
                            Folder newFolder = new Folder();
                            String folderCode = getFolderCode(parentFolder,fileDelete.getEmployeeId());
                            newFolder.setFolderName(parentFolderNameArr[j]);
                            newFolder.setFolderCode(folderCode);
                            newFolder.setEmployeeId(fileDelete.getEmployeeId());
                            newFolder.setFolderType((fileDelete.getEmployeeId()).equals(SystemContant.MANAGER_EMPLOYEE_ID)?FolderType.COMMON.getType() : FolderType.PERSONAL.getType());
                            newFolder.setValidStatus(ValidStatus.NOMAL.getStatus());
                            newFolder.setParentId(parentFolder.getId());
                            newFolder.setCreateAdmin(fileDelete.getCreateAdmin());
                            newFolder.setUpdateAdmin(fileDelete.getCreateAdmin());
                            folderDao.insert(newFolder);
                            parentFolder = newFolder;
                        }
                        break;
                    }
                }
            }
            //创建文件
            createFile(parentFolder,fileDelete);
            //修改文件删除信息表中 状态
            fileDelete.setStatus(FileDeleteStatus.RESTORE.getStatus());
            fileDelete.setActionAdminId(admin.getId());
            fileDeleteDao.update(fileDelete);
            //日志添加
            adminLog.setId(null);
            adminLog.setActionType(AdminActionType.RECOVERYFILERESTORE.getType());
            adminLog.setOperContent("文件还原:"+fileDelete.getFileName());
            adminLogService.addAdminLog(adminLog);
        }
    }
    
    //restoreFile调用，获取文件夹编码
    private String getFolderCode(Folder parentFolder,Integer employeeId){
        if (parentFolder.getId()==null) { //如果true，代表要创建的目录为首层
            parentFolder.setId(0);
            parentFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        FolderQuery query = new FolderQuery();
        query.setEmployeeId(employeeId);
        query.setParentId(parentFolder.getId());
        List<String> folderCodes = folderDao.selectByParentId(query);
        String folderCode = SerialCodeUtil.getFolderCode(folderCodes,
                parentFolder.getFolderCode());
        return folderCode;
    }
    
    //restoreFile调用，创建文件
    private void createFile(Folder parentFolder,FileDelete fileDelete){
        if (parentFolder.getId()==null) { //如果true，代表要创建的目录为首层
            parentFolder.setId(0);
            parentFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //检测同名并重设名称
        FileQuery fileQuery = new FileQuery();
        fileQuery.setEmployeeId(fileDelete.getEmployeeId());
        fileQuery.setFolderId(parentFolder.getId());
        fileQuery.setQueryName(fileDelete.getFileName());
        setFileName(fileDelete, fileQuery);
        //创建
        File file = new File();
        file.setFileName(fileDelete.getFileName());
        file.setFileSuffix(fileDelete.getFileSuffix());
        file.setFileType(fileDelete.getFileType());
        file.setEmployeeId(fileDelete.getEmployeeId());
        file.setFileSize(fileDelete.getFileSize());
        file.setFolderId(parentFolder.getId());
        file.setFolderCode(parentFolder.getFolderCode());
        file.setFilePath(fileDelete.getFilePath());
        file.setFileVersion(0);
        file.setValidStatus(ValidStatus.NOMAL.getStatus());
        file.setCreateAdmin(fileDelete.getCreateAdmin());
        file.setUpdateAdmin(fileDelete.getUpdateAdmin());
        file.setEncryptStatus(fileDelete.getEncryptStatus());
        file.setEncryptKey(fileDelete.getEncryptKey());
        file.setServerCode(fileDelete.getServerCode());
        fileDao.insert(file);
        //还原文件:文件索引表添加
        fileIndexManager.insertFileIndex(file,
                FileIndexOperType.SAVE.getType());
    }
    
    //restoreFile调用，设置同名的文件名称
    private void setFileName(FileDelete fileDelete, FileQuery fileQuery) {
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
        String fileName = SerialCodeUtil.getFileName(fileDelete.getFileName(),
                oldFileNames);
        fileDelete.setFileName(fileName);
    }

    //restoreFolders调用，单个文件夹还原
    private void restoreFolder(Integer id,Admin admin,AdminLog adminLog){
        FolderDelete folderDelete = folderDeleteDao.selectById(id);
        if(folderDelete != null){
            //获取文件夹绝对路径下，所有层级目录名，遍历搜索
            String absolutePath = folderDelete.getAbsolutePath()==null?"":folderDelete.getAbsolutePath();
            String[] parentFolderNameArr = absolutePath.split("/");
            Folder parentFolder = new Folder();
            if(parentFolderNameArr.length > 0 && !StringUtil.nullOrBlank(parentFolderNameArr[0])){
                String folderCodeStr = "";
                for(int i = 0 ; i < parentFolderNameArr.length ; i++){
                    Folder folderTmp = new Folder();
                    folderTmp.setFolderName(parentFolderNameArr[i]);
                    folderTmp.setEmployeeId(folderDelete.getEmployeeId());
                    folderTmp.setFolderCode(folderCodeStr);
                    folderTmp.setFolderCodeLength((i+1)*4);//层级为1，则文件夹编码长度为4；层级为2，则文件夹编码长度为8
                    Folder currentFolder = folderDao.selectRestoreByFolderName(folderTmp);
                    //如果有值，则路径文件夹存在于文件夹表中，继续遍历；如果没值，则从此段开始，创建路径（新建文件夹）
                    if(currentFolder != null){
                        folderCodeStr = currentFolder.getFolderCode();
                        parentFolder = currentFolder;
                    }else{
                        for(int j = i;j< parentFolderNameArr.length ; j++){
                            Folder newFolder = new Folder();
                            String folderCode = getFolderCode(parentFolder,folderDelete.getEmployeeId());
                            newFolder.setFolderName(parentFolderNameArr[j]);
                            newFolder.setFolderCode(folderCode);
                            newFolder.setEmployeeId(folderDelete.getEmployeeId());
                            newFolder.setFolderType((folderDelete.getEmployeeId()).equals(SystemContant.MANAGER_EMPLOYEE_ID)?FolderType.COMMON.getType() : FolderType.PERSONAL.getType());
                            newFolder.setValidStatus(ValidStatus.NOMAL.getStatus());
                            newFolder.setParentId(parentFolder.getId());
                            newFolder.setCreateAdmin(folderDelete.getCreateAdmin());
                            newFolder.setUpdateAdmin(folderDelete.getCreateAdmin());
                            folderDao.insert(newFolder);
                            parentFolder = newFolder;
                        }
                        break;
                    }
                }
            }
            //进行文件夹及其子目录创建
            createFolder(parentFolder,folderDelete);
            //主文件夹删除信息表，状态修改
            FolderDelete updateFolderDelete = new FolderDelete();
            updateFolderDelete.setId(folderDelete.getId());
            updateFolderDelete.setActionAdminId(admin.getId());
            updateFolderDelete.setStatus(FileDeleteStatus.RESTORE.getStatus());
            folderDeleteDao.update(updateFolderDelete);
            //子文件夹删除信息表，状态修改
            FolderDelete updateZFolderDelete = new FolderDelete();
            updateZFolderDelete.setStatus(FileDeleteStatus.RESTORE.getStatus());
            updateZFolderDelete.setActionAdminId(admin.getId());
            updateZFolderDelete.setDeleteType(folderDelete.getOldId());
            folderDeleteDao.updateByDeleteType(updateZFolderDelete);
            //子文件删除信息表，状态修改
            FileDelete updateZFileDelete = new FileDelete();
            updateZFileDelete.setStatus(FileDeleteStatus.RESTORE.getStatus());
            updateZFileDelete.setActionAdminId(admin.getId());
            updateZFileDelete.setDeleteType(folderDelete.getOldId());
            fileDeleteDao.updateByDeleteType(updateZFileDelete);
            //日志添加
            adminLog.setId(null);
            adminLog.setActionType(AdminActionType.RECOVERYFILERESTORE.getType());
            adminLog.setOperContent("文件夹还原:"+folderDelete.getFolderName());
            adminLogService.addAdminLog(adminLog);
        }
        
    }
    
    //restoreFolder调用，创建文件夹
    private void createFolder(Folder parentFolder,FolderDelete folderDelete){
        if (parentFolder.getId()==null) { //如果true，代表要创建的目录为首层
            parentFolder.setId(0);
            parentFolder.setFolderCode(SystemContant.FIRST_FOLDER_ID);
        }
        //存在同名的文件
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setParentId(parentFolder.getId());
        folderQuery.setEmployeeId(folderDelete.getEmployeeId());
        folderQuery.setQueryName(folderDelete.getFolderName());
        setFolderName(folderDelete, folderQuery);
        //创建主文件夹
        String folderCode = getFolderCode(parentFolder,folderDelete.getEmployeeId());
        Folder folder = new Folder();
        folder.setFolderName(folderDelete.getFolderName());
        folder.setFolderType(folderDelete.getFolderType());
        folder.setFolderCode(folderCode);
        folder.setValidStatus(ValidStatus.NOMAL.getStatus());
        folder.setParentId(parentFolder.getId());
        folder.setEmployeeId(folderDelete.getEmployeeId());
        folder.setCreateAdmin(folderDelete.getCreateAdmin());
        folder.setUpdateAdmin(folderDelete.getUpdateAdmin());
        folderDao.insert(folder);
        //对主还原文件夹下的子文件夹及文件进行还原
        createZFolderAndFile(folder,folderDelete.getOldId());
    }
    
    //createFolder调用，子文件及文件夹创建
    private void createZFolderAndFile(Folder parentFolder,Integer rootOldId){
        //此map存放文件夹删除信息表中，子文件夹old_id与folderdelete对象，用于文件删除信息表的恢复，通过folderId查询新创建的文件夹的信息
        Map<Integer, FolderDelete> fileFindMap = new HashMap<Integer, FolderDelete>();
        //将主目录加入map中
        FolderDelete rootFolderDelete = new FolderDelete();
        rootFolderDelete.setId(parentFolder.getId());
        rootFolderDelete.setOldId(rootOldId);
        rootFolderDelete.setFolderCode(parentFolder.getFolderCode());
        fileFindMap.put(rootFolderDelete.getOldId(), rootFolderDelete);
        //开始创建子文件夹，首先搜索出所有子文件夹
        FolderDeleteQuery folderDeleteQuery = new FolderDeleteQuery();
        folderDeleteQuery.setDeleteType(rootOldId);
        List<FolderDelete> folderDeletes = folderDeleteDao.selectAllByQuery(folderDeleteQuery);
        if(folderDeletes != null && folderDeletes.size() > 0){
            //构造hashMap集合folderDeleteMap，用于存放文件夹删除信息表中，子文件夹parentId与folderdelete集合，进行递归处理
            Map<Integer, List<FolderDelete>> folderDeleteMap = new HashMap<Integer, List<FolderDelete>>();
            for(FolderDelete folderDelete : folderDeletes){
                //文件夹恢复所需map
                List<FolderDelete> folderDeleteTmps = folderDeleteMap.get(folderDelete.getParentId());
                if(folderDeleteTmps == null || folderDeleteTmps.size() == 0){
                    folderDeleteTmps = new ArrayList<FolderDelete>();
                }
                folderDeleteTmps.add(folderDelete);
                folderDeleteMap.put(folderDelete.getParentId(), folderDeleteTmps);
                //文件恢复所需map
                fileFindMap.put(folderDelete.getOldId(), folderDelete);
            }
            //递归创建子文件夹
            createZFolder(folderDeleteMap,fileFindMap, rootOldId, parentFolder);
        }
        //循环创建子文件
        createZFile(fileFindMap, rootOldId);
    }
   
    //createZFolderAndFile调用，通过父文件夹查询子文件夹并逐层递归子树创建文件夹（递归）
    private void createZFolder(Map<Integer, List<FolderDelete>> map,Map<Integer, FolderDelete> fileFindMap, Integer oldId,Folder parentFolder){
        List<FolderDelete> folderDeletes = map.get(oldId);
        if(folderDeletes != null && folderDeletes.size() > 0){
            for(int i = 0;i< folderDeletes.size();i++){
                String folderCode = parentFolder.getFolderCode()+String.format("%04d", i+1); ;
                folderDeletes.get(i).setFolderCode(folderCode);
                folderDeletes.get(i).setParentId(parentFolder.getId());
                Folder folder = insertFolderDelete(folderDeletes.get(i));
                //将修改后的folderDelete(此时包含folderCode及id信息)重新放入map
                FolderDelete folderDelete = fileFindMap.get(folderDeletes.get(i).getOldId());
                if(folderDelete != null){
                    folderDelete.setId(folder.getId());
                    folderDelete.setFolderCode(folderCode);
                    fileFindMap.put(folderDelete.getOldId(),folderDelete);
                }
                //递归子树
                createZFolder(map,fileFindMap, folderDeletes.get(i).getOldId(), folder);
            }
        }
    }
    
    //createZFolderAndFile调用，循环创建子文件
    private void createZFile(Map<Integer, FolderDelete> fileFindMap, Integer rootOldId){
        FileDeleteQuery fileDeleteQuery = new FileDeleteQuery();
        fileDeleteQuery.setDeleteType(rootOldId);
        List<FileDelete> fileDeletes = fileDeleteDao.selectAllByQuery(fileDeleteQuery);
        if(fileDeletes != null && fileDeletes.size() > 0){
            for(FileDelete fileDelete : fileDeletes){
                //在创建子文件夹时已经完成fileFindMap更新，此时的map中id与folderCode均为文件夹表中最新值
                FolderDelete delete = fileFindMap.get(fileDelete.getFolderId());
                if(delete != null){
                    File file = new File();
                    file.setFileName(fileDelete.getFileName());
                    file.setFileSuffix(fileDelete.getFileSuffix());
                    file.setFileType(fileDelete.getFileType());
                    file.setEmployeeId(fileDelete.getEmployeeId());
                    file.setFileSize(fileDelete.getFileSize());
                    file.setFolderId(delete.getId());
                    file.setFolderCode(delete.getFolderCode());
                    file.setFilePath(fileDelete.getFilePath());
                    file.setFileVersion(0);
                    file.setValidStatus(ValidStatus.NOMAL.getStatus());
                    file.setCreateAdmin(fileDelete.getCreateAdmin());
                    file.setUpdateAdmin(fileDelete.getUpdateAdmin());
                    file.setEncryptStatus(fileDelete.getEncryptStatus());
                    file.setEncryptKey(fileDelete.getEncryptKey());
                    file.setServerCode(fileDelete.getServerCode());
                    fileDao.insert(file);
                    //还原文件:文件索引表添加
                    fileIndexManager.insertFileIndex(file,
                            FileIndexOperType.SAVE.getType());
                }
                
            }
        }
    }
    
    //createZFolder调用，创建文件夹
    private Folder insertFolderDelete(FolderDelete folderDelete){
        Folder folder = new Folder();
        folder.setFolderName(folderDelete.getFolderName());
        folder.setFolderType(folderDelete.getFolderType());
        folder.setFolderCode(folderDelete.getFolderCode());
        folder.setValidStatus(ValidStatus.NOMAL.getStatus());
        folder.setParentId(folderDelete.getParentId());
        folder.setEmployeeId(folderDelete.getEmployeeId());
        folder.setCreateAdmin(folderDelete.getCreateAdmin());
        folder.setUpdateAdmin(folderDelete.getUpdateAdmin());
        folderDao.insert(folder);
        return folder;
    }
    
    //restoreFolder调用，设置同名的文件夹名称
    private void setFolderName(FolderDelete folderDelete, FolderQuery folderQuery) {
        //存在同名的文件
        List<Folder> folders = folderDao.selectByQuery(folderQuery);
        List<String> olderFolderName = new ArrayList<String>();
        if (folders != null && folders.size() > 0) {
            for (Folder tmFolder : folders) {
                olderFolderName.add(tmFolder.getFolderName());
            }
        }
        String folderName = SerialCodeUtil.getName(folderDelete.getFolderName(),
                olderFolderName);
        folderDelete.setFolderName(folderName);
    }
}
