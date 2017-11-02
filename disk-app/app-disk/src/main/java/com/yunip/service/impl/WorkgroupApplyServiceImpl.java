package com.yunip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.WorkgroupApplyStatus;
import com.yunip.mapper.disk.IWorkgroupApplyDao;
import com.yunip.mapper.disk.IWorkgroupDao;
import com.yunip.mapper.disk.IWorkgroupEmployeeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.WorkgroupApply;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.disk.query.WorkgroupApplyQuery;
import com.yunip.service.IMessageService;
import com.yunip.service.IWorkgroupApplyService;

@Service("iWorkgroupApplyService")
public class WorkgroupApplyServiceImpl implements IWorkgroupApplyService {

    @Resource(name = "iWorkgroupDao")
    private IWorkgroupDao workgroupDao;
    
    @Resource(name = "iWorkgroupEmployeeDao")
    private IWorkgroupEmployeeDao workgroupEmployeeDao;
    
    @Resource(name = "iWorkgroupApplyDao")
    private IWorkgroupApplyDao workgroupApplyDao;
    
    @Resource(name = "iMessageService")
    private IMessageService messageService;

    @Override
    public WorkgroupApplyQuery queryWorkgroupApply(WorkgroupApplyQuery query) {
        List<WorkgroupApply> list = workgroupApplyDao.selectByQuery(query);
        int count = workgroupApplyDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }
    
    @Override
    public int queryCount(WorkgroupApplyQuery query) {
        return workgroupApplyDao.selectCountByQuery(query);
    }

    @Override
    @Transactional
    public int saveWorkgroupApply(Employee employee, Integer workgroupId) {
        //查询申请加入是否重复提交
        WorkgroupApplyQuery query = new WorkgroupApplyQuery();
        query.setWorkgroupId(workgroupId);
        query.setApplyEmployeeId(employee.getId());
        List<WorkgroupApply> list = workgroupApplyDao.selectByQuery(query);
        if(list != null && list.size() > 0){
            for(WorkgroupApply workgroupApply : list){
                if(workgroupApply.getApplyStatus() == WorkgroupApplyStatus.PendingAudit.getStatus()){
                    throw new MyException(DiskException.WORKGROUP_APPLY_REPEAT_COMMIT);
                }
            }
        }
        WorkgroupApply workgroupApply = new WorkgroupApply();
        workgroupApply.setWorkgroupId(workgroupId);
        workgroupApply.setApplyEmployeeId(employee.getId());
        workgroupApply.setApplyStatus(WorkgroupApplyStatus.PendingAudit.getStatus());
        workgroupApply.setCreateAdmin(employee.getEmployeeName());
        int i = workgroupApplyDao.insert(workgroupApply);
        //发送消息
        messageService.sendWorkgroupApplyMessage(employee, workgroupId, workgroupApply.getId());
        return i;
    }
    
    @Override
    @Transactional
    public int saveBatchWorkgroupApply(Employee employee, List<String> workgroupIds) {
        if(workgroupIds != null && workgroupIds.size() > 0){
            for(String workgroupId : workgroupIds){
                saveWorkgroupApply(employee, Integer.valueOf(workgroupId));
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public int examinationWorkgroupApply(List<String> ids, Integer workgroupApplyStatus, Employee employee) {
        if(ids != null && ids.size() > 0){
            for(String id : ids){
                Integer workgroupApplyId = Integer.valueOf(id);
                WorkgroupApply workgroupApply = workgroupApplyDao.selectById(workgroupApplyId);
                if(workgroupApply != null){
                    if(workgroupApplyStatus == WorkgroupApplyStatus.AuditThrough.getStatus()){
                        //审核通过
                        WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
                        workgroupEmployee.setWorkgroupId(workgroupApply.getWorkgroupId());
                        workgroupEmployee.setEmployeeId(workgroupApply.getApplyEmployeeId());
                        workgroupEmployeeDao.insert(workgroupEmployee);
                    }else{
                        //审核不通过，不添加工作组
                    }
                    //状态修改
                    workgroupApply.setId(workgroupApplyId);
                    workgroupApply.setApplyStatus(workgroupApplyStatus);
                    workgroupApply.setUpdateTime(new Date());
                    workgroupApplyDao.update(workgroupApply);
                    //发送消息
                    messageService.sendWorkgroupReviewedMessage(employee,workgroupApply);
                }
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public int quitWorkgroup(Employee employee, Integer workgroupId) {
        WorkgroupEmployee workgroupEmployee = new WorkgroupEmployee();
        workgroupEmployee.setWorkgroupId(workgroupId);
        workgroupEmployee.setEmployeeId(employee.getId());
        workgroupEmployeeDao.delByPrimaryKey(workgroupEmployee);
        //发送消息
        messageService.sendWorkgroupQuitMessage(employee, workgroupId);
        return 0;
    }

    @Override
    @Transactional
    public int quitBatchWorkgroup(Employee employee, List<String> workgroupIds) {
        if(workgroupIds != null && workgroupIds.size() > 0){
            for(String workgroupId : workgroupIds){
                quitWorkgroup(employee, Integer.valueOf(workgroupId));
            }
        }
        return 0;
    }

}
