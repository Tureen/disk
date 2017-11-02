/**
 * 
 */
package com.yunip.enums.api;

/**
 * @author can.du
 * 异常编码
 */
public enum ApiException {

    /** 服务器数据异常 **/
    ERROR(1001, "服务器数据异常"),

    /** 请求数据格式不正确 **/
    PARAMERROR(1002, "请求数据格式不正确"),

    /**
     * 非法请求，md5校验不通过或参数错误
     */
    ILLEAGEREQUEST(1003, "非法请求，md5校验不通过或参数错误"),

    /**
     * 请求接口不存在
     */
    ACTIONNOEXISTS(1004, "请求接口不存在"),

    /**
     * 登录失效
     */
    TOKENFAILURE(1005, "登录失效"),

    /**
     * 手机号码不存在
     */
    MOBILENOEXISTS(1006, "手机号码不存在"),

    /**
     * 帐号或者密码错误
     */
    LOGINFAIL(1007, "密码错误,请重新输入"),

    /**
     * 不在常用设备上登录
     */
    GGIMEI(1008, "不在常用设备上登录"),

    /**
     * 原密码不正确
     */
    OGPWDERROR(1009, "原密码不正确"),
    
    /**
     * 帐号被冻结
     */
    FREEZE(1010, "帐号被冻结"),
    
    /**
     * 权限不足
     */
    NOPERMISSION(1011, "权限不足"),
    
    /**
     * 头像上传失
     */
    UPLOADFAIL(1012, "头像上传失败"),
    
    /**该账户不存在**/
    NOUSERNAME(1013, "该账户不存在"),
    
    /**用户信息未录入**/
    NOEMPLOYEE(1014,"用户信息未录入,请先登录网页版进行设置"),
    
    /** 同一个员工或者部门不能设置不同的权限 **/
    AUTHERROR(2008, "同一个员工或者部门不能设置不同的权限"),
    
    /**目录下最多能创建9999个子目录**/
    NOCREATEFOLDERS(2019,"目录下不能创建超过9999个文件夹"),
    
    /**总空间已满**/
    FULLSPACE(2021,"总空间已满!"),
    
    /**密码错误次数超过5次**/
    LOGINERRORWAIT(2022,"密码错误次数超过5次,请等待5分钟后再试!"),
    
    /**二维码不存在**/
    NOTFINDQRCODE(2023,"二维码不存在"),
    
    /**二维码已失效**/
    UNEFFECTIVE(2024,"二维码已失效"),
    
    /**当前IP不允许登录**/
    NOTLOGIN(2025,"当前IP不允许登录");

    private Integer code;

    private String  msg;

    private ApiException(Integer code, String msg) {
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
