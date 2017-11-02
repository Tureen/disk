/*
 * 描述：〈文件夹树辅助类〉
 * 创建人：can.du
 * 创建时间：2016-5-18
 */
package com.yunip.model.disk.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yunip.constant.SystemContant;
import com.yunip.model.disk.Folder;
import com.yunip.model.teamwork.TeamworkFolder;

/**
 * 文件夹树
 */
public class Tree implements Serializable {

    private static final long serialVersionUID = 1L;

    /**节点ID**/
    private int               id;

    /**节点名称**/
    private String            name;

    /**是否为父节点**/
    private boolean           isParent;

    /**图标**/
    private String            icon             = SystemContant.FOLDER_ICON;
    
    /**编码*/
    private String            code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static List<Tree> getTrees(List<Folder> folders, String path) {
        List<Tree> trees = new ArrayList<Tree>();
        if (folders != null && folders.size() > 0) {
            for (Folder folder : folders) {
                Tree tree = new Tree();
                tree.setId(folder.getId());
                if (folder.getFolders() != null
                        && folder.getFolders().size() > 0) {
                    tree.setIsParent(true);
                } else {
                    tree.setIsParent(false);
                }
                tree.setName(folder.getFolderName());
                tree.setCode(folder.getFolderCode());
                tree.setIcon(path + tree.getIcon());
                trees.add(tree);
            }
        }
        return trees;
    }
    
    public static List<Tree> getTeamworkTrees(List<TeamworkFolder> folders, String path) {
        List<Tree> trees = new ArrayList<Tree>();
        if (folders != null && folders.size() > 0) {
            for (TeamworkFolder folder : folders) {
                Tree tree = new Tree();
                tree.setId(folder.getId());
                if (folder.getTeamworkFolders() != null && folder.getTeamworkFolders().size() > 0) {
                    tree.setIsParent(true);
                } else {
                    tree.setIsParent(false);
                }
                tree.setName(folder.getFolderName());
                tree.setCode(folder.getFolderCode());
                tree.setIcon(path + tree.getIcon());
                trees.add(tree);
            }
        }
        return trees;
    }
}
