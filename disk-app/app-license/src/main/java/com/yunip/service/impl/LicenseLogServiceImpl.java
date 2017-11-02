package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.au.ILicenseLogDao;
import com.yunip.model.au.LicenseLog;
import com.yunip.model.au.query.LicenseLogQuery;
import com.yunip.service.ILicenseLogService;

/**
 * @author ming.zhu
 * 授权日志实现类
 */
@Service("iLicenseLogService")
public class LicenseLogServiceImpl implements ILicenseLogService{
    
    @Resource(name = "iLicenseLogDao")
    private ILicenseLogDao licenseLogDao;

    @Override
    public LicenseLogQuery queryLicenseLog(LicenseLogQuery query) {
        List<LicenseLog> list = licenseLogDao.selectByQuery(query);
        int count = licenseLogDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

}
