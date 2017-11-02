/*
 * 描述：消息（通知）表数据库操作接口
 * 创建人：jian.xiong
 * 创建时间：2016-8-16
 */
package com.yunip.mapper.sys;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.sys.Message;
import com.yunip.model.sys.query.MessageQuery;

/**
 * 消息（通知）表数据库操作接口
 */
@Repository("iMessageDao")
public interface IMessageDao extends IBaseDao<Message>{
    /**
     * 根据条件查询列表
     * @param query 查询对象
     * @return
     */
    List<Message> selectByQuery(MessageQuery query);
    
    /***
     * 根据条件查询总记录数据
     * @param query 查询对象
     * @return
     */
    int selectCountByQuery(MessageQuery query);
    
    /***
     * 批量删除
     * @param query 查询对象
     * @return
     */
    int batchDelete(MessageQuery query);
    
    /***
     * 批量将未读消息标记为已读
     * @param query 查询对象
     * @return
     */
    int batchMarkRead(MessageQuery query);
}
