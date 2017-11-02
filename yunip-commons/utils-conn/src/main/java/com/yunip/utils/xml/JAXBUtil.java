/*
 * 描述：JAXB(Java Architecture for XML Binding) 工具类，实现 Object 和 XML 之间的转换
 * 创建人：junbin.zhou
 * 创建时间：2013-1-8
 */
package com.yunip.utils.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXB;

/**
 * JAXB(Java Architecture for XML Binding) 工具类，实现 Object 和 XML 之间的转换
 * 默认生成的节点顺序是随机的，使用 @XmlType 注解指定 xml 中节点的顺序
 * 默认根节点名称为类名，使用 @XmlRootElement 注解指定 xml 根节点名称
 * 默认节点名称为属性名称，使用 @XmlElement 注解指定节点名称，同时可以指定节点是否为必须
 * 使用 @XmlElementWrapper 和 @XmlElement 注解可以为泛型集合指定父、子节点名称，如：多个 policy 节点，由一个父节点 policys/listpolicy 包含
 * 类字段默认生成 xml 节点，使用 @XmlAttribute 注解将字段定义为根节点的属性，如：<earth desc="地球">
 * 使用 @XmlAccessorType 注解指定需要生成 xml 节点的字段
 * 指定 namespace 默认前缀为ns2 ，如果需要更改前缀，请参考：http://blog.csdn.net/zl3450341/article/details/8155146
 */
public class JAXBUtil {
    /**
     * 构造方法，禁止实例化
     */
    private JAXBUtil() { }
    
    /**
     * 
     * getXML(对象转换为 XML) 
     * @param bean
     * @return  
     * String 
     * @exception
     */
    public static String toXML(Object bean) {
        StringWriter xml = new StringWriter();
        JAXB.marshal(bean,xml);
        return xml.toString();
    }
    
    /**
     * 
     * getObject(xml转换为对象) 
     * @param beanClass
     * @param xml
     * @return  
     * T 
     * @exception
     */
    public static <T> T toObject(Class<T> beanClass, String xml) {
        StringReader reader = new StringReader(xml);
        return JAXB.unmarshal(reader,beanClass);
    }
    
    /**
     * 
     * toObject(xml转换为对象) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param beanClass
     * @param url
     * @return  
     * T 
     * @exception
     */
    public static <T> T toObject(Class<T> beanClass, URL url) {
        return JAXB.unmarshal(url, beanClass);
    }
    
    /**
     * 
     * toObject(xml转换为对象) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param beanClass
     * @param is
     * @return  
     * T 
     * @exception
     */
    public static <T> T toObject(Class<T> beanClass, InputStream is) {
        return JAXB.unmarshal(is, beanClass);
    }
}
