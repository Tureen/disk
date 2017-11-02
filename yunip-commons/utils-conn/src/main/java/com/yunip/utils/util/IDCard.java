/*
 * 描述：身份证的有效验证，只验证省级代码，不验证具体地市代码
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yunip.utils.date.DateUtil;

/**
 * 身份证的有效验证，只验证省级代码，不验证具体地市代码
 */
public final class IDCard {
	/** 性别 ：男 **/
    private static final String MALE             = "1";

    /** 性别：女  **/
    private static final String FEMALE           = "2";

    /** 性别：未知 **/
    private static final String UNKNOWN          = "0";
    /**
     * 构造函数，禁止实例化
     */
    private IDCard() { }
    /**
     * 功能：身份证的有效验证，只验证省级代码，不验证具体地市代码
     * 
     * @param cardNo
     *            身份证号
     * @return 有效：true 无效：false
     * @throws ParseException
     */
    public static boolean isIdCard(String cardNo) {
        String[] valCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String ai = "";

        //号码的长度 15位或18位 
        if (cardNo.length() != 15 && cardNo.length() != 18) {
            return false;
        }

        //数字 除最后以为都为数字
        if (cardNo.length() == 18) {
            ai = cardNo.substring(0, 17);
        } else if (cardNo.length() == 15) {
            ai = cardNo.substring(0, 6) + "19" + cardNo.substring(6, 15);
        }
        if (!isNumeric(ai)) {
            return false;
        }

        //出生年月是否有效
        String strYear = ai.substring(6, 10);
        String strMonth = ai.substring(10, 12);
        String strDay = ai.substring(12, 14);

        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            System.out.println("生日无效。");
            return false;
        }

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = null;
        try {
            birthday = s.parse(strYear + "-" + strMonth + "-" + strDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        GregorianCalendar gc = new GregorianCalendar();
        // 生日不在有效范围（大于0岁，小于150岁）
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - birthday.getTime()) < 0) {
            return false;
        }
        
        // 月份无效
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        
        // 日期无效
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }

        // ================ 地区码是否有效 ================
        Hashtable<String, String> h = getAreaCode();
        if (h.get(ai.substring(0, 2)) == null) {
            return false;
        }

        // ================ 判断最后一位的值 ================
        int totalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalmulAiWi = totalmulAiWi
                    + Integer.parseInt(String.valueOf(ai.charAt(i)))
                    * Integer.parseInt(wi[i]);
        }
        int modValue = totalmulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        ai = ai + strVerifyCode;

        if (cardNo.length() == 18) {
            // 身份证无效，校验码错误
            if (!ai.equalsIgnoreCase(cardNo)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 功能：设置地区编码
     * 
     * @return Hashtable 对象
     */
    private static Hashtable<String, String> getAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * ======================================================================
     * 功能：判断字符串是否为数字
     * 
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     * 
     * @param str
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?"
                + "((((0?[13578])|(1[02]))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|"
                + "([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))"
                + "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|"
                + "([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }
    /**
     * @throws ParseException 
     * 
     * getBirthday(从证件号中获取出生日期) 
     * (非身份证和驾驶证，出生日期随机选，年龄大于18周岁小60周岁。) 
     * @param cardType
     * @param cardNo
     * @return  
     * String 
     * @exception
     */
    public static Date getBirthday(String cardNo) {
    	Date  date=null;
       	//十五位身份证
        if (cardNo.length() == 15) {
            date=DateUtil.parseDate(cardNo.substring(6, 12), "yyMMdd");
        } else {
        //十八位身份证
            date=DateUtil.parseDate(cardNo.substring(6, 14),"yyyyMMdd");
        }
	   return date;
    }
    /**
     * @throws ParseException 
     * 
     * getBirthday(从证件号中获取出生日期) 
     * (非身份证和驾驶证，出生日期随机选，年龄大于18周岁小60周岁。) 
     * @param cardType
     * @param cardNo
     * @return  
     * String 
     * @exception
     */
    public static String getBirthdayToString(String cardNo) {
    	String  date=null;
       	//十五位身份证
        if (cardNo.length() == 15) {
            date="19"+cardNo.substring(6, 12);
        } else {
        //十八位身份证
            date=cardNo.substring(6, 14);
        }
	   return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
    }
    /**
     * 
     * getGendar(获取性别) 
     * (身份证号码是15位的,那么最后一位数字是偶数为女性.是奇数为男性.如果你是新式第二代身份证，身份证号码是18位的,就看倒数第2位数字，是偶数为女性.是奇数为男性. ) 
     * @param cardType
     * @param cardNo
     * @return  
     * String 
     * @exception
     */
    public static String getGendar(String cardNo) {
        if (cardNo.length() != 15 && cardNo.length() != 18) {
            return UNKNOWN;
        } else {
            if (cardNo.length() == 15) {
                //身份证号码是15位的,那么最后一位数字是偶数为女性.是奇数为男性
                int gendarFlag = new Integer(cardNo.substring(14, 15));
                return gendarFlag % 2 == 0 ? FEMALE : MALE;
            } else {
                //身份证号码是18位的,就看倒数第2位数字，是偶数为女性.是奇数为男性.
                int gendarFlag = new Integer(cardNo.substring(16, 17));
                return gendarFlag % 2 == 0 ? FEMALE : MALE;
            }
        }
    }
}
