package com.yunip.model.config.support;

public class BasicsIndexHelper {

    /**网站logo**/
    private String LOGO;

    /**禁止上传的文件类型**/
    private String NOTHROUGHUPLOAD;

    /**允许上传的文件类型**/
    private String THROUGHUPLOAD;

    /**上传模式**/
    private String UPLOADMODE;

    /**是否开启文件加密**/
    private String ISOPENENCRYPTFILE;

    /**启用前台用户操作记录**/
    private String LOGSTATUS;

    /**启用前台单点登录**/
    private String SINGLELOGIN;

    /**启用系统自动备份日志**/
    private String AUTOBACKUP;

    /**版本文件保留个数**/
    private String FILEVERSIONNUM;

    /**禁止登录IP列表**/
    private String NOTHROUGHIP;

    /**允许登录IP列表**/
    private String THROUGHIP;

    /**IP登录拦截模式**/
    private String IPMODE;

    /**登录后默认进入空间**/
    private String TOLOGINSPAGE;

    /**默认文件列表排序方式**/
    private String SORTTYPE;

    /**启用前台AD整合**/
    private String ISOPENAD;

    /**AD域IP地址**/
    private String ADIP;

    /**AD域控制器**/
    private String ADDC;

    /**AD帐户用户名**/
    private String ADUSERNAME;

    /**AD帐户密码**/
    private String ADPASSWORD;

    /**单个文件最大上传大小**/
    private String SMAX;

    /**本地网页语言**/
    private String LOCALLANGUAGE;

    /**是否开启水印**/
    private String IS_WATER_MARK;

    /**文字水印内容**/
    private String WATER_MARK_TEXT;

    /**文字水印颜色**/
    private String TEXT_COLOR;

    /**对齐方式**/
    private String ALIGN;

    /**水印文字大小**/
    private String FONT_SIZE;

    /**水印文字倾斜（旋转）角度**/
    private String ROTATION;

    /**阿里云OSS Bucket名称**/
    private String ALIYUN_OSS_BUCKET_NAME;

    /**阿里云OSS访问域名**/
    private String ALIYUN_OSS_DOMAIN_ENDPOINT;

    /**阿里云OSS Access Key ID**/
    private String ALIYUN_OSS_ACCESS_KEY_ID;

    /**阿里云OSS Access Key Secret**/
    private String ALIYUN_OSS_ACCESS_KEY_SECRET;

    /**默认个人空间大小**/
    private String PSPACE;

    /**允许使用员工数**/
    private String REGISTER_RESTRICTIONS_KEY;

    /**前台网站标题（中）**/
    private String TITLE;

    /**后台网站标题（中）**/
    private String ADMIN_TITLE;

    /**前台网站备案信息（中）**/
    private String WEB_RECORD;

    /**后台网站备案信息（中）**/
    private String ADMIN_WEB_RECORD;

    /**前台主页导航（中）**/
    private String WEB_NAVIGATION;

    /**后台主页导航（中）**/
    private String ADMIN_WEB_NAVIGATION;

    /**移动端主页导航(中)**/
    private String MOBILE_NAVIGATION;

    /**前台网站标题（英）**/
    private String TITLE_ENGLISH;

    /**后台网站标题（英）**/
    private String ADMIN_TITLE_ENGLISH;

    /**前台网站备案信息（英）**/
    private String WEB_RECORD_ENGLISH;

    /**后台网站备案信息（英）**/
    private String ADMIN_WEB_RECORD_ENGLISH;

    /**前台主页导航（英）**/
    private String WEB_NAVIGATION_ENGLISH;

    /**后台主页导航（英）**/
    private String ADMIN_WEB_NAVIGATION_ENGLISH;

    /**加密的上传文件峰值**/
    private String ENCRYPTFILEMAXSIZE;

    /**无峰值限制的文件类型**/
    private String INFINITEENCRYPTFILETYPE;

    /**集群分布是否开启**/
    private String ISOPENCLUSTER;

    /**文件服务器地址**/
    private String FILEDOMAIN;

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String tITLE) {
        TITLE = tITLE;
    }

    public String getLOGO() {
        return LOGO;
    }

    public void setLOGO(String lOGO) {
        LOGO = lOGO;
    }

    public String getNOTHROUGHUPLOAD() {
        return NOTHROUGHUPLOAD;
    }

    public void setNOTHROUGHUPLOAD(String nOTHROUGHUPLOAD) {
        NOTHROUGHUPLOAD = nOTHROUGHUPLOAD;
    }

    public String getTHROUGHUPLOAD() {
        return THROUGHUPLOAD;
    }

    public void setTHROUGHUPLOAD(String tHROUGHUPLOAD) {
        THROUGHUPLOAD = tHROUGHUPLOAD;
    }

    public String getUPLOADMODE() {
        return UPLOADMODE;
    }

    public void setUPLOADMODE(String uPLOADMODE) {
        UPLOADMODE = uPLOADMODE;
    }

    public String getISOPENENCRYPTFILE() {
        return ISOPENENCRYPTFILE;
    }

    public void setISOPENENCRYPTFILE(String iSOPENENCRYPTFILE) {
        ISOPENENCRYPTFILE = iSOPENENCRYPTFILE;
    }

    public String getLOGSTATUS() {
        return LOGSTATUS;
    }

    public void setLOGSTATUS(String lOGSTATUS) {
        LOGSTATUS = lOGSTATUS;
    }

    public String getSINGLELOGIN() {
        return SINGLELOGIN;
    }

    public void setSINGLELOGIN(String sINGLELOGIN) {
        SINGLELOGIN = sINGLELOGIN;
    }

    public String getAUTOBACKUP() {
        return AUTOBACKUP;
    }

    public void setAUTOBACKUP(String aUTOBACKUP) {
        AUTOBACKUP = aUTOBACKUP;
    }

    public String getFILEVERSIONNUM() {
        return FILEVERSIONNUM;
    }

    public void setFILEVERSIONNUM(String fILEVERSIONNUM) {
        FILEVERSIONNUM = fILEVERSIONNUM;
    }

    public String getNOTHROUGHIP() {
        return NOTHROUGHIP;
    }

    public void setNOTHROUGHIP(String nOTHROUGHIP) {
        NOTHROUGHIP = nOTHROUGHIP;
    }

    public String getTHROUGHIP() {
        return THROUGHIP;
    }

    public void setTHROUGHIP(String tHROUGHIP) {
        THROUGHIP = tHROUGHIP;
    }

    public String getTOLOGINSPAGE() {
        return TOLOGINSPAGE;
    }

    public void setTOLOGINSPAGE(String tOLOGINSPAGE) {
        TOLOGINSPAGE = tOLOGINSPAGE;
    }

    public String getSORTTYPE() {
        return SORTTYPE;
    }

    public void setSORTTYPE(String sORTTYPE) {
        SORTTYPE = sORTTYPE;
    }

    public String getISOPENAD() {
        return ISOPENAD;
    }

    public void setISOPENAD(String iSOPENAD) {
        ISOPENAD = iSOPENAD;
    }

    public String getADIP() {
        return ADIP;
    }

    public void setADIP(String aDIP) {
        ADIP = aDIP;
    }

    public String getADDC() {
        return ADDC;
    }

    public void setADDC(String aDDC) {
        ADDC = aDDC;
    }

    public String getADUSERNAME() {
        return ADUSERNAME;
    }

    public void setADUSERNAME(String aDUSERNAME) {
        ADUSERNAME = aDUSERNAME;
    }

    public String getADPASSWORD() {
        return ADPASSWORD;
    }

    public void setADPASSWORD(String aDPASSWORD) {
        ADPASSWORD = aDPASSWORD;
    }

    public String getSMAX() {
        return SMAX;
    }

    public void setSMAX(String sMAX) {
        SMAX = sMAX;
    }

    public String getIPMODE() {
        return IPMODE;
    }

    public void setIPMODE(String iPMODE) {
        IPMODE = iPMODE;
    }

    public String getLOCALLANGUAGE() {
        return LOCALLANGUAGE;
    }

    public void setLOCALLANGUAGE(String lOCALLANGUAGE) {
        LOCALLANGUAGE = lOCALLANGUAGE;
    }

    public String getIS_WATER_MARK() {
        return IS_WATER_MARK;
    }

    public void setIS_WATER_MARK(String iS_WATER_MARK) {
        IS_WATER_MARK = iS_WATER_MARK;
    }

    public String getWATER_MARK_TEXT() {
        return WATER_MARK_TEXT;
    }

    public void setWATER_MARK_TEXT(String wATER_MARK_TEXT) {
        WATER_MARK_TEXT = wATER_MARK_TEXT;
    }

    public String getTEXT_COLOR() {
        return TEXT_COLOR;
    }

    public void setTEXT_COLOR(String tEXT_COLOR) {
        TEXT_COLOR = tEXT_COLOR;
    }

    public String getALIGN() {
        return ALIGN;
    }

    public void setALIGN(String aLIGN) {
        ALIGN = aLIGN;
    }

    public String getFONT_SIZE() {
        return FONT_SIZE;
    }

    public void setFONT_SIZE(String fONT_SIZE) {
        FONT_SIZE = fONT_SIZE;
    }

    public String getROTATION() {
        return ROTATION;
    }

    public void setROTATION(String rOTATION) {
        ROTATION = rOTATION;
    }

    public String getALIYUN_OSS_BUCKET_NAME() {
        return ALIYUN_OSS_BUCKET_NAME;
    }

    public void setALIYUN_OSS_BUCKET_NAME(String aLIYUN_OSS_BUCKET_NAME) {
        ALIYUN_OSS_BUCKET_NAME = aLIYUN_OSS_BUCKET_NAME;
    }

    public String getALIYUN_OSS_DOMAIN_ENDPOINT() {
        return ALIYUN_OSS_DOMAIN_ENDPOINT;
    }

    public void setALIYUN_OSS_DOMAIN_ENDPOINT(String aLIYUN_OSS_DOMAIN_ENDPOINT) {
        ALIYUN_OSS_DOMAIN_ENDPOINT = aLIYUN_OSS_DOMAIN_ENDPOINT;
    }

    public String getALIYUN_OSS_ACCESS_KEY_ID() {
        return ALIYUN_OSS_ACCESS_KEY_ID;
    }

    public void setALIYUN_OSS_ACCESS_KEY_ID(String aLIYUN_OSS_ACCESS_KEY_ID) {
        ALIYUN_OSS_ACCESS_KEY_ID = aLIYUN_OSS_ACCESS_KEY_ID;
    }

    public String getALIYUN_OSS_ACCESS_KEY_SECRET() {
        return ALIYUN_OSS_ACCESS_KEY_SECRET;
    }

    public void setALIYUN_OSS_ACCESS_KEY_SECRET(
            String aLIYUN_OSS_ACCESS_KEY_SECRET) {
        ALIYUN_OSS_ACCESS_KEY_SECRET = aLIYUN_OSS_ACCESS_KEY_SECRET;
    }

    public String getPSPACE() {
        return PSPACE;
    }

    public void setPSPACE(String pSPACE) {
        PSPACE = pSPACE;
    }

    public String getREGISTER_RESTRICTIONS_KEY() {
        return REGISTER_RESTRICTIONS_KEY;
    }

    public void setREGISTER_RESTRICTIONS_KEY(String rEGISTER_RESTRICTIONS_KEY) {
        REGISTER_RESTRICTIONS_KEY = rEGISTER_RESTRICTIONS_KEY;
    }

    public String getADMIN_TITLE() {
        return ADMIN_TITLE;
    }

    public void setADMIN_TITLE(String aDMIN_TITLE) {
        ADMIN_TITLE = aDMIN_TITLE;
    }

    public String getWEB_RECORD() {
        return WEB_RECORD;
    }

    public void setWEB_RECORD(String wEB_RECORD) {
        WEB_RECORD = wEB_RECORD;
    }

    public String getADMIN_WEB_RECORD() {
        return ADMIN_WEB_RECORD;
    }

    public void setADMIN_WEB_RECORD(String aDMIN_WEB_RECORD) {
        ADMIN_WEB_RECORD = aDMIN_WEB_RECORD;
    }

    public String getWEB_NAVIGATION() {
        return WEB_NAVIGATION;
    }

    public void setWEB_NAVIGATION(String wEB_NAVIGATION) {
        WEB_NAVIGATION = wEB_NAVIGATION;
    }

    public String getADMIN_WEB_NAVIGATION() {
        return ADMIN_WEB_NAVIGATION;
    }

    public void setADMIN_WEB_NAVIGATION(String aDMIN_WEB_NAVIGATION) {
        ADMIN_WEB_NAVIGATION = aDMIN_WEB_NAVIGATION;
    }

    public String getENCRYPTFILEMAXSIZE() {
        return ENCRYPTFILEMAXSIZE;
    }

    public void setENCRYPTFILEMAXSIZE(String eNCRYPTFILEMAXSIZE) {
        ENCRYPTFILEMAXSIZE = eNCRYPTFILEMAXSIZE;
    }

    public String getINFINITEENCRYPTFILETYPE() {
        return INFINITEENCRYPTFILETYPE;
    }

    public void setINFINITEENCRYPTFILETYPE(String iNFINITEENCRYPTFILETYPE) {
        INFINITEENCRYPTFILETYPE = iNFINITEENCRYPTFILETYPE;
    }

    public String getTITLE_ENGLISH() {
        return TITLE_ENGLISH;
    }

    public void setTITLE_ENGLISH(String tITLE_ENGLISH) {
        TITLE_ENGLISH = tITLE_ENGLISH;
    }

    public String getADMIN_TITLE_ENGLISH() {
        return ADMIN_TITLE_ENGLISH;
    }

    public void setADMIN_TITLE_ENGLISH(String aDMIN_TITLE_ENGLISH) {
        ADMIN_TITLE_ENGLISH = aDMIN_TITLE_ENGLISH;
    }

    public String getWEB_RECORD_ENGLISH() {
        return WEB_RECORD_ENGLISH;
    }

    public void setWEB_RECORD_ENGLISH(String wEB_RECORD_ENGLISH) {
        WEB_RECORD_ENGLISH = wEB_RECORD_ENGLISH;
    }

    public String getADMIN_WEB_RECORD_ENGLISH() {
        return ADMIN_WEB_RECORD_ENGLISH;
    }

    public void setADMIN_WEB_RECORD_ENGLISH(String aDMIN_WEB_RECORD_ENGLISH) {
        ADMIN_WEB_RECORD_ENGLISH = aDMIN_WEB_RECORD_ENGLISH;
    }

    public String getWEB_NAVIGATION_ENGLISH() {
        return WEB_NAVIGATION_ENGLISH;
    }

    public void setWEB_NAVIGATION_ENGLISH(String wEB_NAVIGATION_ENGLISH) {
        WEB_NAVIGATION_ENGLISH = wEB_NAVIGATION_ENGLISH;
    }

    public String getADMIN_WEB_NAVIGATION_ENGLISH() {
        return ADMIN_WEB_NAVIGATION_ENGLISH;
    }

    public void setADMIN_WEB_NAVIGATION_ENGLISH(
            String aDMIN_WEB_NAVIGATION_ENGLISH) {
        ADMIN_WEB_NAVIGATION_ENGLISH = aDMIN_WEB_NAVIGATION_ENGLISH;
    }

    public String getISOPENCLUSTER() {
        return ISOPENCLUSTER;
    }

    public void setISOPENCLUSTER(String iSOPENCLUSTER) {
        ISOPENCLUSTER = iSOPENCLUSTER;
    }

    public String getFILEDOMAIN() {
        return FILEDOMAIN;
    }

    public void setFILEDOMAIN(String fILEDOMAIN) {
        FILEDOMAIN = fILEDOMAIN;
    }

    public String getMOBILE_NAVIGATION() {
        return MOBILE_NAVIGATION;
    }

    public void setMOBILE_NAVIGATION(String mOBILE_NAVIGATION) {
        MOBILE_NAVIGATION = mOBILE_NAVIGATION;
    }

}
