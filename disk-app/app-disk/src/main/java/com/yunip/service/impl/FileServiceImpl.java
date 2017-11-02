package com.yunip.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.disk.IFileDao;
import com.yunip.model.disk.File;
import com.yunip.service.IFileService;

@Service("iFileService")
public class FileServiceImpl implements IFileService{

    @Resource(name = "iFileDao")
    private IFileDao   fileDao;
    
    @Override
    public File getFileById(Integer id) {
        return fileDao.selectById(id);
    }

}
