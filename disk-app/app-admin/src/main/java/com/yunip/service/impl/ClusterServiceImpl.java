package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.mapper.config.IClusterConfigDao;
import com.yunip.model.config.ClusterConfig;
import com.yunip.model.config.query.ClusterConfigQuery;
import com.yunip.service.IClusterService;

@Service("iClusterService")
public class ClusterServiceImpl implements IClusterService{
    
    @Resource(name = "iClusterConfigDao")
    private IClusterConfigDao clusterConfigDao;

    @Override
    @Transactional
    public int addCluster(ClusterConfig clusterConfig) {
        return clusterConfigDao.insert(clusterConfig);
    }

    @Override
    @Transactional
    public int delCluster(int clusterId) {
        return clusterConfigDao.delById(clusterId);
    }

    @Override
    @Transactional
    public int updateCluster(ClusterConfig clusterConfig) {
        return clusterConfigDao.update(clusterConfig);
    }

    @Override
    public ClusterConfigQuery queryCluster(ClusterConfigQuery query) {
        List<ClusterConfig> list = clusterConfigDao.selectByQuery(query);
        int count = clusterConfigDao.selectCountByQuery(query);
        query.setList(list);
        query.setRecordCount(count);
        return query;
    }

    @Override
    public ClusterConfig getClusterById(Integer clusterId) {
        return clusterConfigDao.selectById(clusterId);
    }

}
