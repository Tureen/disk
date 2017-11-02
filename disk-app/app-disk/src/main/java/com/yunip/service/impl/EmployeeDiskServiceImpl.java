package com.yunip.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.disk.IEmployeeDiskDao;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.mapper.disk.ISignDao;
import com.yunip.mapper.disk.ITakeCodeDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.model.disk.EmployeeDisk;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.query.SignQuery;
import com.yunip.model.disk.query.TakeCodeQuery;
import com.yunip.service.IEmployeeDiskService;

@Service("iEmployeeDiskService")
public class EmployeeDiskServiceImpl implements IEmployeeDiskService {

    @Resource(name = "iEmployeeDiskDao")
    private IEmployeeDiskDao        employeeDiskDao;

    @Resource(name = "iFileDao")
    private IFileDao                fileDao;

    @Resource(name = "iFileVersionDao")
    private IFileVersionDao         fileVersionDao;

    @Resource(name = "iFolderDao")
    private IFolderDao              folderDao;

    @Resource(name = "iSignDao")
    private ISignDao                signDao;

    @Resource(name = "iTakeCodeDao")
    private ITakeCodeDao            takeCodeDao;

    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao        teamworkFileDao;

    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao teamworkFileVersionDao;

    @Override
    @Transactional
    public int addEmployeeDisk(EmployeeDisk employeeDisk) {
        return employeeDiskDao.insert(employeeDisk);
    }

    @Override
    @Transactional
    public int updateEmployeeDisk(EmployeeDisk employeeDisk) {
        return employeeDiskDao.update(employeeDisk);
    }

    @Override
    public EmployeeDisk getEmployeeDiskById(Integer id) {
        return employeeDiskDao.selectById(id);
    }

    @Override
    public EmployeeDisk getNewEmployeeDisk(Integer employeeId) {
        EmployeeDisk disk = new EmployeeDisk();
        //云盘已使用空间大小
        Long fileSize = fileDao.selectSumUseSpace(employeeId);
        Long fileVersionSize = fileVersionDao.selectSumUseSpace(employeeId);
        Long teamworkFileSize = teamworkFileDao.selectSumUseSpace(employeeId);
        Long teamworkFileVersionSize = teamworkFileVersionDao.selectSumUseSpace(employeeId);
        Long diskSize = (fileSize == null ? 0L : fileSize)
                + (fileVersionSize == null ? 0L : fileVersionSize)
                + (teamworkFileSize == null ? 0L : teamworkFileSize)
                + (teamworkFileVersionSize == null ? 0L : teamworkFileVersionSize);
        //目录数
        FolderQuery folderQuery = new FolderQuery();
        folderQuery.setEmployeeId(employeeId);
        folderQuery.setValidStatus(1);
        Integer folderNumber = folderDao.selectCountByQuery(folderQuery);
        //文件数
        FileQuery fileQuery = new FileQuery();
        fileQuery.setEmployeeId(employeeId);
        fileQuery.setValidStatus(1);
        Integer fileNumber = fileDao.selectCountByQuery(fileQuery);
        //我的标签数
        SignQuery signQuery = new SignQuery();
        signQuery.setEmployeeId(employeeId);
        Integer signNumber = signDao.selectCountByQuery(signQuery);
        //可提取文件数
        TakeCodeQuery takeCodeQuery = new TakeCodeQuery();
        takeCodeQuery.setEmployeeId(employeeId);
        takeCodeQuery.setOpenTime(new Date());
        Integer takeFileNumber = takeCodeDao.selectCountByQuery(takeCodeQuery);
        //属性注入
        disk.setId(employeeId);
        disk.setDiskSize(diskSize);
        disk.setFolderNumber(folderNumber);
        disk.setFileNumer(fileNumber);
        disk.setSignNumber(signNumber);
        disk.setTakeFileNumber(takeFileNumber);
        return disk;
    }

}
