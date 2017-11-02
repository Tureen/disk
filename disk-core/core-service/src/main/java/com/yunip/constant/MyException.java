package com.yunip.constant;

import com.yunip.enums.admin.AdminException;
import com.yunip.enums.api.ApiException;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.fileservers.FileServersException;



/***
 * @author can.du
 */
public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** errorCode */
    private Integer errorCode = null;

    /** rootCause */
    private Throwable rootCause = null;
    
    /** msg */
    private String msg = null;
    
    /**手机号码*/
    private String mobile = null;
    
    /**员工编号*/
    private int employeeId = 0;

    /**
     * Constructor
     */
    public MyException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param i
     * @throws SIRMException
     */
    public MyException(Integer code,String msg) throws RuntimeException {
    	errorCode = code;
    	this.msg = msg;
    }
    
    /**
     * 无需记录出错用户
     * @param i
     * @throws SIRMException
     */
    public MyException(AdminException exception) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
    }
    
    /**
     * 需要记录出错用户
     * @param i
     * @throws SIRMException
     */
    public MyException(AdminException exception,int adminId) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg() + "adminId--"+adminId;
    }
    
    /**
     * 记录未登录手机号码操作
     * @param i
     * @throws SIRMException
     */
    public MyException(AdminException exception,String mobile) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg() + ":" + mobile;
    }
    
    /**
     * 需要记录出错用户
     * @param i
     * @throws SIRMException
     */
    public MyException(DiskException exception,int employeeId) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg() ;
        this.employeeId = employeeId;
    }
    
    /**
     * 不需要记录
     * @param i
     * @throws SIRMException
     */
    public MyException(DiskException exception) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
    }
    
    /**
     * 记录未登录手机号码操作
     * @param i
     * @throws SIRMException
     */
    public MyException(DiskException exception,String mobile) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
        this.mobile = mobile;
    }
    
    /**
     * 记录文件服务器操作
     * @param i
     * @throws SIRMException
     */
    public MyException(FileServersException exception) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
    }
    
    /**
     * API无需记录出错用户
     * @param i
     * @throws SIRMException
     */
    public MyException(ApiException exception) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
    }
    
    /**
     * api需要记录出错用户
     * @param i
     * @throws SIRMException
     */
    public MyException(ApiException exception,int adminId) throws RuntimeException {
        errorCode = exception.getCode();
        this.msg = exception.getMsg();
    }
    
    
    /**
     * Constructor
     * 
     * @param i
     * @throws SIRMException
     */
    public MyException(Integer code) throws RuntimeException {
    	errorCode = code;
    }

    /**
     * Constructor
     * 
     * @param errorCode
     * @param rootCause
     * @throws SIRMException
     */
    public MyException(String errorCode, Throwable rootCause) throws RuntimeException {
        super(errorCode, rootCause);
    }

    /**
     * get errorCode 
     * 
     * @return errorCode 
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * get rootCause
     * 
     * @return rootCause 
     */
    public Throwable getRootCause() {
        return rootCause;
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
