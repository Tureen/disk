package com.yunip.controller.company;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReader;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.SystemCode;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.authority.CpPermission;
import com.yunip.model.authority.query.RoleQuery;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.DepartmentQuery;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.log.AdminLog;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IAdminLogService;
import com.yunip.service.ICpPermissionService;
import com.yunip.service.ICpRoleService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.authority.IRoleService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Md5;
import com.yunip.web.common.Authority;

/**
 * @author Administrator
 * 部门管理
 */
@Controller
@RequestMapping("/dept")
public class DepartmentController extends BaseController{
    
    /**
     * 用户功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminService")
    private IAdminService      adminService;
    
    /**
     * 部门功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iDepartmentService")
    private IDepartmentService      departmentService;
    
    /**
     * 员工功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    /**
     * 管理员or员工操作日志 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminLogService")
    private IAdminLogService adminLogService;
    
    /**
     * 角色功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iRoleService")
    private IRoleService       roleService;
    
    /**
     * 员工角色Service
     */
    @Resource(name = "iCpRoleService")
    private ICpRoleService cpRoleService;
    
    /**
     * 员工权限Service
     */
    @Resource(name = "iCpPermissionService")
    private ICpPermissionService cpPermissionService;
    
    /***
     * 部门列表
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,DepartmentQuery query) {
        query = departmentService.queryDepartment(query);
        request.setAttribute("query", query);
        return "dept/index";
    }

    /***
     * 部门树列表
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/tree")
    public String tree(HttpServletRequest request) {
        List<Department> list = departmentService.getAllDeparts();
        request.setAttribute("depts", list);
        return "dept/tree";
    }
    
    /***
     * 跳转添加或者编辑页面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/addoredit")
    public String addOrEdit(HttpServletRequest request, String parentId, String id) {
        List<Department> list = departmentService.getAllDeparts();
        if(StringUtils.isNotBlank(parentId)){
            Department parentDept = departmentService.getDepartmentById(parentId);
            request.setAttribute("parent",parentDept);
        }
        if(StringUtils.isNotBlank(id)){
            Department department = departmentService.getDepartmentById(id);
            request.setAttribute("department",department);
        }
        if(!StringUtils.isNotBlank(parentId) && StringUtils.isNotBlank(id)){
            return "dept/edit";
        }
        request.setAttribute("depts", list);
        return "dept/addoredit";
    }
    
    /**
     * 部门树关联右侧员工列表
     * @param request
     * @param deptId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/treeright/{deptId}")
    public String treeRight(HttpServletRequest request, @PathVariable String deptId,EmployeeQuery query) {
        query.setDeptId(deptId);
        query.setIsAdmin(IsAdminType.YG.getType());
        query = employeeService.getEmployeeByQuery(query);
        Department department = departmentService.getDepartmentById(deptId);
        request.setAttribute("department", department);
        request.setAttribute("query", query);
        request.setAttribute("adminType", IsAdminType.values());
        return "dept/treeright";
    }
    
    /***
     * 添加部门
     * @param request
     * @param department
     * @return
     */
    @RequestMapping("/addDept")
    @ResponseBody
    public JsonData<Department> addDept(HttpServletRequest request,Department department){
        JsonData<Department> data = new JsonData<Department>();
        department.setCreateAdmin(getMyinfo(request).getEmployeeName());
        department = departmentService.addDepartment(department);
        data.setResult(department);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.DEPTMANAGE.getType());
        adminLog.setOperContent("部门添加:"+department.getDeptName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /***
     * 编辑部门
     * @param request
     * @param department 部门对象
     * @return
     */
    @RequestMapping("/editDept")
    @ResponseBody
    public JsonData<?> editDept(HttpServletRequest request,Department department){
        JsonData<Department> data = new JsonData<Department>();
        department.setUpdateAdmin(getMyinfo(request).getEmployeeName());
        departmentService.updateDepartment(department);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.DEPTMANAGE.getType());
        adminLog.setOperContent("部门修改:"+department.getDeptName());
        adminLogService.addAdminLog(adminLog);
        return data;   
    }
    
    /***
     * 删除部门
     * @param request
     * @param deptId  删除部门的ID
     * @return
     */
    @RequestMapping("/delDept/{deptId}")
    @ResponseBody
    public JsonData<?> delDept(HttpServletRequest request,@PathVariable String deptId){
       JsonData<?> data = new JsonData<Department>();
       Department department = departmentService.getDepartmentById(deptId);
       int count = departmentService.delDepartment(deptId);
       if(count == 0){
           data.setCode(AdminException.ERROR.getCode());
           data.setCodeInfo(AdminException.ERROR.getMsg());
       }
       //日志添加
       AdminLog adminLog = getAdminLog(request);
       adminLog.setActionType(AdminActionType.DEPTMANAGE.getType());
       adminLog.setOperContent("部门删除:"+department.getDeptName());
       adminLogService.addAdminLog(adminLog);
       return data;   
    }
    
    /**
     * 部门树关联右侧员工列表
     * @param request
     * @param deptId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toImport/{deptId}")
    public String toImport(HttpServletRequest request, @PathVariable String deptId) {
        Department department = departmentService.getDepartmentById(deptId);
        request.setAttribute("department", department);
        return "dept/import";
    }
    
    /**
     * 导出excel
     * @param request  
     * void 
     * @exception
     */
    @RequestMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, EmployeeQuery query) throws Exception{
        query.setPageSize(Integer.MAX_VALUE);
        query.setIsAdmin(IsAdminType.YG.getType());
        query = employeeService.getEmployeeByQuery(query);
        String templatePath = request.getSession().getServletContext().getRealPath("/") + "/download/template.xlsx";
        Map<String, List<Employee>> beanParams = new HashMap<String, List<Employee>>();
        beanParams.put("employees", query.getList());
        XLSTransformer former = new XLSTransformer();
        InputStream in=null;  
        OutputStream out=null;  
        //设置响应  
        String fileName=new String(("员工基本信息数据").getBytes("gb2312"), "iso8859-1")+ ".xlsx";
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("utf-8");
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEEXPORT.getType());
        adminLog.setOperContent("员工导出");
        adminLogService.addAdminLog(adminLog);
        try {  
            in=new BufferedInputStream(new FileInputStream(templatePath));  
            Workbook workbook= former.transformXLS(in, beanParams);
            out=response.getOutputStream();  
            //将内容写入输出流并把缓存的内容全部发出去  
            workbook.write(out);  
            out.flush();  
        } catch (InvalidFormatException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (in!=null) in.close();  
            if (out!=null) out.close(); 
        } 
    }
    
    /**
     * 导入excel
     * @param request  
     * void 
     * @exception
     */
    @RequestMapping("/import")
    @ResponseBody
    public JsonData<?> importExcel(HttpServletRequest request, HttpServletResponse response, EmployeeQuery query) throws Exception{
        Admin admin = super.getMyinfo(request);
        JsonData<Object> data = new JsonData<Object>();
        String templatePath = request.getSession().getServletContext().getRealPath("/") + "/download/employee.xml";
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;     
        MultipartFile file = multipartRequest.getFile("file"); 
        //构建xml文件输入流 
        InputStream inputXML = new FileInputStream(templatePath); 
        //绑定xml文件 
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML); 
        //通过低级流构建，高级流 
        BufferedInputStream bis=new BufferedInputStream(file.getInputStream()); 
        Employee employee = new Employee();
        List<Employee> employees = new ArrayList<Employee>(); 
        Map<String, Object> beans = new HashMap<String, Object>(); 
        beans.put("employee", employee); 
        beans.put("employees", employees); 
        //通过XSLReader 的read 
        mainReader.read(bis, beans); 
        employeeService.checkRegisterLimit(IsAdminType.YG.getType(),employees.size()-2);//首两行不是数据，减去
        //进行数据存储
        employeeService.importEmployees(employees, query.getDeptId() ,admin);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEIMPORT.getType());
        adminLog.setOperContent("员工导入");
        adminLogService.addAdminLog(adminLog);
        return data; 
    }
    
    /**
     * toInsertOrUpdateAdminPage(跳转新增员工页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @Authority("P54")
    @RequestMapping("/toinsert/{isAdmin}")
    public String toInsertAdminPage(HttpServletRequest request, @PathVariable
    Integer isAdmin) {
        //所有角色获取
        RoleQuery queryTmp = new RoleQuery();
        queryTmp.setSystemCode(SystemCode.OTM.getType());
        queryTmp.setPageSize(Integer.MAX_VALUE);
        queryTmp = roleService.queryRoles(queryTmp);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        //查询所有权限
        List<CpPermission> permissions = cpPermissionService.getPermissions();
        String spaceSize = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                SystemContant.PERSONAL_SPACE);
        request.setAttribute("spaceSize", spaceSize);
        request.setAttribute("permissions", permissions);
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("roleList", queryTmp.getList());
        request.setAttribute("departmentList", departmentList);
        request.setAttribute("type", 1);
        return "dept/saveoredit";
    }

    /**
     * toInsertOrUpdateAdminPage(跳转修改员工页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @Authority("P55")
    @RequestMapping("/toupdate/{isAdmin}/{adminId}")
    public String toUpdateAdminPage(HttpServletRequest request, @PathVariable
    Integer isAdmin, @PathVariable Integer adminId) {
        if (adminId != 0) {
            Employee employee = employeeService.getEmployeeById(adminId);
            AdminQuery query = new AdminQuery();
            query.setId(adminId);
            List<Admin> admins = adminService.queryAdmins(query).getList();
            List<AdminRole> roleList = adminService.getAdminRole(adminId);
            //获取角色串
            StringBuilder roleStr = new StringBuilder();
            for (AdminRole ar : roleList) {
                roleStr.append(ar.getRoleId() + ",");
            }
            if (roleStr.length() > 0) {
                roleStr.deleteCharAt(roleStr.length() - 1);
            }
            //获取权限串
            String permissionStr = cpRoleService.getPermissionIdStrByEmployeeId(adminId);
            request.setAttribute("roleStr", roleStr.toString());
            request.setAttribute("permissionStr", permissionStr);
            request.setAttribute("admin",
                    admins.size() > 0 ? admins.get(0) : null);
            request.setAttribute("employee", employee);
        }
        //所有角色获取
        RoleQuery queryTmp = new RoleQuery();
        queryTmp.setSystemCode(SystemCode.OTM.getType());
        queryTmp.setPageSize(Integer.MAX_VALUE);
        queryTmp = roleService.queryRoles(queryTmp);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        //查询所有权限
        List<CpPermission> permissions = cpPermissionService.getPermissions();
        String spaceSize = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                SystemContant.PERSONAL_SPACE);
        request.setAttribute("spaceSize", spaceSize);
        request.setAttribute("permissions", permissions);
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("roleList", queryTmp.getList());
        request.setAttribute("departmentList", departmentList);
        request.setAttribute("type", 2);
        return "dept/saveoredit";
    }

    /**
     * insertAdmin(保存员工) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/save")
    @Authority("P54")
    @ResponseBody
    public JsonData<?> insertAdmin(HttpServletRequest request,
            Employee employee, Admin admin) {
        //限制员工注册数量
        employeeService.checkRegisterLimit(admin.getIsAdmin(),null);
        
        JsonData<?> data = new JsonData<String>();
        employeeService.checkEmployeeMobile(employee.getEmployeeMobile(), null,
                admin.getIsAdmin());
        employeeService.checkEmployeeCode(employee.getEmployeeCode(), null,
                admin.getIsAdmin());
        employee.setCreateAdmin(getMyinfo(request).getEmployeeName());
        employeeService.addEmployee(employee, admin);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
        adminLog.setOperContent("添加员工:" + employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * editAdmin(修改员工) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/edit")
    @Authority("P55")
    @ResponseBody
    public JsonData<?> editAdmin(HttpServletRequest request, Employee employee,
            Admin admin) {
        JsonData<?> data = new JsonData<String>();
        employeeService.checkEmployeeMobile(employee.getEmployeeMobile(),
                employee.getId(), admin.getIsAdmin());
        employeeService.checkEmployeeCode(employee.getEmployeeCode(),
                employee.getId(), admin.getIsAdmin());
        employee.setUpdateAdmin(getMyinfo(request).getEmployeeName());
        employeeService.updateEmployee(employee, admin);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
        adminLog.setOperContent("修改员工:" + employee.getEmployeeName()
                + " 的资料");
        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * 冻结员工
     * @param request
     * @param adminId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @Authority("P56")
    @ResponseBody
    @RequestMapping("/freeze/{isAdmin}/{adminId}")
    public JsonData<Integer> freeze(HttpServletRequest request, @PathVariable Integer isAdmin, @PathVariable Integer adminId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        adminService.updateValidStatus(adminId, ValidStatus.FREEZE.getStatus());
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
        adminLog.setOperContent("冻结员工:" + employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }

    /**
     * 解冻员工
     * @param request
     * @param adminId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @Authority("P57")
    @ResponseBody
    @RequestMapping("/nomal/{isAdmin}/{adminId}")
    public JsonData<Integer> nomal(HttpServletRequest request, @PathVariable Integer isAdmin, @PathVariable Integer adminId) {
        //限制员工注册数量
        employeeService.checkRegisterLimit(isAdmin,null);
        
        JsonData<Integer> jsonData = new JsonData<Integer>();
        adminService.updateValidStatus(adminId, ValidStatus.NOMAL.getStatus());
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
        adminLog.setOperContent("开启员工:" + employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }

    /**
     * 删除员工
     * @param request
     * @param adminId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P58")
    @ResponseBody
    @RequestMapping("/delete/{isAdmin}/{adminId}")
    public JsonData<?> deleteAdmin(HttpServletRequest request, @PathVariable
    Integer isAdmin, @PathVariable
    Integer adminId) {
        JsonData<String> data = new JsonData<String>();
        Employee employee = employeeService.getEmployeeById(adminId);
        employeeService.deleteEmployeeById(adminId);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
        adminLog.setOperContent("删除员工:" + employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * 重置密码
     * @throws Exception 
     * @param request
     * @param adminId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P59")
    @ResponseBody
    @RequestMapping("/initPwd/{adminId}")
    public JsonData<?> initPwd(HttpServletRequest request, @PathVariable
    Integer adminId) throws Exception {
        JsonData<String> data = new JsonData<String>();
        AdminQuery query = new AdminQuery();
        query.setId(adminId);
        Admin admin = adminService.getLoginAdmin(query);
        String pwd = Md5.encodeByMd5("123456");
        admin.setAccountPwd(pwd);
        adminService.updateAdmin(admin);
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEINITPASSWD.getType());
        adminLog.setOperContent("员工密码重置:" + employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }
}
