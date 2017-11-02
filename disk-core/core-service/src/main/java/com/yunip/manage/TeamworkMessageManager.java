package com.yunip.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.teamwork.ITeamworkDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.mapper.teamwork.ITeamworkMessageDao;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.model.teamwork.query.TeamworkMessageQuery;

/**
 * 协作记录信息管理
 */
@Component("teamworkMessageManager")
public class TeamworkMessageManager {
    
    /**根目录***/
    public static String TEMPLATEPATH = "/";

    @Resource(name = "iTeamworkMessageDao")
    private ITeamworkMessageDao teamworkMessageDao;
    
    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao teamworkFolderDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao teamworkFileDao;
    
    @Resource(name = "iEmployeeDao")
    private IEmployeeDao employeeDao;
    
    @Resource(name = "iTeamworkDao")
    private ITeamworkDao teamworkDao;
    
    public String getTeamworkName(Integer teamworkId){
        Teamwork teamwork = teamworkDao.selectById(teamworkId);
        return teamwork != null ? teamwork.getTeamworkName() : ""; 
    }
    
    /**
     * 条件查询
     * @param messageQuery
     * @return  
     * TeamworkMessageQuery 
     * @exception
     */
    public TeamworkMessageQuery queryAjaxTeamworkMessage(
            TeamworkMessageQuery messageQuery) {
        List<TeamworkMessage> list = teamworkMessageDao.selectAjaxByQuery(messageQuery);
        int count = teamworkMessageDao.selectAjaxCountByQuery(messageQuery);
        messageQuery.setList(list);
        messageQuery.setRecordCount(count);
        return messageQuery;
    }
    
    public List<TeamworkMessage> getTeamworkMessageByFileId(TeamworkMessage teamworkMessage){
        return teamworkMessageDao.selectByFileId(teamworkMessage);
    }
    
    /**
     * 添加协作日志（多文件or文件夹）
     * @param msgType
     * @param employeeId
     * @param employeeName
     * @param teamworkId
     * @param fileIds
     * @param folderIds  
     * void 
     * @exception
     */
    public int saveTeamworkMessages(Integer msgType, Integer employeeId, String employeeName, Integer teamworkId, List<Integer> fileIds, List<Integer> folderIds) {
        AtomicBoolean atomicboolean = new AtomicBoolean(false);
        Integer zid = null;
        if((fileIds != null && fileIds.size() > 0) || (folderIds != null && folderIds.size() > 0)){
            //非留言，可能有多条
            if(fileIds != null && fileIds.size() > 0){
                for(Integer fileId : fileIds){
                    int zidTmp = saveTeamworkMessage(msgType, employeeId, employeeName, teamworkId, null, fileId, null, zid);
                    //只允许进入一次,将第一个新建的记录id赋予为组id
                    if(atomicboolean.compareAndSet(false, true)){
                        zid = zidTmp;
                        if(zid == 0){
                            atomicboolean.set(false);
                        }else{
                            //修改zid
                            TeamworkMessage message = new TeamworkMessage();
                            message.setId(zid);
                            message.setZid(zid);
                            teamworkMessageDao.update(message);
                        }
                    }
                }
            }
            if(folderIds != null && folderIds.size() > 0){
                for(Integer folderId : folderIds){
                    int zidTmp = saveTeamworkMessage(msgType, employeeId, employeeName, teamworkId, null, null, folderId, zid);
                    //只允许进入一次,将第一个新建的记录id赋予为组id
                    if(atomicboolean.compareAndSet(false, true)){
                        zid = zidTmp;
                        if(zid == 0){
                            atomicboolean.set(false);
                        }else{
                            //修改zid
                            TeamworkMessage message = new TeamworkMessage();
                            message.setId(zid);
                            message.setZid(zid);
                            teamworkMessageDao.update(message);
                        }
                    }
                }
            }
        } else {
            //留言，仅单条
            zid = saveTeamworkMessage(msgType, employeeId, employeeName, teamworkId, null, null, null, null);
        }
        return zid == null ? 0 : zid;
    }
    
    /**
     * 添加协作日志（多员工）
     * @param msgType
     * @param employeeId
     * @param employeeName
     * @param teamworkId
     * @param fileIds
     * @param folderIds  
     * void 
     * @exception
     */
    public void saveTeamworkMessages(Integer msgType, List<String> addEmployeeIds, Integer teamworkId, Employee employee) {
        AtomicBoolean atomicboolean = new AtomicBoolean(false);
        Integer zid = null;
        if((addEmployeeIds != null && addEmployeeIds.size() > 0) ){
            //非留言，可能有多条
            for(String employeeId : addEmployeeIds){
                Employee addEmployee = employeeDao.selectById(Integer.parseInt(employeeId));
                int zidTmp = saveTeamworkMessage(msgType, addEmployee.getId(), employee.getEmployeeName(), teamworkId, null, null, null, zid);
                //只允许进入一次,将第一个新建的记录id赋予为组id
                if(atomicboolean.compareAndSet(false, true)){
                    zid = zidTmp;
                    if(zid == 0){
                        atomicboolean.set(false);
                    }else{
                        //修改zid
                        TeamworkMessage message = new TeamworkMessage();
                        message.setId(zid);
                        message.setZid(zid);
                        teamworkMessageDao.update(message);
                    }
                }
            }
        }
    }
    
    /**
     * 添加协作日志
     * @param msgType
     * @param employeeId
     * @param employeeName
     * @param teamworkId
     * @param fileId
     * @param folderId
     * @param zid
     * @return  
     * int 
     * @exception
     */
    public int saveTeamworkMessage(Integer msgType, Integer employeeId, String employeeName, Integer teamworkId, String content , Integer fileId, Integer folderId, Integer zid){
        TeamworkMessage teamworkMessage = new TeamworkMessage();
        teamworkMessage.setEmployeeId(employeeId);
        teamworkMessage.setTeamworkId(teamworkId);
        teamworkMessage.setMsgType(msgType);
        teamworkMessage.setZid(zid);
        teamworkMessage.setFileId(fileId);
        teamworkMessage.setFolderId(folderId);
        teamworkMessage.setCreateAdmin(employeeName);
        List<TeamworkFolder> folders = null;
        if(folderId != null){
            folders = getParentFolders(folderId);
        } else if(fileId != null){
            TeamworkFile teamworkFile = teamworkFileDao.selectById(fileId);
            if(teamworkFile != null && teamworkFile.getFolderId() != null){
                folders = getParentFolders(teamworkFile.getFolderId());
            }
        }
        if(content == null){
            content = getMessageContent(folders, folderId, fileId);
        }
        teamworkMessage.setContent(content);
        int i = teamworkMessageDao.insert(teamworkMessage);
        if(i > 0 && zid == null){
            teamworkMessage.setZid(teamworkMessage.getId());
            i = teamworkMessageDao.update(teamworkMessage);
        }
        return i > 0 ? teamworkMessage.getId() : 0;
    }
    
    /**
     * 递归，获取所有上级文件夹
     * @param folderId
     * @return  
     * List<TeamworkFolder> 
     * @exception
     */
    public List<TeamworkFolder> getParentFolders(Integer folderId) {
        List<TeamworkFolder> folders = new ArrayList<TeamworkFolder>();
        TeamworkFolder folder = teamworkFolderDao.selectById(folderId);
        if (folder != null) {
            folders.add(folder);
        }
        getParentFolder(folders, folder);
        Collections.reverse(folders);
        return folders;
    }
    
    public void getParentFolder(List<TeamworkFolder> folders, TeamworkFolder folder) {
        if (folder != null && folder.getParentId() != 0) {
            TeamworkFolder paFolder = teamworkFolderDao.selectById(folder.getParentId());
            folders.add(paFolder);
            getParentFolder(folders, paFolder);
        }
    }
    
    /****
     * 更改描述
     * @param folders
     * @param oldName
     * @param nowName
     * @return  
     * String 
     * @exception
     */
    public String getMessageContent(List<TeamworkFolder> folders, Integer folderId, Integer fileId){
        StringBuffer path = new StringBuffer(TEMPLATEPATH);
        if(folders != null && folders.size() > 0){
            for(TeamworkFolder folder : folders){
                path.append(folder.getFolderName());
                path.append(TEMPLATEPATH);
            }
        }
        if(folderId != null){
            path.delete(path.length()-TEMPLATEPATH.length(), path.length());
        } else if (fileId != null){
            TeamworkFile teamworkFile = teamworkFileDao.selectById(fileId);
            path.append(teamworkFile.getFileName());
        }
        return path.toString();
    }

    /**
     * 递归查询目录下所有文件及目录，加入协作
     * @param fileIds
     * @param folderIds
     * @param teamworkFolder  
     * void 
     * @exception
     */
    public void addMessageFileOrFolderIds(List<Integer> fileIds, List<Integer> folderIds, TeamworkFolder teamworkFolder){
        List<TeamworkFile> files = teamworkFolder.getTeamworkFiles();
        if(files != null && files.size() > 0){
            for(TeamworkFile file : files){
                fileIds.add(file.getId());
            }
        }
        List<TeamworkFolder> folders = teamworkFolder.getTeamworkFolders();
        if(folders != null && folders.size() > 0){
            for(TeamworkFolder folder : folders){
                folderIds.add(folder.getId());
                addMessageFileOrFolderIds(fileIds, folderIds, folder);
            }
        }
    }
    
    /**
     * 递归查询目录下所有文件及目录，加入协作
     * @param fileIds
     * @param folderIds
     * @param teamworkFolder  
     * void 
     * @exception
     */
    public void addSelectMessageFileOrFolderIds(List<Integer> fileIds, List<Integer> folderIds, TeamworkFolder teamworkFolder){
        List<TeamworkFile> files = teamworkFolder.getTeamworkFiles();
        if(files != null && files.size() > 0){
            for(TeamworkFile file : files){
                fileIds.add(file.getId());
            }
        }
        List<TeamworkFolder> folders = teamworkFolder.getTeamworkFolders();
        if(folders != null && folders.size() > 0){
            for(TeamworkFolder folder : folders){
                folderIds.add(folder.getId());
                //
                List<TeamworkFile> tmpFiles = teamworkFileDao.selectAllByFolderId(folder.getId());
                List<TeamworkFolder> tmpFolders = teamworkFolderDao.selectAllByParentId(folder.getId());
                folder.setTeamworkFiles(tmpFiles);
                folder.setTeamworkFolders(tmpFolders);
                addSelectMessageFileOrFolderIds(fileIds, folderIds, folder);
            }
        }
    }
}
