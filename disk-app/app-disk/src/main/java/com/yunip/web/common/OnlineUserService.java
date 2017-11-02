/*
 * 描述：登录成功的在线用户处理类
 * 创建人：jian.xiong
 * 创建时间：2016-11-3
 */
package com.yunip.web.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功的在线用户处理类
 */
public class OnlineUserService {
    /**
     * 已登录成功的在线用户
     */
    private static Map<String, String> onlineUserMap = new HashMap<String, String>();
    
    /**
     * 添加已登录成功的在线用户
     * @param sessionId 登录会话ID
     * @param userId 用户ID
     */
    public static void addOnlineUser(String userId, String sessionId){
        onlineUserMap.put(userId, sessionId);
    }
    
    /**
     * 根据登录会话sessionId获取当前userId
     * @param sessionId 登录会话ID
     */
    public static String getOnlineUserBySessionId(String sessionId){
        for(Map.Entry<String, String> map : onlineUserMap.entrySet()){
            String key = map.getKey();
            String value = map.getValue();
            if(value.equals(sessionId)){
                return key;
            }
        }
        return null;
    }
    
    /**
     * 根据登录会话userId获取当前user对应的sessionId
     * @param userId 用户ID
     */
    public static String getOnlineUserSessionIdByUserId(String userId){
        return onlineUserMap.get(userId);
    }
}
