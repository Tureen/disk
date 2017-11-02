/*
 * 描述：〈描述〉
 * 创建人：can.du
 * 创建时间：2016-6-27
 */
package com.yunip.constant;

import java.util.List;

import com.yunip.enums.disk.AuthorityType;
import com.yunip.enums.disk.OpenType;
import com.yunip.enums.log.ActionType;
import com.yunip.model.disk.Folder;

/**
 * 员工日志模版
 */
public class EmployeeLogTemplate {
    
    /**根目录***/
    public static String TEMPLATEPATH = "/";
    
    /**空格**/
    public static String SPACEPATH = " ";
    
    /**等**/
    public static String MORE ="..";
    
    /**更改名称的描述**/
    public static String RENAME =" 改为 ";
    
    /***
     * 获取文件夹下文件路径
     * @param folders   文件夹列表
     * @param name      文件或者文件夹名称
     * @return  
     * String 
     * @exception
     */
    public static String getFolderOrFileTemplate(List<Folder> folders,OpenType openType, String name){
        StringBuffer path = new StringBuffer(TEMPLATEPATH);
        if(folders != null && folders.size() > 0){
            for(Folder folder : folders){
                path.append(folder.getFolderName());
                path.append(TEMPLATEPATH);
            }
        }
        if(openType.equals(OpenType.FILE)){
            path.append(name);
        }
        return path.toString();
    }
    
    /***
     * 获取分享的内容
     * @param folders
     * @param name
     * @param shareObj
     * @param authorityType
     * @return  
     * String 
     * @exception
     */
    public static String getShareContent(List<Folder> folders,OpenType openType, String name ,AuthorityType authorityType){
        StringBuffer path = new StringBuffer();
        if(folders != null && folders.size() > 0){
            for(Folder folder : folders){
                path.append(TEMPLATEPATH);
                path.append(folder.getFolderName());
            }
        }
        if(openType.equals(OpenType.FILE)){
            path.append(name);
        }
        return path.toString();
    }
    
    /****
     * 更改描述
     * @param folders
     * @param oldName
     * @param nowName
     * @return  
     * String 
     * @exception
     */
    public static String getRenameContent(List<Folder> folders,OpenType openType, String oldName, String nowName){
        StringBuffer path = new StringBuffer(TEMPLATEPATH);
        if(folders != null && folders.size() > 0){
            for(Folder folder : folders){
                path.append(folder.getFolderName());
                path.append(TEMPLATEPATH);
            }
        }
        if(openType.equals(OpenType.FILE)){
            path.append(oldName);
        }
        path.append(RENAME);
        path.append(nowName);
        return path.toString();
    }
    
    /***
     * 移动或者复制的描述
     * @param folders   当前文件或文件夹的父级文件夹列表
     * @param toFolders 目标文件或文件夹的父级文件夹列表
     * @param isMore    操作是否存在多个文件
     * @param name      操作文件夹或文件名称
     * @param actionType 操作类型
     * @return  
     * String 
     * @exception
     */
    public static String getFolderOrFileTemplates(List<Folder> folders, List<Folder> toFolders, boolean isMore,
            String name , ActionType actionType){
        StringBuffer path = new StringBuffer(TEMPLATEPATH);
        if(folders != null && folders.size() > 0){
            for(Folder folder : folders){
                path.append(folder.getFolderName());
                path.append(TEMPLATEPATH);
            }
        }
        path.append(name);
        if(isMore){
            path.append(MORE);
        }
        path.append(SPACEPATH);
        path.append(actionType.getDesc());
        path.append(SPACEPATH);
        path.append(TEMPLATEPATH);
        if(toFolders != null && toFolders.size() > 0){
            for(Folder folder : toFolders){
                path.append(folder.getFolderName());
                path.append(TEMPLATEPATH);
            }
        }
        return path.toString();
    }
}
