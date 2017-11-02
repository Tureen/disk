/*
 * 描述：用户文件夹权限相关索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.AuthorityShare;
import com.yunip.utils.page.PageQuery;

@Alias("TAuthorityShareQuery")
public class AuthorityShareQuery extends PageQuery<AuthorityShare> implements
        Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 5572836087227960468L;

    /** 主键 **/
    private Integer           id;

    /** 分享的(文件夹ID或者文件) **/
    private Integer           openId;

    /** 分享的文件类型(1.文件夹,2.文件) **/
    private Integer           openType;

    /** 操作权限(具体枚举中展示) **/
    private Integer           operAuth;

    /** 被分享对象ID(员工ID) **/
    private String            shareEid;

    /** 被分享对象ID(部门ID) **/
    private String            shareDid;

    /** 被分享对象ID(工作组ID )**/
    private String            shareWid;

    /** 分享的类型(1.部门,2.员工) **/
    private Integer           shareType;

    /** 分享者员工id **/
    private Integer           employeeId;

    /** 父文件夹ID **/
    private Integer           folderId;

    /*********************辅助字段*************************/

    /** 分享文件夹的类型（公共or私人） **/
    private Integer           folderType;

    /** 分享的名称 **/
    private String            queryName;

    /** 排序 **/
    private String            orderIndex;

    /** 对应工作组员工关系表的员工id **/
    private Integer           workgroupEmployeeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Integer getOperAuth() {
        return operAuth;
    }

    public void setOperAuth(Integer operAuth) {
        this.operAuth = operAuth;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getShareEid() {
        return shareEid;
    }

    public void setShareEid(String shareEid) {
        this.shareEid = shareEid;
    }

    public String getShareDid() {
        return shareDid;
    }

    public void setShareDid(String shareDid) {
        this.shareDid = shareDid;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getWorkgroupEmployeeId() {
        return workgroupEmployeeId;
    }

    public void setWorkgroupEmployeeId(Integer workgroupEmployeeId) {
        this.workgroupEmployeeId = workgroupEmployeeId;
    }

    public String getShareWid() {
        return shareWid;
    }

    public void setShareWid(String shareWid) {
        this.shareWid = shareWid;
    }

}
