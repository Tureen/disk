/*
 * 描述：文件基本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.query.FileQuery;

@Repository("iFileDao")
public interface IFileDao extends IBaseDao<File>{

    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<File> selectByQuery(FileQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FileQuery query);
    
    /**
     * 批量删除
     * @param ids
     * @return  
     * int 
     * @exception
     */
    int delBatchById(List<Integer> ids); 
    
    /***
     * 根据条件查询列表（包含标签）
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> selectBySubQuery(FileQuery query);
    
    /**
     * 删除文件，根据code
     * @param folderCode
     * @return  
     * int 
     * @exception
     */
    int delByFolderCode(String folderCode);
    
    
    /***
     * 根据条件查询列表（包含标签列表）
     * @param query
     * @return  
     * List<File> 
     * @exception
     */
    List<File> selectBySubSignQuery(FileQuery query);
    
    /**
     * 获取总共所有使用的文件大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    Long selectSumUseSpace(Integer employeeId);
    
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<File> selectAdminByQuery(FileQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectAdminCountByQuery(FileQuery query);
    
    /**
     * 删除整个文件夹
     * @param ids
     * @return  
     * int 
     * @exception
     */
    Integer delByCode(File file);
    
    /**
     * 查询总大小
     * selectFileSize(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @return  
     * Long 
     * @exception
     */
    Long selectFileSize();
    
}
