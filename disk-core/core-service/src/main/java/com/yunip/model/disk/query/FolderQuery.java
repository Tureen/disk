/*
 * 描述：文件夹索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.enums.disk.ValidStatus;
import com.yunip.model.disk.Folder;
import com.yunip.utils.page.PageQuery;

@Alias("TFolderQuery")
public class FolderQuery extends PageQuery<Folder> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 985735292222050400L;

    /** 主键 **/
    private Integer           folderId;

    /** 文件夹编码 **/
    private String            folderCode;

    /** 文件夹名称 **/
    private String            folderName;

    /** 文件类型(1.私人,2.公共) **/
    private Integer           folderType;

    /** 员工ID **/
    private Integer           employeeId;

    /**父文件夹Id**/
    private Integer           parentId;

    /**文件状态**/
    private Integer           validStatus      = ValidStatus.NOMAL.getStatus();

    /** 分享状态 **/
    private Integer           shareStatus;

    /**查询名称**/
    private String            queryName;

    /***排除本身的ID***/
    private Integer           pFolderId;

    /*******************辅助字段**********************/

    /**排序类型**/
    private String            orderIndex;

    /**展示方式（1.列表，2.图标）**/
    private int               type;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getpFolderId() {
        return pFolderId;
    }

    public void setpFolderId(Integer pFolderId) {
        this.pFolderId = pFolderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
