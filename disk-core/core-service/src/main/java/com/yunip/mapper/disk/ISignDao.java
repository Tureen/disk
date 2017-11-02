/*
 * 描述：文件标签信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.Sign;
import com.yunip.model.disk.query.SignQuery;

@Repository("iSignDao")
public interface ISignDao extends IBaseDao<Sign>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Sign> selectByQuery(SignQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(SignQuery query);
    
    /**
     * 根据员工id查找标签集合
     * @param employeeId
     * @return  
     * List<Sign> 
     * @exception
     */
    List<Sign> selectByEmployeeId(Integer employeeId);
    
    /**
     * 根据文件id查询标签集合
     * @param fileId
     * @return  
     * List<Sign> 
     * @exception
     */
    List<Sign> selectByFileId(Integer fileId);
}
