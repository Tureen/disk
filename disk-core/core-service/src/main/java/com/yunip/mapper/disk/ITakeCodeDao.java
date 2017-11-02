/*
 * 描述：文件提取码信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.TakeCode;
import com.yunip.model.disk.query.TakeCodeQuery;

@Repository("iTakeCodeDao")
public interface ITakeCodeDao extends IBaseDao<TakeCode>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<TakeCode> selectByQuery(TakeCodeQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(TakeCodeQuery query);
    
    /**
     * 删除多条，根据文件id
     * @param fileId
     * @return  
     * int 
     * @exception
     */
    int delByFileId(Integer fileId);
    
    /**
     * 删除多条，根据提取码
     * @param fileId
     * @return  
     * string 
     * @exception
     */
    int delByTakeCode(String takeCode);
    
    /**
     * 删除,根据文件夹code
     * @param takeCode
     * @return  
     * int 
     * @exception
     */
    int delByCode(TakeCode takeCode);
    
    /**
     * 修改剩余下载数量
     * @param id  
     * void 
     * @exception
     */
    void updateRemainDownloadNum(Integer id);
}
