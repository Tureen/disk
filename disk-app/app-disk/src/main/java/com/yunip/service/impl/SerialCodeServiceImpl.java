package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.mapper.ISerialCodeDao;
import com.yunip.model.SerialCode;
import com.yunip.model.query.SerialCodeQuery;
import com.yunip.service.ISerialCodeService;


/**
 * @author can.du
 * 功能：序列号实现类
 */
@Service("iSerialCodeService")
public class SerialCodeServiceImpl implements ISerialCodeService {

    @Resource(name = "iSerialCodeDao")
    private ISerialCodeDao serialCodeDao;

    public int updateVersion(int type) {
        SerialCodeQuery query = new SerialCodeQuery();
        query.setType(type);
        List<SerialCode> list = serialCodeDao.selectByQuery(query);
        if (list.size()>0 && list.get(0) != null) {
            SerialCode serialCode = list.get(0);
            int level = Integer.parseInt(serialCode.getVersion()) + 1;
            serialCode.setVersion(String.valueOf(level));
            return serialCodeDao.update(serialCode);
        }
        return 0;
    }
}
