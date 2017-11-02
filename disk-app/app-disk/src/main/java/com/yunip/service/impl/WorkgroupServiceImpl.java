package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.enums.disk.DiskException;
import com.yunip.mapper.disk.IWorkgroupDao;
import com.yunip.mapper.disk.IWorkgroupEmployeeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.disk.query.WorkgroupQuery;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.IMessageService;
import com.yunip.service.IWorkgroupService;

@Service("iWorkgroupService")
public class WorkgroupServiceImpl implements IWorkgroupService {

    @Resource(name = "iWorkgroupDao")
    private IWorkgroupDao workgroupDao;
    
    @Resource(name = "iWorkgroupEmployeeDao")
    private IWorkgroupEmployeeDao workgroupEmployeeDao;
    
    @Resource(name = "iMessageService")
    private IMessageService messageService;
    
    @Resource(name = "iAuthorityShareService")
    private IAuthorityShareService authorityShareService;

    @Override
    public WorkgroupQuery queryWorkgroup(WorkgroupQuery query) {
        List<Workgroup> list = workgroupDao.selectByQuery(query);
        int count = workgroupDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }
    
    @Override
    public int quertCount(WorkgroupQuery query) {
        return workgroupDao.selectCountByQuery(query);
    }

    @Override
    @Transactional
    public int addWorkgroup(Workgroup workgroup) {
        return workgroupDao.insert(workgroup);
    }

    @Override
    @Transactional
    public int upWorkgroup(Workgroup workgroup) {
        return workgroupDao.update(workgroup);
    }

    @Override
    @Transactional
    public int delWorkgroup(Integer workgroupId) {
        return workgroupDao.delById(workgroupId);
    }

    @Override
    public List<Integer> getJoinWorkgroup(Integer employeeId) {
        return workgroupEmployeeDao.selectByEmployeeId(employeeId);
    }

    @Override
    public List<Integer> getHasWorkgroup(Integer employeeId) {
        return workgroupDao.selectByEmployee(employeeId);
    }

    @Override
    @Transactional
    public int saveOrEdit(Workgroup workgroup,Employee employee) {
        if(workgroup != null){
            if(workgroup.getId() != null && workgroup.getId() > 0){
                //修改
                workgroup.setUpdateAdmin(employee.getEmployeeName());
                workgroup.setUpdateTime(new Date());
                workgroupDao.update(workgroup);
            }else{
                //保存
                workgroup.setEmployeeId(employee.getId());
                workgroup.setCreateAdmin(employee.getEmployeeName());
                workgroup.setUpdateAdmin(employee.getEmployeeName());
                workgroupDao.insert(workgroup);
                //将创建人加入该组
                WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
                workgroupEmployee.setEmployeeId(employee.getId());
                workgroupEmployee.setWorkgroupId(workgroup.getId());
                workgroupEmployeeDao.insert(workgroupEmployee);
            }
        }
        return 0;
    }

    @Override
    public Workgroup getWorkgroup(Integer workgroupId) {
        return workgroupDao.selectById(workgroupId);
    }
    
    @Override
    @Transactional
    public boolean delBatchWorkgroup(List<String> ids, Employee employee) {
        if(ids == null || ids.size() <= 0){
            return false;
        }else{
            List<Integer> workgroupIds = new ArrayList<Integer>();
            for(String id : ids){
                Integer workgroupId = Integer.valueOf(id);
                //发送消息
                messageService.sendWorkgroupDeleteMessage(employee, workgroupId);
                workgroupIds.add(Integer.valueOf(id));
                //删除工作组关联员工
                workgroupEmployeeDao.delByWorkgroupId(workgroupId);
                //删除工作组相关分享记录
                authorityShareService.delWorkgroupAuthorityShare(workgroupId);
            }
            //批量删除工作组
            Workgroup workgroup = new Workgroup();
            workgroup.setIds(workgroupIds);
            workgroup.setEmployeeId(employee.getId());
            workgroupDao.batchDelete(workgroup);
            
            return  true;
        }
    }

    @Override
    public int saveWorkgroupEmployee(Integer workgroupId,
            Employee employee) {
        WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
        workgroupEmployee.setWorkgroupId(workgroupId);
        workgroupEmployee.setEmployeeId(employee.getId());
        return workgroupEmployeeDao.insert(workgroupEmployee);
    }

    @Override
    public boolean saveBatchWorkgroupEmployee(Integer workgroupId, List<String> ids, Employee employee) {
        if(!ids.contains(""+employee.getId())){
            throw new MyException(DiskException.WORKGROUP_CREATEADMIN_SAME);
        }
        if(ids != null && ids.size() > 0){
            //获取该工作组下所有关联
            List<WorkgroupEmployee> workgroupEmployees = workgroupEmployeeDao.selectByWorkgroupId(workgroupId);
            //判断ids中，哪些是新增id，以及现有ids中哪些是移除id
            List<String> haveIds = new ArrayList<String>();//现有关联集合
            if(workgroupEmployees != null && workgroupEmployees.size() > 0){
                for(WorkgroupEmployee workgroupEmployee : workgroupEmployees){
                    haveIds.add(""+workgroupEmployee.getEmployeeId());
                }
            }
            //需要移除的id集合
            List<String> removeIds = new ArrayList<String>();
            removeIds.addAll(haveIds);
            removeIds.removeAll(ids);
            //需要添加的id集合
            List<String> addIds = new ArrayList<String>();
            addIds.addAll(ids);
            addIds.removeAll(haveIds);
            //先清除该工作组关联的所有员工
            workgroupEmployeeDao.delByWorkgroupId(workgroupId);
            //再循环添加
            for(String employeeId : ids){
                WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
                workgroupEmployee.setWorkgroupId(workgroupId);
                workgroupEmployee.setEmployeeId(Integer.valueOf(employeeId));
                workgroupEmployeeDao.insert(workgroupEmployee);
            }
            //发送消息
            messageService.sendWorkgroupSaveBatchMessage(employee, workgroupId, addIds, removeIds);
            return true;
        }
        return false;
    }

    @Override
    public List<WorkgroupEmployee> getWorkgroupEmployeeIds(Integer workgroupId) {
        return workgroupEmployeeDao.selectByWorkgroupId(workgroupId);
    }

    @Override
    public List<WorkgroupEmployee> getWorkgroupEmployee(Integer workgroupId) {
        return workgroupEmployeeDao.selectEmployeeByWorkgroupId(workgroupId);
    }

    @Override
    @Transactional
    public int transferWorkgroup(Integer workgroupId, Employee employee, Employee bEmployee) {
        Workgroup workgroup = new Workgroup();
        workgroup.setId(workgroupId);
        workgroup.setCreateAdmin(bEmployee.getEmployeeName());
        workgroup.setEmployeeId(bEmployee.getId());
        workgroupDao.update(workgroup);
        //将被转让者加入该工作组
        WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
        workgroupEmployee.setWorkgroupId(workgroupId);
        workgroupEmployee.setEmployeeId(bEmployee.getId());
        WorkgroupEmployee newWorkgroupEmployee =  workgroupEmployeeDao.selectByPrimaryKey(workgroupEmployee);
        //如果不存在关联，则添加
        if(newWorkgroupEmployee == null){
            workgroupEmployeeDao.insert(workgroupEmployee);
        }
        //发送消息
        messageService.sendWorkgroupTransferMessage(employee, bEmployee, workgroupId);
        return 1;
    }

    @Override
    public List<Workgroup> getAllWorkgroup() {
        return workgroupDao.selectByAll();
    }

}
