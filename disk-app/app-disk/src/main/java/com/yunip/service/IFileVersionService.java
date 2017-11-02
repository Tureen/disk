/*
 * 描述：〈版本的service〉
 * 创建人：can.du
 * 创建时间：2016-5-11
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.query.FileVersionQuery;

/**
 * 版本的service
 */
public interface IFileVersionService {

    /**
     * 获取当前文件的历史版本列表
     * @param query   查询对象
     * @return  
     * List<FileVersion> 
     * @exception
     */
    List<FileVersion> getFileVersions(FileVersionQuery query);
    
    /****
     * 恢复文件系统中当前版本
     * @param  fileVersion 文件版本
     * void 
     * @exception
     */
    @Transactional
    void regainFile(FileVersion fileVersion);
}
