/*
 * 描述：消息（通知）业务逻辑接口的实现
 * 创建人：jian.xiong
 * 创建时间：2016-8-17
 */
package com.yunip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.enums.disk.MessageStatus;
import com.yunip.enums.disk.MessageType;
import com.yunip.enums.disk.WorkgroupApplyStatus;
import com.yunip.mapper.disk.IWorkgroupApplyDao;
import com.yunip.mapper.disk.IWorkgroupDao;
import com.yunip.mapper.disk.IWorkgroupEmployeeDao;
import com.yunip.mapper.sys.IMessageDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.WorkgroupApply;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.sys.Message;
import com.yunip.model.sys.query.MessageQuery;
import com.yunip.service.IMessageService;
import com.yunip.utils.util.StringUtil;

/**
 * 消息（通知）业务逻辑接口的实现
 */
@Service("iMessageService")
public class MessageServiceImpl implements IMessageService{
    @Resource(name = "iMessageDao")
    private IMessageDao messageDao;
    
    @Resource(name = "iWorkgroupDao")
    private IWorkgroupDao workgroupDao;
    
    @Resource(name = "iWorkgroupApplyDao")
    private IWorkgroupApplyDao workgroupApplyDao;
    
    @Resource(name = "iWorkgroupEmployeeDao")
    private IWorkgroupEmployeeDao workgroupEmployeeDao;
    
    @Override
    @Transactional
    public boolean addMessage(Message bean) {
        return messageDao.insert(bean) > 0;
    }
    
    @Transactional
    public void sendShareMessage(Employee employee, List<Integer> sendeeEmployeeIds, Folder folder) {
        if(sendeeEmployeeIds != null && sendeeEmployeeIds.size() > 0){
            String msgContent = getShareMessageContent(employee, folder);
            if(StringUtil.nullOrBlank(msgContent)){
                return;
            }
            StringBuffer fileIds = new StringBuffer();
            StringBuffer folderIds = new StringBuffer();
            if(folder.getFiles() != null && folder.getFiles().size() > 0){
                for(File fileTmp : folder.getFiles()){
                    fileIds.append(fileTmp.getId()+",");
                }
                fileIds.deleteCharAt(fileIds.length() - 1);
            }
            if(folder.getFolders() != null && folder.getFolders().size() > 0){
                for(Folder folderTmp : folder.getFolders()){
                    folderIds.append(folderTmp.getId()+",");
                }
                folderIds.deleteCharAt(folderIds.length() - 1);
            }
            String[] temp = msgContent.split("\\|");
            if(temp != null && temp.length == 2){
                String title = temp[0];
                String content = temp[1];
                for(Integer sendeeEmployeeId : sendeeEmployeeIds){
                    if(sendeeEmployeeId == employee.getId()){//发送人包含自己时，不给自己发送消息
                        continue;
                    }
                    Message bean = new Message();
                    bean.setMsgType(MessageType.FXXX.getCode());
                    bean.setTitle(title);
                    bean.setContent(content);
                    bean.setStatus(MessageStatus.WD.getCode());
                    bean.setSenderEmployeeId(employee.getId());
                    bean.setSendeeEmployeeId(sendeeEmployeeId);
                    bean.setSendTime(new Date());
                    bean.setFileIds(fileIds.toString());
                    bean.setFolderIds(folderIds.toString());
                    messageDao.insert(bean);
                }
            }
        }
    }
    
    @Override
    @Transactional
    public void sendWorkgroupApplyMessage(Employee employee, Integer workgroupId, Integer workgroupApplyId) {
        Workgroup workgroup = workgroupDao.selectById(workgroupId);
        String msgContent = getWorkgroupApplyMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            Message bean = new Message();
            bean.setMsgType(MessageType.GZZSQ.getCode());
            bean.setTitle(title);
            bean.setContent(content);
            bean.setStatus(MessageStatus.WD.getCode());
            bean.setSenderEmployeeId(employee.getId());
            bean.setSendeeEmployeeId(workgroup.getEmployeeId());
            bean.setSendTime(new Date());
            bean.setCommonId(workgroupApplyId);
            messageDao.insert(bean);
        }
    }

    @Override
    @Transactional
    public void sendWorkgroupReviewedMessage(Employee employee, WorkgroupApply workgroupApply) {
        Workgroup workgroup = workgroupDao.selectById(workgroupApply.getWorkgroupId());
        String msgContent = getWorkgroupReviewedMessageContent(employee, workgroup, workgroupApply);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            Message bean = new Message();
            bean.setMsgType(MessageType.GZZSH.getCode());
            bean.setTitle(title);
            bean.setContent(content);
            bean.setStatus(MessageStatus.WD.getCode());
            bean.setSenderEmployeeId(employee.getId());
            bean.setSendeeEmployeeId(workgroupApply.getApplyEmployeeId());
            bean.setSendTime(new Date());
            bean.setCommonId(workgroupApply.getId());
            messageDao.insert(bean);
        }
    }
    
    @Override
    @Transactional
    public void sendWorkgroupQuitMessage(Employee employee, Integer workgroupId) {
        Workgroup workgroup = workgroupDao.selectById(workgroupId);
        String msgContent = getWorkgroupQuitMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            Message bean = new Message();
            bean.setMsgType(MessageType.TCGZZ.getCode());
            bean.setTitle(title);
            bean.setContent(content);
            bean.setStatus(MessageStatus.WD.getCode());
            bean.setSenderEmployeeId(employee.getId());
            bean.setSendeeEmployeeId(workgroup.getEmployeeId());
            bean.setSendTime(new Date());
            bean.setCommonId(workgroupId);
            messageDao.insert(bean);
        }
    }
    
    @Override
    @Transactional
    public void sendWorkgroupDeleteMessage(Employee employee, Integer workgroupId) {
        Workgroup workgroup = workgroupDao.selectById(workgroupId);
        String msgContent = getWorkgroupDeleteMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            List<WorkgroupEmployee> workgroupEmployees = workgroupEmployeeDao.selectByWorkgroupId(workgroupId);
            if(workgroupEmployees != null && workgroupEmployees.size() > 0){
                for(WorkgroupEmployee workgroupEmployee : workgroupEmployees){
                    Message bean = new Message();
                    bean.setMsgType(MessageType.GZZJS.getCode());
                    bean.setTitle(title);
                    bean.setContent(content);
                    bean.setStatus(MessageStatus.WD.getCode());
                    bean.setSenderEmployeeId(employee.getId());
                    bean.setSendeeEmployeeId(workgroupEmployee.getEmployeeId());
                    bean.setSendTime(new Date());
                    bean.setCommonId(workgroupId);
                    messageDao.insert(bean);
                }
            }
        }
    }
    
    @Override
    @Transactional
    public void sendWorkgroupTransferMessage(Employee employee, Employee bEmployee, Integer workgroupId) {
        Workgroup workgroup = workgroupDao.selectById(workgroupId);
        String msgContent = getWorkgroupTransferMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            Message bean = new Message();
            bean.setMsgType(MessageType.GZZZR.getCode());
            bean.setTitle(title);
            bean.setContent(content);
            bean.setStatus(MessageStatus.WD.getCode());
            bean.setSenderEmployeeId(employee.getId());
            bean.setSendeeEmployeeId(bEmployee.getId());
            bean.setSendTime(new Date());
            bean.setCommonId(workgroupId);
            messageDao.insert(bean);
        }
    }
    
    @Override
    @Transactional
    public void sendWorkgroupSaveBatchMessage(Employee employee, Integer workgroupId,
            List<String> addIds, List<String> removeIds) {
        Workgroup workgroup = workgroupDao.selectById(workgroupId);
        sendWorkgroupAddMessage(employee, addIds, workgroup);
        sendWorkgroupRemoveMessage(employee, removeIds, workgroup);
    }
    
    //工作组：发送消息至被加入的人
    private void sendWorkgroupAddMessage(Employee employee, List<String> addIds, Workgroup workgroup){
        String msgContent = getWorkgroupAddMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            if(addIds != null && addIds.size() > 0){
                for(String addId : addIds){
                    Integer id = Integer.valueOf(addId);
                    Message bean = new Message();
                    bean.setMsgType(MessageType.GZZXX.getCode());
                    bean.setTitle(title);
                    bean.setContent(content);
                    bean.setStatus(MessageStatus.WD.getCode());
                    bean.setSenderEmployeeId(employee.getId());
                    bean.setSendeeEmployeeId(id);
                    bean.setSendTime(new Date());
                    bean.setCommonId(workgroup.getId());
                    messageDao.insert(bean);
                }
            }
        }
    }
    
    //工作组：发送消息至被移除的人
    private void sendWorkgroupRemoveMessage(Employee employee, List<String> removeIds, Workgroup workgroup){
        String msgContent = getWorkgroupRemoveMessageContent(employee, workgroup);
        String[] temp = msgContent.split("\\|");
        if(temp != null && temp.length == 2){
            String title = temp[0];
            String content = temp[1];
            if(removeIds != null && removeIds.size() > 0){
                for(String removeId : removeIds){
                    Integer id = Integer.valueOf(removeId);
                    Message bean = new Message();
                    bean.setMsgType(MessageType.GZZXX.getCode());
                    bean.setTitle(title);
                    bean.setContent(content);
                    bean.setStatus(MessageStatus.WD.getCode());
                    bean.setSenderEmployeeId(employee.getId());
                    bean.setSendeeEmployeeId(id);
                    bean.setSendTime(new Date());
                    bean.setCommonId(workgroup.getId());
                    messageDao.insert(bean);
                }
            }
        }
    }
    
    /**
     * 生成分享消息的标题和内容
     * @param employee 发送人
     * @param folder 分享目录对象(包含分享的目录+文件)
     */
    private String getShareMessageContent(Employee employee, Folder folder){
        String result = "";
        String template = "{senderName}向您分享了{content}";
        if(folder != null){
            StringBuffer title = new StringBuffer("");
            String content = "\r";
            
            //拼接文件内容
            List<File> files = folder.getFiles();
            if(files != null && files.size() > 0){
                title.append(files.size() + "个文件");
                StringBuffer fileContent = new StringBuffer("文件：");
                for(int i = 0;i < files.size();i++){
                    fileContent.append(files.get(i).getFileName() + "、");
                }
                content += fileContent.substring(0, fileContent.length() - 1);
            }
            
            //拼接文件夹内容
            List<Folder> folders = folder.getFolders();
            if(folders != null && folders.size() > 0){
                if(!StringUtil.nullOrBlank(title.toString())){
                    title.append("，");
                }
                title.append(folders.size() + "个目录");
                StringBuffer folderContent = new StringBuffer("目录：");
                for(int i = 0;i < folders.size();i++){
                    folderContent.append(folders.get(i).getFolderName() + "、");
                }
                if(!StringUtil.nullOrBlank(content)){
                    content += "\r";
                }
                content += folderContent.substring(0, folderContent.length() - 1);
            }
            
            String msgTitle = "";
            if(!StringUtil.nullOrBlank(title.toString())){
                msgTitle = template.replace("{senderName}", employee.getEmployeeName()).replace("{content}", title);
                result = msgTitle;
            }
            String msgContent = "";
            if(!StringUtil.nullOrBlank(content)){
                msgContent = template.replace("{senderName}", employee.getEmployeeName()).replace("{content}", content);
                result += "|" + msgContent;
            }
        }
        return result;
    }
    
    /**
     * 生成工作组申请消息的标题和内容
     * @param employee 发送人
     * @param workgroupId 申请的工作组id
     */
    private String getWorkgroupApplyMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "\r";
            //拼接内容
            title += employee.getEmployeeName() + "申请加入您的工作组";
            content += employee.getEmployeeName() + "申请加入您的工作组："+workgroup.getWorkgroupName();
            result = title + "|" + content;
        }
        return result;
    }
    
    /**
     * 生成工作组申请消息的标题和内容
     * @param employee 发送人
     * @param workgroupId 申请的工作组id
     */
    private String getWorkgroupReviewedMessageContent(Employee employee, Workgroup workgroup, WorkgroupApply workgroupApply){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += "您的工作组申请已审核";
            String reviewed = "";
            for(WorkgroupApplyStatus applyStatus : WorkgroupApplyStatus.values()){
                if(workgroupApply.getApplyStatus() == applyStatus.getStatus()){
                    reviewed = applyStatus.getDesc();
                }
            }
            content += "申请的工作组：" + workgroup.getWorkgroupName() + "\n审核结果：" + reviewed;
            result = title + "|" + content;
        }
        return result;
    }
    
    /**
     * 生成退出工作组消息的标题和内容
     * @param employee
     * @param workgroup
     * @return  
     * String 
     * @exception
     */
    private String getWorkgroupQuitMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += employee.getEmployeeName() + "退出了您的工作组";
            content += employee.getEmployeeName() + "退出了您的工作组，工作组名：" + workgroup.getWorkgroupName();
            result = title + "|" + content;
        }
        return result;
    }
    
    /**
     * 生成解散工作组消息的标题和内容
     * @param employee
     * @param workgroup
     * @return  
     * String 
     * @exception
     */
    private String getWorkgroupDeleteMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += "您所在的工作组被解散";
            content +="您所在的工作组被解散\n工作组名：" + workgroup.getWorkgroupName() + "\n操作人：" + employee.getEmployeeName();
            result = title + "|" + content;
        }
        return result;
    }
    
    /**
     * 生成转让工作组消息的标题和内容
     * @param employee
     * @param workgroup
     * @return  
     * String 
     * @exception
     */
    private String getWorkgroupTransferMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += employee.getEmployeeName() + "将工作组转让给您";
            content += employee.getEmployeeName() + "将工作组转让给您" + "\n工作组名：" + workgroup.getWorkgroupName();
            result = title + "|" + content;
        }
        return result;
    }
    
    /**
     * 生成加入工作组消息的标题和内容
     * @param employee
     * @param workgroup
     * @return  
     * String 
     * @exception
     */
    private String getWorkgroupAddMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += employee.getEmployeeName() + "将您加入工作组";
            content += employee.getEmployeeName() + "将您加入工作组" + "\n工作组名：" + workgroup.getWorkgroupName() + "\n操作人：" + employee.getEmployeeName();
            result = title + "|" + content;
        }
        return result;
    }
    
    
    /**
     * 生成移除工作组消息的标题和内容
     * @param employee
     * @param workgroup
     * @return  
     * String 
     * @exception
     */
    private String getWorkgroupRemoveMessageContent(Employee employee, Workgroup workgroup){
        String result = "";
        if(workgroup != null){
            String title = "";
            String content = "";
            //拼接内容
            title += employee.getEmployeeName() + "将您移除出工作组";
            content += employee.getEmployeeName() + "将您移除出工作组" + "\n工作组名：" + workgroup.getWorkgroupName() + "\n操作人：" + employee.getEmployeeName();
            result = title + "|" + content;
        }
        return result;
    }
    
    @Override
    @Transactional
    public boolean delMessage(Integer id) {
        return messageDao.delById(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean batchDelMessage(Integer employeeId, List<Integer> ids){
        if(ids == null || ids.size() <= 0){
            return false;
        }else{
            MessageQuery query = new MessageQuery();
            query.setSendeeEmployeeId(employeeId);
            query.setIds(ids);
            return messageDao.batchDelete(query) > 0;
        }
    }

    @Override
    public MessageQuery getMessageList(MessageQuery query) {
        if(query.isPageFlg()){
            query.setRecordCount(messageDao.selectCountByQuery(query));
        }
        query.setList(messageDao.selectByQuery(query));
        return query;
    }

    @Override
    public int getUnreadMessageNum(Integer employeeId){
        int num = 0;
        MessageQuery query = new MessageQuery();
        query.setSendeeEmployeeId(employeeId);
        query.setStatus(MessageStatus.WD.getCode());
        num = messageDao.selectCountByQuery(query);
        return num;
    }
    
    @Override
    @Transactional
    public boolean batchMarkRead(Integer employeeId, List<Integer> ids){
        if(ids == null || ids.size() <= 0){
            return false;
        }else{
            MessageQuery query = new MessageQuery();
            query.setSendeeEmployeeId(employeeId);
            query.setIds(ids);
            query.setReadTime(new Date());
            return messageDao.batchMarkRead(query) > 0;
        }
    }
}
