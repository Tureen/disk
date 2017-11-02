/*
 * 描述：OSS区域对应访问域名枚举类(来自：https://help.aliyun.com/document_detail/31837.html?spm=5176.doc32012.6.157.dhutXa)
 * 创建人：jian.xiong
 * 创建时间：2016-9-9
 */
package com.yunip.oss.config;

import java.util.ArrayList;
import java.util.List;

/**
 * OSS区域对应访问域名枚举类
 */
public enum EnumRegionDomain {
    
    /**
     * 华东 1 (杭州)
     */
    hangzhou("华东 1 (杭州)", "oss-cn-hangzhou", "oss-cn-hangzhou.aliyuncs.com", "oss-cn-hangzhou-internal.aliyuncs.com"),
    
    /**
     * 华东 2 (上海)
     */
    shanghai("华东 2 (上海)", "oss-cn-shanghai", "oss-cn-shanghai.aliyuncs.com", "oss-cn-shanghai-internal.aliyuncs.com"),
    
    /**
     * 华北 1 (青岛)
     */
    qingdao("华北 1 (青岛)", "oss-cn-qingdao", "oss-cn-qingdao.aliyuncs.com", "oss-cn-qingdao-internal.aliyuncs.com"),
    
    /**
     * 华北 2 (北京)
     */
    beijing("华北 2 (北京)", "oss-cn-beijing", "oss-cn-beijing.aliyuncs.com", "oss-cn-beijing-internal.aliyuncs.com"),
    
    /**
     * 华南 1 (深圳)
     */
    shenzhen("华南 1 (深圳)", "oss-cn-shenzhen", "oss-cn-shenzhen.aliyuncs.com", "oss-cn-shenzhen-internal.aliyuncs.com"),
    
    /**
     * 香港数据中心
     */
    hongkong("香港数据中心", "oss-cn-hongkong", "oss-cn-hongkong.aliyuncs.com", "oss-cn-hongkong-internal.aliyuncs.com"),
    
    /**
     * 美国硅谷数据中心
     */
    west1("美国硅谷数据中心", "oss-us-west-1", "oss-us-west-1.aliyuncs.com", "oss-us-west-1-internal.aliyuncs.com"),
    
    /**
     * 美国弗吉尼亚数据中心
     */
    east1("美国弗吉尼亚数据中心", "oss-us-east-1", "oss-us-east-1.aliyuncs.com", "oss-us-east-1-internal.aliyuncs.com"),
    
    /**
     * 亚太（新加坡）数据中心
     */
    southeast1("亚太（新加坡）数据中心", "oss-ap-southeast-1", "oss-ap-southeast-1.aliyuncs.com", "oss-ap-southeast-1-internal.aliyuncs.com");
    
    /**
     * 中文名称
     */
    private String chineseName;
    
    /**
     * 英文名称
     */
    private String englishName;
    
    /**
     * 外网Endpoint
     */
    private String extranetEndpoint;
    
    /**
     * ECS访问的内网Endpoint
     */
    private String intranetEndpoint;
    
    private EnumRegionDomain(String chineseName, String englishName, String extranetEndpoint, String intranetEndpoint){
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.extranetEndpoint = extranetEndpoint;
        this.intranetEndpoint = intranetEndpoint;
    }
    
    /**
     * 获得所有的访问域名列表
     * @return 返回包含所有访问域名的list集合
     */
    public static List<EnumRegionDomain> getAllRegionDomainList(){
        List<EnumRegionDomain> list = new ArrayList<EnumRegionDomain>();
        for(EnumRegionDomain bean : EnumRegionDomain.values()){
            list.add(bean);
        }
        return list;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public void setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public void setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
    }
}
