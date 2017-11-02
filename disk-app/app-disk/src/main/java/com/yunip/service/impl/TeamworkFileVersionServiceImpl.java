package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.mapper.teamwork.ITeamworkFileVersionDao;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFileVersion;
import com.yunip.model.teamwork.query.TeamworkFileVersionQuery;
import com.yunip.service.ITeamworkFileVersionService;

@Service("iTeamworkFileVersionService")
public class TeamworkFileVersionServiceImpl implements ITeamworkFileVersionService {

    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao         teamworkFileDao;

    @Resource(name = "iTeamworkFileVersionDao")
    private ITeamworkFileVersionDao  teamworkFileVersionDao;

    @Override
    public List<TeamworkFileVersion> getFileVersions(
            TeamworkFileVersionQuery query) {
        query.setPageSize(Integer.MAX_VALUE);
        List<TeamworkFileVersion> fileVersions = teamworkFileVersionDao.selectByQuery(query);
        return fileVersions;
    }

    @Override
    @Transactional
    public void regainFile(TeamworkFileVersion fileVersion) {
        fileVersion = teamworkFileVersionDao.selectById(fileVersion.getId());
        TeamworkFile file = teamworkFileDao.selectById(fileVersion.getFileId());
        //如果版本中没有当前文件的，则添加当前文件到此版本
        TeamworkFileVersionQuery query = new TeamworkFileVersionQuery();
        query.setFileId(file.getId());
        query.setPageSize(1);
        query.setDesc(true);
        query.setFileVersion(file.getFileVersion());
        List<TeamworkFileVersion> list = teamworkFileVersionDao.selectByQuery(query);
        if(list == null || list.size() == 0){
            TeamworkFileVersion oldFileVersion = new TeamworkFileVersion();
            oldFileVersion.setFileId(file.getId());
            oldFileVersion.setFileName(file.getFileName());
            oldFileVersion.setFileType(file.getFileType());
            oldFileVersion.setFileSize(file.getFileSize());
            oldFileVersion.setFilePath(file.getFilePath());
            oldFileVersion.setFileVersion(file.getFileVersion());
            oldFileVersion.setValidStatus(file.getValidStatus());
            oldFileVersion.setEmployeeId(file.getEmployeeId());
            oldFileVersion.setCreateAdmin(file.getCreateAdmin());
            oldFileVersion.setCreateTime(file.getCreateTime());
            oldFileVersion.setUpdateAdmin(file.getUpdateAdmin());
            oldFileVersion.setUpdateTime(file.getUpdateTime());
            oldFileVersion.setFolderCode(file.getFolderCode());
            oldFileVersion.setEncryptStatus(file.getEncryptStatus());
            oldFileVersion.setEncryptKey(file.getEncryptKey());
            oldFileVersion.setServerCode(file.getServerCode());
            teamworkFileVersionDao.insert(oldFileVersion);
        }
        //文件恢复
        file.setFileVersion(fileVersion.getFileVersion());
        file.setFilePath(fileVersion.getFilePath());
        file.setCreateTime(fileVersion.getCreateTime());
        file.setFileSize(fileVersion.getFileSize());
        file.setFileVersion(fileVersion.getFileVersion());
        file.setEncryptStatus(fileVersion.getEncryptStatus());
        file.setEncryptKey(fileVersion.getEncryptKey());
        file.setServerCode(fileVersion.getServerCode());
        teamworkFileDao.update(file);
        //对应版本删除
        teamworkFileVersionDao.delById(fileVersion.getId());
    }

}
