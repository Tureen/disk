package com.yunip.model.company;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/***
 * @author ming.zhu
 * 功能：部门的简易输出对象
 */
@Alias("TSimpleDeptData")
public class SimpleDeptData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键**/
    private String            id;

    /**名称**/
    private String            deptName;

    /**首字母**/
    private String            deptChar;
    
    /**父部门ID**/
    private String            parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptChar() {
        return deptChar;
    }

    public void setDeptChar(String deptChar) {
        this.deptChar = deptChar;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
