/*
 * 描述：工作组Dao
 * 创建人：ming.zhu
 * 创建时间：2017-01-18
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.company.SimpleWorkgroupData;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.query.WorkgroupQuery;

@Repository("iWorkgroupDao")
public interface IWorkgroupDao extends IBaseDao<Workgroup>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Workgroup> selectByQuery(WorkgroupQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(WorkgroupQuery query);
    
    /**
     * 查找多条id根据所属员工id
     * @param id
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectByEmployee(Integer employeeId);
    
    /**
     * 查找所有开启的工作组
     */
    List<Workgroup> selectByAll();
    
    /**
     * 批量删除，根据employeeId和主键id
     * @param contact
     * @return  
     * int 
     * @exception
     */
    int batchDelete(Workgroup workgroup);
    
    /***
     * 获取工作组的简易输出对象
     * @return
     */
    List<SimpleWorkgroupData> selectSimpleDatas();
}
