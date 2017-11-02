package com.yunip.model.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.SerialCode;
import com.yunip.utils.page.PageQuery;

/**
 * @author ming.zhu
 * 序列号查询对象
 */
@Alias("TSerialCodeQuery")
public class SerialCodeQuery extends PageQuery<SerialCode> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 4929453044368139597L;
    
    /** 序列号 **/
    private String            id;

    /** 父序列编号 **/
    private String            version;

    /** 类型（1.部门ID序列,2.部门版本控制,3.员工版本控制） **/
    private Integer           type;

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
