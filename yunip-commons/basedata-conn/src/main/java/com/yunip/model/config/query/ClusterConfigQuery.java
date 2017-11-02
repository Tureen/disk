/*
 * 描述：集群服务器配置信息查询实体类
 * 创建人：jian.xiong
 * 创建时间：2016-12-21
 */
package com.yunip.model.config.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.config.ClusterConfig;
import com.yunip.utils.page.PageQuery;

/**
 * 集群服务器配置信息查询实体类
 */
@Alias("TClusterConfigQuery")
public class ClusterConfigQuery extends PageQuery<ClusterConfig> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    
    /**
     * 名称
     */
    private String clusterName;
    
    /**
     * 编号
     */
    private String clusterCode;
    
    /**
     * ip地址
     */
    private String clusterIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterCode() {
        return clusterCode;
    }

    public void setClusterCode(String clusterCode) {
        this.clusterCode = clusterCode;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }
}
