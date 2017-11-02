/*
 * 描述：用户动态基本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.DynamicInfo;

@Repository("iDynamicInfoDao")
public interface IDynamicInfoDao extends IBaseDao<DynamicInfo>{

}
