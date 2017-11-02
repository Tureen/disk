/*
 * 描述：〈提取码〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.controller.disk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.FileServiceRouteStrategy;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.TakeCodeQuery;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.fileserver.PreviewFileEntity;
import com.yunip.service.IDepartmentService;
import com.yunip.service.ITakeCodeService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.EncodingUtil;
import com.yunip.utils.util.RandomUtil;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/takecode")
public class TakeCodeController extends BaseController {

    @Resource(name = "iTakeCodeService")
    private ITakeCodeService   takeCodeService;

    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    @Resource(name = "fileServiceRouteStrategy")
    private FileServiceRouteStrategy fileServiceRouteStrategy;
    
    /**
     * 提取码列表页
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, TakeCodeQuery query) {
        Employee employee = getEmployee(request);
        query.setEmployeeId(employee.getId());
        query.setOpenTime(new Date());
        takeCodeService.queryTakeCode(query);
        request.setAttribute("query", query);
        request.setAttribute("authorityTypes", AuthorityType.values());
        request.setAttribute("fileServiceUrl", SystemContant.FILE_SERVICE_URL);
        return "takecode/index";
    }
    
    /**
     * 进入复制提取码页面
     * @param takeCodeUrl 提取码链接
     */
    @RequestMapping("/getTakeCodePage")
    public String getTakeCodePage(HttpServletRequest request,Integer id){
        TakeCode takeCode = takeCodeService.getTakeCodeById(id);
        request.setAttribute("takecode", takeCode);
        return "takecode/getfinish";
    }

    /**
     * 添加提取码并跳转
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toinsert/{fileId}")
    public String toInsertTakeCode(HttpServletRequest request, @PathVariable
    Integer fileId,String timestr,String remainDownloadNum,String remark) {
        Employee employee = getEmployee(request);
        TakeCode takeCode = new TakeCode();
        //获取随机数字提取码并验证
        while (true) {
            String takeCodeStr = RandomUtil.generateNumberString(9);
            TakeCodeQuery query = new TakeCodeQuery();
            query.setTakeCode(takeCodeStr);
            query = takeCodeService.queryTakeCode(query);
            if (query.getRecordCount() == 0) {
                takeCode.setTakeCode(takeCodeStr);
                break;
            }
        }
        //获取所有部门
        List<Department> departments = departmentService.getAllDeparts();
        //组合takecode
        takeCode.setFileId(fileId);
        Date date = DateUtil.parseDate(timestr, DateUtil.YMDHM_DATA);
        takeCode.setEffectiveTime(date);
        if(!StringUtil.nullOrBlank(remainDownloadNum)){
            takeCode.setRemainDownloadNum(Integer.valueOf(remainDownloadNum));
        }
        if(!StringUtil.nullOrBlank(remark)){
            remark = EncodingUtil.decodeUnicodeSwitch(remark);
        }
        takeCode.setRemark(remark);
        takeCode.setCreateTime(new Date());
        //加入数据库！
        takeCodeService.addTakeCode(takeCode,employee);
        //放入requert
        request.setAttribute("takecode", takeCode);
        request.setAttribute("departments", departments);
        return "takecode/addfinish";
    }
    
    /**
     * 添加提取码并跳转(多条)
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toinsertmore")
    public String toInsertTakeCode(HttpServletRequest request,String fileIds,String timestr,String remainDownloadNum,String remark) {
        Employee employee = getEmployee(request);
        TakeCode takeCode = new TakeCode();
        //获取随机数字提取码并验证
        while (true) {
            String takeCodeStr = RandomUtil.generateNumberString(9);
            TakeCodeQuery query = new TakeCodeQuery();
            query.setTakeCode(takeCodeStr);
            query = takeCodeService.queryTakeCode(query);
            if (query.getRecordCount() == 0) {
                takeCode.setTakeCode(takeCodeStr);
                break;
            }
        }
        //获取所有部门
        List<Department> departments = departmentService.getAllDeparts();
        //组合takecode
        String[] strarr = fileIds.split(",");
        Date date = DateUtil.parseDate(timestr, DateUtil.YMDHM_DATA);
        takeCode.setEffectiveTime(date);
        if(!StringUtil.nullOrBlank(remainDownloadNum)){
            takeCode.setRemainDownloadNum(Integer.valueOf(remainDownloadNum));
        }
        if(!StringUtil.nullOrBlank(remark)){
            remark = EncodingUtil.decodeUnicodeSwitch(remark);
        }
        takeCode.setRemark(remark);
        takeCode.setCreateTime(new Date());
        //加入数据库！
        takeCodeService.addTakeCodeMore(takeCode,employee,strarr);
        //放入requert
        request.setAttribute("takecode", takeCode);
        request.setAttribute("departments", departments);
        return "takecode/addfinish";
    }

    /**
     * 根据提取码获取提取列表
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("pick")
    public String pickTakeCode(HttpServletRequest request, TakeCodeQuery query) {
        if (query.getTakeCode() != null && query.getTakeCode() != "") {
            query.setPageSize(Integer.MAX_VALUE);
            query = takeCodeService.queryTakeCode(query);
            request.setAttribute("query", query);
        }
        request.setAttribute("fileServiceUrl", SystemContant.FILE_SERVICE_URL);
        request.setAttribute("isIfreame", true);
        return "takecode/pick";
    }

    /**
     * 添加提取码
     * @param request
     * @param takeCode
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> insertTakeCode(HttpServletRequest request,
            TakeCode takeCode) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = getEmployee(request);
        takeCodeService.addTakeCode(takeCode,employee);
        return data;
    }
    
    /**
     * 添加提取码(多个)
     * @param request
     * @param takeCode
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/savemore")
    @ResponseBody
    public JsonData<?> insertTakeCodeMore(HttpServletRequest request,String fileIds,TakeCode takeCode,String timestr) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = getEmployee(request);
        String[] strarr = fileIds.split(",");
        Date date = DateUtil.parseDate(timestr, DateUtil.YMDHM_DATA);
        takeCode.setEffectiveTime(date);
        takeCodeService.addTakeCodeMore(takeCode,employee,strarr);
        return data;
    }

    /**
     * 删除提取码
     * @param request
     * @param takeCodeId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JsonData<?> deleteTakeCode(HttpServletRequest request, @RequestBody File file) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = getEmployee(request);
        List<TakeCode> takeCodes = file.getTakecodes();
        takeCodeService.delTakeCodeBatchByTakeCode(takeCodes, employee);
        return data;
    }
    
    /**
     * 下载文件，返回文件的下载地址
     */
    @RequestMapping("/download")
    @ResponseBody
    public synchronized JsonData<String> downloadFile(HttpServletRequest request,@RequestBody Folder folder,String takeCode){
        JsonData<String> data= new JsonData<String>();
        //判断下载次数
        TakeCodeQuery takeCodeQuery = new TakeCodeQuery();
        takeCodeQuery.setTakeCode(takeCode);
        takeCodeQuery = takeCodeService.queryTakeCode(takeCodeQuery);
        TakeCode takeCodeTmp = null;
        if(takeCodeQuery.getList() != null && takeCodeQuery.getList().size() > 0){
            takeCodeTmp = takeCodeQuery.getList().get(0);
            if(takeCodeTmp.getRemainDownloadNum() != null && takeCodeTmp.getRemainDownloadNum() <= 0){
                data.setCode(DiskException.UNREMAINDOWNLOADNUM.getCode());
                data.setCodeInfo(DiskException.UNREMAINDOWNLOADNUM.getMsg());
                return data;
            }
        }
        //
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
        myFile.setTakeCode(takeCode);
        
        List<DownloadFileEntity> paramList = new ArrayList<DownloadFileEntity>();
        paramList.add(myFile);
        
        FileOperateRequest<DownloadFileEntity> params = new FileOperateRequest<DownloadFileEntity>();
        params.setCmdId(EnumFileServiceOperateType.TAKECODE_DOWNLOAD_FILE.getCmdId());
        params.setOperateId("0");
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(fileServiceRouteStrategy.getDownloadFileServiceAddress(myFile) + EnumFileServiceOperateType.TAKECODE_DOWNLOAD_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
            //修改该提取码次数
            if(takeCodeTmp != null && takeCodeTmp.getId() != null){
                takeCodeService.upTakeCodeRemainDownloadNum(takeCodeTmp.getId());
            }
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
    public JsonData<String> previewFile(HttpServletRequest request, String fileId,String takeCode){
        JsonData<String> data= new JsonData<String>();
        Employee employee = super.getEmployee(request);
        
        PreviewFileEntity myFile = new PreviewFileEntity();
        myFile.setFileId(fileId);
        myFile.setTakeCode(takeCode);
        
        List<PreviewFileEntity> paramList = new ArrayList<PreviewFileEntity>();
        paramList.add(myFile);
        
        FileOperateRequest<PreviewFileEntity> params = new FileOperateRequest<PreviewFileEntity>();
        params.setCmdId(EnumFileServiceOperateType.TAKECODE_PREVIEW_FILE.getCmdId());
        if(employee != null){
            params.setOperateId(employee.getId().toString());
        }else{
            params.setOperateId("0");
        }
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(fileServiceRouteStrategy.getFileServiceAddressByFileId(Integer.parseInt(fileId)) + EnumFileServiceOperateType.TAKECODE_PREVIEW_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
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
        params.setCmdId(EnumFileServiceOperateType.BATCH_READER_FILE.getCmdId());
        if(employee != null){
            params.setOperateId(employee.getId().toString());
        }else{
            params.setOperateId("0");
        }
        params.setOperateTime(DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA));
        params.setParams(paramList);
        params.setChkValue(FileServiceOperateManagerUtil.getChkValue(params));
        
        String jsonStr = JsonUtils.bean2json(params);
        String result = HttpUtil.post(SystemContant.FILE_SERVICE_URL + EnumFileServiceOperateType.BATCH_READER_FILE.getUrlAddress(), jsonStr, setHttpRequestCookies(request), "UTF-8");
        if(!StringUtil.nullOrBlank(result)){
            data = JsonUtils.json2Bean(result, JsonData.class);
        }else{
            data.setCode(DiskException.ERROR.getCode());
            data.setCodeInfo(DiskException.ERROR.getMsg());
        }
        return data;
    }
}
