/*
 * 描述：〈队列任务对象〉
 * 创建人：can.du
 * 创建时间：2016-8-1
 */
package com.yunip.model.disk.support;

import com.yunip.model.company.Employee;

/**
 * 队列任务对象
 */
public class QueueTask {
    
    /**解压**/
    public final static int DES_TYPE = 1;
    
    /**下载**/
    public final static int DOWN_TYPE = 2;
    
    /***
     * 类型
     */
    private Integer type;

    /***
     * 任务名称(employeeId + YYYYMMDDHHmmss)
     */
    private String  taskName;

    /***
     * 员工对象
     */
    private Employee employee;

    /***
     * 请求数据对象
     */
    private Object  object;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
