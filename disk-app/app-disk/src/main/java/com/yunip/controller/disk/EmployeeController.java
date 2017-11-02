package com.yunip.controller.disk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.DiskException;
import com.yunip.manage.FileManager;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.image.ImageCut;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.ImgCompress;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/user")
public class EmployeeController extends BaseController {

    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;

    @Resource(name = "iAdminService")
    private IAdminService    adminService;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;

    /**
     * 个人设置
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        Employee employee = employeeService.getEmployeeById(super.getEmployee(request).getId());
        fileManager.spaceInfo(request, employee);
        request.setAttribute("employee", employee);
        return "user/index";
    }

    /**
     * 修改个人信息
     * @param request
     * @param employee
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/update")
    @ResponseBody
    public JsonData<?> update(HttpServletRequest request, Employee employee) {
        JsonData<?> data = new JsonData<String>();
        employeeService.updateEmployeeById(employee);
        //重新设置session中的employee
        Employee newEmployee = employeeService.getEmployeeById(super.getEmployee(
                request).getId());
        super.setEmployee(request, newEmployee);
        return data;
    }

    /**
     * 修改密码
     * @param request
     * @param password
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/updatepass")
    @ResponseBody
    public JsonData<?> updatePass(HttpServletRequest request,
            HttpServletResponse response, String password) {
        JsonData<?> data = new JsonData<String>();
        Admin admin = new Admin();
        admin.setId(super.getEmployee(request).getId());
        admin.setAccountPwd(password);
        adminService.updateAdmin(admin);
        //清除session中的employee
        super.removeEmployee(request);
        //清除cookie
        super.removeLoginCookie(request, response);
        return data;
    }
    
    /**
     * 修改空间分配
     * @param request
     * @param employee
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/updateSpace")
    @ResponseBody
    public JsonData<?> updateSpace(HttpServletRequest request, Employee employee) {
        JsonData<?> data = new JsonData<String>();
        if(StringUtil.nullOrBlank(employee.getTeamworkSpaceSize())){
            employee.setTeamworkSpaceSize("0");
        }
        employeeService.updateEmployeeById(employee);
        //重新设置session中的employee
        Employee newEmployee = employeeService.getEmployeeById(super.getEmployee(
                request).getId());
        super.setEmployee(request, newEmployee);
        return data;
    }

    /**
     * 核验密码
     * @throws Exception 
     * @param request
     * @param password
     * @param newPassword
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/check")
    @ResponseBody
    public JsonData<?> checkPass(HttpServletRequest request,
            HttpServletResponse response, String password, String newPassword,
            String confirmPassword) throws Exception {
        JsonData<?> data = new JsonData<String>();
        
        //于2017-3-7增加需求：以test开头的帐号不允许更改密码(为了防止演示环境下，test前缀开头的演示账户被修改密码)。
        Employee currentEmployee = super.getEmployee(request);
        if(currentEmployee.getEmployeeMobile().startsWith("test")){
            throw new MyException(DiskException.NOPERMISSION);
        }
        
        if (password == null || "".equals(password)) {
            throw new MyException(DiskException.NOPASSWORD);
        }
        if (newPassword == null || "".equals(newPassword)) {
            throw new MyException(DiskException.NONEWPASSWORD);
        }
        if (confirmPassword == null || "".equals(confirmPassword)) {
            throw new MyException(DiskException.NOCONFIRMPASSWORD);
        }
        if (!confirmPassword.equals(newPassword)) {
            throw new MyException(DiskException.NOSAMEPASS);
        }
        AdminQuery query = new AdminQuery();
        query.setPassword(Md5.encodeByMd5(password));
        query.setId(super.getEmployee(request).getId());
        query = adminService.queryAdmins(query);
        List<Admin> list = query.getList();
        if (list.size() == 0) {
            //旧密码核验失败，数据库与之对应的用户密码不一致
            throw new MyException(DiskException.NOPASSCHECKPASSWORD);
        }
        //修改密码
        data = updatePass(request, response, Md5.encodeByMd5(newPassword));
        return data;
    }

    /**
     * 加载头像页面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/popage")
    public String popage(HttpServletRequest request) {
        Employee employee = super.getEmployee(request);
        request.setAttribute("employee", employee);
        return "user/popage";
    }
    
    /**
     * 加载头像页面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/portrait")
    public void portrait(HttpServletRequest request, HttpServletResponse response) {
        Employee employee = super.getEmployee(request);
        try{
            // path是指欲下载的文件的路径。
            String imgFilePath = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH); 
            imgFilePath = imgFilePath + SystemContant.HEADERPATH;
            File file = new File(imgFilePath + employee.getEmployeePortrait());
            if(!file.exists()){
                return;
            }
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(imgFilePath + employee.getEmployeePortrait()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request) throws Exception{
        String allowType = ".png|.jpg|.jpeg";
        Employee employee = super.getEmployee(request);
        int x = Integer.parseInt(request.getParameter("x"));
        int y = Integer.parseInt(request.getParameter("y"));
        int w = Integer.parseInt(request.getParameter("w"));
        int h = Integer.parseInt(request.getParameter("h"));
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;     
        // 获得文件：
        String imgFilePath = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH); 
        imgFilePath = imgFilePath + SystemContant.HEADERPATH;
        MultipartFile file = multipartRequest.getFile("file"); 
        String suffix = ImgCompress.getFileSuffix(file.getInputStream());  
        String fileName = UUID.randomUUID() +"." + suffix;
        File targetFile = new File(imgFilePath, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
         // 读取图片文件  
            ImageCut.imgCut(targetFile.getPath(), x, y, w, h);
            employee.setEmployeePortrait(targetFile.getName());
            employeeService.updateEmployeeById(employee);
            super.setEmployee(request, employee);
            //删除
        } catch (Exception e) {
            throw new MyException(DiskException.UPLOADFAIL);
        }
        return "redirect:/user/index";
    }
    
    
}
