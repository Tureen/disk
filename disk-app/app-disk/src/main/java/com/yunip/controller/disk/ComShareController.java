/*
 * 描述：〈公共空间〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-13
 */
package com.yunip.controller.disk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.common.IdentityIndex;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.DiskException;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.FolderReq;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.IFolderService;
import com.yunip.service.user.IAdminService;

@Controller
@RequestMapping("/comshare")
public class ComShareController extends BaseController {

    @Resource(name = "iAuthorityShareService")
    private IAuthorityShareService authorityShareService;

    @Resource(name = "iFolderService")
    private IFolderService         folderService;

    @Resource(name = "iAdminService")
    private IAdminService          adminService;

    /**
     * 公共空间页
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,FolderQuery folderQuery) {
        Employee employee = getEmployee(request);
        //普通用户的公共空间
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setShareEid(""+employee.getId());
        query.setShareDid(employee.getDeptId());
        query.setEmployeeId(SysContant.COMMON_MANAGE_EMPLOYEE_ID);
        query.setWorkgroupEmployeeId(employee.getId());
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        FolderReq folderReq = authorityShareService.getBShare(query,employee.getId());
        //将直接分享文件夹属性存入session，用做权限判断
        Map<Integer, Integer> authFolderMap = new HashMap<Integer, Integer>();
        List<AuthorityShare> authorityShares = folderReq.getAuthorityShares();
        for (AuthorityShare au : authorityShares) {
            if (au.getFolder() != null) {
                Integer oldOperAuth = authFolderMap.get(au.getFolder().getId());
                if(oldOperAuth == null || oldOperAuth > au.getOperAuth()){
                    authFolderMap.put(au.getFolder().getId(), au.getOperAuth());
                }
            }
        }
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PUBLIC_SPACE.getCode());
        //放入request
        request.getSession().setAttribute(SysContant.COMMON_OPEN_AUTH, authFolderMap);
        request.setAttribute("folder", folderReq);
        request.setAttribute("enum", AuthorityType.values());
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "comshare/iconindex";
        }
        return "comshare/index";
    }
    
    /**
     * 点击公共共享文件夹，展示下级所有文件夹及文件
     * @param request
     * @param upId
     * @param folderId
     * @param employeeId
     * @return  
     * String 
     * @exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/indexinto/{upId}/{folderId}/{employeeId}")
    public String intoIndexFolder(HttpServletRequest request,@PathVariable Integer upId,@PathVariable Integer folderId,@PathVariable Integer employeeId){
        //权限验证 获取session中的最上级文件夹id，从前部操作的session中取出map并取出对应的文件夹分享权限
        Integer operAuth = 0;
        Map<String, Integer> authFolderMap = (Map<String, Integer>) request.getSession().getAttribute(SysContant.COMMON_OPEN_AUTH);
        if (authFolderMap.get(upId) == null) {
            throw new MyException(DiskException.NOPERMISSION);
        } else {
            operAuth = authFolderMap.get(upId);
        }
        //子目录下所有文件及文件夹查询
        FolderQuery query = new FolderQuery();
        query.setFolderId(folderId);
        query.setEmployeeId(SysContant.COMMON_MANAGE_EMPLOYEE_ID);
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        Folder folder = folderService.getSubFolder(query);
        List<Folder> folders = folderService.getParentFolders(folderId);
        //判断当前目录是否是upId所在目录的下级
        boolean b = false;
        for(Folder fo : folders){
            if(fo.getId().equals(upId)){
                b = true;
                break;
            }
        }
        if(!b){
            throw new MyException(DiskException.NOPERMISSION);
        }
        //判断父文件夹集合中不存在于权限map中的上级
        List<Folder> foldersListTmp = new ArrayList<Folder>();
        if(folders != null && folders.size() > 0){
            for(Folder fo : folders){
                if (authFolderMap.get(fo.getId()) == null) {
                    foldersListTmp.add(fo);
                }else{
                    break;
                }
            }
            folders.removeAll(foldersListTmp);
        }
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PUBLIC_SPACE.getCode());
        //放入requert
        request.setAttribute("folders", folders);
        request.setAttribute("upId", upId);
        request.setAttribute("operAuth", operAuth);//如果不存在文件夹的权限，采用上层权限
        request.setAttribute("folder", folder);
        request.setAttribute("enum", AuthorityType.values());
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "comshare/iconinto";
        }
        return "comshare/indexinto";
    }
    
    /**
     * 搜索，公共共享的文件夹
     * @throws UnsupportedEncodingException 
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/indexkey")
    public String quertyIndexKey(HttpServletRequest request,AuthorityShareQuery query) throws UnsupportedEncodingException{
        if(query.getQueryName() == null || query.getQueryName().equals("")){
            FolderQuery folderQuery = new FolderQuery();
            index(request,folderQuery);
            return "comshare/index";
        }
        if(StringUtils.isNotBlank(query.getQueryName())){
            String queryName = URLDecoder.decode(query.getQueryName(),SystemContant.encodeType);
            query.setQueryName(queryName.trim());
        }
        query.setShareEid(""+getEmployee(request).getId());
        query.setShareDid(getEmployee(request).getDeptId());
        query.setEmployeeId(SysContant.COMMON_MANAGE_EMPLOYEE_ID);
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        Folder folder = authorityShareService.getBKeyFolder(query);
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PUBLIC_SPACE.getCode());
        request.setAttribute("folder", folder);
        request.setAttribute("enum", AuthorityType.values());
        request.setAttribute("query", query);
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "comshare/iconlistkey";
        }
        return "comshare/indexkey";
    }
    
}
