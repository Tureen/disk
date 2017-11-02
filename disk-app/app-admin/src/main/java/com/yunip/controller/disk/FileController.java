package com.yunip.controller.disk;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.controller.base.BaseController;
import com.yunip.model.company.Department;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IFileService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

    @Resource(name = "iFileService")
    private IFileService fileService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    /***
     * 查看上传文件
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/look")
    public String look(HttpServletRequest request, FileQuery query) {
      //属性值过滤
        replaceStr(query);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        String endDate = query.getEndDate();
        if(StringUtils.isNotBlank(query.getEndDate())){
            //在结束日期中加上一天
            Date endDateTime = DateUtil.parseDate(query.getEndDate(), DateUtil.YMD_DATA) ;
            query.setEndDate(DateUtil.getDateFormat(DateUtil.getAddDates(endDateTime, 1), DateUtil.YMD_DATA));
        }
        query = fileService.getFilesByQuery(query);
        query.setEndDate(endDate);
        request.setAttribute("departmentList", departmentList);
        request.setAttribute("query", query);
        return "file/look";
    }
    
  //对象属性值过滤
    private void replaceStr(Object object){
        Field fields[] = object.getClass().getDeclaredFields();//获得对象所有属性
        for (Field field : fields) {
            String key = field.getName();
            Class<?> type = field.getType();
            if("serialVersionUID".equals(key) || "validStatus".equals(key) ||  "pFileId".equals(key)){
                continue;
            }
            try {
                key = StringUtil.captureName(key);
                Method m = object.getClass().getMethod("get" + key);
                String value = (String)m.invoke(object); //调用getter方法获取属性值
                if(StringUtil.nullOrBlank(value)){
                    continue;
                }
                //过滤属性值(包含.)
                String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
                Pattern p = Pattern.compile(regEx); 
                Matcher mat = p.matcher(value);
                value = mat.replaceAll("").trim();
                //赋值
                Method m2 = object.getClass().getMethod("set"+key, type);
                m2.invoke(object, value);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
