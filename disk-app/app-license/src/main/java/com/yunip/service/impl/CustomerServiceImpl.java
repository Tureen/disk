package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.enums.au.ActionType;
import com.yunip.mapper.au.ICustomerDao;
import com.yunip.mapper.au.ILicenseCodeDao;
import com.yunip.mapper.au.ILicenseLogDao;
import com.yunip.model.au.Customer;
import com.yunip.model.au.LicenseCode;
import com.yunip.model.au.LicenseLog;
import com.yunip.model.au.query.CustomerQuery;
import com.yunip.model.au.query.LicenseCodeQuery;
import com.yunip.service.ICustomerService;
import com.yunip.utils.pwd.Des;
import com.yunip.web.common.License;

@Service("iCustomerService")
public class CustomerServiceImpl implements ICustomerService {

    @Resource(name = "iCustomerDao")
    private ICustomerDao    customerDao;

    @Resource(name = "iLicenseCodeDao")
    private ILicenseCodeDao licenseCodeDao;
    
    @Resource(name = "iLicenseLogDao")
    private ILicenseLogDao licenseLogDao;
    
    //生成时间授权码
    private String getLicenseCode(String clientCode,Integer licenseHour){
        //新的加密密钥
        String signCode = clientCode + License.LICENSECODEKEY;
        //时效序列号
        String timeCode = License.getLicenseCode(licenseHour);
        Des des = new Des();
        //用客户端序列号对失效序列号进行加密
        String licCode = des.strEnc(timeCode, signCode, null, null);
        return licCode;
    }
    
    //生成注册员工数秘钥
    private String getRegisterKey(String clientCode,Integer registerNum){
        Des des = new Des();
        //新的加密密钥
        String signCode = clientCode + License.LICENSECODEKEY;
        //注册员工数秘钥
        String registerKey = des.strEnc(""+registerNum, signCode, null, null);
        return registerKey;
    }

    @Override
    @Transactional
    public int addCustomer(Customer customer, LicenseCode licenseCode) {
        customerDao.insert(customer);
        licenseCode.setCustomerId(customer.getId());
        //时间由天变为小时
        licenseCode.setLicenseHour(licenseCode.getLicenseHour() * 24);
        //授权码：用客户端序列号对失效序列号进行加密
        String licCode = getLicenseCode(licenseCode.getClientCode(), licenseCode.getLicenseHour());
        //注册员工数秘钥
        String registerKey = getRegisterKey(licenseCode.getClientCode(), licenseCode.getRegisterNum());
        //注入
        licenseCode.setLicenseCode(licCode);
        licenseCode.setRegisterKey(registerKey);
        licenseCodeDao.insert(licenseCode);
        return 1;
    }

    @Override
    @Transactional
    public int updateCustomer(Customer customer,LicenseLog licenseLog) {
        LicenseCode licenseCode = licenseCodeDao.selectByCustomerId(customer.getId());
        String operContent = "";
        //在修改信息时，先判断是否重新开通时长或修改注册员工数
        if(customer.getRegisterKeyType() == 1){
            //注册员工数改变,授权时长开通
            if(customer.getLicenseHourType()==1){
                //日志
                licenseLog.setActionType(ActionType.TIMEANDNUM.getType());
                operContent = "允许使用员工数改变：原先为 "+ licenseCode.getRegisterNum() + " ，现变为 " + customer.getRegisterNum() 
                        + "<br>授权时间重生成：原先为 " + (licenseCode.getLicenseHour()/24) + "天 ，现为 " + (customer.getLicenseHour()) + "天";
                licenseLog.setOperContent(operContent);
                //授权码表
                licenseCode.setLicenseCode(getLicenseCode(customer.getClientCode(), customer.getLicenseHour() * 24));
                licenseCode.setLicenseHour(customer.getLicenseHour() * 24);
                licenseCode.setRegisterKey(getRegisterKey(customer.getClientCode(), customer.getRegisterNum()));
                licenseCode.setRegisterNum(customer.getRegisterNum());
            //注册员工数改变
            }else{
                //日志
                licenseLog.setActionType(ActionType.REGISTERNUM.getType());
                operContent = "允许使用员工数改变：原先为 "+ licenseCode.getRegisterNum() + " ，现变为 " + customer.getRegisterNum();
                licenseLog.setOperContent(operContent);
                //授权码表
                licenseCode.setRegisterKey(getRegisterKey(customer.getClientCode(), customer.getRegisterNum()));
                licenseCode.setRegisterNum(customer.getRegisterNum());
            }
        //授权时长开通
        }else if(customer.getLicenseHourType() == 1){
            //日志
            licenseLog.setActionType(ActionType.LICENSETIME.getType());
            operContent = "授权时间重生成：原先为 " + (licenseCode.getLicenseHour()/24) + "天 ，现为 " + (customer.getLicenseHour()) + "天";
            licenseLog.setOperContent(operContent);
            //授权码表
            licenseCode.setLicenseCode(getLicenseCode(customer.getClientCode(), customer.getLicenseHour() * 24));
            licenseCode.setLicenseHour(customer.getLicenseHour() * 24);
        //基础信息，改变
        }else{
            operContent = "客户基础信息修改";
            licenseLog.setActionType(ActionType.BASIC.getType());
        }
        licenseLogDao.insert(licenseLog);
        licenseCodeDao.update(licenseCode);
        return customerDao.update(customer);
    }

    @Override
    public CustomerQuery queryCustomer(CustomerQuery query) {
        List<Customer> list = customerDao.selectByQuery(query);
        int count = customerDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    public Customer getCustomer(Integer id) {
        Customer customer = customerDao.selectById(id);
        LicenseCodeQuery codeQuery = new LicenseCodeQuery();
        codeQuery.setCustomerId(customer.getId());
        List<LicenseCode> list = licenseCodeDao.selectByQuery(codeQuery);
        LicenseCode licenseCode = list == null ? null : (list.size() > 0 ? list.get(0) : null);
        if(licenseCode == null){
            return null;
        }
        customer.setClientCode(licenseCode.getClientCode());
        customer.setLicenseCode(licenseCode.getLicenseCode());
        customer.setLicenseHour(licenseCode.getLicenseHour());
        customer.setRegisterKey(licenseCode.getRegisterKey());
        customer.setRegisterNum(licenseCode.getRegisterNum());
        return customer;
    }

}
