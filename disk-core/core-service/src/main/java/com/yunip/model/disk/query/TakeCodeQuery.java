/*
 * 描述：提取码索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.TakeCode;
import com.yunip.utils.page.PageQuery;

@Alias("TTakeCodeQuery")
public class TakeCodeQuery extends PageQuery<TakeCode> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -3323388860620388172L;

    /** 提取码 **/
    private String            takeCode;

    /** 文件id **/
    private Integer           fileId;

    /** 员工ID **/
    private Integer           employeeId;

    /** 当前时间 **/
    private Date              openTime;

    /**********************辅助字段**********************/

    /** 查询的文件名 **/
    private String            queryName;

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

}
