/*
 * 描述：工作组申请业务层
 * 创建人：ming.zhu
 * 创建时间：2017-02-12
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.query.WorkgroupApplyQuery;

public interface IWorkgroupApplyService {

    /**
     * 条件查询
     * @param query
     * @return  
     * WorkgroupApplyQuery 
     * @exception
     */
    WorkgroupApplyQuery queryWorkgroupApply(WorkgroupApplyQuery query);
    
    int queryCount(WorkgroupApplyQuery query);
    
    /**
     * 保存申请加入信息
     * @param employee
     * @param workgroupId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int saveWorkgroupApply(Employee employee, Integer workgroupId);
    
    /**
     * 批量保存申请加入信息
     * @param employee
     * @param workgroupIds
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int saveBatchWorkgroupApply(Employee employee, List<String> workgroupIds); 
    
    /**
     * 退出工作组
     * @param employee
     * @param workgroupIds
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int quitWorkgroup(Employee employee, Integer workgroupId);
    
    /**
     * 批量退出工作组
     * @param employee
     * @param workgroupIds
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int quitBatchWorkgroup(Employee employee, List<String> workgroupIds);
    
    /**
     * 审核，改变状态及工作组表
     * @param ids
     * @param workgroupApplyStatus
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int examinationWorkgroupApply(List<String> ids, Integer workgroupApplyStatus, Employee employee);
}
