/*
 * 描述：〈同名操作辅助对象〉
 * 创建人：can.du
 * 创建时间：2016-5-20
 */
package com.yunip.model.disk.support;

import java.util.ArrayList;
import java.util.List;

import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;

/**
 * 同名操作辅助对象
 */
public class SameNameHelper {

    /**已经存在的集合和nowFolders一一对应**/
    public List<Folder> oldFolders = new ArrayList<Folder>();

    /**当前操作的同名文件夹集合**/
    public List<Folder> nowFolders = new ArrayList<Folder>();

    /**已经存在的集合和nowFiles一一对应**/
    private List<File>  oldFiles = new ArrayList<File>();

    /**当前操作的同名文件集合**/
    private List<File>  nowFiles = new ArrayList<File>();

    public List<Folder> getOldFolders() {
        return oldFolders;
    }

    public void setOldFolders(List<Folder> oldFolders) {
        this.oldFolders = oldFolders;
    }

    public List<Folder> getNowFolders() {
        return nowFolders;
    }

    public void setNowFolders(List<Folder> nowFolders) {
        this.nowFolders = nowFolders;
    }

    public List<File> getOldFiles() {
        return oldFiles;
    }

    public void setOldFiles(List<File> oldFiles) {
        this.oldFiles = oldFiles;
    }

    public List<File> getNowFiles() {
        return nowFiles;
    }

    public void setNowFiles(List<File> nowFiles) {
        this.nowFiles = nowFiles;
    }
}
