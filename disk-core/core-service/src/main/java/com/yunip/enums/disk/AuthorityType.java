/*

 * 描述：〈权限枚举类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 权限枚举类型
 */
public enum AuthorityType {
    
    /**管理权限**/
    MANAGER(1,"管理",new Authority[]{Authority.READ,Authority.DOWN,Authority.COPY,Authority.CREATE,Authority.UPLOAD,Authority.UPDATE}),
    
    /**查看权限**/
    SEE(2,"查看",new Authority[]{Authority.READ,Authority.DOWN,Authority.COPY}),
    
    /**预览权限**/
    READ(3,"预览",new Authority[]{Authority.READ});

    private int code;
    
    private String desc;
    
    private Authority[] authorities;
    
    private AuthorityType(int code,String desc, Authority[] authorities) {
        this.code = code;
        this.desc = desc;
        this.authorities = authorities;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Authority[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authority[] authorities) {
        this.authorities = authorities;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static AuthorityType getAuthTypeByAuth(int auth){
        for(AuthorityType authorityType : AuthorityType.values()){
            if(authorityType.getCode() == auth){
                return authorityType;
            }
        }
        return null;
    }
}
