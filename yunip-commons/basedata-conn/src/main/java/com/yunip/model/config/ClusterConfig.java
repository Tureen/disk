/*
 * 描述：集群服务器配置信息实体类
 * 创建人：jian.xiong
 * 创建时间：2016-12-21
 */
package com.yunip.model.config;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 集群服务器配置信息实体类
 */
@Alias("TClusterConfig")
public class ClusterConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer           id;

    /**
     * 名称
     */
    private String            clusterName;

    /**
     * 编号
     */
    private String            clusterCode;

    /**
     * ip地址
     */
    private String            clusterIp;

    /**
     * 文件磁盘上存储目录
     */
    private String            filePath;

    /**
     * 共享目录名
     */
    private String            sharePath;

    /**
     * 文件服务器访问地址
     */
    private String            fileUrl;

    /**
     * 备注
     */
    private String            remark;

    /**
     * 预留空间（MB）
     */
    private String            reserveSpace;

    /***********************************************************/

    /**
     * 剩余空间大小（Byte）
     */
    private long              freeSpace;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSharePath() {
        return sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getReserveSpace() {
        return reserveSpace;
    }

    public void setReserveSpace(String reserveSpace) {
        this.reserveSpace = reserveSpace;
    }
}
