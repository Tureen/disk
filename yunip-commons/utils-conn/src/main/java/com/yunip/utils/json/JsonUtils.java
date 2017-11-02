package com.yunip.utils.json;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunip.utils.json.JsonDateValueProcessor;
import com.yunip.utils.date.DateJsonValueProcessor;

/**
 * json工具类.
 *
 * @author xiaojf
 */
public class JsonUtils {
    /** * logger. */
    private static Log logger = LogFactory.getLog(JsonUtils.class);

    /** * 工具类需要的保护构造方法. */
    protected JsonUtils() {}

    /**
     * write.
     *
     * @param bean obj
     * @param writer 输出流
     * @param excludes 不转换的属性数组
     * @param datePattern date到string转换的模式
     * @throws Exception 写入数据可能出现异常
     */
    public static void write(Object bean, Writer writer, String[] excludes,
            String datePattern) throws Exception {
        JsonConfig jsonConfig = configJson(excludes, datePattern);
        JSON json = JSONSerializer.toJSON(bean, jsonConfig);
        json.write(writer);
    }

    public static JSONObject fromObject(Object obj, String[] excludes,
            String datePattern) throws Exception {
        JsonConfig jsonConfig = configJson(excludes, datePattern);
        JSON json = JSONObject.fromObject(obj, jsonConfig);
        return (JSONObject) json;
    }

    /**
     * 配置json-lib需要的excludes和datePattern.
     *
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return JsonConfig 根据excludes和dataPattern生成的jsonConfig，用于write
     */
    public static JsonConfig configJson(String[] excludes, String datePattern) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(excludes);
        jsonConfig.setIgnoreDefaultExcludes(false);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor(datePattern));
        //        jsonConfig.registerJsonBeanProcessor(   
        //            org.hibernate.proxy.HibernateProxy.class,   
        //            new HibernateJsonBeanProcessor());   
        jsonConfig.setJsonBeanProcessorMatcher(new HibernateJsonBeanProcessorMatcher());

        return jsonConfig;
    }

    /**
     * data={"id":"1"}用json的数据创建指定的pojo.
     *
     * @param <T> Object
     * @param data json字符串
     * @param clazz 需要转换成bean的具体类型
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return T
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    public static <T extends Object> T json2Bean(String data, Class<T> clazz,
            String[] excludes, String datePattern) throws Exception {
        // JsonUtils.configJson(excludes, datePattern);
        T entity = clazz.newInstance();

        return json2Bean(data, entity, excludes, datePattern);
    }

    /**
     * data={"id":"1"}用json里的数据，填充指定的pojo.
     *
     * @param <T> Object
     * @param data json字符串
     * @param entity 需要填充数据的bean
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return T
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    public static <T extends Object> T json2Bean(String data, T entity,
            String[] excludes, String datePattern) throws Exception {
        // JsonUtils.configJson(excludes, datePattern);
        JSONObject jsonObject = JSONObject.fromObject(data);

        return json2Bean(jsonObject, entity, excludes, datePattern);
    }

    /**
     * 根据Class生成entity，再把JSONObject中的数据填充进去.
     *
     * @param <T> Object
     * @param jsonObject json对象
     * @param clazz 需要转换成bean的具体类型
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return T
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    public static <T extends Object> T json2Bean(JSONObject jsonObject,
            Class<T> clazz, String[] excludes, String datePattern)
            throws Exception {
        // JsonUtils.configJson(excludes, datePattern);
        T entity = clazz.newInstance();

        return json2Bean(jsonObject, entity, excludes, datePattern);
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象，形如：
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}}
     * @param object
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Object> T json2Bean(String jsonString, Class clazz) {
        JSONObject jsonObject = null;
        setDataFormat2JAVA();
        jsonObject = JSONObject.fromObject(jsonString);
        /*
         * 去除json提交时闯入 带点（parent.id）值 保证数据有效性
         */

        for (Iterator itor = jsonObject.keys(); itor.hasNext();) {
            String key = String.valueOf(itor.next());
            if (key.indexOf('.') > 0) {
                itor.remove();
            }
        }
        return (T) JSONObject.toBean(jsonObject, clazz);
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如：
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...},
     * beansList:[{}, {}, ...]}
     * @param jsonString
     * @param clazz
     * @param map 集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" : Bean.class)
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Object> T json2Bean(String jsonString,
            Class clazz, Map map) {
        JSONObject jsonObject = null;
        try {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
            /*
             * 去除json提交时闯入 带点（parent.id）值 保证数据有效性
             */
            for (Iterator itor = jsonObject.keys(); itor.hasNext();) {
                String key = String.valueOf(itor.next());
                if (key.indexOf('.') > 0) {
                    itor.remove();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (T) JSONObject.toBean(jsonObject, clazz, map);
    }

    /**
     * 把数据对象转换成json字符串
     * DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
     * 数组对象形如：[{}, {}, {}, ...]
     * map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...}
     * @param object
     * @return
     */
    public static String getJSONString(Object object) {
        String jsonString = null;
        //日期值处理器
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new JsonDateValueProcessor());
        if (object != null) {
            if (object instanceof Collection || object instanceof Object[]) {
                jsonString = JSONArray.fromObject(object, jsonConfig).toString();
            }
            else {
                jsonString = JSONObject.fromObject(object, jsonConfig).toString();
            }
        }
        return jsonString == null ? "{}" : jsonString;
    }

    /**
     * 把数据对象转换成json字符串
     * DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
     * 数组对象形如：[{}, {}, {}, ...]
     * map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...}
     * @param object
     * @return
     */
    public static String getJSONString(Object object, String[] excludes)
            throws Exception {
        String jsonString = null;
        //日期值处理器
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new JsonDateValueProcessor());
        jsonConfig.setExcludes(excludes);
        if (object != null) {
            if (object instanceof Collection || object instanceof Object[]) {
                jsonString = JSONArray.fromObject(object, jsonConfig).toString();
            }
            else {
                jsonString = JSONObject.fromObject(object, jsonConfig).toString();
            }
        }
        return jsonString == null ? "{}" : jsonString;
    }

    /*
     * 设置日期格式
     */
    private static void setDataFormat2JAVA() {
        //设定日期转换格式

        JSONUtils.getMorpherRegistry().registerMorpher(
                new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd" }));
        //JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd"}));
    }

    /**
     * 把JSONObject中的数据填充到entity中.
     *
     * @param <T> Object
     * @param jsonObject json对象
     * @param entity 需要填充数据的node
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return T
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Object> T json2Bean(JSONObject jsonObject,
            T entity, String[] excludes, String datePattern) throws Exception {
        // JsonUtils.configJson(excludes, datePattern);
        Set<String> excludeSet = new HashSet<String>();

        for (String exclude : excludes) {
            excludeSet.add(exclude);
        }

        for (Object object : jsonObject.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String propertyName = entry.getKey().toString();

            if (excludeSet.contains(propertyName)) {
                continue;
            }

            String propertyValue = entry.getValue().toString();

            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
                        propertyName, entity.getClass());
                Class propertyType = propertyDescriptor.getPropertyType();

                Method writeMethod = propertyDescriptor.getWriteMethod();

                if (propertyType == String.class) {
                    writeMethod.invoke(entity, propertyValue);
                }
                else if ((propertyType == Byte.class)
                        || (propertyType == byte.class)) {
                    writeMethod.invoke(entity, Byte.parseByte(propertyValue));
                }
                else if ((propertyType == Short.class)
                        || (propertyType == short.class)) {
                    writeMethod.invoke(entity, Short.parseShort(propertyValue));
                }
                else if ((propertyType == Integer.class)
                        || (propertyType == int.class)) {
                    writeMethod.invoke(entity, Integer.parseInt(propertyValue));
                }
                else if ((propertyType == Long.class)
                        || (propertyType == long.class)) {
                    writeMethod.invoke(entity, Long.parseLong(propertyValue));
                }
                else if ((propertyType == Float.class)
                        || (propertyType == float.class)) {
                    writeMethod.invoke(entity, Float.parseFloat(propertyValue));
                }
                else if ((propertyType == Double.class)
                        || (propertyType == double.class)) {
                    writeMethod.invoke(entity,
                            Double.parseDouble(propertyValue));
                }
                else if ((propertyType == Boolean.class)
                        || (propertyType == boolean.class)) {
                    writeMethod.invoke(entity,
                            Boolean.parseBoolean(propertyValue));
                }
                else if ((propertyType == Character.class)
                        || (propertyType == char.class)) {
                    writeMethod.invoke(entity, propertyValue.charAt(0));
                }
                else if (propertyType == Date.class) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            datePattern);
                    writeMethod.invoke(entity, dateFormat.parse(propertyValue));
                }
            }
            catch (IntrospectionException ex) {
                logger.error(ex);

                continue;
            }
        }

        return entity;
    }

    /**
     * data=[{"id":"1"},{"id":2}]用json里的数据，创建pojo队列.
     *
     * @param <T> Object
     * @param data json字符串
     * @param clazz 需要转换成node的具体类型
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return List
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    public static <T extends Object> List<T> json2List(String data,
            Class<T> clazz, String[] excludes, String datePattern)
            throws Exception {
        JSONArray jsonArray = JSONArray.fromObject(data);

        return json2List(jsonArray, clazz, excludes, datePattern);
    }

    /**
     * data=[{"id":"1"},{"id":2}]用json里的数据，创建pojo队列.
     *
     * @param <T> Object
     * @param jsonArray JSONArray
     * @param clazz 需要转换成node的具体类型
     * @param excludes 不需要转换的属性数组
     * @param datePattern 日期转换模式
     * @return List
     * @throws Exception java.lang.InstantiationException,
     *                   java.beans.IntrospectionException,
     *                   java.lang.IllegalAccessException
     */
    public static <T extends Object> List<T> json2List(JSONArray jsonArray,
            Class<T> clazz, String[] excludes, String datePattern)
            throws Exception {
        List<T> list = new ArrayList<T>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T node = json2Bean(jsonObject, clazz, excludes, datePattern);
            list.add(node);
        }

        return list;
    }

    /**
     * 
     * returnStringArray(传入json 字符串  返回对象数组) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param insurestr
     * @return  
     * Object[] 
     * @exception
     */
    public static Object[] returnObjArray(String insurestr) {
        JSONArray array = JSONArray.fromObject(insurestr);
        Object[] os = array.toArray();
        return os;
    }

    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        }
        else if (obj instanceof String || obj instanceof Integer
                || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double
                || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        }
        else if (obj instanceof Object[]) {
            json.append(array2json((Object[]) obj));
        }
        else if (obj instanceof List) {
            json.append(list2json((List<?>) obj));
        }
        else if (obj instanceof Map) {
            json.append(map2json((Map<?, ?>) obj));
        }
        else if (obj instanceof Set) {
            json.append(set2json((Set<?>) obj));
        }
        else {
            json.append(bean2json(obj));
        }
        return json.toString();
    }

    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        }
        catch (IntrospectionException e) {}
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = object2json(props[i].getName());
                    String value = object2json(props[i].getReadMethod().invoke(
                            bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                }
                catch (Exception e) {}
            }
            json.setCharAt(json.length() - 1, '}');
        }
        else {
            json.append("}");
        }
        return json.toString();
    }

    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        }
        else {
            json.append("]");
        }
        return json.toString();
    }

    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        }
        else {
            json.append("]");
        }
        return json.toString();
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        }
        else {
            json.append("}");
        }
        return json.toString();
    }

    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        }
        else {
            json.append("]");
        }
        return json.toString();
    }

    public static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                }
                else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    /**
     * @throws IOException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * 
     * jsonTobean(json 转为bean 解决json 中字符大写问题) 
     * @param jsonStr
     * @param clazz
     * @return  
     * T 
     * @exception
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T jsonTobeanBig(String jsonStr,
            Class<T> clazz) throws JsonParseException, JsonMappingException,
            InstantiationException, IllegalAccessException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(jsonStr, clazz.newInstance().getClass());
    }

    /**
     * 字符串大写
     * list2jsonBig(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param list
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException  
     * String 
     * @exception
     */
    public static String list2jsonBig(List<?> list)
            throws JsonGenerationException, JsonMappingException, IOException {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(new ObjectMapper().writeValueAsString(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        }
        else {
            json.append("]");
        }
        return json.toString();
    }
}
