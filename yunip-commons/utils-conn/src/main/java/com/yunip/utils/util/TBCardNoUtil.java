/*
 * 描述：太保证件号码工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.util;

import java.util.Date;

import com.yunip.utils.date.DateUtil;

/**
 * 太保证件号码工具类
 */
public final class TBCardNoUtil {
    /**
     * 禁止实例化
     */
    private TBCardNoUtil() { }

    /** 性别 ：男 **/
    private static final String MALE             = "1";

    /** 性别：女  **/
    private static final String FEMALE           = "2";

    /** 性别：未知 **/
    private static final String UNKNOWN          = "0";

    /** 证件类型：身份证 **/
    private static final String IDCARD           = "1";

    /** 证件类型：驾驶证 **/
    private static final String DRIVING_LICENCE  = "7";

    /** 默认出生日期 : 1988-08-08 **/
    //private static final String DEFAULT_BIRTHDAY = "19880808";

    /**
     * 
     * validCardNo(验证太保证件号码) 
     * 重点验证身份证（1）、驾驶证（7），驾驶证和身份证验证规则相同，其他类型证件只做非空校验
     * @param cardType 证件类型
     * @param cardNo 证件号码
     * @return  
     * boolean 
     * @exception
     */
    public static boolean validCardNo(String cardType, String cardNo) {
        //非身份证和驾驶证，证件号码不为空即通过
        if (!IDCARD.equals(cardType) && !DRIVING_LICENCE.equals(cardType)) {
            return cardNo != null && !"".equals(cardNo.trim());
        } else {
            return IDCard.isIdCard(cardNo);
        }
    }

    /**
     * 
     * getBirthday(从证件号中获取出生日期) 
     * (非身份证和驾驶证，出生日期随机选，年龄大于18周岁小60周岁。) 
     * @param cardType 证件类型
     * @param cardNo 证件号码
     * @return  
     * String 
     * @exception
     */
    public static Date getBirthday(String cardType, String cardNo) {
        //如果证件类型不是身份证 或 驾驶证，返回默认出生日期
        if (!IDCARD.equals(cardType) && !DRIVING_LICENCE.equals(cardType)) {
            return null;
        } else {
            if (cardNo.length() != 15 && cardNo.length() != 18) {
                return null;
            } else {
                if (cardNo.length() == 15) {
                    return DateUtil.parseDate(cardNo.substring(6, 12), "yyMMdd");
                } else {
                    return DateUtil.parseDate(cardNo.substring(6, 14),
                            "yyyyMMdd");
                }
            }
        }
    }

    /**
     * 
     * getGendar(获取性别) 
     * (身份证号码是15位的,那么最后一位数字是偶数为女性.是奇数为男性.如果你是新式第二代身份证，身份证号码是18位的,就看倒数第2位数字，是偶数为女性.是奇数为男性. ) 
     * @param cardType 证件类型
     * @param cardNo 证件号码
     * @return  
     * String 
     * @exception
     */
    public static String getGendar(String cardType, String cardNo) {
        try {
            //如果证件类型不是身份证 或 驾驶证，返回“未知”
            if (!IDCARD.equals(cardType) && !DRIVING_LICENCE.equals(cardType)) {
                return UNKNOWN;
            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
            return UNKNOWN;
        }
    }

    /**
     * 功能：身份证的有效验证，只验证省级代码，不验证具体地市代码
     * 
     * @param cardNo
     *            身份证号
     * @return 有效：true 无效：false
     * @throws ParseException
     */
    public static boolean isIdCard(String cardNo) {
        return IDCard.isIdCard(cardNo);
    }
}
