/*
 * 描述：〈描述〉
 * 创建人：pengfs
 * 创建时间：2013-2-21
 */
package com.yunip.utils.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Hibernate;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 一句话功能描述
 */
public class JsonDateValueProcessor
   implements JsonValueProcessor
 {
   private String format = "yyyy-MM-dd HH:mm:ss";
 
  public JsonDateValueProcessor()
  {
   }
 
   public JsonDateValueProcessor(String format) {
     this.format = format;
   }
 
   public Object processArrayValue(Object value, JsonConfig jsonConfig)
   {
     return process(value, jsonConfig);
   }
 
   public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
     return process(value, jsonConfig);
   }
 
   private Object process(Object value, JsonConfig jsonConfig) {
     if ((value instanceof Date)) {
       String str = new SimpleDateFormat(this.format).format((Date)value);
       return str;
     }
     if (value !=null && Hibernate.isInitialized(value)) {
       Hibernate.initialize(value);
     }
     return value == null ? null : value.toString();
   }
 
   public String getFormat() {
     return this.format;
   }
 
   public void setFormat(String format) {
     this.format = format;
   }
 }
