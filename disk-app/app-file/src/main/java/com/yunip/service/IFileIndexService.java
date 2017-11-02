/*
 * 描述：〈全文检索操作Service〉
 * 创建人：can.du
 * 创建时间：2016-8-8
 */
package com.yunip.service;

import org.springframework.stereotype.Service;

/**
 * 全文检索操作Service
 */
@Service("iFileIndexService")
public interface IFileIndexService {
    
    /****
     * 检测表数据生成文件索引
     * void 
     * @exception
     */
    void createFileIndex() throws Exception;
}
