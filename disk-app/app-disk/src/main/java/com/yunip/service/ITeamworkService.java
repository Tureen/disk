/*
 * 描述：协作业务层
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.query.TeamworkQuery;

public interface ITeamworkService {

    /**
     * 条件查询
     * @param query
     * @return  
     * TeamworkQuery 
     * @exception
     */
    TeamworkQuery queryTeamwork(TeamworkQuery query);
    
    /**
     * 保存或修改
     * @param workgroup
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int saveOrEdit(Teamwork teamwork,Employee employee);
    
    /**
     * 获取协作信息
     * @param teamworkId
     * @return  
     * Teamwork 
     * @exception
     */
    Teamwork getTeamwork(Integer teamworkId);
    
    /**
     * 批量删除
     * @param ids
     * @param employeeId
     * @return  
     * boolean 
     * @exception
     */
    @Transactional
    boolean delBatchTeamwork(List<String> ids,Employee employee);
    
    /**
     * 获取拥有的协作id集合
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> getHasTeamwork(Integer employeeId);
    
}
