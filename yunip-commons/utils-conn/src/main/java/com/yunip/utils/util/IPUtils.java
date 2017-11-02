/*
 * 描述：〈IP处理的工具类〉
 * 创建人：can.du
 * 创建时间：2016-6-27
 */
package com.yunip.utils.util;

import org.apache.commons.lang.StringUtils;


/**
 * IP处理的工具类
 */
public class IPUtils {
    
    /***
     * 检测IP是否处于IP段内
     * @ip 当前IP
     * @ips　ip段
     * @return  
     * boolean true:不包含,false:包含
     * @exception
     */
    public static boolean getConatinsIp(String ip, String ips){
        if(!StringUtils.isNotBlank(ips)){
            return true;
        }
        if(ips.contains(ip)){
            return false;
        }
        String[] ipsArr = ips.split(",");
        boolean isSame = false;
        for(String aip : ipsArr){
            //判断(再次循环)
            String[] sips = aip.split("\\.");
            String[] ipd = ip.split("\\.");
            int index = 0;
            for(String sip : sips){
                if(!"*".equals(sip) && !sip.equals(ipd[index])){
                    isSame = true;
                }
                index ++ ;
            }
        }
        return isSame;
    }
}
