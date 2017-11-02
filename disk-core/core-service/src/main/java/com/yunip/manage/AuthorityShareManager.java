package com.yunip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.constant.SystemContant;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.fileservers.FileServersType;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IAuthorityShareDao;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.teamwork.ITeamworkEmployeeDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFolderDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.teamwork.TeamworkEmployee;

/**
 * 分享权限数据模块
 */
@Component("authorityShareManager")
public class AuthorityShareManager {

    @Resource(name = "iAuthorityShareDao")
    private IAuthorityShareDao   authorityShareDao;

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao         employeeDao;

    @Resource(name = "iFileDao")
    private IFileDao             fileDao;

    @Resource(name = "iFolderDao")
    private IFolderDao           folderDao;

    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao     teamworkFileDao;

    @Resource(name = "iTeamworkFolderDao")
    private ITeamworkFolderDao   teamworkFolderDao;

    @Resource(name = "iTeamworkEmployeeDao")
    private ITeamworkEmployeeDao teamworkEmployeeDao;

    /**
     * 查询当前用户对文件或文件夹的操作权限
     * @param fileIds 文件集合
     * @param folderIds 文件夹集合
     * @param employeeId 员工ID
     * @param employeeRoles 员工角色ID集合
     * @param fileServersType 权限类型(来自枚举FileServersType)
     * @param spaceType 空间类型(验证类型为上传时才需要使用到)
     * @return 返回值true|false true：具备该权限  false：不具备该权限
     */
    public boolean getOpenAuthById(List<String> fileIds,
            List<String> folderIds, Integer employeeId,
            List<Integer> employeeRoles, FileServersType fileServersType,
            EnumSpaceType spaceType) {
        Employee employee = employeeDao.selectById(employeeId);
        //权限集合
        List<Integer> authList = new ArrayList<Integer>();
        //遍历查询权限,
        if (fileIds != null && fileIds.size() > 0) {
            for (String fileId : fileIds) {
                File file = fileDao.selectById(fileId);
                if (file == null) { //未找到文件
                    continue;
                }
                //先查是否是自身文件
                if (file != null && employeeId.equals(file.getEmployeeId())) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //如果文件拥有者是公共空间管理员,查看用户角色是否是公共空间管理员
                else if (file != null
                        && file.getEmployeeId().equals(
                                SystemContant.MANAGER_EMPLOYEE_ID)
                        && employeeRoles != null
                        && employeeRoles.size() > 0
                        && employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID)) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //再查被分享
                AuthorityShareQuery query = new AuthorityShareQuery();
                query.setShareEid("" + employeeId);
                query.setShareDid(employee.getDeptId());
                query.setWorkgroupEmployeeId(employee.getId());
                query.setOpenId(Integer.parseInt(fileId));
                query.setOpenType(OpenType.FILE.getType());
                List<AuthorityShare> fileAuthList = authorityShareDao.selectOpenAuth(query);
                Integer checkTmp = 99;
                //针对同一文件,同一个用户,有可能有多条权限,取最大的
                if (fileAuthList != null && fileAuthList.size() > 0) {
                    int authTmp = 99;
                    for (AuthorityShare share : fileAuthList) {
                        authTmp = share.getOperAuth() < authTmp ? share.getOperAuth() : authTmp;
                    }
                    checkTmp = authTmp;
                }
                //最后查看上级文件夹的分享权限
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setEmployeeId(file.getEmployeeId());
                folderQuery.setFolderCode(file.getFolderCode());
                Integer authTmp = folderDao.selectAuthByCode(folderQuery);
                if (authTmp != null && authTmp > 0) {
                    if (authTmp < checkTmp) {
                        checkTmp = authTmp;
                    }
                }
                //被分享权限赋值
                if (checkTmp != 99) {
                    authList.add(checkTmp);
                    continue;
                }
            }
        }
        if (folderIds != null && folderIds.size() > 0) {
            for (String folderId : folderIds) {
                //如果文件夹id为0,则是最上层上传的操作判断,判断标准是根据上传到个人空间还是管理公共空间权限
                if ("0".equals(folderId)
                        && fileServersType.equals(FileServersType.UPLOAD)) {
                    if (spaceType.equals(EnumSpaceType.PRIVATE_SPACE)) {
                        authList.add(AuthorityType.MANAGER.getCode());
                        continue;
                    }
                    else {
                        boolean b = employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID);
                        if (b) {
                            authList.add(AuthorityType.MANAGER.getCode());
                        }
                        continue;
                    }
                }
                //查询文件夹信息
                Folder folder = folderDao.selectById(folderId);
                //上传限制(链接擅自修改:个人空间类型与公共空间类型互改):如果操作类型为上传,并且上传空间类型为公共空间或个人空间,判断文件夹拥有者是否是公共空间管理员或是否不是公共空间管理员
                if (folder != null
                        && fileServersType.equals(FileServersType.UPLOAD)
                        && spaceType.equals(EnumSpaceType.PUBLIC_SPACE)) {
                    if (!(SystemContant.MANAGER_EMPLOYEE_ID).equals(folder.getEmployeeId())) {
                        continue;
                    }
                }
                else if (folder != null
                        && fileServersType.equals(FileServersType.UPLOAD)
                        && spaceType.equals(EnumSpaceType.PRIVATE_SPACE)) {
                    if ((SystemContant.MANAGER_EMPLOYEE_ID).equals(folder.getEmployeeId())) {
                        continue;
                    }
                }
                //先查是否是自身文件
                if (folder != null && employeeId.equals(folder.getEmployeeId())) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //如果文件夹拥有者是公共空间管理员,查看用户角色是否是公共空间管理员
                else if (folder != null
                        && folder.getEmployeeId().equals(
                                SystemContant.MANAGER_EMPLOYEE_ID)
                        && employeeRoles != null
                        && employeeRoles.size() > 0
                        && employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID)) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //再查被分享
                AuthorityShareQuery query = new AuthorityShareQuery();
                query.setShareEid("" + employeeId);
                query.setShareDid(employee.getDeptId());
                query.setWorkgroupEmployeeId(employee.getId());
                query.setOpenId(Integer.parseInt(folderId));
                query.setOpenType(OpenType.FOLDER.getType());
                List<AuthorityShare> folderAuthList = authorityShareDao.selectOpenAuth(query);
                Integer checkTmp = 99;
                //针对同一文件,同一个用户,有可能有多条权限,取最大的
                if (folderAuthList != null && folderAuthList.size() > 0) {
                    int authTmp = 99;
                    for (AuthorityShare share : folderAuthList) {
                        authTmp = share.getOperAuth() < authTmp ? share.getOperAuth() : authTmp;
                    }
                    checkTmp = authTmp;
                }
                //最后查看上级文件夹的分享权限
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setEmployeeId(folder.getEmployeeId());
                folderQuery.setFolderCode(folder.getFolderCode());
                Integer authTmp = folderDao.selectAuthByCode(folderQuery);
                if (authTmp != null && authTmp > 0) {
                    if (authTmp < checkTmp) {
                        checkTmp = authTmp;
                    }
                }
                //被分享权限赋值
                if (checkTmp != 99) {
                    authList.add(checkTmp);
                    continue;
                }
            }
        }
        //选取权限集合中最小的
        int minAuth = -1;
        if (authList != null && authList.size() > 0) {
            for (Integer auth : authList) {
                if (auth != null) {
                    minAuth = auth > minAuth ? auth : minAuth;
                }
            }
        }
        else {
            return false;
        }
        //对应操作权限判断
        if (minAuth == -1) {
            return false;
        }
        if (fileServersType.equals(FileServersType.DOWNLOAD)
                && minAuth <= AuthorityType.SEE.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.UPLOAD)
                && minAuth <= AuthorityType.MANAGER.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.ONLINEEDIT)
                && minAuth <= AuthorityType.MANAGER.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.PREVIEWFILE)
                && minAuth <= AuthorityType.READ.getCode()) {
            return true;
        }
        return false;
    }

    /**
     * 查询当前用户对文件或文件夹的操作权限
     * @param fileIds 文件集合
     * @param folderIds 文件夹集合
     * @param employeeId 员工ID
     * @param employeeRoles 员工角色ID集合
     * @param fileServersType 权限类型(来自枚举FileServersType)
     * @param spaceType 空间类型(验证类型为上传时才需要使用到)
     * @return 返回值true|false true：具备该权限  false：不具备该权限
     */
    public boolean getApiOpenAuthById(List<String> fileIds,
            List<String> folderIds, Integer employeeId,
            List<Integer> employeeRoles, FileServersType fileServersType,
            EnumSpaceType spaceType) {
        Employee employee = employeeDao.selectById(employeeId);
        //权限集合
        List<Integer> authList = new ArrayList<Integer>();
        //遍历查询权限,
        if (fileIds != null && fileIds.size() > 0) {
            for (String fileId : fileIds) {
                File file = fileDao.selectById(fileId);
                if (file == null) { //未找到文件
                    continue;
                }
                //先查是否是自身文件
                if (file != null && employeeId.equals(file.getEmployeeId())) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //如果文件拥有者是公共空间管理员,查看用户角色是否是公共空间管理员
                else if (file != null
                        && file.getEmployeeId().equals(
                                SystemContant.MANAGER_EMPLOYEE_ID)
                        && employeeRoles != null
                        && employeeRoles.size() > 0
                        && employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID)) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //再查被分享
                AuthorityShareQuery query = new AuthorityShareQuery();
                query.setShareEid("" + employeeId);
                query.setShareDid(employee.getDeptId());
                query.setWorkgroupEmployeeId(employee.getId());
                query.setOpenId(Integer.parseInt(fileId));
                query.setOpenType(OpenType.FILE.getType());
                List<AuthorityShare> fileAuthList = authorityShareDao.selectOpenAuth(query);
                Integer checkTmp = 99;
                //针对同一文件,同一个用户,有可能有多条权限,取最大的
                if (fileAuthList != null && fileAuthList.size() > 0) {
                    int authTmp = 99;
                    for (AuthorityShare share : fileAuthList) {
                        authTmp = share.getOperAuth() < authTmp ? share.getOperAuth() : authTmp;
                    }
                    checkTmp = authTmp;
                }
                //最后查看上级文件夹的分享权限
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setEmployeeId(file.getEmployeeId());
                folderQuery.setFolderCode(file.getFolderCode());
                Integer authTmp = folderDao.selectAuthByCode(folderQuery);
                if (authTmp != null && authTmp > 0) {
                    if (authTmp < checkTmp) {
                        checkTmp = authTmp;
                    }
                }
                //被分享权限赋值
                if (checkTmp != 99) {
                    authList.add(checkTmp);
                    continue;
                }
            }
        }
        if (folderIds != null && folderIds.size() > 0) {
            for (String folderId : folderIds) {
                //如果文件夹id为0,则是最上层上传的操作判断,判断标准是根据上传到个人空间还是管理公共空间权限
                if ("0".equals(folderId)
                        && fileServersType.equals(FileServersType.UPLOAD)) {
                    if (spaceType.equals(EnumSpaceType.PRIVATE_SPACE)) {
                        authList.add(AuthorityType.MANAGER.getCode());
                        continue;
                    }
                    else {
                        boolean b = employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID);
                        if (b) {
                            authList.add(AuthorityType.MANAGER.getCode());
                        }
                        continue;
                    }
                }
                //查询文件夹信息
                Folder folder = folderDao.selectById(folderId);
                //上传限制(链接擅自修改:个人空间类型与公共空间类型互改):如果操作类型为上传,并且上传空间类型为公共空间或个人空间,判断文件夹拥有者是否是公共空间管理员或是否不是公共空间管理员
                if (folder != null
                        && fileServersType.equals(FileServersType.UPLOAD)
                        && spaceType.equals(EnumSpaceType.PUBLIC_SPACE)) {
                    if (!(SystemContant.MANAGER_EMPLOYEE_ID).equals(folder.getEmployeeId())) {
                        continue;
                    }
                }
                else if (folder != null
                        && fileServersType.equals(FileServersType.UPLOAD)
                        && spaceType.equals(EnumSpaceType.PRIVATE_SPACE)) {
                    if ((SystemContant.MANAGER_EMPLOYEE_ID).equals(folder.getEmployeeId())) {
                        continue;
                    }
                }
                //先查是否是自身文件
                if (folder != null && employeeId.equals(folder.getEmployeeId())) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //如果文件夹拥有者是公共空间管理员,查看用户角色是否是公共空间管理员
                else if (folder != null
                        && folder.getEmployeeId().equals(
                                SystemContant.MANAGER_EMPLOYEE_ID)
                        && employeeRoles != null
                        && employeeRoles.size() > 0
                        && employeeRoles.contains(SystemContant.COMMON_MAGENAGE_ID)) {
                    authList.add(AuthorityType.MANAGER.getCode());
                    continue;
                }
                //再查被分享
                AuthorityShareQuery query = new AuthorityShareQuery();
                query.setShareEid("" + employeeId);
                query.setShareDid(employee.getDeptId());
                query.setWorkgroupEmployeeId(employee.getId());
                query.setOpenId(Integer.parseInt(folderId));
                query.setOpenType(OpenType.FOLDER.getType());
                List<AuthorityShare> folderAuthList = authorityShareDao.selectOpenAuth(query);
                Integer checkTmp = 99;
                //针对同一文件,同一个用户,有可能有多条权限,取最大的
                if (folderAuthList != null && folderAuthList.size() > 0) {
                    int authTmp = 99;
                    for (AuthorityShare share : folderAuthList) {
                        authTmp = share.getOperAuth() < authTmp ? share.getOperAuth() : authTmp;
                    }
                    checkTmp = authTmp;
                }
                //最后查看上级文件夹的分享权限
                FolderQuery folderQuery = new FolderQuery();
                folderQuery.setEmployeeId(folder.getEmployeeId());
                folderQuery.setFolderCode(folder.getFolderCode());
                Integer authTmp = folderDao.selectAuthByCode(folderQuery);
                if (authTmp != null && authTmp > 0) {
                    if (authTmp < checkTmp) {
                        checkTmp = authTmp;
                    }
                }
                //被分享权限赋值
                if (checkTmp != 99) {
                    authList.add(checkTmp);
                    continue;
                }
            }
        }
        //选取权限集合中最小的
        int minAuth = -1;
        if (authList != null && authList.size() > 0) {
            for (Integer auth : authList) {
                if (auth != null) {
                    minAuth = auth > minAuth ? auth : minAuth;
                }
            }
        }
        else {
            return false;
        }
        //对应操作权限判断
        if (minAuth == -1) {
            return false;
        }
        if (fileServersType.equals(FileServersType.DOWNLOAD)
                && minAuth <= AuthorityType.SEE.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.UPLOAD)
                && minAuth <= AuthorityType.MANAGER.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.ONLINEEDIT)
                && minAuth <= AuthorityType.MANAGER.getCode()) {
            return true;
        }
        if (fileServersType.equals(FileServersType.PREVIEWFILE)
                && minAuth <= AuthorityType.READ.getCode()) {
            return true;
        }
        return false;
    }

    /**
     * 协作员工对协作文件及文件夹的操作权限
     * @param fileIds
     * @param folderIds
     * @param employeeId
     * @param teamworkId
     * @return  
     * boolean 
     * @exception
     */
    public boolean getTeamworkOpenAuthById(List<String> fileIds,
            List<String> folderIds, Integer employeeId, Integer teamworkId) {
        //查询该员工所有参与协作的协作文件
        List<Integer> allFileIds = teamworkFileDao.selectFileByEmployeeId(employeeId);
        //查询该员工所有参与协作的协作文件夹
        List<Integer> allFolderIds = teamworkFolderDao.selectFolderByEmployeeId(employeeId);
        //判断需操作的协作文件及文件夹是否包含在员工所能操作的协作文件及文件夹内
        if (fileIds != null && fileIds.size() > 0) {
            for (String fileId : fileIds) {
                if (!allFileIds.contains(Integer.valueOf(fileId))) {
                    return false;
                }
            }
        }
        if (folderIds != null && folderIds.size() > 0) {
            for (String folderId : folderIds) {
                if ("0".equals(folderId)) {
                    TeamworkEmployee teamworkEmployee = new TeamworkEmployee();
                    teamworkEmployee.setEmployeeId(employeeId);
                    teamworkEmployee.setTeamworkId(teamworkId);
                    TeamworkEmployee temp = teamworkEmployeeDao.selectByPrimaryKey(teamworkEmployee);
                    if(temp != null && temp.getAuthorityType() != null && temp.getAuthorityType().equals(1)){
                        return true;
                    }
                }
                if (!allFolderIds.contains(Integer.valueOf(folderId))) {
                    return false;
                }
            }
        }
        return true;
    }

}
