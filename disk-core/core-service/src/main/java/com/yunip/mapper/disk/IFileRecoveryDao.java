/*
 * 描述：文件回收信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.FileRecovery;

@Repository("iFileRecoveryDao")
public interface IFileRecoveryDao extends IBaseDao<FileRecovery>{

}
