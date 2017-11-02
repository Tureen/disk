/*
 * 描述：计划任务入口类
 * 创建人：jian.xiong
 * 创建时间：2016-5-18
 */
package com.yunip.task;

import java.io.File;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.memcached.MemCachedClient;
import com.yunip.model.config.ClusterConfig;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.util.FileUtil;

/**
 * 计划任务入口类
 */
@Component("TaskEntry")
public class TaskEntry {
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name = "iFileEncryptDecryptService")
    private IFileEncryptDecryptService fileEncryptDecryptService;
    
    /**
     * 过期天数
     */
    private int expireDay = 1;
    
    /**
     * 预览文件过期天数
     */
    private int previewExpireDay = 5;

    /**
     * 清理上传过期的缓存目录文件
     */
    public void clearExpireTempDir(){
        Date now = new Date();
        log.info("清理过期的缓存目录start……");
        long t1 = System.currentTimeMillis();
        //读取缓存目录下的文件夹
        List<File> list = FileUtil.getDirSubDirs(Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR);
        log.info("缓存目录下文件夹数量：" + list.size());
        for(File file : list){
            //获取目录最后修改时间
            Date lastModified = new Date(file.lastModified());
            log.info(file.getPath() + "最后修改时间：" + DateUtil.getDateFormat(lastModified, DateUtil.YMDHMS_DATA) + "相隔：" + DateUtil.getDayDiff(lastModified, now) + "天");
            if(DateUtil.getDayDiff(lastModified, now) >= expireDay){//判断目录最后修改时间与当前时间相隔超过多少天后，就进行删除
                //删除过期的缓存目录
                boolean flag = FileUtil.deleteFolder(file);
                log.info("目录：" + file.getPath() + "  删除" + (flag ? "成功" : "失败"));
            }
        }
        long t2 = System.currentTimeMillis();
        log.info("清理过期的缓存目录，共耗时：" + (t2 - t1) + "毫秒");
        log.info("清理过期的缓存目录end");
    }
    
    /**
     * 清理文件预览目录下过期的文件
     */
    public void clearExpirePreviewDir(){
        Date now = new Date();
        log.info("清理文件预览目录start……");
        long t1 = System.currentTimeMillis();
        //读取缓存目录下的文件夹
        List<File> list = FileUtil.getDirFiles(Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR);
        log.info("缓存目录下文件夹数量：" + list.size());
        for(File file : list){
            //获取目录最后修改时间
            Date lastModified = new Date(file.lastModified());
            log.info(file.getPath() + "最后修改时间：" + DateUtil.getDateFormat(lastModified, DateUtil.YMDHMS_DATA) + "相隔：" + DateUtil.getDayDiff(lastModified, now) + "天");
            if(DateUtil.getDayDiff(lastModified, now) >= previewExpireDay){//判断目录最后修改时间与当前时间相隔超过多少天后，就进行删除
                //删除过期的缓存目录
                if (file.exists() && file.isFile()) {
                    boolean flag = file.delete();
                    log.info("目录：" + file.getPath() + "  删除" + (flag ? "成功" : "失败"));
                }
            }
        }
        long t2 = System.currentTimeMillis();
        log.info("清理文件预览目录，共耗时：" + (t2 - t1) + "毫秒");
        log.info("清理文件预览目录end");
    }
    
    /**
     * 计算磁盘剩余空间并加入进memcache
     * void 
     * @exception
     */
    @SuppressWarnings("static-access")
    public void addMemcacheDiskspace(){
        //判断是否开启了分布式部署
        String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());
        if((BasicsBool.NO.getBool()).equals(isOpenCluster)){
            return;
        }
        log.info("计算磁盘剩余空间start……");
        long t1 = System.currentTimeMillis();
        //获取ip
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            String localip=ia.getHostAddress();
            //获取该服务器指定存放目录
            List<ClusterConfig> clusterConfigs = ClusterConfigHelper.getAllList();
            for(ClusterConfig clusterConfig : clusterConfigs){
                //如果缓存配置中的服务器ip与本机ip一致，获取到其服务器编号及指向磁盘，前者做键后者做剩余磁盘空间计算的指向目标
                if(clusterConfig.getClusterIp().equals(localip)){
                    String clusterCode = clusterConfig.getClusterCode();
                    String filePath = clusterConfig.getFilePath();
                    //获取盘符
                    filePath = filePath.substring(0, filePath.indexOf(":")+1);
                    File diskPartition = new File(filePath);
                    //该磁盘剩余空间(Byte)
                    long freePartitionSpace = diskPartition.getFreeSpace();
                    //该磁盘预留空间(MB)
                    String reserveSpace = clusterConfig.getReserveSpace();
                    long reserveSpaceSize = Long.valueOf(reserveSpace) * 1024 * 1024;
                    freePartitionSpace = freePartitionSpace - reserveSpaceSize;
                    MemCachedClient memCachedClient = new MemCachedClient();
                    if(freePartitionSpace > 0){
                        //写入Memcache中
                        memCachedClient.set(clusterCode, freePartitionSpace);
                    }else{
                        memCachedClient.delete(clusterCode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t2 = System.currentTimeMillis();
        log.info("计算磁盘剩余空间，共耗时：" + (t2 - t1) + "毫秒");
        log.info("计算磁盘剩余空间end");
    }
    
    /**
     * 加密待加密文件
     */
    public void waitForEncryptFile(){
        log.info("加密待加密文件start……");
        long t1 = System.currentTimeMillis();
        fileEncryptDecryptService.addAllWaitForEncryptionFile();
        long t2 = System.currentTimeMillis();
        log.info("加密待加密文件end，共耗时：" + (t2 - t1) + "毫秒");
    }
    
   /* public static void main(String[] args) {
        List<File> list = FileUtil.getDirFiles(Constant.FILE_PREVIEW_DIR);
        System.out.println(list.size());
    }*/
}
