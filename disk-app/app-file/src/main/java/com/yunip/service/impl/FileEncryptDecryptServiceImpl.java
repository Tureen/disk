/*
 * 描述：文件加密/解密服务接口实现
 * 创建人：jian.xiong
 * 创建时间：2016-11-23
 */
package com.yunip.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.disk.EncryptStatus;
import com.yunip.enums.fileservers.FileStreamType;
import com.yunip.enums.teamwork.FileType;
import com.yunip.manage.FileManager;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileDeleteDao;
import com.yunip.mapper.disk.IFileIndexDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.FileIndex;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.support.FileEncrypt;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.query.TeamworkFileQuery;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.utils.util.FileEncrypDecrypUtil;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.RandomUtil;
import com.yunip.utils.util.StringUtil;
import com.yunip.web.common.FileEncryptService;

/**
 * 文件加密/解密服务接口实现
 */
@Service("iFileEncryptDecryptService")
public class FileEncryptDecryptServiceImpl implements IFileEncryptDecryptService {
    public static Logger logger = Logger.getLogger(FileEncryptDecryptServiceImpl.class);
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @Resource(name = "iFileIndexDao")
    private IFileIndexDao fileIndexDao;
    
    @Resource(name = "iFileDeleteDao")
    private IFileDeleteDao fileDeleteDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao teamworkFileDao;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Override
    public void addFileToEncryptQueue(com.yunip.model.disk.File file) {
        if(file.getId() != null && fileManager.isEncrypt(file)){
            FileEncrypt fileEncrypt = new FileEncrypt();
            fileEncrypt.setFileId(file.getId());
            fileEncrypt.setType(FileType.IS_FILE.getType());
            SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.offer(fileEncrypt);
            //唤醒处理文件加密线程
            FileEncryptService.pleaseNotify();
        }
    }
    
    @Override
    public void addFileToEncryptQueue(TeamworkFile file){
        if(file.getId() != null && fileManager.isEncrypt(file)){
            FileEncrypt fileEncrypt = new FileEncrypt();
            fileEncrypt.setFileId(file.getId());
            fileEncrypt.setType(FileType.IS_TEAMWORK_FILE.getType());
            SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.offer(fileEncrypt);
            //唤醒处理文件加密线程
            FileEncryptService.pleaseNotify();
        }
    }

    @Override
    @Transactional
    public void fileEncrypt(Integer fileId, Integer type) {
        if(FileType.IS_FILE.getType() == type){
            fileEncrypt(fileId);
        }else if (FileType.IS_TEAMWORK_FILE.getType() == type){
            teamworkFileEncrypt(fileId);
        }
    }
    
    @Transactional
    public void fileEncrypt(Integer fileId) {
        com.yunip.model.disk.File sourceFile = fileDao.selectById(fileId);
        if(sourceFile != null && sourceFile.getEncryptStatus() == EncryptStatus.WAITFORENCRYPTION.getStatus()){
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                String tempFileName = UUID.randomUUID() + FileUtil.getFileNameExt(sourceFile.getFileName());
                String tempDirPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID();
                String tempPath = tempDirPath + File.separator + tempFileName;
                int offset = RandomUtil.getInt(1, 21);
                boolean encryptResult = FileEncrypDecrypUtil.encrypt(sourcePath, tempPath, offset);
                boolean thumbImageEncryptResult = true;//加密图片缩略图结果
                String thumbImagePath = "";//图片的缩略图路径
                String tempThumbImagePath = "";//加密后的缩略图临时路径
                if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase())){//上传文件的是图片，对其缩略图也进行加密
                    thumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + file.getName();
                    tempThumbImagePath = tempDirPath + File.separator + Constant.thumbImagePrefixName + tempFileName;
                    thumbImageEncryptResult = FileEncrypDecrypUtil.encrypt(thumbImagePath, tempThumbImagePath, offset);
                }
                if(encryptResult && thumbImageEncryptResult){
                    String newFilePath = file.getParentFile().getPath() + File.separator + tempFileName;
                    try {
                        FileUtil.copyFile(tempPath, newFilePath);
                        if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase())){//上传文件的是图片，对其缩略图也进行加密，将其复制到正式目录下
                            String newThumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + tempFileName;
                            FileUtil.copyFile(tempThumbImagePath, newThumbImagePath);
                        }
                        String filePath = file.getParentFile().getName() + File.separator + tempFileName;
                        com.yunip.model.disk.File newFile = new com.yunip.model.disk.File();
                        newFile.setId(fileId);
                        newFile.setFilePath(file.getParentFile().getName() + File.separator + tempFileName);
                        newFile.setEncryptStatus(EncryptStatus.ENCRYPTED.getStatus());
                        newFile.setEncryptKey(String.valueOf(offset));
                        fileDao.update(newFile);
                        
                        //修改文件索引表中的记录
                        FileIndex index = new FileIndex();
                        index.setFileId(fileId);
                        index.setFilePath(filePath);
                        fileIndexDao.updateFilePathByFileId(index);
                        
                        file.delete();//删除加密后的原未加密文件
                        if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase()) && !StringUtil.nullOrBlank(thumbImagePath)){//上传文件的是图片，对其缩略图也进行加密后删除原文件
                            FileUtil.deleteFile(thumbImagePath);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                        logger.error("加密文件异常：" + e.getMessage());
                    }
                }
                FileUtil.deleteFolder(new File(tempDirPath));//删除加密过程中产生的临时目录
            }
        }
    }
    
    @Transactional
    private void teamworkFileEncrypt(Integer fileId) {
        TeamworkFile sourceFile = teamworkFileDao.selectById(fileId);
        if(sourceFile != null && sourceFile.getEncryptStatus() == EncryptStatus.WAITFORENCRYPTION.getStatus()){
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                String tempFileName = UUID.randomUUID() + FileUtil.getFileNameExt(sourceFile.getFileName());
                String tempDirPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID();
                String tempPath = tempDirPath + File.separator + tempFileName;
                int offset = RandomUtil.getInt(1, 21);
                boolean encryptResult = FileEncrypDecrypUtil.encrypt(sourcePath, tempPath, offset);
                boolean thumbImageEncryptResult = true;//加密图片缩略图结果
                String thumbImagePath = "";//图片的缩略图路径
                String tempThumbImagePath = "";//加密后的缩略图临时路径
                if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase())){//上传文件的是图片，对其缩略图也进行加密
                    thumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + file.getName();
                    tempThumbImagePath = tempDirPath + File.separator + Constant.thumbImagePrefixName + tempFileName;
                    thumbImageEncryptResult = FileEncrypDecrypUtil.encrypt(thumbImagePath, tempThumbImagePath, offset);
                }
                if(encryptResult && thumbImageEncryptResult){
                    String newFilePath = file.getParentFile().getPath() + File.separator + tempFileName;
                    try {
                        FileUtil.copyFile(tempPath, newFilePath);
                        if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase())){//上传文件的是图片，对其缩略图也进行加密，将其复制到正式目录下
                            String newThumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + tempFileName;
                            FileUtil.copyFile(tempThumbImagePath, newThumbImagePath);
                        }
                        TeamworkFile newFile = new TeamworkFile();
                        newFile.setId(fileId);
                        newFile.setFilePath(file.getParentFile().getName() + File.separator + tempFileName);
                        newFile.setEncryptStatus(EncryptStatus.ENCRYPTED.getStatus());
                        newFile.setEncryptKey(String.valueOf(offset));
                        teamworkFileDao.update(newFile);
                        
                        file.delete();//删除加密后的原未加密文件
                        if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(sourcePath).toLowerCase()) && !StringUtil.nullOrBlank(thumbImagePath)){//上传文件的是图片，对其缩略图也进行加密后删除原文件
                            FileUtil.deleteFile(thumbImagePath);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                        logger.error("加密文件异常：" + e.getMessage());
                    }
                }
                FileUtil.deleteFolder(new File(tempDirPath));//删除加密过程中产生的临时目录
            }
        }
    }

    @Override
    public String fileDecrypt(Integer fileId) {
        com.yunip.model.disk.File sourceFile = fileDao.selectById(fileId);
        if(sourceFile != null){//对已加密的文件进行解密
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                if(sourceFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){
                    String encryptKey = sourceFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        String decryptFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID() + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(sourceFile.getFileName());
                        return FileEncrypDecrypUtil.decrypt(sourcePath, decryptFilePath, offset);
                    }
                }else{
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
                }
            }
        }
        return null;
    }
    
    @Override
    public String recycleFileDecrypt(Integer recycleFileId) {
        FileDelete fileDelete = fileDeleteDao.selectById(recycleFileId);
        if(fileDelete != null){//对已加密的文件进行解密
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + fileDelete.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                if(fileDelete.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){
                    String encryptKey = fileDelete.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        String decryptFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID() + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(fileDelete.getFileName());
                        return FileEncrypDecrypUtil.decrypt(sourcePath, decryptFilePath, offset);
                    }
                }else{
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + fileDelete.getFilePath();
                }
            }
        }
        return null;
    }
    
    @Override
    public String teamworkFileDecrypt(Integer teamworkFileId) {
        TeamworkFile teamworkFile = teamworkFileDao.selectById(teamworkFileId);
        if(teamworkFile != null){//对已加密的文件进行解密
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + teamworkFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                if(teamworkFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){
                    String encryptKey = teamworkFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        String decryptFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID() + File.separator + UUID.randomUUID() + FileUtil.getFileNameExt(teamworkFile.getFileName());
                        return FileEncrypDecrypUtil.decrypt(sourcePath, decryptFilePath, offset);
                    }
                }else{
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + teamworkFile.getFilePath();
                }
            }
        }
        return null;
    }

    @Override
    public void getFileStream(Integer fileId, FileStreamType type, HttpServletResponse response) {
        com.yunip.model.disk.File sourceFile = fileDao.selectById(fileId);
        if(sourceFile != null){
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(FileStreamType.THUMB_IMAGE.getType() == type.getType()){//判断读取的文件是否为缩略图
                String thumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + file.getName();
                File thumbImageFile = new File(thumbImagePath);
                if(thumbImageFile.exists() && thumbImageFile.isFile()){
                    file = thumbImageFile;
                    sourcePath = thumbImagePath;
                }else{
                    return;
                }
            }
            if(file.exists() && file.isFile()){
                response.setContentType("application/octet-stream");
                if(sourceFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){//加密文件需解密
                    String encryptKey = sourceFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        FileEncrypDecrypUtil.decrypt(sourcePath, offset, response);
                    }
                }else{//非加密文件
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
                    }catch (Exception e) {
                        logger.error("文件读取异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    @Override
    public void getTeamworkFileStream(Integer fileId, FileStreamType type, HttpServletResponse response) {
        TeamworkFile sourceFile = teamworkFileDao.selectById(fileId);
        if(sourceFile != null){
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(FileStreamType.THUMB_IMAGE.getType() == type.getType()){//判断读取的文件是否为缩略图
                String thumbImagePath = file.getParentFile().getPath() + File.separator + Constant.thumbImagePrefixName + file.getName();
                File thumbImageFile = new File(thumbImagePath);
                if(thumbImageFile.exists() && thumbImageFile.isFile()){
                    file = thumbImageFile;
                    sourcePath = thumbImagePath;
                }else{
                    return;
                }
            }
            if(file.exists() && file.isFile()){
                response.setContentType("application/octet-stream");
                if(sourceFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){//加密文件需解密
                    String encryptKey = sourceFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        FileEncrypDecrypUtil.decrypt(sourcePath, offset, response);
                    }
                }else{//非加密文件
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
                    }
                    catch (Exception e) {
                        logger.error("协作文件读取异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String getPreviewFile(com.yunip.model.disk.File sourceFile) {
        if(sourceFile != null){//对已加密的文件进行解密
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                if(sourceFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){
                    String encryptKey = sourceFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        String decryptFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID() + File.separator + file.getName();
                        return FileEncrypDecrypUtil.decrypt(sourcePath, decryptFilePath, offset);
                    }
                }else{
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
                }
            }
        }
        return null;
    }
    
    @Override
    public String getPreviewTeamworkFile(TeamworkFile sourceFile) {
        if(sourceFile != null){//对已加密的文件进行解密
            String sourcePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
            File file = new File(sourcePath);
            if(file.exists() && file.isFile()){
                if(sourceFile.getEncryptStatus() == EncryptStatus.ENCRYPTED.getStatus()){
                    String encryptKey = sourceFile.getEncryptKey();
                    if(!StringUtil.nullOrBlank(encryptKey)){//判断加密密钥
                        int offset = Integer.parseInt(encryptKey);
                        String decryptFilePath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + UUID.randomUUID() + File.separator + file.getName();
                        return FileEncrypDecrypUtil.decrypt(sourcePath, decryptFilePath, offset);
                    }
                }else{
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + sourceFile.getFilePath();
                }
            }
        }
        return null;
    }
    
    @Override
    public void addAllWaitForEncryptionFile(){
        //标记唤醒线程
        boolean b = false;
        FileQuery query = new FileQuery();
        query.setEncryptStatus(EncryptStatus.WAITFORENCRYPTION.getStatus());
        List<com.yunip.model.disk.File> files = fileDao.selectByQuery(query);
        if(files != null && files.size() > 0){
            for(com.yunip.model.disk.File file : files){
                FileEncrypt fileEncrypt = new FileEncrypt();
                fileEncrypt.setFileId(file.getId());
                fileEncrypt.setType(FileType.IS_FILE.getType());
                SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.offer(fileEncrypt);
            }
            b = true;
        }
        TeamworkFileQuery teamworkFileQuery = new TeamworkFileQuery();
        teamworkFileQuery.setEncryptStatus(EncryptStatus.WAITFORENCRYPTION.getStatus());
        List<TeamworkFile> teamworkFiles = teamworkFileDao.selectByQuery(teamworkFileQuery);
        if(teamworkFiles != null && teamworkFiles.size() > 0){
            for(TeamworkFile file : teamworkFiles){
                FileEncrypt fileEncrypt = new FileEncrypt();
                fileEncrypt.setFileId(file.getId());
                fileEncrypt.setType(FileType.IS_TEAMWORK_FILE.getType());
                SystemContant.WAIT_FOR_ENCRYPTION_QUEUE_POOL.offer(fileEncrypt);
            }
            b = true;
        }
        if(b){
            //唤醒处理文件加密线程
            FileEncryptService.pleaseNotify();
        }
    }
}
