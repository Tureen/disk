package com.yunip.mapper.au;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.au.Customer;
import com.yunip.model.au.query.CustomerQuery;

@Repository("iCustomerDao")
public interface ICustomerDao extends IBaseDao<Customer>{

	/**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Customer> selectByQuery(CustomerQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(CustomerQuery query);
}
