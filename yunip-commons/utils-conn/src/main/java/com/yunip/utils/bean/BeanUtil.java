/*
 * 描述：javabean属性操作工具
 * 创建人：pan.tang
 * 创建时间：2012-8-15
 */
package com.yunip.utils.bean;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 利用java反射对属性进行操作
 */
public final class BeanUtil {
    /**
     * 
     * 创建一个新的实例 BeanUtils.
     */
    private BeanUtil() {

    }

    /**
     * 将全部需要转换的bean方法进行缓存
     */
    @SuppressWarnings("rawtypes")
    private static HashMap<Class, Method[]>                beanMethodCache     = new HashMap<Class, Method[]>();

    /**
     * 将全部转换的bean方法进行缓存
     */
    @SuppressWarnings("rawtypes")
    private static HashMap<Class, HashMap<String, Method>> fromBeanMethodCache = new HashMap<Class, HashMap<String, Method>>();

    /**
     * 
     * copyProperties(将fromBean相同的基本类型属性复制给beanClass) 
     * @param beanClass 被复制属性的对象
     * @param fromBean 对象属性源
     * @return  T 将属性进行负责并返回对象
     * @exception
     */
    @SuppressWarnings("rawtypes")
    public static <T> T copyProperties(Class<T> beanClass, Object fromBean) {
        Object[] objects = new Object[0];
        try {
            T bean = beanClass.newInstance();
            Class formBeanClass = fromBean.getClass();

            //将属性接收对象的方法放入到缓存中
            Method[] beanMethods = beanMethodCache.get(beanClass);
            if (beanMethods == null) {
                beanMethods = beanClass.getMethods();
                beanMethodCache.put(beanClass, beanMethods);
            }

            //将属性源对象的方法放入到缓存中
            HashMap<String, Method> fromBeanMethods = fromBeanMethodCache.get(formBeanClass);
            if (fromBeanMethods == null) {
                fromBeanMethods = new HashMap<String, Method>();
                Method[] methods = formBeanClass.getMethods();
                String getMethodName = null;
                for (Method method : methods) {
                    getMethodName = method.getName();
                    if (getMethodName.startsWith("get")
                            || getMethodName.startsWith("is")) {
                        fromBeanMethods.put(getMethodName, method);
                    }
                }
                fromBeanMethodCache.put(formBeanClass, fromBeanMethods);
            }

            String methodName = null;
            String getMethodName = null;
            Class[] paramsType = null;
            Class paramType = null;
            for (Method method : beanMethods) {
                methodName = method.getName();
                //对前边包含set的属性进行赋值
                if (methodName.startsWith("set")) {
                    paramsType = method.getParameterTypes();
                    //如果是基本对象
                    if (paramsType.length != 1) {
                        continue;
                    }
                    paramType = paramsType[0];
                    if (paramType.isInstance(boolean.class)) {
                        getMethodName = "is" + methodName.substring(3);
                    } else {
                        getMethodName = "get" + methodName.substring(3);
                    }
                    Method formBeanGetMethod = fromBeanMethods.get(getMethodName);
                    //如果数据源对象有该方法
                    if (formBeanGetMethod != null) {
                        if (paramType.equals(formBeanGetMethod.getReturnType())) {
                            try {
                                Object value = formBeanGetMethod.invoke(
                                        fromBean, objects);
                                if (value != null) {
                                    method.invoke(bean, new Object[] {value});
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
