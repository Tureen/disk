/*
 * 描述：〈日志管理〉
 * 创建人：can.du
 * 创建时间：2016-6-27
 */
package com.yunip.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.constant.EmployeeLogTemplate;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.log.ActionType;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.log.IAdminLogDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.model.log.AdminLog;

/**
 * 日志管理
 */
@Component("logManager")
public class LogManager {

    @Resource(name = "iFolderDao")
    private IFolderDao folderDao;
    
    @Resource(name = "iAdminLogDao")
    private IAdminLogDao adminLogDao;
    
    
    /***
     * 插入基本路径操作
     * @param folderId   文件夹ID
     * @param name       文件夹或文件名称
     * @param actionType 操作类型
     * @param employee   员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(int folderId, OpenType openType, String name, ActionType actionType, 
            Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        List<Folder> folders = getParentFolders(folderId);
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        String content = EmployeeLogTemplate.getFolderOrFileTemplate(folders, openType, name);
        adminLog.setOperContent(content);
        adminLogDao.insert(adminLog);
    }
    
    
    /***
     * 移动或者复制(包含多文件)
     * @param folderId   文件夹ID
     * @param name       文件夹或文件名称
     * @param isMore     是否操作多个
     * @param actionType 操作类型
     * @param employee   员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(int folderId, String name, boolean isMore, int toFolderId, ActionType actionType,
            Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        List<Folder> folders = getParentFolders(folderId);
        List<Folder> toFolders = getParentFolders(toFolderId);
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        String content = EmployeeLogTemplate.getFolderOrFileTemplates(folders, toFolders, isMore, name, actionType);
        adminLog.setOperContent(content);
        adminLogDao.insert(adminLog);
    }
    
    
    /***
     * 插入基本路径操作
     * @param folderId   文件夹ID
     * @param names      文件夹或文件名称集合
     * @param actionType 操作类型
     * @param employee   员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(int folderId,  OpenType openType, List<String> names, ActionType actionType, 
            Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        List<Folder> folders = getParentFolders(folderId);
        if(names != null && names.size() > 0){
            for(String name : names){
                String content = EmployeeLogTemplate.getFolderOrFileTemplate(folders, openType, name);
                adminLog.setOperContent(content);
                adminLog.setIsAdmin(IsAdminType.YG.getType());
                adminLogDao.insert(adminLog);
            }
        }
    }
    
    /***
     * 常规不存在描述
     * @param actionType 操作类型
     * @param employee   员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(ActionType actionType, Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        adminLogDao.insert(adminLog);
    }
    
    /***
     * 常规存在描述
     * @param actionType 操作类型
     * @param content    描述内容
     * @param employee   员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(ActionType actionType,String content, Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        adminLog.setOperContent(content);
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        adminLogDao.insert(adminLog);
    }
    
    /***
     * 插入基本路径操作
     * @param folderId   文件夹ID
     * @param name       文件夹或文件名称
     * @param shareObj   分享对象
     * @param authorityType 分享权限
     * @param actionType   操作类型
     * @param employee  员工对象
     * void 
     * @exception
     */
    public void insertShareLog(int folderId,  OpenType openType, String name, String shareObj, AuthorityType authorityType, 
            ActionType actionType, Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        List<Folder> folders = getParentFolders(folderId);
        String content = EmployeeLogTemplate.getShareContent(folders, openType, name, authorityType);
        adminLog.setOperContent(content);
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        adminLogDao.insert(adminLog);
    }

    
    /****
     * @param folderId     文件夹ID 
     * @param oldName      原文件夹或文件名称
     * @param nowName      现文件夹或文件名称
     * @param actionType   操作类型
     * @param employee     员工对象
     * void 
     * @exception
     */
    public void insertEmpLoyeeLog(int folderId, OpenType openType, String oldName, String nowName, ActionType actionType, 
            Employee employee){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(employee.getId());
        adminLog.setOperAdmin(employee.getEmployeeName());
        adminLog.setOperIp(employee.getLoginIp());
        adminLog.setOperTime(new Date());
        adminLog.setActionType(actionType.getType());
        List<Folder> folders = getParentFolders(folderId);
        String content = EmployeeLogTemplate.getRenameContent(folders, openType,oldName, nowName);
        adminLog.setOperContent(content);
        adminLog.setIsAdmin(IsAdminType.YG.getType());
        adminLogDao.insert(adminLog);
    }
    
    public List<Folder> getParentFolders(int folderId) {
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = folderDao.selectById(folderId);
        if (folder != null) {
            folders.add(folder);
        }
        getParentFolder(folders, folder);
        Collections.reverse(folders);
        return folders;
    }

    public void getParentFolder(List<Folder> folders, Folder folder) {
        if (folder != null && folder.getParentId() != 0) {
            Folder paFolder = folderDao.selectById(folder.getParentId());
            folders.add(paFolder);
            getParentFolder(folders, paFolder);
        }
    }

}
