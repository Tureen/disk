package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.disk.Contact;
import com.yunip.model.disk.query.ContactQuery;

public interface IContactService {

    /**
     * 添加联系人
     * @param contact
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addContact(Contact contact);
    
    @Transactional
    int addContacts(Integer employeeId,List<String> contactIds); 
    
    /**
     * 根据员工id，获取关联联系人id集合
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> getContactIdByEmployeeId(Integer employeeId);
    
    /**
     * 
     * getContactByEmployeeId(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param employeeId
     * @return  
     * List<Contact> 
     * @exception
     */
    List<Contact> getContactByEmployeeId(Integer employeeId);
    
    /**
     * 条件查询
     * queryContact(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param contactQuery
     * @return  
     * ContactQuery 
     * @exception
     */
    ContactQuery queryContact(ContactQuery query);
    
    @Transactional
    boolean delBatchContact(List<Integer> ids,Integer employeeId);
}
