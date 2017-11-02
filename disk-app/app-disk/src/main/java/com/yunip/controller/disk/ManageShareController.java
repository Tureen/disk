/*
 * 描述：〈公共空间〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-13
 */
package com.yunip.controller.disk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.common.IdentityIndex;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.query.FileVersionQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.AuthHelper;
import com.yunip.model.disk.support.AuthHelperData;
import com.yunip.model.disk.support.AuthRelationHelper;
import com.yunip.model.disk.support.AuthReq;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameNameHelper;
import com.yunip.model.disk.support.Tree;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.IFileVersionService;
import com.yunip.service.IFolderService;
import com.yunip.service.IWorkgroupService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/manageshare")
public class ManageShareController extends BaseController {

    @Resource(name = "iFolderService")
    private IFolderService         folderService;
    
    @Resource(name = "iFileVersionService")
    private IFileVersionService fileVersionService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iAuthorityShareService")
    private IAuthorityShareService authorityShareService;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    
    @Resource(name = "iWorkgroupService")
    private IWorkgroupService workgroupService;
    
    
    /***
     * 文件树
     * @param request
     * @param folderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/folderTree")
    public String getFolderTree(HttpServletRequest request, FolderQuery folderQuery){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        if(folderQuery.getFolderId() == null){
            folderQuery.setFolderId(0);
        }
        return "manageshare/folder";
    }
    
    /***
     * 文件树
     * @param request
     * @param folderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/jsonFolderTree")
    @ResponseBody
    public List<Tree> getJsonFolderTree(HttpServletRequest request, Integer id){
      //获取模拟人，公共空间
        Employee employee = getRobotEmployee(request);
        if(id == null){
            id = 0;
        }
        List<Folder> folders = folderService.getSubTwoFolders(id, employee);
        List<Tree> trees = Tree.getTrees(folders, request.getContextPath());
        return trees;
    }

    /**
     * 管理公共空间页
     * @throws UnsupportedEncodingException 
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,FolderQuery folderQuery) throws UnsupportedEncodingException {
        //获取用户
        Employee employee = getEmployee(request);
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        if (employee.getCommonShareStatus()) {
            //管理员的公共空间
            folderQuery.setEmployeeId(robotEmployee.getId());
            folderQuery.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
            if(StringUtils.isNotBlank(folderQuery.getQueryName())){
                String queryName = URLDecoder.decode(folderQuery.getQueryName(),SystemContant.encodeType);
                folderQuery.setQueryName(queryName.trim());
            }
            Folder folder = folderService.getQuerySubFolder(folderQuery, robotEmployee);
            List<Folder> folders = folderService.getParentFolders(folderQuery.getFolderId());
            //上传下载request
            downLoadRequest(request, EnumSpaceType.PUBLIC_SPACE.getCode());
            //放入request
            request.setAttribute("folder", folder);
            request.setAttribute("query", folderQuery);
            request.setAttribute("folders", folders);
            request.setAttribute("authorityTypes", AuthorityType.values());
            request.setAttribute("robotEmployee", robotEmployee);
            if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
                return "manageshare/iconindex";
            }
            return "manageshare/index";
        }
        return "404";
    }
    
    /***
     * 创建私人文件夹
     * @param request
     * @param folder
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/createFolder")
    @ResponseBody
    public JsonData<Integer> createFolder(HttpServletRequest request, Folder folder){
        //获取用户
        Employee employee = getEmployee(request);
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        folder.setEmployeeId(robotEmployee.getId());
        folder.setCreateAdmin(employee.getEmployeeName());
        folder.setFolderName(folder.getFolderName().trim());
        //检验
        folderService.createPersonalFolder(folder, employee);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的复制
     * @param request
     * @param folder    封装对象
     * @param folderId  目标文件夹ID
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/copyManageFolder")
    @ResponseBody
    public JsonData<Integer> manageFolder(HttpServletRequest request,@RequestBody Folder folder){
        //获取用户
        Employee employee = getEmployee(request);
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        //设置模拟人名称
        robotEmployee.setEmployeeName(employee.getEmployeeName());
        robotEmployee.setLoginIp(employee.getLoginIp());
        JsonData<Integer> data = new JsonData<Integer>();
        folderService.copyFolderOrFile(folder, folder.getId(), robotEmployee);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的移动
     * @param request
     * @param folder    封装对象
     * @param folderId  目标文件夹ID
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/moveManageFolder")
    @ResponseBody
    public JsonData<Integer> moveManageFolder(HttpServletRequest request,@RequestBody Folder folder){
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        folderService.moveFolderOrFile(folder, folder.getId(), robotEmployee);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的删除
     * @param request
     * @param folder    封装对象
     * @param folderId  目标文件夹ID
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/delManageFolder")
    @ResponseBody
    public JsonData<Integer> delManageFolder(HttpServletRequest request,@RequestBody Folder folder){
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        //获取当前登录人,用于日志等记录
        Employee employee = getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        folderService.delFolderOrFile(folder, false, robotEmployee,employee);
        return data;
    }
    
    /***
     * 重命名
     * @param request
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/rename")
    @ResponseBody
    public JsonData<Integer> rename(HttpServletRequest request,RenameHelper rename){
        //获取用户
        Employee employee = getEmployee(request);
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        //创建新的employee对象
        Employee newEmployee = new Employee();
        newEmployee.setId(robotEmployee.getId());
        newEmployee.setEmployeeName(employee.getEmployeeName());
        newEmployee.setLoginIp(employee.getLoginIp());
        JsonData<Integer> data = new JsonData<Integer>();
        folderService.rename(rename, newEmployee);
        return data;
    }

    /***
     *  版本列表
     * @param request
     * @param folderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/version")
    public String getFileVersionList(HttpServletRequest request,FileVersionQuery query){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        List<FileVersion> versions = fileVersionService.getFileVersions(query);
        request.setAttribute("query", query);
        request.setAttribute("versions", versions);
        return "version/index";
    }
    
    /**
     * 恢复
     * @param request
     * @param folder
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/regainFile")
    @ResponseBody
    public JsonData<Integer> regainFile(HttpServletRequest request, FileVersion fileVersion){
        JsonData<Integer> data = new JsonData<Integer>();
        fileVersionService.regainFile(fileVersion);
        return data;
    }
    
  
    /***
     * 跳转权限分配
     * @param request
     * @return  
     * String 
     * @exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/authPage")
    public String getAuthPage(HttpServletRequest request, AuthHelper authHelper, Boolean removeFlag) throws Exception{
        Employee employee = super.getEmployee(request);
        if(removeFlag != null && removeFlag){
            //先删除分享文件or文件夹的分享
            Map<String, Class> clazzMap = new HashMap<String, Class>();
            clazzMap.put("folders",Folder.class);
            clazzMap.put("files", File.class);
            String json = URLDecoder.decode(authHelper.getData(),SystemContant.encodeType);
            Folder removeFolder = getJsonData(json, Folder.class, clazzMap);
            AuthReq authReq = buildAuthReq(removeFolder);
            if (authReq != null && authReq.getShares().size() > 0) {
                authorityShareService.delAuthorityShareBatch(authReq, employee);
            }
        }
      //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        List<Department> departments = departmentService.getAllDeparts();
        Map<String, List<Employee>> employees = employeeService.getAllEmployees();
        List<Workgroup> workgroups = workgroupService.getAllWorkgroup();
        AuthHelper manageAuthHelper = null;
        AuthHelper seeAuthHelper = null;
        AuthHelper readAuthHelper = null;
        if(StringUtils.isBlank(authHelper.getData())){
            manageAuthHelper = new AuthHelper();
            seeAuthHelper = new AuthHelper();
            readAuthHelper = new AuthHelper();
            manageAuthHelper.setAuthType(AuthorityType.MANAGER.getCode());
            seeAuthHelper.setAuthType(AuthorityType.SEE.getCode());
            readAuthHelper.setAuthType(AuthorityType.READ.getCode());
            //管理权限
            manageAuthHelper.setOpenId(authHelper.getOpenId());
            manageAuthHelper.setOpenType(authHelper.getOpenType());
            manageAuthHelper= authorityShareService.getAuthHelperByAuth(manageAuthHelper, employee);
            //查看权限
            seeAuthHelper.setOpenId(authHelper.getOpenId());
            seeAuthHelper.setOpenType(authHelper.getOpenType());
            seeAuthHelper = authorityShareService.getAuthHelperByAuth(seeAuthHelper, employee);
            //预览权限
            readAuthHelper.setOpenId(authHelper.getOpenId());
            readAuthHelper.setOpenType(authHelper.getOpenType());
            readAuthHelper = authorityShareService.getAuthHelperByAuth(readAuthHelper, employee);
        }
        request.setAttribute("authHelper", authHelper);
        request.setAttribute("manageAuthHelper", manageAuthHelper);
        request.setAttribute("seeAuthHelper", seeAuthHelper);
        request.setAttribute("readAuthHelper", readAuthHelper);
        request.setAttribute("departments", departments);
        request.setAttribute("employees", employees);
        request.setAttribute("workgroups", workgroups);
        request.setAttribute("manageAuthorityType", AuthorityType.MANAGER);
        request.setAttribute("seeAuthorityType", AuthorityType.SEE);
        request.setAttribute("readAuthorityType", AuthorityType.READ);
        request.setAttribute("authorityTypes", AuthorityType.values());
        return "manageauth/index";
    }
    
    /***
     * 跳转权限分配
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/load")
    public String load(HttpServletRequest request, AuthHelper authHelper){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        Employee robotEmployee = getRobotEmployee(request);
        List<Department> departments = departmentService.getAllDeparts();
        Map<String, List<Employee>> employees = employeeService.getAllEmployees();
        AuthorityType authorityType = AuthorityType.getAuthTypeByAuth(authHelper.getAuthType());
        if(StringUtils.isBlank(authHelper.getData())){
            authHelper = authorityShareService.getAuthHelperByAuth(authHelper, robotEmployee);
        }
        request.setAttribute("departments", departments);
        request.setAttribute("employees", employees);
        request.setAttribute("authorityType", authorityType);
        request.setAttribute("authHelper", authHelper);
        return "manageauth/load";
    }

    
    
    /**
     * @throws UnsupportedEncodingException *
     * 分享权限
     * @param request
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/shareAuth")
    @ResponseBody
    public JsonData<Integer> shareAuth(HttpServletRequest request,@RequestBody AuthHelperData authHelperData) throws UnsupportedEncodingException{
        //获取模拟人 =>公共空间
        Employee robotEmployee = getRobotEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        if(authHelperData.getAuthHelpers() != null && authHelperData.getAuthHelpers().size() > 0){
            List<AuthHelper> authHelpers = authHelperData.getAuthHelpers();
            //先删除
            AuthorityShare authorityTmp = new AuthorityShare();
            for(AuthHelper authHelper : authHelpers){
                authorityTmp.setOpenId(authHelper.getOpenId());
                authorityTmp.setOpenType(authHelper.getOpenType());
                authorityShareService.delByAuthorityShare(authorityTmp);
            }
            for(AuthHelper authHelper : authHelperData.getAuthHelpers()){
                if(StringUtils.isNotBlank(authHelper.getData())){
                    String json = URLDecoder.decode(authHelper.getData(),SystemContant.encodeType);
                    Map<String, Class> map = new HashMap<String, Class>();
                    map.put("folders", Folder.class);
                    map.put("files", File.class);
                    Folder folder = JsonUtils.json2Bean(json, Folder.class ,map);
                    authHelper.setFolder(folder);
                    authorityShareService.addMoreAuthorityShare(authHelper, robotEmployee);
                } else {
                    authorityShareService.addAuthorityShare(authHelper, robotEmployee);
                }
            }
        }
        return data;
    }
    
    /****
     * 移动或者复制先查目标文件夹下是否存在的同名文件夹或者文件
     * @param request
     * @param folder
     * @return  
     * JsonData<Folder> 
     * @exception
     */
    @RequestMapping("/getSameName")
    @ResponseBody
    public JsonData<SameNameHelper> getSameName(HttpServletRequest request,@RequestBody Folder folder){
        JsonData<SameNameHelper> data = new JsonData<SameNameHelper>();
        Employee robotEmployee = getRobotEmployee(request);
        SameNameHelper nameHelper = folderService.getSameName(folder, folder.getId(), robotEmployee);
        data.setResult(nameHelper);
        return data;
    }
    
    /****
     * 验证当前改名文件是否在当前目录下存在同样的名字
     * @param request
     * @param folder
     * @return  
     * JsonData<Folder> 
     * @exception
     */
    @RequestMapping("/checkSameName")
    @ResponseBody
    public JsonData<Boolean> checkSameName(HttpServletRequest request, RenameHelper rename){
        JsonData<Boolean> data = new JsonData<Boolean>();
        boolean bool = folderService.checkSameName(rename);
        data.setResult(bool);
        return data;
    }
    
    /**
     * 下载文件，返回文件的下载地址
     */
    @RequestMapping("/download")
    @ResponseBody
    public JsonData<String> downloadFile(HttpServletRequest request,@RequestBody Folder folder){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        List<File> listFiles = folder.getFiles();
        List<String> files = new ArrayList<String>();
        for(File f : listFiles){
            files.add(f.getId() + "");
        }
        
        List<Folder> listFolders = folder.getFolders();
        List<String> folders = new ArrayList<String>();
        for(Folder f : listFolders){
            folders.add(f.getId() + "");
        }
        
        DownloadFileEntity myFile = new DownloadFileEntity();
        myFile.setFiles(files);
        myFile.setFolders(folders);
        
        List<DownloadFileEntity> paramList = new ArrayList<DownloadFileEntity>();
        paramList.add(myFile);
        
        FileOperateRequest<DownloadFileEntity> params = new FileOperateRequest<DownloadFileEntity>();
        params.setCmdId(EnumFileServiceOperateType.DOWNLOAD_FILE.getCmdId());
        params.setOperateId(employee.getId().toString());
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(fileServiceRouteStrategy.getDownloadFileServiceAddress(myFile) + EnumFileServiceOperateType.DOWNLOAD_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    /**
     * 预览文件，返回文件的预览地址
     */
    @RequestMapping("/preview")
    @ResponseBody
    public JsonData<String> previewFile(HttpServletRequest request, String fileId){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        List<String> paramList = new ArrayList<String>();
        paramList.add(fileId);
        
        FileOperateRequest<String> params = new FileOperateRequest<String>();
        params.setCmdId(EnumFileServiceOperateType.PREVIEW_FILE.getCmdId());
        params.setOperateId(employee.getId().toString());
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(fileServiceRouteStrategy.getFileServiceAddressByFileId(Integer.parseInt(fileId)) + EnumFileServiceOperateType.PREVIEW_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    /**
     * 在线编辑文件，返回进行在线编辑的文件地址
     */
    @RequestMapping("/onlineEdit")
    @ResponseBody
    public JsonData<String> onlineEdit(HttpServletRequest request, String fileId){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        List<String> paramList = new ArrayList<String>();
        paramList.add(fileId);
        
        FileOperateRequest<String> params = new FileOperateRequest<String>();
        params.setCmdId(EnumFileServiceOperateType.ONLINE_EDIT_FILE.getCmdId());
        params.setOperateId(employee.getId().toString());
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(fileServiceRouteStrategy.getFileServiceAddressByFileId(Integer.parseInt(fileId)) + EnumFileServiceOperateType.ONLINE_EDIT_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    @RequestMapping("/auth")
    @ResponseBody
    public JsonData<String> getAuthority(HttpServletRequest request, Integer openId, Integer openType){
        JsonData<String> data= new JsonData<String>();
        Employee robotEmployee = getRobotEmployee(request);
        AuthorityShareQuery authorityShareQuery = new AuthorityShareQuery();
        authorityShareQuery.setOpenType(openType);
        authorityShareQuery.setOpenId(openId);
        authorityShareQuery.setEmployeeId(robotEmployee.getId());
        Map<String, AuthRelationHelper> map = authorityShareService.getAuthorityShareRelationName(authorityShareQuery);
        JSONObject jsonObject = JSONObject.fromObject(map);
        data.setResult(jsonObject.toString());
        return data;
    }
    
    private AuthReq buildAuthReq(Folder folder){
        //构造移除权限对象
        AuthReq authReq = new AuthReq();
        authReq.setShares(new ArrayList<AuthorityShare>());
        if(folder != null){
            if(folder.getFiles() != null && folder.getFiles().size() > 0){
                for(File rFile : folder.getFiles()){
                    AuthorityShare authorityShare = new AuthorityShare();
                    authorityShare.setOpenType(OpenType.FILE.getType());
                    authorityShare.setOpenId(rFile.getId());
                    authReq.getShares().add(authorityShare);
                }
            }
            if(folder.getFolders() != null && folder.getFolders().size() > 0){
                for(Folder rFolder : folder.getFolders()){
                    AuthorityShare authorityShare = new AuthorityShare();
                    authorityShare.setOpenType(OpenType.FOLDER.getType());
                    authorityShare.setOpenId(rFolder.getId());
                    authReq.getShares().add(authorityShare);
                }
            }
        }
        return authReq;
    }
}
