/*
 * 描述：查询下级目录权限封装类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.support;

import java.util.Map;

public class AuthMap {

    /**文件id对应权限type**/
    private Map<Integer, Integer> fileMap;

    /**文件夹id对应权限type**/
    private Map<Integer, Integer> folderMap;

    public Map<Integer, Integer> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<Integer, Integer> fileMap) {
        this.fileMap = fileMap;
    }

    public Map<Integer, Integer> getFolderMap() {
        return folderMap;
    }

    public void setFolderMap(Map<Integer, Integer> folderMap) {
        this.folderMap = folderMap;
    }

}
