package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.SystemContant;
import com.yunip.mapper.sys.IBackupDao;
import com.yunip.model.sys.Backup;
import com.yunip.model.sys.query.BackupQuery;
import com.yunip.service.IBackupService;
import com.yunip.utils.pwd.Des;

@Service("iBackupService")
public class BackupServiceImpl implements IBackupService{
    
    @Resource(name = "iBackupDao")
    private IBackupDao backupDao;

    @Override
    @Transactional
    public int addBackup(Backup backup) {
        return backupDao.insert(backup);
    }

    @Override
    @Transactional
    public int delBackup(Integer id) {
        return backupDao.delById(id);
    }

    @Override
    public BackupQuery queryBackup(BackupQuery query) {
        List<Backup> list = backupDao.selectByQuery(query);
        int count = backupDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        desEncrypt(query);
        return query;
    }
    
    /**
     * 下载地址参数加密
     * @param query  
     * void 
     * @exception
     */
    private void desEncrypt(BackupQuery query){
        if(query != null && query.getList() != null && query.getList().size() > 0){
            for(Backup backup : query.getList()){
                String path = backup.getSqlPath();
                String name = backup.getSqlName();
                String params = path + "|" + name;
                Des des = new Des();
                params = des.strEnc(params, SystemContant.DATABASEKEY, null, null);
                backup.setParams(params);
            }
        }
    }

    @Override
    public Backup getBackupById(Integer id) {
        return backupDao.selectById(id);
    }

}
