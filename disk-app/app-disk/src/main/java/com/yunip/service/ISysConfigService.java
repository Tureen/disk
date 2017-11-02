package com.yunip.service;

import java.util.List;

import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;

public interface ISysConfigService {

    /**
     * 条件查询（query）
     * @param query
     * @return  
     * SysConfig 
     * @exception
     */
    List<SysConfig> getSysConfig(SysConfigQuery query);
}
