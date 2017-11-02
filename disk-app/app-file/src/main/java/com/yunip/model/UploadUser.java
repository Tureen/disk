/*
 * 描述：文件上传用户实体类
 * 创建人：jian.xiong
 * 创建时间：2016-5-11
 */
package com.yunip.model;

import java.io.Serializable;

import com.yunip.constant.SystemContant;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.util.StringUtil;

/**
 * 文件上传用户实体类
 */
public class UploadUser implements Serializable{
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户身份标识ID（登录人ID）
     */
    private String identity;
    
    /**
     * 登录人名称
     */
    private String name;
    
    /**
     * token令牌（验证是否可以上传文件）
     */
    private String token;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * 获取加密后的identity
     */
    public String getEncryIdentity(){
        String identity = "";
        if(!StringUtil.nullOrBlank(this.getIdentity())){
            Des desObj = new Des();
            identity = desObj.strEnc(this.getIdentity(), SystemContant.desKey, null, null);
        }
        return identity;
    }
}
