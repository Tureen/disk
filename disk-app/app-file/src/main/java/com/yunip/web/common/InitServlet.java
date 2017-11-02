/*
 * 描述：系统初始化Servlet
 * 创建人：jian.xiong
 * 创建时间：2016-5-13
 */
package com.yunip.web.common;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.manage.CommonManager;
import com.yunip.socket.server.SocketServer;
import com.yunip.system.ResourcesSetting;
import com.yunip.task.LuceneEntry;
import com.yunip.task.QueueEntry;
import com.yunip.utils.util.StringUtil;

/**
 * 系统初始化Servlet
 */
public class InitServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    
    /**
     * app上传soket服务
     */
    private SocketServer server;
    
    Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public void destroy() {
        //关闭时退出socket服务
        server.quit();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            logger.info("加载系统配置……");
            SysConfigHelper.reload();
            CommonManager.reloadSysCache();
            //启动queue线程start
            QueueEntry entry = new QueueEntry();
            entry.start();
            //启动queue线程end
            //启动创建索引线程start
             LuceneEntry luceneEntry = new LuceneEntry();
            luceneEntry.start();
            //end
            //启动文件加密服务线程start
            FileEncryptService fileEncryptService = new FileEncryptService();
            fileEncryptService.start();
            //启动文件加密服务线程end
            
            //加载保存上传文件的根目录
            Constant.ROOTPATH = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH);
            logger.info("保存文件根目录：" + Constant.ROOTPATH);
            logger.info("加载系统配置结束");
            
            String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());//判断是否开启集群部署
            if((BasicsBool.YES.getBool()).equals(isOpenCluster)){
            	String config_server_code = ResourcesSetting.getSetting("server.code");//读取配置的文件服务器所属编号
                if(!StringUtil.nullOrBlank(config_server_code)){
                    SystemContant.SERVER_CODE = config_server_code;
                    if(ClusterConfigHelper.getValue(SystemContant.SERVER_CODE) != null){//根据配置文件中的文件服务器编号加载其对应的配置信息
                        Constant.ROOTPATH = ClusterConfigHelper.getValue(SystemContant.SERVER_CODE).getFilePath();
                        logger.info("当前服务器编号：" + SystemContant.SERVER_CODE + "文件存储目录：" + Constant.ROOTPATH);
                    }
                }
            }
            
            String socketPort = SysConfigHelper.getValue(SystemContant.OPENCODE, "APPUPLOADSOCKETPORT");
            if(!StringUtil.nullOrBlank(socketPort)){
                logger.info("启动socket上传服务,端口号:" + socketPort);
                server = new SocketServer(Integer.parseInt(socketPort));
                new Thread(new Runnable() {         
                    @Override
                    public void run() {
                        try {
                            server.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else{
                logger.info("socket上传服务,端口号为空不予以启动");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("启动socket上传服务失败！" + e.getMessage());
        }
    }
}
