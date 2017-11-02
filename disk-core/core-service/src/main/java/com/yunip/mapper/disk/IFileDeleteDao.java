package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.disk.query.FileDeleteQuery;

@Repository("iFileDeleteDao")
public interface IFileDeleteDao extends IBaseDao<FileDelete>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<FileDelete> selectByQuery(FileDeleteQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FileDeleteQuery query);
    
    /**
     * 根据最上级直属文件夹，修改子文件状态
     * @param fileDelete  
     * void 
     * @exception
     */
    void updateByDeleteType(FileDelete fileDelete);
    
    /**
     * 根据条件查询列表(不分页，查全部)
     * @param query 查询对象
     * @return
     */
    List<FileDelete> selectAllByQuery(FileDeleteQuery query);
}
