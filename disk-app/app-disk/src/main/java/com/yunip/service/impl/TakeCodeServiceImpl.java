package com.yunip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.enums.disk.OpenType;
import com.yunip.enums.log.ActionType;
import com.yunip.manage.LogManager;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.ITakeCodeDao;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.TakeCodeQuery;
import com.yunip.service.ITakeCodeService;
import com.yunip.utils.date.DateUtil;

@Service("iTakeCodeService")
public class TakeCodeServiceImpl implements ITakeCodeService {

    @Resource(name = "iTakeCodeDao")
    private ITakeCodeDao takeCodeDao;
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @Resource(name = "logManager")
    private LogManager logManager;

    @Override
    public int addTakeCode(TakeCode takeCode,Employee employee) {
        File file = fileDao.selectById(takeCode.getFileId());
        Date date = takeCode.getEffectiveTime();
        //失效时间判断 没有就设置成无限期
        if(date == null){
            date = DateUtil.getAddDates(new Date(), 365*999);
        }
        //失效天数
        int day = DateUtil.getDayDiff(new Date(), date);
        takeCode.setEffectiveTime(date);
        takeCode.setEffectiveDate(day);
        takeCode.setEmployeeId(employee.getId());
        takeCode.setCreateAdmin(employee.getEmployeeName());
        takeCode.setUpdateAdmin(employee.getEmployeeName());
        takeCode.setFolderCode(file.getFolderCode());
        //日志
        logManager.insertEmpLoyeeLog(0, OpenType.FILE, file.getFileName(), ActionType.CTAKECODE, employee);
        return takeCodeDao.insert(takeCode);
    }

    @Override
    public int delTakeCode(Integer id) {
        return takeCodeDao.delById(id);
    }

    @Override
    public TakeCodeQuery queryTakeCode(TakeCodeQuery query) {
        List<TakeCode> list = takeCodeDao.selectByQuery(query);
        int count = takeCodeDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    public int delTakeCodeBatch(List<Integer> ids) {
        for (Integer id : ids) {
            takeCodeDao.delById(id);
        }
        return 1;
    }
    
    @Override
    public int delTakeCodeBatchByTakeCode(List<TakeCode> takeCodes, Employee employee) {
        for (TakeCode code : takeCodes) {
            takeCodeDao.delByTakeCode(code.getTakeCode());
            //日志
            logManager.insertEmpLoyeeLog(ActionType.RETAKECODE, code.getTakeCode(), employee);
        }
        return 1;
    }

    @Override
    @Transactional
    public int addTakeCodeMore(TakeCode takeCode,Employee employee,String[] fileIds) {
        Date date = takeCode.getEffectiveTime();
        //失效时间判断 没有就设置成无限期
        if(date == null){
            date = DateUtil.getAddDates(new Date(), 365*999);
        }
        //失效天数
        int day = DateUtil.getDayDiff(new Date(), date);
        for(String fileId : fileIds){
            File file = fileDao.selectById(fileId);
            TakeCode takeCodeTmp = new TakeCode();
            takeCodeTmp.setEffectiveTime(date);
            takeCodeTmp.setFileId(Integer.valueOf(fileId));
            takeCodeTmp.setEffectiveDate(day);
            takeCodeTmp.setTakeCode(takeCode.getTakeCode());
            takeCodeTmp.setEmployeeId(employee.getId());
            takeCodeTmp.setCreateAdmin(employee.getEmployeeName());
            takeCodeTmp.setUpdateAdmin(employee.getEmployeeName());
            takeCodeTmp.setFolderCode(file.getFolderCode());
            takeCodeTmp.setRemark(takeCode.getRemark());
            takeCodeTmp.setRemainDownloadNum(takeCode.getRemainDownloadNum());
            takeCodeDao.insert(takeCodeTmp);
            //日志
            logManager.insertEmpLoyeeLog(0, OpenType.FILE, file.getFileName(), ActionType.CTAKECODE, employee);
        }
        //注入
        takeCode.setEffectiveTime(date);
        takeCode.setEffectiveDate(day);
        takeCode.setEmployeeId(employee.getId());
        takeCode.setCreateAdmin(employee.getEmployeeName());
        takeCode.setUpdateAdmin(employee.getEmployeeName());
        return 0;
    }

    @Override
    public void upTakeCodeRemainDownloadNum(Integer id) {
        takeCodeDao.updateRemainDownloadNum(id);
    }

    @Override
    public TakeCode getTakeCodeById(Integer id) {
        return takeCodeDao.selectById(id);
    }

}
