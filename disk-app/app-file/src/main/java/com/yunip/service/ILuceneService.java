/*
 * 描述：〈全文检索〉
 * 创建人：can.du
 * 创建时间：2016-8-8
 */
package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.utils.lucene.Page;

/**
 * 全文检索
 */
public interface ILuceneService {

    /***
     * 全文检索
     * @param page
     * @return
     * @throws Exception  
     * Page<Object> 
     * @exception
     */
    @Transactional
    public Page<Object> getPageByQuery(Page<Object> page) throws Exception ;
}
