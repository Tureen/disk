/*
 * 描述：分布式集群部署访问文件服务器的路由策略
 * 创建人：jian.xiong
 * 创建时间：2016-12-16
 */
package com.yunip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFileDeleteDao;
import com.yunip.mapper.teamwork.ITeamworkFileDao;
import com.yunip.memcached.MemCachedClient;
import com.yunip.model.config.ClusterConfig;
import com.yunip.model.disk.File;
import com.yunip.model.disk.FileDelete;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.utils.util.RandomUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 分布式集群部署访问文件服务器的路由策略
 */
@Component("fileServiceRouteStrategy")
public class FileServiceRouteStrategy {
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @Resource(name = "iFileDeleteDao")
    private IFileDeleteDao fileDeleteDao;
    
    @Resource(name = "iTeamworkFileDao")
    private ITeamworkFileDao teamworkFileDao;
    
    /**
     * 根据数据库配置查询是否开启了分布式部署
     * @return  true:开启  false:未开启
     */
    private boolean isOpenCluster(){
        String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());
        if((BasicsBool.YES.getBool()).equals(isOpenCluster)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 下载文件时获得对应的请问地址
     * @param fileId 文件ID
     * @return 返回文件所属服务器的请求地址
     */
    public String getDownloadFileServiceAddress(DownloadFileEntity entity){
        String url = SystemContant.FILE_SERVICE_URL;
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        if(entity != null){
            List<String> files = entity.getFiles();
            if(files != null && files.size() == 1){
                File file = fileDao.selectById(files.get(0));
                if(file != null){
                    url = getFileServiceAddressByServerCode(file.getServerCode());
                }
            }else{
                throw new MyException(DiskException.COLONY_NOT_SUPPORTED_DOWNLOAD);
            }
        }
        return url;
    }
    
    /**
     * 回收站下载文件时获得对应的请问地址
     * @param fileId 文件ID
     * @return 返回文件所属服务器的请求地址
     */
    public String getDownloadRecycleFileServiceAddress(DownloadFileEntity entity){
        String url = SystemContant.FILE_SERVICE_URL;//admin项目下只能通过SysConfigHelper获取缓存中的值
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        if(entity != null){
            List<String> files = entity.getFiles();
            if(files != null && files.size() == 1){
                FileDelete fileDelete = fileDeleteDao.selectById(files.get(0));
                if(fileDelete != null){
                    url = getFileServiceAddressByServerCode(fileDelete.getServerCode());
                }
            }else{
                throw new MyException(DiskException.COLONY_NOT_SUPPORTED_DOWNLOAD);
            }
        }
        return url;
    }
    
    /**
     * 根据文件ID，获取对应的访问地址
     * @param fileId 文件ID
     * @return 返回文件所属服务器的请求地址
     */
    public String getFileServiceAddressByFileId(Integer fileId){
        String url = SystemContant.FILE_SERVICE_URL;
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        if(fileId != null){
            File file = fileDao.selectById(fileId);
            if(file != null){
                url = getFileServiceAddressByServerCode(file.getServerCode());
            }
        }
        return url;
    }
    
    /**
     * 根据协作文件ID，获取对应的访问地址
     * @param fileId 文件ID
     * @return 返回文件所属服务器的请求地址
     */
    public String getFileServiceAddressByTeamworkFileId(Integer teamworkFileId){
        String url = SystemContant.FILE_SERVICE_URL;
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        if(teamworkFileId != null){
            TeamworkFile file = teamworkFileDao.selectById(teamworkFileId);
            if(file != null){
                url = getFileServiceAddressByServerCode(file.getServerCode());
            }
        }
        return url;
    }
    
    /**
     * 根据文件所属服务器编号，获取对应的访问地址
     * @param serverCode 服务器编号
     * @return 返回文件所属服务器的请求地址
     */
    public String getFileServiceAddressByServerCode(String serverCode){
        String url = SystemContant.FILE_SERVICE_URL;
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        if(!StringUtil.nullOrBlank(serverCode)){
            ClusterConfig config = ClusterConfigHelper.getValue(serverCode);
            if(config != null){
                url = config.getFileUrl();
            }
        }else{
            throw new MyException(FileServersException.CLUSTERCONFIGERROR);
        }
        return url;
    }
    
    /**
     * 上传文件服务地址
     */
    public String getUploadFileServiceAddress(){
        String url = SystemContant.FILE_SERVICE_URL;
        if(!isOpenCluster()){//判断是否开启了分布式部署
            return url;
        }
        List<ClusterConfig> list = ClusterConfigHelper.getAllList();
        if(list != null && list.size() > 0){
            if(list.size() == 1){
                url = list.get(0).getFileUrl();
            }else{
                long maxLong = 0;//所有磁盘中最大剩余磁盘
                for(ClusterConfig config : list){
                    MemCachedClient memCachedClient = new MemCachedClient();
                    Object object = memCachedClient.get(config.getClusterCode());
                    long freeSpace = 0l;
                    if(object != null){
                        freeSpace = (Long) object;
                    }
                    config.setFreeSpace(freeSpace);
                    if(freeSpace != 0l){
                        maxLong = maxLong >= freeSpace ? maxLong : freeSpace;
                    }
                }
                long minAllowLong = maxLong;//被允许加入的磁盘中，最小的磁盘大小
                for(ClusterConfig config : list){
                    if(config.getFreeSpace() > maxLong/10){
                        minAllowLong = minAllowLong <= config.getFreeSpace() ? minAllowLong : config.getFreeSpace();
                    }
                }
                if(maxLong == 0 || minAllowLong == 0){
                    throw new MyException(FileServersException.NO_ALLOW_FILE_SPACE);
                }
                //进行随机取值的加权集合
                List<String> urls = new ArrayList<String>();
                for(ClusterConfig config : list){
                    if(config.getFreeSpace() > maxLong/10){
                        //其磁盘空间是最大磁盘的百分之10以上，加入进可选磁盘中，并计算所占份额进行加权随机
                        int ratio = (int) ((config.getFreeSpace())/minAllowLong);
                        while ((ratio--) > 0) {
                            urls.add(config.getFileUrl());
                        }
                    }
                }
                url = urls.get(RandomUtil.getInt(0, urls.size()));
            }
        }
        return url;
    }
    
    /**
     * 获得所有集群文件服务地址
     */
    public List<String> getAllFileServiceAddress(){
        List<String> urls = new ArrayList<String>();
        if(!isOpenCluster()){//判断是否开启了分布式部署
            urls.add(SystemContant.FILE_SERVICE_URL);
            return urls;
        }
        List<ClusterConfig> list = ClusterConfigHelper.getAllList();
        if(list != null && list.size() > 0){
            for(ClusterConfig config : list){
                urls.add(config.getFileUrl());
            }
        }
        return urls;
    }
    
    /**
     * 获得所有集群文件服务器部署对应的ip地址
     */
    public List<String> getAllFileServiceIPAddress(){
        List<String> urls = new ArrayList<String>();
        List<ClusterConfig> list = ClusterConfigHelper.getAllList();
        if(list != null && list.size() > 0){
            for(ClusterConfig config : list){
                urls.add(config.getClusterIp());
            }
        }
        return urls;
    }
    
    /**
     * 获得所有集群文件服务器部署对应的共享目录
     */
    public List<String> getAllFileServiceSharePath(){
        List<String> urls = new ArrayList<String>();
        List<ClusterConfig> list = ClusterConfigHelper.getAllList();
        if(list != null && list.size() > 0){
            for(ClusterConfig config : list){
                urls.add(config.getSharePath());
            }
        }
        return urls;
    }
    
    public static void main(String[] args) {
        
    }
}
