/*
 * 描述：工作组员工关联Dao
 * 创建人：ming.zhu
 * 创建时间：2017-01-18
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.WorkgroupEmployee;

@Repository("iWorkgroupEmployeeDao")
public interface IWorkgroupEmployeeDao extends IBaseDao<WorkgroupEmployee>{

    /**
     * 查找单条根据组合主键
     * @param workgroupEmployee
     * @return  
     * WorkgroupEmployee 
     * @exception
     */
    WorkgroupEmployee selectByPrimaryKey(WorkgroupEmployee workgroupEmployee);
    
    /**
     * 查询多条工作组id,根据关联员工id
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectByEmployeeId(Integer employeeId);
    
    /**
     * 查找多条员工id根据工作组
     * @param id
     * @return  
     * List<WorkgroupEmployee> 
     * @exception
     */
    List<WorkgroupEmployee> selectByWorkgroupId(Integer workgroupId);
    
    List<WorkgroupEmployee> selectEmployeeByWorkgroupId(Integer workgroupId);
    
    /**
     * 删除单条根据组合主键
     * @param workgroupEmployee
     * @return  
     * int 
     * @exception
     */
    int delByPrimaryKey(WorkgroupEmployee workgroupEmployee);
    
    /**
     * 删除多条根据关联员工
     * @param employeeId
     * @return  
     * int 
     * @exception
     */
    int delByEmployeeId(Integer employeeId);
    
    /**
     * 删除多条根据工作组id
     * @param workgroupId
     * @return  
     * int 
     * @exception
     */
    int delByWorkgroupId(Integer workgroupId);
}
