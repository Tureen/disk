/*
 * 描述：〈文件操作服务的实现类〉
 * 创建人：can.du
 * 创建时间：2016-6-24
 */
package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileVersionDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.query.FileQuery;
import com.yunip.service.IFileService;

/**
 * 文件操作服务的实现类
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    @Resource(name = "iFileDao")
    private IFileDao        fileDao;

    @Resource(name = "iFileVersionDao")
    private IFileVersionDao fileVersionDao;

    @Override
    public FileQuery getFilesByQuery(FileQuery query) {
        List<File> files = fileDao.selectAdminByQuery(query);
        int count = fileDao.selectAdminCountByQuery(query);
        query.setRecordCount(count);
        query.setList(files);
        return query;
    }

    @Override
    public String getFileSize() {
        Long fSize = fileDao.selectFileSize();
        fSize = fSize == null ? 0L : fSize;
        Long fVersionSize = fileVersionDao.selectFileSize();
        fVersionSize = fVersionSize == null ? 0L : fVersionSize;
        return String.valueOf(fSize + fVersionSize);
    }

}
