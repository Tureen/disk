package com.yunip.mapper.company;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.company.Employee;
import com.yunip.model.company.SimpleEmpData;
import com.yunip.model.company.query.EmployeeQuery;

/**
 * @author ming.zhu
 * 员工Dao
 */
@Repository("iEmployeeDao")
public interface IEmployeeDao extends IBaseDao<Employee>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Employee> selectByQuery(EmployeeQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(EmployeeQuery query);
    
    /***
     * 获取员工的简易输出对象
     * @return
     */
    List<SimpleEmpData> selectSimpleDatas();
    
    /***
     * 根据查询条件查询对象
     * @param query
     * @return
     */
    Employee selectEmpByQuery(EmployeeQuery query);
}
