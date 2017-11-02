/*
 * 描述：〈文件删除信息对象Service〉
 * 创建人：ming.zhu
 * 创建时间：2016-11-17
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.disk.query.FileDeleteQuery;
import com.yunip.model.disk.query.FolderDeleteQuery;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.log.AdminLog;
import com.yunip.model.user.Admin;

public interface IFileDeleteService {

    /**
     * 根据条件查询文件删除信息的列表
     * @param query
     * @return  
     * FileDeleteQuery 
     * @exception
     */
    FileDeleteQuery getFileDeletesByQuery(FileDeleteQuery query);
    
    /**
     * 根据条件查询文件夹删除信息的列表
     * @param query
     * @return  
     * FolderDeleteQuery 
     * @exception
     */
    FolderDeleteQuery getFolderDeletesByQuery(FolderDeleteQuery query);
    
    /**
     * 删除文件
     * @param idStr  
     * void 
     * @exception
     */
    @Transactional
    void deleteFile(String idStr,Admin admin,AdminLog adminLog);
    
    /**
     * 删除文件夹
     * @param idStr  
     * void 
     * @exception
     */
    @Transactional
    void deleteFolder(String idStr,Admin admin,AdminLog adminLog);

    /**
     * 删除真实物理地址
     * @param fileEntities
     * @param employeeId  
     * void 
     * @exception
     */
    void delRealFileByFilePath(List<FileEntity> fileEntities, Integer employeeId);
    
    /**
     * 还原文件
     * @param idStr
     * @param admin  
     * void 
     * @exception
     */
    @Transactional
    void restoreFiles(String idStr,Admin admin,AdminLog adminLog);
    
    /**
     * 还原文件夹
     * @param idStr
     * @param admin  
     * void 
     * @exception
     */
    @Transactional
    void restoreFolders(String idStr,Admin admin,AdminLog adminLog);
}
