package com.yunip.constant;

/***
 * @author can.du
 * 功能：系统常量配置
 */
public class SysContant {

    /**存储验证码常量**/
    public final static String   CHOCKCODE                      = "imagecheckcode";

    /**session存储的key**/
    public final static String   EMPLOYEE_IN_SESSION            = "employee";

    /**cookie存储的key**/
    public final static String   ADMIN_IN_COOKIE                = "admin";

    /**cookie存储的key（PC客户端登录时使用）**/
    public final static String   ADMIN_PCCLIENT_IN_COOKIE       = "adminPCClient";

    /**被分享文件夹权限map(文件夹id对应权限)的key**/
    public final static String   SHARE_OPEN_AUTH                = "shareopenauth";

    /**公共空间文件夹权限map(文件夹id对应权限)的key**/
    public final static String   COMMON_OPEN_AUTH               = "cmmonopenauth";

    /**公共空间模拟用户id**/
    public final static int      COMMON_MANAGE_EMPLOYEE_ID      = 1;

    /**session存储的公共空间模拟用户key**/
    public final static String   COMMON_MANAGE_EMPLOYEE_SESSION = "robotemployee";

    /**提取码失效时间**/
    public final static int      TAKE_CODE_EFFECTIVEDATE        = 10;

    /***所有左页节点关键字**/
    public final static String[] NAME_SPACES                    = { "/home",
            "/personal", "/share", "/manageshare", "/comshare", "/bshare",
            "/sign", "/takecode", "/message", "/user", "/lucene", "/contact",
            "/comcontact", "/workgroup", "/wgapply", "/teamwork" };

    /**网站标题**/
    public final static String   DISK_TITLE                     = "TITLE";

    /**用户尝试登录标识**/
    public final static String   ATTEMPTLOGIN                   = "attemptlogin";

    /**首页展示标识**/
    public final static String   INDEXIDENTIFICATION            = "Identification";

    /** 文件存放根目录(C:\\file) */
    public static String         ROOTPATH                       = "C:\\file";
}
