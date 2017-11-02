package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.enums.teamwork.MsgType;
import com.yunip.manage.FileManager;
import com.yunip.manage.TeamworkMessageManager;
import com.yunip.mapper.teamwork.ITeamworkDao;
import com.yunip.mapper.teamwork.ITeamworkEmployeeDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkEmployee;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.query.TeamworkQuery;
import com.yunip.service.ITeamworkService;

@Service("iTeamworkService")
public class TeamworkServiceImpl implements ITeamworkService{
    
    @Resource(name = "iTeamworkDao")
    private ITeamworkDao teamworkDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao teamworkFileDao;
    
    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao teamworkFileVersionDao;
    
    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao teamworkFolderDao;
    
    @Resource(name = "iTeamworkEmployeeDao")
    private ITeamworkEmployeeDao teamworkEmployeeDao;
    
    @Resource(name = "teamworkMessageManager")
    private TeamworkMessageManager teamworkMessageManager;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;

    @Override
    public TeamworkQuery queryTeamwork(TeamworkQuery query) {
        List<Teamwork> list = teamworkDao.selectByQuery(query);
        int count = teamworkDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }
    
    @Override
    @Transactional
    public int saveOrEdit(Teamwork teamwork,Employee employee) {
        if(teamwork != null){
            if(teamwork.getId() != null && teamwork.getId() > 0){
                //修改
                teamwork.setUpdateAdmin(employee.getEmployeeName());
                teamwork.setUpdateTime(new Date());
                teamworkDao.update(teamwork);
                //协作消息
                teamworkMessageManager.saveTeamworkMessage(MsgType.EDIT_TEAMWORK.getType(), employee.getId(), employee.getEmployeeName(), teamwork.getId(), teamwork.getTeamworkName(), null, null, null);
            }else{
                //保存
                teamwork.setEmployeeId(employee.getId());
                teamwork.setCreateAdmin(employee.getEmployeeName());
                teamwork.setUpdateAdmin(employee.getEmployeeName());
                teamwork.setUseSpaceSize(0L);
                teamworkDao.insert(teamwork);
                //将创建人加入该协作
                TeamworkEmployee teamworkEmployee = new TeamworkEmployee();
                teamworkEmployee.setTeamworkId(teamwork.getId());
                teamworkEmployee.setEmployeeId(employee.getId());
                teamworkEmployee.setAuthorityType(1);
                teamworkEmployeeDao.insert(teamworkEmployee);
                //协作消息
                teamworkMessageManager.saveTeamworkMessage(MsgType.CREATE_TEAMWORK.getType(), employee.getId(), employee.getEmployeeName(), teamwork.getId(), teamwork.getTeamworkName(), null, null, null);
            }
        }
        return 0;
    }

    @Override
    public Teamwork getTeamwork(Integer teamworkId) {
        return teamworkDao.selectById(teamworkId);
    }
    
    @Override
    @Transactional
    public boolean delBatchTeamwork(List<String> ids, Employee employee) {
        if(ids == null || ids.size() <= 0){
            return false;
        }else{
            List<Integer> teamworkIds = new ArrayList<Integer>();
            for(String id : ids){
                Integer teamworkId = Integer.valueOf(id);
                teamworkIds.add(teamworkId);
                //发送消息
                /*messageService.sendWorkgroupDeleteMessage(employee, workgroupId);
                workgroupIds.add(Integer.valueOf(id));*/
                //删除工作组关联员工
                teamworkEmployeeDao.delByTeamworkId(teamworkId);
            }
            //批量删除协作
            Teamwork teamwork = new Teamwork();
            teamwork.setIds(teamworkIds);
            teamwork.setEmployeeId(employee.getId());
            teamworkDao.batchDelete(teamwork);
            
            //查询出协作集所有关联的文件路径
            Set<String> deleteFilePaths = new HashSet<String>();
            //构建临时查询类：文件 文件夹 文件版本
            TeamworkFile tmpTeamworkFile = new TeamworkFile();
            tmpTeamworkFile.setIds(ids);
            TeamworkFileVersion tmpTeamworkFileVersion = new TeamworkFileVersion();
            tmpTeamworkFileVersion.setIds(ids);
            TeamworkFolder tmpTeamworkFolder = new TeamworkFolder();
            tmpTeamworkFolder.setIds(ids);
            //查询协作文件表中的路径
            Set<String> tmpFilePath = teamworkFileDao.selectPathByTeamworkIds(tmpTeamworkFile);
            //查询协作文件历史表中的路径
            Set<String> tmpFileVersionPath = teamworkFileVersionDao.selectPathByTeamworkIds(tmpTeamworkFileVersion);
            //加入待删除路径
            deleteFilePaths.addAll(tmpFilePath);
            deleteFilePaths.addAll(tmpFileVersionPath);
            //清空协作相关表：文件，文件版本，文件夹
            teamworkFileDao.delByTeamworkIds(tmpTeamworkFile);
            teamworkFileVersionDao.delByTeamworkIds(tmpTeamworkFileVersion);
            teamworkFolderDao.delByTeamworkIds(tmpTeamworkFolder);
            //清空后，整理待删除路径集，查其他引用
            fileManager.getOnlyUseFilePath(deleteFilePaths);
            //线程删除物理路径
            fileManager.threadDeleteFile(deleteFilePaths);
            return  true;
        }
    }

    @Override
    public List<Integer> getHasTeamwork(Integer employeeId) {
        return teamworkDao.selectByEmployee(employeeId);
    }

}
