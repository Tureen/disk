/*
 * 描述：〈清理结果MAP〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.task;

import java.util.Date;

import com.yunip.constant.SystemContant;
import com.yunip.utils.date.DateUtil;

/**
 * 清理结果MAP
 */
public class ClearQueue extends Thread{

    @Override
    public void run() {
        try {
            Thread.sleep(1000 * 60 * 24);
            //将没有及时清理掉的结构数据 进行清理
            Date date = DateUtil.getDateByDay(new Date(), -1);
            String code = DateUtil.getDateFormat(date, DateUtil.yyyyMMdd);
            for(String key : SystemContant.TASKMAP.keySet()){
                if(key.contains(code)){
                    SystemContant.TASKMAP.remove(key);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
