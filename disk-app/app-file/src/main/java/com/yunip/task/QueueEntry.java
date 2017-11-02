/*
 * 描述：〈队列处理〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.task;

import org.apache.log4j.Logger;

import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.model.disk.support.QueueTask;

/**
 * 一句话功能描述
 */
public class QueueEntry extends Thread{
    
    public static Logger logger = Logger.getLogger(QueueEntry.class);

    @Override
    public void run() {
       while(true){
            if(SystemContant.TASKQUEUE.size() > 0){
                //开始执行（判断正在执行的任务列表的数量）
                if(SystemContant.TASKLIST.size() <= 2){
                    //检查内存
                    if(getOutMemory()){
                        //取出队列的第一个对象
                        QueueTask queueTask = SystemContant.TASKQUEUE.poll();
                        logger.info("开始执行队列对象 ：" + queueTask.getTaskName());
                        if(SystemContant.CANCELMAP.containsKey(queueTask.getTaskName())){
                            //取消
                            SystemContant.CANCELMAP.remove(queueTask.getTaskName());
                            continue;
                        }
                        FileTaskEntry taskEntry = new FileTaskEntry();
                        taskEntry.setQueueTask(queueTask);
                        taskEntry.start();
                    }
                }
            } else {
                //当请求队列为空循环，则线程等待
                logger.info("当前线程无解压任务，线程进入等待状态");
                pleaseWait();
            }
        }
    }
    
    /****
     * 超出80%的内存使用量 将不进行解压
     * @return  
     * boolean 
     * @exception
     */
    public static boolean getOutMemory(){
       Runtime runtime = Runtime.getRuntime();
       double data = Double.valueOf(runtime.freeMemory())/runtime.totalMemory() * 100;
       logger.info("当前总内存为："+ runtime.totalMemory() +"," +
       		"空闲内存为："+runtime.freeMemory()+",可用内存比为：" + data);
       return data <= 30 ? false : true;
    }
    
    /****
     * 检查是否存在队列中
     * @param taskName
     * @return  
     * boolean 
     * @exception
     */
    public static boolean checkQueueExist(String taskName){
        if(SystemContant.TASKQUEUE.size() > 0){
            for(QueueTask queueTask : SystemContant.TASKQUEUE){
               if(queueTask.getTaskName().equals(taskName)){
                   return true;
               }
            }
        }
        return false;
    }
    
    /****
     * 检查是否存在任务列表中
     * @param taskName
     * @return  
     * boolean 
     * @exception
     */
    public static boolean checkListExist(String taskName){
        if(SystemContant.TASKLIST.size() > 0){
            for(String taskQueue : SystemContant.TASKLIST){
               if(taskQueue.equals(taskName)){
                   return true;
               }
            }
        }
        return false;
    }
    
    /****
     * 获取任务执行的结果
     * @param taskName
     * @return  
     * boolean 
     * @exception
     */
    public static JsonData<Object> getQueueResult(String taskName){
        JsonData<Object> data = new JsonData<Object>();
        data.setCode(SystemContant.TASKMAP.get(taskName).getCode());
        data.setCodeInfo(SystemContant.TASKMAP.get(taskName).getCodeInfo());
        SystemContant.TASKMAP.remove(taskName);
        return data;
    }
    
    /**
     * 使线程处于等待状态
     * @return  
     * boolean 
     * @exception
     */
    public boolean pleaseWait() {  
        synchronized (SystemContant.wait) {  
            if (SystemContant.wait.get() == true) {  
                return false;  
            }  
            SystemContant.wait.set(true);  
            try {  
                SystemContant.wait.wait();  
            } catch (InterruptedException e) {  
            }  
            return true;  
        }  
    }
    
    /***
     * 唤醒线程
     * @return  
     * boolean 
     * @exception
     */
    public static boolean pleaseNotify() {  
        synchronized (SystemContant.wait) {  
            if (SystemContant.wait.get() == false) {  
                return true;  
            }  
            SystemContant.wait.set(false);  
            try {  
                SystemContant.wait.notify(); 
            } catch (Exception e) {  
            }  
            return true;  
        }  
    }
}
