/*
 * 描述：〈同名协作操作辅助对象〉
 * 创建人：ming.zhu
 * 创建时间：2017-03-07
 */
package com.yunip.model.disk.support;

import java.util.ArrayList;
import java.util.List;

import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.model.teamwork.TeamworkFolder;

/**
 * 同名操作辅助对象
 */
public class SameTeamworkNameHelper {

    /**已经存在的集合和nowFolders一一对应**/
    public List<TeamworkFolder> oldFolders = new ArrayList<TeamworkFolder>();

    /**当前操作的同名文件夹集合**/
    public List<TeamworkFolder> nowFolders = new ArrayList<TeamworkFolder>();

    /**已经存在的集合和nowFiles一一对应**/
    private List<TeamworkFile>  oldFiles   = new ArrayList<TeamworkFile>();

    /**当前操作的同名文件集合**/
    private List<TeamworkFile>  nowFiles   = new ArrayList<TeamworkFile>();

    public List<TeamworkFolder> getOldFolders() {
        return oldFolders;
    }

    public void setOldFolders(List<TeamworkFolder> oldFolders) {
        this.oldFolders = oldFolders;
    }

    public List<TeamworkFolder> getNowFolders() {
        return nowFolders;
    }

    public void setNowFolders(List<TeamworkFolder> nowFolders) {
        this.nowFolders = nowFolders;
    }

    public List<TeamworkFile> getOldFiles() {
        return oldFiles;
    }

    public void setOldFiles(List<TeamworkFile> oldFiles) {
        this.oldFiles = oldFiles;
    }

    public List<TeamworkFile> getNowFiles() {
        return nowFiles;
    }

    public void setNowFiles(List<TeamworkFile> nowFiles) {
        this.nowFiles = nowFiles;
    }

}
