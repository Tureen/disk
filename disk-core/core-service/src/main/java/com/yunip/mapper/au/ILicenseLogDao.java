package com.yunip.mapper.au;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.au.LicenseLog;
import com.yunip.model.au.query.LicenseLogQuery;

@Repository("iLicenseLogDao")
public interface ILicenseLogDao extends IBaseDao<LicenseLog>{

	/**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<LicenseLog> selectByQuery(LicenseLogQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(LicenseLogQuery query);
}
