/*
 * 描述：日期工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期工具类
 */
public final class DateUtil {
    /** 
     * 禁止实例化
     */
    private DateUtil() { }

    /**
     * 以下为日期转换的格式 默认方式
     */
    public static final String YMD_DATA = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    /** 日期格式 **/
    public static final String YMD01_DATA = "yyyy/MM/dd";

    /** 日期格式 **/
    public static final String YMDHMS_DATA = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式 **/
    public static final String YMDSTRING_DATA = "yyyyMMddHHmmss";

    /** 日期格式 **/
    public static final String YMDGB_DATA = "yyyy年MM月dd日";

    /** 日期格式 **/
    public static final String YMDTHMSGB_DATA = "yyyy年MM月dd日 HH时mm分ss秒";

    /** 日期格式 **/
    public static final String YMD24H_DATA = "yyyy-MM-dd HH:mm:ss";

    /** 星期中文表示 */
    public static final String[] DAYNAMES = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /** 一天的毫秒数 **/
    public static final int ONE_DATE_MILLISECOND = 24 * 60 * 60 * 1000;
    
    /** 一天的分钟数 **/
    public static final int ONE_MINUTE_MILLISECOND =  60 * 1000;
    
    /** 日期格式 **/
    public static final String YMDHM_DATA = "yyyy-MM-dd HH:mm";
    
    /** 日期格式 **/
    public static final String YMDSTRINGFFF_DATA = "yyyyMMddHHmmssSSS";
    
    /** 日期格式 **/
    public static final String YMDHMS_DATA_MINTUE = "yyyy-MM-dd HH:mm";
    
    /**
     * 获取当前年份
     * @return 当前年份
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return 当前月份
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * 获取当前日期（天）
     * @return 当前月份
     */
    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取中文星期
     * @return 中文星期
     */
    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DAYNAMES[dayOfWeek - 1];
    }

    /**
     * 
     * getDateWeek(返回指定日期的星期  0,1,2,3,4,5,6) 
     * @param date 指定日期格式为 YYYY-MM-DD
     * @return  
     * String 
     * @exception
     */
    public static String getDateWeek(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, "YYYY-MM-DD"));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek + "";
    }

    /**
     * 获取当前时间日期表示，格式 YYYY-MM-DD 
     * @return 当前时间日期表示，格式 YYYY-MM-DD 
     */
    public static String getYMD() {
        return getYMD("-");
    }

    /**
     * 获取当前时间中文日期表示，格式 YYYY年MM月DD日 
     * @param 分割符，如：“/”，“-”，“.”，“”
     * @return 当前时间中文日期表示，格式 YYYY年MM月DD日 
     */
    public static String getYMD(String separator) {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) + separator
                + (now.get(Calendar.MONTH) + 1) + separator
                + now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间中文日期表示，格式 YYYY年MM月DD日 
     * @return 当前时间中文日期表示，格式 YYYY年MM月DD日 
     */
    public static String getYMD_CN() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1)
                + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
    }

    /**
     * 将 Date 对象转换为制定格式字符串 
     * @param date 
     * @param format
     * @return  
     */
    public static String getDateFormat(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 将 Calendar 对象转换为制定格式字符串 
     * @param calendar 
     * @param format
     * @return  
     */
    public static String getDateFormat(Calendar calendar, String format) {
        return getDateFormat(calendar.getTime(), format);
    }

    /**
     * 获取中文星期
     * @return 中文星期
     */
    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DAYNAMES[dayOfWeek - 1];
    }

    /**
     * 将 日期字符串转为 Date 对象
     * @param dateStr
     * @param format
     * @return  Date 类型时间 
     */
    public static Date parseDate(String dateStr, String format) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }

        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    /**
     * 
     * getToday(获取当前日期，不包含时分秒) 
     * (这里描述这个方法适用条件 – 可选) 
     * @return  
     * Date 
     * @exception
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 
     * getDayDiff(计算两个日期相差天数：end - start) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param date1
     * @param date2
     * @return  
     * int 
     * @exception
     */
    public static int getDayDiff(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / ONE_DATE_MILLISECOND);
    }

    /**
     * 获取当前月的第一天
     * @return
     */
    public static String getMonthStart() {
        Calendar calendar = Calendar.getInstance();     
        calendar.set(Calendar.DAY_OF_MONTH, calendar     
                .getActualMinimum(Calendar.DAY_OF_MONTH));     
        
        return getDateFormat( calendar,"yyyy-MM-dd");     
    }

    /**
     * 获取对应月份的最后一天
     * @param date
     * @return
     */
    public static String doGetMonthEnd(String querymonth) {
        String monthend = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            date = sdf.parse(querymonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            monthend = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthend;
    }

    /**
     * 获取最近三个月
     * @param date
     * @return
     */
    public static Map<String, String> getThreeYM() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 1; i > -2; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            //添加判断 calendar 月份为0 时候的月份 年月
            if (calendar.get(Calendar.MONTH) == 0) {
                int year = calendar.get(Calendar.YEAR) - 1;
                map.put(year + "年" + "12月", year + "-12" + "-01");
            } else {
                map.put(calendar.get(Calendar.YEAR) + "年"
                        + calendar.get(Calendar.MONTH) + "月",
                        calendar.get(Calendar.YEAR) + "-"
                                + calendar.get(Calendar.MONTH) + "-01");
            }
        }
        return map;
    }
    /**
     * 获得当天开始时间
     * @param date
     * @return
     */     
    public static Date dayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    /**
     * 获得当天或某天的几天后的时间
     * @param date
     * @return
     */     
    public static Date getlaterdaytime(Date date,int days) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+days);
        return calendar.getTime();
    }
    /**
     * 获得当天或某天的几个月后的时间
     * @param date
     * @return
     */     
    public static Date getlatermonthstime(Date date,int months) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+months);
        return calendar.getTime();
    }
 
     /**
      * 获得当天的结束时间
      * @param date
      * @return
      */
    
    public static Date dayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /***
     * getDaysAfterToday(获取当天前的几天的日期) 
     * @return  
     * List<String> 
     * @exception
     */
    public static List<String> getDaysAfterToday(Date date,int index){
        List<String> datelist = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i = 0 ;i <= index - 1; i++){
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            datelist.add(getDateFormat(calendar, YMD_DATA));
        }
        return datelist;
    }
 
    /**
     * getCurrYearFirst(获取某年第一天日期) 
     * @param year  年份 
     * @param date  日期
     * @return  
     * Date 
     * @exception
     */
    public static Date getCurrYearFirst(String date){  
        Calendar calendar = Calendar.getInstance();  
        int nowYear = calendar.get(Calendar.YEAR);
        //如果是2015则按照上线的时间开始
        Date startDate = parseDate(date, YMD_DATA);
        if(nowYear == 2015){
            return startDate;
        }
        calendar.clear();  
        calendar.set(Calendar.YEAR, nowYear);  
        return calendar.getTime();  
    } 
    
    /***
     * getCurrYear（获取当前年的第一天) 
     * @return  
     * Date 
     * @exception
     */
    public static Date getCurrYearFirstDay(Date date){  
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, nowYear);  
        return calendar.getTime();  
    } 
    
    /**
     * 
     * getLastYear(获取去年的最后一天) 
     * @return  
     * String 
     * @exception
     */
    public static Date getLastDayForLastYear(Date date){  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, nowYear);  
        calendar.set(Calendar.DAY_OF_YEAR, -1);  
        return calendar.getTime();
    } 
    
    /**
     * getCurrWeek(获取当前周的第一天) 
     * @return  
     * String 
     * @exception
     */
    public static Date getCurrWeekFirstDay(Date date){  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
        return calendar.getTime();
    }
    
    /***
     * getLastWeek(获取上周的最后一天) 
     * @return  
     * String 
     * @exception
     */
    public static Date getLastDayForLastWeek(Date date){  
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
    
    /**
     * getCurrMouth(获取当前月的第一天) 
     * @return  
     * String 
     * @exception
     */
    public static Date getCurrMouthFirstDay(Date date){  
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    
    /**
     * getLastMouth(获取上个月的最后一天) 
     * @return  
     * String 
     * @exception
     */
    public static Date getLastDayForLastMouth(Date date){  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
    
    
    /**
     * getYestoday(获取昨天的日期) 
     * @return  
     * Date 
     * @exception
     */
    public static Date getYestoday(){  
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();  
    } 
    
    /***
     * getLastDate(前一天的日期) 
     * @param date
     * @return  
     * Date 
     * @exception
     */
    public static Date getLastDate(Date date){  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();  
    } 
    
    
    /***
     * getDateList(获取两个日期内的所有日期列表) 
     * @param startDate
     * @param endDate
     * @return  
     * List<String> 
     * @exception
     */
    public static List<String> getDateList(Date startDate,Date endDate){
        List<String> list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        String startDateStr = getDateFormat(startDate, YMD_DATA);
        String endDateStr = getDateFormat(endDate, YMD_DATA);
        while(!startDateStr.equals(endDateStr)){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            list.add(startDateStr);
            startDateStr = getDateFormat(calendar, YMD_DATA);
        }
        list.add(endDateStr);
        return list;
    }
    
    /**
     * getDayDiff(计算两个日期相差天数：end - start 返回分钟数) 
     * @param start
     * @param end
     * @return  
     * int 
     * @exception
     */
    public static int getMinuteDiff(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / ONE_MINUTE_MILLISECOND);
    }

    /**
     * checkHoliday(检查日期是否为节假日（周六周日）) 
     * @param date
     * @return  
     * boolean 
     * @exception
     */
    public static boolean checkHoliday(Date date){
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        }
        return false;
    }
    
    /***
     * checkDateForDay(检查日期是否为今天) 
     * @param date
     * @return  
     * boolean 
     * @exception
     */
    public static boolean checkDateForDay(Date date){
        String dateStr = getDateFormat(date, YMD_DATA);
        String day = getDateFormat(new Date(), YMD_DATA);
        if(dateStr.equals(day)){
            return true;
        }
        return false;
    }
    
    /***
     * getDateNumDiff(差距多少天) 
     * @param start 起始日期
     * @param end   终止日期
     * @param date  上线日期
     * @return  
     * int 
     * @exception
     */
    public static int getDateNumDiff(Date start,  Date end,Date date) {
        if(start.compareTo(date) < 0){
            start = date;
        }
        return (int) ((end.getTime() - start.getTime()) / ONE_DATE_MILLISECOND);
    }
    
    /**
     * 获得当天开始时间
     * @param number 天的数量
     * @return
     */     
    public static Date getAddDates(Date date,int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, number);
        return calendar.getTime();
    }
    
    
    /***
     * 操作分钟数量
     * @param date   数日时间
     * @param minutes
     * @return
     */
    public static Date getDateByMinutes(Date date, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
    
    /**
     * 获取指定日期的前几天（负数）或后几天（正数）的Date
     * getDateByDay(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param date
     * @param day
     * @return  
     * Date 
     * @exception
     */
    public static Date getDateByDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        date = calendar.getTime();
        return date;
    }
    
    public static void main(String[] args) {
        Date date = parseDate("2012-10-01", YMD_DATA);
         System.out.println(getDayDiff(date, new Date()));
    }   
}
