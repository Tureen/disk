/*
 * 描述：阿里云OSS下载文件
 * 创建人：jian.xiong
 * 创建时间：2016-9-19
 */
package com.yunip.oss.operate;

import java.io.File;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.yunip.oss.common.GetObjectProgressListener;
import com.yunip.oss.config.HttpProtocol;
import com.yunip.utils.util.StringUtil;

/**
 * 阿里云OSS下载文件
 */
public class OssDownloadFile {
    /**
     * 下载到本地文件
     * @param endpoint 访问地址
     * @param accessKeyId 操作账户key ID
     * @param accessKeySecret 操作账户密钥
     * @param bucketName 操作帐号bucket名称
     * @param fileName 下载的文件名
     * @param filePath 存放在本地文件路径
     * @throws Exception 
     */
    public static void uploadToLocalFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String fileName, String filePath) throws Exception{
        if(StringUtil.nullOrBlank(filePath)){
            return;
        }
        //创建OSSClient实例
        OSSClient client = new OSSClient(HttpProtocol.HTTP.getProtocol() + endpoint, accessKeyId, accessKeySecret);
        try {
            // 下载object到文件
            client.getObject(new GetObjectRequest(bucketName, fileName), new File(filePath));
        }catch (Exception e) {
            throw e;
        }finally{
            // 关闭client
            client.shutdown();
        }
    }
    
    /**
     * 下载到本地文件,带进度条
     * @param endpoint 访问地址
     * @param accessKeyId 操作账户key ID
     * @param accessKeySecret 操作账户密钥
     * @param bucketName 操作帐号bucket名称
     * @param fileName 下载的文件名
     * @param filePath 存放在本地文件路径
     * @throws Exception 
     */
    public static void uploadToLocalFileProgress(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String fileName, String filePath) throws Exception{
        if(StringUtil.nullOrBlank(filePath)){
            return;
        }
        //创建OSSClient实例
        OSSClient client = new OSSClient(HttpProtocol.HTTP.getProtocol() + endpoint, accessKeyId, accessKeySecret);
        try {
            // 下载object到文件
            client.getObject(new GetObjectRequest(bucketName, fileName).<GetObjectRequest>withProgressListener(new GetObjectProgressListener()), new File(filePath));
        }catch (Exception e) {
            throw e;
        }finally{
            // 关闭client
            client.shutdown();
        }
    }
}
