/*
 * 描述：文件夹信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.FolderQuery;

@Repository("iFolderDao")
public interface IFolderDao extends IBaseDao<Folder> {

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Folder> selectByQuery(FolderQuery query);

    /**
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FolderQuery query);

    /***
     * 根据父文件夹ID查询子文件夹集合
     * @param query
     * @return  
     * List<String> 
     * @exception
     */
    List<String> selectByParentId(FolderQuery query);
    
    /**
     * 根据code查询上级所有文件夹权限中的最大权限(数值最小)
     * selectAuthByCode(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param query
     * @return  
     * Integer
     * @exception
     */
    Integer selectAuthByCode(FolderQuery query);

    /***
     * 删除
     * @param query  
     * void 
     * @exception
     */
    void deleteById(FolderQuery query);

    /**
     * 删除整个文件夹
     * @param ids
     * @return  
     * int 
     * @exception
     */
    Integer delByFolderCode(String folderCode);

    /**
     * 删除整个文件夹
     * @param ids
     * @return  
     * int 
     * @exception
     */
    Integer delByCode(Folder folder);
    
    /**
     * 精确查找文件夹根据文件夹编码
     * @param folder
     * @return  
     * Folder 
     * @exception
     */
    List<Folder> selectByFolderCode(Folder folder);

    /**
     * 恢复文件or文件夹时，精确查询层级目录名
     * @param folder
     * @return  
     * Folder 
     * @exception
     */
    Folder selectRestoreByFolderName(Folder folder);
}
