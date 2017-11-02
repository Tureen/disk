package com.yunip.model.config;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 枚举参照信息表
 */
@Alias("TReference")
public class Reference implements Serializable {
    /** 属性代码 **/
    private String            refCode;

    /** 属性键 **/
    private String            refKey;

    /** 属性值 **/
    private String            refValue;

    /** 排序号 **/
    private Integer           sortNo;

    /** 有效性  是:1,否:0 **/
    private Integer           validStatus;

    /** 是否默认  是:1,否:0 */
    private Integer           defaultStatus;

    private static final long serialVersionUID = 1L;

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

}
