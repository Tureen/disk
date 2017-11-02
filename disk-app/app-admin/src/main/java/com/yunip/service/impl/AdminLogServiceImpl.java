package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.log.IAdminLogDao;
import com.yunip.model.log.AdminLog;
import com.yunip.model.log.query.AdminLogQuery;
import com.yunip.service.IAdminLogService;

/**
 * @author ming.zhu
 * 管理员or员工操作日志Service
 */
@Service("iAdminLogService")
public class AdminLogServiceImpl implements IAdminLogService {

    @Resource(name = "iAdminLogDao")
    private IAdminLogDao adminLogDao;

    @Override
    @Transactional
    public int addAdminLog(AdminLog adminLog) {
        return adminLogDao.insert(adminLog);
    }

    @Override
    @Transactional
    public int delAdminLog(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            for(Integer id : ids){
                adminLogDao.delById(id);
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public int clearAdminLog(Integer isAdmin) {
        return adminLogDao.delByIsAdmin(isAdmin);
    }

    @Override
    public AdminLogQuery queryAdminLog(AdminLogQuery query) {
        List<AdminLog> list = adminLogDao.selectByQuery(query);
        int count = adminLogDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

}
