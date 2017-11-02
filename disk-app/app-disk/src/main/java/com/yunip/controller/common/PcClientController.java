/*
 * 描述：PC客户端请求控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-10-17
 */
package com.yunip.controller.common;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.service.IFolderService;

/**
 * PC客户端请求控制器层
 */
@Controller
@RequestMapping("/pcclient")
public class PcClientController extends BaseController {
    
    @Resource(name = "iFolderService")
    private IFolderService folderService;
    
    /**
     * 刷新快捷上传目录
     */
    @RequestMapping("/refreshQuickUploadFolder")
    public String refreshQuickUploadFolder(HttpServletRequest request){
        Employee employee = super.getEmployee(request);
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setFolderName(SystemContant.INITFOLDERNAME);
        folderQuery.setEmployeeId(employee.getId());
        List<Folder> folders = folderService.getFolderInfoListByQuery(folderQuery);
        if(folders != null && folders.size() > 0){
            Folder folder = folders.get(0);
            return "redirect:/personal/index?folderId=" + folder.getId();
        }else{
            return "redirect:/personal/index";
        }
    }
}
