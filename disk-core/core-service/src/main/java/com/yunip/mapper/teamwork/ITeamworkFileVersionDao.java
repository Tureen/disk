/*
 * 描述：协作文件版本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.mapper.teamwork;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;

@Repository("iTeamworkFileVersionDao")
public interface ITeamworkFileVersionDao extends IBaseDao<TeamworkFileVersion>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TeamworkFileVersion> selectByQuery(TeamworkFileVersionQuery query);

    /**
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TeamworkFileVersionQuery query);
    
    /**
     * 删除多条，根据关联的协作文件id
     * @param fileId
     * @return  
     * int 
     * @exception
     */
    int delByFileId(int fileId);
    
    /**
     * 删除,根据文件夹code
     * @param teamworkFileVersion
     * @return  
     * int 
     * @exception
     */
    int delByCode(TeamworkFileVersion teamworkFileVersion);
    
    /**
     * 获取总共所有使用的文件大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    Long selectSumUseSpace(Integer employeeId);
    
    /**
     * 获取历史文件路径, 根据协作id集
     * @param teamworkFileVersion
     * @return  
     * Set<String> 
     * @exception
     */
    Set<String> selectPathByTeamworkIds(TeamworkFileVersion teamworkFileVersion);
    
    /**
     * 删除多条，根据协作id集
     * @param teamworkFileVersion
     * @return  
     * TeamworkFileVersion 
     * @exception
     */
    int delByTeamworkIds(TeamworkFileVersion teamworkFileVersion);
}
