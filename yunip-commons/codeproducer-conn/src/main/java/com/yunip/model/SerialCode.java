package com.yunip.model;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ming.zhu
 * 序列编码配置表
 */
@Alias("TSerialCode")
public class SerialCode implements Serializable {
    /** 序列号 **/
    private String            id;

    /** 父序列编号 **/
    private String            version;

    /** 类型（1.部门ID序列,2.部门版本控制,3.员工版本控制） **/
    private Integer           type;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
