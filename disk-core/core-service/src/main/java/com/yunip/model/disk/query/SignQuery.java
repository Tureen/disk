/*
 * 描述：标签索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.Sign;
import com.yunip.utils.page.PageQuery;

@Alias("TSignQuery")
public class SignQuery extends PageQuery<Sign> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -6107316507087844085L;

    /** 标签内容 **/
    private String            signName;

    /** 员工ID **/
    private Integer           employeeId;
    
    /** 开始时间 **/
    private String            startDate;

    /** 结束时间 **/
    private String            endDate;

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
