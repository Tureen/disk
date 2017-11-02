/*
 * 描述：〈标签〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-11
 */
package com.yunip.controller.disk;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Sign;
import com.yunip.model.disk.query.FileSignQuery;
import com.yunip.model.disk.query.SignQuery;
import com.yunip.model.disk.support.FileSignReq;
import com.yunip.model.disk.support.SignReq;
import com.yunip.service.IFileService;
import com.yunip.service.ISignService;
import com.yunip.utils.json.JsonUtils;

@Controller
@RequestMapping("/sign")
public class SignController extends BaseController {

    @Resource(name = "iSignService")
    private ISignService signService;

    @Resource(name = "iFileService")
    private IFileService fileService;

    /**
     * 标签列表页
     * @throws ParseException 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, SignQuery signQuery) throws ParseException {
        Employee employee = getEmployee(request);
        //查询标签
        signQuery.setEmployeeId(employee.getId());
        signQuery = signService.querySign(signQuery);
        //建立标签库名称数组，用于页面判断重复名称
        List<Sign> allList = signService.getSignByEmployeeId(employee.getId());
        String[] strArr = new String[allList.size()];
        for (int i = 0; i < allList.size(); i++) {
            strArr[i] = allList.get(i).getSignName();
        }
        request.setAttribute("strArr", JsonUtils.getJSONString(strArr));
        //放入request
        request.setAttribute("fileServiceUrl", SystemContant.FILE_SERVICE_URL);
        request.setAttribute("signQuery", signQuery);
        return "sign/index";
    }
    
    @RequestMapping("/loadTable")
    public String loadTable(HttpServletRequest request,  FileSignQuery fileSignQuery) {
        Employee employee = getEmployee(request);
        //查询标签对应文件
        fileSignQuery.setEmployeeId(employee.getId());
        fileSignQuery = signService.queryFileSign(fileSignQuery);
        //放入request
        request.setAttribute("fileSignQuery", fileSignQuery);
        return "sign/loadTable";
    }

    /**
     * 所有标签页（文件标签关联时的弹出页）
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/list/{fileId}")
    public String getSign(HttpServletRequest request, @PathVariable
    Integer fileId) {
        Employee employee = super.getEmployee(request);
        List<Sign> signList = signService.getSignByFileId(fileId);
        List<Sign> allList = signService.getSignByEmployeeId(employee.getId());
        File file = fileService.getFileById(fileId);
        //建立标签库名称数组，用于页面判断重复名称
        String[] strArr = new String[allList.size()];
        for (int i = 0; i < allList.size(); i++) {
            strArr[i] = allList.get(i).getSignName();
        }
        request.setAttribute("strArr", JsonUtils.getJSONString(strArr));
        request.setAttribute("signList", signList);
        request.setAttribute("allList", allList);
        request.setAttribute("file", file);
        return "sign/listsign";
    }

    /**
     * 添加标签
     * @param request
     * @param sign
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> insertSign(HttpServletRequest request, Sign sign) {
        JsonData<Sign> data = new JsonData<Sign>();
        Employee employee = getEmployee(request);
        sign.setEmployeeId(employee.getId());
        sign.setCreateAdmin(employee.getEmployeeName());
        sign.setUpdateAdmin(employee.getEmployeeName());
        signService.addSign(sign);
        data.setResult(sign);
        return data;
    }
    
    /**
     * 编辑标签
     * @param request
     * @param sign
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/edit")
    @ResponseBody
    public JsonData<?> updateSign(HttpServletRequest request, Sign sign){
        JsonData<Sign> data = new JsonData<Sign>();
        signService.updateSign(sign);
        data.setResult(sign);
        return data;
    }

    /**
     * 添加文件与标签关联
     * @param request
     * @param fileSign
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/savefile")
    @ResponseBody
    public JsonData<?> addFileSign(HttpServletRequest request, @RequestBody
    SignReq signReq) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = getEmployee(request);
        signReq.setEmployeeId(employee.getId());
        signReq.setCreateAdmin(employee.getEmployeeName());
        signReq.setUpdateAdmin(employee.getEmployeeName());
        signService.addFileSign(signReq);
        return data;
    }

    /**
     * 删除标签
     * @param request
     * @param signId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/delete")
    @ResponseBody
    public JsonData<?> deleteSign(HttpServletRequest request, Integer signId) {
        JsonData<?> data = new JsonData<String>();
        signService.delSign(signId);
        return data;
    }
    
    /**
     * 删除标签与文件关联
     * 
     * @param request
     * @param fileSignReq
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/delfilesign")
    @ResponseBody
    public JsonData<?> deleteFileSign(HttpServletRequest request, @RequestBody FileSignReq fileSignReq){
        JsonData<?> data = new JsonData<String>();
        signService.delFileSign(fileSignReq.getFileSigns());
        return data;
    }

}
