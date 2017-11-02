package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.teamwork.MsgType;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.mapper.teamwork.ITeamworkDao;
import com.yunip.mapper.teamwork.ITeamworkEmployeeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.TeamworkEmployee;
import com.yunip.service.ITeamworkEmployeeService;

@Service("iTeamworkEmployeeService")
public class TeamworkEmployeeServiceImpl implements ITeamworkEmployeeService{
    
    @Resource(name = "iTeamworkDao")
    private ITeamworkDao teamworkDao;
    
    @Resource(name = "iTeamworkEmployeeDao")
    private ITeamworkEmployeeDao teamworkEmployeeDao;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;

    @Override
    @Transactional
    public int quitTeamwork(Employee employee, Integer teamworkId) {
        TeamworkEmployee teamworkEmployee = new TeamworkEmployee();
        teamworkEmployee.setTeamworkId(teamworkId);
        teamworkEmployee.setEmployeeId(employee.getId());
        teamworkEmployeeDao.delByPrimaryKey(teamworkEmployee);
        //协作消息
        teamworkMessageManager.saveTeamworkMessage(MsgType.QUIT_TEAMWORK_EMPLOYEE.getType(), employee.getId(), employee.getEmployeeName(), teamworkId, "", null, null, null);
        return 0;
    }

    @Override
    @Transactional
    public int quitBatchTeamwork(Employee employee, List<String> teamworkIds) {
        if(teamworkIds != null && teamworkIds.size() > 0){
            for(String teamworkId : teamworkIds){
                quitTeamwork(employee, Integer.valueOf(teamworkId));
            }
        }
        return 0;
    }

    @Override
    public List<TeamworkEmployee> getTeamworkEmployeeIds(Integer teamworkId) {
        return teamworkEmployeeDao.selectByTeamworkId(teamworkId);
    }
    
    @Override
    public List<Integer> getJoinTeamwork(Integer employeeId) {
        return teamworkEmployeeDao.selectByEmployeeId(employeeId);
    }
    
    @Override
    public boolean saveBatchTeamworkEmployee(Integer teamworkId, List<String> ids, Employee employee) {
        if(!ids.contains(""+employee.getId())){
            throw new MyException(DiskException.TEAMWORK_CREATEADMIN_SAME);
        }
        if(ids != null && ids.size() > 0){
            //获取该工作组下所有关联
            List<TeamworkEmployee> teamworkEmployees = teamworkEmployeeDao.selectByTeamworkId(teamworkId);
            //判断ids中，哪些是新增id，以及现有ids中哪些是移除id
            List<String> haveIds = new ArrayList<String>();//现有关联集合
            if(teamworkEmployees != null && teamworkEmployees.size() > 0){
                for(TeamworkEmployee teamworkEmployee : teamworkEmployees){
                    haveIds.add(""+teamworkEmployee.getEmployeeId());
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
            teamworkEmployeeDao.delByTeamworkId(teamworkId);
            //再循环添加
            for(String employeeId : ids){
                TeamworkEmployee teamworkEmployee = new TeamworkEmployee();
                teamworkEmployee.setTeamworkId(teamworkId);
                teamworkEmployee.setEmployeeId(Integer.valueOf(employeeId));
                teamworkEmployee.setAuthorityType(1);
                teamworkEmployeeDao.insert(teamworkEmployee);
            }
            //协作消息:邀请的成员
            teamworkMessageManager.saveTeamworkMessages(MsgType.ADD_TEAMWORK_EMPLOYEE.getType(), addIds, teamworkId, employee);
            //协作消息:移除的成员
            teamworkMessageManager.saveTeamworkMessages(MsgType.DELETE_TEAMWORK_EMPLOYEE.getType(), removeIds, teamworkId, employee);
            return true;
        }
        return false;
    }

    @Override
    public TeamworkEmployee getTeamworkEmployee(
            TeamworkEmployee teamworkEmployee) {
        return teamworkEmployeeDao.selectByPrimaryKey(teamworkEmployee);
    }
}
