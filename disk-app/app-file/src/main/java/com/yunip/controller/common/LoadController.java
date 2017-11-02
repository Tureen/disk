package com.yunip.controller.common;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.I18nResourceHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.manage.CommonManager;
import com.yunip.system.ResourcesSetting;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/load")
public class LoadController extends BaseController {
    Logger log = Logger.getLogger(this.getClass());

    /****
     * 加载数据
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/data")
    @ResponseBody
    public JsonData<Object> index(HttpServletRequest request) {
        JsonData<Object> data = new JsonData<Object>();
        SysConfigHelper.reload();
        //重新加载全局的静态系统变量
        CommonManager.reloadSysCache();
        //加载保存上传文件的根目录
        String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());//判断是否开启集群部署
        if((BasicsBool.YES.getBool()).equals(isOpenCluster)){////没开启集群则根据配置文件中的来
            String config_server_code = ResourcesSetting.getSetting("server.code");//读取配置的文件服务器所属编号if(!StringUtil.nullOrBlank(config_server_code)){
            if(!StringUtil.nullOrBlank(config_server_code)){
                SystemContant.SERVER_CODE = config_server_code;
                if(ClusterConfigHelper.getValue(SystemContant.SERVER_CODE) != null){//根据配置文件中的文件服务器编号加载其对应的配置信息
                    Constant.ROOTPATH = ClusterConfigHelper.getValue(SystemContant.SERVER_CODE).getFilePath();
                    log.info("当前服务器编号：" + SystemContant.SERVER_CODE + "文件存储目录：" + Constant.ROOTPATH);
                }
            }
        }else{//没开启集群则根据数据库配置的来
            Constant.ROOTPATH = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH);
            SystemContant.SERVER_CODE = null;
        }
        //重新加载国际化资源文件
        I18nResourceHelper.reload();
        return data;
    }
    
    /**
     * 获取项目路径
     * @param request
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/path")
    @ResponseBody
    public JsonData<Object> projectPath(HttpServletRequest request){
        JsonData<Object> data = new JsonData<Object>();
        String path = request.getSession().getServletContext().getRealPath("/");
        data.setResult(path);
        return data;
    }
    
    /**
     * 重加载文件服务器配置
     * @param request
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/cluster")
    @ResponseBody
    public JsonData<Object> reloadClusterHelper(HttpServletRequest request){
        JsonData<Object> data = new JsonData<Object>();
        ClusterConfigHelper.reload();
        return data;
    }
    
    /****
     * 清理预览文件
     * JsonData<Object> 
     */
    @RequestMapping("/clearPreviewFile")
    @ResponseBody
    public JsonData<Object> clearPreviewFile(HttpServletRequest request) {
        JsonData<Object> data = new JsonData<Object>();
        log.info("手动清理文件预览目录start……");
        long t1 = System.currentTimeMillis();
        //读取缓存目录下的文件夹
        List<File> list = FileUtil.getDirFiles(Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR);
        log.info("手动缓存目录下文件夹数量：" + list.size());
        for(File file : list){
            //删除缓存目录文件
            if (file.exists() && file.isFile()) {
                boolean flag = file.delete();
                log.info("目录：" + file.getPath() + "  删除" + (flag ? "成功" : "失败"));
            }
        }
        long t2 = System.currentTimeMillis();
        log.info("手动清理文件预览目录，共耗时：" + (t2 - t1) + "毫秒");
        log.info("手动清理文件预览目录end");
        return data;
    }
    
    /****
     * 清理产生的临时文件
     * JsonData<Object> 
     */
    @RequestMapping("/clearTempFile")
    @ResponseBody
    public JsonData<Object> clearTempFile(HttpServletRequest request) {
        JsonData<Object> data = new JsonData<Object>();
        log.info("手动清理产生的临时文件start……");
        
        log.info("清理过期的缓存目录start……");
        long t1 = System.currentTimeMillis();
        //读取缓存目录下的文件夹
        List<File> tempList = FileUtil.getDirSubDirs(Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR);
        log.info("缓存目录下文件夹数量：" + tempList.size());
        Date now = new Date();
        int expireDay = 1;//间隔天数
        for(File file : tempList){
            //获取目录最后修改时间
            Date lastModified = new Date(file.lastModified());
            log.info(file.getPath() + "最后修改时间：" + DateUtil.getDateFormat(lastModified, DateUtil.YMDHMS_DATA) + "相隔：" + DateUtil.getDayDiff(lastModified, now) + "天");
            if(DateUtil.getDayDiff(lastModified, now) >= expireDay){//判断目录最后修改时间与当前时间相隔超过多少天后，就进行删除（间隔时间是为了防止删除断点续传已经上传成功的文件块）
                //删除过期的缓存目录
                boolean flag = FileUtil.deleteFolder(file);
                log.info("目录：" + file.getPath() + "  删除" + (flag ? "成功" : "失败"));
            }
        }
        long t2 = System.currentTimeMillis();
        log.info("清理过期的缓存目录，共耗时：" + (t2 - t1) + "毫秒");
        log.info("清理过期的缓存目录end");
        
        log.info("手动清理文件预览目录start……");
        long t3 = System.currentTimeMillis();
        //读取缓存目录下的文件夹
        List<File> list = FileUtil.getDirFiles(Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR);
        log.info("手动缓存目录下文件夹数量：" + list.size());
        for(File file : list){
            //删除缓存目录文件
            if (file.exists() && file.isFile()) {
                boolean flag = file.delete();
                log.info("目录：" + file.getPath() + "  删除" + (flag ? "成功" : "失败"));
            }
        }
        long t4 = System.currentTimeMillis();
        log.info("手动清理文件预览目录，共耗时：" + (t4 - t3) + "毫秒");
        log.info("手动清理文件预览目录end");
        log.info("手动清理产生的临时文件end");
        return data;
    }
}
