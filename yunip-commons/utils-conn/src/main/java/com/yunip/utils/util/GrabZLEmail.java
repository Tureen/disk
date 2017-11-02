package com.yunip.utils.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrabZLEmail {
    
    /**
     * 招聘列表url地址
     */
    @SuppressWarnings("unused")
    private static final String ZP_LIST_URL = "http://sou.zhaopin.com/jobs/searchresult.ashx?in=160400&jl=%E6%B7%B1%E5%9C%B3&sm=0&p=718";
    
    
    /******************************************匹配正则表达式 start******************************************/
    /**
     * 匹配招聘列表页面的招聘链接url地址正则
     */
    private static final String ZP_LIST_URL_REGEX = "http://jobs.zhaopin.com/([\\s\\S]*?)htm";
    
    /**
     * 匹配招聘内容页面的Email正则
     */
    private static final String ZP_EMAIL_REGEX = "[0-9a-zA-Z_\\-\\.]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
    //private static final String ZP_EMAIL_REGEX = "\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*";
    //private static final String ZP_EMAIL_REGEX = "\\w+@\\w+(\\.\\w+)+";
    /******************************************匹配正则表达式 end******************************************/
    
    
    /**
     * 爬取招聘列表url地址保存的文件目录
     */
    private static final String SAVE_ZP_LIST_URL_PATH = "D:/zp_list_url.txt";
    
    /**
     * 爬取招聘内容页面的Email保存的文件目录
     */
    private static final String SAVE_ZP_EMAIL_PATH = "D:/zp_email.txt";
    
    /**
     * 爬取列表页面的招聘链接
     */
    public static void getListUrl(String requestUrl){
        StringBuffer result = new StringBuffer("");
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();//取得链接
            BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));// 取得网页数据
            
            String line = null;
            Pattern p = Pattern.compile(ZP_LIST_URL_REGEX);
            while ((line = buff.readLine()) != null) {
                Matcher m = p.matcher(line);// 进行匹配
                while (m.find()) {
                    result.append(m.group() + "\r\n");// 将匹配的字符输入到目标文件
                }
            }
            //System.out.println("=" + result.toString());
            //将匹配结果写入文本文件进行保存
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SAVE_ZP_LIST_URL_PATH, true)));
                out.write(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 总共爬取多少页的数据
     * @param page 总共爬取页数
     */
    public static void startGetListUrl(int totalPage){
        int count = 1;
        while(totalPage > 0){
            //String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?in=140000&jl=%E6%B7%B1%E5%9C%B3&kw=%E9%82%AE%E7%AE%B1&sm=0&source=0";
            String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?jl=%E6%B7%B1%E5%9C%B3&kw=%E9%82%AE%E7%AE%B1&isadv=0&ispts=1&isfilter=1&p=1";
            System.out.println("列表页面第" + count + "页");
            getListUrl(url + "&p=" + count);
            totalPage--;
            count++;
        }
    }
    
    /**
     * 从招聘页面中获取email信息
     */
    public static void getContentEmail(String requestUrl){
        StringBuffer result = new StringBuffer("");
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();//取得链接
            BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));// 取得网页数据
            
            String line = null;
            Pattern p = Pattern.compile(ZP_EMAIL_REGEX);
            while ((line = buff.readLine()) != null) {
                //System.out.println(line);
                Matcher m = p.matcher(line);// 进行匹配
                while (m.find()) {
                    result.append(m.group() + "\r\n");// 将匹配的字符输入到目标文件
                }
            }
            System.out.println("=" + result.toString());
            //将匹配结果写入文本文件进行保存
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SAVE_ZP_EMAIL_PATH, true)));
                out.write(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 根据网址爬取里面的Email
     * @param filePath 文件路径
     */
    @SuppressWarnings("resource")
    public static void startGetEmail(String filePath){
        try {
            File file = new File(filePath);
            if(file.exists() && file.isFile()){
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String strTemp;
                int count = 1;
                while((strTemp = br.readLine()) != null){
                    System.out.println(count + "：" + strTemp);
                    getContentEmail(strTemp);
                    count++;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        startGetEmail("D:/zp_list_url.txt");
    	//抓取列表
        //startGetListUrl(31);
        //getContentEmail("http://jobs.zhaopin.com/402645787250011.htm");
        //getContentEmail("http://jobs.zhaopin.com/277516835250128.htm");
        //String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        //String regEx = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        //String regEx = "[0-9a-zA-Z_\\-\\.]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}){1,3}[a-zA-z\\-]{1,}";
        /*String regEx = "[0-9a-zA-Z_\\-\\.]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
        Pattern p = Pattern.compile(regEx);
        String[] arr = {"861133947@qq.com", "szxxfs@sina.com", "zhaopin@xuanweiqk.com", "yajia_18@163.com", "root_design@163.com", "service@noapeople.com"
                , "ping@hym-originals.com", "info@matrix-design.cn", "afra@legion.net.cn", "gys@vip.163.com", "service@elender.cc", "chenqinxuan@wow-trend.com"
                , "hr_ielts@gedu.org", "yihui.zheng@pearson.com", "aa@ringdoll.hk", "contact@arc-anime.com", "zp-hr@fsmeeting.com"};
        
        for(int i = 0;i < arr.length;i++){
            String line = arr[i];
            Matcher m = p.matcher(line);// 进行匹配
            while (m.find()) {
                System.out.println((i+1) + "：" + m.group());
            }
        }*/
        
    }

}
