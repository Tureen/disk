/*
 * 描述：授权操作文件服务器控制器
 * 创建人：jian.xiong
 * 创建时间：2016-5-12
 */
package com.yunip.controller.upload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.LocalLanguageHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.I18nContant;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.enums.fileservers.FileServersType;
import com.yunip.manage.AuthorityShareManager;
import com.yunip.manage.FileManager;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.model.UploadUser;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.service.IEmployeeService;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.StringUtil;

/**
 * 授权操作文件服务器控制器
 */
@RequestMapping("/authorize")
@Controller
public class AuthorizeController extends FileManagerBaseController{
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "authorityShareManager")
    private AuthorityShareManager authorityShareManager;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;

    /**
     * 验证上传身份是否合法并进入文件上传页面
     */
    @RequestMapping("/toUploadPage")
    public String uploadPage(HttpServletRequest request, ModelMap modelMap){
        String folderId = request.getParameter("folderId");
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        if(StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(spaceType)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        EnumSpaceType mySpaceType = null;
        if(spaceType.equals(generateMd5Str(EnumSpaceType.PUBLIC_SPACE.getCode()))){//判断是否为公共空间
            mySpaceType = EnumSpaceType.PUBLIC_SPACE;
        }else if(spaceType.equals(generateMd5Str(EnumSpaceType.PRIVATE_SPACE.getCode()))){
            mySpaceType = EnumSpaceType.PRIVATE_SPACE;
        }
        if(mySpaceType == null){
            throw new MyException(FileServersException.PARAMERROR);
        }
        UploadUser user = this.getMyInfo(request);
        String eId = user.getIdentity();
        List<String> folderList = new ArrayList<String>();
        folderList.add(folderId);
        //查询当前登录所属角色
        List<Integer> roles = getEmployeeRoles(eId);
        //判断是否具备上传权限
        if(!authorityShareManager.getOpenAuthById(null, folderList, Integer.parseInt(eId), roles, FileServersType.UPLOAD, mySpaceType)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_CURRENT_FOLDER_UPLOAD_AUTHORITY, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        
        /*String folderId = request.getParameter("folderId");
        String identity = request.getParameter("identity");//人员ID
        String token = request.getParameter("token");//验证token
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        
        if(StringUtil.nullOrBlank(identity) || StringUtil.nullOrBlank(token) || StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(spaceType)){
            String msg = "非法请求！";
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        UploadUser user = (UploadUser) request.getSession().getAttribute(Constant.UPLOADUSER_IN_SESSION);
        if(user == null || !user.getEncryIdentity().equals(identity)){//登录的用户改变后，重新验证登录
            if(!(generateMd5Str(identity)).equals(token)){
                String msg = "身份验证不通过！";
                modelMap.put("msg", msg);
                return "common/prompt";
            }else{
                Des desObj = new Des();
                String dec = desObj.strDec(identity, SystemContant.desKey, null, null);
                UploadUser uploadUser = new UploadUser();
                uploadUser.setIdentity(dec);
                uploadUser.setToken(token);
                user = uploadUser;
                request.getSession().setAttribute(Constant.UPLOADUSER_IN_SESSION, uploadUser);
            }
        }*/
        //验证当前需要上传的目录是否有效
        if(StringUtil.nullOrBlank(folderId)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }else{
            Integer employeeId = Integer.parseInt(user.getIdentity());
            List<String> fileList = new ArrayList<String>();
            if(mySpaceType.getCode().equals(EnumSpaceType.PUBLIC_SPACE.getCode())){//判断是否为公共空间
                employeeId = SystemContant.MANAGER_EMPLOYEE_ID;//特殊处理，公共空间操作时使用的员工ID默认为1
            }
            Folder folder = fileManager.getFolderAndFilesByFolderId(Integer.parseInt(folderId), employeeId);
            if (folder != null && folder.getId() == 0) {//设置最上层默认目录名
                folder.setFolderName(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_PERSONAL_FILE, request));
            }
            if(folder != null && folder.getFiles() != null && folder.getFiles().size() > 0){
                for(File file : folder.getFiles()){
                    fileList.add(file.getFileName());
                }
            }
            
            if(folder == null){
                String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
                modelMap.put("msg", msg);
                return "common/prompt";
            }else{
                String folderJsonStr = JsonUtils.list2json(fileList);
                request.setAttribute("spaceType", spaceType);
                request.setAttribute("currentFolder", folder);
                request.setAttribute("fileList", folderJsonStr);
                
                //描述允许上传类型的提示
                StringBuffer uploadTypeDescribe = new StringBuffer();
                String uploadModelType = SysConfigHelper.getValue(SystemContant.BASICSCODE, "UPLOADMODE");//可上传文件类型验证模式
                int uploadModel = 0;
                String validateRule = SysConfigHelper.getValue(SystemContant.BASICSCODE, uploadModelType);
                if(uploadModelType.equals(BasicsInfoCode.THROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//允许上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CAN_ONLY_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 1;
                }else if(uploadModelType.equals(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//禁止上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 2;
                }
                //允许单个文件上传的最大大小
                request.setAttribute("maxSize", SysConfigHelper.getValue(SystemContant.BASICSCODE, "SMAX"));
                //上传验证模式
                request.setAttribute("uploadModel", uploadModel);
                List<String> validateFormat = new ArrayList<String>();
                if(!StringUtil.nullOrBlank(validateRule)){
                    validateFormat = Arrays.asList(validateRule.split(","));
                }
                request.setAttribute("validateRule", JsonUtils.list2json(validateFormat));
                request.setAttribute("uploadTypeDescribe", uploadTypeDescribe.toString());
                request.setAttribute("usableDiskSize", fileManager.getEmployeeUsableDiskSize(folder.getEmployeeId()));
            }
        }
        return "upload/upload";
    }
    
    /**
     * 进入文件上传文件夹页面
     */
    @RequestMapping("/toFolderUploadPage")
    public String uploadFolderPage(HttpServletRequest request, ModelMap modelMap){
        if(!getBrowerIsSupportFolderUpload(request)){//判断浏览器版本是否支持文件夹上传
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CURRENT_BROWSER_VERSION_NOT_SUPPORTED, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        String folderId = request.getParameter("folderId");
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        if(StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(spaceType)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        EnumSpaceType mySpaceType = null;
        if(spaceType.equals(generateMd5Str(EnumSpaceType.PUBLIC_SPACE.getCode()))){//判断是否为公共空间
            mySpaceType = EnumSpaceType.PUBLIC_SPACE;
        }else if(spaceType.equals(generateMd5Str(EnumSpaceType.PRIVATE_SPACE.getCode()))){
            mySpaceType = EnumSpaceType.PRIVATE_SPACE;
        }
        if(mySpaceType == null){
            throw new MyException(FileServersException.PARAMERROR);
        }
        UploadUser user = this.getMyInfo(request);
        String eId = user.getIdentity();
        List<String> folderList = new ArrayList<String>();
        folderList.add(folderId);
        //查询当前登录所属角色
        List<Integer> roles = getEmployeeRoles(eId);
        //判断是否具备上传权限
        if(!folderId.equals("-1") && !authorityShareManager.getOpenAuthById(null, folderList, Integer.parseInt(eId), roles, FileServersType.UPLOAD, mySpaceType)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_CURRENT_FOLDER_UPLOAD_AUTHORITY, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        
        //验证当前需要上传的目录是否有效
        if(StringUtil.nullOrBlank(folderId)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }else{
            Integer employeeId = Integer.parseInt(user.getIdentity());
            if(mySpaceType.getCode().equals(EnumSpaceType.PUBLIC_SPACE.getCode())){//判断是否为公共空间
                employeeId = SystemContant.MANAGER_EMPLOYEE_ID;//特殊处理，公共空间操作时使用的员工ID默认为1
            }
            Folder folder = fileManager.getFolderAndFilesByFolderId(Integer.parseInt(folderId), employeeId);
            if(folderId.equals("-1")){
                folder.setId(-1);
                folder.setEmployeeId(Integer.parseInt(eId));
            }
            
            if(folder == null){
                String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
                modelMap.put("msg", msg);
                return "common/prompt";
            }else{
                request.setAttribute("spaceType", spaceType);
                request.setAttribute("currentFolder", folder);
                
                //描述允许上传类型的提示
                StringBuffer uploadTypeDescribe = new StringBuffer();
                String uploadModelType = SysConfigHelper.getValue(SystemContant.BASICSCODE, "UPLOADMODE");//可上传文件类型验证模式
                int uploadModel = 0;
                String validateRule = SysConfigHelper.getValue(SystemContant.BASICSCODE, uploadModelType);
                if(uploadModelType.equals(BasicsInfoCode.THROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//允许上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CAN_ONLY_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 1;
                }else if(uploadModelType.equals(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//禁止上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 2;
                }
                //允许单个文件上传的最大大小
                request.setAttribute("maxSize", SysConfigHelper.getValue(SystemContant.BASICSCODE, "SMAX"));
                //上传验证模式
                request.setAttribute("uploadModel", uploadModel);
                List<String> validateFormat = new ArrayList<String>();
                if(!StringUtil.nullOrBlank(validateRule)){
                    validateFormat = Arrays.asList(validateRule.split(","));
                }
                request.setAttribute("validateRule", JsonUtils.list2json(validateFormat));
                request.setAttribute("uploadTypeDescribe", uploadTypeDescribe.toString());
                request.setAttribute("usableDiskSize", fileManager.getEmployeeUsableDiskSize(folder.getEmployeeId()));
            }
        }
        if(isPCClientRequest(request)){//判断是否来自PC客户端程序的请求
            return "upload/pcClientStreamUpload";
        }else{
            return "upload/streamUpload";
        }
    }
    
    /**
     * 通过iframe登录文件服务器保持session
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public void login(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String identity = request.getParameter("identity");
        String token = request.getParameter("token");
        logger.info("请求参数：" + identity + "," + token);
        UploadUser user = (UploadUser) request.getSession().getAttribute(Constant.UPLOADUSER_IN_SESSION);
        if(user == null || !user.getEncryIdentity().equals(identity)){//登录的用户改变后，重新验证登录并重置session
            if(StringUtil.nullOrBlank(identity) || StringUtil.nullOrBlank(token)){//参数不对移除session
                request.getSession().removeAttribute(Constant.UPLOADUSER_IN_SESSION);
            }else{
                if(generateMd5Str(identity).equals(token)){//验证通过，登录成功
                    Des desObj = new Des();
                    String dec = desObj.strDec(identity, SystemContant.desKey, null, null);
                    Employee employee = employeeService.getEmployeeByEmployeeId(dec);
                    if(employee != null){
                        UploadUser uploadUser = new UploadUser();
                        uploadUser.setIdentity(dec);
                        uploadUser.setName(employee.getEmployeeName());
                        uploadUser.setToken(token);
                        request.getSession().setAttribute(Constant.UPLOADUSER_IN_SESSION, uploadUser);
                        setLocalLanguage(request, response);//cookie中设置本地化语言
                    }
                }else{//验证不通过移除session
                    request.getSession().removeAttribute(Constant.UPLOADUSER_IN_SESSION);
                }
            }
        }else{
            setLocalLanguage(request, response);//cookie中设置本地化语言
        }
    }
    
    /**
     * cookie中设置本地化语言
     */
    private void setLocalLanguage(HttpServletRequest request,HttpServletResponse response){
        String localLanguage = request.getParameter("language");
        setLanguageCookie(localLanguage, response);
    }
    
    /**
     * 通过jsonp退出登录文件服务器
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    @ResponseBody
    public void loginOut(HttpServletRequest request,HttpServletResponse response) throws Exception{
        JsonData<String> jsonData = new JsonData<String>();
        UploadUser user = (UploadUser) request.getSession().getAttribute(Constant.UPLOADUSER_IN_SESSION);
        if(user != null){
            request.getSession().removeAttribute(Constant.UPLOADUSER_IN_SESSION);
        }
        jsonData.setCode(1000);
        jsonData.setCodeInfo("退出成功！");
        jsonData.setResult("SUCCESS");
        String json = JsonUtils.object2json(jsonData);
        this.write("jsonpCallback(" + json + ")", response);
    }
    
    /**
     * 生成身份验证识别规则
     */
    private String generateMd5Str(String str){
        try {
            return Md5.encodeByMd5(str + SystemContant.md5Key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 验证上传身份是否合法并进入文件协作上传页面
     */
    @RequestMapping("/toTeamworkUploadPage")
    public String teamworkUploadPage(HttpServletRequest request, ModelMap modelMap){
        String folderId = request.getParameter("folderId");
        String teamworkId = request.getParameter("teamworkId");
        String domain = request.getParameter("domain");
        String webname = request.getParameter("webname");
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        if(StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(teamworkId) || StringUtil.nullOrBlank(spaceType)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        EnumSpaceType mySpaceType = null;
        if(spaceType.equals(generateMd5Str(EnumSpaceType.TEAMWORK_SPACE.getCode()))){//判断是否为协作空间
            mySpaceType = EnumSpaceType.TEAMWORK_SPACE;
        }
        if(mySpaceType == null){
            throw new MyException(FileServersException.PARAMERROR);
        }
        UploadUser user = this.getMyInfo(request);
        String eId = user.getIdentity();
        Integer employeeId = Integer.parseInt(eId);
        List<String> folderList = new ArrayList<String>();
        folderList.add(folderId);
        
        //判断是否具备上传权限
        if(!authorityShareManager.getTeamworkOpenAuthById(null, folderList, employeeId, Integer.parseInt(teamworkId))){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_CURRENT_FOLDER_UPLOAD_AUTHORITY, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        
        //验证当前需要上传的目录是否有效
        if(StringUtil.nullOrBlank(folderId)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }else{
            
            List<String> fileList = new ArrayList<String>();
            TeamworkFolder folder = fileManager.getTeamworkFolderAndFilesByFolderId(Integer.parseInt(folderId), employeeId);
            if (folder != null && folder.getId() == 0) {//设置最上层默认目录名
                folder.setFolderName(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_PERSONAL_FILE, request));
            }
            if(folder != null && folder.getTeamworkFiles() != null && folder.getTeamworkFiles().size() > 0){
                for(TeamworkFile file : folder.getTeamworkFiles()){
                    fileList.add(file.getFileName());
                }
            }
            
            if(folder == null){
                String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
                modelMap.put("msg", msg);
                return "common/prompt";
            }else{
                //如果上传到的文件夹是最上级，则最上级名称赋予该协作组名称
                if(!StringUtil.nullOrBlank(teamworkId) && folder != null && folder.getId() == 0){
                    String teamworkName = teamworkMessageManager.getTeamworkName(Integer.parseInt(teamworkId));
                    folder.setFolderName(teamworkName);
                }
                String folderJsonStr = JsonUtils.list2json(fileList);
                request.setAttribute("spaceType", spaceType);
                request.setAttribute("currentFolder", folder);
                request.setAttribute("fileList", folderJsonStr);
                request.setAttribute("teamworkId", teamworkId);
                request.setAttribute("diskServerUrl", domain);
                request.setAttribute("webname", webname);
                
                //描述允许上传类型的提示
                StringBuffer uploadTypeDescribe = new StringBuffer();
                String uploadModelType = SysConfigHelper.getValue(SystemContant.BASICSCODE, "UPLOADMODE");//可上传文件类型验证模式
                int uploadModel = 0;
                String validateRule = SysConfigHelper.getValue(SystemContant.BASICSCODE, uploadModelType);
                if(uploadModelType.equals(BasicsInfoCode.THROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//允许上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CAN_ONLY_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 1;
                }else if(uploadModelType.equals(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//禁止上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 2;
                }
                //允许单个文件上传的最大大小
                request.setAttribute("maxSize", SysConfigHelper.getValue(SystemContant.BASICSCODE, "SMAX"));
                //上传验证模式
                request.setAttribute("uploadModel", uploadModel);
                List<String> validateFormat = new ArrayList<String>();
                if(!StringUtil.nullOrBlank(validateRule)){
                    validateFormat = Arrays.asList(validateRule.split(","));
                }
                request.setAttribute("validateRule", JsonUtils.list2json(validateFormat));
                request.setAttribute("uploadTypeDescribe", uploadTypeDescribe.toString());
                request.setAttribute("usableDiskSize", fileManager.getTeamworkUsableDiskSize(Integer.parseInt(teamworkId), null));
            }
        }
        return "upload/uploadTeamwork";
    }
    
    /**
     * 进入文件上传文件夹页面
     */
    @RequestMapping("/toTeamworkFolderUploadPage")
    public String teamworkUploadFolderPage(HttpServletRequest request, ModelMap modelMap){
        if(!getBrowerIsSupportFolderUpload(request)){//判断浏览器版本是否支持文件夹上传
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CURRENT_BROWSER_VERSION_NOT_SUPPORTED, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        String folderId = request.getParameter("folderId");
        String teamworkId = request.getParameter("teamworkId");
        String domain = request.getParameter("domain");
        String webname = request.getParameter("webname");
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        if(StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(spaceType)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        EnumSpaceType mySpaceType = null;
        if(spaceType.equals(generateMd5Str(EnumSpaceType.TEAMWORK_SPACE.getCode()))){//判断是否为协作空间
            mySpaceType = EnumSpaceType.TEAMWORK_SPACE;
        }
        if(mySpaceType == null){
            throw new MyException(FileServersException.PARAMERROR);
        }
        UploadUser user = this.getMyInfo(request);
        String eId = user.getIdentity();
        Integer employeeId = Integer.valueOf(eId);
        List<String> folderList = new ArrayList<String>();
        folderList.add(folderId);
        
        //判断是否具备上传权限
        if(!authorityShareManager.getTeamworkOpenAuthById(null, folderList, employeeId, Integer.parseInt(teamworkId))){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_CURRENT_FOLDER_UPLOAD_AUTHORITY, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }
        
        //验证当前需要上传的目录是否有效
        if(StringUtil.nullOrBlank(folderId)){
            String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
            modelMap.put("msg", msg);
            return "common/prompt";
        }else{
            TeamworkFolder folder = fileManager.getTeamworkFolderAndFilesByFolderId(Integer.parseInt(folderId), employeeId);
            if(folderId.equals("-1")){
                folder.setId(-1);
                folder.setEmployeeId(employeeId);
            }
            
            if(folder == null){
                String msg = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SELECT_UPLOAD_FOLDER, request);
                modelMap.put("msg", msg);
                return "common/prompt";
            }else{
                request.setAttribute("spaceType", spaceType);
                request.setAttribute("currentFolder", folder);
                request.setAttribute("teamworkId", teamworkId);
                
                //描述允许上传类型的提示
                StringBuffer uploadTypeDescribe = new StringBuffer();
                String uploadModelType = SysConfigHelper.getValue(SystemContant.BASICSCODE, "UPLOADMODE");//可上传文件类型验证模式
                int uploadModel = 0;
                String validateRule = SysConfigHelper.getValue(SystemContant.BASICSCODE, uploadModelType);
                if(uploadModelType.equals(BasicsInfoCode.THROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//允许上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CAN_ONLY_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 1;
                }else if(uploadModelType.equals(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateRule)){//禁止上传类型
                    uploadTypeDescribe.append(LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_CANNOT_UPLOAD, request) + validateRule + LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_FILE_TYPE, request));
                    uploadModel = 2;
                }
                //允许单个文件上传的最大大小
                request.setAttribute("maxSize", SysConfigHelper.getValue(SystemContant.BASICSCODE, "SMAX"));
                //上传验证模式
                request.setAttribute("uploadModel", uploadModel);
                List<String> validateFormat = new ArrayList<String>();
                if(!StringUtil.nullOrBlank(validateRule)){
                    validateFormat = Arrays.asList(validateRule.split(","));
                }
                request.setAttribute("validateRule", JsonUtils.list2json(validateFormat));
                request.setAttribute("uploadTypeDescribe", uploadTypeDescribe.toString());
                request.setAttribute("usableDiskSize", fileManager.getTeamworkUsableDiskSize(Integer.parseInt(teamworkId), null));
                request.setAttribute("diskServerUrl", domain);
                request.setAttribute("webname", webname);
            }
        }
        if(isPCClientRequest(request)){//判断是否来自PC客户端程序的请求
            return "upload/pcClientStreamUpload";
        }else{
            return "upload/streamUploadTeamwork";
        }
    }
}
