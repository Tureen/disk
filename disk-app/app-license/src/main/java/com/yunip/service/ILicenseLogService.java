package com.yunip.service;

import com.yunip.model.au.query.LicenseLogQuery;

/**
 * 授权日志service
 */
public interface ILicenseLogService {

    /**
     * 
     * @param query
     * @return  
     * LicenseLogQuery 
     * @exception
     */
    LicenseLogQuery queryLicenseLog(LicenseLogQuery query);
}
