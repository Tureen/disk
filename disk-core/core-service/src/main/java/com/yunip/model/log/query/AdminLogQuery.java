/*
 * 描述：后台管理员or前台员工日志信息索引类
 * 创建人：ming.zhu
 * 创建时间：2016-6-27
 */
package com.yunip.model.log.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.log.AdminLog;
import com.yunip.utils.page.PageQuery;

@Alias("TAdminLogQuery")
public class AdminLogQuery extends PageQuery<AdminLog> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 8482209049146361622L;

    /** 行为类型  **/
    private Integer           actionType;

    /** 操作人ID **/
    private Integer           adminId;

    /** 操作人 **/
    private String            operAdmin;

    /** 是否为管理员(0.管理员,1.普通员工) **/
    private Integer           isAdmin;

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**********************辅助字段***********************/

    /**栏目名**/
    private String            menuName;

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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

    public String getOperAdmin() {
        return operAdmin;
    }

    public void setOperAdmin(String operAdmin) {
        this.operAdmin = operAdmin;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
