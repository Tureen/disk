/*
 * 描述：〈描述〉
 * 创建人：yongbo.zhang
 * 创建时间：2013-9-23
 */
package com.yunip.web.helper;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * 文件上传辅助
 */
public class FileuploadHelper {

    public static Boolean fileuploadStart(InputStream inputStream,String targetURL) {
        PostMethod postMethod = null;
        Boolean isloadStatus = false;
        try{
                 HttpClient client = new HttpClient();
                 postMethod = new PostMethod(targetURL); 
                 RequestEntity requestEntity = new InputStreamRequestEntity(inputStream);
                 
                 postMethod.setRequestEntity(requestEntity);
                 client.getHttpConnectionManager().getParams().setConnectionTimeout(1150000);//设置超时
                 int status;
                 status = client.executeMethod(postMethod);
                 if(status == HttpStatus.SC_OK){
                     isloadStatus = true; 
                 }
                 
             }catch (Exception ex){
                 ex.printStackTrace();
             }finally{
                 if(postMethod!=null){
                     postMethod.releaseConnection();
                 }
             }
             
      return isloadStatus;
       
    }
}
