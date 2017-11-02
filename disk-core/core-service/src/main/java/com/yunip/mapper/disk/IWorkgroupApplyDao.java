/*
 * 描述：工作组申请Dao
 * 创建人：ming.zhu
 * 创建时间：2017-02-04
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.WorkgroupApply;
import com.yunip.model.disk.query.WorkgroupApplyQuery;

@Repository("iWorkgroupApplyDao")
public interface IWorkgroupApplyDao extends IBaseDao<WorkgroupApply> {

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<WorkgroupApply> selectByQuery(WorkgroupApplyQuery query);

    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(WorkgroupApplyQuery query);

}
