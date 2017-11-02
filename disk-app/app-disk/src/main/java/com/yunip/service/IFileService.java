/*
 * 描述：文件业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-21
 */
package com.yunip.service;

import com.yunip.model.disk.File;

public interface IFileService {

    /**
     * 根据id获取file对象
     * getFileById(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param id
     * @return  
     * File 
     * @exception
     */
    File getFileById(Integer id);
}
