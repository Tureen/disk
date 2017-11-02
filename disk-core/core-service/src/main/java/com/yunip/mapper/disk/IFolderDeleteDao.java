package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.FolderDelete;
import com.yunip.model.disk.query.FolderDeleteQuery;

@Repository("iFolderDeleteDao")
public interface IFolderDeleteDao extends IBaseDao<FolderDelete>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<FolderDelete> selectByQuery(FolderDeleteQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FolderDeleteQuery query);
    
    /**
     * 根据最上级直属文件夹，修改子文件夹状态
     * @param fileDelete  
     * void 
     * @exception
     */
    void updateByDeleteType(FolderDelete folderDelete);
    
    /**
     * 根据条件查询列表(不分页，查全部)
     * @param query 查询对象
     * @return
     */
    List<FolderDelete> selectAllByQuery(FolderDeleteQuery query);
}
