package com.yunip.controller.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.SortType;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.log.AdminActionType;
import com.yunip.manage.FileManager;
import com.yunip.model.company.Employee;
import com.yunip.model.config.SysConfig;
import com.yunip.model.config.support.BasicsIndexHelper;
import com.yunip.model.disk.support.MergeHelper;
import com.yunip.model.log.AdminLog;
import com.yunip.service.IAdminLogService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.config.ICommonBaseDataService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.http.HttpUtil;

@Controller
@RequestMapping("/basicsinfo")
public class BasicsInfoController extends BaseController {

    @Resource(name = "iCommonBaseDataService")
    private ICommonBaseDataService commonBaseDataService;

    @Resource(name = "iEmployeeService")
    private IEmployeeService       employeeService;

    @Resource(name = "fileManager")
    private FileManager            fileManager;
    
    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    /**
     * 管理员or员工操作日志 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminLogService")
    private IAdminLogService adminLogService;

    /**
     * 基础信息配置列表
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/index")
    public String listSysConfig(HttpServletRequest request) {
        HashMap<String, String> map = SysConfigHelper.getMap(SystemContant.BASICSCODE);
        request.setAttribute("map", map);
        return "basicsinfo/index";
    }

    /**
     * ip配置列表
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/toip")
    public String listIPSysConfig(HttpServletRequest request) {
        HashMap<String, String> map = SysConfigHelper.getMap(SystemContant.BASICSCODE);
        request.setAttribute("map", map);
        return "basicsinfo/ip";
    }

    /**
     * 默认注册设置
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/toregister")
    public String listRegisterSysConfig(HttpServletRequest request) {
        HashMap<String, String> map = SysConfigHelper.getMap(SystemContant.BASICSCODE);
        request.setAttribute("map", map);
        request.setAttribute("sortenum", SortType.values());
        return "basicsinfo/register";
    }

    /**
     * ad域
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/toadyun")
    public String listADYunSysConfig(HttpServletRequest request) {
        HashMap<String, String> map = SysConfigHelper.getMap(SystemContant.BASICSCODE);
        request.setAttribute("map", map);
        return "basicsinfo/adyun";
    }
    
    /**
     * 跳转合并
     * 
     * @param sysConfigQuery
     * @param configcode
     * @param modelMap
     * @return
     */
    @RequestMapping("/tomerge")
    public String listMergeSysConfig(HttpServletRequest request) {
        return "basicsinfo/merge";
    }

    /**
     * 合并
     * @param request
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/merge")
    @ResponseBody
    public JsonData<?> mergeSysConfig(HttpServletRequest request,
            MergeHelper mergeHelper) {
        JsonData<?> data = new JsonData<String>();
        //查询主用户
        Employee firstEmployee = employeeService.getEmployeeByCodeMobile(
                mergeHelper.getFirstMobile(), null);
        //查询副用户
        Employee secondEmployee = employeeService.getEmployeeByCodeMobile(
                mergeHelper.getSecondMobile(), null);
        checkMergeEmployee(firstEmployee, secondEmployee);
        //合并
        fileManager.mergeEmployee(firstEmployee, secondEmployee);
        //冻结副用户
        adminService.updateValidStatus(secondEmployee.getId(), ValidStatus.FREEZE.getStatus());
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEMERGE.getType());
        adminLog.setOperContent("员工合并:"+firstEmployee.getEmployeeName()+" 与 "+secondEmployee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * 合并用户判断
     * @param firstEmployee
     * @param secondEmployee  
     * void 
     * @exception
     */
    private void checkMergeEmployee(Employee firstEmployee,
            Employee secondEmployee) {
        if (firstEmployee == null) {
            throw new MyException(AdminException.ZYHWK);
        }
        if (secondEmployee == null) {
            throw new MyException(AdminException.FYHWK);
        }
        if (firstEmployee.getId() == secondEmployee.getId()) {
            throw new MyException(AdminException.YHCF);
        }
    }

    /**
     * 跳转新增基础信息
     * @param request
     * @param id
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/tosave")
    public String toAddSysConfig(HttpServletRequest request) {
        return "basicsinfo/add";
    }

    /**
     * 跳转编辑基础信息
     * @param request
     * @param id
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toedit")
    public String toEditSysConfig(HttpServletRequest request,
            SysConfig sysConfig) {
        SysConfig config = commonBaseDataService.getSysConfig(
                SystemContant.BASICSCODE, sysConfig.getConfigKey());
        request.setAttribute("config", config);
        return "basicsinfo/edit";
    }

    /**
     * 新增基础信息
     * @param request
     * @param sysConfig
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> addSysConfig(HttpServletRequest request,
            SysConfig sysConfig) {
        JsonData<String> data = new JsonData<String>();
        sysConfig.setConfigCode(SystemContant.BASICSCODE);
        int sub = commonBaseDataService.saveSysConfig(sysConfig);
        if (sub <= 0) {
            throw new MyException(AdminException.ERROR);
        }
        return data;
    }

    /**
     * 修改基础信息
     * 
     * @param request
     * @param modelMap
     * @return
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    @RequestMapping("/edit")
    @ResponseBody
    public JsonData<?> editSysConfig(HttpServletRequest request,
            BasicsIndexHelper helper) throws Exception {
        JsonData<String> data = new JsonData<String>();
        //遍历对象
        Field fields[] = helper.getClass().getDeclaredFields();//获得对象所有属性
        for (Field field : fields) {
            String key = field.getName();
            Method m = helper.getClass().getMethod("get" + key);
            String value = (String) m.invoke(helper); //调用getter方法获取属性值
            //若属性值为null,这次不进行修改
            if (value == null) {
                continue;
            }
            //过滤特殊字符
            String regEx="[`~!@#$%^&()+=|{}':;'\\[\\]<>?~！@#￥%……&（）——+|{}【】‘；：”“’。，、？]"; 
            Pattern p = Pattern.compile(regEx); 
            Matcher mat = p.matcher(value);
            value = mat.replaceAll("").trim();
            //创建修改的配置对象
            SysConfig sysConfig = new SysConfig();
            sysConfig.setConfigCode(SystemContant.BASICSCODE);
            sysConfig.setConfigKey(key);
            sysConfig.setConfigValue(value);
            int sub = commonBaseDataService.editSysConfig(sysConfig);
            if (sub <= 0) {
                throw new MyException(AdminException.ERROR);
            }
        }
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.SYSCONFIGUPDATE.getType());
        adminLog.setOperContent("基本配置修改");
        adminLogService.addAdminLog(adminLog);
        //重载配置
        //后台
        SysConfigHelper.reload();
        //云盘
        HttpUtil.post(
                SysConfigHelper.getValue(SystemContant.SYSTEMCODE,
                        SystemContant.SYSTEM_DISK_DOMAIN)
                        + SystemContant.RELOAD_PATH, "", "UTF-8");
        //文件服务器
        HttpUtil.post(
                SysConfigHelper.getValue(SystemContant.SYSTEMCODE,
                        SystemContant.SYSTEM_FILE_DOMAIN)
                        + SystemContant.RELOAD_PATH, "", "UTF-8");
        return data;
    }
}
