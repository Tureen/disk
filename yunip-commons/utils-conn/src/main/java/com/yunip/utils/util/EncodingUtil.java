/*
 * 描述：字符编码处理工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.util;

/**
 * 字符编码处理工具类
 */
public class EncodingUtil {
    /** 
     * 将UTF编码的字符串转化为GB2312编码的字符串，主要用来处理中文显示乱码的问题 
     * 
     * @param UTF 
     *            通过UTF编码的字符串 
     * @return 通过GB2312编码的字符串 
     */
    public static String gb2312FromUTF(String UTF) {
        if (UTF == null) {
            return "";
        }
        else {
            try {
                return new String(UTF.getBytes("UTF-8"), "GB2312");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 将GB2312编码的字符串转化为UTF-8编码的字符串，主要用来处理中文显示乱码的问题 
     * 
     * @param GB2312 
     *            通过GB2312编码的字符串 
     * @return 通过UTF-8编码的字符串 
     */
    public static String utfFromGB2312(String GB2312) {
        if (GB2312 == null) {
            return "";
        }
        else {
            try {
                return new String(GB2312.getBytes("GB2312"), "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String gbkFromISO8859_1(String ISO8859_1) {
        if (ISO8859_1 == null) {
            return "";
        }
        else {
            try {
                return new String(ISO8859_1.getBytes("ISO8859_1"), "GBK");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String GBKFromUTF(String UTF) {
        if (UTF == null) {
            return "";
        }
        else {
            try {
                return new String(UTF.getBytes("UTF-8"), "GBK");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 将ISO8859_1编码的字符串转化为UTF-8编码的字符串，主要用来处理中文显示乱码的问题 
     * 
     * @param ISO8859_1str 
     *            通过ISO8859_1编码的字符串 
     * @return 通过UTF-8编码的字符串 
     */
    public static String UTFFromISO8859_1(String ISO8859_1str) {
        return ISO8859_1str;
    }

    public static String UTFFromGBK(String GBK) {
        if (GBK == null) {
            return "";
        }
        else {
            try {
                return new String(GBK.getBytes("GBK"), "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 将UTF-8编码的字符串转化为ISO8859_1编码的字符串，主要用来处理中文显示乱码的问题 
     * 
     * @param UTF 
     *            通过UTF编码的字符串 
     * @return 通过ISO8859_1编码的字符串 
     */
    public static String ISO8859_1FromUTF(String UTFstr) {
        if (UTFstr == null) {
            return "";
        }
        else {
            try {
                return new String(UTFstr.getBytes("UTF-8"), "ISO8859_1");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 将GB2312编码的字符串转化为ISO8859_1编码的字符串 
     * 
     * @param GBstr 
     *            GB2312编码的字符串 
     * @return ISO8859_1编码的字符串 
     */
    public static String ISO8859_1String(String GBstr) {
        if (GBstr == null) {
            return "";
        }
        else {
            try {
                return new String(GBstr.getBytes("GB2312"), "ISO8859_1");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 将GB2312编码的字符串转化为ISO8859_1编码的字符串 
     * 
     * @param GBstr 
     *            GB2312编码的字符串 
     * @return ISO8859_1编码的字符串 
     */
    public String ISO8859_1FromGB2312(String GBstr) {
        if (GBstr == null) {
            return "";
        }
        else {
            try {
                return new String(GBstr.getBytes("GB2312"), "ISO8859_1");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String ISO8859_1FromGBK(String GBK) {
        if (GBK == null) {
            return "";
        }
        else {
            try {
                return new String(GBK.getBytes("GBK"), "ISO8859_1");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /** 
     * 去除字符串两端空格。 
     * 
     * @param str 
     *            需要处理的字符串 
     * @return 去掉了两端空格的字符串，如果str 为 null 则返回 "" 
     */
    public static String trim(String str) {
        if (str != null) {
            return str.trim();
        }
        else {
            return "";
        }
    }

    /**
     * 函数传入汉字的Unicode编码字符串，返回相应的汉字字符串
     * @param utfString
     * @return  
     * String 
     * @exception
     */
    public static String decodeUnicode(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(
                        utfString.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }

    /**
     * 函数传入汉字的Unicode编码字符串，返回相应的汉字字符串
     * @param theString
     * @return  
     * String 
     * @exception
     */
    public static String decodeUnicodeSwitch(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx      
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
            outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
