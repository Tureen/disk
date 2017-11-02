package com.yunip.model.config.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.config.SysConfig;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 系统配置查询类
 */
@Alias("TSysConfigQuery")
public class SysConfigQuery extends PageQuery<SysConfig> implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -7542218886893632882L;

    /** 配置类别 **/
    private String            configCode;

    /** 配置属性 **/
    private String            configKey;

    /** 属性值 **/
    private String            configValue;

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

}
