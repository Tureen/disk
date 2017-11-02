package com.yunip.controller.log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.controller.base.BaseController;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.log.query.AdminLogQuery;
import com.yunip.service.IAdminLogService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/log")
public class AdminLogController extends BaseController {

    @Resource(name = "iAdminLogService")
    private IAdminLogService adminLogService;

    @RequestMapping("/lookadmin")
    public String adminLog(HttpServletRequest request, AdminLogQuery query) {
        //属性值过滤
        replaceStr(query);
        String endDate = query.getEndDate();
        if(StringUtils.isNotBlank(endDate)){
            //在结束日期中加上一天
            Date endDateTime = DateUtil.parseDate(query.getEndDate(), DateUtil.YMD_DATA) ;
            query.setEndDate(DateUtil.getDateFormat(DateUtil.getAddDates(endDateTime, 1), DateUtil.YMD_DATA));
        }
        //栏目名搜索
        if(!StringUtil.nullOrBlank(query.getMenuName())){
            query.setActionType(0);
            for(AdminActionType at : AdminActionType.values()){
                if((at.getDesc()).equals(query.getMenuName())){
                    query.setActionType(at.getType());
                    break;
                }
            }
        }
        query.setIsAdmin(IsAdminType.GLY.getType());
        query = adminLogService.queryAdminLog(query);
        query.setEndDate(endDate);
        request.setAttribute("query", query);
        request.setAttribute("enum", AdminActionType.values());
        return "/log/lookadmin";
    }
    
    //对象属性值过滤
    private void replaceStr(Object object){
        Field fields[] = object.getClass().getDeclaredFields();//获得对象所有属性
        for (Field field : fields) {
            String key = field.getName();
            Class<?> type = field.getType();
            if("serialVersionUID".equals(key)){
                continue;
            }
            try {
                key = StringUtil.captureName(key);
                Method m = object.getClass().getMethod("get" + key);
                String value = (String) m.invoke(object); //调用getter方法获取属性值
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
