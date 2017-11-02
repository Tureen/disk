package com.yunip.model.disk.support;

import java.util.List;

import com.yunip.model.disk.AuthorityShare;

public class AuthReq {

    private List<AuthorityShare> shares;

    public List<AuthorityShare> getShares() {
        return shares;
    }

    public void setShares(List<AuthorityShare> shares) {
        this.shares = shares;
    }

}
