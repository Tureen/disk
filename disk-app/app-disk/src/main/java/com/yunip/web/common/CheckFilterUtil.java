package com.yunip.web.common;

import javax.servlet.http.HttpServletRequest;

import jcifs.smb.NtlmPasswordAuthentication;

import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.utils.pwd.Des;

public class CheckFilterUtil {
    
    /**不检查session的路径**/
    static String[] noCheckPaths = null;
    
    /**
     * isVerification(判断指定的url是否需要验证，默认是需要验证的) 
     * @param path
     * @return  false代表不用验证  true 代表需要进行验证
     * Boolean 
     * @exception
     */
    public static Boolean isVerificationJs(String path){        
        //默认需要验证url
        boolean isVerification = true;
        //view包下的文件不需要验证 
        if (path.contains("/static") || path.contains("/plugins")) {
            isVerification = false;
        }
        return isVerification;
    }
    
    /**
     * isVerification(判断指定的url是否需要验证，默认是需要验证的) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param path
     * @return  false代表不用验证  true 代表需要进行验证
     * Boolean 
     * @exception
     */
    public static Boolean isVerification(String path){        
        //默认需要验证url
        boolean isVerification = true;
        for (String p : noCheckPaths) {
            if (path.indexOf(p) == 0) {
                isVerification = false;
                break;
            }
        }
        //view包下的文件不需要验证 
        if (path.indexOf("/view/") == 0 && (path.indexOf(".js") > 0 || path.indexOf(".js?") > 0 )) {
            isVerification = false;
        }
        return isVerification;
    }

    public static void checkNameSpace(String path, HttpServletRequest request){
        String[] namespaces = SysContant.NAME_SPACES;
        for(String namespace : namespaces){
            if(path.contains(namespace)){
                request.setAttribute("namespace", namespace);
                break;
            }
        }
        
    }
    
    public static String desEnc(String data){
        Des desObj = new Des();
        data = data.replace("\"", "\\\"");
        //解密
        String b = desObj.strDec(data.substring(10, 378), SystemContant.CHECKFILTERKEY, null, null);
        String b2 = desObj.strDec(data.substring(1178, 1578), SystemContant.CHECKFILTERKEY, null, null);
        String b3 = desObj.strDec(data.substring(2278, 2886), SystemContant.CHECKFILTERKEY, null, null);
        String b4 = desObj.strDec(data.substring(3536, 3936), SystemContant.CHECKFILTERKEY, null, null);
        String b5 = desObj.strDec(data.substring(4436, 4836), SystemContant.CHECKFILTERKEY, null, null);
        data = data.substring(0, 10) + b + data.substring(378,1178) + b2 + data.substring(1578,2278) + b3 + data.substring(2886,3536) + b4 + data.substring(3936,4436) + b5 + data.substring(4836);
        data = data.replace("\\\"", "\"");
        return data;
    }
    
    public static NtlmHttpServletRequest regetNum(HttpServletRequest request, NtlmPasswordAuthentication ntlm){
        return new NtlmHttpServletRequest(request, ntlm);
    }
}
