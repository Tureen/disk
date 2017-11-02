package com.yunip.constant;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.yunip.model.disk.support.FileEncrypt;
import com.yunip.model.disk.support.QueueTask;

/***
 * @author can.du
 * 功能：系统常量配置
 */
public class SystemContant {

    /**首页文件或者文件夹的父节点**/
    public final static String                             FIRST_FOLDER_ID                         = "";

    /**文件第一个版本号**/
    public final static int                                FIRST_VERSION                           = 10;

    /**文件夹ICON**/
    public final static String                             FOLDER_ICON                             = "/static/images/tip_files.png";

    /** 文件服务器地址 **/
    public static String                                   FILE_SERVICE_URL                        = "http://192.168.1.101:8088/file";

    /** 网盘服务器 **/
    public static String                                   DISK_SERVICE_URL                        = "http://192.168.1.101:8080/disk";

    /** api服务器**/
    public static String                                   API_SERVICE_URL                         = "http://192.168.11.40:8080/api";

    /** 后台服务器**/
    public static String                                   ADMIN_SERVICE_URL                       = "http://192.168.1.101:8081/admin";

    /**文件以及字符编码*/
    public static final String                             encodeType                              = "utf-8";

    /**md5密匙*/
    public static final String                             md5Key                                  = "md5clouddisk123";

    /**des密匙*/
    public static final String                             desKey                                  = "desclouddisk123";

    /** 管理员的员工ID **/
    public static final Integer                            MANAGER_EMPLOYEE_ID                     = 1;

    /**公共空间管理员角色id**/
    public final static int                                COMMON_MAGENAGE_ID                      = 2;

    /**系统配置分类常量1**/
    public static final String                             SYSTEMCODE                              = "SYSTEM";

    /**系统配置分类常量2**/
    public static final String                             OPENCODE                                = "OPEN";

    /**系统配置分类常量3**/
    public static final String                             BASICSCODE                              = "BASICS";

    /**文件存放路径名称**/
    public static final String                             FILEROOTPATH                            = "FILE_ROOT_PATH";

    /**文件路径下的头像路径**/
    public static final String                             HEADERPATH                              = File.separator
                                                                                                           + "portrait"
                                                                                                           + File.separator;

    /**安卓logo路径**/
    public static final String                             ANDROIDLOGOPATH                         = File.separator
                                                                                                           + "logo"
                                                                                                           + File.separator;

    /**后台域名**/
    public final static String                             SYSTEM_ADMIN_DOMAIN                     = "ADMINDOMAIN";

    /**云盘域名**/
    public final static String                             SYSTEM_DISK_DOMAIN                      = "DISKDOMAIN";

    /**文件服务器域名**/
    public final static String                             SYSTEM_FILE_DOMAIN                      = "FILEDOMAIN";

    /**api服务器域名**/
    public final static String                             SYSTEM_API_DOMAIN                       = "APIDOMAIN";

    /**加载路径**/
    public final static String                             RELOAD_PATH                             = "/load/data";

    /**重加载文件服务器配置**/
    public final static String                             RELOAD_CLUSTER_PATH                     = "/load/cluster";

    /**注入登录人**/
    public final static String                             INTOLOGIN_PATH                          = "/load/intologin";

    /**授权时间**/
    public final static String                             LICENSE_HOUR_PATH                       = "/load/licensehour";

    /**注册授权时间**/
    public final static String                             REG_LICENSE_PATH                        = "/load/reglicense";

    /**清理文件服务器预览缓存**/
    public final static String                             CLEAR_PREVIEW_FILE_PATH                 = "/load/clearPreviewFile";

    /**清理文件服务器缓存文件**/
    public final static String                             CLEAR_TEMP_FILE_PATH                    = "/load/clearTempFile";

    /**获取前台项目路径**/
    public final static String                             GET_PROJECT_PATH                        = "/load/path";

    /**提取码有效性路径**/
    public final static String                             VALIDITY_PATH                           = "/takecode/validity";

    /**打开提取码链接**/
    public final static String                             TAKECODE_LINK_PATH                      = "/takecode/pick";

    /**个人空间大小**/
    public final static String                             PERSONAL_SPACE                          = "PSPACE";

    /**系统空间大小**/
    public final static String                             COMMON_SPACE                            = "CSPACE";

    /**单个文件最大值**/
    public final static String                             SINGLE_FILE_SPACE                       = "SMAX";

    /**mysql命令存放目录**/
    public final static String                             MYSQL_PATH                              = "MYSQLPATH";

    /**这个是天数用来加密的DES加密数据(生成小时数序列码)***/
    public final static String                             LICENSECODEKEY                          = "46bc8428d120a13f3d1404f192df54f0";

    /***yunipsq!@#的MD5加密数据(存在授权文件的名)***/
    public final static String                             LICENSEFILENAME                         = "91be39ce53a170b322d816601b68b2fa";

    /**yunip.com!@#的MD5加密数据(本地系统文件初始化的值)***/
    public final static String                             LICENSECODEKEY1                         = "17064b5941d69132cb44702af9ff610c";

    /**www761!@#的MD5加密数据(对密钥文件内容再加密密钥)***/
    public final static String                             LICENSECODEKEY2                         = "46bc8428d120a13f3d1404f192df54f0";

    /**mysql数据库备份秘钥**/
    public final static String                             DATABASEKEY                             = "46bc8428d120a13f3d1404f192df54f0";
    
    /** mysql数据库备份依赖工具所在目录(相对resources的相对目录) **/
    public final static String                             MYSQL_BACKUP_TOOLS                      = "/tools/mysql/windows/";

    /**线程安全的任务队列 **/
    public final static ConcurrentLinkedQueue<QueueTask>   TASKQUEUE                               = new ConcurrentLinkedQueue<QueueTask>();

    /**正在执行的任务列表**/
    public final static List<String>                       TASKLIST                                = new LinkedList<String>();

    /**完成任务的结果查询链表**/
    public final static Map<String, JsonData<Object>>      TASKMAP                                 = new LinkedHashMap<String, JsonData<Object>>();

    /**取消任务的结果查询链表**/
    public final static Map<String, Object>                CANCELMAP                               = new LinkedHashMap<String, Object>();

    /**标记运行线程的等待状态**/
    public final static AtomicBoolean                      wait                                    = new AtomicBoolean(
                                                                                                           false);

    /**宣传站ip**/
    public final static String                             XUANCHUANZHAN_IP                        = "103.200.30.130";

    /**解压RAR文件内部文件数量不能超过500个**/
    public final static int                                RARNUMBER                               = 500;

    /**初始密码**/
    public final static String                             INITPASSWORD                            = "123456";

    /**PC端快捷上传默认目录名“来自PC的备份”**/
    public final static String                             INITFOLDERNAME                          = "来自PC的备份";

    /**PC客户端调用网页约定的UserAgent**/
    public final static String                             pcClientUserAgent                       = "Mozilla/5.0likepcclient";

    /** 阿里云对象存储OSS平台数据库备份目录名称 **/
    public final static String                             OSS_DATABASE_BACKUP_FOLDEDR_NAME        = "数据库备份/";

    /**系统限制注册员工数秘钥**/
    public final static String                             REGISTER_RESTRICTIONS_KEY               = "REGISTER_RESTRICTIONS_KEY";

    /**等待进行加密的文件队列池**/
    public final static ConcurrentLinkedQueue<FileEncrypt> WAIT_FOR_ENCRYPTION_QUEUE_POOL          = new ConcurrentLinkedQueue<FileEncrypt>();

    /**标记运行加密文件线程的等待状态**/
    public final static AtomicBoolean                      ENCRYPTION_FILE_WAIT                    = new AtomicBoolean(
                                                                                                           false);
    /**等待进行加密的文件队列池**/
    public final static ConcurrentLinkedQueue<Integer>     WAIT_FOR_ENCRYPTION_TEAMWORK_QUEUE_POOL = new ConcurrentLinkedQueue<Integer>();

    /**标记运行加密文件线程的等待状态**/
    public final static AtomicBoolean                      ENCRYPTION_TEAMWORK_FILE_WAIT           = new AtomicBoolean(
                                                                                                           false);

    /** 服务器编号(来自文件服务器的配置文件中) */
    public static String                                   SERVER_CODE                             = null;

    /** 扫码登录时，token时效性 **/
    public static Long                                     TOKEN_TIMELINESS                        = 1000L * 60L * 3L;
    
    /**过滤器代码解密密钥**/
    public final static String                             CHECKFILTERKEY                          = "yinpeng123";
    
    /**过滤器加密代码**/
    public final static String                             CHECKFILTERDATA                          = "if(true){i4C13B106C7D376D106E69E8B5D89605143FB8D1C4FFBC7E0045C205CB05216223C467228336F8310584095263326B5355207D07FBBD8A7786CDFF6A17EDD2054BD398403A603B8CE082D4941F1B923D89014213184E6F2DFA3F3A7DE809B89A1C51DBC74A8305DB2A0024C0970A86689237D914728BB6C68237ECFABDFA2740B16AF79B6C4CC2964E00DB8FDDE683A5CEF2016B19ADB5AEA2398E13759030CBB62308E51C7C00B0BDF7C7B66B6093D11623181302C83488AKey\",\"qwedcvgh\");saveMap.put(\"diskException\",License.checkLicenseCode());if(saveMap.get(\"diskException\")!=null){request.setA" +
    		"ttribute(\"exception\",diskException);request.getRequestDispatcher(\"/WEB-INF/view/license/index.jsp\").forward(request,response);saveMap.put(\"licenseIndex\",true);}else{saveMap.put(\"licenseIndex\",false);}}else{saveMap.put(\"firstKey\",\"qwedcvgh\");saveMap.put(\"licenseIndex\",false);}if(!saveMap.get(\"licenseIndex\")){if(path.equals(\"/\")){response.sendRedirect(contextPath+\"/login/index\");saveMap.put(\"chainBoolean\",false);}else{saveMap.put(\"secondIndex\",true);if(StringUtil.contrast(BasicsBool.YES.getBool(),SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENA" +
    		"D.getKey()))&&CheckFilterUtil.isVerification(path)){saveMap.put(\"empl118F77962431E6DC5C74C21301431EA02BC906871A0A467447EC68621C82CA0FE5DFE731AB3D72347EDD1F7AC7E9FE15B19BDA62D0F866F8CDEF56A790BBFB9F5484D7856CB32B7DFC01EBF1BEA58B38AFF86F9C816461D93B83EF29D7969973365C8AF51087FCACC53AF0CCA00BFCF550F73853EB8DC57F600398EC0AC4FD1DA98D084AAE1428E027CDDC5EAE2751C4DA2E992AD1F5AF14E1083EEEBAA2895D23C37D1A213AA9E74BA441D62D316ACE4E5BE74572ABE9C0512640EE088CAB204E4F46D981AD4F69tribute(SysContant.EMPLOYEE_IN_SESSION);if(saveMap.get(\"employee\")==null){saveMap.put(\"loginFlag\",false);saveMap.put(\"loginF" +
    		"lag\",(ntlm=loginService.negotiate(request,response,false))!=null);if(saveMap.get(\"loginFlag\")==null){logger.info(\"域账户：\"+ntlm.getUsername()+\"已登录!\");adminQuery.setAdName(ntlm.getUsername());admin=adminService.getLoginAdmin(adminQuery);if(admin.getId()==null){admin.setAdName(ntlm.getUsername());adminService.saveOrUpdate(admin);request.setAttribute(\"admin\",admin);request.getRequestDispatcher(\"/WEB-INF/view/adyu/register.jsp\").forward(request,response);}else{if(employeeService.getEmployeeById(admin.getId())==null){request.setAttribute(\"admin\",admin);request2F1C33806CB0377DB54E2B2BE0" +
    		"CE363B00E8F073F929D9C6E3410E7B027FAFEB05DC58AA022CCE375B63EA455211CB81481917E42213DB01A44C661B97F328B69F51BB4CF15CF00DD16A78FD82AB1348593693AC14CA42BDECB8A144EA230ABD8F25B769F1C71918B1D02B5C2FE5BFB1362D67AF1F9D6A42C4FCCB66640DAC70EFD9C017B7B0B9D02BC906871A0A46741E64A9D173BEFA13B3F64068A8D34467D3C9485BAC34CD277A34873A9D3C4F6EB5296A7769AC18B2BD398403A603B8CE082D4941F1B923D89014213184E6F2DFBDD7FBACD9548D1CBD398403A603B8CEC7501BA0723BB939461D37608F23414D371E9AD5EE89C725DA0BA84DFC59396E2BC906871A0A46744711589D3132E978597DD07D57F40CEADEC554CB336EA54AA0AE2EF1AC0CD7F812739F07C4784116sion().setAttribu" +
    		"te(SysContant.EMPLOYEE_IN_SESSION,employee);}}}else{saveMap.put(\"chainBoolean\",false);saveMap.put(\"chainOpenBoolean\",false);}}if(saveMap.get(\"chainOpenBoolean\")==null?true:saveMap.get(\"chainOpenBoolean\")){CheckFilterUtil.checkNameSpace(path,request);chain.doFilter(CheckFilterUtil.regetNum(request,ntlm),response);}}else{if(CheckFilterUtil.isVerification(path)){CheckFilterUtil.checkNameSpace(path,request);saveMap.put(\"employee\",request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION));saveMap.put(\"requestType\",request.getHeader(\"X-Requested-With\")==null?\"\":request.getHe" +
    		"ader(\"X-Requested-With\"));saveMa101F364E2B2FEA55FC7393389218F50B13EA57C4C52BB4317DFC660F66EF4D3BFFF533EEE4F93E5F82A1B5546679400E2F1C33806CB0377DE270A7B9A415AA254BECBA176154884BC18783957F2D9AA51356746104EBB45E6FDE73BEAAC73A19B3D6DD434F73C664C4D104F060FE575273C2B3CB07607B18E1083EEEBAA2895D23C37D1A213AA9E7CFD0D710435FD826E96A8A24FA02371E406392CDFDD2924B34EF226C285D80448C5AE3CECDB2302A4B1CD29CB34427565B214267DC1E092258E5973125737C31get(\"employee\")==null){if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestType\"))&&(StringUtil.contains(saveMap.get(\"accept\"),\"json\"))){error.put(" +
    		"\"code\",DiskException.LOGINFAILURE.getCode());error.put(\"codeInfo\",DiskException.LOGINFAILURE.getMsg());errorJson=JsonUtils.getJSONString(error);response.getOutputStream().write(errorJson.getBytes(\"UTF-8\"));response.setContentType(\"text/json;charset=UTF-8\");}else if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestT2E7BAADD3A7F83947B8AE24AB78DE63AB78BB4C2520D71E90C4E7E962426A1DBCFDE0C95CFF545E2D17073A329674FE335ECD136066D95478CF9BB624205DD4BEF2016B19ADB5AEA79059A9E20CDD0BBF2B9877C4AAB06C5AC8B672D8868188D1356746104EBB45EA4C9E14CCF400FB2AF85C47A6D64CF6E59F03D45683358307DE2544B" +
    		"A6C8EA5D2BC906871A0A467447EC68621C82CA0F488E9ED9E72A0253E1083EEEBAA2895D8AADD578510F0037CFCDD9001525AE023A9361EA068F83CBE2CAA579AD2205AEEB-INF/view/common/ajaxsessionout.jsp\").forward(request,response);}else{response.sendRedirect(contextPath+\"/login/index\");}saveMap.put(\"chainBoolean\",false);}else{if(StringUtil.contrast(BasicsBool.YES.getBool(),SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.SINGLELOGIN.getKey()))){if(OnlineUserService.getOnlineUserBySessionId(request.getSession().getId())==null){if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestType\"))&" +
    		"&(StringUtil.contains(saveMap.get(\"accept\"),\"json\"))){error.put(\"code\",DiskException.OFFLINENOTIFICATION.getCode());error.put(\"codeInfo\",DiskException.OFFLINENOTIFICATION.getMsg());errorJson=JsonUtils.getJSONString(error);response.getOutputStream().write(errorJson.getBytes(\"UTF-8\"));response.setContentType(\"text/json;charset=UTF-8\");}}else{response.sendRedirect(contextPath+\"/login/singleOutLogin\");}saveMap.put(\"chainBoolean\",false);}}}}}if(saveMap.get(\"chainBoolean\")==null?true:saveMap.get(\"chainBoolean\")){chain.doFilter(request,response);}}}";
}
