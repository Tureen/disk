/*
 * 描述：〈文件创建索引记录信息〉
 * 创建人：can.du
 * 创建时间：2016-8-8
 */
package com.yunip.service.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunip.constant.Constant;
import com.yunip.enums.disk.FileIndexOperType;
import com.yunip.mapper.disk.IFileIndexDao;
import com.yunip.model.disk.FileIndex;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.service.IFileIndexService;
import com.yunip.utils.lucene.IndexFile;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 文件创建索引记录信息
 */
@Service("iFileIndexService")
public class FileIndexServiceImpl implements IFileIndexService{
    
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "iFileIndexDao")
    private IFileIndexDao fileIndexDao;
    
    @Resource(name = "iFileEncryptDecryptService")
    private IFileEncryptDecryptService fileEncryptDecryptService;

    @Override
    public void createFileIndex() throws Exception {
        List<FileIndex> fileIndexs = fileIndexDao.selectByAll();
        if(fileIndexs != null && fileIndexs.size() > 0){
            for(FileIndex fileIndex : fileIndexs){
                //创建索引
                //索引目录
                String indexPath = Constant.ROOTPATH + File.separator + Constant.INDEX_DIR  + File.separator + fileIndex.getEmployeeId() + File.separator;
                //文件地址
                //String dirPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR  + File.separator + fileIndex.getFilePath();
                String dirPath = fileEncryptDecryptService.fileDecrypt(fileIndex.getFileId());
                //创建索引
                try {
                    if(fileIndex.getOperType().equals(FileIndexOperType.DEL.getType())){
                        IndexFile.deleteIndex(indexPath, fileIndex.getFileId().toString());;
                        //判断文件存在
                        logger.info(fileIndex.getEmployeeId() + "：删除索引文件 " + fileIndex.getFileName() + " 路径：" + fileIndex.getFilePath());
                    } else {
                        if(!StringUtil.nullOrBlank(dirPath)){//原文件存在时，创建索引
                            IndexFile.createIndex(dirPath, indexPath, fileIndex.getFileId().toString(), fileIndex.getFileName());
                            logger.info(fileIndex.getEmployeeId() + "：创建或者更新索引文件 " + fileIndex.getFileName() + " 路径：" + fileIndex.getFilePath());
                        }
                    }
                    //删除记录
                    fileIndexDao.delById(fileIndex.getId());
                    FileUtil.deleteFileTempFolder(dirPath);//删除缓存文件
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(fileIndex.getEmployeeId() + "：创建索引文件 " + fileIndex.getFileName() + " 失败");
                }
            }
        }
    }

}
