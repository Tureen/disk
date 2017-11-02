/*
 * 描述：文件索引操作信息表Dao
 * 创建人：ming.zhu
 * 创建时间：2016-8-5
 */
package com.yunip.mapper.disk;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.FileIndex;

@Repository("iFileIndexDao")
public interface IFileIndexDao extends IBaseDao<FileIndex>{
    /**
     * 根据文件ID修改文件路径
     * @param bean  
     */
    void updateFilePathByFileId(FileIndex bean);
}
