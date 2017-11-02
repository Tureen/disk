/*
 * 描述：文件标签关联Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.FileSign;
import com.yunip.model.disk.query.FileSignQuery;

@Repository("iFileSignDao")
public interface IFileSignDao extends IBaseDao<FileSign>{

    /**
     * 条件删除
     * @param fileSign
     * @return  
     * int 
     * @exception
     */
    int delByFileSign(FileSign fileSign);
    
    /**
     * 删除,根据fileId
     * @param fileSign
     * @return  
     * int 
     * @exception
     */
    int delByFileId(Integer fileId);
    
    /**
     * 删除,根据文件夹code
     * @param fileSign
     * @return  
     * int 
     * @exception
     */
    int delByCode(FileSign fileSign);
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<FileSign> selectByQuery(FileSignQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FileSignQuery query);
    
    /**
     * 搜索文件名
     * @param query
     * @return  
     * List<FileSign> 
     * @exception
     */
    List<FileSign> selectKeyByFileName(FileSignQuery query);
}
