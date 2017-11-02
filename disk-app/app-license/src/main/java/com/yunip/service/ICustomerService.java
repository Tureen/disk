package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.au.Customer;
import com.yunip.model.au.LicenseCode;
import com.yunip.model.au.LicenseLog;
import com.yunip.model.au.query.CustomerQuery;

/**
 * 客户service
 * @author ming.zhu
 *
 */
public interface ICustomerService {

	/**
	 * 添加客户信息与授权码
	 * @param customer
	 * @return
	 */
	@Transactional
	int addCustomer(Customer customer,LicenseCode licenseCode);
	
	/**
	 * 修改客户信息
	 * @param customer
	 * @return
	 */
	@Transactional
	int updateCustomer(Customer customer,LicenseLog licenseLog);
	
	/**
	 * 根据条件查询客户信息
	 * @param query
	 * @return
	 */
	CustomerQuery queryCustomer(CustomerQuery query);
	
	/**
	 * 查询
	 * @param id
	 * @return  
	 * Customer 
	 * @exception
	 */
	Customer getCustomer(Integer id);
}
