package com.yunip.config;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yunip.enums.LoginQrcodeStatus;
import com.yunip.enums.QrcodeEffectiveStatus;
import com.yunip.model.config.support.LoginQrcode;

public final class LoginQrcodeHelper {
    
    /** 失效分钟 **/
    private static int effectiveMinues = 3;

    /** Map形式配置信息（包含 isvalid = false 的数据）**/
    private static HashMap<String, LoginQrcode> loginMap = new HashMap<String, LoginQrcode>();
    
    /**
     * 禁止实例化
     */
    private LoginQrcodeHelper() {
        
    }
    
    /**
     * 扫描前端登录
     * @param qrcode
     * @return  
     * Integer 
     * @exception
     */
    public static Integer scanLoginQrcode(String qrcode){
        boolean b = isLoginQrcode(qrcode);
        if(b){
            LoginQrcode loginQrcode = getLoginQrcode(qrcode);
            return loginQrcode.getStatus();
        }else{
            addLoginMap(qrcode);
            return LoginQrcodeStatus.UNLOGIN.getStatus();
        }
    }
    
    /**
     * 判断失效性 ，有效返回true  失效返回false
     * @param date
     * @param loginQrcode
     * @return  
     * boolean 
     * @exception
     */
    public static boolean isEffective(Date date,String qrcode){
        LoginQrcode loginQrcode = getLoginQrcode(qrcode);
        if( loginQrcode != null && ( loginQrcode.getEffective() == QrcodeEffectiveStatus.UNEFFECTIVE.getStatus() || date.getTime() >= loginQrcode.getEffectiveTime().getTime()) ){
            loginQrcode.setEffective(QrcodeEffectiveStatus.UNEFFECTIVE.getStatus());
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * 失效二维码移除
     * @param qrcode  
     * void 
     * @exception
     */
    public static void unEffective(String qrcode){
        LoginQrcode loginQrcode = getLoginQrcode(qrcode);
        if( loginQrcode != null ){
            loginQrcode.setEffective(QrcodeEffectiveStatus.UNEFFECTIVE.getStatus());
        }
    }
    
    /**
     * 跳转登录获取token，并移除内存中的code
     * @param qrcode
     * @return  
     * Map<String,String> 
     * @exception
     */
    public static Map<String, String> getTokenAndIpMap(String qrcode){
        Map<String, String> map = new HashMap<String, String>();
        LoginQrcode loginQrcode = getLoginQrcode(qrcode);
        if(loginQrcode != null && loginQrcode.getToken() != null){
            map.put("ip", loginQrcode.getLoginIp());
            map.put("token", loginQrcode.getToken());
            //移除该qrcode
            loginMap.remove(qrcode);
        }
        return map;
    }
    
    public static LoginQrcode getLoginQrcode(String qrcode){
        return loginMap.get(qrcode);
    }
    
    /**
     * 二维码qrcode添加入内存
     * @param qrcode  
     * void 
     * @exception
     */
    private static void addLoginMap(String qrcode){
        //缓存清除
        clearLoginMap();
        //添加
        Calendar calendar = Calendar.getInstance(); 
        LoginQrcode loginQrcode = new LoginQrcode();
        loginQrcode.setCode(qrcode);
        loginQrcode.setCreateTime(calendar.getTime());
        calendar.add(Calendar.MINUTE, effectiveMinues);
        loginQrcode.setEffectiveTime(calendar.getTime());
        loginQrcode.setStatus(LoginQrcodeStatus.UNLOGIN.getStatus());
        loginQrcode.setEffective(QrcodeEffectiveStatus.EFFECTIVE.getStatus());
        loginMap.put(qrcode, loginQrcode);
    }
    
    /**
     * 内存清除控制
     * void 
     * @exception
     */
    private static void clearLoginMap(){
        Set<String> list = loginMap.keySet();
        if(list.size() > 500){
            for(String qrcode : list){
                LoginQrcode loginQrcode = loginMap.get(qrcode);
                if(loginQrcode.getEffective() == QrcodeEffectiveStatus.UNEFFECTIVE.getStatus() || (new Date()).getTime() >= loginQrcode.getEffectiveTime().getTime()){
                    loginMap.remove(qrcode);
                }
            }
        }
    }
    
    /**
     * 通过内存获取二维码
     * @param qrcode
     * @return  
     * boolean 
     * @exception
     */
    private static boolean isLoginQrcode(String qrcode){
        LoginQrcode loginQrcode = getLoginQrcode(qrcode);
        if(loginQrcode == null){
            return false;
        }
        return true;
    }
}
