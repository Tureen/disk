/*
 * 描述：自定义文件加密解密工具类
 * 创建人：jian.xiong
 * 创建时间：2016-11-22
 */
package com.yunip.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 自定义文件加密解密工具类
 */
public class FileEncrypDecrypUtil {
	private static Logger logger = Logger.getLogger(FileEncrypDecrypUtil.class);
	
    /**
     * 读取文件字节数大小
     */
    private final static int readByte = 1024; 
    
    /**
     * 加密
     * @param soureceFilePath 待加密文件路径
     * @param offset 偏移量
     */
    public static void encrypt(String soureceFilePath, int offset){
        File sourceFile = new File(soureceFilePath);
        if(!sourceFile.exists()){
            return;
        }
        //临时文件目录
        String tempPath = sourceFile.getParent() + File.separator + UUID.randomUUID();
        File tempFile = new File(tempPath);
        try {
            FileInputStream is = new FileInputStream(sourceFile);
            FileOutputStream os = new FileOutputStream(tempFile);
            byte[] buffer1 = new byte[readByte];
            byte[] buffer2 = new byte[readByte];
            int len;
            while((len = is.read(buffer1)) != -1) {
                for(int i = 0;i < len; i++){
                    byte b = buffer1[i];
                    buffer2[i] = (byte) (b + offset);
                }
                os.write(buffer2, 0, len);
                os.flush();
            }
            os.close();
            is.close();
            sourceFile.delete();
            tempFile.renameTo(sourceFile);
        }catch (Exception e) {
        	logger.error("文件加密异常：" + e.getMessage());
        	e.printStackTrace();
        }
    }
    
    /**
     * 加密
     * @param soureceFilePath 待加密文件路径
     * @param encryptFilePath 加密后的文件路径
     * @param offset 偏移量
     * @return 返回是否加密成功（true:加密成功  false:加密失败）
     */
    public static boolean encrypt(String soureceFilePath, String encryptFilePath, int offset){
        File sourceFile = new File(soureceFilePath);
        if(!sourceFile.exists()){
            return false;
        }
        //临时文件目录
        File tempFile = new File(encryptFilePath);
        if(!tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdirs();
        }
        try {
            long t1 = System.currentTimeMillis();
            FileInputStream is = new FileInputStream(sourceFile);
            FileOutputStream os = new FileOutputStream(tempFile);
            byte[] buffer1 = new byte[readByte];
            byte[] buffer2 = new byte[readByte];
            int len;
            while((len = is.read(buffer1)) != -1) {
                for(int i = 0;i < len; i++){
                    byte b = buffer1[i];
                    buffer2[i] = (byte) (b + offset);
                }
                os.write(buffer2, 0, len);
                os.flush();
            }
            os.close();
            is.close();
            long t2 = System.currentTimeMillis();
            logger.info("文件加密耗时：" + (t2 - t1) + "毫秒");
            return true;
        }catch (Exception e) {
            logger.error("文件加密异常：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 解密
     * @param encryptFilePath 加密文件路径
     * @param decryptFilePath 解密文件路径
     * @param offset 偏移量
     * @return 返回解密后的文件路径
     */
    public static String decrypt(String encryptFilePath, String decryptFilePath, int offset){
    	File encryptFile = new File(encryptFilePath);
        if(!encryptFile.exists()){
            return null;
        }
        
        File decryptFile = new File(decryptFilePath);
        if(!decryptFile.getParentFile().exists()){
        	decryptFile.getParentFile().mkdirs();
        }
        
        try {
            long t1 = System.currentTimeMillis();
	        FileInputStream is = new FileInputStream(encryptFile);
	        FileOutputStream os = new FileOutputStream(decryptFile);
	        
	        byte[] buffer1 = new byte[readByte];
            byte[] buffer2 = new byte[readByte];
            int len;
            while((len = is.read(buffer1)) != -1) {
            	for(int i = 0;i < len; i++){
                    byte b = buffer1[i];
                    buffer2[i] =  (byte) (b - offset);
                }
            	os.write(buffer2, 0, len);
                os.flush();
            }
            os.close();
            is.close();
            long t2 = System.currentTimeMillis();
            logger.info("文件解密耗时：" + (t2 - t1) + "毫秒");
        }catch (Exception e) {
        	logger.error("文件加密异常：" + e.getMessage());
        	e.printStackTrace();
        	return null;
        }
    	return decryptFilePath;
    }
    
    /**
     * 解密
     * @param encryptFilePath 加密文件路径
     * @param offset 偏移量
     */
    public static void decrypt(String encryptFilePath, int offset, HttpServletResponse response){
        File encryptFile = new File(encryptFilePath);
        if(!encryptFile.exists()){
            return;
        }
        try {
            long t1 = System.currentTimeMillis();
            FileInputStream is = new FileInputStream(encryptFile);
            OutputStream os = response.getOutputStream();
            
            byte[] buffer1 = new byte[readByte];
            byte[] buffer2 = new byte[readByte];
            int len;
            while((len = is.read(buffer1)) != -1) {
                for(int i = 0;i < len; i++){
                    byte b = buffer1[i];
                    buffer2[i] =  (byte) (b - offset);
                }
                os.write(buffer2, 0, len);
                os.flush();
            }
            os.close();
            is.close();
            long t2 = System.currentTimeMillis();
            logger.info("文件解密耗时：" + (t2 - t1) + "毫秒");
        }catch (Exception e) {
            logger.error("文件加密异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	int offset = 10;
        System.out.println("===============================文件解密Start===============================");
        long t3 = System.currentTimeMillis();
        FileEncrypDecrypUtil.decrypt("C:\\test\\encry\\test-encryp.jpg", "C:\\test\\encry\\test-decryp.jpg", offset);
        long t4 = System.currentTimeMillis();
        System.out.println("加密耗时：" + (t4-t3) + "毫秒");
        System.out.println("===============================文件解密 End===============================");
        
    }
}
