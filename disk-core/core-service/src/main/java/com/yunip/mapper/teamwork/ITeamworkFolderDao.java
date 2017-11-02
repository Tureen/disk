/*
 * 描述：协作文件夹基本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-27
 */
package com.yunip.mapper.teamwork;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.query.TeamworkFolderQuery;

@Repository("iTeamworkFolderDao")
public interface ITeamworkFolderDao extends IBaseDao<TeamworkFolder>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TeamworkFolder> selectByQuery(TeamworkFolderQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TeamworkFolderQuery query);
    
    /***
     * 根据父文件夹ID查询子文件夹集合
     * @param query
     * @return  
     * List<String> 
     * @exception
     */
    List<String> selectByParentId(TeamworkFolderQuery query);
    
    /**
     * 删除整个文件夹
     * @param teamworkFolder
     * @return  
     * int 
     * @exception
     */
    int delByCode(TeamworkFolder teamworkFolder);
    
    /**
     * 查询员工所在协作的所有文件夹
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectFolderByEmployeeId(Integer employeeId);
    
    /**
     * 根据父文件id查询主键
     * @param parentId
     * @return  
     * List<TeamworkFile> 
     * @exception
     */
    List<TeamworkFolder> selectAllByParentId(Integer parentId);
    
    /**
     * 删除多条，根据协作id集
     * @param teamworkFolder
     * @return  
     * TeamworkFolder 
     * @exception
     */
    int delByTeamworkIds(TeamworkFolder teamworkFolder);
}
