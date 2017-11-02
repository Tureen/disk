package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.sys.Backup;
import com.yunip.model.sys.query.BackupQuery;

/**
 * 数据库备份service
 */
public interface IBackupService {

    /**
     * 添加数据库备份记录
     * @param backup
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addBackup(Backup backup);
    
    /**
     * 根据id获取备份
     * @param id
     * @return  
     * Backup 
     * @exception
     */
    Backup getBackupById(Integer id);
    
    /**
     * 删除数据库备份记录,根据id
     * @param id
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delBackup(Integer id);
    
    /**
     * 根据条件获取备份数据
     * @param query
     * @return  
     * BackupQuery 
     * @exception
     */
    BackupQuery queryBackup(BackupQuery query);
}
