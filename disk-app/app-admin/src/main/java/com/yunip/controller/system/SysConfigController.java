package com.yunip.controller.system;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminException;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.query.SysConfigQuery;
import com.yunip.service.config.ICommonBaseDataService;
import com.yunip.utils.http.HttpUtil;

@Controller
@RequestMapping("/commonbasedata")
public class SysConfigController extends BaseController {

    @Resource(name = "iCommonBaseDataService")
    ICommonBaseDataService commonBaseDataService;

    /**
     * 重新加载配置
     * @param request
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/reload")
    @ResponseBody
    public JsonData<?> reloadSysConfig(HttpServletRequest request) {
        JsonData<?> data = new JsonData<String>();
        //后台
        SysConfigHelper.reload();
        //云盘
        HttpUtil.post(SystemContant.DISK_SERVICE_URL
                + SystemContant.RELOAD_PATH, "", "", "UTF-8");
        //文件服务器
        HttpUtil.post(SystemContant.FILE_SERVICE_URL
                + SystemContant.RELOAD_PATH, "", "", "UTF-8");
        return data;
    }

    /**
     * 系统配置列表
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/index")
    public String listSysConfig(HttpServletRequest request, SysConfigQuery query) {
        query.setConfigCode(SystemContant.OPENCODE);
        query = commonBaseDataService.listSysConfig(query);
        request.setAttribute("query", query);
        return "sysconfig/index";
    }

    /**
     * 跳转新增系统参照信息
     * @param request
     * @param id
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toaddsysconfig")
    public String toAddSysConfig(HttpServletRequest request) {
        return "sysconfig/add";
    }

    /**
     * 跳转编辑系统参照信息
     * @param request
     * @param id
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toeditsysconfig")
    public String toEditSysConfig(HttpServletRequest request,
            SysConfig sysConfig) {
        SysConfig config = commonBaseDataService.getSysConfig(
                sysConfig.getConfigCode(), sysConfig.getConfigKey());
        request.setAttribute("config", config);
        return "sysconfig/edit";
    }

    /**
     * 新增系统参照信息
     * @param request
     * @param sysConfig
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/addsysconfig")
    @ResponseBody
    public JsonData<?> addSysConfig(HttpServletRequest request,
            SysConfig sysConfig) {
        JsonData<String> data = new JsonData<String>();
        sysConfig.setConfigCode(SystemContant.OPENCODE);
        int sub = commonBaseDataService.saveSysConfig(sysConfig);
        if (sub <= 0) {
            throw new MyException(AdminException.ERROR);
        }
        return data;
    }

    /**
     * 修改系统参照信息
     * 
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("/editsysconfig")
    @ResponseBody
    public JsonData<?> editSysConfig(HttpServletRequest request,
            SysConfig sysConfig) {
        JsonData<String> data = new JsonData<String>();
        int sub = commonBaseDataService.editSysConfig(sysConfig);
        if (sub <= 0) {
            throw new MyException(AdminException.ERROR);
        }
        return data;
    }

    /**
     * 跳转logo设置
     * toLogo(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/logo")
    public String toLogo(HttpServletRequest request) {
        return "sysconfig/logo";
    }

    @RequestMapping("/uplogo")
    @ResponseBody
    public JsonData<?> upLogo(HttpServletRequest request)
            throws IllegalStateException, IOException {
        JsonData<String> data = new JsonData<String>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String backProjectPath = request.getSession().getServletContext().getRealPath(
                "/");//本地存放路径
        
        //前台项目存放路径
        String frontProjectPath = HttpUtil.post(SystemContant.DISK_SERVICE_URL
                + SystemContant.GET_PROJECT_PATH, "", "", "UTF-8");
        String regEx = "[<>]";
        Pattern p = Pattern.compile(regEx);
        Matcher mat = p.matcher(frontProjectPath);
        mat.matches();
        //        value = mat.replaceAll("").trim();
        JSONObject jsonObject = JSONObject.fromObject(frontProjectPath);
        frontProjectPath = jsonObject.getString("result");
        //文件项目存放路径
        String andriodProjectPath = HttpUtil.post(SystemContant.FILE_SERVICE_URL
                + SystemContant.GET_PROJECT_PATH, "", "", "UTF-8");
        mat = p.matcher(andriodProjectPath);
        mat.matches();
        jsonObject = JSONObject.fromObject(andriodProjectPath);
        andriodProjectPath = jsonObject.getString("result");
        
        //前台登录logo
        MultipartFile fLoginLogoFile = multipartRequest.getFile("frontLoginLogo");
        if (fLoginLogoFile != null && fLoginLogoFile.getSize() > 0) {
            File localFLoginLogoFile = new File(frontProjectPath
                    + "/static/images/logo_login.png");
            fLoginLogoFile.transferTo(localFLoginLogoFile);
        }
        //前台登录背景图
        MultipartFile fLoginBackgroundFile = multipartRequest.getFile("frontLoginBackground");
        if (fLoginBackgroundFile != null && fLoginBackgroundFile.getSize() > 0) {
            File localFLoginBackgroundFile = new File(frontProjectPath
                    + "/static/images/login_bg.jpg");
            fLoginBackgroundFile.transferTo(localFLoginBackgroundFile);
        }
        //前台首页logo
        MultipartFile fLogoFile = multipartRequest.getFile("frontLogo");
        if (fLogoFile != null && fLogoFile.getSize() > 0) {
            File localFLogoFile = new File(frontProjectPath
                    + "/static/images/logo1.png");
            fLogoFile.transferTo(localFLogoFile);
        }
        //前台网址logo
        MultipartFile fWebLogoFile = multipartRequest.getFile("frontWebLogo");
        if (fWebLogoFile != null && fWebLogoFile.getSize() > 0) {
            File localFWebLogoFile = new File(frontProjectPath + "/favicon.ico");
            fWebLogoFile.transferTo(localFWebLogoFile);
        }
        //后台登录logo
        MultipartFile bLoginLogoFile = multipartRequest.getFile("backLoginLogo");
        if (bLoginLogoFile != null && bLoginLogoFile.getSize() > 0) {
            File localBLoginLogoFile = new File(backProjectPath
                    + "/plugins/static/h-ui/images/logo.png");
            bLoginLogoFile.transferTo(localBLoginLogoFile);
        }
        //后台登录背景图
        MultipartFile bLoginBackgroundFile = multipartRequest.getFile("backLoginBackground");
        if (bLoginBackgroundFile != null && bLoginBackgroundFile.getSize() > 0) {
            File localBLoginBackgroundFile = new File(backProjectPath
                    + "/plugins/static/h-ui/images/admin-login-bg.jpg");
            bLoginBackgroundFile.transferTo(localBLoginBackgroundFile);
        }
        //后台网址logo
        MultipartFile bWebLogoFile = multipartRequest.getFile("backWebLogo");
        if (bWebLogoFile != null && bWebLogoFile.getSize() > 0) {
            File localBWebLogoFile = new File(backProjectPath + "/favicon.ico");
            bWebLogoFile.transferTo(localBWebLogoFile);
        }
        //安卓logo存放地址
        String androidPath = SysConfigHelper.getValue(SystemContant.OPENCODE,
                SystemContant.FILEROOTPATH);
        androidPath = androidPath + SystemContant.ANDROIDLOGOPATH;
        //安卓端logo
        MultipartFile andriodLogoFile = multipartRequest.getFile("andriodLogo");
        if (andriodLogoFile != null && andriodLogoFile.getSize() > 0) {
            File localAndroidLogoFile = new File(andriodProjectPath
                    + "/static/images/androidlogo.png");
            andriodLogoFile.transferTo(localAndroidLogoFile);
        }
        return data;
    }

}
