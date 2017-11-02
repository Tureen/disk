package com.yunip.mapper.sys;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.sys.Backup;
import com.yunip.model.sys.query.BackupQuery;

@Repository("iBackupDao")
public interface IBackupDao extends IBaseDao<Backup>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Backup> selectByQuery(BackupQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(BackupQuery query);
}
