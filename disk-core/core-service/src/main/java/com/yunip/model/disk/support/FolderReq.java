package com.yunip.model.disk.support;

import java.util.List;

import com.yunip.model.disk.AuthorityShare;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;

public class FolderReq {

    private List<File>           files;

    private List<Folder>         folders;

    private List<AuthorityShare> authorityShares;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<AuthorityShare> getAuthorityShares() {
        return authorityShares;
    }

    public void setAuthorityShares(List<AuthorityShare> authorityShares) {
        this.authorityShares = authorityShares;
    }

}
