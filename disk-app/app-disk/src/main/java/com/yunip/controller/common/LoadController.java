package com.yunip.controller.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.I18nResourceHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.DiskException;
import com.yunip.manage.CommonManager;
import com.yunip.web.common.License;

@Controller
@RequestMapping("/load")
public class LoadController extends BaseController {

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
        CommonManager.reloadSysCache();
        I18nResourceHelper.reload();
        //加载保存上传文件的根目录
        String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());//判断是否开启集群部署
        if((BasicsBool.YES.getBool()).equals(isOpenCluster)){//开启集群则重新加载下
            ClusterConfigHelper.reload();
        }
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
     * 获得授权时间
     * @param request
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/licensehour")
    @ResponseBody
    public JsonData<Object> sendlicenseHour(HttpServletRequest request){
        JsonData<Object> data = new JsonData<Object>();
        data.setResult(License.hour);
        return data;
    }
    
    /**
     * 授权时间注册
     * @param request
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/reglicense")
    @ResponseBody
    public JsonData<Object> regLicense(HttpServletRequest request){
        JsonData<Object> data = new JsonData<Object>();
        String serCode = this.getPostContent(request);
        try {
            License.generateCode(request,serCode);
        }catch (Exception e) {
            if(e instanceof MyException){
                MyException ex = (MyException) e;
                data.setCode(ex.getErrorCode());
                data.setCodeInfo(ex.getMsg());
            }else{
                data.setCode(DiskException.ERROR.getCode());
                data.setCodeInfo(DiskException.ERROR.getMsg());
            }
        }
        return data;
    }
    
    /**
     * 读取图片
     * @param request
     * @param response
     * @throws Exception  
     * void 
     * @exception
     */
    @RequestMapping("/portrait")
    public void portrait(HttpServletRequest request, HttpServletResponse response) throws Exception{
        final String path = request.getParameter("Path");
        response.setHeader("Pragma","No-cache");    
        response.setHeader("Cache-Control","no-cache");    
        response.setDateHeader("Expires", 0);    
        
        File file = new File(path);
        if(file.exists()){
            BufferedInputStream bis = null;  
            OutputStream os = null;  
            FileInputStream fileInputStream = new FileInputStream(new File(path));  
            
            bis = new BufferedInputStream(fileInputStream);  
            byte[] buffer = new byte[512];  
            response.reset();  
            response.setCharacterEncoding("UTF-8");  
            //不同类型的文件对应不同的MIME类型  
            response.setContentType("image/png");  
            //文件以流的方式发送到客户端浏览器  
            //response.setHeader("Content-Disposition","attachment; filename=img.jpg");  
            //response.setHeader("Content-Disposition", "inline; filename=img.jpg");  
            response.setContentLength(bis.available());  
            os = response.getOutputStream();  
            int n;  
            while ((n = bis.read(buffer)) != -1) {  
              os.write(buffer, 0, n);  
            }
            bis.close();  
            os.flush();  
            os.close();
        }
    }
}
