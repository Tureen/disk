/*
 * 描述：工作组业务层
 * 创建人：ming.zhu
 * 创建时间：2017-02-12
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.disk.query.WorkgroupQuery;

public interface IWorkgroupService {

    /**
     * 条件查询
     * @param query
     * @return  
     * WorkgroupQuery 
     * @exception
     */
    WorkgroupQuery queryWorkgroup(WorkgroupQuery query);
    
    int quertCount(WorkgroupQuery query);
    
    /**
     * 获取工作组
     * @param workgroupId
     * @return  
     * Workgroup 
     * @exception
     */
    Workgroup getWorkgroup(Integer workgroupId);
    
    /**
     * 保存或修改
     * @param workgroup
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int saveOrEdit(Workgroup workgroup,Employee employee);
    
    /**
     * 转让工作组
     * @param workgroupId
     * @param employee 转让者
     * @param bEmployee 被转让者
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int transferWorkgroup(Integer workgroupId, Employee employee, Employee bEmployee);
    
    /**
     * 保存
     * @param workgroup
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addWorkgroup(Workgroup workgroup);
    
    /**
     * 修改
     * @param workgroup
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int upWorkgroup(Workgroup workgroup);
    
    /**
     * 删除
     * @param workgroupId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delWorkgroup(Integer workgroupId);
    
    /**
     * 批量删除
     * @param ids
     * @param employeeId
     * @return  
     * boolean 
     * @exception
     */
    @Transactional
    boolean delBatchWorkgroup(List<String> ids,Employee employee);
    
    /**
     * 获取参与的工作组id集合
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> getJoinWorkgroup(Integer employeeId);
    
    /**
     * 获取拥有的工作组id集合
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> getHasWorkgroup(Integer employeeId);
    
    /**
     * 获取该工作组员工id集合
     * @param workgroupId
     * @return  
     * List<WorkgroupEmployee> 
     * @exception
     */
    List<WorkgroupEmployee> getWorkgroupEmployeeIds(Integer workgroupId);
    
    /**
     * 获取该工作组员工对象集合
     * @param workgroupId
     * @return  
     * List<WorkgroupEmployee> 
     * @exception
     */
    List<WorkgroupEmployee> getWorkgroupEmployee(Integer workgroupId);
    
    /**
     * 添加员工进工作组
     * @param workgroupId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int saveWorkgroupEmployee(Integer workgroupId, Employee employee);
    
    /**
     * 批量添加员工进工作组
     * saveBatchWorkgroupEmployee(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param workgroupId
     * @param ids
     * @return  
     * int 
     * @exception
     */
    @Transactional
    boolean saveBatchWorkgroupEmployee(Integer workgroupId, List<String> ids, Employee employee);
    
    /**
     * 获取所有开启的工作组
     * @return  
     * List<Workgroup> 
     * @exception
     */
    List<Workgroup> getAllWorkgroup();
}
