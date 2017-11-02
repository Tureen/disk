/*
 * 描述：json数据返回格式实体类
 * 创建人：jian.xiong
 * 创建时间：2015-6-8
 */
package com.yunip.constant;

import java.io.Serializable;

/**
 * json数据返回格式实体类
 */
public class JsonData<T> implements Serializable {
    private static final long serialVersionUID = 7921975378883540770L;

    /**
     * 消息编号
     */
    private int               code  =  1000;

    /**
     * 消息描述
     */
    private String            codeInfo = "数据请求成功";

    /**
     * 消息数据项
     */
    private T                 result ;

    public JsonData() {

    }

    public JsonData(int code, String codeInfo) {
        this.code = code;
        this.codeInfo = codeInfo;
    }

    public JsonData(int code, String codeInfo, T result) {
        this.code = code;
        this.codeInfo = codeInfo;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
