/*
 * 描述：消息通知控制器
 * 创建人：jian.xiong
 * 创建时间：2016-8-17
 */
package com.yunip.controller.disk;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.MessageType;
import com.yunip.model.company.Employee;
import com.yunip.model.sys.query.MessageQuery;
import com.yunip.service.IMessageService;

/**
 * 消息通知控制器
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController{
    @Resource(name = "iMessageService")
    private IMessageService messageService;

    /**
     * 消息通知列表
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, MessageQuery query, ModelMap modelMap){
        Employee employee = super.getEmployee(request);
        query.setSendeeEmployeeId(employee.getId());
        query.setOrderby("send_time");
        query.setDesc(true);
        query = messageService.getMessageList(query);
        modelMap.put("query", query);
        modelMap.put("typeList", MessageType.getMessageTypeList());
        return "message/index";
    }
    
    /**
     * 查询用户未读消息数量
     */
    @RequestMapping("/getEmployeeUnreadMessageNum")
    @ResponseBody
    public int getEmployeeUnreadMessageNum(HttpServletRequest request){
        Employee employee = super.getEmployee(request);
        return messageService.getUnreadMessageNum(employee.getId());
    }
    
    /**
     * 删除消息
     */
    @RequestMapping("/delMessage")
    @ResponseBody
    public JsonData<Boolean> delMessage(HttpServletRequest request,@RequestBody List<Integer> ids){
        JsonData<Boolean> jsonData = new JsonData<Boolean>();
        jsonData.setResult(false);
        Employee employee = super.getEmployee(request);
        boolean result = messageService.batchDelMessage(employee.getId(), ids);
        jsonData.setResult(result);
        return jsonData;
    }
    
    /**
     * 标记已读
     * @param request
     * @param ids 消息id集合
     */
    @RequestMapping("/markRead")
    @ResponseBody
    public JsonData<Boolean> markRead(HttpServletRequest request,@RequestBody List<Integer> ids){
        JsonData<Boolean> jsonData = new JsonData<Boolean>();
        Employee employee = super.getEmployee(request);
        messageService.batchMarkRead(employee.getId(), ids);
        return jsonData;
    }
}
