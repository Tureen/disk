/*
 * 描述：〈重命名辅助类〉
 * 创建人：can.du
 * 创建时间：2016-5-21
 */
package com.yunip.model.disk.support;

import java.io.Serializable;

/**
 * 重命名辅助类
 */
public class RenameHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**名称**/
    private String            name;

    /**类型1.文件夹2文件**/
    private int               openType;

    /**操作对象的ID**/
    private int               openId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }
}
