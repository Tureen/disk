/**
 * 
 */
package com.yunip.enums.disk;


/**
 * @author can.du
 * 异常编码
 */
public enum DiskException {

    /** 服务器数据异常 **/
    SUCCESS(1000, "数据正常呢"),

    /** 服务器数据异常 **/
    ERROR(2000, "服务器数据异常"),

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

    /** 文件服务器连接操作失败 **/
    FILESERVICEERROR(2007, "文件服务器连接操作失败"),

    /** 同一个员工或者部门不能设置不同的权限 **/
    AUTHERROR(2008, "同一个员工或者部门不能设置不同的权限"),

    /** 密码核验不通过 **/
    NOPASSCHECKPASSWORD(2009, "原密码错误"),

    /** 旧密码为空 **/
    NOPASSWORD(2010, "原密码为空"),
    
    /** 新密码为空 **/
    NONEWPASSWORD(2011, "新密码为空"),
    
    /** 再次输入密码为空 **/
    NOCONFIRMPASSWORD(2012, "再次输入密码为空"),
    
    /** 两次密码不一致 **/
    NOSAMEPASS(2013, "两次密码不一致"),
    
    /** 帐号含有空格 **/
    USERNAMEHAVASPACE(2014, "帐号含有空格"),
    
    /** 密码含有空格 **/
    PASSWORDHAVASPACE(2015, "密码含有空格"),
    
    /** 上传失败 **/
    UPLOADFAIL(2016, "上传失败,仅支持png、jpeg、jpg等图片格式"),
    
    /** 格式不支持 只支持png、jpeg、jpg **/
    TYPENOALLOW(2017, "仅支持png、jpeg、jpg等图片格式"),
    
    /**用户姓名为空**/
    NOEMPLOYEENAME(2018,"用户姓名为空!"),
    
    /**目录下最多能创建9999个子目录**/
    NOCREATEFOLDERS(2019,"目录下不能创建超过9999个文件夹"),
    
    /**当前IP不允许登录**/
    NOTLOGIN(2020,"当前IP不允许登录"),
    
    /**总空间已满**/
    FULLSPACE(2021,"总空间已满!"),
    
    /**未输入员工编号**/
    WSRBH(2022,"未输入员工编号"),
    
    /**已经存在该员工编号**/
    YJCZGBH(2023,"已经存在该员工编号"),
    
    /**未输入手机号**/
    WSRSJ(2024,"未输入员工手机号"),
    
    /**手机格式不正确**/
    SJGSBZQ(2025,"手机格式不正确!"),
    
    /**已经存在该员工手机号**/
    YJCZGSJ(2026,"已经存在该员工手机号"),
    
    /**非法授权**/
    FFSQ(2027,"非法授权"),
    
    /**授权已超时**/
    SQCS(2028,"授权已超时"),
    
    /**授权已超时**/
    STFFCZ(2029,"系统非法操作"),
    
    /**该授权码已经使用过**/
    SQMYJSYG(2030,"该授权码已经使用过"),
    
    /**未输入邮箱**/
    WSRYX(2031,"未输入邮箱"),
    
    /**邮箱格式不正确**/
    YXGSBZQ(2032,"邮箱格式不正确!"),
    
    /**注册人数限制秘钥有误**/
    REGISTER_KEY_ERROR(2033,"注册人数限制秘钥有误，解密后不是数字"),
    
    /**注册员工到达上限**/
    EXCEED_REGISTER_MAXIMUM(2034,"注册员工到达上限，不能再次进行注册及解冻操作，请开通更多员工上限。"),
    
    /** 当前帐号已在别处登录，此处已下线！ **/
    OFFLINENOTIFICATION(2035, "当前帐号已在别处登录，此处已下线！"),
    
    /**允许使用员工数秘钥md5错误**/
    KHDSCBM_MD5_ERROR(2036,"允许使用员工数秘钥md5错误"),
    
    /**提取码剩余下载次数为0**/
    UNREMAINDOWNLOADNUM(2037,"提取码剩余下载次数为0，该文件下载失败！"),
    
    /**集群版-不支持同时下载多个文件、目录、文件+目录**/
    COLONY_NOT_SUPPORTED_DOWNLOAD(2038,"不支持同时下载多个文件、目录、文件+目录"),
    
    /**工作组申请加入时，验证重复提交申请的异常**/
    WORKGROUP_APPLY_REPEAT_COMMIT(2039,"申请失败：申请已在审核，请勿重复提交"),
    
    /**工作组管理员不能被移除**/
    WORKGROUP_CREATEADMIN_SAME(2040,"工作组管理员不能被移除"),
    
    /**协作管理员不能退出该协作**/
    TEAMWORK_CREATEADMIN_SAME(2041,"协作管理员不能被移除"),
    
    /**个人空间已满**/
    FULL_PERSONAL_SIZE(2042, "个人空间已满，可前往【个人信息设置】中调整空间分配"),
    
    /**协作空间已满**/
    FULL_TEAMWORK_SIZE(2043, "协作空间已满，可前往【个人信息设置】中调整空间分配");

    private Integer code;

    private String  msg;

    private DiskException(Integer code, String msg) {
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
