package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileSignDao;
import com.yunip.mapper.disk.ISignDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileSign;
import com.yunip.model.disk.Sign;
import com.yunip.model.disk.query.FileSignQuery;
import com.yunip.model.disk.query.SignQuery;
import com.yunip.model.disk.support.SignReq;
import com.yunip.service.ISignService;

@Service("iSignService")
public class SignServiceImpl implements ISignService {

    @Resource(name = "iSignDao")
    private ISignDao     signDao;

    @Resource(name = "iFileSignDao")
    private IFileSignDao fileSignDao;
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;

    @Override
    public int addSign(Sign sign) {
        return signDao.insert(sign);
    }

    @Override
    public int delSign(Integer id) {
        //删除关联标签与文件关系
        FileSign fileSign = new FileSign();
        fileSign.setSignId(id);
        fileSignDao.delByFileSign(fileSign);
        //删除标签
        return signDao.delById(id);
    }

    @Override
    public int delSign(List<Integer> ids) {
        for (Integer id : ids) {
            //删除关联标签与文件关系
            FileSign fileSign = new FileSign();
            fileSign.setSignId(id);
            fileSignDao.delByFileSign(fileSign);
            //删除标签
            signDao.delById(id);
        }
        return 1;
    }

    @Override
    public int addFileSign(SignReq signreq) {
        //删除文件所关联的标签
        FileSign tmpFileSign = new FileSign();
        tmpFileSign.setFileId(signreq.getFileId());
        fileSignDao.delByFileSign(tmpFileSign);
        //创建真实标签集合
        List<Sign> newSigns = new ArrayList<Sign>();
        //将标签添加入标签库,并赋予真实id
        List<Sign> signs = signreq.getSigns();
        if (signs.size() > 0) {
            //文件查询
            File file = fileDao.selectById(signreq.getFileId());
            for (Sign sign : signs) {
                if (sign.getId() == 0) {
                    sign.setEmployeeId(signreq.getEmployeeId());
                    sign.setCreateAdmin(signreq.getCreateAdmin());
                    sign.setUpdateAdmin(signreq.getUpdateAdmin());
                    signDao.insert(sign);
                }
                newSigns.add(sign);
            }
            if(file!=null){
                //循环添加
                for (Sign sign : newSigns) {
                    FileSign fileSign = new FileSign();
                    fileSign.setFolderCode(file.getFolderCode());
                    fileSign.setEmployeeId(file.getEmployeeId());
                    fileSign.setFileId(signreq.getFileId());
                    fileSign.setSignId(sign.getId());
                    fileSignDao.insert(fileSign);
                }
            }
        }
        return 1;
    }

    @Override
    public SignQuery querySign(SignQuery query) {
        List<Sign> list = signDao.selectByQuery(query);
        int count = signDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    public List<Sign> getSignByEmployeeId(Integer employeeId) {
        return signDao.selectByEmployeeId(employeeId);
    }

    @Override
    public List<Sign> getSignByFileId(Integer fileId) {
        return signDao.selectByFileId(fileId);
    }

    @Override
    public FileSignQuery queryFileSign(FileSignQuery query) {
        List<FileSign> list = fileSignDao.selectKeyByFileName(query);
        query.setList(list);
        query.setRecordCount(list.size());
        return query;
    }

    @Override
    public void delFileSign(List<FileSign> fileSigns) {
        for(FileSign fileSign : fileSigns){
            if (fileSign.getFileId() != null && fileSign.getSignId() != null) {
                fileSignDao.delByFileSign(fileSign);
            }
        }
    }

    @Override
    @Transactional
    public int updateSign(Sign sign) {
        return signDao.update(sign);
    }

}
