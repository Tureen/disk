package com.yunip.model.company;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 员工
 */
@Alias("TEmployee")
public class Employee implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 员工编号 **/
    private String            employeeCode;

    /** 头像 **/
    private String            employeePortrait;

    /** 首字母（大写） **/
    private String            employeeChar;

    /** 真实名称 **/
    private String            employeeName;

    /** 身份证 **/
    private String            idCard;

    /** 部门ID **/
    private String            deptId;

    /** 微信 **/
    private String            employeeWechat;

    /** QQ **/
    private String            employeeQq;

    /** 固定电话 **/
    private String            employeeTelephone;

    /** 移动电话 **/
    private String            employeeMobile;

    /**邮箱**/
    private String            employeeEmail;

    /**职位**/
    private String            employeeJob;

    /** 性别 **/
    private Integer           employeeSex;

    /**空间大小**/
    private String            spaceSize;

    /**协作空间大小**/
    private String            teamworkSpaceSize;

    /** 自我介绍 **/
    private String            introduction;

    /** 有效状态（启用:1,冻结:0） **/
    private Integer           validStatus;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    /*********************************辅助字段**************************************/
    /** 部门名称 **/
    private String            deptName;

    /** 管理员状态 **/
    private Integer           adminValidStatus;

    /**终端表示**/
    private String            token;

    /** 是否是公共空间管理员 **/
    private boolean           commonShareStatus = false;

    /**性别描述**/
    private String            sex;

    /**登录IP**/
    private String            loginIp;

    /**个人空间总大小**/
    private String            personalSize;

    private static final long serialVersionUID  = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeePortrait() {
        return employeePortrait;
    }

    public void setEmployeePortrait(String employeePortrait) {
        this.employeePortrait = employeePortrait;
    }

    public String getEmployeeChar() {
        return employeeChar;
    }

    public void setEmployeeChar(String employeeChar) {
        this.employeeChar = employeeChar;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getEmployeeWechat() {
        return employeeWechat;
    }

    public void setEmployeeWechat(String employeeWechat) {
        this.employeeWechat = employeeWechat;
    }

    public String getEmployeeQq() {
        return employeeQq;
    }

    public void setEmployeeQq(String employeeQq) {
        this.employeeQq = employeeQq;
    }

    public String getEmployeeTelephone() {
        return employeeTelephone;
    }

    public void setEmployeeTelephone(String employeeTelephone) {
        this.employeeTelephone = employeeTelephone;
    }

    public String getEmployeeMobile() {
        return employeeMobile;
    }

    public void setEmployeeMobile(String employeeMobile) {
        this.employeeMobile = employeeMobile;
    }

    public Integer getEmployeeSex() {
        return employeeSex;
    }

    public void setEmployeeSex(Integer employeeSex) {
        this.employeeSex = employeeSex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getAdminValidStatus() {
        return adminValidStatus;
    }

    public void setAdminValidStatus(Integer adminValidStatus) {
        this.adminValidStatus = adminValidStatus;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeJob() {
        return employeeJob;
    }

    public void setEmployeeJob(String employeeJob) {
        this.employeeJob = employeeJob;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getCommonShareStatus() {
        return commonShareStatus;
    }

    public void setCommonShareStatus(boolean commonShareStatus) {
        this.commonShareStatus = commonShareStatus;
    }

    public String getShowSex() {
        return employeeSex == null ? "保密" : (employeeSex == 0 ? "男" : "女");
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getSpaceSize() {
        return spaceSize;
    }

    public void setSpaceSize(String spaceSize) {
        this.spaceSize = spaceSize;
    }

    public String getTeamworkSpaceSize() {
        return teamworkSpaceSize;
    }

    public void setTeamworkSpaceSize(String teamworkSpaceSize) {
        this.teamworkSpaceSize = teamworkSpaceSize;
    }

    public String getPersonalSize() {
        return personalSize;
    }

    public void setPersonalSize(String personalSize) {
        this.personalSize = personalSize;
    }
}
