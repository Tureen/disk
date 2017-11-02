package com.yunip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.constant.MyException;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.company.CodeType;
import com.yunip.mapper.ISerialCodeDao;
import com.yunip.mapper.company.IDepartmentDao;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.model.SerialCode;
import com.yunip.model.company.Department;
import com.yunip.model.company.query.DepartmentQuery;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.query.SerialCodeQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.ISerialCodeService;
import com.yunip.utils.serial.SerialCodeUtil;
import com.yunip.utils.util.PinYinUtil;

/**
 * @author ming.zhu
 * 部门Service
 */
@Service("iDepartmentService")
public class DepartmentServiceImpl implements IDepartmentService{

    @Resource(name = "iDepartmentDao")
    private IDepartmentDao departmentDao;
    
    @Resource(name = "iSerialCodeDao")
    private ISerialCodeDao serialCodeDao;
    
    @Resource(name = "iEmployeeDao")
    private IEmployeeDao employeeDao;
    
    @Resource(name = "iSerialCodeService")
    private ISerialCodeService serialCodeService;
    
    /**
     * 添加部门
     */
    public Department addDepartment(Department department) {
        //第一步 获取当前级别下的最高序列号
        SerialCodeQuery query = new SerialCodeQuery();
        query.setType(CodeType.DEPT.getType());
        query.setVersion(department.getParentId());
        List<SerialCode> list = serialCodeDao.selectByQuery(query);
        SerialCode serialCode = null;
        if(list.size()>0 && list.get(0) != null){
            serialCode = list.get(0);
        }else{
            serialCode = new SerialCode();
            serialCode.setVersion(department.getParentId());
        }
        String deptId = SerialCodeUtil.getChildDeptId(serialCode.getId(),serialCode.getVersion());
        department.setId(deptId);
        String deptChar = PinYinUtil.getFirstInitWord(department.getDeptName()).toUpperCase();
        department.setDeptChar(deptChar);
        department.setCreateTime(new Date());
        departmentDao.insert(department);
        //添加序列号
        serialCode.setType(CodeType.DEPT.getType());
        serialCode.setId(deptId);
        serialCodeDao.insert(serialCode);
        //更新部门版本号
        serialCodeService.updateVersion(CodeType.DVERSION.getType());
        return department;
    }

    /**
     * 修改部门
     */
    public int updateDepartment(Department department) {
        String deptChar = PinYinUtil.getFirstInitWord(department.getDeptName()).toUpperCase();
        department.setDeptChar(deptChar);
        department.setUpdateTime(new Date());
        //更新部门版本号
        serialCodeService.updateVersion(CodeType.DVERSION.getType());
        return departmentDao.update(department);
    }

    /**
     * 获取部门
     */
    public List<Department> getAllDeparts() {
        return departmentDao.selectByAll();
    }
    
    /**
     * 删除部门
     */
    public int delDepartment(String deptId) {
        //第一步检查当前部门下是否存在部门
        DepartmentQuery query = new DepartmentQuery();
        query.setParentId(deptId);
        int count = departmentDao.selectCountByQuery(query);
        if(count > 0 ){
            throw new MyException(AdminException.ZCZBM);
        }
        //第二步检查部门下是否存在员工
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setDeptId(deptId);
        int empCount = employeeDao.selectCountByQuery(employeeQuery);
        if(empCount > 0 ){
            throw new MyException(AdminException.BMCZYG);
        }
        //更新部门版本号
        serialCodeService.updateVersion(CodeType.DVERSION.getType());
        return departmentDao.delById(deptId); 
    }

    public Department getDepartmentById(String deptId) {
        return departmentDao.selectById(deptId);
    }

    /**
     * 根据条件获取部门列表
     */
    public DepartmentQuery queryDepartment(DepartmentQuery query) {
        List<Department> list = departmentDao.selectByQuery(query);
        int count = departmentDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }
}
