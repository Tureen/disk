/*
 * 描述：文件版本信息Dao
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.mapper.disk;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileVersion;
import com.yunip.model.disk.query.FileVersionQuery;

@Repository("iFileVersionDao")
public interface IFileVersionDao extends IBaseDao<FileVersion>{
    
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<FileVersion> selectByQuery(FileVersionQuery query);

    /**
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(FileVersionQuery query);
    
    /**
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    FileVersion selectNewByFileId(Integer fileId);
    
    /**
     * 删除，根据文件id
     * @param fileId
     * @return  
     * int 
     * @exception
     */
    int delByFileId(Integer fileId);
    
    /**
     * 删除,根据文件夹code
     * @param fileVersion
     * @return  
     * int 
     * @exception
     */
    int delByCode(FileVersion fileVersion);
    
    /**
     * 获取总共所有使用的文件大小
     * @param employeeId
     * @return  
     * Long 
     * @exception
     */
    Long selectSumUseSpace(Integer employeeId);

    /**
     * 获取总大小
     * selectFileSize(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @return  
     * Long 
     * @exception
     */
    Long selectFileSize();
    
    /**
     * 根据文件夹获取所有子文件对应的文件版本
     * @param folderId
     * @return  
     * List<FileVersion> 
     * @exception
     */
    List<FileVersion> selectByFolderCode(File file);
}
