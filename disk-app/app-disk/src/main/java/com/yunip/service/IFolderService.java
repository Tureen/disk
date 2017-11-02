/*
 * 描述：文件夹业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-10
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.model.disk.query.FolderQuery;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameNameHelper;
import com.yunip.model.fileserver.FileEntity;

public interface IFolderService {

    /**
     * 公共获取文件夹下的子目录
     * @param folderQuery   文件夹查询对象        
     * @return  
     * ParentFolder 
     * @exception
     */
    Folder getSubFolder(FolderQuery folderQuery);

    /**
     * 个人获取文件夹下的子目录
     * @param folderQuery   文件夹查询对象       
     * @param employee      员工
     * @return  
     * ParentFolder 
     * @exception
     */
    Folder getQuerySubFolder(FolderQuery folderQuery, Employee employee);

    /***
     * 获取当前文件夹上级的列表(逆推网上查)
     * @param folderId
     * @return  
     * List<Folder> 
     * @exception
     */
    List<Folder> getParentFolders(int folderId);

    /*****
     * createPersonalFolder(创建私人文件夹) 
     * @param folder  文件对象
     * void 
     * @exception
     */
    @Transactional
    void createPersonalFolder(Folder folder, Employee employee);

    /***
     * 复制文件
     * @param file      需要复制的文件
     * @param folderId  选择的文件夹Id
     * @param employee  操作的员工对象
     * void 
     * @exception
     */
    @Transactional
    File copyFile(File file, int folderId, Employee employee);

    /***
     * 复制文件夹
     * @param folder    需要复制的文件夹
     * @param folderId  选择的文件夹ID
     * @param employee  选择的文件夹ID
     * void 
     * @exception
     */
    @Transactional
    void copyFolder(Folder folder, int folderId, Employee employee, List<Long> fileSizeList);

    /***
     * 复制文件或者文件夹
     * @param folder    需要复制的文件夹或者文件(封装到folder下的folders和files)
     * @param folderId  选择的文件夹ID
     * @param employee  选择的文件夹ID
     * @param type      行为是复制or移动 （复制：1 移动：0）
     * void 
     * @exception
     */
    void copyFolderOrFile(Folder folder, int folderId, Employee employee);

    /***
     * 移动文件或者文件夹
     * @param folder    需要移动的文件夹或者文件(封装到folder下的folders和files)
     * @param folderId  选择的文件夹ID
     * @param employee  选择的文件夹ID
     * void 
     * @exception
     */
    void moveFolderOrFile(Folder folder, int folderId, Employee employee);

    /***
     * 移动文件
     * @param file      需要移动的文件
     * @param folderId  选择的文件夹ID
     * @param employee  操作的员工对象
     * void 
     * @exception
     */
    @Transactional
    void moveFile(File file, int folderId, Employee employee);

    /***
     * 移动文件夹
     * @param folder    需要移动的文件夹
     * @param folderId  选择的文件夹ID
     * @param employee  操作的员工对象
     * void 
     * @exception
     */
    @Transactional
    void moveFolder(Folder folder, int folderId, Employee employee);

    /**
     * @param fileId  删除文件Id
     * @param isDel   是否删除存储文件
     * @param employee 员工对象
     * void 
     * @exception
     */
    void delFile(List<FileEntity> fileEntities,List<FileEntity> fileVersionEntities, List<File> fileIndexEntities,
            int fileId, boolean isDel, Employee employee, Employee employeeLog);

    /**
     * 删除的文件夹
     * @param folderId 需要删除的文件夹ID
     * @param isDel    是否删除存储文件
     * @param employee 员工对象
     * void 
     * @exception
     */
    void delFolder(List<FileEntity> fileEntities, List<FileEntity> fileVersionEntities, List<File> fileIndexEntities,
            int folderId, boolean isDel, Employee employee, Employee employeeLog);

    /**
     * 删除的文件夹
     * @param folder   需要删除的文件夹对象
     * @param isDel    是否删除存储文件
     * @param employee 所属员工对象
     * @param employeeLog 当前员工对象
     * void 
     * @exception
     */
    @Transactional
    void delFolderOrFile(Folder folder, boolean isDel, Employee employee,
            Employee employeeLog);

    /***
     * 查询复制会存在同名的文件或者文件夹
     * @param folder
     * @param folderId
     * @param employee
     * @return  
     * Folder 
     * @exception
     */
    SameNameHelper getSameName(Folder folder, int folderId, Employee employee);

    /***
     * 检测是否存在同名文件
     * @param renameHelper
     * @return  
     * boolean 
     * @exception
     */
    boolean checkSameName(RenameHelper renameHelper);

    /***
     * 查询出文件夹下的文件夹列表(两级)
     * @param folderId  文件夹ID
     * @param employee  员工对象
     * @return  
     * List<Folder> 
     * @exception
     */
    List<Folder> getSubTwoFolders(int folderId, Employee employee);

    /***
     * 重命名
     * @param renameHelper  
     * @param employee
     * void 
     * @exception
     */
    @Transactional
    void rename(RenameHelper renameHelper, Employee employee);

    /**
     * 根据查询条件查询文件夹信息
     * @param folderQuery   文件夹查询对象       
     * @return  
     */
    List<Folder> getFolderInfoListByQuery(FolderQuery folderQuery);
}
