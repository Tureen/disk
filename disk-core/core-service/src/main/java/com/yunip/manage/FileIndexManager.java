package com.yunip.manage;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.enums.disk.FileType;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileIndexDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileIndex;

/**
 * 文件索引
 */
@Component("fileIndexManager")
public class FileIndexManager {

    @Resource(name = "iFileIndexDao")
    private IFileIndexDao fileIndexDao;

    @Resource(name = "iFileDao")
    private IFileDao      fileDao;

    /**
     * 插入文件索引表
     * @param file
     * @param fileIndexOperType  
     * void 
     * @exception
     */
    public void insertFileIndex(File file, Integer fileIndexOperType) {
        //判断文件类型
        if (FileType.TXT.getType() == file.getFileType()
                || FileType.WORD.getType() == file.getFileType()
                || FileType.EXCEL.getType() == file.getFileType()
                || FileType.PPT.getType() == file.getFileType()
                || FileType.WPSWZ.getType() == file.getFileType()
                || FileType.WPSYS.getType() == file.getFileType()
                || FileType.WPSBG.getType() == file.getFileType()
                || FileType.PDF.getType() == file.getFileType()) {
            FileIndex fileIndex = new FileIndex();
            fileIndex.setFileId(file.getId());
            fileIndex.setFileName(file.getFileName());
            fileIndex.setEmployeeId(file.getEmployeeId());
            fileIndex.setFilePath(file.getFilePath());
            fileIndex.setFileType(file.getFileType());
            fileIndex.setOperType(fileIndexOperType);
            fileIndexDao.insert(fileIndex);
        }
    }

    /**
     * 插入文件索引表
     * @param fileId
     * @param fileIndexOperType  
     * void 
     * @exception
     */
    public void insertFileIndex(Integer fileId, Integer fileIndexOperType) {
        File file = fileDao.selectById(fileId);
        if (file != null
                && (FileType.TXT.getType() == file.getFileType()
                        || FileType.WORD.getType() == file.getFileType()
                        || FileType.EXCEL.getType() == file.getFileType()
                        || FileType.PPT.getType() == file.getFileType()
                        || FileType.WPSWZ.getType() == file.getFileType()
                        || FileType.WPSYS.getType() == file.getFileType()
                        || FileType.WPSBG.getType() == file.getFileType()
                        || FileType.PDF.getType() == file.getFileType())) {
            FileIndex fileIndex = new FileIndex();
            fileIndex.setFileId(fileId);
            fileIndex.setFileName(file.getFileName());
            fileIndex.setEmployeeId(file.getEmployeeId());
            fileIndex.setFilePath(file.getFilePath());
            fileIndex.setFileType(file.getFileType());
            fileIndex.setOperType(fileIndexOperType);
            fileIndexDao.insert(fileIndex);
        }
    }
}
