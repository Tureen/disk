/*
 * 描述：使用Stream插件进行文件上传控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-7-7
 */
package com.yunip.controller.twinklingstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.Constant;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.manage.FileManager;
import com.yunip.model.UploadUser;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.plugins.stream.config.Configurations;
import com.yunip.plugins.stream.constant.StreamConstant;
import com.yunip.plugins.stream.servlet.Range;
import com.yunip.plugins.stream.util.IoUtil;
import com.yunip.plugins.stream.util.TokenUtil;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.ImgCompress;
import com.yunip.utils.util.StringUtil;
import com.yunip.web.common.StreamException;

/**
 * 使用Stream插件进行文件上传控制器层
 */
@Controller
@RequestMapping("/streamUpload")
public class StreamUploaderContoller extends FileManagerBaseController{
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    static final String FILE_NAME_FIELD = "name";
    static final String FILE_SIZE_FIELD = "size";
    static final String FILE_LAST_MODIFIED_DATE = "modified";
    static final String TOKEN_FIELD = "token";
    static final String SERVER_FIELD = "server";
    static final String SUCCESS = "success";
    static final String MESSAGE = "message";
    
    /**
     * GET方式调用上传
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public void getUpLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       logger.info("get文件上传请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
       final String token = req.getParameter(StreamConstant.TOKEN_FIELD);
       final String size = req.getParameter(StreamConstant.FILE_SIZE_FIELD);
       final String fileName = req.getParameter(StreamConstant.FILE_NAME_FIELD);
       final PrintWriter writer = resp.getWriter();
       
       /** TODO: validate your token. */
       
       JSONObject json = new JSONObject();
       long start = 0;
       boolean success = true;
       String message = "";
       try {
           File f = IoUtil.getTokenedFile(token);
           start = f.length();
           /** file size is 0 bytes. */
           if (token.endsWith("_0") && "0".equals(size) && 0 == start)
               f.renameTo(IoUtil.getFile(fileName, token));
       } catch (FileNotFoundException fne) {
           message = "Error: " + fne.getMessage();
           success = false;
       } finally {
           try {
               if (success)
                   json.put(StreamConstant.START_FIELD, start);
               json.put(StreamConstant.SUCCESS, success);
               json.put(StreamConstant.MESSAGE, message);
           } catch (JSONException e) {}
           writer.write(json.toString());
           IoUtil.close(writer);
       }
    }
    
    /**
     * POST方式调用上传
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void postUpLoad(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        JSONObject json = new JSONObject();
        
       logger.info("post文件上传token请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
       final String token = req.getParameter(StreamConstant.TOKEN_FIELD);
       final String fileName = req.getParameter(StreamConstant.FILE_NAME_FIELD);
       String folderId = req.getParameter(StreamConstant.FOLDER_ID);
       final String spaceType = req.getParameter(StreamConstant.SPACE_TYPE);//空间类型(个人空间/公共空间)
       logger.info("参数：" + token + "_" + fileName + "_" + folderId + "_" + spaceType);
       
       if(folderId.equals("-1")){//判断是否来自PC客户端程序的请求（folderId为-1，视作来自PC客户端的操作）
           if(!isPCClientRequest(req) || !spaceType.equals(Md5.encodeByMd5(EnumSpaceType.PRIVATE_SPACE.getCode() + SystemContant.md5Key))){//PC客户端操作上传时，文件默认上传到个人空间，目录id为-1(来自PC的备份)目录下
               json.put(SUCCESS, false);
               json.put(MESSAGE, "非法请求");
               this.write(json.toString(), resp);
               return;
           }
       }
       
       UploadUser uploadUser = this.getMyInfo(req);
       String operateId = uploadUser.getIdentity();
       if(spaceType.equals(Md5.encodeByMd5(EnumSpaceType.PUBLIC_SPACE.getCode() + SystemContant.md5Key))){//判断是否为公共空间
           operateId = SystemContant.MANAGER_EMPLOYEE_ID.toString();//特殊处理，公共空间操作时使用的员工ID默认为1
       }
       
       if(StringUtil.nullOrBlank(operateId) || StringUtil.nullOrBlank(folderId)|| StringUtil.nullOrBlank(spaceType)){
           throw new MyException(FileServersException.PARAMERROR);
       }
       Range range = IoUtil.parseRange(req);
       
       OutputStream out = null;
       InputStream content = null;
       //final PrintWriter writer = resp.getWriter();
       
       /** TODO: validate your token. */
       
       
       long start = 0;
       boolean success = true;
       String message = "";
       File f = IoUtil.getTokenedFile(token);
       try {
           if (f.length() != range.getFrom()) {
               /** drop this uploaded data */
               throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
           }
           
           out = new FileOutputStream(f, true);
           content = req.getInputStream();
           int read = 0;
           final byte[] bytes = new byte[StreamConstant.STREAM_BUFFER_LENGTH];
           while ((read = content.read(bytes)) != -1)
               out.write(bytes, 0, read);

           start = f.length();
       } catch (StreamException se) {
           success = StreamException.ERROR_FILE_RANGE_START == se.getCode();
           message = "Code: " + se.getCode();
       } catch (FileNotFoundException fne) {
           message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
           success = false;
       } catch (IOException io) {
           message = "IO Error: " + io.getMessage();
           success = false;
       } finally {
           IoUtil.close(out);
           IoUtil.close(content);

           /** rename the file */
           if (range.getSize() == start) {
               /** fix the `renameTo` bug */
               File dst = IoUtil.getFile(fileName, token);
               dst.delete();
               f.renameTo(dst);
               System.out.println("TK: `" + token + "`, NE: `" + fileName + "`");
               
               String filePath = "";//文件上传成功后的路径
               try {
                
                   /**************业务操作****************/
                   Employee employee = new Employee();
                   employee.setId(Integer.parseInt(operateId));
                   employee.setEmployeeName(uploadUser.getName());
                   
                   String fileDirPath = Configurations.getFileRepository(token);
                   String myFileName = FileUtil.getShortFileName(fileName);
                   
                   String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
                   String newFilePath =  operateId + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(myFileName);
                   String completeNewFilePath = uploadPath + newFilePath;
                   File newFile = new File(completeNewFilePath);
                   //目录不存在时创建
                   if (!newFile.getParentFile().exists()){
                       newFile.getParentFile().mkdirs();
                   }
                       
                   if (!newFile.exists()){
                       newFile.createNewFile();
                   }
                       
                   newFile.delete();
                   dst.renameTo(newFile);
                   filePath = completeNewFilePath;
                   
                   if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(completeNewFilePath).toLowerCase())){//上传文件的是图片，则生成一张缩略图
                       String thumbImagePath = uploadPath + operateId + File.separator + Constant.thumbImagePrefixName + FileUtil.getShortFileName(completeNewFilePath);
                       try {
                           File imgFile = new File(completeNewFilePath);
                           if(imgFile.exists()){
                               if(imgFile.length() <= 1024 * 20){//当图片大小小于20K时，直接复制不进行压缩
                                   FileUtil.copyFile(imgFile.getPath(), thumbImagePath);
                               }else{
                                   ImgCompress imgCom = new ImgCompress(imgFile.getPath(), thumbImagePath);
                                   imgCom.resizeFix(100, 100);
                               }
                           }
                       }catch (Exception e) {
                           logger.error("生成缩略图失败："+ thumbImagePath + ",原因：" + e.getMessage());
                       }
                   }
                   
                   com.yunip.model.disk.File file = new com.yunip.model.disk.File();
                   file.setEmployeeId(Integer.parseInt(operateId));
                   file.setFileName(myFileName);
                   file.setFileSize(FileUtil.getFileSize(completeNewFilePath));
                   file.setFilePath(newFilePath);
                   folderId = fileManager.getFinalFolderId(Integer.parseInt(folderId), employee).toString();
                   Folder folder = new Folder();
                   if(fileName.indexOf("/") == -1){//上传文件
                       file.setFolderId(Integer.parseInt(folderId));
                       fileManager.insertUploadFile(file, employee);
                   }else{//上传的文件夹
                       folder.setParentId(Integer.parseInt(folderId));
                       String subDirPath = fileName.substring(0, fileName.lastIndexOf("/"));
                       createFolder(folder, file, subDirPath);
                       fileManager.insertFolderAndFile(folder, null, employee);
                   }
                   
                   /** if `STREAM_DELETE_FINISH`, then delete it. */
                   if (Configurations.isDeleteFinished()) {
                       File delFolder = new File(fileDirPath);
                       if(delFolder.exists() && delFolder.isDirectory()){
                           FileUtil.deleteFolder(delFolder);
                       }
                       //dst.delete();
                   }
                   
                   //文件上传成功，添加到待加密队列中进行加密
                   addFileToEncryptQueue(file);
               }catch (Exception e) {
                   message = "上传失败";
                   success = false;
                   //上传失败则删除文件
                   if(!StringUtil.nullOrBlank(filePath)){
                       FileUtil.deleteFile(filePath);
                   }
                   e.printStackTrace();
               }
           }
           try {
               if (success)
                   json.put(StreamConstant.START_FIELD, start);
               json.put(StreamConstant.SUCCESS, success);
               json.put(StreamConstant.MESSAGE, message);
           } catch (JSONException e) {}
           
           /*writer.write(json.toString());
           IoUtil.close(writer);*/
           this.write(json.toString(), resp);
       }
    }
    
    /**
     * GET方式调用上传
     */
    @RequestMapping(value = "/uploadTeamwork", method = RequestMethod.GET)
    public void getUpLoadTeamwork(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       logger.info("get文件上传请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
       final String token = req.getParameter(StreamConstant.TOKEN_FIELD);
       final String size = req.getParameter(StreamConstant.FILE_SIZE_FIELD);
       final String fileName = req.getParameter(StreamConstant.FILE_NAME_FIELD);
       final PrintWriter writer = resp.getWriter();
       
       /** TODO: validate your token. */
       
       JSONObject json = new JSONObject();
       long start = 0;
       boolean success = true;
       String message = "";
       try {
           File f = IoUtil.getTokenedFile(token);
           start = f.length();
           /** file size is 0 bytes. */
           if (token.endsWith("_0") && "0".equals(size) && 0 == start)
               f.renameTo(IoUtil.getFile(fileName, token));
       } catch (FileNotFoundException fne) {
           message = "Error: " + fne.getMessage();
           success = false;
       } finally {
           try {
               if (success)
                   json.put(StreamConstant.START_FIELD, start);
               json.put(StreamConstant.SUCCESS, success);
               json.put(StreamConstant.MESSAGE, message);
           } catch (JSONException e) {}
           writer.write(json.toString());
           IoUtil.close(writer);
       }
    }
    
    /**
     * POST方式调用上传
     */
    @RequestMapping(value = "/uploadTeamwork", method = RequestMethod.POST)
    public void postUpLoadTeamwork(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        JSONObject json = new JSONObject();
        
       logger.info("post文件上传token请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
       final String token = req.getParameter(StreamConstant.TOKEN_FIELD);
       final String fileName = req.getParameter(StreamConstant.FILE_NAME_FIELD);
       String folderId = req.getParameter(StreamConstant.FOLDER_ID);
       String teamworkId = req.getParameter("teamworkId");
       final String spaceType = req.getParameter(StreamConstant.SPACE_TYPE);//空间类型(个人空间/公共空间)
       Integer zFileId = req.getParameter("zFileId") != null ? Integer.parseInt(req.getParameter("zFileId")) : null;//上一个上传文件的id
       Integer teamworkMessageId = null;
       logger.info("参数：" + token + "_" + fileName + "_" + folderId + "_" + spaceType);
       
       if(folderId.equals("-1")){//判断是否来自PC客户端程序的请求（folderId为-1，视作来自PC客户端的操作）
           if(!isPCClientRequest(req) || !spaceType.equals(Md5.encodeByMd5(EnumSpaceType.TEAMWORK_SPACE.getCode() + SystemContant.md5Key))){//PC客户端操作上传时，文件默认上传到个人空间，目录id为-1(来自PC的备份)目录下
               json.put(SUCCESS, false);
               json.put(MESSAGE, "非法请求");
               this.write(json.toString(), resp);
               return;
           }
       }
       
       UploadUser uploadUser = this.getMyInfo(req);
       String operateId = uploadUser.getIdentity();
       
       if(StringUtil.nullOrBlank(operateId) || StringUtil.nullOrBlank(folderId)|| StringUtil.nullOrBlank(spaceType)){
           throw new MyException(FileServersException.PARAMERROR);
       }
       Range range = IoUtil.parseRange(req);
       
       OutputStream out = null;
       InputStream content = null;
       //final PrintWriter writer = resp.getWriter();
       
       /** TODO: validate your token. */
       
       
       long start = 0;
       boolean success = true;
       String message = "";
       File f = IoUtil.getTokenedFile(token);
       try {
           if (f.length() != range.getFrom()) {
               /** drop this uploaded data */
               throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
           }
           
           out = new FileOutputStream(f, true);
           content = req.getInputStream();
           int read = 0;
           final byte[] bytes = new byte[StreamConstant.STREAM_BUFFER_LENGTH];
           while ((read = content.read(bytes)) != -1)
               out.write(bytes, 0, read);

           start = f.length();
       } catch (StreamException se) {
           success = StreamException.ERROR_FILE_RANGE_START == se.getCode();
           message = "Code: " + se.getCode();
       } catch (FileNotFoundException fne) {
           message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
           success = false;
       } catch (IOException io) {
           message = "IO Error: " + io.getMessage();
           success = false;
       } finally {
           IoUtil.close(out);
           IoUtil.close(content);

           /** rename the file */
           if (range.getSize() == start) {
               /** fix the `renameTo` bug */
               File dst = IoUtil.getFile(fileName, token);
               dst.delete();
               f.renameTo(dst);
               System.out.println("TK: `" + token + "`, NE: `" + fileName + "`");
               
               String filePath = "";//文件上传成功后的路径
               try {
                
                   /**************业务操作****************/
                   Employee employee = new Employee();
                   employee.setId(Integer.parseInt(operateId));
                   employee.setEmployeeName(uploadUser.getName());
                   
                   String fileDirPath = Configurations.getFileRepository(token);
                   String myFileName = FileUtil.getShortFileName(fileName);
                   
                   String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator;
                   String newFilePath =  operateId + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(myFileName);
                   String completeNewFilePath = uploadPath + newFilePath;
                   File newFile = new File(completeNewFilePath);
                   //目录不存在时创建
                   if (!newFile.getParentFile().exists()){
                       newFile.getParentFile().mkdirs();
                   }
                       
                   if (!newFile.exists()){
                       newFile.createNewFile();
                   }
                       
                   newFile.delete();
                   dst.renameTo(newFile);
                   filePath = completeNewFilePath;
                   
                   if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(completeNewFilePath).toLowerCase())){//上传文件的是图片，则生成一张缩略图
                       String thumbImagePath = uploadPath + operateId + File.separator + Constant.thumbImagePrefixName + FileUtil.getShortFileName(completeNewFilePath);
                       try {
                           File imgFile = new File(completeNewFilePath);
                           if(imgFile.exists()){
                               if(imgFile.length() <= 1024 * 20){//当图片大小小于20K时，直接复制不进行压缩
                                   FileUtil.copyFile(imgFile.getPath(), thumbImagePath);
                               }else{
                                   ImgCompress imgCom = new ImgCompress(imgFile.getPath(), thumbImagePath);
                                   imgCom.resizeFix(100, 100);
                               }
                           }
                       }catch (Exception e) {
                           logger.error("生成缩略图失败："+ thumbImagePath + ",原因：" + e.getMessage());
                       }
                   }
                   
                   TeamworkFile file = new TeamworkFile();
                   file.setTeamworkId(Integer.parseInt(teamworkId));
                   file.setEmployeeId(Integer.parseInt(operateId));
                   file.setFileName(myFileName);
                   file.setFileSize(FileUtil.getFileSize(completeNewFilePath));
                   file.setFilePath(newFilePath);
                   TeamworkFolder folder = new TeamworkFolder();
                   folder.setTeamworkId(Integer.parseInt(teamworkId));
                   if(fileName.indexOf("/") == -1){//上传文件
                       file.setFolderId(Integer.parseInt(folderId));
                       teamworkMessageId = fileManager.insertUploadTeamworkFile(file, employee, zFileId);
                   }else{//上传的文件夹
                       folder.setParentId(Integer.parseInt(folderId));
                       String subDirPath = fileName.substring(0, fileName.lastIndexOf("/"));
                       createTeamworkFolder(folder, file, subDirPath);
                       teamworkMessageId = fileManager.insertTeamworkFolderAndFile(folder, null, employee, zFileId);
                   }
                   zFileId = file.getId();
                   
                   /** if `STREAM_DELETE_FINISH`, then delete it. */
                   if (Configurations.isDeleteFinished()) {
                       File delFolder = new File(fileDirPath);
                       if(delFolder.exists() && delFolder.isDirectory()){
                           FileUtil.deleteFolder(delFolder);
                       }
                       //dst.delete();
                   }
                   
                   //文件上传成功，添加到待加密队列中进行加密
                   addFileToEncryptQueue(file);
               }catch (Exception e) {
                   message = "上传失败";
                   success = false;
                   //上传失败则删除文件
                   if(!StringUtil.nullOrBlank(filePath)){
                       FileUtil.deleteFile(filePath);
                   }
                   e.printStackTrace();
               }
           }
           try {
               if (success){
                   json.put(StreamConstant.START_FIELD, start);
                   json.put(StreamConstant.Z_FILE_ID, zFileId);
                   json.put(StreamConstant.TEAMWORK_MESSGE_ID, teamworkMessageId);
               }
               json.put(StreamConstant.SUCCESS, success);
               json.put(StreamConstant.MESSAGE, message);
           } catch (JSONException e) {}
           
           /*writer.write(json.toString());
           IoUtil.close(writer);*/
           this.write(json.toString(), resp);
       }
    }
    
    /**
     * 根据文件名、大小等信息获取Token的URI（用于生成断点续传令牌）
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping("/token")
    public void token(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        logger.info("文件上传获取token请求时间：" + DateUtil.getDateFormat(new Date(), DateUtil.YMD24H_DATA));
        String name = req.getParameter(FILE_NAME_FIELD);
        String size = req.getParameter(FILE_SIZE_FIELD);
        String lastModifiedDate = req.getParameter(FILE_LAST_MODIFIED_DATE);
        UploadUser uploadUser = this.getMyInfo(req);
        String operateId = uploadUser.getIdentity();
        
        String token = TokenUtil.generateToken(name, size, operateId, lastModifiedDate);
        
        PrintWriter writer = resp.getWriter();
        
        JSONObject json = new JSONObject();
        try {
            json.put(TOKEN_FIELD, token);
            if (Configurations.isCrossed())
                json.put(SERVER_FIELD, Configurations.getCrossServer());
            json.put(SUCCESS, true);
            json.put(MESSAGE, "");
        } catch (JSONException e) {
        }
        /** TODO: save the token. */
        
        writer.write(json.toString());
    }
    
    /**
     * 根据路径构建文件夹对象
     * @param folder 文件夹
     * @param path 文件夹路径
     */
    public void createFolder(Folder folder, com.yunip.model.disk.File file, String path){
        if(!StringUtil.nullOrBlank(path)){
            String folderName = "";
            if(path.indexOf("/") != -1){
                int begin = 0;
                int end = path.indexOf("/");
                folderName = path.substring(begin, end);
                path = path.substring(end + 1, path.length());
            }else{
                folderName = path;
                path = "";
            }
            if(folder.getFolderName() == null){
                folder.setFolderName(folderName);
                createFolder(folder, file, path);
            }else{
                Folder tempFolder = new Folder();
                tempFolder.setFolderName(folderName);
                List<Folder> folders = new ArrayList<Folder>();
                folders.add(tempFolder);
                folder.setFolders(folders);
                createFolder(tempFolder, file, path);
            }
        }else{
            List<com.yunip.model.disk.File> files = new ArrayList<com.yunip.model.disk.File>();
            files.add(file);
            folder.setFiles(files);
        }
    }
    
    /**
     * 根据路径构建文件夹对象
     * @param folder 文件夹
     * @param path 文件夹路径
     */
    public void createTeamworkFolder(TeamworkFolder folder, TeamworkFile file, String path){
        if(!StringUtil.nullOrBlank(path)){
            String folderName = "";
            if(path.indexOf("/") != -1){
                int begin = 0;
                int end = path.indexOf("/");
                folderName = path.substring(begin, end);
                path = path.substring(end + 1, path.length());
            }else{
                folderName = path;
                path = "";
            }
            if(folder.getFolderName() == null){
                folder.setFolderName(folderName);
                createTeamworkFolder(folder, file, path);
            }else{
                TeamworkFolder tempFolder = new TeamworkFolder();
                tempFolder.setFolderName(folderName);
                List<TeamworkFolder> folders = new ArrayList<TeamworkFolder>();
                folders.add(tempFolder);
                folder.setTeamworkFolders(folders);
                createTeamworkFolder(tempFolder, file, path);
            }
        }else{
            List<TeamworkFile> files = new ArrayList<TeamworkFile>();
            files.add(file);
            folder.setTeamworkFiles(files);
        }
    }
}