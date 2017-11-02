/*
 * 描述：协作信息Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.mapper.teamwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.SimpleTeamworkData;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.query.TeamworkQuery;

@Repository("iTeamworkDao")
public interface ITeamworkDao extends IBaseDao<Teamwork>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Teamwork> selectByQuery(TeamworkQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TeamworkQuery query);
    
    /**
     * 查找多条id根据所属员工id
     * @param id
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectByEmployee(Integer employeeId);
 
    /**
     * 批量删除，根据employeeId和主键id
     * @param teamwork
     * @return  
     * int 
     * @exception
     */
    int batchDelete(Teamwork teamwork);
    
    /**
     * 简易列表
     * @param query
     * @return  
     * List<SimpleTeamworkData> 
     * @exception
     */
    List<SimpleTeamworkData> selectSimpleDatas(TeamworkQuery query);
}
