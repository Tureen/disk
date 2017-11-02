/*
 * 描述：文件加密/解密服务接口
 * 创建人：jian.xiong
 * 创建时间：2016-11-23
 */
package com.yunip.service;

import javax.servlet.http.HttpServletResponse;

import com.yunip.enums.fileservers.FileStreamType;
import com.yunip.model.disk.File;
import com.yunip.model.teamwork.TeamworkFile;

/**
 * 文件加密/解密服务接口
 */
public interface IFileEncryptDecryptService {
    /**
     * 添加文件ID到待加密文件队列之中
     * @param file 文件
     */
    public void addFileToEncryptQueue(File file);
    
    /**
     * 添加协作文件ID到待加密文件队列之中
     * @param file  
     * void 
     * @exception
     */
    public void addFileToEncryptQueue(TeamworkFile file);
    
    /**
     * 文件加密
     * @param fileId 文件ID
     */
    public void fileEncrypt(Integer fileId, Integer type);
    
    /**
     * 文件解密
     * @param fileId 文件ID
     * @return 返回加密的文件所在路径
     */
    public String fileDecrypt(Integer fileId);
    
    /**
     * 回收文件解密
     * @param recycleFileId 文件回收站ID
     * @return 返回加密的文件所在路径
     */
    public String recycleFileDecrypt(Integer recycleFileId);
    
    /**
     * 文件解密
     * @param teamworkFileId 协作文件ID
     * @return 返回加密的文件所在路径
     */
    public String teamworkFileDecrypt(Integer teamworkFileId);
    
    /**
     * 获取文件流（文件内容写入http响应流）
     * @param fileId 文件ID
     * @param response http响应
     */
    public void getFileStream(Integer fileId, FileStreamType type, HttpServletResponse response);
    
    /**
     * 获取协作文件流
     * @param fileId
     * @param type
     * @param response  
     * void 
     * @exception
     */
    public void getTeamworkFileStream(Integer fileId, FileStreamType type, HttpServletResponse response);
    
    /**
     * 获得预览文件
     * @param fileId 文件ID
     */
    public String getPreviewFile(File file);
    
    /**
     * 获得预览协作文件
     * @param fileId 文件ID
     */
    public String getPreviewTeamworkFile(TeamworkFile file);
    
    /**
     * 添加所有的待加密文件至加密队列中
     */
    public void addAllWaitForEncryptionFile();
}
