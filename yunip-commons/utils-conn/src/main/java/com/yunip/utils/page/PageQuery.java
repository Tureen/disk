/*
 * 描述：分页查询模型类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-14
 */
package com.yunip.utils.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询模型类
 */
public class PageQuery<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 每页记录条数，默认20 **/
    private int    pageSize = 15;

    /** 页码索引，默认为1 **/
    private int    pageIndex=1;

    /** 总记录条数 **/
    private int  recordCount;

    /** 排序语句 **/
    private String orderby;
    /** 页码索引，默认为1 **/
    private boolean pageFlg = true;
    
    private boolean desc = false;
    
    /**放置查询返回的LIST**/
    private List<T> list;
    
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if (pageIndex > 0) {
            this.pageIndex = pageIndex;
        }
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        if (recordCount < 1) {
            return 0;
        }
        if (recordCount % pageSize > 0) {
            return recordCount / pageSize + 1;
        } else {
            return recordCount / pageSize;
        }
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public int getPageStart() {
        return (this.pageIndex - 1) * this.pageSize;
    }

    public int getPageend() {
        return this.pageIndex * this.pageSize;
    }

    /** 
     * 获得 pageFlg 
     * @return  the pageFlg 
    */ 
    
    public boolean isPageFlg() {
        return pageFlg;
    }

    /** 
     * @param pageFlg the pageFlg to set 
     */
    public void setPageFlg(boolean pageFlg) {
        this.pageFlg = pageFlg;
        //不分页，但有些是轮询分批 获取信息的。为了不调用查询总数， 囧~~
        if(!pageFlg &&  pageSize == 15){
            pageSize=Integer.MAX_VALUE;
        }
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
