package com.yunip.enums.log;

public enum AdminActionType {
    /** 管理员 **/
    ADMINMANAGE(1,"管理员管理"),
    
    /** 角色 **/
    ROLEMANAGE(6,"角色管理"),
    
    /** 权限:修改 **/
    PERMISSIONMANAGE(11,"权限管理"),
    
    /** 部门:添加 **/
    DEPTMANAGE(14,"部门管理"),
    
    /** 员工:添加 **/
    EMPLOYEEMANAGE(19,"员工管理"),
    
    /** 导入员工 **/
    EMPLOYEEIMPORT(24,"导入员工"),
    
    /** 导出员工 **/
    EMPLOYEEEXPORT(25,"导出员工"),
    
    /** 合并员工 **/
    EMPLOYEEMERGE(26,"合并员工"),
    
    /** 基本配置修改 **/
    SYSCONFIGUPDATE(27,"基本配置修改"),
    
    /** 初始化员工密码 **/
    EMPLOYEEINITPASSWD(28,"初始化密码"),
    
    /** 回收站删除 **/
    RECOVERYFILEDELETE(29,"回收站删除"),
    
    /** 回收站还原 **/
    RECOVERYFILERESTORE(30,"回收站还原");
    

    private AdminActionType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int    type;

    private String desc;
    

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
