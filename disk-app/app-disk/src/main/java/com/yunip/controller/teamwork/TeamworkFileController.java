/*
 * 描述：〈协作文件信息管理〉
 * 创建人：ming.zhu
 * 创建时间：2017-03-02
 */
package com.yunip.controller.teamwork;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.enums.teamwork.MsgType;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameTeamworkNameHelper;
import com.yunip.model.disk.support.Tree;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkEmployee;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;
import com.yunip.model.teamwork.query.TeamworkFolderQuery;
import com.yunip.model.teamwork.query.TeamworkMessageQuery;
import com.yunip.model.teamwork.support.TeamworkMessageEntity;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.IFolderService;
import com.yunip.service.ITeamworkEmployeeService;
import com.yunip.service.ITeamworkFileService;
import com.yunip.service.ITeamworkFileVersionService;
import com.yunip.service.ITeamworkService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/teamworkfile")
public class TeamworkFileController extends BaseController{
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    @Resource(name = "iTeamworkService")
    private ITeamworkService teamworkService;
    
    @Resource(name = "iTeamworkEmployeeService")
    private ITeamworkEmployeeService teamworkEmployeeService;
    
    @Resource(name = "iTeamworkFileService")
    private ITeamworkFileService teamworkFileService;
    
    @Resource(name = "iTeamworkFileVersionService")
    private ITeamworkFileVersionService teamworkFileVersionService;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;
    
    @Resource(name = "iFolderService")
    private IFolderService folderService;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    
    /**
     * iframe跨域中间件,传递上传完成后保存的协作信息id
     * @param request
     * @param teamworkMessageId
     * @return
     * @throws Exception  
     * String 
     * @exception
     */
    @RequestMapping("/toexec")
    public String toExec(HttpServletRequest request, Integer teamworkMessageId) throws Exception{
        request.setAttribute("teamworkMessageId", teamworkMessageId);
        return "teamworkfile/execteamworkfile";
    }
    
    /**
     * 判断是否有进入协作文件列表权限
     * @param request
     * @param teamworkId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/checkIndex")
    @ResponseBody
    public JsonData<?> checkTeamworkFolder(HttpServletRequest request, Integer teamworkId){
        JsonData<?> data = new JsonData<String>();
        //查看员工是否拥有该协作组权限
        Employee employee = super.getEmployee(request);
        TeamworkEmployee teamworkEmployeeTmp = new TeamworkEmployee();
        teamworkEmployeeTmp.setEmployeeId(employee.getId());
        teamworkEmployeeTmp.setTeamworkId(teamworkId);
        TeamworkEmployee teamworkEmployee = teamworkEmployeeService.getTeamworkEmployee(teamworkEmployeeTmp);
        if(teamworkEmployee == null || teamworkEmployee.getAuthorityType() == null || !teamworkEmployee.getAuthorityType().equals(1)){
            throw new MyException(DiskException.NOPERMISSION);
        }
        return data;
    }
    
    /**
     * @throws Exception *
     * 获取协作文件列表
     * @param request
     * @param teamworkFolderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String getTeamworkFolderList(HttpServletRequest request, TeamworkFolderQuery teamworkFolderQuery) throws Exception{
        if(StringUtils.isNotBlank(teamworkFolderQuery.getQueryName())){
            String queryName = URLDecoder.decode(teamworkFolderQuery.getQueryName(),SystemContant.encodeType);
            teamworkFolderQuery.setQueryName(queryName.trim());
        }
        teamworkFolderQuery.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        TeamworkFolder teamworkFolder = teamworkFileService.getQuerySubFolder(teamworkFolderQuery);
        List<TeamworkFolder> teamworkFolders = teamworkFileService.getParentFolders(teamworkFolderQuery.getFolderId());
        //查询成员数量
        List<TeamworkEmployee> employeeAddObjs = teamworkEmployeeService.getTeamworkEmployeeIds(teamworkFolderQuery.getTeamworkId());
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("teamworkFolder", teamworkFolder);
        request.setAttribute("teamworkFolders", teamworkFolders);
        request.setAttribute("query", teamworkFolderQuery);
        //上传下载request
        downLoadRequest(request, EnumSpaceType.TEAMWORK_SPACE.getCode());
        return "teamworkfile/index";
    }
    
    /**
     * 右侧菜单
     * @param request
     * @param teamworkId
     * @return  
     * JsonData<TeamworkMessageEntity> 
     * @exception
     */
    @RequestMapping("/rightMenu")
    @ResponseBody
    public JsonData<TeamworkMessageEntity> rightMenu(HttpServletRequest request, @RequestBody TeamworkMessageQuery messageQuery){
        JsonData<TeamworkMessageEntity> data = new JsonData<TeamworkMessageEntity>();
        //
        TeamworkMessageEntity messageEntity = new TeamworkMessageEntity();   
        //查询成员数量
        List<TeamworkEmployee> employeeAddObjs = teamworkEmployeeService.getTeamworkEmployeeIds(messageQuery.getTeamworkId());
        //
        messageQuery = teamworkMessageManager.queryAjaxTeamworkMessage(messageQuery);
        //转换
        Map<Integer, List<TeamworkMessage>> map = new HashMap<Integer, List<TeamworkMessage>>();
        List<Integer> mapKey = new ArrayList<Integer>();
        if(messageQuery != null && messageQuery.getList() != null && messageQuery.getList().size() > 0){
            Date minDate = messageQuery.getList().get(0).getCreateTime();
            for(TeamworkMessage teamworkMessage : messageQuery.getList()){
                if(!mapKey.contains(teamworkMessage.getZid())){
                    mapKey.add(teamworkMessage.getZid());
                }
                if(map.get(teamworkMessage.getZid()) == null){
                    map.put(teamworkMessage.getZid(), new ArrayList<TeamworkMessage>());
                }
                map.get(teamworkMessage.getZid()).add(teamworkMessage);
                //
                minDate = minDate.getTime() > teamworkMessage.getCreateTime().getTime() ? teamworkMessage.getCreateTime() : minDate;
            }
            messageQuery.setOperationDate(DateUtil.getDateFormat(minDate, DateUtil.YMDHMS_DATA));
        }
        String portraitPath = SysContant.ROOTPATH + SystemContant.HEADERPATH;
        //拼接
        messageEntity.setEmployeeAddObjs(employeeAddObjs);
        messageEntity.setTeamworkMessageQuery(messageQuery);
        messageEntity.setMap(map);
        messageEntity.setMapKey(mapKey);
        messageEntity.setPortraitPath(portraitPath);
        data.setResult(messageEntity);
        return data;
    }
    
    /***
     * 创建私人文件夹
     * @param request
     * @param folder
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/createTeamworkFolder")
    @ResponseBody
    public JsonData<Integer> createTeamworkFolder(HttpServletRequest request, TeamworkFolder teamworkFolder){
        Employee employee = super.getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        //检验
        int teamworkMessageId = teamworkFileService.createTeamworkFolder(teamworkFolder, employee);
        data.setResult(teamworkMessageId);
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
        boolean bool = teamworkFileService.checkSameName(rename);
        data.setResult(bool);
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
    public JsonData<Integer> rename(HttpServletRequest request, RenameHelper rename){
        JsonData<Integer> data = new JsonData<Integer>();
        Employee employee = super.getEmployee(request);
        int teamworkMessageId = teamworkFileService.rename(rename, employee);
        data.setResult(teamworkMessageId);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的删除
     * @param request
     * @param teamworkFolder 封装对象
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/delManageFolder")
    @ResponseBody
    public JsonData<Integer> delManageFolder(HttpServletRequest request, @RequestBody TeamworkFolder teamworkFolder){
        Employee employee = super.getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        int teamworkMessageId = teamworkFileService.delFolderOrFile(teamworkFolder, employee, employee);
        data.setResult(teamworkMessageId);
        return data;
    }
    
    /**
     * 下载文件，返回文件的下载地址
     */
    @RequestMapping("/download")
    @ResponseBody
    public JsonData<String> downloadFile(HttpServletRequest request, @RequestBody TeamworkFolder teamworkFolder){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        List<TeamworkFile> listFiles = teamworkFolder.getTeamworkFiles();
        List<String> files = new ArrayList<String>();
        for(TeamworkFile f : listFiles){
            files.add(f.getId() + "");
        }
        
        List<TeamworkFolder > listFolders = teamworkFolder.getTeamworkFolders();
        List<String> folders = new ArrayList<String>();
        for(TeamworkFolder f : listFolders){
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
        String result = HttpUtil.post(fileServiceRouteStrategy.getDownloadFileServiceAddress(myFile) + EnumFileServiceOperateType.TEAMWORK_DOWNLOAD_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    /***
     * 文件树
     * @param request
     * @param folderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/folderTree")
    public String getFolderTree(HttpServletRequest request, TeamworkFolderQuery folderQuery){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        if(folderQuery.getFolderId() == null){
            folderQuery.setFolderId(0);
        }
        Teamwork teamwork = teamworkService.getTeamwork(folderQuery.getTeamworkId());
        request.setAttribute("teamworkId", folderQuery.getTeamworkId());
        request.setAttribute("teamworkName", teamwork.getTeamworkName());
        return "teamworkfile/folder";
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
    public List<Tree> getJsonFolderTree(HttpServletRequest request, Integer id, String teamworkId){
        Employee employee = super.getEmployee(request);
        if(id == null){
            id = 0;
        }
        List<TeamworkFolder> folders = teamworkFileService.getSubTwoFolders(id, employee, teamworkId);
        List<Tree> trees = Tree.getTeamworkTrees(folders, request.getContextPath());
        return trees;
    }
    
    /****
     * 移动或者复制先查目标文件夹下是否存在的同名文件夹或者文件
     * @param request
     * @param folder
     * @param teamworkId
     * @return  
     * JsonData<SameTeamworkNameHelper> 
     * @exception
     */
    @RequestMapping("/getSameName")
    @ResponseBody
    public JsonData<SameTeamworkNameHelper> getSameName(HttpServletRequest request, @RequestBody TeamworkFolder folder){
        JsonData<SameTeamworkNameHelper> data = new JsonData<SameTeamworkNameHelper>();
        SameTeamworkNameHelper nameHelper = teamworkFileService.getSameName(folder, folder.getId());
        data.setResult(nameHelper);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的移动
     * @param request
     * @param folder    封装对象
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/moveManageFolder")
    @ResponseBody
    public JsonData<Integer> moveManageFolder(HttpServletRequest request, @RequestBody TeamworkFolder folder){
        Employee employee = super.getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        teamworkFileService.moveFolderOrFile(folder, folder.getId(), employee, folder.getTeamworkId());
        return data;
    }
    
    /***
     * 导出文件树
     * @param request
     * @param folderQuery
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/exportFolderTree")
    public String getExportFolderTree(HttpServletRequest request, FolderQuery folderQuery){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        if(folderQuery.getFolderId() == null){
            folderQuery.setFolderId(0);
        }
        return "teamworkfile/exportfolder";
    }
    
    /****
     * 导出时先查目标文件夹下是否存在的同名文件夹或者文件
     * @param request
     * @param folder
     * @param teamworkId
     * @return  
     * JsonData<SameTeamworkNameHelper> 
     * @exception
     */
    @RequestMapping("/getExportSameName")
    @ResponseBody
    public JsonData<SameTeamworkNameHelper> getExportSameName(HttpServletRequest request, @RequestBody TeamworkFolder folder){
        JsonData<SameTeamworkNameHelper> data = new JsonData<SameTeamworkNameHelper>();
        Employee employee = super.getEmployee(request);
        SameTeamworkNameHelper nameHelper = teamworkFileService.getExportSameName(folder, folder.getId(), employee);
        data.setResult(nameHelper);
        return data;
    }
    
    /***
     * 多个文件或者文件夹的导出
     * @param request
     * @param folder    封装对象
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/exportManageFolder")
    @ResponseBody
    public JsonData<Integer> exportManageFolder(HttpServletRequest request, @RequestBody TeamworkFolder folder){
        Employee employee = super.getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        int teamworkMessageId = teamworkFileService.exportFolderOrFile(folder, folder.getId(), employee);
        data.setResult(teamworkMessageId);
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
    public String getFileVersionList(HttpServletRequest request, TeamworkFileVersionQuery query){
        //需要判断数据权限(该接口只允许自己的查看自己的文件列表，不包含分享)
        int versionTmp = query.getFileVersion();
        query.setFileVersion(0);
        List<TeamworkFileVersion> versions = teamworkFileVersionService.getFileVersions(query);
        query.setFileVersion(versionTmp);
        request.setAttribute("query", query);
        request.setAttribute("versions", versions);
        return "teamworkversion/index";
    }
    
    /**
     * 版本恢复
     * @param request
     * @param folder
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/regainFile")
    @ResponseBody
    public JsonData<Integer> regainFile(HttpServletRequest request, TeamworkFileVersion fileVersion){
        JsonData<Integer> data = new JsonData<Integer>();
        teamworkFileVersionService.regainFile(fileVersion);
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
        String result = HttpUtil.post(fileServiceRouteStrategy.getFileServiceAddressByTeamworkFileId(Integer.parseInt(fileId)) + EnumFileServiceOperateType.PREVIEW_TEAMWORK_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    /**
     * 留言发送
     * @param request
     * @param teamworkMessage
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @RequestMapping("/sendContent")
    @ResponseBody
    public JsonData<Integer> sendContent(HttpServletRequest request, @RequestBody TeamworkMessage teamworkMessage){
        Employee employee = super.getEmployee(request);
        JsonData<Integer> data = new JsonData<Integer>();
        int teamworkMessageId = teamworkMessageManager.saveTeamworkMessage(MsgType.LEAVE_MESSAGE.getType(), employee.getId(), employee.getEmployeeName(), teamworkMessage.getTeamworkId(), teamworkMessage.getContent(), null, null, null);
        data.setResult(teamworkMessageId);
        return data;
    }
    
    /**
     * 预览图片，返回预览图片的地址
     */
    @RequestMapping("/read")
    @ResponseBody
    public JsonData<String> ready(HttpServletRequest request, String fileIdString){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        List<String> paramList = new ArrayList<String>();
        String[] fileIds = fileIdString.split(",");
        if(fileIds.length == 0){
            return data;
        }
        for(String fileId : fileIds){
            paramList.add(fileId);
        }
        FileOperateRequest<String> params = new FileOperateRequest<String>();
        params.setCmdId(EnumFileServiceOperateType.BATCH_READER_TEAMWORK_FILE.getCmdId());
        params.setOperateId(employee.getId().toString());
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(SystemContant.FILE_SERVICE_URL + EnumFileServiceOperateType.BATCH_READER_TEAMWORK_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
}
