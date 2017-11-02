/*
 * 描述：协作记录信息Dao
 * 创建人：ming.zhu
 * 创建时间：2017-03-16
 */
package com.yunip.mapper.teamwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.model.teamwork.query.TeamworkMessageQuery;

@Repository("iTeamworkMessageDao")
public interface ITeamworkMessageDao extends IBaseDao<TeamworkMessage>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TeamworkMessage> selectByQuery(TeamworkMessageQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TeamworkMessageQuery query);
    
    /**
     *  
     * @param id
     * @return  
     * List<TeamworkMessage> 
     * @exception
     */
    List<TeamworkMessage> selectObjById(Integer id);
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TeamworkMessage> selectAjaxByQuery(TeamworkMessageQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectAjaxCountByQuery(TeamworkMessageQuery query);
 
    /**
     * 查询根据fileId和msgType
     * @param teamworkMessage
     * @return  
     * List<TeamworkMessage> 
     * @exception
     */
    List<TeamworkMessage> selectByFileId(TeamworkMessage teamworkMessage);
}
