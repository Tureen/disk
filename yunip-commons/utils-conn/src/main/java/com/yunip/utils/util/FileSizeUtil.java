/*
 * 描述：〈文件大小工具类〉
 * 创建人：can.du
 * 创建时间：2016-5-25
 */
package com.yunip.utils.util;


/**
 * 文件大小工具类
 */
public class FileSizeUtil {

    public static String  bytesToSize(Long bytes){
        Float size  = Float.valueOf(bytes);
        String[] unitArr = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int rank =0;
        while(size > 1000){
            size = size/1024;
            rank++;
        }
        String result = String.format("%2.2f",size);
        if(result.indexOf(".") != -1){
            if(size > 10){//超过10的单位大小文件只显示小数点后一位
                return result.substring(0, result.indexOf(".")+2) + " "+ unitArr[rank];
            }else{
                return result.substring(0, result.indexOf(".")+3) + " "+ unitArr[rank];
            }
        }else{
            return result + " "+ unitArr[rank];
        }
    }
}
