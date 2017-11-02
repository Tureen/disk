package com.yunip.manage;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.mapper.disk.ITakeCodeDao;
import com.yunip.model.disk.query.TakeCodeQuery;

@Component("takeCodeManager")
public class TakeCodeManager {

    @Resource(name = "iTakeCodeDao")
    private ITakeCodeDao takeCodeDao;
    
    /**
     * 验证提取码是否过期
     * @param takeCode 提取码
     * @return 返回提取码是否可用  
     */
    public boolean validityTakeCode(List<String> fileIds,String takeCode){
        if(fileIds != null && fileIds.size() > 0){
            for(String fileId : fileIds){
                TakeCodeQuery query = new TakeCodeQuery();
                query.setTakeCode(takeCode);
                query.setFileId(Integer.parseInt(fileId));
                query.setOpenTime(new Date());
                int i = takeCodeDao.selectCountByQuery(query);
                if(i == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
