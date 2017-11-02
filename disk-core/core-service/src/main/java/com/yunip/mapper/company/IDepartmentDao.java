package com.yunip.mapper.company;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.company.Department;
import com.yunip.model.company.SimpleDeptData;
import com.yunip.model.company.query.DepartmentQuery;

/**
 * @author ming.zhu
 * 部门Dao
 */
@Repository("iDepartmentDao")
public interface IDepartmentDao extends IBaseDao<Department>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Department> selectByQuery(DepartmentQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(DepartmentQuery query);
    
    /***
     * 获取部门的简易输出对象
     * @return
     */
    List<SimpleDeptData> selectSimpleDatas();
}
