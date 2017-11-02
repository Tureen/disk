/*
 * 描述：集合处理工具类
 * 创建人：ming.zhu
 * 创建时间：2017-02-21
 */
package com.yunip.utils.util;

import java.util.ArrayList;
import java.util.List;

/**
* 集合处理工具类
*
*/
public final class ListUtil {

    /**
     * list集合string转int
     * @param lstr
     * @return  
     * List<Integer> 
     * @exception
     */
    public static List<Integer> strToInt(List<String> lstr) {
        List<Integer> lint = new ArrayList<Integer>();
        for (String str : lstr) {
            if (!str.matches("^([0-9])+$")) {
                continue;
            }
            int i = Integer.parseInt(str);
            lint.add(i);
        }
        return lint;
    }
}
