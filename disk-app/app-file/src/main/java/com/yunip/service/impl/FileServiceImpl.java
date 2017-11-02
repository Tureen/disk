/*
 * 描述：〈文件处理类〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.Constant;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.manage.FileManager;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.service.IFileService;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.ImgCompress;
import com.yunip.utils.util.RarUtil;
import com.yunip.utils.util.ZipUtil;

/**
 * 文件处理类
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{
    
    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "iFileEncryptDecryptService")
    private IFileEncryptDecryptService fileEncryptDecryptService;
    
    /**
     * 将该目录下的所有文件添加到文件加密队列中
     * @param folderCode
     * @param employeeId  
     */
    private void addFolderAllFilesToEncryptQueue(String folderCode, Integer employeeId){
        List<com.yunip.model.disk.File> files = fileManager.getEmployeeFolderAllFilesByFolderCode(folderCode, employeeId);
        if(files != null && files.size() > 0){
            for(com.yunip.model.disk.File file : files){
                fileEncryptDecryptService.addFileToEncryptQueue(file);
            }
        }
    }
    
    @Override
    @Transactional
    public void decompressFile(com.yunip.model.disk.File file, Employee employee, String taskName) {
        JsonData<Object> jsonData = new JsonData<Object>();
        //查看当前文件是否符合解压条件(zip)
        try {
            int beginIndex = file.getFileName().lastIndexOf(".");
            String lastFileName = file.getFileName().substring(beginIndex);
            //进行解压
            //String decFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + file.getFilePath();
            String decFilePath = fileEncryptDecryptService.fileDecrypt(file.getId());
            String distFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID(); 
            String lastPath = distFilePath + File.separator +  FileUtil.getShortFileNameWithoutExt(file.getFileName());
            //解压之后文件夹的地址
            logger.info("employeeId:" + employee.getId() + " 解压文件:" + file.getFileName());
            //设置 解压信息
            if(lastFileName.toLowerCase().equals(ZipUtil.EXT)){
                ZipUtil.TASKNAME = taskName;
                ZipUtil.CANCELMAP = SystemContant.CANCELMAP;
                ZipUtil.decompress(decFilePath, lastPath);
            } else {
                //rar文件内部不能超过500个文件
                int number = RarUtil.getRarSize(decFilePath);
                if(number > SystemContant.RARNUMBER){
                    throw new MyException(FileServersException.RARNUMBERERROE);
                }
                RarUtil.TASKNAME = taskName;
                RarUtil.CANCELMAP = SystemContant.CANCELMAP;
                RarUtil.unRarFile(decFilePath, lastPath);
            }
            //解压完成 检查是否存在文件夹
            File fileDir = new File(lastPath);
            if(!fileDir.exists()){
                //不存在该文件夹(解压失败)
                jsonData.setCode(FileServersException.NOTSUPPORTONLINEDEP.getCode());
                jsonData.setCodeInfo(FileServersException.NOTSUPPORTONLINEDEP.getMsg());
            } else {
                Folder folder = new Folder();
                folder.setParentId(file.getFolderId());
                folder.setFolderName(FileUtil.getShortFileNameWithoutExt(file.getFileName()));
                folder.setEmployeeId(file.getEmployeeId());
                //获取文件树列表
                getFolderAndFile(fileDir, folder, employee);
                fileManager.decompressionFolders(folder, employee);
                //删除文件夹
                File tempDir = new File(distFilePath);
                //删除临时目录
                FileUtil.deleteFolder(tempDir);
                //加入到文件加密队列中
                addFolderAllFilesToEncryptQueue(folder.getFolderCode(), employee.getId());
                //删除原文件的目录
                FileUtil.deleteFileTempFolder(decFilePath);
            }
        } catch (Exception exception) {
            if(exception instanceof MyException){
                    MyException exce = (MyException) exception;
                    jsonData.setCode(exce.getErrorCode());
                    jsonData.setCodeInfo(exce.getMsg());
            } else {
                //移除取消任务的记录
                if(!exception.getMessage().equals(taskName)){
                    //表示为取消
                    exception.printStackTrace();
                    jsonData.setCode(FileServersException.DECOMPRESSFAIL.getCode());
                    jsonData.setCodeInfo(FileServersException.DECOMPRESSFAIL.getMsg());
                }else {
                    jsonData.setCode(FileServersException.CANCELFILEDEC.getCode());
                    jsonData.setCodeInfo(FileServersException.CANCELFILEDEC.getMsg());
                } 
            }
        }
        //加入已经完成的链表对象中  同时移除正在指向的任务列表中的任务
        SystemContant.CANCELMAP.remove(taskName);
        SystemContant.TASKLIST.remove(taskName);
        SystemContant.TASKMAP.put(taskName, jsonData);
    }
    
    //TODO 将static去掉有何影响
    private void getFolderAndFile(File dir, Folder folder, Employee employee) throws Exception{
        File[] files = dir.listFiles();
        List<com.yunip.model.disk.File> sonFiles = new ArrayList<com.yunip.model.disk.File>();
        List<Folder> subFolders = new ArrayList<Folder>(); 
        String rootPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR 
                + File.separator;
        for(File file : files){
            if(file.isDirectory()){
                //解压完成 检查是否存在文件夹
                Folder sonfolder = new Folder();
                sonfolder.setFolderName(file.getName());
                sonfolder.setEmployeeId(folder.getEmployeeId());
                getFolderAndFile(file, sonfolder, employee);
                subFolders.add(sonfolder);
            } else {
                com.yunip.model.disk.File sonFile = new com.yunip.model.disk.File();
                sonFile.setFileName(file.getName());
                sonFile.setFileSize(FileUtil.getFileSize(file.getPath()));
                sonFile.setEmployeeId(folder.getEmployeeId());
                sonFile.setCreateAdmin(employee.getEmployeeName());
                sonFile.setValidStatus(ValidStatus.NOMAL.getStatus());
                sonFiles.add(sonFile);
                //将文件移动到相关目录下去
                String randomStr =  ""+UUID.randomUUID();
                String realPath =  employee.getId() + File.separator + randomStr + FileUtil.getFileNameExt(file.getName());
                String filePath = rootPath + realPath;
                File newFile = new File(filePath);
                file.renameTo(newFile);
                sonFile.setFilePath(realPath);
                //如果文件类型属图片，生成缩略图
                if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sonFile.getFileName()).toLowerCase())){//上传文件的是图片，则生成一张缩略图
                    String thumbImagePath = rootPath+employee.getId() + File.separator + Constant.thumbImagePrefixName + randomStr + FileUtil.getFileNameExt(file.getName()) ;
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
            }
        }
        folder.setFiles(sonFiles);
        folder.setFolders(subFolders);
    }

}
