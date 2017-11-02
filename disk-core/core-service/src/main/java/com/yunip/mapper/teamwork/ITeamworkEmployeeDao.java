/*
 * 描述：协作信息员工关联Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.mapper.teamwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.TeamworkEmployee;

@Repository("iTeamworkEmployeeDao")
public interface ITeamworkEmployeeDao extends IBaseDao<TeamworkEmployee>{

    /**
     * 查询单条，根据主键
     * @param teamworkEmployee
     * @return  
     * TeamworkEmployee 
     * @exception
     */
    TeamworkEmployee selectByPrimaryKey(TeamworkEmployee teamworkEmployee);
    
    /**
     * 查询多条协作id,根据关联员工id
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectByEmployeeId(Integer employeeId);
    
    /**
     * 删除单条，根据主键
     * @param teamworkEmployee
     * @return  
     * int 
     * @exception
     */
    int delByPrimaryKey(TeamworkEmployee teamworkEmployee);
    
    /**
     * 删除多条，根据协作id
     * @param teamworkId
     * @return  
     * int 
     * @exception
     */
    int delByTeamworkId(Integer teamworkId);
    
    /**
     * 查找多条员工id根据协作
     * @param id
     * @return  
     * List<TeamworkEmployee> 
     * @exception
     */
    List<TeamworkEmployee> selectByTeamworkId(Integer teamworkId);
}
