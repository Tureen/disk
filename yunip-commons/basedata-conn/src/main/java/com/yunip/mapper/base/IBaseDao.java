/**
 * 基础dao接口类型
 */
package com.yunip.mapper.base;

import java.util.List;


/**
 * @author can.du
 *
 */
public interface IBaseDao<T> {

    /**
     * 增加对象
     * @param t  添加的对象
     * @return
     */
    int insert(T t);
    
    /**
     * 修改对象
     * @param t  修改的对象
     * @return
     */
    int update(T t);
    
    /**
     * 根据主键获取对象
     * @param id 主键
     * @return
     */
    T selectById(int id);
    
    /**
     * 根据主键获取对象
     * @param id 主键
     * @return
     */
    T selectById(String id);
    
    /**
     * 根据主键删除对象
     * @param id 主键
     * @return
     */
    int delById(int id);
    
    /**
     * 根据主键删除对象
     * @param id 主键
     * @return
     */
    int delById(String id);
    
    /***
     * 
     * @return
     */
    List<T> selectByAll();
    
}
