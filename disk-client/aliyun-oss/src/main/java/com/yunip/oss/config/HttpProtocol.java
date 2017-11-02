/*
 * 描述：传输协议枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-9-19
 */
package com.yunip.oss.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 传输协议枚举类
 */
public enum HttpProtocol {
    /**
     * http协议
     */
    HTTP("http://", "http协议"),
    
    /**
     * https协议
     */
    HTTPS("https://", "https协议");
    
    /**
     * 传输协议
     */
    private String protocol;
    
    /**
     * 描述
     */
    private String describe;
    
    private HttpProtocol(String protocol, String describe){
        this.protocol = protocol;
        this.describe = describe;
    }
    
    /**
     * 获得所有的传输协议列表
     * @return 返回包含所有传输协议的list集合
     */
    public static List<HttpProtocol> getAllProtocalList(){
        List<HttpProtocol> list = new ArrayList<HttpProtocol>();
        for(HttpProtocol bean : HttpProtocol.values()){
            list.add(bean);
        }
        return list;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
