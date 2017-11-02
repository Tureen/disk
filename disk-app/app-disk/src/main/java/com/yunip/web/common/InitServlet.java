/*
 * 描述：系统初始化Servlet
 * 创建人：junbin.zhou
 * 创建时间：2012-8-23
 */
package com.yunip.web.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.manage.CommonManager;


/**
 * 系统初始化Servlet
 */
public class InitServlet extends HttpServlet {
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;
    
    /**
     * Servlet初始化
     */
    public void init() throws ServletException {
        //加载系统配置
        SysConfigHelper.reload();
        CommonManager.reloadSysCache();
        SysContant.ROOTPATH = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH);
        SyncLicense.initLicenseHour(this.getServletContext());
        SyncLicense license = new SyncLicense();
        license.setServletContext(this.getServletContext());
        license.start();
      // System.out.println("-----++------" + ResourcesSetting.getSetting("license.code"));
        
        
        
    }
}
