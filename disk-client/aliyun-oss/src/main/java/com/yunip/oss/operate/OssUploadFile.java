/*
 * 描述：阿里云OSS上传文件
 * 创建人：jian.xiong
 * 创建时间：2016-9-7
 */
package com.yunip.oss.operate;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.aliyun.oss.OSSClient;
import com.yunip.oss.config.HttpProtocol;
import com.yunip.utils.util.StringUtil;


/**
 * 阿里云OSS上传文件
 */
public class OssUploadFile {
    
    /**
     * 创建文件夹
     * @param client 
     * @param bucketName bucket名称
     * @param folderName 文件夹名称
     */
    public static void createFolder(OSSClient client, String bucketName, String folderName){
        client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]));
    }
    
    /**
     * 目录是否存在
     * @param client
     * @param bucketName
     * @param folderName  
     */
    public static boolean isExistsFolder(OSSClient client, String bucketName, String folderName){
        return client.doesObjectExist(bucketName, folderName + "/");
    }
    
    /**
     * 上传本地文件
     * @param endpoint 访问地址
     * @param accessKeyId 操作账户key ID
     * @param accessKeySecret 操作账户密钥
     * @param bucketName 操作帐号bucket名称
     * @param filePath 本地文件路径
     * @throws Exception 
     */
    public static void uploadLocalFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String filePath) throws Exception{
        if(StringUtil.nullOrBlank(filePath)){
            return;
        }
        File file = new File(filePath);
        if(!file.exists() || !file.isFile()){
            return;
        }
        //创建OSSClient实例
        OSSClient client = new OSSClient(HttpProtocol.HTTP.getProtocol() + endpoint, accessKeyId, accessKeySecret);
        try {
            //上传本地文件
            client.putObject(bucketName, file.getName(), file);
        }catch (Exception e) {
            throw e;
        }finally{
            // 关闭client
            client.shutdown();
        }
    }
    
    /**
     * 上传本地文件到指定目录下
     * @param endpoint 访问地址
     * @param accessKeyId 操作账户key ID
     * @param accessKeySecret 操作账户密钥
     * @param bucketName 操作帐号bucket名称
     * @param folderName 目录名(结构为:目录/  “/”是必须的，表示目录)
     * @param filePath 本地文件路径
     * @throws Exception 
     */
    public static void uploadLocalFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String folderName, String filePath) throws Exception{
        if(StringUtil.nullOrBlank(filePath)){
            return;
        }
        File file = new File(filePath);
        if(!file.exists() || !file.isFile()){
            return;
        }
        //判断目录名称不能为空且必须以“/”结尾
        if(StringUtil.nullOrBlank(folderName) || (!StringUtil.nullOrBlank(folderName) && !folderName.endsWith("/"))){
            return;
        }
        //创建OSSClient实例
        OSSClient client = new OSSClient(HttpProtocol.HTTP.getProtocol() + endpoint, accessKeyId, accessKeySecret);
        try {
            //上传本地文件
            client.putObject(bucketName, folderName + file.getName(), file);
        }catch (Exception e) {
            throw e;
        }finally{
            // 关闭client
            client.shutdown();
        }
    }
}
