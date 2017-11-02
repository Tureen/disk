package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.disk.IContactDao;
import com.yunip.model.disk.Contact;
import com.yunip.model.disk.query.ContactQuery;
import com.yunip.service.IContactService;

@Service("iContactService")
public class ContactServiceImpl implements IContactService {

    @Resource(name = "iContactDao")
    private IContactDao contactDao;

    @Override
    @Transactional
    public int addContact(Contact contact) {
        return contactDao.insert(contact);
    }

    @Override
    @Transactional
    public int addContacts(Integer employeeId, List<String> contactIds) {
        if(contactIds!= null && contactIds.size() > 0){
            for(String contactId : contactIds){
                Contact contact = new Contact();
                contact.setEmployeeId(employeeId);
                contact.setContactId(Integer.valueOf(contactId));
                //判断唯一
                int count = contactDao.selectOne(contact);
                if(count == 0){
                    contactDao.insert(contact);
                }
            }
            return 1;
        }
        return 0;
    }

    @Override
    public List<Integer> getContactIdByEmployeeId(Integer employeeId) {
        return contactDao.selectContactIdByEmployeeId(employeeId);
    }

    @Override
    public List<Contact> getContactByEmployeeId(Integer employeeId) {
        return contactDao.selectByEmployeeId(employeeId);
    }

    @Override
    public ContactQuery queryContact(ContactQuery query) {
        List<Contact> list = contactDao.selectByQuery(query);
        int count = contactDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    @Transactional
    public boolean delBatchContact(List<Integer> ids,Integer employeeId) {
        if(ids == null || ids.size() <= 0){
            return false;
        }else{
            Contact contact = new Contact();
            contact.setIds(ids);
            contact.setEmployeeId(employeeId);
            return contactDao.batchDelete(contact) > 0;
        }
    }

}
