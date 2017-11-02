package com.yunip.model.disk;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.company.Employee;

@Alias("TContact")
public class Contact implements Serializable {
    /**主键**/
    private Integer           id;

    /**员工id**/
    private Integer           employeeId;

    /**联系人id**/
    private Integer           contactId;

    /**************************************************/

    private Employee          employee;
    
    /**id集合**/
    private List<Integer>     ids;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

}
