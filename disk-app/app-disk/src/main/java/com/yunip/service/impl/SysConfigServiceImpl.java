package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.config.ISysConfigDao;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;
import com.yunip.service.ISysConfigService;

@Service("iSysConfigService")
public class SysConfigServiceImpl implements ISysConfigService{
    
    @Resource(name = "iSysConfigDao")
    private ISysConfigDao sysConfigDao;

    @Override
    public List<SysConfig> getSysConfig(SysConfigQuery query) {
        return sysConfigDao.selectByQuery(query);
    }

}
