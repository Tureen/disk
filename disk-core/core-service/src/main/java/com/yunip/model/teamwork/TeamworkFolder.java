/*
 * 描述：协作文件夹基本信息表
 * 创建人：ming.zhu
 * 创建时间：2017-02-27
 */
package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.IconUtil;

@Alias("TTeamworkFolder")
public class TeamworkFolder implements Serializable {
    /**主键**/
    private Integer              id;

    /**文件夹名称**/
    private String               folderName;

    /**文件类型**/
    private Integer              folderType;

    /**有效状态（启用:1,冻结:0）**/
    private Integer              validStatus;

    /**文件夹编码**/
    private String               folderCode;

    /**父文件ID**/
    private Integer              parentId;

    /**当前目录下的版本号**/
    private Integer              folderFbVersion;

    /**员工id**/
    private Integer              employeeId;

    /**协作ID**/
    private Integer              teamworkId;

    /**创建人**/
    private String               createAdmin;

    /**创建时间**/
    private Date                 createTime;

    /**更新人**/
    private String               updateAdmin;

    /**更新时间**/
    private Date                 updateTime;

    /*******************************************/

    /**存放子目录集**/
    private List<TeamworkFolder> teamworkFolders;

    /**存放子文件集**/
    private List<TeamworkFile>   teamworkFiles;

    /**显示修改时间**/
    private String               updateDate;

    /**协作组名称**/
    private String               teamworkName;

    /**协作图标**/
    private Integer              icon;

    /**id集合**/
    private List<String>        ids;

    private static final long    serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
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

    public Integer getFolderFbVersion() {
        return folderFbVersion;
    }

    public void setFolderFbVersion(Integer folderFbVersion) {
        this.folderFbVersion = folderFbVersion;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<TeamworkFolder> getTeamworkFolders() {
        return teamworkFolders;
    }

    public void setTeamworkFolders(List<TeamworkFolder> teamworkFolders) {
        this.teamworkFolders = teamworkFolders;
    }

    public List<TeamworkFile> getTeamworkFiles() {
        return teamworkFiles;
    }

    public void setTeamworkFiles(List<TeamworkFile> teamworkFiles) {
        this.teamworkFiles = teamworkFiles;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getTeamworkName() {
        return teamworkName;
    }

    public void setTeamworkName(String teamworkName) {
        this.teamworkName = teamworkName;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getIconStr() {
        return IconUtil.teamworkIcon(icon);
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
