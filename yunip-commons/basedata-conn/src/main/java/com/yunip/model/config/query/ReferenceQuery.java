package com.yunip.model.config.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.config.Reference;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 系统参照信息  封装查询条件类
 *
 */
@Alias("TReferenceQuery")
public class ReferenceQuery extends PageQuery<Reference> implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -2373001055746840947L;

    /**
     * 属性代码
     */
    private String            refCode;

    /**
     * 属性键
     */
    private String            refKey;

    /**
     * 属性值
     */
    private String            refValue;

    /**
     * 排序号
     */
    private Integer           sortNo;

    /**
     * 是否有效(有效:1,无效:0)
     */
    private Integer           validStatus;

    /**
     * 是否默认(是:1,否:0)
     */
    private Integer           defaultStatus;

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
