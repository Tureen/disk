package com.yunip.controller.disk;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.DeleteType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.FileDeleteStatus;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.FileDeleteQuery;
import com.yunip.model.disk.query.FolderDeleteQuery;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.log.AdminLog;
import com.yunip.service.IFileDeleteService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/deleteinfo")
public class FileDeleteController extends BaseController{

    @Resource(name = "iFileDeleteService")
    private IFileDeleteService fileDeleteService;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    
    @RequestMapping("/file")
    public String file(HttpServletRequest request, FileDeleteQuery query){
        //属性值过滤
        replaceStr(query);
        String endDate = query.getEndDate();
        if(StringUtils.isNotBlank(query.getEndDate())){
            //在结束日期中加上一天
            Date endDateTime = DateUtil.parseDate(query.getEndDate(), DateUtil.YMD_DATA) ;
            query.setEndDate(DateUtil.getDateFormat(DateUtil.getAddDates(endDateTime, 1), DateUtil.YMD_DATA));
        }
        query.setDeleteType(DeleteType.DIRECT.getType());
        query = fileDeleteService.getFileDeletesByQuery(query);
        query.setEndDate(endDate);
        request.setAttribute("query", query);
        request.setAttribute("blackType", FileDeleteStatus.FALSEDELETE);
        request.setAttribute("grayType", FileDeleteStatus.TRUEDELETE);
        request.setAttribute("greenType", FileDeleteStatus.RESTORE);
        return "deleteinfo/file";
    }
    
    @RequestMapping("/folder")
    public String folder(HttpServletRequest request, FolderDeleteQuery query){
        //属性值过滤
        replaceStr(query);
        String endDate = query.getEndDate();
        if(StringUtils.isNotBlank(query.getEndDate())){
            //在结束日期中加上一天
            Date endDateTime = DateUtil.parseDate(query.getEndDate(), DateUtil.YMD_DATA) ;
            query.setEndDate(DateUtil.getDateFormat(DateUtil.getAddDates(endDateTime, 1), DateUtil.YMD_DATA));
        }
        query.setDeleteType(DeleteType.DIRECT.getType());
        query = fileDeleteService.getFolderDeletesByQuery(query);
        query.setEndDate(endDate);
        request.setAttribute("query", query);
        request.setAttribute("blackType", FileDeleteStatus.FALSEDELETE);
        request.setAttribute("grayType", FileDeleteStatus.TRUEDELETE);
        request.setAttribute("greenType", FileDeleteStatus.RESTORE);
        return "deleteinfo/folder";
    }
    
    @RequestMapping("/deletefile")
    @ResponseBody
    public JsonData<?> deleteFile(HttpServletRequest request, String idStr){
        JsonData<?> data = new JsonData<String>();
        AdminLog adminLog = getAdminLog(request);
        fileDeleteService.deleteFile(idStr,getMyinfo(request),adminLog);
        return data;
    }
    
    @RequestMapping("/restorefile")
    @ResponseBody
    public JsonData<?> restoreFile(HttpServletRequest request, String idStr){
        JsonData<?> data = new JsonData<String>();
        AdminLog adminLog = getAdminLog(request);
        fileDeleteService.restoreFiles(idStr, getMyinfo(request),adminLog);
        return data;
    }
    
    @RequestMapping("/deletefolder")
    @ResponseBody
    public JsonData<?> deleteFolder(HttpServletRequest request, String idStr){
        JsonData<?> data = new JsonData<String>();
        AdminLog adminLog = getAdminLog(request);
        fileDeleteService.deleteFolder(idStr,getMyinfo(request),adminLog);
        return data;
    }
    
    @RequestMapping("/restorefolder")
    @ResponseBody
    public JsonData<?> restoreFolder(HttpServletRequest request, String idStr){
        JsonData<?> data = new JsonData<String>();
        AdminLog adminLog = getAdminLog(request);
        fileDeleteService.restoreFolders(idStr, getMyinfo(request),adminLog);
        return data;
    }
    
    @RequestMapping("/download")
    @ResponseBody
    public JsonData<String> downloadFile(HttpServletRequest request,@RequestBody Folder folder){
        JsonData<String> data= new JsonData<String>();
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
        params.setCmdId(EnumFileServiceOperateType.RECOVERY_DOWNLOAD_FILE.getCmdId());
        params.setOperateId("0");
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        //String result = HttpUtil.post(SysConfigHelper.getValue(SystemContant.BASICSCODE, SystemContant.SYSTEM_FILE_DOMAIN) + EnumFileServiceOperateType.RECOVERY_DOWNLOAD_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        String result = HttpUtil.post(fileServiceRouteStrategy.getDownloadRecycleFileServiceAddress(myFile) + EnumFileServiceOperateType.RECOVERY_DOWNLOAD_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
    
    //对象属性值过滤
    private void replaceStr(Object object){
        Field fields[] = object.getClass().getDeclaredFields();//获得对象所有属性
        for (Field field : fields) {
            String key = field.getName();
            Class<?> type = field.getType();
            if("serialVersionUID".equals(key)){
                continue;
            }
            try {
                key = StringUtil.captureName(key);
                Method m = object.getClass().getMethod("get" + key);
                String value = (String) m.invoke(object); //调用getter方法获取属性值
                if(StringUtil.nullOrBlank(value)){
                    continue;
                }
                //过滤属性值(包含.)
                String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
                Pattern p = Pattern.compile(regEx); 
                Matcher mat = p.matcher(value);
                value = mat.replaceAll("").trim();
                //赋值
                Method m2 = object.getClass().getMethod("set"+key, type);
                m2.invoke(object, value);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
