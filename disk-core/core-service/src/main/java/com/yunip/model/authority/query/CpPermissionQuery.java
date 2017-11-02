package com.yunip.model.authority.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.authority.CpPermission;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 员工权限查询对象
 */
@Alias("TCpPermissionQuery")
public class CpPermissionQuery extends PageQuery<CpPermission> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 3917142394117029461L;

    /** 权限id **/
    private Integer           id;

    /** 权限名称 **/
    private String            permissionName;

    /** 系统代码 **/
    private String            systemCode;

    /** 有效状态（启用:1,冻结:0） **/
    private Integer           validStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }
    
    
}
