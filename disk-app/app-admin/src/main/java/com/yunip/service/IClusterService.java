package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.config.ClusterConfig;
import com.yunip.model.config.query.ClusterConfigQuery;

public interface IClusterService {

    @Transactional
    int addCluster(ClusterConfig clusterConfig);
    
    @Transactional
    int delCluster(int clusterId);
    
    @Transactional
    int updateCluster(ClusterConfig clusterConfig);
    
    ClusterConfig getClusterById(Integer clusterId);
    
    ClusterConfigQuery queryCluster(ClusterConfigQuery query);
}
