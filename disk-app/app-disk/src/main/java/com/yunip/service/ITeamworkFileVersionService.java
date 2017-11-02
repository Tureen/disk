/*
 * 描述：协作文件版本业务层
 * 创建人：ming.zhu
 * 创建时间：2017-03-10
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;

public interface ITeamworkFileVersionService {

    /**
     * 获取当前协作文件的历史版本列表
     * @param query   查询对象
     * @return  
     * List<TeamworkFileVersion> 
     * @exception
     */
    List<TeamworkFileVersion> getFileVersions(TeamworkFileVersionQuery query);
    
    /****
     * 恢复文件系统中当前版本
     * @param  fileVersion 文件版本
     * void 
     * @exception
     */
    @Transactional
    void regainFile(TeamworkFileVersion fileVersion);
}
