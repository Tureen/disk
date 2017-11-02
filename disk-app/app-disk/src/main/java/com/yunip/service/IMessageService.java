/*
 * 描述：消息（通知）业务逻辑接口
 * 创建人：jian.xiong
 * 创建时间：2016-8-17
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.WorkgroupApply;
import com.yunip.model.sys.Message;
import com.yunip.model.sys.query.MessageQuery;

/**
 * 消息（通知）业务逻辑接口
 */
public interface IMessageService {
    
    /** 
     * 添加消息
     * @param bean 消息实体类
     * @return 返回显示是否添加成功
     */
    @Transactional
    boolean addMessage(Message bean);
    
    /**
     * 发送分享消息
     * @param employee 当前发送人对象
     * @param sendeeEmployeeIds 接收人结合
     * @param folder 分享目录对象(包含分享的目录+文件)
     */
    @Transactional
    void sendShareMessage(Employee employee, List<Integer> sendeeEmployeeIds, Folder folder);
    
    /**
     * 发送消息: 申请工作组
     * @param employee 当前发送人对象
     * @param workgroupId 工作组id
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupApplyMessage(Employee employee, Integer workgroupId, Integer workgroupApplyId); 
    
    /**
     * 发送消息: 工作组申请审核结果
     * @param employee 当前发送人对象
     * @param workgroupId 申请的工作组
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupReviewedMessage(Employee employee, WorkgroupApply workgroupApply); 
    
    /**
     * 发送消息：退出工作组
     * @param employee
     * @param workgroupId  
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupQuitMessage(Employee employee, Integer workgroupId);
    
    /**
     * 发送消息：解散工作组
     * @param employee
     * @param workgroupId  
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupDeleteMessage(Employee employee, Integer workgroupId);
    
    /**
     * 发送消息：转让工作组
     * @param employee
     * @param bEmployee
     * @param workgroupId  
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupTransferMessage(Employee employee, Employee bEmployee, Integer workgroupId);
    
    /**
     * 发送消息：批量添加工作组（管理）
     * @param employee 操作者
     * @param workgroupId 工作组id
     * @param addIds 新添加进的员工id
     * @param removeIds 被移除出的员工id
     * void 
     * @exception
     */
    @Transactional
    void sendWorkgroupSaveBatchMessage(Employee employee, Integer workgroupId, List<String> addIds, List<String> removeIds);
    
    /**
     * 根据消息ID，删除消息
     * @param id 消息ID
     * @return 返回消息是否删除成功  
     */
    @Transactional
    boolean delMessage(Integer id);
    
    /**
     * 批量删除消息
     * @param employeeId 员工ID
     * @param ids 消息ID集合
     * @return 返回消息是否删除成功  
     */
    @Transactional
    boolean batchDelMessage(Integer employeeId, List<Integer> ids);
    
    /**
     * 根据查询条件返回消息结果列表
     * @param query 查询条件实体类
     * @return 返回查询条件对象，结果包含在查询对象中
     */
    MessageQuery getMessageList(MessageQuery query);
    
    /**
     * 查询未读消息数量
     * @param employeeId 员工ID
     * @return 返回未读消息条数
     */
    int getUnreadMessageNum(Integer employeeId);
    
    /**
     * 批量标记消息已读
     * @param employeeId 员工ID
     * @param ids 消息ID集合
     * @return 返回消息是否标记成功  
     */
    @Transactional
    boolean batchMarkRead(Integer employeeId, List<Integer> ids);
}
