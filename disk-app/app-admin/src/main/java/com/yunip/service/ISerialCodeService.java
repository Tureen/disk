package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author ming.zhu
 * 功能：序列号编码Service
 */
public interface ISerialCodeService {

    /***
     * 更改版本序列号序列号版本
     * @param type      类型
     * @return
     */
    @Transactional
    int updateVersion(int type);
}
