/*
 * 描述：http请求工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * http请求工具类，以get或post方式发送http请求，获取返回结果（字符串）
 */
public final class HttpUtil {
    /**
     * 禁止实例化
     */
    private HttpUtil() { }
    
    /** 响应超时时间（毫秒）**/
    private static final int    DEFAULT_CONNECTION_TIME_OUT = 100000;

    /** 读取超时时间（毫秒）**/
    private static final int    DEFAULT_READ_TIME_OUT       = 100000;

    /** 默认编码  **/
    private static final String DEFAULT_ENCODING            = "gb2312";

    /** 请求方式 GET  **/
    private static final String REQUEST_METHOD_GET          = "GET";

    /** 请求方式 POST **/
    private static final String REQUEST_METHOD_POST         = "POST";

    /**
     * 采用 Get 方式发出Http请求，以 GB2312 编码格式发送请求
     * @param urlAddress 请求地址（包含参数）
     * @return
     */
    public static String get(String urlAddress) {
        return request(urlAddress, REQUEST_METHOD_GET, "", "", DEFAULT_ENCODING);
    }

    /**
     * 采用 Get 方式发出Http请求
     * @param urlAddress 请求地址（包含参数）
     * @param encoding 编码格式
     * @return
     */
    public static String get(String urlAddress, String encoding) {
        return request(urlAddress, REQUEST_METHOD_GET, "", "", encoding);
    }

    /**
     * 采用 POST 方式发出Http请求
     * @param urlAddress 请求地址
     * @param params POST参数
     * @return
     */
    public static String post(String urlAddress, String params, String cookieValue) {
        return request(urlAddress, REQUEST_METHOD_POST, params, cookieValue, DEFAULT_ENCODING);
    }

    /**
     * 采用 POST 方式发出Http请求
     * @param urlAddress 请求地址
     * @param params POST参数
     * @param encoding 编码格式
     * @return
     */
    public static String post(String urlAddress, String params, String cookieValue, String encoding) {
        return request(urlAddress, REQUEST_METHOD_POST, params, cookieValue, encoding);
    }

    /**
     * 发出HTTP请求
     * @param urlAddress url地址
     * @param method 请求方式
     * @param params POST请求参数
     * @param encoding 编码格式
     * @return
     */
    private static String request(String urlAddress, String method,
            String params, String cookieValue, String encoding) {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            url = new URL(urlAddress);
            con = (HttpURLConnection) url.openConnection();

            //如果不设置 Accept-Encoding，部分网站会识别为爬虫程序，不能正常访问
            con.setRequestProperty("Accept-Encoding", "gzip,deflate");
            if(cookieValue != null && !cookieValue.equals("")){
                con.setRequestProperty("Cookie", cookieValue);
            }
            con.setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT);
            con.setReadTimeout(DEFAULT_READ_TIME_OUT);
            con.setUseCaches(false);

            //如果调用方式不对，部分网站不能正常访问
            con.setRequestMethod(method);

            if (REQUEST_METHOD_POST.equals(method)) {
                con.setDoOutput(true);
                byte[] b = params.getBytes();
                con.getOutputStream().write(b);
                con.getOutputStream().flush();
                con.getOutputStream().close();
            } else {
                con.setDoOutput(false);
            }

            //如果返回结果是 gzip 压缩数据，进行流处理
            if (null != con.getContentEncoding()
                    && con.getContentEncoding().indexOf("gzip") > -1) {
                GZIPInputStream gzin = new GZIPInputStream(con.getInputStream());
                in = new BufferedReader(new InputStreamReader(gzin, encoding));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), encoding));
            }

            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else {
                    result.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
    /**
     * 
     * getFilebyUrl(通过url 获得文件服务器file) 
     * @param response
     * @param fileUrl  
     * void 
     * @exception
     */
    @SuppressWarnings("static-access")
    public static void getFilebyUrl(HttpServletResponse response,String fileUrl){
    	URL url = null;  
        InputStream is =null;  
        try {  
            url = new URL(fileUrl);  
         } catch (MalformedURLException e) {  
            e.printStackTrace();  
         }  
        try {  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.   
            conn.setDoInput(true);  
            conn.connect();  
            is = conn.getInputStream(); //得到网络返回的输入流   
            ServletOutputStream out = response.getOutputStream();
            //循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while((len=is.read(b)) >0){
          	  out.write(b,0,len);
            }
           response.setStatus( response.SC_OK );
            response.flushBuffer();
           out.close();
           is.close();
        } catch (IOException e) {  
            e.printStackTrace();  
       }
    }
}
