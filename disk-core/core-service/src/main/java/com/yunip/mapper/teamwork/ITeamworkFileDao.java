/*
 * 描述：协作文件基本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-23
 */
package com.yunip.mapper.teamwork;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.query.TeamworkFileQuery;

@Repository("iTeamworkFileDao")
public interface ITeamworkFileDao extends IBaseDao<TeamworkFile>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TeamworkFile> selectByQuery(TeamworkFileQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TeamworkFileQuery query);
    
    /**
     * 删除整个文件夹
     * @param teamworkFile
     * @return  
     * int 
     * @exception
     */
    int delByCode(TeamworkFile teamworkFile);
    
    /**
     * 查询员工所在协作的所有文件
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectFileByEmployeeId(Integer employeeId);
    
    /**
     * 根据父文件id查询主键
     * @param folderId
     * @return  
     * List<TeamworkFile> 
     * @exception
     */
    List<TeamworkFile> selectAllByFolderId(Integer folderId);
    
    /**
     * 获取总共所有使用的文件大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    Long selectSumUseSpace(Integer employeeId);
    
    /**
     * 获取文件路径, 根据协作id集
     * @param teamworkFile
     * @return  
     * Set<String> 
     * @exception
     */
    Set<String> selectPathByTeamworkIds(TeamworkFile teamworkFile);
    
    /**
     * 删除多条，根据协作id集
     * @param teamworkFile
     * @return  
     * TeamworkFile 
     * @exception
     */
    int delByTeamworkIds(TeamworkFile teamworkFile);
}
