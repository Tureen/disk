package com.yunip.enums.basics;

/**
 * 基础信息配置枚举
 */
public enum BasicsInfoCode {

    /**网站logo**/
    LOGO("LOGO","网站logo"),
    
    /**禁止上传类型**/
    NOTHROUGHUPLOAD("NOTHROUGHUPLOAD","禁止上传类型"),
    
    /**允许上传类型**/
    THROUGHUPLOAD("THROUGHUPLOAD","允许上传类型"),
    
    /**上传模式**/
    UPLOADMODE("UPLOADMODE","上传模式"),
    
    /**是否开启文件加密**/
    ISOPENENCRYPTFILE("ISOPENENCRYPTFILE","是否开启文件加密"),
    
    /**启用用户操作记录**/
    LOGSTATUS("LOGSTATUS","用户操作记录状态"),
    
    /**启用前台单点登录**/
    SINGLELOGIN("SINGLELOGIN","单点登录状态"),
    
    /**启用系统自动备份日志**/
    AUTOBACKUP("AUTOBACKUP","系统自动备份日志状态"),
    
    /**版本文件保留个数**/
    FILEVERSIONNUM("FILEVERSIONNUM","版本文件保留个数"),
    
    /**禁止登录IP列表**/
    NOTHROUGHIP("NOTHROUGHIP","禁止登录IP列表"),
    
    /**允许登录IP列表**/
    THROUGHIP("THROUGHIP","允许登录IP列表"),
    
    /**IP登录拦截模式**/
    IPMODE("IPMODE","IP登录拦截模式"),
    
    /**登录后默认进入空间**/
    TOLOGINSPAGE("TOLOGINSPAGE","登录后默认进入空间"),
    
    /**默认文件列表排序方式**/
    SORTTYPE("SORTTYPE","默认文件列表排序方式"),
    
    /**AD域IP地址**/
    ADIP("ADIP","AD域IP地址"),
    
    /**AD域控制器**/
    ADDC("ADDC","AD域控制器"),
    
    /**AD帐户用户名**/
    ADUSERNAME("ADUSERNAME","AD帐户用户名"),
    
    /**AD帐户密码**/
    ADPASSWORD("ADPASSWORD","AD帐户密码"),
    
    /**启用前台AD整合**/
    ISOPENAD("ISOPENAD","AD域开启状态"),
    
    /**单个文件最大上传大小**/
    SIMPLYFILESIZE("SMAX","单个文件最大上传大小"),
    
    /**欢迎页面**/
    WELCOMEPAGE("WELCOMEPAGE","欢迎页面"),
    
    /**本地网页语言**/
    LOCALLANGUAGE("LOCALLANGUAGE","网页语言"),
    
    /**是否开启水印**/
    IS_WATER_MARK("IS_WATER_MARK","是否开启水印"),
    
    /**水印文字内容**/
    WATER_MARK_TEXT("WATER_MARK_TEXT","文字水印内容"),
    
    /**水印文字颜色**/
    TEXT_COLOR("TEXT_COLOR","文字水印颜色"),
    
    /**水印文字对齐方式**/
    ALIGN("ALIGN","对齐方式"),
    
    /**水印文字大小**/
    FONT_SIZE("FONT_SIZE","水印文字大小"),
    
    /**水印文字倾斜（旋转）角度**/
    ROTATION("ROTATION","水印文字倾斜（旋转）角度"),
    
    /**阿里云OSS Bucket名称**/
    ALIYUN_OSS_BUCKET_NAME("ALIYUN_OSS_BUCKET_NAME","Bucket名称"),
    
    /**阿里云OSS访问域名**/
    ALIYUN_OSS_DOMAIN_ENDPOINT("ALIYUN_OSS_DOMAIN_ENDPOINT","OSS访问域名"),
    
    /**阿里云OSS Access Key ID**/
    ALIYUN_OSS_ACCESS_KEY_ID("ALIYUN_OSS_ACCESS_KEY_ID","Access Key ID"),
    
    /**阿里云OSS Access Key Secret**/
    ALIYUN_OSS_ACCESS_KEY_SECRET("ALIYUN_OSS_ACCESS_KEY_SECRET","Access Key Secret"),
    
    /**默认个人空间大小**/
    PSPACE("PSPACE","默认个人空间大小"),
    
    /**允许使用员工数秘钥**/
    REGISTER_RESTRICTIONS_KEY("REGISTER_RESTRICTIONS_KEY","允许使用员工数秘钥"),
    
    /**网站标题（中）**/
    TITLE("TITLE","网站标题"),
    
    /**后台网站标题（中）**/
    ADMIN_TITLE("ADMIN_TITLE","后台网站标题"),
    
    /**前台网站备案信息（中）**/
    WEB_RECORD("WEB_RECORD","前台网站备案信息"),
    
    /**后台网站备案信息（中）**/
    ADMIN_WEB_RECORD("ADMIN_WEB_RECORD","后台网站备案信息"),
    
    /**前台主页导航（中）**/
    WEB_NAVIGATION("WEB_NAVIGATION","前台主页导航"),
    
    /**后台主页导航（中）**/
    ADMIN_WEB_NAVIGATION("ADMIN_WEB_NAVIGATION","后台主页导航"),
    
    /**移动端主页导航(中)**/
    MOBILE_NAVIGATION("MOBILE_NAVIGATION", "移动端主页导航"),
    
    /**网站标题（英）**/
    TITLE_ENGLISH("TITLE_ENGLISH","网站标题"),
    
    /**后台网站标题（英）**/
    ADMIN_TITLE_ENGLISH("ADMIN_TITLE_ENGLISH","后台网站标题"),
    
    /**前台网站备案信息（英）**/
    WEB_RECORD_ENGLISH("WEB_RECORD_ENGLISH","前台网站备案信息"),
    
    /**后台网站备案信息（英）**/
    ADMIN_WEB_RECORD_ENGLISH("ADMIN_WEB_RECORD_ENGLISH","后台网站备案信息"),
    
    /**前台主页导航（英）**/
    WEB_NAVIGATION_ENGLISH("WEB_NAVIGATION_ENGLISH","前台主页导航"),
    
    /**后台主页导航（英）**/
    ADMIN_WEB_NAVIGATION_ENGLISH("ADMIN_WEB_NAVIGATION_ENGLISH","后台主页导航"),
    
    /**加密的上传文件峰值**/
    ENCRYPTFILEMAXSIZE("ENCRYPTFILEMAXSIZE","加密的上传文件峰值"),
    
    /**无峰值限制的文件类型**/
    INFINITEENCRYPTFILETYPE("INFINITEENCRYPTFILETYPE","无峰值限制的文件类型"),
    
    /**集群分布是否开启**/
    ISOPENCLUSTER("ISOPENCLUSTER","集群分布是否开启"),
    
    /**文件服务器地址**/
    FILEDOMAIN("FILEDOMAIN","文件服务器地址");
    
    private BasicsInfoCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    
    private String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
