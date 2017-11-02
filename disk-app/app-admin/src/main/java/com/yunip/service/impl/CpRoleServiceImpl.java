package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.enums.company.CpRoleType;
import com.yunip.mapper.authority.ICpPermissionDao;
import com.yunip.mapper.authority.ICpRoleDao;
import com.yunip.mapper.authority.ICpRolePermissionDao;
import com.yunip.mapper.authority.IEmployeeRoleDao;
import com.yunip.model.authority.CpPermission;
import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.CpRolePermission;
import com.yunip.model.authority.EmployeeRole;
import com.yunip.model.authority.query.CpRoleQuery;
import com.yunip.model.company.Employee;
import com.yunip.service.ICpRoleService;

/**
 * @author ming.zhu
 * 员工权限业务层
 */
@Service("iCpRoleService")
public class CpRoleServiceImpl implements ICpRoleService{

    @Resource(name = "iCpPermissionDao")
    private ICpPermissionDao cpPermissionDao;
    
    @Resource(name = "iCpRoleDao")
    private ICpRoleDao cpRoleDao;
    
    @Resource(name = "iCpRolePermissionDao")
    private ICpRolePermissionDao cpRolePermissionDao;
    
    @Resource(name = "iEmployeeRoleDao")
    private IEmployeeRoleDao employeeRoleDao;
    
    @Override
    public CpRoleQuery queryCpRole(CpRoleQuery query) {
        List<CpRole> list = cpRoleDao.selectByQuery(query);
        int count = cpRoleDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    public CpRole getCpRole(Integer roleId) {
        return cpRoleDao.selectById(roleId);
    }

    @Override
    public List<CpRolePermission> getCpRolePermission(Integer roleId) {
        List<CpRolePermission> rolePermissions = cpRolePermissionDao.selectByRoleId(roleId);
        return rolePermissions;
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(CpRole role) {
        int result = 0;
        if (role.getId() == null) {
            role.setValidStatus(1);
            result = cpRoleDao.insert(role);
        } else {
            //相关权限全部删除再添加
            cpRolePermissionDao.delByRoleId(role.getId());
            result = cpRoleDao.update(role);
        }
        if(role.getCpPermissions() != null && role.getCpPermissions().size()>0){
            for(CpPermission per : role.getCpPermissions()){
                if(per.getId()==null){
                    continue;
                }
                CpRolePermission rp = new CpRolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(per.getId());
                cpRolePermissionDao.insert(rp);
            }
        }
        return result > 0;
    }

    @Override
    @Transactional
    public void updateValidStatus(Integer roleId, int validStatus) {
        CpRole role = new CpRole();
        role.setId(roleId);
        role.setValidStatus(validStatus);
        cpRoleDao.update(role);
    }

    @Override
    @Transactional
    public int deleteRole(Integer roleId) {
        //TODO 判断是否有用户引用该角色
        List<EmployeeRole> employeeRoles = employeeRoleDao.selectByRoleId(roleId);
        if(employeeRoles.size() > 0){
            return 1;
        }
        cpRoleDao.delById(roleId);
        cpRolePermissionDao.delByRoleId(roleId);
        return 0;
    }

    @Override
    @Transactional
    public void saveEmployeeRoles(int roleId,List<Employee> employees,List<Employee> allEmployees) {
        if(allEmployees != null && allEmployees.size() > 0){
            for(Employee employee : allEmployees){
                if(employee.getId() != null){
                    //先删除本页面中展示的所有员工的该角色关联
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setRoleId(roleId);
                    employeeRole.setEmployeeId(employee.getId());
                    employeeRoleDao.delByPrimaryKey(employeeRole);
                }
            }
        }
        if(employees != null && employees.size() > 0){
            for(Employee employee : employees){
                if(employee.getId() != null){
                    //再添加现有关联
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setRoleId(roleId);
                    employeeRole.setEmployeeId(employee.getId());
                    employeeRoleDao.insert(employeeRole);
                }
            }
        }
    }

    @Override
    public String getEmployeeIdStrByRoleId(int roleId) {
        List<EmployeeRole> employeeRoles = employeeRoleDao.selectByRoleId(roleId);
        StringBuffer strb = new StringBuffer();
        if(employeeRoles != null && employeeRoles.size() > 0){
            for(EmployeeRole employeeRole : employeeRoles){
                strb.append(employeeRole.getEmployeeId()+",");
            }
            if(strb.length()>0){
                strb.deleteCharAt(strb.length()-1);
            }
        }
        return strb.toString();
    }

    @Override
    public String getPermissionIdStrByEmployeeId(int employeeId) {
        //定义角色id集合
        List<Integer> idList = new ArrayList<Integer>();
        //查询员工对应角色包
        List<EmployeeRole> employeeRoles = employeeRoleDao.selectByEmployeeId(employeeId);
        if(employeeRoles != null && employeeRoles.size() > 0){
            for(EmployeeRole employeeRole : employeeRoles){
                int roleId = employeeRole.getRoleId();
                CpRole role = cpRoleDao.selectById(roleId);
                //r1:如果该角色为专属个人的角色，并且没有其他角色，那么该员工的权限只与该角色有关
                if(employeeRoles.size() == 1 && role.getRoleType().equals(CpRoleType.EXCLUSIVEROLE.getType())){
                    StringBuffer strb = new StringBuffer();
                    //查询该角色权限集合，并拼接
                    List<CpRolePermission> cpRolePermissions = cpRolePermissionDao.selectByRoleId(roleId);
                    if(cpRolePermissions != null && cpRolePermissions.size() > 0){
                        for(CpRolePermission cpRolePermission : cpRolePermissions){
                            strb.append(cpRolePermission.getPermissionId()+",");
                        }
                        if(strb.length()>0){
                            strb.deleteCharAt(strb.length()-1);
                        }
                    }
                    return strb.toString();
                }else if(role.getRoleType().equals(CpRoleType.DEFAULTROLE.getType())){
                    //r2:若果不是，将该角色id加入Integer集合，在下面的代码下统一查询权限
                    idList.add(roleId);
                }
            }
            //r2:对角色id集合遍历，找出不重复的权限id并拼接
            if(idList != null && idList.size() > 0){
                CpRolePermission cpRolePermissionTmp = new CpRolePermission();
                cpRolePermissionTmp.setRoleIds(idList);
                List<CpRolePermission> cpRolePermissions = cpRolePermissionDao.selectByRoleIds(cpRolePermissionTmp);
                //拼接
                StringBuffer strb = new StringBuffer();
                if(cpRolePermissions != null && cpRolePermissions.size() > 0){
                    for(CpRolePermission cpRolePermission : cpRolePermissions){
                        strb.append(cpRolePermission.getPermissionId()+",");
                    }
                    if(strb.length()>0){
                        strb.deleteCharAt(strb.length()-1);
                    }
                }
                return strb.toString();
            }
        }
        return "";
    }
}
