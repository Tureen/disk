/*
 * 描述：集群服务器配置信息
 * 创建人：jian.xiong
 * 创建时间：2016-12-21
 */
package com.yunip.mapper.config;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.config.ClusterConfig;
import com.yunip.model.config.query.ClusterConfigQuery;

/**
 * 集群服务器配置信息数据库访问接口
 */
@Repository("iClusterConfigDao")
public interface IClusterConfigDao extends IBaseDao<ClusterConfig>{
    /**
     * @describe 根据查询条件进行查询
     * @return 返回list结果集
     */
    List<ClusterConfig> selectByQuery(ClusterConfigQuery query);
    
    /**
     * @describe 根据查询条件进行查询，返回符合查询条件结果总条数
     * @return 返回总条数
     */
    int selectCountByQuery(ClusterConfigQuery query);
}
