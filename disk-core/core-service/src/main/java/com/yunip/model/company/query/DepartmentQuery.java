package com.yunip.model.company.query;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.model.company.Department;
import com.yunip.utils.page.PageQuery;

/**
 * @author can.du
 * 部门的查询对象
 */
@Alias("TDepartmentQuery")
public class DepartmentQuery extends PageQuery<Department> implements
        Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 2248260016676862559L;

    /** 主键 **/
    private String            id;

    /** 首字母（大写） **/
    private String            deptChar;

    /** 部门名称 **/
    private String            deptName;

    /**父部门ID*/
    private String            parentId;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新时间 **/
    private Date              updateTime;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptChar() {
        return deptChar;
    }

    public void setDeptChar(String deptChar) {
        this.deptChar = deptChar;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
