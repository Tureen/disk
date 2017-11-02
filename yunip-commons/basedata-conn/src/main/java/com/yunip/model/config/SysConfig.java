package com.yunip.model.config;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 配置信息表
 */
@Alias("TSysConfig")
public class SysConfig implements Serializable {
    /** 配置类别 **/
    private String            configCode;

    /** 配置属性 **/
    private String            configKey;

    /** 属性值 **/
    private String            configValue;

    private static final long serialVersionUID = 1L;

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
