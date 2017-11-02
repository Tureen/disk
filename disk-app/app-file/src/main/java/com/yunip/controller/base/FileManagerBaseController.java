/*
 * 描述：文件管理基础控制器类
 * 创建人：jian.xiong
 * 创建时间：2016-7-6
 */
package com.yunip.controller.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunip.constant.Constant;
import com.yunip.constant.MyException;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.enums.fileservers.FileServersOnlineEditValidateType;
import com.yunip.enums.fileservers.FileServersType;
import com.yunip.enums.fileservers.FileStreamType;
import com.yunip.manage.AuthorityShareManager;
import com.yunip.manage.FileManager;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.user.AdminRole;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.ImageUtils;
import com.yunip.utils.util.StringUtil;
import com.yunip.utils.util.ZipUtil;
import com.yunip.web.common.OfficeConverter;

/**
 * 文件管理基础控制器类
 */
public class FileManagerBaseController extends BaseController{
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "authorityShareManager")
    private AuthorityShareManager authorityShareManager;
    
    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    @Resource(name = "iFileEncryptDecryptService")
    private IFileEncryptDecryptService fileEncryptDecryptService;
    
    /**
     * 根据员工ID查询该员工所有拥有的角色
     * @param employeeId 员工ID
     */
    public List<Integer> getEmployeeRoles(String employeeId){
        Integer eId = Integer.parseInt(employeeId);
        List<Integer> employeeRoles = new ArrayList<Integer>();
        List<AdminRole> roles = adminService.getAdminRole(eId);
        if(roles != null){
            for(AdminRole role : roles){
                employeeRoles.add(role.getRoleId());
            }
        }
        return employeeRoles;
    }
    
    /**
     * 预览前置验证
     * @param fileId 文件ID
     * @param employeeId 员工ID
     * @return 返回预览文件的路径
     */
    public String previewValidate(String fileId, String employeeId){
        String previewFilePath = "";
        if(StringUtil.nullOrBlank(fileId) || StringUtil.nullOrBlank(employeeId)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        
        //查询当前登录所属角色
        List<Integer> roles = getEmployeeRoles(employeeId);
        List<String> privewFileList = new ArrayList<String>();
        privewFileList.add(fileId);
        //判断是否具备预览权限
        if(!authorityShareManager.getOpenAuthById(privewFileList, null, Integer.parseInt(employeeId), roles, FileServersType.PREVIEWFILE, null)){
            throw new MyException(FileServersException.NOTAUTHORITY);
        }
        
        List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileId);
        if(list != null && list.size() == 1){
            com.yunip.model.disk.File downFile = list.get(0);
            previewFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + downFile.getFilePath();
        }
        if(StringUtil.nullOrBlank(previewFilePath)){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        
        //验证预览格式是否支持
        if(!Constant.previewFileType.contains(FileUtil.getFileNameExt(previewFilePath).toLowerCase())){
            throw new MyException(FileServersException.NOTSUPPORTFILEPRIVIEW);
        }
        
        //验证预览文件是否存在
        File file = new File(previewFilePath);
        if(!file.exists() || !file.isFile()){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        return previewFilePath;
    }
    
    /**
     * 协作文件预览前置验证
     * @param fileId 协作文件ID
     * @param employeeId 员工ID
     * @return 返回预览文件的路径
     */
    public String previewValidateTeamwork(String fileId, String employeeId){
        String previewFilePath = "";
        if(StringUtil.nullOrBlank(fileId) || StringUtil.nullOrBlank(employeeId)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        //判断是否具备预览权限
        List<String> privewFileList = new ArrayList<String>();
        privewFileList.add(fileId);
        if(!authorityShareManager.getTeamworkOpenAuthById(privewFileList, null, Integer.parseInt(employeeId), null)){
            throw new MyException(FileServersException.NOTAUTHORITY);
        }
        
        List<TeamworkFile> list = fileManager.getTeamworkFileByIds(fileId);
        if(list != null && list.size() == 1){
            TeamworkFile downFile = list.get(0);
            previewFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + downFile.getFilePath();
        }
        if(StringUtil.nullOrBlank(previewFilePath)){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        
        //验证预览格式是否支持
        if(!Constant.previewFileType.contains(FileUtil.getFileNameExt(previewFilePath).toLowerCase())){
            throw new MyException(FileServersException.NOTSUPPORTFILEPRIVIEW);
        }
        
        //验证预览文件是否存在
        File file = new File(previewFilePath);
        if(!file.exists() || !file.isFile()){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        return previewFilePath;
    }
    
    /**
     * 在线编辑前置验证
     * @param fileId 文件ID
     * @param employeeId 员工ID
     * @param type 类型(0:全部，1：onlineEditFileType offices文档类型，2：onlineEditTxtFileType 文本文档类型)
     * @return 返回编辑文件的路径
     */
    public String onlineEditValidate(String fileId, String employeeId, FileServersOnlineEditValidateType type){
        String onlineEditFilePath = "";
        if(StringUtil.nullOrBlank(fileId) || StringUtil.nullOrBlank(employeeId)){
            throw new MyException(FileServersException.PARAMERROR);
        }
        
        //查询当前登录所属角色
        List<Integer> roles = getEmployeeRoles(employeeId);
        List<String> privewFileList = new ArrayList<String>();
        privewFileList.add(fileId);
        //判断是否具备在线编辑权限
        if(!authorityShareManager.getOpenAuthById(privewFileList, null, Integer.parseInt(employeeId), roles, FileServersType.ONLINEEDIT, null)){
            throw new MyException(FileServersException.NOTAUTHORITY);
        }
        
        List<com.yunip.model.disk.File> list = fileManager.getFileByIds(fileId);
        if(list != null && list.size() == 1){
            com.yunip.model.disk.File tempFile = list.get(0);
            onlineEditFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + tempFile.getFilePath();
        }
        
        if(StringUtil.nullOrBlank(onlineEditFilePath)){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        
        //验证格式是否支持在线编辑
        if(type == FileServersOnlineEditValidateType.ALL){
            List<String> allowOnlineEditFileType = new ArrayList<String>();
            allowOnlineEditFileType.addAll(Constant.onlineEditFileType);
            allowOnlineEditFileType.addAll(Constant.onlineEditTxtFileType);
            if(!allowOnlineEditFileType.contains(FileUtil.getFileNameExt(onlineEditFilePath).toLowerCase())){
                throw new MyException(FileServersException.NOTSUPPORTONLINEEDIT);
            }
        }else if(type == FileServersOnlineEditValidateType.OFFICES){
            if(!Constant.onlineEditFileType.contains(FileUtil.getFileNameExt(onlineEditFilePath).toLowerCase())){
                throw new MyException(FileServersException.NOTSUPPORTONLINEEDIT);
            }
        }else if(type == FileServersOnlineEditValidateType.TXT){
            if(!Constant.onlineEditTxtFileType.contains(FileUtil.getFileNameExt(onlineEditFilePath).toLowerCase())){
                throw new MyException(FileServersException.NOTSUPPORTONLINEEDIT);
            }
        }else {
            throw new MyException(FileServersException.NOTSUPPORTONLINEEDIT);
        }
        
        //验证在线编辑文件是否存在
        File file = new File(onlineEditFilePath);
        if(!file.exists() || !file.isFile()){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        return onlineEditFilePath;
    }
    
    /**
     * 单个文件下载
     * @param downFilePath
     * @param isDelete 下载完成后是否删除原文件
     */
    public void singleFileDownload(String downFilePath, String downfileName, boolean isDelete, HttpServletRequest request, HttpServletResponse response){
//        response.setContentType("application/octet-stream");
        response.setContentType("text/html");//谷歌浏览器用“application/octet-stream”有警告，需换用text/html
        File downFile = new File(downFilePath);
        if(!downFile.exists() || !downFile.isFile()){
            return;
        }
        response.setHeader("Content-Length", downFile.length()+"");
        try {
            InputStream fis = new FileInputStream(downFile);
            setFileDownloadHeader(request, response, downfileName);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while((length = fis.read(b)) != -1){
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            log.error("下载失败了......" + e.getMessage());
            e.printStackTrace();
        } finally{
            //若下载后删除文件
            if(isDelete){
                deleteFileTempFolder(downFilePath);
            }
        }
    }
    
    /**
     * 设置让浏览器弹出下载对话框的Header.
     * 根据浏览器的不同设置不同的编码格式  防止中文乱码
     * @param fileName 下载后的文件名.
     */
    public void setFileDownloadHeader(HttpServletRequest request,HttpServletResponse response, String fileName) {
        try {
            String encodedfileName = null;
            String agent = request.getHeader("USER-AGENT");
            if(null != agent && -1 != agent.indexOf("MSIE")){//IE
                encodedfileName = URLEncoder.encode(fileName,"UTF-8");
            }else if(null != agent && -1 != agent.indexOf("Mozilla")){
                encodedfileName = new String (fileName.getBytes("UTF-8"),"iso-8859-1");
            }else{
                encodedfileName = URLEncoder.encode(fileName,"UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 读取文件
     * @param response
     * @param filePath 文件路径
     */
    public void ioReaderFile(HttpServletResponse response, String filePath){
        response.setContentType("application/octet-stream");
        File file = new File(filePath);
        if(!file.exists()){
            return;
        }
        try {
            InputStream fis = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while((length = fis.read(b)) != -1){
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            log.error("文件读取失败了......" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 生成预览图片
     * @param sourceImagePath 需要生存预览图的图片地址
     */
    public String generatePreviewImage(String sourceImagePath){
        if(!Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourceImagePath).toLowerCase())){//允许生成预览图的文件格式
            return sourceImagePath;
        }
        String shortFileName = FileUtil.getShortFileName(sourceImagePath);
        String previewPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + Constant.previewImagePrefixName + shortFileName;
        File previewFile = new File(previewPath);
        if(!previewFile.exists()){
            try {
                ImageUtils.autoadaptationThumbImage(sourceImagePath, previewPath, Constant.imagePreviewMinCompressSize, Constant.imagePreviewPixelRadix, Constant.previewImagePrefixName);
                return previewPath;
            }catch (Exception e) {
                e.printStackTrace();
                log.error("生成预览图片失败：" + e.getMessage());
                return sourceImagePath;
            }
        }else{
            return previewPath;
        }
    }
    
    /**
     * 创建压缩文件
     * @param compressDir 需要压缩的目录
     * @param zipFilePath 生成的压缩文件路径(目录+文件名)  
     * @throws Exception 
     */
    public void createCompressFile(String compressDir, String zipFilePath) throws Exception{
        File compressDirFile = new File(compressDir);
        if(compressDirFile.isDirectory() && compressDirFile.exists()){
            try {
                FileOutputStream fous = new FileOutputStream(new File(zipFilePath));
                ZipOutputStream zipOut = new ZipOutputStream(fous);
                ZipUtil.compressDir(compressDirFile, zipOut, "");
                zipOut.close();
            }
            catch (Exception e) {
                log.error("压缩目录错误：" + e.getMessage());
                throw e;
            }
        }
    }
    
    /**
     * 获取文件路径
     * @param fileId 文件ID
     * @return 返回文件路径
     */
    public String getFile(Integer fileId){
        if(fileId == null){
            return null;
        }
        String path = "";
        try {
            path = fileEncryptDecryptService.fileDecrypt(fileId);
        } catch (Exception e) {
            log.error("读取文件失败：" + e.getMessage());
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 获取回收站文件路径
     * @param recycleFileId 回收站文件ID
     * @return 返回文件路径
     */
    public String getRecycleFile(Integer recycleFileId){
        if(recycleFileId == null){
            return null;
        }
        String path = "";
        try {
            path = fileEncryptDecryptService.recycleFileDecrypt(recycleFileId);
        } catch (Exception e) {
            log.error("读取文件失败：" + e.getMessage());
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 获取回收站文件路径
     * @param teamworkFileId 协作文件ID
     * @return 返回文件路径
     */
    public String getTeamworkFile(Integer teamworkFileId){
        if(teamworkFileId == null){
            return null;
        }
        String path = "";
        try {
            path = fileEncryptDecryptService.teamworkFileDecrypt(teamworkFileId);
        } catch (Exception e) {
            log.error("读取文件失败：" + e.getMessage());
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 获取预览文件路径
     * @param fileId 文件ID
     * @param request
     * @return 返回文件路径
     */
    public Map<String, String> getPreviewFile(String fileId, HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        com.yunip.model.disk.File file = fileManager.getFileByFileId(fileId);
        if(file == null){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        boolean isUsePdfPreview = getBrowerIsSupportH5(request);
        
        String previewPDFTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(file.getFilePath()) + Constant.PDF_SUFFIX;
        String previewSWFTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(file.getFilePath()) + Constant.SWF_SUFFIX;
        File pdfTempFile = new File(previewPDFTempPath);
        File swfTempFile = new File(previewSWFTempPath);
        if(!pdfTempFile.exists() || !pdfTempFile.isFile() || !swfTempFile.exists() || !swfTempFile.isFile()){
            String previewFilePath = fileEncryptDecryptService.getPreviewFile(file);
            try {
                OfficeConverter officeConverter = new OfficeConverter(previewFilePath);
                if(officeConverter.converter()){//转换成功
                    File previewFile;
                    if(isUsePdfPreview){//支持可直接预览pdf格式
                        previewFile = officeConverter.getPdfFile();
                    }else{//不支持的使用flexPage进行预览,预览文件为.swf格式
                        previewFile = officeConverter.getSwfFile();
                    }
                    if(previewFile == null || !previewFile.exists()){
                        throw new MyException(FileServersException.FILENOEXISTS);
                    }
                }
                deleteFileTempFolder(previewFilePath);//删除临时文件
            }catch (Exception e) {
                throw new MyException(FileServersException.PRIVIEWERROR);
            }
        }
        String page = "upload/preview";
        if(isUsePdfPreview){//支持可直接预览pdf格式
            page = "upload/preview";
        }else{//不支持的使用flexPage进行预览,预览文件为.swf格式
            page = "upload/flexPagePreview";
        }
        result.put("page", page);
        result.put("fileName", file.getFileName());
        return result;
    }
    
    /**
     * 获取预览文件路径
     * @param fileId 协作文件ID
     * @param request
     * @return 返回文件路径
     */
    public Map<String, String> getPreviewTeamworkFile(String fileId, HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        TeamworkFile file = fileManager.getTeakworkFileByFileId(fileId);
        if(file == null){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        boolean isUsePdfPreview = getBrowerIsSupportH5(request);
        
        String previewPDFTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(file.getFilePath()) + Constant.PDF_SUFFIX;
        String previewSWFTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + FileUtil.getShortFileNameWithoutExt(file.getFilePath()) + Constant.SWF_SUFFIX;
        File pdfTempFile = new File(previewPDFTempPath);
        File swfTempFile = new File(previewSWFTempPath);
        if(!pdfTempFile.exists() || !pdfTempFile.isFile() || !swfTempFile.exists() || !swfTempFile.isFile()){
            String previewFilePath = fileEncryptDecryptService.getPreviewTeamworkFile(file);
            try {
                OfficeConverter officeConverter = new OfficeConverter(previewFilePath);
                if(officeConverter.converter()){//转换成功
                    File previewFile;
                    if(isUsePdfPreview){//支持可直接预览pdf格式
                        previewFile = officeConverter.getPdfFile();
                    }else{//不支持的使用flexPage进行预览,预览文件为.swf格式
                        previewFile = officeConverter.getSwfFile();
                    }
                    if(previewFile == null || !previewFile.exists()){
                        throw new MyException(FileServersException.FILENOEXISTS);
                    }
                }
                deleteFileTempFolder(previewFilePath);//删除临时文件
            }catch (Exception e) {
                throw new MyException(FileServersException.PRIVIEWERROR);
            }
        }
        String page = "upload/preview";
        if(isUsePdfPreview){//支持可直接预览pdf格式
            page = "upload/preview";
        }else{//不支持的使用flexPage进行预览,预览文件为.swf格式
            page = "upload/flexPagePreview";
        }
        result.put("page", page);
        result.put("fileName", file.getFileName());
        return result;
    }
    
    /**
     * 预览图片（对图片进行压缩减少网络传输）
     * @param fileId
     */
    public String getPreviewImage(String fileId){
        com.yunip.model.disk.File file = fileManager.getFileByFileId(fileId);
        if(file == null){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        String shortFileName = FileUtil.getShortFileName(file.getFilePath());
        String previewImageTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + Constant.previewImagePrefixName + shortFileName;
        File previewImageTempFile = new File(previewImageTempPath);
        if(!previewImageTempFile.exists() || !previewImageTempFile.isFile()){
            String previewFilePath = fileEncryptDecryptService.getPreviewFile(file);
            String previewPath = generatePreviewImage(previewFilePath);
            deleteFileTempFolder(previewFilePath);//删除临时文件
            return previewPath;
        }else{
            return previewImageTempPath;
        }
    }
    
    /**
     * 预览协作图片（对图片进行压缩减少网络传输）
     * @param fileId
     */
    public String getTeamworkPreviewImage(String fileId){
        TeamworkFile file = fileManager.getTeakworkFileByFileId(fileId);
        if(file == null){
            throw new MyException(FileServersException.FILENOEXISTS);
        }
        String shortFileName = FileUtil.getShortFileName(file.getFilePath());
        String previewImageTempPath = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR + File.separator + Constant.previewImagePrefixName + shortFileName;
        File previewImageTempFile = new File(previewImageTempPath);
        if(!previewImageTempFile.exists() || !previewImageTempFile.isFile()){
            String previewFilePath = fileEncryptDecryptService.getPreviewTeamworkFile(file);
            String previewPath = generatePreviewImage(previewFilePath);
            deleteFileTempFolder(previewFilePath);//删除临时文件
            return previewPath;
        }else{
            return previewImageTempPath;
        }
    }
    
    /**
     * 读取文件内容，返回文本内容
     * @param path 文件路径
     * @param fileId 文件ID
     * @return 返回文件的文本内容
     */
    public String readFileTextContent(Integer fileId){
        String context = "", tempPath = "";
        try {
            tempPath = fileEncryptDecryptService.fileDecrypt(fileId);
            if(!StringUtil.nullOrBlank(tempPath)){
                context = FileUtil.readTxt(tempPath);
                deleteFileTempFolder(tempPath);//读取完成后，删除临时文件
            }
        } catch (Exception e) {
            log.error("读取文件文本内容失败：" + e.getMessage());
            e.printStackTrace();
        }
        return context;
    }
    
    /**
     * 读取协作文件内容，返回文本内容
     * @param path 文件路径
     * @param fileId 协作文件ID
     * @return 返回文件的文本内容
     */
    public String readTeamworkFileTextContent(Integer fileId){
        String context = "", tempPath = "";
        try {
            tempPath = fileEncryptDecryptService.teamworkFileDecrypt(fileId);
            if(!StringUtil.nullOrBlank(tempPath)){
                context = FileUtil.readTxt(tempPath);
                deleteFileTempFolder(tempPath);//读取完成后，删除临时文件
            }
        } catch (Exception e) {
            log.error("读取文件文本内容失败：" + e.getMessage());
            e.printStackTrace();
        }
        return context;
    }
    
    /** 
     * 根据文件ID读取文件内容并以流的形式进行返回
     * @param fileId 文件ID
     */
    public void readerFileStream(Integer fileId, HttpServletResponse response){
        fileEncryptDecryptService.getFileStream(fileId, FileStreamType.ALL, response);
    }
    
    /** 
     * 根据文件ID读取文件缩略图并以流的形式进行返回
     * @param fileId 文件ID
     */
    public void readerThumbImagerStream(Integer fileId, HttpServletResponse response){
        fileEncryptDecryptService.getFileStream(fileId, FileStreamType.THUMB_IMAGE, response);
    }
    
    /**
     * 根据文件ID读取文件内容并以流的形式进行返回
     * @param fileId
     * @param response  
     */
    public void readerTeamworkFileThumbImagerStream(Integer fileId, HttpServletResponse response){
        fileEncryptDecryptService.getTeamworkFileStream(fileId, FileStreamType.THUMB_IMAGE, response);
    }
    
    /**
     * 根据文件ID读取文件内容并以流的形式进行返回
     * @param fileId
     * @param response  
     */
    public void readerTeamworkFileStream(Integer fileId, HttpServletResponse response){
        fileEncryptDecryptService.getTeamworkFileStream(fileId, FileStreamType.ALL, response);
    }
    
    /**
     * 根据文件路径，若该文件属于缓存目录下的进行删除
     */
    public void deleteFileTempFolder(String filePath){
        FileUtil.deleteFileTempFolder(filePath);
    }
    
    /**
     * 添加文件到加密队列中进行加密
     * @param file 文件
     */
    public void addFileToEncryptQueue(com.yunip.model.disk.File file){
        fileEncryptDecryptService.addFileToEncryptQueue(file);
    }
    
    /**
     * 添加协作文件到加密队列中进行加密
     * @param file  
     * void 
     * @exception
     */
    public void addFileToEncryptQueue(TeamworkFile file){
        fileEncryptDecryptService.addFileToEncryptQueue(file);
    }
}
