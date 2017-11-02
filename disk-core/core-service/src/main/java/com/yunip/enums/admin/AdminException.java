/**
 * 
 */
package com.yunip.enums.admin;

/**
 * @author can.du
 * 异常编码
 */
public enum AdminException{
    
    /** 服务器数据异常 **/
    SUCCESS(1000, "数据正常呢"),
    
    /** 服务器数据异常 **/
    ERROR(2000, "服务器数据异常"),
    
    /** 正在使用的角色不能删除 **/
    USERINGROLE(1001, "正在使用的角色不能删除"),
    
    /**存在子部门**/
    ZCZBM(1002,"存在子部门"),
    
    /**部门下存在员工**/
    BMCZYG(1003,"部门下存在员工"),
    
    /**已经存在该员工编号**/
    YJCZGBH(1004,"已经存在该员工编号"),
    
    /**已经存在该员工手机号**/
    YJCZGSJ(1005,"已经存在该员工手机号"),
    
    /**提取码失效**/
    TQMSX(1006,"提取码失效"),
    
    /**未输入员工编号**/
    WSRBH(1007,"未输入员工编号"),
    
    /**未输入手机号**/
    WSRSJ(1008,"未输入员工手机号"),
    
    /**必填字段为空，请检查**/
    BTZDWK(1009,"必填字段为空，请检查"),
    
    /**手机格式不正确**/
    SJGSBZQ(1010,"手机格式不正确!"),
    
    /**主用户未找到**/
    ZYHWK(1011,"主账户未找到!"),
    
    /**副用户未找到**/
    FYHWK(1012,"副账户未找到!"),
    
    /**主账户与副账户为同一用户**/
    YHCF(1013,"主账户与副账户为同一用户"),
    
    /** 数据库备份错误 **/
    DATABASEEXPOERTERROR(1014,"数据库备份错误,请重试"),
    
    /** 数据库备份文件不存在 **/
    DATABASEBACKUPFILENOTEXISTS(1015,"数据库备份文件不存在！"),
    
    /** 操作阿里云OSS错误 **/
    OPERATEALIYUNOSSERROR(1016,"操作阿里云对象存储OSS平台错误，请检查阿里云对象存储OSS平台的配置信息！"),
    
    /** 参数异常 **/
    PARAMERROR(2001, "参数异常"),
    
    /** 验证码输入不正确 **/
    CODEERROR(2002, "验证码输入不正确"),
    
    /** 帐号或者密码错误 **/
    AORPERROR(2003, "帐号或者密码错误"),
    
    /** 帐号被冻结 **/
    ACCOUNTFREEZE(2004, "帐号被冻结"),
    
    /** 登录失效 **/
    LOGINFAILURE(2005, "登录失效"),
    
    /** 无该操作权限 **/
    NOPERMISSION(2006, "无该操作权限"),
    
    /** 旧密码为空 **/
    NOPASSWORD(2010, "旧密码为空"),
    
    /** 新密码为空 **/
    NONEWPASSWORD(2011, "新密码为空"),
    
    /** 再次输入密码为空 **/
    NOCONFIRMPASSWORD(2012, "再次输入密码为空"),
    
    /** 两次密码不一致 **/
    NOSAMEPASS(2013, "两次密码不一致"),
    
    /** 密码含有空格 **/
    PASSWORDHAVASPACE(2015, "密码含有空格"),
    
    /**注册人数限制秘钥有误**/
    REGISTER_KEY_ERROR(2016,"注册人数限制秘钥有误，解密后不是数字"),
    
    /**注册员工到达上限**/
    EXCEED_REGISTER_MAXIMUM(2017,"注册员工到达上限，不能再次进行注册及解冻操作，请开通更多员工上限。"),
    
    /**允许使用员工数秘钥md5错误**/
    KHDSCBM_MD5_ERROR(2018,"允许使用员工数秘钥md5错误");
    
    private Integer code;
     
    private String msg;
     
    private AdminException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
     
     
}
