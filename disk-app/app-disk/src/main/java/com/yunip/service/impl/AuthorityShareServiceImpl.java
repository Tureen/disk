/*
 * 描述：用户分享权限业务层实现
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.disk.ShareStatus;
import com.yunip.enums.disk.ShareType;
import com.yunip.enums.log.ActionType;
import com.yunip.manage.LogManager;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IAuthorityShareDao;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.disk.IWorkgroupEmployeeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.support.AuthHelper;
import com.yunip.model.disk.support.AuthMap;
import com.yunip.model.disk.support.AuthRelationHelper;
import com.yunip.model.disk.support.AuthReq;
import com.yunip.model.disk.support.FolderReq;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.IMessageService;

@Service("iAuthorityShareService")
public class AuthorityShareServiceImpl implements IAuthorityShareService {

    @Resource(name = "iAuthorityShareDao")
    private IAuthorityShareDao authorityShareDao;

    @Resource(name = "iFileDao")
    private IFileDao           fileDao;

    @Resource(name = "iFolderDao")
    private IFolderDao         folderDao;

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao       employeeDao;

    @Resource(name = "logManager")
    private LogManager         logManager;

    @Resource(name = "iMessageService")
    private IMessageService    messageService;
    
    @Resource(name = "iWorkgroupEmployeeDao")
    private IWorkgroupEmployeeDao workgroupEmployeeDao;

    @Override
    public int addAuthorityShare(AuthorityShare authorityShare) {
        return authorityShareDao.insert(authorityShare);
    }

    @Override
    public int updateAuthorityShare(AuthorityShare authorityShare) {
        return authorityShareDao.update(authorityShare);
    }

    @Override
    public FolderReq getBShare(AuthorityShareQuery query, Integer employeeId) {
        List<AuthorityShare> authorityShares = authorityShareDao.selectBShareByQuery(query);
        List<File> files = new ArrayList<File>();
        List<Folder> folders = new ArrayList<Folder>();
        Map<String, AuthorityShare> authorityMap = new HashMap<String, AuthorityShare>();
        List<AuthorityShare> shares = new ArrayList<AuthorityShare>();
        //去重
        for (AuthorityShare authorityShare : authorityShares) {
            if (authorityMap.containsKey(authorityShare.getOpenId() + "/"
                    + authorityShare.getOpenType())) {
                //对比
                AuthorityShare share = authorityMap.get(authorityShare.getOpenId()
                        + "/" + authorityShare.getOpenType());
                if (authorityShare.getOperAuth() < share.getOperAuth()) {
                    authorityMap.put(authorityShare.getOpenId() + "/"
                            + authorityShare.getOpenType(), authorityShare);
                }
            }
            else {
                authorityMap.put(authorityShare.getOpenId() + "/"
                        + authorityShare.getOpenType(), authorityShare);
            }
        }
        //        for(String key  : authorityMap.keySet()){
        //            shares.add(authorityMap.get(key));
        //        }
        for (AuthorityShare authorityShare : authorityShares) {
            AuthorityShare shareTmp = authorityMap.get(authorityShare.getOpenId()
                    + "/" + authorityShare.getOpenType());
            if (shareTmp != null) {
                shares.add(shareTmp);
                authorityMap.remove(authorityShare.getOpenId() + "/"
                        + authorityShare.getOpenType());
            }
        }
        //封装
        if (authorityShares.size() > 0) {
            for (AuthorityShare share : shares) {
                File file = share.getFile();
                Folder folder = share.getFolder();
                if (file != null && file.getEmployeeId() != employeeId) {
                    file.setAuthorityShare(share);
                    files.add(file);
                }
                if (folder != null && folder.getEmployeeId() != employeeId) {
                    folder.setAuthorityShare(share);
                    folders.add(folder);
                }
            }
        }
        FolderReq folderReq = new FolderReq();
        folderReq.setFiles(files);
        folderReq.setFolders(folders);
        folderReq.setAuthorityShares(authorityShares);
        return folderReq;
    }

    @Override
    public FolderReq getShare(AuthorityShareQuery query) {
        List<AuthorityShare> authorityShares = authorityShareDao.selectShareByQuery(query);
        List<File> files = new ArrayList<File>();
        List<Folder> folders = new ArrayList<Folder>();
        for (AuthorityShare share : authorityShares) {
            File file = share.getFile();
            Folder folder = share.getFolder();
            if (file != null) {
                file.setAuthorityShare(share);
                files.add(file);
            }
            if (folder != null) {
                folder.setAuthorityShare(share);
                folders.add(folder);
            }
        }
        FolderReq folderReq = new FolderReq();
        folderReq.setFiles(files);
        folderReq.setFolders(folders);
        return folderReq;
    }

    @Override
    public AuthMap getOpenAuth(AuthorityShareQuery query) {
        List<AuthorityShare> list = authorityShareDao.selectOpenAuthById(query);
        Map<Integer, Integer> fileMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> folderMap = new HashMap<Integer, Integer>();
        if (list.size() > 0) {
            for (AuthorityShare authorityShare : list) {
                if (authorityShare.getOpenType() == OpenType.FILE.getType()) {
                    fileMap.put(authorityShare.getOpenId(),
                            authorityShare.getOperAuth());
                }
                else if (authorityShare.getOpenType() == OpenType.FOLDER.getType()) {
                    folderMap.put(authorityShare.getOpenId(),
                            authorityShare.getOperAuth());
                }
            }
        }
        //封装
        AuthMap authMap = new AuthMap();
        authMap.setFileMap(fileMap);
        authMap.setFolderMap(folderMap);
        return authMap;
    }

    @Override
    public int delCascadeAuthorityShare(List<AuthorityShare> authorityShares) {
        // yun_dk_authority_share,yun_dk_file表批量删除id集合建立
        List<Integer> authorityIds = new ArrayList<Integer>();
        List<Integer> fileIds = new ArrayList<Integer>();
        List<Folder> folders = new ArrayList<Folder>();
        for (AuthorityShare au : authorityShares) {
            authorityIds.add(au.getId());
            if (au.getOpenType() == OpenType.FILE.getType()) {
                fileIds.add(au.getOpenId());
            }
            if (au.getOpenType() == OpenType.FOLDER.getType()) {
                Folder folder = new Folder();
                folder.setFolderCode(au.getFolder().getFolderCode());
                folder.setEmployeeId(au.getFolder().getEmployeeId());
                folders.add(folder);
            }
        }
        //删除用户文件夹权限相关信息（分享信息）
        if (authorityIds.size() > 0) {
            authorityShareDao.delBatchById(authorityIds);
        }
        //删除文件
        if (fileIds.size() > 0) {
            fileDao.delBatchById(fileIds);
        }
        //删除文件夹 及 下级所有树节点
        if (folders.size() > 0) {
            for (Folder fo : folders) {
                folderDao.delByFolderCode(fo.getFolderCode());
            }
        }
        return 1;
    }

    @Override
    public int getOpenAuthById(Employee employee, Integer openId,
            Integer openType) {
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setShareEid("" + employee.getId());
        query.setShareDid(employee.getDeptId());
        query.setWorkgroupEmployeeId(employee.getId());
        query.setOpenId(openId);
        query.setOpenType(openType);
        List<AuthorityShare> authList = authorityShareDao.selectOpenAuth(query);
        Integer checkTmp = 0;
        //针对同一文件,同一个用户,有可能有多条权限,取最大的
        if (authList != null && authList.size() > 0) {
            int authTmp = 99;
            for (AuthorityShare share : authList) {
                authTmp = share.getOperAuth() < authTmp ? share.getOperAuth() : authTmp;
            }
            checkTmp = authTmp;
        }
        return checkTmp;
    }

    @Override
    public List<File> getKeyFile(AuthorityShareQuery query) {
        return authorityShareDao.selectKeyFileByQuery(query);
    }

    @Override
    public Folder getKeyFolder(AuthorityShareQuery query) {
        List<Folder> folders = authorityShareDao.selectKeyFolderByQuery(query);
        List<File> files = authorityShareDao.selectKeyFileByQuery(query);
        Folder folder = new Folder();
        folder.setFolders(folders);
        folder.setFiles(files);
        return folder;
    }

    @Override
    public List<File> getBKeyFile(AuthorityShareQuery query) {
        return authorityShareDao.selectBKeyFileByQuery(query);
    }

    @Override
    public Folder getBKeyFolder(AuthorityShareQuery query) {
        List<Folder> folders = authorityShareDao.selectBKeyFolderByQuery(query);
        List<File> files = authorityShareDao.selectBKeyFileByQuery(query);
        //遍历去重，留下文件中权限最高的一列
        List<File> minFiles = new ArrayList<File>();
        Map<Integer, File> fileMap = new HashMap<Integer, File>();
        for (File file : files) {
            File tmpFile = fileMap.get(file.getId());
            if (tmpFile == null) {
                fileMap.put(file.getId(), file);
            }
            else if (tmpFile.getOperAuth() > file.getOperAuth()) {
                fileMap.put(file.getId(), file);
                minFiles.add(tmpFile);
            }
            else {
                minFiles.add(file);
            }
        }
        files.removeAll(minFiles);
        //遍历去重，留下文件夹中权限最高的一列
        List<Folder> minFolder = new ArrayList<Folder>();
        Map<Integer, Folder> folderMap = new HashMap<Integer, Folder>();
        for (Folder folder : folders) {
            Folder tmpFolder = folderMap.get(folder.getId());
            if (tmpFolder == null) {
                folderMap.put(folder.getId(), folder);
            }
            else if (tmpFolder.getOperAuth() > folder.getOperAuth()) {
                folderMap.put(folder.getId(), folder);
                minFolder.add(tmpFolder);
            }
            else {
                minFolder.add(folder);
            }
        }
        folders.removeAll(minFolder);
        //放入
        Folder folder = new Folder();
        folder.setFolders(folders);
        folder.setFiles(files);
        return folder;
    }

    @Override
    public int delAuthorityShare(Integer openId, Integer openType,
            Employee employee) {
        //修改文件or文件夹分享状态
        if (openType == OpenType.FILE.getType()) {
            File file = new File();
            file.setId(openId);
            file.setShareStatus(ShareStatus.NONE.getStatus());
            fileDao.update(file);
            if (employee != null) {
                //添加用户日志
                logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                        file.getFileName(), ActionType.RESHARE, employee);
            }
        }
        else if (openType == OpenType.FOLDER.getType()) {
            Folder folder = new Folder();
            folder.setId(openId);
            folder.setShareStatus(ShareStatus.NONE.getStatus());
            folderDao.update(folder);
            //添加用户日志
            if (employee != null) {
                logManager.insertEmpLoyeeLog(folder.getId(), OpenType.FOLDER,
                        folder.getFolderName(), ActionType.RESHARE, employee);
            }
        }
        //移除权限
        AuthorityShare authorityShare = new AuthorityShare();
        authorityShare.setOpenId(openId);
        authorityShare.setOpenType(openType);
        return authorityShareDao.delByKeyId(authorityShare);
    }

    @Override
    public int delAuthorityShareBatch(AuthReq authReq, Employee employee) {
        for (AuthorityShare share : authReq.getShares()) {
            //修改文件or文件夹分享状态
            if (share.getOpenType() == OpenType.FILE.getType()) {
                File file = new File();
                file.setId(share.getOpenId());
                file.setShareStatus(ShareStatus.NONE.getStatus());
                fileDao.update(file);
                file = fileDao.selectById(file.getId());
                logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                        file.getFileName(), ActionType.RESHARE, employee);
            }
            else if (share.getOpenType() == OpenType.FOLDER.getType()) {
                Folder folder = new Folder();
                folder.setId(share.getOpenId());
                folder.setShareStatus(ShareStatus.NONE.getStatus());
                folderDao.update(folder);
                folder = folderDao.selectById(folder.getId());
                logManager.insertEmpLoyeeLog(folder.getId(), OpenType.FOLDER,
                        folder.getFolderName(), ActionType.RESHARE, employee);
            }
            authorityShareDao.delByKeyId(share);
        }
        return 1;
    }

    @Override
    public int delAuthorityShareById(List<AuthorityShare> authorityShares) {
        for (AuthorityShare authorityShare : authorityShares) {
            authorityShareDao.delById(authorityShare.getId());
        }
        return 1;
    }

    @Override
    public int delAuthorityShareById(Integer id) {
        return authorityShareDao.delById(id);
    }

    @Override
    @Transactional
    public int addAuthorityShare(AuthHelper authHelper, Employee employee) {
        //checkShareAuth(authHelper, employee);
        //获取父文件夹ID
        if (authHelper.isAll()) {
            //添加全部分享
            AuthorityShare authorityShare = getAuthorityShare(authHelper, null,
                    ShareType.ALLCOM.getType(), employee);
            return authorityShareDao.insert(authorityShare);
        }
        else {
            //开始涉及部门分享
            boolean isExitsDeptIds = false;;
            if (authHelper.getDeptIds() != null
                    && authHelper.getDeptIds().size() > 0) {
                for (String deptId : authHelper.getDeptIds()) {
                    AuthorityShare authorityShare = getAuthorityShare(
                            authHelper, deptId, ShareType.DEPTMENT.getType(),
                            employee);
                    authorityShareDao.insert(authorityShare);
                    isExitsDeptIds = true;
                }
            }
            //开始涉及员工分享
            if (authHelper.getEmployees() != null
                    && authHelper.getEmployees().size() > 0) {
                for (Employee emp : authHelper.getEmployees()) {
                    if (!(isExitsDeptIds && authHelper.getDeptIds().contains(
                            emp.getDeptId()))) {
                        //包含
                        AuthorityShare authorityShare = getAuthorityShare(
                                authHelper, emp.getId().toString(),
                                ShareType.EMPLOYEE.getType(), employee);
                        authorityShareDao.insert(authorityShare);
                    }
                }
            }
            //开始涉及工作组分享 (不用排除已加入的员工，分享只与工作组关联)
            if (authHelper.getWorkgroupIds() != null
                    && authHelper.getWorkgroupIds().size() > 0) {
                for (Integer workgroupId : authHelper.getWorkgroupIds()) {
                    AuthorityShare authorityShare = getAuthorityShare(
                            authHelper, workgroupId.toString(),
                            ShareType.WORKGROUP.getType(), employee);
                    authorityShareDao.insert(authorityShare);
                }
            }
        }
        //最终查一次是否该对象存在分享
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setOpenId(authHelper.getOpenId());
        query.setOpenType(authHelper.getOpenType());
        query.setEmployeeId(employee.getId());
        List<AuthorityShare> authorityShares = authorityShareDao.selectByQuery(query);
        if (authorityShares == null || authorityShares.size() == 0) {
            //更新
            if (OpenType.FOLDER.getType() == authHelper.getOpenType()) {
                //文件夹
                Folder folder = folderDao.selectById(authHelper.getOpenId());
                folder.setShareStatus(ShareStatus.NONE.getStatus());
                folderDao.update(folder);
            }
            else {
                //文件
                File file = fileDao.selectById(authHelper.getOpenId());
                file.setShareStatus(ShareStatus.NONE.getStatus());
                fileDao.update(file);
            }
        }
        return 0;
    }

    //设置
    private AuthorityShare getAuthorityShare(AuthHelper authHelper,
            String shareId, Integer shareType, Employee employee) {
        AuthorityShare authorityShare = new AuthorityShare();
        int parentFolderId = 0;
        if (OpenType.FOLDER.getType() == authHelper.getOpenType()) {
            //文件夹
            Folder folder = folderDao.selectById(authHelper.getOpenId());
            folder.setShareStatus(ShareStatus.ALREAY.getStatus());
            folderDao.update(folder);
            if (folder != null) {
                parentFolderId = folder.getId();
            }
            authorityShare.setFolderCode(folder.getFolderCode());
            logManager.insertEmpLoyeeLog(folder.getId(), OpenType.FOLDER,
                    folder.getFolderName(), ActionType.SHARE, employee);
        }
        else {
            //文件
            File file = fileDao.selectById(authHelper.getOpenId());
            file.setShareStatus(ShareStatus.ALREAY.getStatus());
            fileDao.update(file);
            if (file != null) {
                parentFolderId = file.getFolderId();
            }
            authorityShare.setFolderCode(file.getFolderCode());
            logManager.insertEmpLoyeeLog(file.getFolderId(), OpenType.FILE,
                    file.getFileName(), ActionType.SHARE, employee);
        }
        authorityShare.setEmployeeId(employee.getId());
        authorityShare.setFolderId(parentFolderId);
        authorityShare.setOperAuth(authHelper.getAuthType());
        authorityShare.setOpenId(authHelper.getOpenId());
        authorityShare.setOpenType(authHelper.getOpenType());
        authorityShare.setShareType(shareType);
        authorityShare.setOperAuth(authHelper.getAuthType());
        authorityShare.setShareId(shareId);
        return authorityShare;
    }

    @Override
    @Transactional
    public int addMoreAuthorityShare(AuthHelper authHelper, Employee employee) {
        Folder folder = authHelper.getFolder();
        if (folder != null && folder.getFiles() != null
                && folder.getFiles().size() > 0) {
            for (File file : folder.getFiles()) {
                authHelper.setOpenId(file.getId());
                authHelper.setOpenType(OpenType.FILE.getType());
                addAuthorityShare(authHelper, employee);
            }
        }
        if (folder != null && folder.getFolders() != null
                && folder.getFolders().size() > 0) {
            for (Folder subfolder : folder.getFolders()) {
                authHelper.setOpenId(subfolder.getId());
                authHelper.setOpenType(OpenType.FOLDER.getType());
                addAuthorityShare(authHelper, employee);
            }
        }
        return 0;
    }

    @Override
    public AuthHelper getAuthHelperByAuth(AuthHelper authHelper,
            Employee employee) {
        //在查看
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setOpenId(authHelper.getOpenId());
        query.setOpenType(authHelper.getOpenType());
        query.setOperAuth(authHelper.getAuthType());
        List<AuthorityShare> authorityShares = authorityShareDao.selectByQuery(query);
        if (authorityShares != null && authorityShares.size() > 0) {
            for (AuthorityShare authorityShare : authorityShares) {
                if (ShareType.ALLCOM.getType() == authorityShare.getShareType()) {
                    authHelper.setAll(true);
                }
                else if (ShareType.DEPTMENT.getType() == authorityShare.getShareType()) {
                    authHelper.getDeptIds().add(authorityShare.getShareId());
                }
                else if (ShareType.EMPLOYEE.getType() == authorityShare.getShareType()) {
                    Employee emp = new Employee();
                    emp.setId(Integer.parseInt(authorityShare.getShareId()));
                    authHelper.getEmployees().add(emp);
                }
                else if (ShareType.WORKGROUP.getType() == authorityShare.getShareType()) {
                    authHelper.getWorkgroupIds().add(
                            Integer.parseInt(authorityShare.getShareId()));
                }
            }
        }
        else {
            return null;
        }
        return authHelper;
    }

    @Override
    public void checkShareAuth(AuthHelper authHelper, Employee employee) {
        //在查看
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setOpenId(authHelper.getOpenId());
        query.setOpenType(authHelper.getOpenType());
        List<AuthorityShare> authorityShares = authorityShareDao.selectByQuery(query);
        if (authorityShares != null && authorityShares.size() > 0) {
            for (AuthorityShare authorityShare : authorityShares) {
                if (authHelper.getAuthType() != authorityShare.getOperAuth()) {
                    //判断有其他权限 判断是否包含
                    if (authorityShare.getShareType() == ShareType.ALLCOM.getType()
                            || authHelper.isAll()) {
                        //全公司直接提示错误
                        throw new MyException(DiskException.AUTHERROR);
                    }
                    else if (authorityShare.getShareType() == ShareType.DEPTMENT.getType()) {
                        //部门包含
                        if (authHelper.getDeptIds().size() > 0) {
                            //判断是否包含
                            for (String deptId : authHelper.getDeptIds()) {
                                if (deptId.length() > authorityShare.getShareId().length()) {
                                    //判断长短
                                    String parentId = deptId.substring(
                                            0,
                                            authorityShare.getShareId().length());
                                    if (authorityShare.getShareId().equals(
                                            parentId)) {
                                        throw new MyException(
                                                DiskException.AUTHERROR);
                                    }
                                }
                                else {
                                    //判断长短
                                    String parentId = authorityShare.getShareId().substring(
                                            0, deptId.length());
                                    if (deptId.equals(parentId)) {
                                        throw new MyException(
                                                DiskException.AUTHERROR);
                                    }
                                }
                            }
                        }
                        //检测员工
                        if (authHelper.getEmployees().size() > 0) {
                            //判断是否包含
                            for (Employee emp : authHelper.getEmployees()) {
                                //只需要判断emp是否属于已经分享的部门内
                                String deptId = emp.getDeptId();
                                if (deptId.length() >= authorityShare.getShareId().length()) {
                                    String parentId = deptId.substring(
                                            0,
                                            authorityShare.getShareId().length());
                                    if (authorityShare.getShareId().equals(
                                            parentId)) {
                                        throw new MyException(
                                                DiskException.AUTHERROR);
                                    }
                                }
                            }
                        }
                    }
                    else if (authorityShare.getShareType() == ShareType.EMPLOYEE.getType()) {
                        if (authHelper.getDeptIds().size() > 0) {
                            Employee emp = employeeDao.selectById(authorityShare.getShareId());
                            if (authHelper.getDeptIds().contains(
                                    emp.getDeptId())) {
                                throw new MyException(DiskException.AUTHERROR);
                            }
                        }
                        else if (authHelper.getEmployees().size() > 0) {
                            //判断是否包含
                            for (Employee emp : authHelper.getEmployees()) {
                                String deptId = emp.getDeptId();
                                if (deptId.length() >= authorityShare.getShareId().length()) {
                                    String parentId = deptId.substring(
                                            0,
                                            authorityShare.getShareId().length());
                                    if (authorityShare.getShareId().equals(
                                            parentId)) {
                                        throw new MyException(
                                                DiskException.AUTHERROR);
                                    }
                                }
                                //只需要判断emp是否属于已经分享的部门内
                                if (emp.getId() == Integer.parseInt(authorityShare.getShareId())) {
                                    throw new MyException(
                                            DiskException.AUTHERROR);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<AuthHelper> mergeAuthHelper(List<AuthHelper> authHelpers) {
        Map<Integer, List<AuthHelper>> dataMap = new HashMap<Integer, List<AuthHelper>>();
        //合并同类型
        if (authHelpers != null && authHelpers.size() > 0) {
            for (AuthHelper authHelper : authHelpers) {
                if (dataMap.containsKey(authHelper.getAuthType())) {
                    List<AuthHelper> helpers = dataMap.get(authHelper.getAuthType());
                    helpers.add(authHelper);
                }
                else {
                    List<AuthHelper> helpers = new ArrayList<AuthHelper>();
                    helpers.add(authHelper);
                    dataMap.put(authHelper.getAuthType(), helpers);
                }
            }
        }
        //对同类型的数据去重
        List<AuthHelper> list = new ArrayList<AuthHelper>();
        for (Integer key : dataMap.keySet()) {
            List<AuthHelper> listAuth = dataMap.get(key);
            if (listAuth != null && listAuth.size() > 0) {
                boolean isAll = false;
                for (AuthHelper authHelper : listAuth) {
                    if (authHelper.isAll()) {
                        isAll = true;
                        list.add(authHelper);
                        break;
                    }
                }
                if (!isAll) {
                    //计算部门ID
                    List<String> deptIds = new ArrayList<String>();
                    Set<String> deptIdSet = new HashSet<String>();
                    AuthHelper auth = new AuthHelper();
                    for (AuthHelper authHelper : listAuth) {
                        deptIdSet.addAll(authHelper.getDeptIds());
                        auth = authHelper;
                    }
                    deptIds.addAll(deptIdSet);
                    auth.setDeptIds(deptIds);
                    //计算员工
                    List<Employee> employees = new ArrayList<Employee>();
                    for (AuthHelper authHelper : listAuth) {
                        if (authHelper.getEmployees() != null
                                && authHelper.getEmployees().size() > 0) {
                            for (Employee employee : authHelper.getEmployees()) {
                                if (employees != null && employees.size() > 0) {
                                    //判断是否存在
                                    boolean isExits = false;
                                    for (Employee emp : employees) {
                                        if (employee.getId().equals(emp.getId())) {
                                            isExits = true;
                                        }
                                    }
                                    if (!isExits) {
                                        employees.add(employee);
                                    }
                                }
                                else {
                                    employees.add(employee);
                                }
                            }
                        }
                    }
                    auth.setEmployees(employees);
                    list.add(auth);
                }
            }
        }
        return list;
    }

    @Override
    public void shareDataMessage(List<AuthHelper> authHelpers, Employee employee) {
        Set<Integer> employeeIdSet = new HashSet<Integer>();
        for (AuthHelper authHelper : authHelpers) {
            //先判断全部
            if (authHelper.isAll()) {
                //全部
                List<Employee> employees = employeeDao.selectByAll();
                if (employees != null && employees.size() > 0) {
                    if (employees != null && employees.size() > 0) {
                        for (Employee emp : employees) {
                            employeeIdSet.add(emp.getId());
                        }
                    }
                }
            }
            else {
                if (authHelper.getDeptIds() != null
                        && authHelper.getDeptIds().size() > 0) {
                    for (String deptId : authHelper.getDeptIds()) {
                        EmployeeQuery query = new EmployeeQuery();
                        query.setDeptId(deptId);
                        List<Employee> emps = employeeDao.selectByQuery(query);
                        if (emps != null && emps.size() > 0) {
                            for (Employee emp : emps) {
                                employeeIdSet.add(emp.getId());
                            }
                        }
                    }
                }
                if (authHelper.getEmployees() != null
                        && authHelper.getEmployees().size() > 0) {
                    for (Employee emp : authHelper.getEmployees()) {
                        employeeIdSet.add(emp.getId());
                    }
                }
                if (authHelper.getWorkgroupIds() != null
                        && authHelper.getWorkgroupIds().size() > 0) {
                    for (Integer workgroupId : authHelper.getWorkgroupIds()){
                        List<WorkgroupEmployee> workgroupEmployees = workgroupEmployeeDao.selectByWorkgroupId(workgroupId);
                        for (WorkgroupEmployee workgroupEmployee : workgroupEmployees){
                            employeeIdSet.add(workgroupEmployee.getEmployeeId());
                        }
                    }
                }
            }
        }
        AuthHelper authHelper = authHelpers.get(0);
        Folder folder = new Folder();;
        if (StringUtils.isNotBlank(authHelper.getData())) {
            //多个
            folder = authHelper.getFolder();
            if (folder.getFolders() != null && folder.getFolders().size() > 0) {
                List<Folder> folders = new ArrayList<Folder>();
                for (Folder zfolder : folder.getFolders()) {
                    //文件夹
                    zfolder = folderDao.selectById(zfolder.getId());
                    folders.add(zfolder);
                    folder.setFolders(folders);
                }
            }
            if (folder.getFiles() != null && folder.getFiles().size() > 0) {
                List<File> files = new ArrayList<File>();
                for (File file : folder.getFiles()) {
                    //文件夹
                    file = fileDao.selectById(file.getId());
                    files.add(file);
                    folder.setFiles(files);
                }
            }
        }
        else {
            //单个
            if (OpenType.FOLDER.getType() == authHelper.getOpenType()) {
                List<Folder> folders = new ArrayList<Folder>();
                //文件夹
                Folder zfolder = folderDao.selectById(authHelper.getOpenId());
                folders.add(zfolder);
                folder.setFolders(folders);
            }
            else {
                List<File> files = new ArrayList<File>();
                File file = fileDao.selectById(authHelper.getOpenId());
                files.add(file);
                folder.setFiles(files);
            }
        }
        List<Integer> employeeIds = new ArrayList<Integer>();
        employeeIds.addAll(employeeIdSet);
        messageService.sendShareMessage(employee, employeeIds, folder);
    }

    @Override
    public int delByAuthorityShare(AuthorityShare authorityShare) {
        return authorityShareDao.delByKeyId(authorityShare);
    }

    @Override
    public Map<String, AuthRelationHelper> getAuthorityShareRelationName(
            AuthorityShareQuery authorityShareQuery) {
        List<AuthorityShare> list = authorityShareDao.selectRelationNameByOpenId(authorityShareQuery);
        Map<String, AuthRelationHelper> map = new HashMap<String, AuthRelationHelper>();
        AuthRelationHelper manageAuthRHelper = new AuthRelationHelper();
        AuthRelationHelper seeAuthRHelper = new AuthRelationHelper();
        AuthRelationHelper readAuthRHelper = new AuthRelationHelper();
        if (list != null && list.size() > 0) {
            for (AuthorityShare authorityShare : list) {
                if (AuthorityType.MANAGER.getCode() == authorityShare.getOperAuth()) {
                    if (ShareType.DEPTMENT.getType() == authorityShare.getShareType()) {
                        manageAuthRHelper.getDeptNames().add(
                                authorityShare.getShareDeptName());
                    }
                    else if (ShareType.EMPLOYEE.getType() == authorityShare.getShareType()) {
                        manageAuthRHelper.getEmployeeNames().add(
                                authorityShare.getShareEmployeeName());
                    }
                    else if (ShareType.WORKGROUP.getType() == authorityShare.getShareType()) {
                        manageAuthRHelper.getWorkgroupNames().add(
                                authorityShare.getWorkgroupName());
                    }
                    else if (ShareType.ALLCOM.getType() == authorityShare.getShareType()) {
                        manageAuthRHelper.setAll(true);
                    }
                }
                else if (AuthorityType.SEE.getCode() == authorityShare.getOperAuth()) {
                    if (ShareType.DEPTMENT.getType() == authorityShare.getShareType()) {
                        seeAuthRHelper.getDeptNames().add(
                                authorityShare.getShareDeptName());
                    }
                    else if (ShareType.EMPLOYEE.getType() == authorityShare.getShareType()) {
                        seeAuthRHelper.getEmployeeNames().add(
                                authorityShare.getShareEmployeeName());
                    }
                    else if (ShareType.WORKGROUP.getType() == authorityShare.getShareType()) {
                        seeAuthRHelper.getWorkgroupNames().add(
                                authorityShare.getWorkgroupName());
                    }
                    else if (ShareType.ALLCOM.getType() == authorityShare.getShareType()) {
                        seeAuthRHelper.setAll(true);
                    }
                }
                else if (AuthorityType.READ.getCode() == authorityShare.getOperAuth()) {
                    if (ShareType.DEPTMENT.getType() == authorityShare.getShareType()) {
                        readAuthRHelper.getDeptNames().add(
                                authorityShare.getShareDeptName());
                    }
                    else if (ShareType.EMPLOYEE.getType() == authorityShare.getShareType()) {
                        readAuthRHelper.getEmployeeNames().add(
                                authorityShare.getShareEmployeeName());
                    }
                    else if (ShareType.WORKGROUP.getType() == authorityShare.getShareType()) {
                        readAuthRHelper.getWorkgroupNames().add(
                                authorityShare.getWorkgroupName());
                    }
                    else if (ShareType.ALLCOM.getType() == authorityShare.getShareType()) {
                        readAuthRHelper.setAll(true);
                    }
                }
            }
        }
        map.put("manager", manageAuthRHelper);
        map.put("see", seeAuthRHelper);
        map.put("read", readAuthRHelper);
        return map;
    }

    @Override
    @Transactional
    public int delWorkgroupAuthorityShare(Integer workgroupId) {
        //查询关联工作组的文件or文件夹的所有分享
        List<AuthorityShare> authorityShares = authorityShareDao.selectAllAuthorityByWorkgroupId(workgroupId);
        if(authorityShares != null && authorityShares.size() > 0){
            Set<Integer> fileIds = new HashSet<Integer>();
            Set<Integer> folderIds = new HashSet<Integer>();
            //创建移除对象list
            Set<Integer> removeFileIds = new HashSet<Integer>();
            Set<Integer> removeFolderIds = new HashSet<Integer>();
            for(AuthorityShare authorityShare : authorityShares){
                if(authorityShare.getOpenType() == OpenType.FILE.getType()){
                    fileIds.add(authorityShare.getOpenId());
                    //若该分享不是此工作组，说明对应的文件or文件夹有分享
                    if(!(authorityShare.getShareType() == ShareType.WORKGROUP.getType() && authorityShare.getShareId().equals(""+workgroupId))){
                        removeFileIds.add(authorityShare.getOpenId());
                    }
                }else if(authorityShare.getOpenType() == OpenType.FOLDER.getType()){
                    folderIds.add(authorityShare.getOpenId());
                    if(!(authorityShare.getShareType() == ShareType.WORKGROUP.getType() && authorityShare.getShareId().equals(""+workgroupId))){
                        removeFolderIds.add(authorityShare.getOpenId());
                    }
                }
            }
            //移除，得到可修改成无分享的文件or文件夹
            fileIds.removeAll(removeFileIds);
            folderIds.removeAll(removeFolderIds);
            //修改文件or文件夹的分享状态
            if(fileIds.size() >0){
                for(Integer fileId : fileIds){
                    File file = new File();
                    file.setId(fileId);
                    file.setShareStatus(ShareStatus.NONE.getStatus());
                    fileDao.update(file);
                }
            }
            if(folderIds.size() >0){
                for(Integer folderId : folderIds){
                    Folder folder = new Folder();
                    folder.setId(folderId);
                    folder.setShareStatus(ShareStatus.NONE.getStatus());
                    folderDao.update(folder);
                }
            }
        }
        //删除分享关联
        AuthorityShare authorityShare = new AuthorityShare();
        authorityShare.setShareType(ShareType.WORKGROUP.getType());
        authorityShare.setShareId(""+workgroupId);
        authorityShareDao.delByShareId(authorityShare);
        return 0;
    }
}
