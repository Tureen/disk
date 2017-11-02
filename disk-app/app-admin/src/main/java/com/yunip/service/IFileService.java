/*
 * 描述：〈文件对象Service〉
 * 创建人：can.du
 * 创建时间：2016-6-24
 */
package com.yunip.service;

import com.yunip.model.disk.query.FileQuery;

/**
 * 文件对象Service
 */
public interface IFileService {


    /***
     * 根据条件查询上传文件的列表
     * @param query
     * @return  
     * FileQuery 
     * @exception
     */
    FileQuery getFilesByQuery(FileQuery query);
    
    /**
     * 查询文件总大小(包括版本文件)
     * @return  
     * String 
     * @exception
     */
    String getFileSize();
}
