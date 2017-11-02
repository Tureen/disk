package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.log.AdminLog;
import com.yunip.model.log.query.AdminLogQuery;

/**
 * @author ming.zhu
 * 管理员or员工操作日志service
 */
public interface IAdminLogService {

    /**
     * 添加日志
     * @param adminLog
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addAdminLog(AdminLog adminLog);
    
    /**
     * 批量删除日志
     * @param ids
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delAdminLog(List<Integer> ids);
    
    /**
     * 清空日志
     * @param isAdmin 判断是前台or后台
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int clearAdminLog(Integer isAdmin);
    
    /**
     * 条件查询列表
     * @param query
     * @return  
     * AdminLogQuery 
     * @exception
     */
    AdminLogQuery queryAdminLog(AdminLogQuery query);
}
