/*
 * 描述：〈分享空间〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-10
 */
package com.yunip.controller.disk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.EnumSpaceType;
import com.yunip.enums.common.IdentityIndex;
import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.FolderType;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.AuthorityShareQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.AuthReq;
import com.yunip.model.disk.support.FolderReq;
import com.yunip.service.IAuthorityShareService;
import com.yunip.service.ICpRoleService;
import com.yunip.service.IFolderService;

@Controller
@RequestMapping("/share")
public class ShareController extends BaseController{

    @Resource(name = "iAuthorityShareService")
    private IAuthorityShareService authorityShareService;
    
    @Resource(name = "iFolderService")
    private IFolderService folderService;
    
    /**
     * 员工角色Service
     */
    @Resource(name = "iCpRoleService")
    private ICpRoleService cpRoleService;
    
    /**
     * 已共享的文件
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String quertySendShare(HttpServletRequest request){
        AuthorityShareQuery query = new AuthorityShareQuery();
        query.setEmployeeId(getEmployee(request).getId());
        query.setFolderType(FolderType.PERSONAL.getType());
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        FolderReq folderReq = authorityShareService.getShare(query);
        //获取用户的个人空间权限串
        String permissionStr = cpRoleService.getPermissionIdStrByEmployeeId(getEmployee(request).getId());
        request.setAttribute("permissionStr", permissionStr);
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PRIVATE_SPACE.getCode());
        request.setAttribute("folder", folderReq);
        request.setAttribute("authorityTypes", AuthorityType.values());
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "share/iconindex";
        }
        return "share/index";
    }
    
    /**
     * 点击已共享文件夹，展示下级所有文件夹及文件
     * @param request
     * @param folderId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/indexinto/{folderId}")
    public String intoSendFolder(HttpServletRequest request,@PathVariable Integer folderId){
        FolderQuery query = new FolderQuery();
        query.setFolderId(folderId);
        query.setEmployeeId(getEmployee(request).getId());
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        Folder folder = folderService.getSubFolder(query);
        List<Folder> folders = folderService.getParentFolders(folderId);
        //获取用户的个人空间权限串
        String permissionStr = cpRoleService.getPermissionIdStrByEmployeeId(getEmployee(request).getId());
        request.setAttribute("permissionStr", permissionStr);
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PRIVATE_SPACE.getCode());
        request.setAttribute("folders", folders);
        request.setAttribute("folder", folder);
        request.setAttribute("enum", AuthorityType.values());
        request.setAttribute("authorityTypes", AuthorityType.values());
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "share/iconinto";
        }
        return "share/into";
    }
    
    /**
     * 搜索，已共享的文件夹
     * @throws UnsupportedEncodingException 
     * @return  request
     * @return  request
     * String 
     * @exception
     */
    @RequestMapping("/listkey")
    public String quertySendShareByKey(HttpServletRequest request,AuthorityShareQuery query) throws UnsupportedEncodingException{
        if(query.getQueryName() == null || query.getQueryName().equals("")){
            quertySendShare(request);
            return "share/index";
        }
        if(StringUtils.isNotBlank(query.getQueryName())){
            String queryName = URLDecoder.decode(query.getQueryName(),SystemContant.encodeType);
            query.setQueryName(queryName.trim());
        }
        query.setEmployeeId(getEmployee(request).getId());
        query.setFolderType(FolderType.PERSONAL.getType());
        query.setOrderIndex(SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.SORTTYPE.getKey()));
        Folder folder = authorityShareService.getKeyFolder(query);
        //获取用户的个人空间权限串
        String permissionStr = cpRoleService.getPermissionIdStrByEmployeeId(getEmployee(request).getId());
        request.setAttribute("permissionStr", permissionStr);
        //上传下载request
        downLoadRequest(request, EnumSpaceType.PRIVATE_SPACE.getCode());
        request.setAttribute("folder", folder);
        request.setAttribute("query", query);
        request.setAttribute("authorityTypes", AuthorityType.values());
        if(IdentityIndex.ICONINDEX.getType()==checkIndexIdentification(request,""+request.getParameter("type")).getType()){
            return "share/iconlistkey";
        }
        return "share/listkey";
    }
    
    /**
     * 移除权限(批量)
     * @param request
     * @param authReq
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/moveauthbatch")
    @ResponseBody
    public JsonData<?> moveAuthorityShare(HttpServletRequest request,@RequestBody AuthReq authReq) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        if (authReq != null && authReq.getShares().size() > 0) {
            authorityShareService.delAuthorityShareBatch(authReq, employee);
        }
        return data;
    }
    
}
