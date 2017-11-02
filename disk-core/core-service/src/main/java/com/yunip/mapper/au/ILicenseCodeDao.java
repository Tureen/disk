package com.yunip.mapper.au;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.au.LicenseCode;
import com.yunip.model.au.query.LicenseCodeQuery;

@Repository("iLicenseCodeDao")
public interface ILicenseCodeDao extends IBaseDao<LicenseCode>{

	/**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<LicenseCode> selectByQuery(LicenseCodeQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(LicenseCodeQuery query);
    
    /**
     * 根据customerId,查询
     * @param customerId
     * @return  
     * LicenseCode 
     * @exception
     */
    LicenseCode selectByCustomerId(Integer customerId);
}
