/*
 * 描述：后台管理员or前台员工日志信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-6-27
 */
package com.yunip.mapper.log;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.log.AdminLog;
import com.yunip.model.log.query.AdminLogQuery;

@Repository("iAdminLogDao")
public interface IAdminLogDao extends IBaseDao<AdminLog>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<AdminLog> selectByQuery(AdminLogQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(AdminLogQuery query);
    
    /**
     * 清空日志
     * @param isAdmin  判断是前台or后台
     * void 
     * @exception
     */
    int delByIsAdmin(Integer isAdmin);
}
