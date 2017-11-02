/*
 * 描述：联系人Dao
 * 创建人：ming.zhu
 * 创建时间：2017-01-08
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.Contact;
import com.yunip.model.disk.query.ContactQuery;

@Repository("iContactDao")
public interface IContactDao extends IBaseDao<Contact>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Contact> selectByQuery(ContactQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(ContactQuery query);
    
    /**
     * 该员工所有关联联系人id
     * @param employeeId
     * @return  
     * List<Integer> 
     * @exception
     */
    List<Integer> selectContactIdByEmployeeId(Integer employeeId);
    
    List<Contact> selectByEmployeeId(Integer employeeId);
    
    int batchDelete(Contact contact);
    
    int selectOne(Contact contact);
}
