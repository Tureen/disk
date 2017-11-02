/*
 * 描述：协作员工关联业务层
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.TeamworkEmployee;

public interface ITeamworkEmployeeService {

    /**
     * 退出协作
     * @param employee
     * @param teamworkId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int quitTeamwork(Employee employee, Integer teamworkId);
    
    /**
     * 批量退出协作
     * @param employee
     * @param teamworkIds
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int quitBatchTeamwork(Employee employee, List<String> teamworkIds);
    
    /**
     * 获取该协作员工id集合
     * @param teamworkId
     * @return  
     * List<TeamworkEmployee> 
     * @exception
     */
    List<TeamworkEmployee> getTeamworkEmployeeIds(Integer teamworkId);
    
    /**
     * 获取参与的工作组id集合
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> getJoinTeamwork(Integer employeeId);
    
    /**
     * 批量添加员工进协作
     * @param teamworkId
     * @param ids
     * @return  
     * int 
     * @exception
     */
    @Transactional
    boolean saveBatchTeamworkEmployee(Integer teamworkId, List<String> ids, Employee employee);
    
    /**
     * 获取协作与员工关联对象
     * @param teamworkEmployee
     * @return  
     * TeamworkEmployee 
     * @exception
     */
    TeamworkEmployee getTeamworkEmployee(TeamworkEmployee teamworkEmployee);
}
