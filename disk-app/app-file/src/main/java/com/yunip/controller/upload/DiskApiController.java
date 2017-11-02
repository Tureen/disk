/*
 * 描述：云盘接口请求控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-6-3
 */
package com.yunip.controller.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.yunip.constant.Constant;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.enums.fileservers.FileServersOnlineEditValidateType;
import com.yunip.enums.fileservers.FileServersType;
import com.yunip.enums.teamwork.MsgType;
import com.yunip.manage.AuthorityShareManager;
import com.yunip.manage.FileManager;
import com.yunip.manage.FileServiceOperateManagerUtil;
import com.yunip.manage.TakeCodeManager;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.FolderDelete;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.fileserver.PreviewFileEntity;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;
import com.yunip.web.common.FileEncryptService;

/**
 * 云盘接口请求控制器层
 */
@Controller
@RequestMapping("/diskApi")
public class DiskApiController extends FileManagerBaseController{
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "takeCodeManager")
    private TakeCodeManager takeCodeManager;
    
    @Resource(name = "authorityShareManager")
    private AuthorityShareManager authorityShareManager;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;
    
    @Resource(name = "iEmployeeDao")
    private IEmployeeDao employeeDao;
    
    /**
     * 压缩下载缓存原文件的目录
     */
    private static final String ZIP_DOWN_SOURCE_DIR = Constant.ZIP_DOWN_TEMP_DIR;
    
    /**
     * 生成压缩文件的文件名
     */
    private static final String ZIP_DOWN_FILE_NAME = Constant.ZIP_DOWN_FILE_NAME;
    
    /**
     * 临时测试使用入口，用于监控加密队列运行情况，后续可以删除
     */
    @RequestMapping(value = "/showQueue")
    @ResponseBody
    public JsonData<String> showQueue(){
        JsonData<String> jsonData = new JsonData<String>();
        String str = "待加密文件：";
        Object[] obj = SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.toArray();
        for(Object o : obj){
            str += o.toString() + ",";
        }
        str += "|";
        str += "当前正在加密的文件：" + FileEncryptService.CURRENT_EXECUTION_ENCRYPTION_QUEUE.toString();
        jsonData.setCode(1000);
        jsonData.setCodeInfo("SUCCESS");
        jsonData.setResult(str);
        return jsonData;
    }
    
    /**
     * 删除文件
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> deleteFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try {
            //解析接口请求的数据
            Map<String, Class<FileEntity>> map = new HashMap<String, Class<FileEntity>>();
            map.put("params", FileEntity.class);
            FileOperateRequest<FileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<FileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                code = FileServersException.PARAMERROR.getCode();
                msg = FileServersException.PARAMERROR.getMsg();
                result = "FAIL";
            }else{
                boolean flag = true;//标识本次操作全部成功，有一个失败则是失败
                List<FileEntity> delList = reqData.getParams();
                //循环删除物理文件
                for(FileEntity delFile : delList){
                    try {
                        String delFilePath = uploadPath + delFile.getFilePath();
                        File file = new File(delFilePath);
                        if(file.exists() && file.isFile()){
                            //删除前备份原文件
                            String backupFileDirPath = Constant.ROOTPATH + File.separator + Constant.FILE_BACKUP_DIR + File.separator + delFile.getFileId();
                            String delFileName = FileUtil.getShortFileName(delFilePath);
                            String backupFilePath = backupFileDirPath + File.separator + delFileName;
                            File backupFileDir = new File(backupFileDirPath);
                            //判断备份目录是否存在，不存在则先进行创建
                            if(!backupFileDir.exists()){
                                backupFileDir.mkdirs();
                            }
                            FileUtil.copyFile(delFilePath, backupFilePath);
                            File backupFile = new File(backupFilePath);
                            if(backupFile.exists() && backupFile.isFile()){
                                if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(delFilePath).toLowerCase())){//删除的是图片，将其缩略图也删除
                                    String thumbFilePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + delFileName;
                                    File thumbFile = new File(thumbFilePath);
                                    if(thumbFile.exists() && thumbFile.isFile()){
                                        FileUtil.deleteFile(thumbFile);
                                    }
                                }
                                FileUtil.deleteFile(file);
                                msg += delFile.getFileId() + ":操作成功|";
                            }
                            //文件有生成预览文件的需要进行删除
                            String previewFilePath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(delFilePath);
                            String previewPdfFilePath = previewFilePath + Constant.PDF_SUFFIX;
                            String previewSwfFilePath = previewFilePath + Constant.SWF_SUFFIX;
                            File previewPdfFile = new File(previewPdfFilePath);
                            if(previewPdfFile.exists() && previewPdfFile.isFile()){
                                previewPdfFile.delete();
                            }
                            File previewSwfFile = new File(previewSwfFilePath);
                            if(previewSwfFile.exists() && previewSwfFile.isFile()){
                                previewSwfFile.delete();
                            }
                        }else{
                            msg += delFile.getFileId() + ":原文件不存在|";
                            flag = false;
                        }
                    }catch (Exception e) {
                        msg += delFile.getFileId() + ":操作异常|";
                        flag = false;
                    }
                }
                if(!flag){
                    code = FileServersException.ERROR.getCode();
                    result = "FAIL";
                }
                if(!StringUtil.nullOrBlank(msg)){
                    msg = msg.substring(0, msg.lastIndexOf("|"));
                }
            }
        }catch (Exception e) {
            code = FileServersException.ERROR.getCode();
            result = "FAIL";
            logger.error("请求接口错误：" + e.getMessage());
            e.printStackTrace();
        }
        msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 下载文件请求
     * 返回加密的后具体文件下载路径
     * 格式：目录|下载时使用的文件名|是否为打包文件|源文件名 
     * eg:upload\9|H-ui.admin_v2.4.zip|false|7a691439-948a-416b-b04e-fd5e39bc2611.zip
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> downloadFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        String ZIP_CACHE_DIR = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try {
            String downFilePath = "";//下载的文件地址
            String downFileName = "";//下载的文件名称
            //boolean isTempFile = false;//是否为打包下载生成的临时，若是下载完成后需要删除该文件
            //解析接口请求的数据
            Map<String, Class<DownloadFileEntity>> map = new HashMap<String, Class<DownloadFileEntity>>();
            map.put("params", DownloadFileEntity.class);
            FileOperateRequest<DownloadFileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<DownloadFileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue()) || StringUtil.nullOrBlank(reqData.getOperateId())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                Integer employeeId = Integer.parseInt(reqData.getOperateId());
                List<DownloadFileEntity> downloadList = reqData.getParams();
                for(DownloadFileEntity entity : downloadList){
                    List<String> fileList = entity.getFiles();
                    List<String> folderList = entity.getFolders();
                    
                    //查询当前登录所属角色
                    List<Integer> roles = getEmployeeRoles(employeeId.toString());
                    //判断是否具备下载权限
                    if(!authorityShareManager.getOpenAuthById(fileList, folderList, employeeId, roles, FileServersType.DOWNLOAD, null)){
                        throw new MyException(FileServersException.NOTAUTHORITY);
                    }
                    
                    int count = fileList.size() + folderList.size();
                    if(count == 1 && fileList.size() == 1){//直接下载单个文件
                        List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileList.get(0));
                        if(list != null && list.size() == 1){
                            com.yunip.model.disk.File downFile = list.get(0);
                            downFilePath = uploadPath + downFile.getFilePath();
                            downFileName = downFile.getFileName();
                            File oneDownFile = new File(downFilePath);
                            if(!oneDownFile.exists() || !oneDownFile.isFile()){//判断下载的文件是否存在
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }else{
                                downFilePath = getFile(downFile.getId());
                            }
                        }
                    }else if(count > 1 || folderList.size() > 0){//打包下载(多文件或目录)
                        //每一次压缩缓存的根目录
                        String tempDir = ZIP_CACHE_DIR + File.separator + UUID.randomUUID();
                        //每一次压缩原文件存放目录
                        String tempSourceDir = tempDir + File.separator + ZIP_DOWN_SOURCE_DIR;
                        File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }
                        
                        if(fileList != null && fileList.size() > 0){
                            //查询需要下载的文件并复制到进行压缩下载的缓存目录下
                            String[] downFileArr = (String[])fileList.toArray(new String[fileList.size()]);
                            List<com.yunip.model.disk.File> downFileList = fileManager.getFileByIds(downFileArr);
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
                        }
                        
                        //压缩目录
                        String zipDownPath = tempDir + File.separator + ZIP_DOWN_FILE_NAME;
                        //下载文件名
                        downFileName = ZIP_DOWN_FILE_NAME;
                        if(folderList != null && folderList.size() > 0){
                            //查询需要下载的目录并创建，同时将目录下的文件复制到进行压缩下载的缓存目录下
                            String[] downFolderArr = (String[])folderList.toArray(new String[folderList.size()]);
                            List<Folder> downFolderList = fileManager.getFolderByIds(downFolderArr);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir;
                                zipDownPath = tempDir + File.separator + downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                                downFileName = downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                            }
                            copyDirAndFile(downFolderList, tempSourceDir);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir + File.separator + downFolderList.get(0).getFolderName();
                            }
                        }
                        
                        //判断下载的目录中是否包含文件
                        if(tempDirFile.isDirectory()){
                            int fileCount = FileUtil.getDirAllFiles(tempSourceDir).size();
                            if(fileCount == 0){
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }
                        }
                        
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
                        msg = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + true + "|" + sourceFileName;
                        Des desObj = new Des();
                        msg = desObj.strEnc(msg, SystemContant.desKey, null, null);
                        msg = getRequestRootUrl(request) + "/fileManager/download?params=" + msg;
                    }
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            result = "FAIL";
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 通过提取码下载文件请求
     * 返回加密的后具体文件下载路径
     * 格式：目录|下载时使用的文件名|源文件名
     * eg:upload\9|H-ui.admin_v2.4.zip|7a691439-948a-416b-b04e-fd5e39bc2611.zip
     */
    @RequestMapping(value = "/takeCodeDownloadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> takeCodeDownloadFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        String ZIP_CACHE_DIR = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try {
            String downFilePath = "";//下载的文件地址
            String downFileName = "";//下载的文件名称
            //解析接口请求的数据
            Map<String, Class<DownloadFileEntity>> map = new HashMap<String, Class<DownloadFileEntity>>();
            map.put("params", DownloadFileEntity.class);
            FileOperateRequest<DownloadFileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<DownloadFileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                List<DownloadFileEntity> downloadList = reqData.getParams();
                for(DownloadFileEntity entity : downloadList){
                    if(StringUtil.nullOrBlank(entity.getTakeCode())){//是否有提取码
                        throw new MyException(FileServersException.TAKECODEINVALID);
                    }
                    if(!takeCodeManager.validityTakeCode(entity.getFiles(), entity.getTakeCode())){//判断提取码是否有效
                        throw new MyException(FileServersException.TAKECODEINVALID);
                    }
                    List<String> fileList = entity.getFiles();
                    List<String> folderList = entity.getFolders();
                    int count = fileList.size() + folderList.size();
                    if(count == 0){//为0表示既不下载文件又补下载文件夹
                        throw new MyException(FileServersException.PARAMERROR);
                    }
                    //每一次压缩缓存的根目录
                    String tempDir = ZIP_CACHE_DIR + File.separator + UUID.randomUUID();
                    if(count == 1 && fileList.size() == 1){//直接下载单个文件
                        //临时存放目录
                        //String tempSourceDir = tempDir;
                        /*File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }*/
                        List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileList.get(0));
                        if(list != null && list.size() == 1){
                            com.yunip.model.disk.File downFile = list.get(0);
                            //获得需要下载的原文件的路径
                            String sourceFilePath = uploadPath + downFile.getFilePath();
                            //获得需要下载的原文件的名称
                            String sourceFileName = downFile.getFileName();
                            //最终的下载文件路径
                            //String target = tempSourceDir + File.separator + sourceFileName;
                            //获得需要下载的原文件
                            File sourceFile = new File(sourceFilePath);
                            if(sourceFile.exists() && sourceFile.isFile()){
                                sourceFilePath = getFile(downFile.getId());
                                //FileUtil.copyFile(sourceFilePath, target);
                            }else{
                                throw new MyException(FileServersException.FILENOEXISTS);//判断下载的文件是否存在
                            }
                            downFilePath = sourceFilePath;
                            downFileName = sourceFileName;
                        }
                    }else if(count > 1 || folderList.size() > 0){//打包下载(多文件或目录)
                        //每一次压缩原文件存放目录
                        String tempSourceDir = tempDir + File.separator + ZIP_DOWN_SOURCE_DIR;
                        File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }
                        if(fileList != null && fileList.size() > 0){
                            //查询需要下载的文件并复制到进行压缩下载的缓存目录下
                            String[] downFileArr = (String[])fileList.toArray(new String[fileList.size()]);
                            List<com.yunip.model.disk.File> downFileList = fileManager.getFileByIds(downFileArr);
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
                                if(sourceFile.exists() && sourceFile.isFile()){
                                    sourceFilePath = getFile(downFile.getId());
                                    FileUtil.copyFile(sourceFilePath, tempSourceDir + File.separator + sourceFileName);
                                    deleteFileTempFolder(sourceFilePath);
                                }
                            }
                        }
                        
                        //压缩目录
                        String zipDownPath = tempDir + File.separator + ZIP_DOWN_FILE_NAME;
                        //下载文件名
                        downFileName = ZIP_DOWN_FILE_NAME;
                        if(folderList != null && folderList.size() > 0){
                            //查询需要下载的目录并创建，同时将目录下的文件复制到进行压缩下载的缓存目录下
                            String[] downFolderArr = (String[])folderList.toArray(new String[folderList.size()]);
                            List<Folder> downFolderList = fileManager.getFolderByIds(downFolderArr);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir;
                                zipDownPath = tempDir + File.separator + downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                                downFileName = downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                            }
                            copyDirAndFile(downFolderList, tempSourceDir);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir + File.separator + downFolderList.get(0).getFolderName();
                            }
                        }
                        
                        //判断下载的目录中是否包含文件
                        if(tempDirFile.isDirectory()){
                            int fileCount = FileUtil.getDirAllFiles(tempSourceDir).size();
                            if(fileCount == 0){
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }
                        }
                        
                        createCompressFile(tempSourceDir, zipDownPath);
                        //删除已被压缩后的目录
                        FileUtil.deleteFolder(new File(tempSourceDir));
                        //下载压缩的文件
                        downFilePath = zipDownPath;
                    }
                    //返回下载信息
                    if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                        String sourceFileName = FileUtil.getShortFileName(downFilePath);
                        String downloadPath = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + sourceFileName;
                        Des desObj = new Des();
                        downloadPath = desObj.strEnc(downloadPath, SystemContant.desKey, null, null);
                        result = getRequestRootUrl(request) + "/diskApi/download?params=" + downloadPath;
                    }
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 回收站下载
     */
    @RequestMapping(value = "/recoveryDownloadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> recoveryDownloadFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        String ZIP_CACHE_DIR = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try {
            String downFilePath = "";//下载的文件地址
            String downFileName = "";//下载的文件名称
            //解析接口请求的数据
            Map<String, Class<DownloadFileEntity>> map = new HashMap<String, Class<DownloadFileEntity>>();
            map.put("params", DownloadFileEntity.class);
            FileOperateRequest<DownloadFileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<DownloadFileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                List<DownloadFileEntity> downloadList = reqData.getParams();
                for(DownloadFileEntity entity : downloadList){
                    List<String> fileDeleteList = entity.getFiles();
                    List<String> folderDeleteList = entity.getFolders();
                    int count = fileDeleteList.size() + folderDeleteList.size();
                    if(count == 0){//为0表示既不下载文件又补下载文件夹
                        throw new MyException(FileServersException.PARAMERROR);
                    }
                    //每一次压缩缓存的根目录
                    String tempDir = ZIP_CACHE_DIR + File.separator + UUID.randomUUID();
                    if(count == 1 && fileDeleteList.size() == 1){//直接下载单个文件
                        //临时存放目录
                        /*String tempSourceDir = tempDir;
                        File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }*/
                        List<FileDelete> list = fileManager.getFileDeleteByIds(fileDeleteList.get(0));
                        
                        if(list != null && list.size() == 1){
                            FileDelete downFile = list.get(0);
                            //获得需要下载的原文件的路径
                            String sourceFilePath = uploadPath + downFile.getFilePath();
                            //获得需要下载的原文件的名称
                            String sourceFileName = downFile.getFileName();
                            //最终的下载文件路径
                            //String target = tempSourceDir + File.separator + sourceFileName;
                            //获得需要下载的原文件
                            File sourceFile = new File(sourceFilePath);
                            if(sourceFile.exists() && sourceFile.isFile()){
                                sourceFilePath = getRecycleFile(downFile.getId());
                            }else{
                                throw new MyException(FileServersException.FILENOEXISTS);//判断下载的文件是否存在
                            }
                            downFilePath = sourceFilePath;
                            downFileName = sourceFileName;
                        }
                    }else if(count > 1 || folderDeleteList.size() > 0){//打包下载(多文件或目录)
                        //每一次压缩原文件存放目录
                        String tempSourceDir = tempDir + File.separator + ZIP_DOWN_SOURCE_DIR;
                        File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }
                        if(fileDeleteList != null && fileDeleteList.size() > 0){
                            //查询需要下载的文件并复制到进行压缩下载的缓存目录下
                            String[] downFileArr = (String[])fileDeleteList.toArray(new String[fileDeleteList.size()]);
                            List<FileDelete> downFileList = fileManager.getFileDeleteByIds(downFileArr);
                            for(FileDelete downFile : downFileList){
                                //获得需要下载的原文件的路径
                                String sourceFilePath = uploadPath + downFile.getFilePath();
                                //获得需要下载的原文件的名称
                                String sourceFileName = downFile.getFileName();
                                if(StringUtil.nullOrBlank(sourceFilePath) || StringUtil.nullOrBlank(sourceFileName)){
                                    continue;
                                }
                                //获得需要下载的原文件
                                File sourceFile = new File(sourceFilePath);
                                if(sourceFile.exists() && sourceFile.isFile()){
                                    sourceFilePath = getRecycleFile(downFile.getId());
                                    FileUtil.copyFile(sourceFilePath, tempSourceDir + File.separator + sourceFileName);
                                    deleteFileTempFolder(sourceFilePath);
                                }
                            }
                        }
                        
                        //压缩目录
                        String zipDownPath = tempDir + File.separator + ZIP_DOWN_FILE_NAME;
                        //下载文件名
                        downFileName = ZIP_DOWN_FILE_NAME;
                        if(folderDeleteList != null && folderDeleteList.size() > 0){
                            //查询需要下载的目录并创建，同时将目录下的文件复制到进行压缩下载的缓存目录下
                            String[] downFolderArr = (String[])folderDeleteList.toArray(new String[folderDeleteList.size()]);
                            List<FolderDelete> downFolderList = fileManager.getFolderDeleteByIds(downFolderArr);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir;
                                zipDownPath = tempDir + File.separator + downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                                downFileName = downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                            }
                            copyDirAndFileDelete(downFolderList, tempSourceDir);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir + File.separator + downFolderList.get(0).getFolderName();
                            }
                        }
                        
                        //判断下载的目录中是否包含文件
                        if(tempDirFile.isDirectory()){
                            int fileCount = FileUtil.getDirAllFiles(tempSourceDir).size();
                            if(fileCount == 0){
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }
                        }
                        
                        createCompressFile(tempSourceDir, zipDownPath);
                        //删除已被压缩后的目录
                        FileUtil.deleteFolder(new File(tempSourceDir));
                        //下载压缩的文件
                        downFilePath = zipDownPath;
                    }
                    //返回下载信息
                    if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                        String sourceFileName = FileUtil.getShortFileName(downFilePath);
                        String downloadPath = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + sourceFileName;
                        System.out.println(downloadPath);
                        Des desObj = new Des();
                        downloadPath = desObj.strEnc(downloadPath, SystemContant.desKey, null, null);
                        result = getRequestRootUrl(request) + "/diskApi/download?params=" + downloadPath;
                    }
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 协作下载文件请求
     * 返回加密的后具体文件下载路径
     * 格式：目录|下载时使用的文件名|是否为打包文件|源文件名 
     * eg:upload\9|H-ui.admin_v2.4.zip|false|7a691439-948a-416b-b04e-fd5e39bc2611.zip
     */
    @RequestMapping(value = "/teamworkDownloadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<Integer> teamworkDownloadFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        String ZIP_CACHE_DIR = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
        JsonData<Integer> jsonData = new JsonData<Integer>();
        int code = 1000;
        String msg = "";
        int teamworkMessageId = 0;
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try {
            String downFilePath = "";//下载的文件地址
            String downFileName = "";//下载的文件名称
            //boolean isTempFile = false;//是否为打包下载生成的临时，若是下载完成后需要删除该文件
            //解析接口请求的数据
            Map<String, Class<DownloadFileEntity>> map = new HashMap<String, Class<DownloadFileEntity>>();
            map.put("params", DownloadFileEntity.class);
            FileOperateRequest<DownloadFileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<DownloadFileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue()) || StringUtil.nullOrBlank(reqData.getOperateId())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                Integer employeeId = Integer.parseInt(reqData.getOperateId());
                Integer teamworkId = null;
                List<DownloadFileEntity> downloadList = reqData.getParams();
                for(DownloadFileEntity entity : downloadList){
                    List<String> fileList = entity.getFiles();
                    List<String> folderList = entity.getFolders();
                    
                    //判断是否具备下载权限（协作员工对协作文件及文件夹的操作权限）
                    if(!authorityShareManager.getTeamworkOpenAuthById(fileList, folderList, employeeId, null)){
                        throw new MyException(FileServersException.NOTAUTHORITY);
                    }
                    //协作日志数据
                    List<Integer> messageFileIds = new ArrayList<Integer>();
                    List<Integer> messageFolderIds = new ArrayList<Integer>();
                    
                    int count = fileList.size() + folderList.size();
                    if(count == 1 && fileList.size() == 1){//直接下载单个文件
                        //协作日志文件id添加
                        messageFileIds.add(Integer.valueOf(fileList.get(0)));
                        
                        List<TeamworkFile> list = fileManager.getTeamworkFileByIds(fileList.get(0));
                        if(list != null && list.size() == 1){
                            TeamworkFile downFile = list.get(0);
                            teamworkId = downFile.getTeamworkId();
                            downFilePath = uploadPath + downFile.getFilePath();
                            downFileName = downFile.getFileName();
                            File oneDownFile = new File(downFilePath);
                            if(!oneDownFile.exists() || !oneDownFile.isFile()){//判断下载的文件是否存在
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }else{
                                downFilePath = getTeamworkFile(downFile.getId());
                            }
                        }
                    }else if(count > 1 || folderList.size() > 0){//打包下载(多文件或目录)
                        //每一次压缩缓存的根目录
                        String tempDir = ZIP_CACHE_DIR + File.separator + UUID.randomUUID();
                        //每一次压缩原文件存放目录
                        String tempSourceDir = tempDir + File.separator + ZIP_DOWN_SOURCE_DIR;
                        File tempDirFile = new File(tempSourceDir);
                        //判断压缩后的缓存目录是否存在，不存在则先进行创建
                        if(!tempDirFile.exists()){
                            tempDirFile.mkdirs();
                        }
                        
                        if(fileList != null && fileList.size() > 0){
                            //查询需要下载的文件并复制到进行压缩下载的缓存目录下
                            String[] downFileArr = (String[])fileList.toArray(new String[fileList.size()]);
                            List<TeamworkFile> downFileList = fileManager.getTeamworkFileByIds(downFileArr);
                            for(TeamworkFile downFile : downFileList){
                                //协作日志文件id添加
                                messageFileIds.add(downFile.getId());
                                teamworkId = downFile.getTeamworkId();
                                
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
                                    sourceFilePath = getTeamworkFile(downFile.getId());
                                    FileUtil.copyFile(sourceFilePath, tempSourceDir + File.separator + sourceFileName);
                                    deleteFileTempFolder(sourceFilePath);
                                }
                            }
                        }
                        
                        //压缩目录
                        String zipDownPath = tempDir + File.separator + ZIP_DOWN_FILE_NAME;
                        //下载文件名
                        downFileName = ZIP_DOWN_FILE_NAME;
                        if(folderList != null && folderList.size() > 0){
                            //查询需要下载的目录并创建，同时将目录下的文件复制到进行压缩下载的缓存目录下
                            String[] downFolderArr = (String[])folderList.toArray(new String[folderList.size()]);
                            List<TeamworkFolder> downFolderList = fileManager.getTeamworkFolderByIds(downFolderArr);
                            
                            //协作日志文件id及文件夹id添加
                            if(downFolderList != null && downFolderList.size() > 0){
                                for(TeamworkFolder downFolder : downFolderList){
                                    teamworkId = downFolder.getTeamworkId();
                                    messageFolderIds.add(downFolder.getId());
                                    teamworkMessageManager.addMessageFileOrFolderIds(messageFileIds, messageFolderIds, downFolder);
                                }
                            }
                            
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir;
                                zipDownPath = tempDir + File.separator + downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                                downFileName = downFolderList.get(0).getFolderName() + Constant.ZIP_SUFFIX;
                            }
                            copyDirAndTeamworkFile(downFolderList, tempSourceDir);
                            if(count == 1 && downFolderList.size() == 1){//下载目录只是单个目录时,打包的文件名为目录名
                                tempSourceDir = tempDir + File.separator + downFolderList.get(0).getFolderName();
                            }
                        }
                        
                        //判断下载的目录中是否包含文件
                        if(tempDirFile.isDirectory()){
                            int fileCount = FileUtil.getDirAllFiles(tempSourceDir).size();
                            if(fileCount == 0){
                                throw new MyException(FileServersException.FILENOEXISTS);
                            }
                        }
                        
                        createCompressFile(tempSourceDir, zipDownPath);
                        //删除已被压缩后的目录
                        FileUtil.deleteFolder(new File(tempSourceDir));
                        //下载压缩的文件
                        downFilePath = zipDownPath;
                        
                        //isTempFile = true;
                    }
                    
                    Employee employee = employeeDao.selectById(employeeId);
                    //协作日志
                    teamworkMessageId = teamworkMessageManager.saveTeamworkMessages(MsgType.DOWNLOAD.getType(), employeeId, employee.getEmployeeName(), teamworkId, messageFileIds, messageFolderIds);
                    
                    
                    //返回下载信息
                    if(!StringUtil.nullOrBlank(downFilePath) && !StringUtil.nullOrBlank(downFileName)){
                        String sourceFileName = FileUtil.getShortFileName(downFilePath);
                        msg = downFilePath.substring((Constant.ROOTPATH + File.separator).length(), downFilePath.lastIndexOf(sourceFileName) - 1) + "|" + downFileName + "|" + true + "|" + sourceFileName;
                        Des desObj = new Des();
                        msg = desObj.strEnc(msg, SystemContant.desKey, null, null);
                        msg = getRequestRootUrl(request) + "/fileManager/downloadTeamwork?params=" + msg;
                    }
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(teamworkMessageId);
        return jsonData;
    }
    
    /**
     * 根据文件夹的目录结构复制创建一份目录结构一样的副本
     * @param folderList 文件夹List
     * @param basePath 相对路径的根目录
     * @throws IOException 
     */
    private void copyDirAndFile(List<Folder> folderList, String basePath) throws IOException{
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        try {
            for(Folder folder : folderList){
                String folderPath = basePath + File.separator + folder.getFolderName();
                File folderFile = new File(folderPath);
                if(!folderFile.exists()){
                    folderFile.mkdirs();
                }
                List<com.yunip.model.disk.File> fileList = folder.getFiles();
                if(fileList != null && fileList.size() > 0){
                    for(com.yunip.model.disk.File file : fileList){
                        String filePath = uploadPath + file.getFilePath();
                        if(StringUtil.nullOrBlank(filePath) || StringUtil.nullOrBlank(file.getFileName())){
                            continue;
                        }
                        File sourceFile = new File(filePath);
                        if(sourceFile.exists() && sourceFile.isFile()){
                            filePath = getFile(file.getId());
                            FileUtil.copyFileAndDir(filePath, folderPath + File.separator + file.getFileName());
                            deleteFileTempFolder(filePath);
                        }
                    }
                }
                List<Folder> subFolderList = folder.getFolders();
                if(subFolderList != null && subFolderList.size() > 0){
                    copyDirAndFile(subFolderList, folderPath);
                }
            }
        }catch (IOException e) {
            logger.error("复制目录失败：" + e.getMessage());
            throw e;
        }
        
    }
    
    /**
     * 根据回收文件夹的目录结构复制创建一份目录结构一样的副本
     * @param folderList 文件夹List
     * @param basePath 相对路径的根目录
     * @throws IOException 
     */
    private void copyDirAndFileDelete(List<FolderDelete> folderDeleteList, String basePath) throws IOException{
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        try {
            for(FolderDelete folderDelete : folderDeleteList){
                String folderPath = basePath + File.separator + folderDelete.getFolderName();
                File folderFile = new File(folderPath);
                if(!folderFile.exists()){
                    folderFile.mkdirs();
                }
                List<FileDelete> fileDeleteList = folderDelete.getFileDeletes();
                if(fileDeleteList != null && fileDeleteList.size() > 0){
                    for(FileDelete fileDelete : fileDeleteList){
                        String filePath = uploadPath + fileDelete.getFilePath();
                        if(StringUtil.nullOrBlank(filePath) || StringUtil.nullOrBlank(fileDelete.getFileName())){
                            continue;
                        }
                        File sourceFile = new File(filePath);
                        if(sourceFile.exists() && sourceFile.isFile()){
                            filePath = getRecycleFile(fileDelete.getId());
                            FileUtil.copyFileAndDir(filePath, folderPath + File.separator + fileDelete.getFileName());
                            deleteFileTempFolder(filePath);
                        }
                    }
                }
                List<FolderDelete> subFolderDeleteList = folderDelete.getFolderDeletes();
                if(subFolderDeleteList != null && subFolderDeleteList.size() > 0){
                    copyDirAndFileDelete(subFolderDeleteList, folderPath);
                }
            }
        }catch (IOException e) {
            logger.error("复制目录失败：" + e.getMessage());
            throw e;
        }
        
    }
    
    /**
     * 根据协作文件夹的目录结构复制创建一份目录结构一样的副本
     * @param folderList 协作文件夹List
     * @param basePath 相对路径的根目录
     * @throws IOException 
     */
    private void copyDirAndTeamworkFile(List<TeamworkFolder> folderList, String basePath) throws IOException{
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        try {
            for(TeamworkFolder folder : folderList){
                String folderPath = basePath + File.separator + folder.getFolderName();
                File folderFile = new File(folderPath);
                if(!folderFile.exists()){
                    folderFile.mkdirs();
                }
                List<TeamworkFile> fileList = folder.getTeamworkFiles();
                if(fileList != null && fileList.size() > 0){
                    for(TeamworkFile file : fileList){
                        String filePath = uploadPath + file.getFilePath();
                        if(StringUtil.nullOrBlank(filePath) || StringUtil.nullOrBlank(file.getFileName())){
                            continue;
                        }
                        File sourceFile = new File(filePath);
                        if(sourceFile.exists() && sourceFile.isFile()){
                            filePath = getTeamworkFile(file.getId());
                            FileUtil.copyFileAndDir(filePath, folderPath + File.separator + file.getFileName());
                            deleteFileTempFolder(filePath);
                        }
                    }
                }
                List<TeamworkFolder> subFolderList = folder.getTeamworkFolders();
                if(subFolderList != null && subFolderList.size() > 0){
                    copyDirAndTeamworkFile(subFolderList, folderPath);
                }
            }
        }catch (IOException e) {
            logger.error("复制目录失败：" + e.getMessage());
            throw e;
        }
        
    }
    
    /**
     * 预览文件请求
     * 返回加密的后具体文件预览路径
     * 格式：目录|源文件名 
     * eg:upload\9|7a691439-948a-416b-b04e-fd5e39bc2611.doc
     */
    @RequestMapping(value = "/previewFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> previewFile(HttpServletRequest request,HttpServletResponse response){
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            String previewFilePath = "";//预览文件地址
            //解析接口请求的数据
            FileOperateRequest<String> reqData = JsonUtils.json2Bean(paramStr, FileOperateRequest.class);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue()) || StringUtil.nullOrBlank(reqData.getOperateId())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                String employeeId = reqData.getOperateId();
                String fileId = reqData.getParams().get(0);
                previewFilePath = previewValidate(fileId, employeeId);
                //返回预览地址
                if(!StringUtil.nullOrBlank(previewFilePath)){
                    msg = getRequestRootUrl(request) + "/fileManager/previewConvert?params=" + fileId;
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            result = "FAIL";
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 预览协作文件请求
     * 返回加密的后具体文件预览路径
     * 格式：目录|源文件名 
     * eg:upload\9|7a691439-948a-416b-b04e-fd5e39bc2611.doc
     */
    @RequestMapping(value = "/previewTeamworkFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> previewTeamworkFile(HttpServletRequest request,HttpServletResponse response){
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            String previewFilePath = "";//预览文件地址
            //解析接口请求的数据
            FileOperateRequest<String> reqData = JsonUtils.json2Bean(paramStr, FileOperateRequest.class);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue()) || StringUtil.nullOrBlank(reqData.getOperateId())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                String employeeId = reqData.getOperateId();
                String fileId = reqData.getParams().get(0);
                previewFilePath = previewValidateTeamwork(fileId, employeeId);
                //返回预览地址
                if(!StringUtil.nullOrBlank(previewFilePath)){
                    msg = getRequestRootUrl(request) + "/fileManager/teamworkPreviewConvert?params=" + fileId;
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            result = "FAIL";
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 提取码预览文件请求
     * 返回加密的后具体文件预览路径
     * 格式：目录|源文件名 
     * eg:upload\9|7a691439-948a-416b-b04e-fd5e39bc2611.doc
     */
    @RequestMapping(value = "/takeCodePreviewFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> takeCodePreviewFile(HttpServletRequest request,HttpServletResponse response){
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            String previewFilePath = "";//预览文件地址
            //解析接口请求的数据
            Map<String, Class<PreviewFileEntity>> map = new HashMap<String, Class<PreviewFileEntity>>();
            map.put("params", PreviewFileEntity.class);
            FileOperateRequest<PreviewFileEntity> reqData = JsonUtils.json2Bean(paramStr, new FileOperateRequest<PreviewFileEntity>().getClass(), map);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                PreviewFileEntity previewFile = reqData.getParams().get(0);
                if(previewFile == null){
                    throw new MyException(FileServersException.PARAMERROR);
                }
                String fileId = previewFile.getFileId();
                String takeCode = previewFile.getTakeCode();
                previewFilePath = takeCodePreviewValidate(fileId, takeCode);
                //返回预览地址
                if(!StringUtil.nullOrBlank(previewFilePath)){
                    result = getRequestRootUrl(request) + "/diskApi/previewConvert?fileId=" + fileId + "&takeCode=" + takeCode;
                }else{
                    throw new MyException(FileServersException.FILENOEXISTS);
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 提取码预览前置验证
     * @param fileId 文件ID
     * @param takeCode 提取码
     * @return 返回预览文件的路径
     */
    private String takeCodePreviewValidate(String fileId, String takeCode){
        String previewFilePath = "";
        if(StringUtil.nullOrBlank(fileId) || StringUtil.nullOrBlank(takeCode)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        List<String> privewFileList = new ArrayList<String>();
        privewFileList.add(fileId);
        if(!takeCodeManager.validityTakeCode(privewFileList, takeCode)){//判断提取码是否有效
            throw new MyException(FileServersException.TAKECODEINVALID);
        }
        List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileId);
        if(list != null && list.size() == 1){
            com.yunip.model.disk.File downFile = list.get(0);
            previewFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + downFile.getFilePath();
        }
        if(StringUtil.nullOrBlank(previewFilePath)){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        if(!Constant.previewFileType.contains(FileUtil.getFileNameExt(previewFilePath).toLowerCase())){
            throw new MyException(FileServersException.NOTSUPPORTFILEPRIVIEW);
        }
        File file = new File(previewFilePath);
        if(!file.exists() || !file.isFile()){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        return previewFilePath;
    }
    
    /**
     * 进入预览文件转换界面
     */
    @RequestMapping("/previewConvert")
    public String previewConvert(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String fileId = request.getParameter("fileId");
        String takeCode = request.getParameter("takeCode");
        takeCodePreviewValidate(fileId, takeCode);
        modelMap.put("url", getRequestRootUrl(request) + "/diskApi/preview?fileId=" + fileId + "&takeCode=" + takeCode);
        return "upload/previewConvert";
    }
    
    /**
     * 转换生成预览文件
     */
    @RequestMapping("/preview")
    public String preview(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String page = "upload/preview";
        String fileId = request.getParameter("fileId");
        String takeCode = request.getParameter("takeCode");
        takeCodePreviewValidate(fileId, takeCode);
        /*String previewFilePath = takeCodePreviewValidate(fileId, takeCode);
        try {
            OfficeConverter officeConverter = new OfficeConverter(previewFilePath);
            if(officeConverter.converter()){//转换成功
                //获得客户端请求的类型，支持H5的浏览器使用pdfjs进行预览，不支持的使用flexPage进行预览
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
        }catch (Exception e) {
            throw new MyException(FileServersException.PRIVIEWERROR);
        }*/
        Map<String, String> result = getPreviewFile(fileId, request);
        page = result.get("page");
        modelMap.put("fileName", result.get("fileName"));
        modelMap.put("url", "/diskApi/previewReader?fileId=" + fileId + "&takeCode=" + takeCode);
        return page;
    }
    
    /**
     * 转换并预览读取文件
     */
    @RequestMapping("/previewReader")
    public void previewReader(HttpServletRequest request, HttpServletResponse response){
        String fileId = request.getParameter("fileId");
        String takeCode = request.getParameter("takeCode");
        String previewFilePath = takeCodePreviewValidate(fileId, takeCode);
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
            throw new MyException(FileServersException.PRIVIEWERROR);
        }
    }
    
    /**
     * 在线编辑文件请求
     * 返回在线编辑文件的操作界面
     */
    @RequestMapping(value = "/onlineEditFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<String> onlineEditFile(HttpServletRequest request,HttpServletResponse response){
        JsonData<String> jsonData = new JsonData<String>();
        int code = 1000;
        String msg = "", result = "";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            String onlineEditFilePath = "";//文件地址
            //解析接口请求的数据
            FileOperateRequest<String> reqData = JsonUtils.json2Bean(paramStr, FileOperateRequest.class);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue()) || StringUtil.nullOrBlank(reqData.getOperateId())){
                throw new MyException(FileServersException.PARAMERROR);
            }else{
                String employeeId = reqData.getOperateId();
                String fileId = reqData.getParams().get(0);
                onlineEditFilePath = onlineEditValidate(fileId, employeeId, FileServersOnlineEditValidateType.ALL);
                //返回结果
                if(!StringUtil.nullOrBlank(onlineEditFilePath)){
                    if(Constant.onlineEditTxtFileType.contains(FileUtil.getFileNameExt(onlineEditFilePath).toLowerCase())){//进入文本编辑器页面
                        result = getRequestRootUrl(request) + "/fileManager/onlineEditTxt?params=" + fileId;
                    }else{//进入offices编辑器页面
                        result = getRequestRootUrl(request) + "/fileManager/onlineEdit?params=" + fileId;
                    }
                }
            }
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                code = ex.getErrorCode();
            }else{
                code = FileServersException.ERROR.getCode();
            }
            msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
            e.printStackTrace();
            logger.error("请求接口错误：" + msg);
        }
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 批量读取文件
     */
    @RequestMapping(value = "/batchReaderFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<List<String>> batchReaderFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        JsonData<List<String>> jsonData = new JsonData<List<String>>();
        List<String> result = new ArrayList<String>();
        int code = 1000;
        String msg = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            //解析接口请求的数据
            FileOperateRequest<String> reqData = JsonUtils.json2Bean(paramStr, FileOperateRequest.class);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                code = FileServersException.PARAMERROR.getCode();
                msg = FileServersException.PARAMERROR.getMsg();
            }else{
                List<String> fileIds = reqData.getParams();
                if(fileIds != null && fileIds.size() > 0){
                    //查询需要读取的文件
                    String[] fileIdArr = (String[])fileIds.toArray(new String[fileIds.size()]);
                    List<com.yunip.model.disk.File> fileList = fileManager.getFileByIds(fileIdArr);
                    for(com.yunip.model.disk.File temp : fileList){
                        //获得需要读取的原文件的路径
                        String sourceFilePath = uploadPath + temp.getFilePath();
                        //获得需要读取的原文件的名称
                        String sourceFileName = temp.getFileName();
                        if(StringUtil.nullOrBlank(sourceFilePath) || StringUtil.nullOrBlank(sourceFileName)){
                            continue;
                        }
                        //获得需要下载的原文件
                        File sourceFile = new File(sourceFilePath);
                        
                        if(sourceFile.exists() && sourceFile.isFile()){
                            /*String shortFileName = FileUtil.getShortFileName(sourceFilePath);
                            String readerPath = sourceFilePath.substring((Constant.ROOTPATH + File.separator).length(), sourceFilePath.lastIndexOf(shortFileName) - 1) + "|" + shortFileName;
                            Des desObj = new Des();
                            readerPath = desObj.strEnc(readerPath, SystemContant.desKey, null, null);
                            result.add(SystemContant.FILE_SERVICE_URL + "/diskApi/readerImage?params=" + readerPath);*/
                            Des desObj = new Des();
                            String resultParam = desObj.strEnc(temp.getId().toString(), SystemContant.desKey, null, null);
                            result.add(getRequestRootUrl(request) + "/diskApi/readerImage?params=" + resultParam);
                        }else{
                            result.add(SystemContant.DISK_SERVICE_URL + "/static/images/notExistsImage.png");
                        }
                    }
                }
            }
        }catch (Exception e) {
            code = FileServersException.ERROR.getCode();
            msg = FileServersException.ERROR.getMsg();
            logger.error("请求接口错误：" + e.getMessage());
            e.printStackTrace();
        }
        msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 批量读取协作文件
     */
    @RequestMapping(value = "/batchReaderTeamworkFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonData<List<String>> batchReaderTeamworkFile(HttpServletRequest request,HttpServletResponse response){
        String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
        JsonData<List<String>> jsonData = new JsonData<List<String>>();
        List<String> result = new ArrayList<String>();
        int code = 1000;
        String msg = "SUCCESS";
        String paramStr = this.getPostContent(request);
        logger.info("接口请求参数：" + paramStr);
        try{
            //解析接口请求的数据
            FileOperateRequest<String> reqData = JsonUtils.json2Bean(paramStr, FileOperateRequest.class);
            
            String serviceChkValue = FileServiceOperateManagerUtil.getChkValue(reqData);
            if(StringUtil.nullOrBlank(reqData.getChkValue()) || StringUtil.nullOrBlank(serviceChkValue) || !serviceChkValue.equals(reqData.getChkValue())){
                code = FileServersException.PARAMERROR.getCode();
                msg = FileServersException.PARAMERROR.getMsg();
            }else{
                List<String> fileIds = reqData.getParams();
                if(fileIds != null && fileIds.size() > 0){
                    //查询需要读取的文件
                    String[] fileIdArr = (String[])fileIds.toArray(new String[fileIds.size()]);
                    List<TeamworkFile> fileList = fileManager.getTeamworkFileByIds(fileIdArr);
                    for(TeamworkFile temp : fileList){
                        //获得需要读取的原文件的路径
                        String sourceFilePath = uploadPath + temp.getFilePath();
                        //获得需要读取的原文件的名称
                        String sourceFileName = temp.getFileName();
                        if(StringUtil.nullOrBlank(sourceFilePath) || StringUtil.nullOrBlank(sourceFileName)){
                            continue;
                        }
                        //获得需要下载的原文件
                        File sourceFile = new File(sourceFilePath);
                        
                        if(sourceFile.exists() && sourceFile.isFile()){
                            Des desObj = new Des();
                            String resultParam = desObj.strEnc(temp.getId().toString(), SystemContant.desKey, null, null);
                            result.add(getRequestRootUrl(request) + "/diskApi/readerTeamworkImage?params=" + resultParam);
                        }else{
                            result.add(SystemContant.DISK_SERVICE_URL + "/static/images/notExistsImage.png");
                        }
                    }
                }
            }
        }catch (Exception e) {
            code = FileServersException.ERROR.getCode();
            msg = FileServersException.ERROR.getMsg();
            logger.error("请求接口错误：" + e.getMessage());
            e.printStackTrace();
        }
        msg = LocalLanguageHelper.getI18nValue(String.valueOf(code), request);
        jsonData.setCode(code);
        jsonData.setCodeInfo(msg);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 读取图片文件
     */
    @RequestMapping("/readerImage")
    public void readerImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要读取的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        readerFileStream(Integer.parseInt(decParams), response);
        /*String[] paramsArr = decParams.split("\\|");
        if(paramsArr != null && paramsArr.length == 2){
            String imageFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[1];
            //读取预览文件
            ioReaderFile(response, imageFilePath);
        }*/
    }
    
    /**
     * 读取协作图片文件
     */
    @RequestMapping("/readerTeamworkImage")
    public void readerTeamworkImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要读取的具体文件
        String decParams = desObj.strDec(params, SystemContant.desKey, null, null);
        readerTeamworkFileStream(Integer.parseInt(decParams), response);
    }
    
    /**
     * 读取用户头像
     */
    @RequestMapping("/readerUserHeadPortraitImage")
    public void readerUserHeadPortraitImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String params = request.getParameter("params");
        if(!StringUtil.nullOrBlank(params)){
            String filePath = Constant.ROOTPATH + SystemContant.HEADERPATH + params;
            File file = new File(filePath);
            if(file.exists() && file.isFile()){
                ioReaderFile(response, filePath);
            }
        }
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
        if(paramsArr != null && paramsArr.length == 3){
            String downFilePath = Constant.ROOTPATH + File.separator + paramsArr[0] + File.separator + paramsArr[2];
            String downFileName = paramsArr[1];
            singleFileDownload(downFilePath, downFileName, true, request, response);
        }else{
            this.write("非法下载请求!", response);
        }
    }
    
    /**
     * 以文本方式在线预览文件
     */
    @RequestMapping("/onlinePreviewTxt")
    public String onlinePreviewTxt(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception{
        String fileId = request.getParameter("fileId");
        String takeCode = request.getParameter("takeCode");
        takeCodePreviewValidate(fileId, takeCode);
        String content = readFileTextContent(Integer.parseInt(fileId));
        request.setAttribute("content", content);
        return "upload/onlineEditTxt";
    }
}
