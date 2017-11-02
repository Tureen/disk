/*
 * 描述：使用WebUpload插件进行文件上传控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-4-28
 */
package com.yunip.controller.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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
import com.yunip.enums.disk.UploadType;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.manage.FileManager;
import com.yunip.model.UploadUser;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.service.IEmployeeService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.ImgCompress;
import com.yunip.utils.util.StringUtil;

/**
 * 使用WebUpload插件进行文件上传控制器层
 */
@Controller
@RequestMapping("/upload")
public class WebUploaderController extends FileManagerBaseController{
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    /**
     * 文件存放根目录(C:\\file)
     */
    //private static String rootPath = Constant.ROOTPATH;
    
    /**
     * 上传目录
     */
    private static final String UPLOAD_DIR = Constant.UPLOAD_DIR;
    
    /**
     * 上传临时目录
     */
    private static final String UPLOAD_TEMP_DIR = Constant.UPLOAD_TEMP_DIR;
    
    /**
     * 分块上传文件
     * @throws Exception 
     */
    @RequestMapping("/upLoad")
    public void upLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String rootPath = Constant.ROOTPATH;
        logger.info("文件上传请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
        
        UploadUser uploadUser = this.getMyInfo(request);
        if(uploadUser == null){
            throw new MyException(FileServersException.SFYZSX);
        }
        
        String employeeId = uploadUser.getIdentity();
        String folderId = request.getParameter("folderId");//上传到的目录
        String uploadType = request.getParameter("uploadType");//文件重命名时采取的上传方式
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间/协作空间)
        Integer zFileId = request.getParameter("zFileId") != null ? Integer.parseInt(request.getParameter("zFileId")) : null;//上一个上传文件的id
        Integer realUploadType = request.getParameter("realUploadType") != null ? Integer.parseInt(request.getParameter("realUploadType")) : null;//上一个上传文件的模式
        if(StringUtil.nullOrBlank(employeeId) || StringUtil.nullOrBlank(folderId) || StringUtil.nullOrBlank(uploadType) || UploadType.getUploadTypeByTypeValue(Integer.parseInt(uploadType)) == null || StringUtil.nullOrBlank(spaceType)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        
        String teamworkId = null;//协作ID
        if(spaceType.equals(Md5.encodeByMd5(EnumSpaceType.TEAMWORK_SPACE.getCode() + SystemContant.md5Key))){//判断是否为协作空间
            teamworkId = request.getParameter("teamworkId");//协作ID
            logger.info("文件协作ID：" + teamworkId);
            if(StringUtil.nullOrBlank(teamworkId)){//文件协作上传时，协作ID必须有
                throw new MyException(FileServersException.PARAMERROR);
            }
        }
        
        String filePath = "";//文件上传成功后的路径
        /*Map<String, String> result = new HashMap<String, String>();
        result.put("status", "false");
        result.put("filePath", "");//完全上传成功后，文件地址。只有全部上传成功并合并后才有*/
        //文件保存根目录(未配置其他盘符路径时，默认放在部署所在服务器目录下)
        if(StringUtil.nullOrBlank(rootPath)){
            rootPath = request.getRealPath("/");
        }
        
        String chunk = request.getParameter("chunk");//当前文件块数
        String chunks = request.getParameter("chunks");//文件块总数
        if(StringUtil.nullOrBlank(chunk)){
            chunk = "0";
        }
        if(StringUtil.nullOrBlank(chunks)){
            chunks = "1";
        }
        try {
            //文件上传的临时目录
            String tempDir = rootPath + File.separator + UPLOAD_TEMP_DIR + File.separator + getFileTempDir(request);
            //用户上传文件的保存目录
            String userDir = getFileSaveDir(request);
            String saveDir = rootPath + File.separator + UPLOAD_DIR + File.separator + userDir;
            //解析器解析request的上下文，创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 
            //先判断request中是否包涵multipart类型的数据(判断 request 是否有文件上传,即多部分请求)
            if(multipartResolver.isMultipart(request)){
                //再将request中的数据转化成multipart类型的数据
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while(iter.hasNext()){
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if(file != null && !file.isEmpty()){
                        //取得当前上传文件的文件名称
                        String oldFileName = file.getOriginalFilename();
                        logger.info("原文件名：" + oldFileName);
                        //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if(!oldFileName.trim().equals("")){
                            String path = tempDir + File.separator + chunk;
                            File localFile = new File(path);
                            if (!localFile.exists()) {//判断该文件夹是否存在，不存在则创建
                                localFile.mkdirs();
                            }
                            //写文件到本地
                            file.transferTo(localFile);
                            
                            //合并文件
                            String mergeFileName = UUID.randomUUID() + FileUtil.getFileNameExt(oldFileName);
                            if(mergePartFiles(tempDir, Integer.parseInt(chunks), saveDir, mergeFileName)){
                                //合并成功后删除临时目录
                                boolean flag = FileUtil.deleteFolder(new File(tempDir));
                                logger.info("删除临时目录：" + tempDir + "，结果：" + flag);
                                filePath = saveDir + File.separator + mergeFileName;
                                if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(mergeFileName).toLowerCase())){//上传文件的是图片，则生成一张缩略图
                                    String thumbImagePath = saveDir + File.separator + Constant.thumbImagePrefixName + mergeFileName;
                                    try {
                                        File imgFile = new File(filePath);
                                        if(imgFile.exists()){
                                            if(imgFile.length() <= 1024 * 20){//当图片大小小于20K时，直接复制不进行压缩
                                                FileUtil.copyFile(imgFile.getPath(), thumbImagePath);
                                            }else{
                                                ImgCompress imgCom = new ImgCompress(filePath, thumbImagePath);
                                                imgCom.resizeFix(100, 100);
                                            }
                                        }
                                    }catch (Exception e) {
                                        logger.error("生成缩略图失败："+ thumbImagePath + ",原因：" + e.getMessage());
                                    }
                                }
                                
                                if(spaceType.equals(Md5.encodeByMd5(EnumSpaceType.TEAMWORK_SPACE.getCode() + SystemContant.md5Key))){//上传到文件协作空间下
                                    //文件协作的上传
                                    TeamworkFile teamworkFile = new TeamworkFile();
                                    teamworkFile.setTeamworkId(Integer.parseInt(teamworkId));
                                    teamworkFile.setEmployeeId(Integer.parseInt(employeeId));
                                    teamworkFile.setFolderId(Integer.parseInt(folderId));
                                    teamworkFile.setFileName(oldFileName);
                                    teamworkFile.setFileSize(FileUtil.getFileSize(filePath));
                                    teamworkFile.setFilePath(userDir + File.separator + mergeFileName);//路径存放相对路径
                                    //查询缓存中版本文件保留数
                                    String fileVersionNum = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.FILEVERSIONNUM.getKey());
                                    int teamworkMessageId = fileManager.saveTeamworkFile(teamworkFile, UploadType.getUploadTypeByTypeValue(Integer.parseInt(uploadType)), Integer.parseInt(employeeId),Integer.valueOf(fileVersionNum), this.getClientIP(request), zFileId, realUploadType);
                                    //将协作消息id加入待传递json对象teamworkFile中
                                    teamworkFile.setTeamworkMessageId(teamworkMessageId);
                                    //添加协作文件到待加密队列中
                                    addFileToEncryptQueue(teamworkFile);
                                    String json = JsonUtils.object2json(teamworkFile);
                                    this.write(json, response);
                                }else{
                                    //TODO 模板将上传的文件保存到数据库
                                    com.yunip.model.disk.File myFile = new com.yunip.model.disk.File();
                                    myFile.setEmployeeId(Integer.parseInt(employeeId));
                                    myFile.setFolderId(Integer.parseInt(folderId));
                                    myFile.setFileName(oldFileName);
                                    myFile.setFileSize(FileUtil.getFileSize(filePath));
                                    myFile.setFilePath(userDir + File.separator + mergeFileName);//路径存放相对路径
                                    if(spaceType.equals(Md5.encodeByMd5(EnumSpaceType.PUBLIC_SPACE.getCode() + SystemContant.md5Key))){//判断是否为公共空间
                                        myFile.setEmployeeId(SystemContant.MANAGER_EMPLOYEE_ID);//特殊处理，公共空间操作时使用的员工ID默认为1
                                    }
                                    //查询缓存中版本文件保留数
                                    String fileVersionNum = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.FILEVERSIONNUM.getKey());
                                    fileManager.saveFile(myFile, UploadType.getUploadTypeByTypeValue(Integer.parseInt(uploadType)), Integer.parseInt(employeeId),Integer.valueOf(fileVersionNum), this.getClientIP(request));
                                    
                                    //TODO 结束
                                    //添加文件到待加密队列中
                                    addFileToEncryptQueue(myFile);
                                }
                                //result.put("status", "true");
                                //result.put("filePath", (userDir + File.separator + mergeFileName).replace("\\", "/"));
                                logger.info("文件上传成功！文件位置：" + filePath);
                            }
                        }
                    }
                }
            }
            
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            //上传失败则删除文件
            if(!StringUtil.nullOrBlank(filePath)){
                FileUtil.deleteFile(filePath);
            }
            throw e;
        }
        
    }
    
    /**
     * 验证文件块是否已上传(用于验证断点续传已经上传过的文件块验证)
     */
    @ResponseBody
    @RequestMapping("/chunkCheck")
    public JsonData<Boolean> chunkCheck(HttpServletRequest request, HttpServletResponse response){
        String rootPath = Constant.ROOTPATH;
        JsonData<Boolean> jsonData = new JsonData<Boolean>();
        jsonData.setResult(false);
        String uniqueFileName = request.getParameter("file");
        String chunkIndex = request.getParameter("chunkIndex");
        String size = request.getParameter("size");
        //TODO 服务端验证暂时不增加
        /*String fileName = request.getParameter("fileName");
        if(!validateFileTypeIsUpload(fileName)){
            throw new MyException(FileServersException.FILETYPENOTUPLOAD);
        }*/
        //文件保存根目录(未配置其他盘符路径时，默认放在部署所在服务器目录下)
        if(StringUtil.nullOrBlank(rootPath)){
            rootPath = request.getRealPath("/");
        }
        String filePath = rootPath + File.separator + UPLOAD_TEMP_DIR + File.separator + uniqueFileName + File.separator + chunkIndex;
        long fileSize = FileUtil.getFileSize(filePath);
        logger.info("当前验证的文件块：" + filePath + "文件大小：" + fileSize);
        if(Long.parseLong(size) == fileSize){
            jsonData.setResult(true);
        }
        return jsonData;
    }
    
    /**
     * 验证文件类型是否允许上传
     * @param fileName 文件名
     */
    private boolean validateFileTypeIsUpload(String fileName){
        boolean flag = false;
        if(StringUtil.nullOrBlank(fileName)){
            return flag;
        }
        String suffix = FileUtil.getFileNameExt(fileName);
        if(!StringUtil.nullOrBlank(suffix) && suffix.indexOf(".") != -1){
            suffix = suffix.substring(1, suffix.length()).toLowerCase();
        }
        String uploadModelType = SysConfigHelper.getValue(SystemContant.BASICSCODE, "UPLOADMODE");//可上传文件类型验证模式
        String validateType = SysConfigHelper.getValue(SystemContant.BASICSCODE, uploadModelType);
        if(!StringUtil.nullOrBlank(validateType)){
            List<String> validateFormat = Arrays.asList(validateType.split(","));
            if(uploadModelType.equals(BasicsInfoCode.THROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateType)){//允许上传类型
                for(String str : validateFormat){
                    if(str.equalsIgnoreCase(suffix)){
                        flag = true;
                        return flag;
                    }
                }
            }else if(uploadModelType.equals(BasicsInfoCode.NOTHROUGHUPLOAD.getKey()) && !StringUtil.nullOrBlank(validateType)){//禁止上传类型
                for(String str : validateFormat){
                    if(str.equalsIgnoreCase(suffix)){
                        return flag;
                    }
                }
                flag = true;
            }
        }else{
            flag = true;
        }
        return flag;
    }
    
    /**
     * 生成文件的临时目录
     * @param request
     */
    private String getFileTempDir(HttpServletRequest request) throws Exception{
        String tempDir = "";
        String userId = request.getParameter("userId");//用户ID
        String fileId = request.getParameter("id");//上传文件控件ID
        String fileName = request.getParameter("name");//文件名称
        String fileType = request.getParameter("type");//文件类型
        String fileLastModifiedDate = request.getParameter("lastModifiedDate");//文件最后修改时间
        String fileSize = request.getParameter("size");//文件大小
        tempDir = Md5.encodeByMd5(userId + fileId + fileName + fileType + fileLastModifiedDate + fileSize);
        return tempDir;
    }
    
    
    /**
     * 生成文件的保存目录
     * @param request
     */
    private String getFileSaveDir(HttpServletRequest request) throws Exception{
        String userDir = request.getParameter("userId");//用户ID
        String spaceType = request.getParameter("spaceType");//空间类型(个人空间/公共空间)
        if(!StringUtil.nullOrBlank(spaceType) && spaceType.equals(Md5.encodeByMd5(EnumSpaceType.PUBLIC_SPACE.getCode() + SystemContant.md5Key))){//判断是否为公共空间
            userDir = SystemContant.MANAGER_EMPLOYEE_ID.toString();//特殊处理，公共空间操作时使用的员工ID默认为1
        }
        return userDir;
    }
    
    /**
     * 合并目录下的所有分块文件
     * @param dirPath 目录
     * @param chunks 当前文件的被分块的总个数
     * @param targetDirPath 合并后的新文件存放目录
     * @param mergeFileName 合并后文件名称
     * boolean 返回是否合并成功 
     * @throws IOException 
     */
    private boolean mergePartFiles(String dirPath, int chunks, String targetDirPath, String mergeFileName) throws IOException{
        boolean flag = false;
        //获得上传文件的临时缓存目录下分块文件的数量
        int partFileNum = FileUtil.getDirFileCount(dirPath);
        if(chunks > 0 && partFileNum == chunks){//判断所有分块是否上传完成
            logger.info("所有文件上传完毕，开始合并……");
            long t1 = System.currentTimeMillis();
            ArrayList<File> partFiles = FileUtil.getDirFiles(dirPath);
            Collections.sort(partFiles, new FileComparator());
            File dirFile =new File(targetDirPath);    
            if(!dirFile.exists()  && !dirFile.isDirectory()){       
                dirFile .mkdirs();    
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(targetDirPath + File.separator + mergeFileName, "rw");
            for(File file : partFiles){
                FileInputStream fs = new FileInputStream(file);
                byte[] b = new byte[fs.available()];
                fs.read(b);
                fs.close();
                randomAccessFile.write(b);
            }
            randomAccessFile.close();
            flag = true;
            long t2 = System.currentTimeMillis();
            logger.info("合并文件完毕，耗时：" + (t2 - t1) + "毫秒");
        }
        return flag;
    }
    
    /**
     * 在线编辑文件上传
     */
    @RequestMapping("/onlineEditUpLoad")
    public void onlineEditUpLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String rootPath = Constant.ROOTPATH;
        String result = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SAVE_SUCCESS, request);
        logger.info("在线编辑文件上传请求start……");
        UploadUser uploadUser = this.getMyInfo(request);
        if(uploadUser == null){
            throw new MyException(FileServersException.SFYZSX);
        }
        
        //获得操作人信息
        Employee employee = employeeService.getEmployeeByEmployeeId(uploadUser.getIdentity());
        employee.setLoginIp(this.getClientIP(request));
        
        //文件保存根目录(未配置其他盘符路径时，默认放在部署所在服务器目录下)
        if(StringUtil.nullOrBlank(rootPath)){
            rootPath = request.getRealPath("/");
        }
        
        //编辑的文件ID
        String fileId = request.getParameter("fileId");
        if(StringUtil.nullOrBlank(fileId)){
            throw new MyException(FileServersException.ERROR);
        }
        
        List<com.yunip.model.disk.File> fileList = fileManager.getFileByIds(fileId);
        if(fileList == null || fileList.size() != 1){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        //当前操作的文件
        com.yunip.model.disk.File operateFile = fileList.get(0);
        
        try {
            //文件上传的临时目录
            String tempDir = rootPath + File.separator + UPLOAD_TEMP_DIR + File.separator;
            //文件上传目录
            String uploadPath = rootPath + File.separator + UPLOAD_DIR + File.separator;
            //解析器解析request的上下文，创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 
            //先判断request中是否包涵multipart类型的数据(判断 request 是否有文件上传,即多部分请求)
            if(multipartResolver.isMultipart(request)){
                //再将request中的数据转化成multipart类型的数据
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while(iter.hasNext()){
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if(file != null && !file.isEmpty()){
                        //取得当前上传文件的文件名称
                        String oldFileName = file.getOriginalFilename();
                        logger.info("原文件名：" + oldFileName);
                        //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if(!oldFileName.trim().equals("")){
                            String tempFilePath = tempDir + File.separator + UUID.randomUUID();
                            File localFile = new File(tempFilePath + File.separator + oldFileName);
                            if (!localFile.exists()) {//判断该文件夹是否存在，不存在则创建
                                localFile.mkdirs();
                            }
                            file.transferTo(localFile);
                            
                            String operateFilePath = uploadPath + operateFile.getFilePath();
                            File operateTempFile = new File(operateFilePath);
                            if(operateTempFile.exists() && operateTempFile.isFile()){//判断原文件是否存在，存在则进行覆盖
                                boolean isOnlyFile = fileManager.isOnlyFilePath(operateFile.getFilePath());//判断该文件是否仅被引用一次，是则删除
                                //替换为修改后新的文件
                                String newFilePath = FileUtil.getFileParentPath(operateFilePath) + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(operateFilePath);
                                FileUtil.copyFile(localFile.getPath(), newFilePath);
                                com.yunip.model.disk.File newFile = new com.yunip.model.disk.File();
                                newFile.setId(operateFile.getId());
                                newFile.setFileName(operateFile.getFileName());
                                newFile.setFolderId(operateFile.getFolderId());
                                newFile.setFileSize(FileUtil.getFileSize(newFilePath));
                                newFile.setFilePath(newFilePath.substring(newFilePath.indexOf(UPLOAD_DIR) + UPLOAD_DIR.length() + 1, newFilePath.length()));
                                newFile.setUpdateTime(new Date());
                                newFile.setUpdateAdmin(employee.getEmployeeName());
                                fileManager.updateFile(newFile, employee);
                                if(isOnlyFile){//判断该文件是否仅被引用一次，是则删除
                                    operateTempFile.delete();
                                }
                                logger.info("删除上传的在线编辑文件所在的缓存目录：" + tempFilePath);
                                FileUtil.deleteFolder(new File(tempFilePath));
                                //添加文件到待加密队列中
                                addFileToEncryptQueue(newFile);
                            }else{
                                result = "原文件被删除！";
                            }
                            //文件被在线编辑过，有生成预览文件的需要进行删除
                            String previewFilePath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(operateFilePath);
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
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result = LocalLanguageHelper.getI18nValue(I18nContant.I18N_FILE_SAVE_FAIL, request);
        }
        this.write(result, response);
    }
    
    /**
     * 自定义比较器内部类(根据文件名进行从小到大的排序)
     */
    private class FileComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            return new Integer(o1.getName()).compareTo(new Integer(o2.getName()));
        }
    }
}
