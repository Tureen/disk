/*
 * 描述：〈文件上传类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;


/**
 * 文件类型
 */
public enum UploadType {
     
    /**取消上传**/
    CANCEL(1,"取消上传"),
    
    /**覆盖上传**/
    OVER(2,"覆盖上传"),
    
    /**升级版本**/
    UPVSERSION(3,"升级版本"),
    
    /**重命名**/
    RENAME(4,"重命名");
    
    private int type;
    
    private String desc;
    
    private UploadType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    
    /**
     * 根据文件类型的值获取该枚举类型
     * @param type 文件类型值
     */
    public static UploadType getUploadTypeByTypeValue(int type){
        for (UploadType bean : UploadType.values()) {
            if(bean.getType() == type){
                return bean;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
