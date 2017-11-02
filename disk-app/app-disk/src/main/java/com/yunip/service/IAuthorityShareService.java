/*
 * 描述：用户分享权限业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.support.AuthHelper;
import com.yunip.model.disk.support.AuthMap;
import com.yunip.model.disk.support.AuthRelationHelper;
import com.yunip.model.disk.support.AuthReq;
import com.yunip.model.disk.support.FolderReq;

public interface IAuthorityShareService {

    /**
     * 添加分享文件
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addAuthorityShare(AuthorityShare authorityShare);

    /**
     * 修改分享文件
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int updateAuthorityShare(AuthorityShare authorityShare);

    /**
     * 获取分享出去的
     * @param query
     * @return  
     * AuthorityShareQuery 
     * @exception
     */
    FolderReq getShare(AuthorityShareQuery query);

    /**
     * 获取被分享的
     * @param query
     * @return  
     * AuthorityShareQuery 
     * @exception
     */
    FolderReq getBShare(AuthorityShareQuery query,Integer employeeId);

    /**
     * 条件查询，指定父id文件夹的权限（根据用户所获得分享文件夹的唯一性）
     * @param query
     * @return  
     * AuthMap 
     * @exception
     */
    AuthMap getOpenAuth(AuthorityShareQuery query);

    /**
     * 级联删除文件or文件夹，批量(根据 ：id，openId，openType,folder.folderCode,folder.employeeId)
     * @param authorityShares
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delCascadeAuthorityShare(List<AuthorityShare> authorityShares);
    
    /**
     * 根据接收者id与文件id查询权限
     * @param query
     * @return  
     * int 
     * @exception
     */
    int getOpenAuthById(Employee employee,Integer openId,Integer openType);
    
    /**
     * 搜索，我的分享文件
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> getKeyFile(AuthorityShareQuery query);
    
    /**
     * 搜索，我的分享文件夹
     * @param query
     * @return  
     * Folder 
     * @exception
     */
    Folder getKeyFolder(AuthorityShareQuery query);
    
    /**
     * 搜索，被分享文件
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> getBKeyFile(AuthorityShareQuery query);
    
    /**
     * 搜索，被分享文件夹
     * @param query
     * @return  
     * Folder 
     * @exception
     */
    Folder getBKeyFolder(AuthorityShareQuery query);
    
    /**
     * 删除分享，根据openId,openType
     * @param openId
     * @param openType
     * @param employee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delAuthorityShare(Integer openId,Integer openType, Employee employee);
    
    /**
     * 删除分享(批量)，根据openId,openType
     * @param authReq
     * @param employee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delAuthorityShareBatch(AuthReq authReq, Employee employee);
    
    /**
     * 删除分享(批量)
     * @param ids
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delAuthorityShareById(List<AuthorityShare> authorityShares);
    
    /**
     * 删除分享
     * @param id
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delAuthorityShareById(Integer id);
    
    /**
     * 添加分享文件夹或者文件
     * @param authorityShare
     * @param employee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addAuthorityShare(AuthHelper authHelper, Employee employee);
    
    /****
     * 添加多个文件或者文件夹分享
     * @param authHelper
     * @param employee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addMoreAuthorityShare(AuthHelper authHelper, Employee employee);
    
    /***
     * 合并同类型的数据
     * @param authHelpers
     * @return  
     * List<AuthHelper> 
     * @exception
     */
    List<AuthHelper> mergeAuthHelper(List<AuthHelper> authHelpers);
    
    /***
     * 根据辅助对象查询相关选中的记录
     * @param authHelper
     * @return  
     * AuthHelper 
     * @exception
     */
    AuthHelper getAuthHelperByAuth(AuthHelper authHelper, Employee employee);
    
    /***
     * 检查分享权限
     * @param authHelper
     * @param employee  
     * void 
     * @exception
     */
    void checkShareAuth(AuthHelper authHelper, Employee employee);
    
    /***
     * 处理分享数据
     * @param authHelper 分享数据对象
     * @param employee   分享人
     * void 
     * @exception
     */
    @Transactional
    void shareDataMessage(List<AuthHelper> authHelpers, Employee employee);
    
    /**
     * 删除单条（根据openId，openType）
     * @param authorityShare
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delByAuthorityShare(AuthorityShare authorityShare);
    
    Map<String, AuthRelationHelper> getAuthorityShareRelationName(AuthorityShareQuery authorityShareQuery);
    
    /**
     * 删除工作组时的分享修改
     * @param workgroupId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delWorkgroupAuthorityShare(Integer workgroupId);
}
