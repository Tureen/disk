/*
 * 描述：提取码业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.TakeCodeQuery;

public interface ITakeCodeService {

    /**
     * 添加提取码
     * @param takeCode
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addTakeCode(TakeCode takeCode, Employee employee);

    /**
     * 添加多条提取码
     * @param takeCode
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addTakeCodeMore(TakeCode takeCode,Employee employee,String[] fileIds);

    /**
     * 删除提取码
     * @param id
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delTakeCode(Integer id);

    /**
     * 删除提取码(批量)
     * @param id
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delTakeCodeBatch(List<Integer> ids);
    
    /**
     * 删除提取码，根据takeCode(批量)
     * @param takeCodes
     * @param employees
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int delTakeCodeBatchByTakeCode(List<TakeCode> takeCodes, Employee employee);

    /**
     * 条件查询列表
     * @param query
     * @return  
     * TakeCodeQuery 
     * @exception
     */
    TakeCodeQuery queryTakeCode(TakeCodeQuery query);
    
    /**
     * 修改
     * @param takeCode  
     * void 
     * @exception
     */
    void upTakeCodeRemainDownloadNum(Integer id);
    
    /**
     * 查询单个，根据id
     * getTakeCodeById(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param id
     * @return  
     * TakeCode 
     * @exception
     */
    TakeCode getTakeCodeById(Integer id);
}
