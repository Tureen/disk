package com.yunip.utils.util;

import java.util.Random;

/** 
* 随机数、随机字符串工具 
*/
public final class RandomUtil {
    /** 所有字符 **/
    public static final String ALL_CHAR    = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** 字母字符 **/
    public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** 数字字符 **/
    public static final String NUMBER_CHAR = "0123456789";
    
    /**
     * 禁止实例化
     */
    private RandomUtil() { }

    /** 
    * 返回一个定长的随机字符串(只包含大小写字母、数字) 
    * 
    * @param length 随机字符串长度 
    * @return 随机字符串 
    */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回 int 随机数
     * (返回 0 和 max(不包括)之间的随机数 
     * @param max 随机数范围
     * @return  
     * int 
     * @exception
     */
    public static int getInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    
    /**
     * 返回 int 随机数
     * (返回 min（包括） 和 max(不包括)之间的随机数 
     * @param max 随机数范围
     * @return  
     * int 
     * @exception
     */
    public static int getInt(int min, int max) {
        return getInt(max - min) + min;
    }
    
    /** 
    * 返回一个定长的随机纯字母字符串(只包含大小写字母) 
    * 
    * @param length 随机字符串长度 
    * @return 随机字符串 
    */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return sb.toString();
    }
    
    /** 
     * 返回一个定长的随机纯数字字符串
     * 
     * @param length 随机数字字符串长度 
     * @return 随机字符串 
     */
     public static String generateNumberString(int length) {
         StringBuffer sb = new StringBuffer();
         Random random = new Random();
         for (int i = 0; i < length; i++) {
             sb.append(ALL_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
         }
         return sb.toString();
     }

    /** 
    * 返回一个定长的随机纯大写字母字符串(只包含大小写字母) 
    * 
    * @param length 随机字符串长度 
    * @return 随机字符串 
    */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /** 
    * 返回一个定长的随机纯小写字母字符串(只包含大小写字母) 
    * 
    * @param length 随机字符串长度 
    * @return 随机字符串 
    */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /** 
    * 生成一个定长的纯0字符串 
    * 
    * @param length 字符串长度 
    * @return 纯0字符串 
    */
    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /** 
    * 根据数字生成一个定长的字符串，长度不够前面补0 
    * 
    * @param num 数字 
    * @param fixdlenth 字符串长度 
    * @return 定长的字符串 
    */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /** 
    * 根据数字生成一个定长的字符串，长度不够前面补0 
    * 
    * @param num 数字 
    * @param fixdlenth 字符串长度 
    * @return 定长的字符串 
    */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * main(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param args  
     * void 
     * @exception
     */
    public static void main(String[] args) {
        System.out.println(generateString(15));
        System.out.println(generateMixString(15));
        System.out.println(generateLowerString(15));
        System.out.println(generateUpperString(15));
        System.out.println(generateZeroString(15));
        System.out.println(toFixdLengthString(123, 15));
        System.out.println(toFixdLengthString(123L, 15));
    }
}
