/*
 * 描述：〈文件版本服务实现类〉
 * 创建人：can.du
 * 创建时间：2016-5-11
 */
package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.manage.FileIndexManager;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.query.FileVersionQuery;
import com.yunip.service.IFileVersionService;

/**
 * 文件版本服务实现类
 */
@Service("iFileVersionService")
public class FileVersionServiceImpl implements IFileVersionService{
    
    @Resource(name = "iFileVersionDao")
    private IFileVersionDao fileVersionDao;
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @Resource(name = "fileIndexManager")
    private FileIndexManager fileIndexManager;

    @Override
    public List<FileVersion> getFileVersions(FileVersionQuery query) {
        query.setPageSize(Integer.MAX_VALUE);
        List<FileVersion> fileVersions = fileVersionDao.selectByQuery(query);
        return fileVersions;
    }

    @Override
    public void regainFile(FileVersion fileVersion) {
        fileVersion = fileVersionDao.selectById(fileVersion.getId());
        File file = fileDao.selectById(fileVersion.getFileId());
        //如果版本中没有当前文件的,则添加当前文件到此版本
        FileVersionQuery query = new FileVersionQuery();
        query.setFileId(file.getId());
        query.setPageSize(1);
        query.setDesc(true);
        query.setFileVersion(file.getFileVersion());
        List<FileVersion> list = fileVersionDao.selectByQuery(query);
        if(list == null || list.size() == 0){
            FileVersion oldFileVersion = new FileVersion();
            oldFileVersion.setFileId(file.getId());
            oldFileVersion.setFileName(file.getFileName());
            oldFileVersion.setFileType(file.getFileType());
            oldFileVersion.setFileSize(file.getFileSize());
            oldFileVersion.setFilePath(file.getFilePath());
            oldFileVersion.setFileVersion(file.getFileVersion());
            oldFileVersion.setValidStatus(file.getValidStatus());
            oldFileVersion.setEmployeeId(file.getEmployeeId());
            oldFileVersion.setCreateAdmin(file.getCreateAdmin());
            oldFileVersion.setCreateTime(file.getCreateTime());
            oldFileVersion.setUpdateAdmin(file.getUpdateAdmin());
            oldFileVersion.setUpdateTime(file.getUpdateTime());
            oldFileVersion.setFolderCode(file.getFolderCode());
            oldFileVersion.setEncryptStatus(file.getEncryptStatus());
            oldFileVersion.setEncryptKey(file.getEncryptKey());
            oldFileVersion.setServerCode(file.getServerCode());
            fileVersionDao.insert(oldFileVersion);
            //当前版本文件移除:文件索引表数据添加
            File fileTmp = new File();
            fileTmp.setId(oldFileVersion.getFileId());
            fileTmp.setFilePath(oldFileVersion.getFilePath());
            fileTmp.setFileName(oldFileVersion.getFileName());
            fileTmp.setFileType(oldFileVersion.getFileType());
            fileTmp.setEmployeeId(oldFileVersion.getEmployeeId());
            fileIndexManager.insertFileIndex(fileTmp, FileIndexOperType.DEL.getType());
        }
        //文件恢复
        file.setFileVersion(fileVersion.getFileVersion());
        file.setFilePath(fileVersion.getFilePath());
        file.setCreateTime(fileVersion.getCreateTime());
        file.setFileSize(fileVersion.getFileSize());
        file.setFileVersion(fileVersion.getFileVersion());
        file.setEncryptStatus(fileVersion.getEncryptStatus());
        file.setEncryptKey(fileVersion.getEncryptKey());
        file.setServerCode(fileVersion.getServerCode());
        fileDao.update(file);
        //对应版本删除
        fileVersionDao.delById(fileVersion.getId());
        //历史版本文件写入当前文件:文件索引表数据添加
        fileIndexManager.insertFileIndex(file, FileIndexOperType.SAVE.getType());
    }

}
