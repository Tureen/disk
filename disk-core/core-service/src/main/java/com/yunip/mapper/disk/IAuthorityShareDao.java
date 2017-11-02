/*
 * 描述：用户动态基本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.AuthorityShareQuery;

@Repository("iAuthorityShareDao")
public interface IAuthorityShareDao extends IBaseDao<AuthorityShare>{
    
    List<AuthorityShare> selectByQuery(AuthorityShareQuery query);

    /**
     * 根据条件查询，文件夹列表
     * @param query 查询对象
     * @return
     */
    List<AuthorityShare> selectOpenAuthById(AuthorityShareQuery query);
    
    /**
     * 我的分享
     * @param query 查询对象
     * @return
     */
    List<AuthorityShare> selectShareByQuery(AuthorityShareQuery query);
    
    /**
     * 被分享
     * @param query 查询对象
     * @return
     */
    List<AuthorityShare> selectBShareByQuery(AuthorityShareQuery query);
    
    /**
     * 删除单条（根据openId，openType）
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    int delByKeyId(AuthorityShare authorityShare);
    
    /**
     * 删除，根据shareId(shareId, shareType)
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    int delByShareId(AuthorityShare authorityShare);
    
    /**
     * 批量删除
     * @param ids
     * @return  
     * int 
     * @exception
     */
    int delBatchById(List<Integer> ids); 
    
    /**
     * 删除,根据文件夹code
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    int delByCode(AuthorityShare authorityShare);
    
    /**
     * 我的文件夹分享，文件夹列表搜素
     * @param query
     * @return  
     * List<Folder> 
     * @exception
     */
    List<Folder> selectKeyFolderByQuery(AuthorityShareQuery query);
    
    /**
     * 我的文件夹分享，文件夹下文件搜素
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> selectKeyFileByQuery(AuthorityShareQuery query);
    
    /**
     * 被分享文件夹，文件夹列表搜素
     * @param query
     * @return  
     * List<Folder> 
     * @exception
     */
    List<Folder> selectBKeyFolderByQuery(AuthorityShareQuery query);
    
    /**
     * 被分享文件夹，文件夹下文件搜素
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> selectBKeyFileByQuery(AuthorityShareQuery query);
    
    /**
     * 根据文件id与员工,查询被分享人
     * selectOpenAuthById(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param query
     * @return  
     * List<AuthorityShare> 
     * @exception
     */
    List<AuthorityShare> selectOpenAuth(AuthorityShareQuery query);
    
    /**
     * 查询文件对应权限及关联部门名和员工名
     * @param query
     * @return  
     * List<AuthorityShare> 
     * @exception
     */
    List<AuthorityShare> selectRelationNameByOpenId(AuthorityShareQuery query);
    
    /**
     * 根据工作组id查询所有关联文件or文件夹的分享
     * @param workgroupId
     * @return  
     * List<AuthorityShare> 
     * @exception
     */
    List<AuthorityShare> selectAllAuthorityByWorkgroupId(Integer workgroupId);
}
