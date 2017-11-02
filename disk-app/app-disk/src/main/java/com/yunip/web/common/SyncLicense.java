/*
 * 描述：〈授权码同步线程〉
 * 创建人：Administrator
 * 创建时间：2016-7-5
 */
package com.yunip.web.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import com.yunip.constant.SystemContant;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.SystemUtil;

/**
 * 授权码同步线程
 */
public class SyncLicense extends Thread{
    
    private ServletContext servletContext;
    
    @Override
    public void run() {
        try {
            SyncLicenseCode(servletContext);
        }  catch (Exception e) {
            License.hour = 0;
            e.printStackTrace();
        }
    }
    
    /****
     * 同步数据
     * @param request
     * @throws Exception  
     * void 
     * @exception
     */
    public static void SyncLicenseCode(ServletContext servletContext) throws Exception {
        while(true){
            Thread.sleep(1000 * 60 * 60);
            minusLicenseHour(servletContext);
        }
    }
    
    /*****
     * 每一小时执行一次
     * @param request
     * @param filePath
     * @throws Exception  
     * void 
     * @exception
     */
    public static void minusLicenseHour(ServletContext servletContext) throws Exception{
        String filePath = servletContext.getRealPath("/") + File.separator + SystemContant.LICENSEFILENAME;
        Des des = new Des();
        String linceseCode = "";
        //读取文件内容
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String str;
        while(null != (str=br.readLine())){
            linceseCode += str;
        }
        linceseCode = des.strDec(linceseCode, SystemContant.LICENSECODEKEY2, null, null);
        br.close();
        String[] linceseCodes = linceseCode.split("\\|");
        int index = linceseCode.lastIndexOf("|");
        String syLicense = linceseCode;
        if(linceseCodes.length >= 2){
            syLicense = linceseCode.substring(0,index+1);
        }
        linceseCode = linceseCodes[linceseCodes.length - 1];
        //再一次进行解密
        String sySignHours = des.strDec(linceseCode.toString(), Md5.encodeByMd5(SystemUtil.getConfig()), null, null);
        if(StringUtils.isNotBlank(sySignHours) && StringUtils.isNumeric(sySignHours)){
            String randomNumber = sySignHours.substring(0,8);
            sySignHours = sySignHours.substring(8);
            //设置 -1小时
            int syHour = Integer.parseInt(sySignHours) - 1;
            License.hour = syHour;
            //新的字符串
            String licenseCode = randomNumber + syHour;
            sySignHours = des.strEnc(licenseCode, Md5.encodeByMd5(SystemUtil.getConfig()), null, null);
            //获取生成路径
            File file = new File(filePath);
            //合法通过的情况下，替换掉以前的授权码使用新授权时间计算
            syLicense += sySignHours;
            //再次机密
            String desc = des.strEnc(syLicense, SystemContant.LICENSECODEKEY2, null, null);
            FileWriter fileWritter = new FileWriter(file);
            fileWritter.write(desc);
            fileWritter.close();
        }
    }
    
    /*****
     * 启动初始化值
     * @param request
     * @param filePath
     * @throws Exception  
     * void 
     * @exception
     */
    public static void initLicenseHour(ServletContext servletContext){
        try {
            String filePath = servletContext.getRealPath("/") + File.separator + SystemContant.LICENSEFILENAME;
            Des des = new Des();
            String linceseCode = "";
            //读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String str;
            while(null != (str=br.readLine())){
                linceseCode += str;
            }
            linceseCode = des.strDec(linceseCode, SystemContant.LICENSECODEKEY2, null, null);
            br.close();
            //再一次进行解密
            String[] linceseCodes = linceseCode.split("\\|");
            linceseCode = linceseCodes[linceseCodes.length - 1];
            if(!linceseCode.equals(SystemContant.LICENSECODEKEY1)){
                String sySignHours = des.strDec(linceseCode.toString(), Md5.encodeByMd5(SystemUtil.getConfig()), null, null);
                if(StringUtils.isNotBlank(sySignHours) && StringUtils.isNumeric(sySignHours)){
                    sySignHours = sySignHours.substring(8);
                    //设置 -1小时
                    License.hour =  Integer.parseInt(sySignHours);
                }
            }
        } catch (Exception e) {
            License.hour = 0;
            e.printStackTrace();
        }
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
