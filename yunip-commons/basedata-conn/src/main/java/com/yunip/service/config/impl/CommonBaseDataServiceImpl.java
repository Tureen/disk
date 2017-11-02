package com.yunip.service.config.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.config.IReferenceDao;
import com.yunip.mapper.config.ISysConfigDao;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;
import com.yunip.service.config.ICommonBaseDataService;

/**
 * reference表，sysconfig表，region表的 维护功能Service实现类
 * 
 * @author Administrator
 * 
 */
@Service("iCommonBaseDataService")
public class CommonBaseDataServiceImpl implements ICommonBaseDataService {

    @Resource(name = "iSysConfigDao")
    private ISysConfigDao sysConfigDao;
    
    @Resource(name = "iReferenceDao")
    private IReferenceDao referenceDao;
    
    /**
     * 获得多条全局静态配置信息
     */
    @Override
    public SysConfigQuery listSysConfig(SysConfigQuery query) {
        List<SysConfig> list = sysConfigDao.selectByQuery(query);
        int count = sysConfigDao.selectCountByQuery(query);
        query.setRecordCount(count);
        query.setList(list);
        return query;
    }

    /**
     * 保存全局静态配置信息
     */
    @Override
    @Transactional 
    public int saveSysConfig(SysConfig sysConfig) {
        int i = sysConfigDao.insert(sysConfig);
        return i;
    }

    /**
     * 获得单条全局静态配置信息
     */
    @Override
    public SysConfig getSysConfig(String configcode, String configkey) {
        SysConfigQuery sysConfigQuery = new SysConfigQuery();
        sysConfigQuery.setConfigCode(configcode);
        sysConfigQuery.setConfigKey(configkey);
        List<SysConfig> list = sysConfigDao.selectByQuery(sysConfigQuery);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 修改全局静态配置信息
     */
    @Override
    @Transactional
    public int editSysConfig(SysConfig sysConfig) {
        int i = sysConfigDao.update(sysConfig);
        return i;
    }

    @Override
    public HashMap<String, String> getSysConfigByCode(String code) {
        HashMap<String, String> map = new HashMap<String, String>();
        SysConfigQuery sysConfigQuery = new SysConfigQuery();
        sysConfigQuery.setConfigCode(code);
        List<SysConfig> list = sysConfigDao.selectByQuery(sysConfigQuery);
        for (SysConfig sysConfig : list) {
            map.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }
        return map;
    }
    
}