/*
 * 描述：〈描述〉
 * 创建人：can.du
 * 创建时间：2016-7-6
 */
package com.yunip.web.common;

import java.util.Random;

import com.yunip.utils.pwd.Des;

/**
 * 一句话功能描述
 */
public class License {


    /**这个是天数用来加密的DES加密数据(生成小时数序列码)***/
    public final static String  LICENSECODEKEY      = "46bc8428d120a13f3d1404f192df54f0";

    /***
     * 生成时长授权签名
     * @return  
     * String 
     * @exception
     */
    public static String getLicenseCode(int hours){
        String random =  getFixLenthString(9);
      /*  int hours = 24 * 30 * 1000;*/
        return random + hours;
    }
    
    
    /*
     * 返回长度为【strLength】的随机数，在前面补0
     */
    private static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 1);
    }
    
    
    /** 
     * main(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param args  
     * void 
     * @exception  
    */
    public static void main(String[] args) {
        //客户端序列号
        String clientLiencese = "44033453ef1ead045082c047f58a02c4";
        //新的加密密钥
        String signCode = clientLiencese + LICENSECODEKEY;
        int hours = 24 * 30 * 1000 ;
        //时效序列号
        String timeCode = getLicenseCode(hours);
        Des des = new Des();
        //用客户端序列号对失效序列号进行加密
        String mac = des.strEnc(timeCode, signCode, null, null);
        //FEC0C9275F6733C59F395341A3DC8797030E592FA987C771
        //FEC0C9275F6733C58AB4F0B5FFD16872030E592FA987C771
        System.out.println(mac);
        System.out.println(getFixLenthString(9));
    }

}
