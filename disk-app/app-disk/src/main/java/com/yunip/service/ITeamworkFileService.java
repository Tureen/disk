/*
 * 描述：协作文件or文件夹业务层
 * 创建人：ming.zhu
 * 创建时间：2017-03-02
 */
package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.disk.support.RenameHelper;
import com.yunip.model.disk.support.SameTeamworkNameHelper;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;
import com.yunip.model.teamwork.query.TeamworkFolderQuery;

public interface ITeamworkFileService {

    /**
     * 获取协作文件夹下的子目录或最上级目录及文件
     * @param teamworkFolderQuery   协作文件夹查询对象       
     * @param employee      员工
     * @return  
     * ParentFolder 
     * @exception
     */
    TeamworkFolder getQuerySubFolder(TeamworkFolderQuery teamworkFolderQuery);

    /**
     * 获取当前文件夹上级列表(递归查询)
     * @param folderId
     * @return  
     * List<TeamworkFolder> 
     * @exception
     */
    List<TeamworkFolder> getParentFolders(int folderId);

    /**
     * createTeamworkFolder(创建协作文件夹) 
     * @param teamworkFolder  文件对象
     * @return  协作信息id
     * void 
     * @exception
     */
    @Transactional
    int createTeamworkFolder(TeamworkFolder teamworkFolder, Employee employee);

    /***
     * 检测是否存在同名文件
     * @param renameHelper
     * @return  
     * boolean 
     * @exception
     */
    boolean checkSameName(RenameHelper renameHelper);

    /**
     * 重命名
     * @param renameHelper
     * @param employee
     * @return  协作信息id
     * int 
     * @exception
     */
    @Transactional
    int rename(RenameHelper renameHelper, Employee employee);

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
    int delFolderOrFile(TeamworkFolder teamworkFolder, Employee employee,
            Employee employeeLog);

    /***
     * 查询出文件夹下的文件夹列表(两级)
     * @param folderId    文件夹ID
     * @param employee    员工对象
     * @param teamworkId  工作组ID
     * @return  
     * List<Folder> 
     * @exception
     */
    List<TeamworkFolder> getSubTwoFolders(int folderId, Employee employee,
            String teamworkId);
    
    /***
     * 查询复制会存在同名的文件或者文件夹
     * @param folder
     * @param folderId
     * @param teamworkId
     * @return  
     * Folder 
     * @exception
     */
    SameTeamworkNameHelper getSameName(TeamworkFolder folder, int folderId);
    
    /**
     * 查询导出时会存在同名的文件或者文件夹
     * @param folder
     * @param folderId
     * @param employee
     * @return  
     * SameTeamworkNameHelper 
     * @exception
     */
    SameTeamworkNameHelper getExportSameName(TeamworkFolder folder, int folderId, Employee employee);
    
    /***
     * 移动文件或者文件夹
     * @param folder    需要移动的文件夹或者文件(封装到folder下的folders和files)
     * @param folderId  选择的文件夹ID
     * @param employee  员工ID
     * @param teamworkId  协作ID
     * void 
     * @exception
     */
    void moveFolderOrFile(TeamworkFolder folder, int folderId, Employee employee, Integer teamworkId);
    
    /***
     * 移动文件
     * @param file        需要移动的文件
     * @param folderId    选择的文件夹ID
     * @param employee    操作的员工对象
     * @param teamworkId  协作ID
     * void 
     * @exception
     */
    @Transactional
    void moveFile(TeamworkFile file, int folderId, Employee employee, Integer teamworkId);
    
    /***
     * 移动文件夹
     * @param folder      需要移动的文件夹
     * @param folderId    选择的文件夹ID
     * @param employee    操作的员工对象
     * @param teamworkId  协作ID
     * void 
     * @exception
     */
    @Transactional
    void moveFolder(TeamworkFolder folder, int folderId, Employee employee, Integer teamworkId);
    
    /***
     * 导出文件或者文件夹
     * @param folder    需要移动的文件夹或者文件(封装到folder下的folders和files)
     * @param folderId  选择的文件夹ID
     * @param employee  员工ID
     * void 
     * @exception
     */
    int exportFolderOrFile(TeamworkFolder folder, int folderId, Employee employee);
    
    /***
     * 导出文件
     * @param file        需要移动的文件
     * @param folderId    选择的文件夹ID
     * @param employee    操作的员工对象
     * void 
     * @exception
     */
    @Transactional
    void exportFile(TeamworkFile file, int folderId, Employee employee);
    
    /***
     * 导出文件夹
     * @param folder      需要移动的文件夹
     * @param folderId    选择的文件夹ID
     * @param employee    操作的员工对象
     * void 
     * @exception
     */
    @Transactional
    void exportFolder(TeamworkFolder folder, int folderId, Employee employee, Integer teamworkId, List<Long> fileSizeList);
}
