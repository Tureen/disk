/*
 * 描述：文件管理控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-5-23
 */
package com.yunip.controller.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.Constant;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.enums.fileservers.FileServersOnlineEditValidateType;
import com.yunip.enums.fileservers.FileServersType;
import com.yunip.manage.AuthorityShareManager;
import com.yunip.manage.FileManager;
import com.yunip.model.UploadUser;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.support.QueueTask;
import com.yunip.service.IEmployeeService;
import com.yunip.task.QueueEntry;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 文件管理控制器层
 */
@Controller
@RequestMapping("/fileManager")
public class FileManagerController extends FileManagerBaseController{
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "authorityShareManager")
    private AuthorityShareManager authorityShareManager;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    /**
     * 文件下载前的请求
     */
    @RequestMapping(value = "/reqDownloadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> reqDownloadFile(HttpServletRequest request,HttpServletResponse response,@RequestBody String[] fileIds){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        String ZIP_CACHE_DIR = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "";
        try {
            String downFilePath = "";//下载的文件地址
            String downFileName = "";//下载的文件名称
            //boolean isTempFile = false;//是否为打包下载生成的临时，若是下载完成后需要删除该文件
            
            UploadUser uploadUser = this.getMyInfo(request);
            String employeeId = uploadUser.getIdentity();//当前登陆人ID
            if(fileIds == null || fileIds.length == 0){
                throw new MyException(FileServersException.FILENOEXISTS);
            }
            //查询当前登录所属角色
            List<Integer> roles = getEmployeeRoles(employeeId);
            List<String> fileList = Arrays.asList(fileIds);
            //判断是否具备下载权限
            if(!authorityShareManager.getOpenAuthById(fileList, null, Integer.parseInt(employeeId), roles, FileServersType.DOWNLOAD, null)){
                throw new MyException(FileServersException.NOTAUTHORITYORFILENOTEXISTS);
            }
            if(fileIds.length == 1){////直接下载单个文件
                List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileList.get(0));
                if(list != null && list.size() == 1){
                    com.yunip.model.disk.File downFile = list.get(0);
                    downFilePath = getFile(downFile.getId());
                    downFileName = downFile.getFileName();
                }
            }else{//多个打包下载
                //每一次压缩缓存的根目录
                String tempDir = ZIP_CACHE_DIR + File.separator + UUID.randomUUID();
                //每一次压缩原文件存放目录
                String tempSourceDir = tempDir + File.separator + Constant.ZIP_DOWN_TEMP_DIR;
                File tempDirFile = new File(tempSourceDir);
                //判断压缩后的缓存目录是否存在，不存在则先进行创建
                if(!tempDirFile.exists()){
                    tempDirFile.mkdirs();
                }
                
                //查询需要下载的文件并复制到进行压缩下载的缓存目录下
                List<com.yunip.model.disk.File> downFileList = fileManager.getFileByIds(fileIds);
                for(com.yunip.model.disk.File downFile : downFileList){
                    //获得需要下载的原文件的路径
                    String sourceFilePath = uploadPath + downFile.getFilePath();
                    //获得需要下载的原文件的名称
                    String sourceFileName = downFile.getFileName();
                    if(StringUtil.nullOrBlank(sourceFilePath) || StringUtil.nullOrBlank(sourceFileName)){
                        continue;
                    }
                    //获得需要下载的原文件
                    File sourceFile = new File(sourceFilePath);
                    if(sourceFile.isFile() && sourceFile.exists()){
                        sourceFilePath = getFile(downFile.getId());
                        FileUtil.copyFile(sourceFilePath, tempSourceDir + File.separator + sourceFileName);
                        deleteFileTempFolder(sourceFilePath);
                    }
                }
                
                //压缩目录
                String zipDownPath = tempDir + File.separator + Constant.ZIP_DOWN_FILE_NAME;
                //下载文件名
                downFileName = Constant.ZIP_DOWN_FILE_NAME;
                
                createCompressFile(tempSourceDir, zipDownPath);
                //删除已被压缩后的目录
                FileUtil.deleteFolder(new File(tempSourceDir));
                //下载压缩的文件
                downFilePath = zipDownPath;
                //isTempFile = true;
            }
            
            //返回下载信息
            if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                String sourceFileName = FileUtil.getShortFileName(downFilePath);
                String downloadPath = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + true + "|" + sourceFileName;
                System.out.println(downloadPath);
                Des desObj = new Des();
                downloadPath = desObj.strEnc(downloadPath, SystemContant.desKey, null, null);
                result = getRequestRootUrl(request) + "/fileManager/download?params=" + downloadPath;
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
                msg = ex.getMsg();
            }else{
                code = FileServersException.ERROR.getCode();
                msg = FileServersException.ERROR.getMsg();
            }
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        if(!StringUtil.nullOrBlank(msg)){
            jsonData.setCodeInfo(msg);
        }
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 文件下载
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //下载文件加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要下载的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        String[] paramsArr = decParams.split("\\|");
        if(paramsArr != null && paramsArr.length == 4){
            String downFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[3];
            String downFileName = paramsArr[1];
            boolean isDelete = Boolean.valueOf(paramsArr[2]);
            singleFileDownload(downFilePath, downFileName, isDelete, request, response);
        }else{
            this.write("非法下载请求!", response);
        }
    }
    
    /**
     * 协助文件下载
     * @param request
     * @param response
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/downloadTeamwork")
    public void downloadTeamwork(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //下载文件加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要下载的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        String[] paramsArr = decParams.split("\\|");
        if(paramsArr != null && paramsArr.length == 4){
            String downFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[3];
            String downFileName = paramsArr[1];
            boolean isDelete = Boolean.valueOf(paramsArr[2]);
            singleFileDownload(downFilePath, downFileName, isDelete, request, response);
        }else{
            this.write("非法下载请求!", response);
        }
    }
    
    /**
     * 进入预览文件转换界面
     */
    @RequestMapping("/previewConvert")
    public String previewConvert(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        //预览文件加密参数
        String fileId = request.getParameter("params");
        UploadUser uploadUser = this.getMyInfo(request);
        previewValidate(fileId, uploadUser.getIdentity());
        modelMap.put("url", getRequestRootUrl(request) + "/fileManager/preview?fileId=" + fileId);
        return "upload/previewConvert";
    }
    
    /**
     * 转换生成预览文件
     */
    @RequestMapping("/preview")
    public String preview(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String page = "upload/preview";
        UploadUser uploadUser = this.getMyInfo(request);
        String fileId = request.getParameter("fileId");
        previewValidate(fileId, uploadUser.getIdentity());
        /*String previewFilePath = previewValidate(fileId, uploadUser.getIdentity());
        previewFilePath = getPreviewFile(fileId);
        try {
            OfficeConverter officeConverter = new OfficeConverter(previewFilePath);
            if(officeConverter.converter()){//转换成功
                boolean isUsePdfPreview = getBrowerIsSupportH5(request);
                
                File previewFile;
                if(isUsePdfPreview){//支持可直接预览pdf格式
                    previewFile = officeConverter.getPdfFile();
                }else{//不支持的使用flexPage进行预览,预览文件为.swf格式
                    previewFile = officeConverter.getSwfFile();
                    page = "upload/flexPagePreview";
                }
                if(previewFile == null || !previewFile.exists()){
                    throw new MyException(FileServersException.FILENOEXISTS);
                }
            }
            deleteFileTempFolder(previewFilePath);//删除临时文件
        }catch (Exception e) {
            throw new MyException(FileServersException.PRIVIEWERROR);
        }
        com.yunip.model.disk.File previewFile = fileManager.getFileByFileId(fileId);*/
        Map<String, String> result = getPreviewFile(fileId, request);
        page = result.get("page");
        modelMap.put("fileName", result.get("fileName"));
        modelMap.put("url", "/fileManager/previewReader?fileId=" + fileId);
        return page;
    }
    
    /**
     * 转换并预览读取文件
     */
    @RequestMapping("/previewReader")
    public void previewReader(HttpServletRequest request, HttpServletResponse response) throws Exception{
        UploadUser uploadUser = this.getMyInfo(request);
        String fileId = request.getParameter("fileId");
        String previewFilePath = previewValidate(fileId, uploadUser.getIdentity());
        //获得客户端请求的类型，支持H5的浏览器使用pdfjs进行预览，不支持的使用flexPage进行预览
        boolean isUsePdfPreview = getBrowerIsSupportH5(request);
        String previewDir = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator;
        if(isUsePdfPreview){//支持预览pdf格式文件
            previewFilePath = previewDir + FileUtil.getShortFileNameWithoutExt(previewFilePath) + Constant.PDF_SUFFIX;
        }else{//不支持预览swf格式文件
            previewFilePath = previewDir + FileUtil.getShortFileNameWithoutExt(previewFilePath) + Constant.SWF_SUFFIX;
        }
        //读取预览文件
        response.setContentType("application/octet-stream");
        File downFiles = new File(previewFilePath);
        if(!downFiles.exists()){
            return;
        }
        try {
            InputStream fis = new FileInputStream(downFiles);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while((length = fis.read(b)) != -1){
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            logger.error("预览失败了......" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 进入预览文件转换界面
     */
    @RequestMapping("/teamworkPreviewConvert")
    public String teamworkPreviewConvert(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        //预览文件加密参数
        String fileId = request.getParameter("params");
        UploadUser uploadUser = this.getMyInfo(request);
        previewValidateTeamwork(fileId, uploadUser.getIdentity());
        modelMap.put("url", getRequestRootUrl(request) + "/fileManager/teamworkPreview?fileId=" + fileId);
        return "upload/previewConvert";
    }
    
    /**
     * 转换生成预览文件
     */
    @RequestMapping("/teamworkPreview")
    public String teamworkPreview(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String page = "upload/preview";
        UploadUser uploadUser = this.getMyInfo(request);
        String fileId = request.getParameter("fileId");
        previewValidateTeamwork(fileId, uploadUser.getIdentity());
        Map<String, String> result = getPreviewTeamworkFile(fileId, request);
        page = result.get("page");
        modelMap.put("fileName", result.get("fileName"));
        modelMap.put("url", "/fileManager/teamworkPreviewReader?fileId=" + fileId);
        return page;
    }
    
    /**
     * 转换并预览读取文件
     */
    @RequestMapping("/teamworkPreviewReader")
    public void teamworkPreviewReader(HttpServletRequest request, HttpServletResponse response) throws Exception{
        UploadUser uploadUser = this.getMyInfo(request);
        String fileId = request.getParameter("fileId");
        String previewFilePath = previewValidateTeamwork(fileId, uploadUser.getIdentity());
        //获得客户端请求的类型，支持H5的浏览器使用pdfjs进行预览，不支持的使用flexPage进行预览
        boolean isUsePdfPreview = getBrowerIsSupportH5(request);
        String previewDir = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator;
        if(isUsePdfPreview){//支持预览pdf格式文件
            previewFilePath = previewDir + FileUtil.getShortFileNameWithoutExt(previewFilePath) + Constant.PDF_SUFFIX;
        }else{//不支持预览swf格式文件
            previewFilePath = previewDir + FileUtil.getShortFileNameWithoutExt(previewFilePath) + Constant.SWF_SUFFIX;
        }
        //读取预览文件
        response.setContentType("application/octet-stream");
        File downFiles = new File(previewFilePath);
        if(!downFiles.exists()){
            return;
        }
        try {
            InputStream fis = new FileInputStream(downFiles);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while((length = fis.read(b)) != -1){
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            logger.error("预览失败了......" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 在线编辑文件
     */
    @RequestMapping("/onlineEdit")
    public String onlineEdit(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String fileId = request.getParameter("params");
        UploadUser uploadUser = this.getMyInfo(request);
        String employeeId = uploadUser.getIdentity();
        String onlineEditFilePath = onlineEditValidate(fileId, employeeId, FileServersOnlineEditValidateType.OFFICES);
        modelMap.put("fileId", fileId);
        modelMap.put("fileName", FileUtil.getShortFileName(onlineEditFilePath));
        modelMap.put("url", getRequestRootUrl(request) + "/fileManager/readerFile?fileId=" + fileId);
        return "upload/onlineEdit";
    }
    
    /**
     * 以文本方式在线编辑文件
     */
    @RequestMapping("/onlineEditTxt")
    public String onlineEditTxt(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception{
        String fileId = request.getParameter("params");
        String read = request.getParameter("read");
        UploadUser uploadUser = this.getMyInfo(request);
        String employeeId = uploadUser.getIdentity();
        if("0".equals(read)){//0表示预览,否则是编辑
            previewValidate(fileId, employeeId);
        } else {
            onlineEditValidate(fileId, employeeId, FileServersOnlineEditValidateType.TXT);
        }
        String content = readFileTextContent(Integer.parseInt(fileId));
        request.setAttribute("content", content);
        request.setAttribute("read", read);
        modelMap.put("url", getRequestRootUrl(request) + "/fileManager/uploadEditTxtContent?fileId=" + fileId);
        return "upload/onlineEditTxt";
    }
    
    /**
     * 以文本方式在线预览协作文件
     */
    @RequestMapping("/onlinePreviewTxtTeamwork")
    public String onlinePreviewTxtTeamwork(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception{
        String fileId = request.getParameter("fileId");
        UploadUser uploadUser = this.getMyInfo(request);
        String employeeId = uploadUser.getIdentity();
        previewValidateTeamwork(fileId, employeeId);
        String content = readTeamworkFileTextContent(Integer.parseInt(fileId));
        request.setAttribute("content", content);
        return "upload/onlineEditTxt";
    }
    
    /***
     * 上传在线编辑的文本文件
     */
    @RequestMapping("/uploadEditTxtContent")
    @ResponseBody
    public JsonData<Integer> uploadEditTxtContent(HttpServletRequest request,int fileId, String content) throws Exception {
        JsonData<Integer> data = new JsonData<Integer>();
        UploadUser uploadUser = this.getMyInfo(request);
        String employeeId = uploadUser.getIdentity();
        String onlineEditFilePath = onlineEditValidate(fileId+"", employeeId, FileServersOnlineEditValidateType.TXT);
        
        List<com.yunip.model.disk.File> fileList = fileManager.getFileByIds(fileId+"");
        if(fileList == null || fileList.size() != 1){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        //当前操作的文件
        com.yunip.model.disk.File operateFile = fileList.get(0);
        
        //写入到临时目录
        String tempDirPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID();
        File tempDir = new File(tempDirPath);
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }
        String tempFilePath = tempDir + File.separator + FileUtil.getShortFileName(onlineEditFilePath);
        FileUtil.write(content, tempFilePath);
        
        //替换为修改后新的文件(将修改后的内容重命名为新的文件)
        String newFilePath = FileUtil.getFileParentPath(onlineEditFilePath) + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(onlineEditFilePath);
        //将临时文件替换原文件
        FileUtil.copyFile(tempFilePath, newFilePath);
        
        //获得操作人信息
        Employee employee = employeeService.getEmployeeByEmployeeId(uploadUser.getIdentity());
        employee.setLoginIp(this.getClientIP(request));
        
        //查询当前文件路径是否被引用一次(复制时会出现多次)
        boolean isOnlyFile = fileManager.isOnlyFilePath(operateFile.getFilePath());//判断该文件是否仅被引用一次，是则删除
        //修改文件信息
        com.yunip.model.disk.File newFile = new com.yunip.model.disk.File();
        newFile.setId(operateFile.getId());
        newFile.setFileName(operateFile.getFileName());
        newFile.setFolderId(operateFile.getFolderId());
        newFile.setFileSize(FileUtil.getFileSize(newFilePath));
        newFile.setFilePath(newFilePath.substring(newFilePath.indexOf(Constant.UPLOAD_DIR) + Constant.UPLOAD_DIR.length() + 1, newFilePath.length()));
        newFile.setUpdateTime(new Date());
        newFile.setUpdateAdmin(employee.getEmployeeName());
        fileManager.updateFile(newFile, employee);
        //删除临时目录
        FileUtil.deleteFolder(tempDir);
        if(isOnlyFile){//判断该文件是否仅被引用一次，是则删除
            File operateTempFile = new File(onlineEditFilePath);
            if(operateTempFile.exists()){
                operateTempFile.delete();
            }
        }
        //添加文件到待加密队列中
        addFileToEncryptQueue(newFile);
        return data;
    }
    
    /**
     * 读取文件(在线编辑使用)
     */
    @RequestMapping("/readerFile")
    public void readerFile(HttpServletRequest request, HttpServletResponse response){
        UploadUser uploadUser = this.getMyInfo(request);
        String fileId = request.getParameter("fileId");
        String onlineEditFilePath = onlineEditValidate(fileId, uploadUser.getIdentity(), FileServersOnlineEditValidateType.OFFICES);
        onlineEditFilePath = getFile(Integer.parseInt(fileId));
        //读取在线文件
        ioReaderFile(response, onlineEditFilePath);
        deleteFileTempFolder(onlineEditFilePath);
    }
    
    /**
     * 读取图片文件
     */
    /*@RequestMapping("/readerImage")
    public void readerImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要读取的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        String[] paramsArr = decParams.split("\\|");
        if(paramsArr != null && paramsArr.length == 2){
            String imageFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[1];
            //读取预览文件
            ioReaderFile(response, imageFilePath);
        }
    }*/
    
    /***************************************在线解压（杜灿）*******************************************************/
    /**
     * @return *
     * 在线解压   
     * @param request
     * @param response
     * @param fileId  文件ID
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/decompressZip")
    @ResponseBody
    public void decompressZip(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception{
        JsonData<Object> jsonData = new JsonData<Object>();
        com.yunip.model.disk.File file = fileManager.getFileByFileId(fileId);
        UploadUser uploadUser = this.getMyInfo(request);
        if(uploadUser == null){
            jsonData.setCode(FileServersException.SFYZSX.getCode());
            jsonData.setCodeInfo(FileServersException.SFYZSX.getMsg());
        }
        try {
            //获得操作人信息
            Employee employee = employeeService.getEmployeeByEmployeeId(uploadUser.getIdentity());
            if(file == null){
                jsonData.setCode(FileServersException.FILENOEXISTS.getCode());
                jsonData.setCodeInfo(FileServersException.FILENOEXISTS.getMsg());
            } else {
                if(file.getFileSize() >= 1024 * 1024 * 1024){
                    jsonData.setCode(FileServersException.ZYWJCCDX.getCode());
                    jsonData.setCodeInfo(FileServersException.ZYWJCCDX.getMsg());
                } else {
                    //查看当前文件是否符合解压条件(zip)
                    QueueTask queueTask = new QueueTask();
                    queueTask.setEmployee(employee);
                    queueTask.setObject(file);
                    //taskName（employeeId + YYYYMMDDHHmmss）
                    String code = DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA);
                    queueTask.setTaskName(employee.getId() + code);
                    queueTask.setType(QueueTask.DES_TYPE);
                    //加入消息队列
                    SystemContant.TASKQUEUE.offer(queueTask);
                    //唤醒线程
                    QueueEntry.pleaseNotify();
                    Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
                    dataMap.put("taskName", queueTask.getTaskName());
                    dataMap.put("progress", 0);
                    jsonData.setResult(dataMap);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            jsonData.setCode(FileServersException.DECOMPRESSFAIL.getCode());
            jsonData.setCodeInfo(FileServersException.DECOMPRESSFAIL.getMsg());
        }
        String json = JsonUtils.object2json(jsonData);
        this.write("jsonpCallback(" + json + ")", response);
    }   
    
    /**
     * 查询任务进度
     * @param request
     * @param response
     * @param taskName  任务名称
     * @param progress  进度(队列中为0,正在执行为1,已经完成为2)
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/decompressInfo")
    @ResponseBody
    public void decompressInfo(HttpServletRequest request, HttpServletResponse response, String taskName, Integer progress) throws Exception{
        JsonData<Object> jsonData = new JsonData<Object>();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        try {
            if(progress == 0){
                //查队列
                boolean isExit = QueueEntry.checkQueueExist(taskName);
                if(!isExit){
                    //查询下一步
                    boolean isExitList = QueueEntry.checkListExist(taskName);
                    if(isExitList){
                        //存在
                        progress = 1;
                    } else {
                        //不存在
                        jsonData = QueueEntry.getQueueResult(taskName);
                        progress = 2;
                    }
                }
            } else if(progress == 1){
                //查询下一步
                boolean isExitList = QueueEntry.checkListExist(taskName);
                if(!isExitList){
                    //不存在
                    jsonData = QueueEntry.getQueueResult(taskName);
                    progress = 2;
                }
            }
        } catch (Exception e) {
            jsonData.setCode(FileServersException.DECOMPRESSFAIL.getCode());
            jsonData.setCodeInfo(FileServersException.DECOMPRESSFAIL.getMsg());
        }
        dataMap.put("taskName", taskName);
        dataMap.put("progress", progress);
        jsonData.setResult(dataMap);
        String json = JsonUtils.object2json(jsonData);
        this.write("jsonpCallback(" + json + ")", response);
    }  
    
    
    /**
     * 取消解压任务
     * @param request
     * @param response
     * @param taskName  任务名称
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/cancelDecompress")
    @ResponseBody
    public void canceldecompress(HttpServletRequest request, HttpServletResponse response, String taskName) throws Exception{
        JsonData<Object> jsonData = new JsonData<Object>();
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        // 加入到取消队列中
        SystemContant.CANCELMAP.put(taskName, null);
        jsonData.setResult(dataMap);
        String json = JsonUtils.object2json(jsonData);
        this.write("jsonpBack(" + json + ")", response);
    }  
    
    /**
     * 读取图片文件缩略图
     */
    @RequestMapping("/readerthumbImage")
    public void readerthumbImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String params = request.getParameter("params");
        if(!StringUtil.nullOrBlank(params)){
            Integer fileId = Integer.parseInt(params);
            readerThumbImagerStream(fileId, response);
        }
    }
}
