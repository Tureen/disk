/*
 * 描述：〈协作文件版本的查询类〉
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.model.teamwork.query;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.page.PageQuery;

/**
 * 协作文件版本的查询类
 */
@Alias("TTeamworkFileVersionQuery")
public class TeamworkFileVersionQuery extends PageQuery<TeamworkFileVersionQuery> {

    private static final long serialVersionUID = 1L;

    /**文件ID**/
    private int               fileId;

    /**文件版本**/
    private int               fileVersion;

    /**协作ID**/
    private Integer           teamworkId;

    /** 文件存储地址 **/
    private String            filePath;

    private boolean           desc;

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(int fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

}
