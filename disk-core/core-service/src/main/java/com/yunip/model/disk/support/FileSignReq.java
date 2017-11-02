package com.yunip.model.disk.support;

import java.util.List;

import com.yunip.model.disk.FileSign;

public class FileSignReq {

    private List<FileSign> fileSigns;

    public List<FileSign> getFileSigns() {
        return fileSigns;
    }

    public void setFileSigns(List<FileSign> fileSigns) {
        this.fileSigns = fileSigns;
    }

}
