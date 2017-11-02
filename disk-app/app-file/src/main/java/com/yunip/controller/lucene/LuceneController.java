/*
 * 描述：授权操作文件服务器控制器
 * 创建人：jian.xiong
 * 创建时间：2016-5-12
 */
package com.yunip.controller.lucene;

import java.io.File;
import java.text.DecimalFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.Constant;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.FileManagerBaseController;
import com.yunip.enums.fileservers.FileServersException;
import com.yunip.model.UploadUser;
import com.yunip.service.ILuceneService;
import com.yunip.utils.lucene.Page;

/**
 * 授权操作文件服务器控制器
 */
@RequestMapping("/lucene")
@Controller
public class LuceneController extends FileManagerBaseController{
    
    Logger logger = Logger.getLogger(this.getClass());
    
    @Resource(name = "iLuceneService")
    private ILuceneService luceneService;
    
    @RequestMapping("/index")
    public String index(HttpServletRequest request, Page<Object> page) throws Exception{
        UploadUser uploadUser = this.getMyInfo(request);
        if(uploadUser == null){
            throw new MyException(FileServersException.SFYZSX);
        }
        //获取用户登录信息
        long start = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("0.###");
        String employeeId = uploadUser.getIdentity();
        String indexPath = Constant.ROOTPATH + File.separator + Constant.INDEX_DIR  + File.separator + 
                employeeId + File.separator;
        page.setQueryIndexDir(indexPath);
        if(StringUtils.isNotBlank(page.getQueryValue())){
            page = luceneService.getPageByQuery(page);
        }
        long end = System.currentTimeMillis();
        double time = (Double.valueOf(end - start))/1000;
        page.setQueryTime(df.format(time));
        request.setAttribute("page", page);
        request.setAttribute("diskServer", SystemContant.DISK_SERVICE_URL);
        return "lucene/index";
    }
}
