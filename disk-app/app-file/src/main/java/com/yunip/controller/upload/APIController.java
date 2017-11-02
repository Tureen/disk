/*
 * 描述：APP接口请求控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-6-3
 */
package com.yunip.controller.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.Constant;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.api.ApiException;
import com.yunip.enums.teamwork.FileType;
import com.yunip.manage.FileManager;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.service.IEmployeeService;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * APP接口请求控制器层
 */
@Controller
@RequestMapping("/api")
public class APIController extends FileManagerBaseController{
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    /**
     * 文件下载请求,返回下载地址
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
        int code = ApiException.ERROR.getCode();
        String codeInfo = "没有下载文件";
        List<Map<String, String>> reuslt = new ArrayList<Map<String, String>>();
        try {
            String paramStr = this.getPostContent(request);
            log.info("接口请求参数：" + paramStr);
            JSONObject json = JSONObject.fromObject(paramStr);
            String token = json.getString("token");
            JSONArray files = json.getJSONArray("files");
            //验证是否登录
            Employee employee = employeeService.getEmployee(token);
            if(employee != null && files != null && files.size() > 0){
                String[] downFileArr = new String[files.size()];
                for(int i = 0;i < files.size();i++){
                    downFileArr[i] = files.getString(i);
                }
                List<com.yunip.model.disk.File> downFileList = fileManager.getFileByIds(downFileArr);
                for(com.yunip.model.disk.File downFile : downFileList){
                    //获得需要下载的原文件的路径
                    String downFilePath = getFile(downFile.getId());
                    //获得需要下载的原文件的名称
                    String downFileName = downFile.getFileName();
                    String downloadUrl = "";
                    //返回组装加密后的下载信息
                    if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                        String sourceFileName = FileUtil.getShortFileName(downFilePath);
                        String downloadStr = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + sourceFileName;
                        Des desObj = new Des();
                        downloadUrl = desObj.strEnc(downloadStr, SystemContant.desKey, null, null);
                    }
                    
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("fileId", downFile.getId().toString());
                    map.put("downloadUrl", SystemContant.FILE_SERVICE_URL + "/api/downloadFile?params=" + downloadUrl);
                    reuslt.add(map);
                }
                code = 1000;
                codeInfo = "请求成功！";
            }else{
                code = ApiException.ILLEAGEREQUEST.getCode();
                codeInfo = "非法请求！";
            }
        }catch (Exception e) {
            String msg = e.getMessage();
            if(!StringUtil.nullOrBlank(msg) && "登录失效".equals(msg)){
                code = ApiException.TOKENFAILURE.getCode();
                codeInfo = msg;
            }else{
                codeInfo = "异常请求：" + msg;
            }
        }
        String json = "{\"code\":" + code + ",\"codeInfo\":\"" + codeInfo + "\",\"result\":" + JsonUtils.list2json(reuslt) + "}";
        this.write(json, response);
    }
    
    /**
     * 文件下载请求,返回下载地址
     */
    @RequestMapping("/teamworkdownload")
    public void teamworkDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        int code = ApiException.ERROR.getCode();
        String codeInfo = "没有下载文件";
        List<Map<String, String>> reuslt = new ArrayList<Map<String, String>>();
        try {
            String paramStr = this.getPostContent(request);
            log.info("接口请求参数：" + paramStr);
            JSONObject json = JSONObject.fromObject(paramStr);
            String token = json.getString("token");
            JSONArray files = json.getJSONArray("files");
            //验证是否登录
            Employee employee = employeeService.getEmployee(token);
            if(employee != null && files != null && files.size() > 0){
                String[] downFileArr = new String[files.size()];
                for(int i = 0;i < files.size();i++){
                    downFileArr[i] = files.getString(i);
                }
                List<TeamworkFile> downFileList = fileManager.getTeamworkFileByIds(downFileArr);
                for(TeamworkFile downFile : downFileList){
                    //获得需要下载的原文件的路径
                    String downFilePath = getTeamworkFile(downFile.getId());
                    //获得需要下载的原文件的名称
                    String downFileName = downFile.getFileName();
                    String downloadUrl = "";
                    //返回组装加密后的下载信息
                    if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                        String sourceFileName = FileUtil.getShortFileName(downFilePath);
                        String downloadStr = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + sourceFileName;
                        Des desObj = new Des();
                        downloadUrl = desObj.strEnc(downloadStr, SystemContant.desKey, null, null);
                    }
                    
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("fileId", downFile.getId().toString());
                    map.put("downloadUrl", SystemContant.FILE_SERVICE_URL + "/api/downloadFile?params=" + downloadUrl);
                    reuslt.add(map);
                }
                code = 1000;
                codeInfo = "请求成功！";
            }else{
                code = ApiException.ILLEAGEREQUEST.getCode();
                codeInfo = "非法请求！";
            }
        }catch (Exception e) {
            String msg = e.getMessage();
            if(!StringUtil.nullOrBlank(msg) && "登录失效".equals(msg)){
                code = ApiException.TOKENFAILURE.getCode();
                codeInfo = msg;
            }else{
                codeInfo = "异常请求：" + msg;
            }
        }
        String json = "{\"code\":" + code + ",\"codeInfo\":\"" + codeInfo + "\",\"result\":" + JsonUtils.list2json(reuslt) + "}";
        this.write(json, response);
    }
    
    /**
     * 下载文件
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //下载文件加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要下载的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        String[] paramsArr = decParams.split("\\|");
        if(paramsArr != null && paramsArr.length == 3){
            String downFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[2];
            String downFileName = paramsArr[1];
            breakPointDownload(downFilePath, downFileName, request, response);
        }else{
            this.write("非法下载请求!", response);
        }
    }
    
    /**
     * 读取图片文件缩略图
     */
    @RequestMapping("/readerthumbImage")
    public void readerthumbImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String fileId = request.getParameter("params");
        String fileType = request.getParameter("fileType");
        if(!StringUtil.nullOrBlank(fileId)){
            if((""+FileType.IS_TEAMWORK_FILE.getType()).equals(fileType)){
                readerTeamworkFileThumbImagerStream(Integer.parseInt(fileId), response);
            }else{
                readerThumbImagerStream(Integer.parseInt(fileId), response);
            }
        }
    }
    
    /**
     * 读取预览图片
     */
    @RequestMapping("/readerPreviewImage")
    public void readerPreviewImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String fileId = request.getParameter("params");
        String fileType = request.getParameter("fileType");
        if(!StringUtil.nullOrBlank(fileId)){
            String filePath = "";
            if((""+FileType.IS_TEAMWORK_FILE.getType()).equals(fileType)){
                filePath = getTeamworkPreviewImage(fileId);
            }else{
                filePath = getPreviewImage(fileId);
            }
            File file = new File(filePath);
            if(file.exists() && file.isFile()){
                ioReaderFile(response, filePath);
            }
        }
    }
    
    /**
     * 读取安卓logo
     * @param request
     * @param response
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/readerAndroidLogoImage")
    public void readerAndroidLogoImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String filePath = request.getSession().getServletContext().getRealPath("/");
        filePath += "/static/images/androidlogo.png";
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            ioReaderFile(response, filePath);
        }
    }
    
    /**
     * 断点文件下载
     * @param downFilePath
     */
    private void breakPointDownload(String downFilePath, String downfileName, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/octet-stream");
        File downFiles = new File(downFilePath);
        if(!downFiles.exists()){
            return;
        }
        try {
            InputStream fis = new FileInputStream(downFiles);
            
            String range = request.getHeader("Range");//接收服务端传递过来的断点下载位置请求(Range: bytes=startOffset-targetOffset/sum  [表示从startOffset读取，一直读取到targetOffset位置，读取总数为sum直接[字节总数sum也可以去掉]])
            int status = HttpServletResponse.SC_OK; //返回的状态码，默认200，首次下载
            //如果range下载区域为空，则首次下载
            if(range == null){
                range = "bytes=0-";
            } else {  
                status = HttpServletResponse.SC_PARTIAL_CONTENT;//通过下载区域下载，使用206状态码支持断点续传 
            }  
            //设置响应状态码 
            response.setStatus(status);
            //上传开始的点
            long pos = 0;  
            if (null != range && range.startsWith("bytes=")) {  
                String[] values = range.split("=")[1].split("-");  
                pos = Integer.parseInt(values[0]);  
            }
            if(pos != 0){  
                // 跳已经传输过的字节  
                fis.skip(pos);  
            }
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while((length = fis.read(b)) != -1){
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
            deleteFileTempFolder(downFilePath);//删除临时缓存目录
        } catch (Exception e) {
            log.error("下载中断......" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 文件预览
     */
    @RequestMapping("/previewFile")
    public String previewFile(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception{
        String apiToken = request.getParameter("token");
        String fileId = request.getParameter("fileId");
        String fileType = request.getParameter("fileType");
        //验证是否登录
        Employee employee = employeeService.getEmployee(apiToken);
        if(employee != null && !StringUtil.nullOrBlank(fileId)){
            Des desObj = new Des();
            String identity = desObj.strEnc(employee.getId().toString(), SystemContant.desKey, null, null);
            String token = Md5.encodeByMd5(identity + SystemContant.md5Key);
            modelMap.put("identity", identity);
            modelMap.put("token", token);
            if((""+FileType.IS_TEAMWORK_FILE.getType()).equals(fileType)){
                modelMap.put("url", SystemContant.FILE_SERVICE_URL + "/fileManager/teamworkPreviewConvert?params=" + fileId);
            }else{
                modelMap.put("url", SystemContant.FILE_SERVICE_URL + "/fileManager/previewConvert?params=" + fileId);
            }
        }else{
            throw new MyException(ApiException.PARAMERROR);
        }
        return "app/startPreview";
    }
}
