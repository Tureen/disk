/*
 * 描述：标签业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-11
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.disk.FileSign;
import com.yunip.model.disk.Sign;
import com.yunip.model.disk.query.FileSignQuery;
import com.yunip.model.disk.query.SignQuery;
import com.yunip.model.disk.support.SignReq;

public interface ISignService {

    /**
     * 添加标签
     * @param sign
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addSign(Sign sign);
    
    /**
     * 编辑标签
     * @param sign
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int updateSign(Sign sign);
    
    /**
     * 删除，根据id
     * @param id
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delSign(Integer id);
    
    /**
     * 删除，根据ids
     * @param ids
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delSign(List<Integer> ids);
    
    /**
     * 添加标签以及关联(批量，先删后加)
     * @param fileSign
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addFileSign(SignReq signreq);
    
    /**
     * 根据条件，查找标签
     * @param query
     * @return  
     * SignQuery 
     * @exception
     */
    SignQuery querySign(SignQuery query);
    
    /**
     * 根据员工id，查找标签
     * @param employeeId
     * @return  
     * List<Sign> 
     * @exception
     */
    List<Sign> getSignByEmployeeId(Integer employeeId);
    
    /**
     * 根据文件id，查询标签
     * @param fileId
     * @return  
     * List<Sign> 
     * @exception
     */
    List<Sign> getSignByFileId(Integer fileId);
    
    /**
     * 根据条件，查询文件与标签相关信息
     * 
     * @param query
     * @return  
     * FileSignQuery 
     * @exception
     */
    FileSignQuery queryFileSign(FileSignQuery query);
    
    /**
     * 删除标签文件管理
     * @param fileSign  
     * void 
     * @exception
     */
    void delFileSign(List<FileSign> fileSigns);
}
