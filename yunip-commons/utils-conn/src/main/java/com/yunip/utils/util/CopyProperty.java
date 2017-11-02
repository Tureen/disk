/**
 * 
 */
package com.yunip.utils.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author can.du
 *  属性copy 工具类
 */
public class CopyProperty {
    
    public static void copyProperty(Object oldObj,Object newObj,String ...fields){
        //新的class
        Class<? extends Object> newClass = newObj.getClass();
        //老的class
        Class<? extends Object> oldClass = oldObj.getClass();
        //该类所有的属性
        Field[] newFields = newClass.getDeclaredFields();
        //新的属性
        Field newField = null;
        //老的属性
        Field oldField = null;
        try {
            for(Field f : newFields){
                boolean isContinue = true;
                //类中的属性名称
                String fieldName = f.getName();
                if(Modifier.isFinal(f.getModifiers())){
                    continue;
                }
                //过滤不需要更新的字段
                for(String field : fields){
                    if(field.equals(fieldName)){
                       isContinue = false;
                       break;  
                    }
                }
                if(!isContinue){
                    continue;
                }
                //通过属性名称获取属性
                newField = newClass.getDeclaredField(fieldName);
                //获取属性的值时需要设置为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
                //值为 false 则指示反射的对象应该实施 Java 语言访问检查。 
                newField.setAccessible(true);
                //根据属性获取对象上的值
                Object newObject = newField.get(newObj);
                //过滤空的属性或者一些默认值
                if (isContinue(newObject)) {
                    continue;
                }
                oldField = oldClass.getDeclaredField(fieldName);
                oldField.setAccessible(true);
                oldField.set(oldObj, newObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     *  是否跳出本次循环
      * @author 2014-11-6 上午11:37:22
      * @param object
      * @return true 是 有null或者默认值
      *         false 否 有默认值
     */
    private static boolean isContinue(Object object){
        //序列过滤
        if (object == null || "".equals(object)) {
            return true;
        }
        return false;
    }
}
