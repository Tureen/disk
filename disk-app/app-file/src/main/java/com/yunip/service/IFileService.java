/*
 * 描述：〈文件处理的接口类〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.service;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;

/**
 * 文件处理的接口类
 */
public interface IFileService {

    /***
     * 解压文件(目前支持ZIP和RAR)
     * @param file      解压的文件对象
     * @param employee  操作的用户对象
     * @param taskName  任务名称
     * void 
     * @exception
     */
    void decompressFile(File file, Employee employee, String taskName);
}
