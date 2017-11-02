package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.company.IDepartmentDao;
import com.yunip.model.company.Department;
import com.yunip.service.IDepartmentService;

/**
 * @author ming.zhu
 * 部门Service
 */
@Service("iDepartmentService")
public class DepartmentServiceImpl implements IDepartmentService{

    @Resource(name = "iDepartmentDao")
    private IDepartmentDao departmentDao;
    
    /**
     * 获取部门
     */
    public List<Department> getAllDeparts() {
        return departmentDao.selectByAll();
    }

    @Override
    public Department getDepartmentById(String deptId) {
        return departmentDao.selectById(deptId);
    }
}
