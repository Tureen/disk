/*
 * 描述：公共操作管理类
 * 创建人：jian.xiong
 * 创建时间：2016-6-25
 */
package com.yunip.manage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SystemContant;

/**
 * 公共操作管理类
 */
public class CommonManager {
    /**
     * 重新加载定义的静态常量类
     */
    public static void reloadSysCache() {
        SystemContant.FILE_SERVICE_URL = SysConfigHelper.getValue(
                SystemContant.BASICSCODE, SystemContant.SYSTEM_FILE_DOMAIN);

        SystemContant.DISK_SERVICE_URL = SysConfigHelper.getValue(
                SystemContant.SYSTEMCODE, SystemContant.SYSTEM_DISK_DOMAIN);

        SystemContant.API_SERVICE_URL = SysConfigHelper.getValue(
                SystemContant.SYSTEMCODE, SystemContant.SYSTEM_API_DOMAIN);

        SystemContant.ADMIN_SERVICE_URL = SysConfigHelper.getValue(
                SystemContant.SYSTEMCODE, SystemContant.SYSTEM_ADMIN_DOMAIN);
    }

    private static void reloadMyproperties() {
        String enPath = Thread.currentThread().getContextClassLoader().getResource(
                "i18n/myproperties_en.properties").getPath();
        String zhPath = Thread.currentThread().getContextClassLoader().getResource(
                "i18n/myproperties_zh.properties").getPath();
        reloadMyproperties(enPath);
        reloadMyproperties(zhPath);
    }

    private static void reloadMyproperties(String path) {
        FileOutputStream outputStream = null;
        try {
            // 读取配置文件
            FileInputStream inputStream = new FileInputStream(path);
            Properties prop = new Properties();
            // 加载
            prop.load(inputStream);
            // 修改键值对
            prop.setProperty("i18n_pclogin_theme", "123");
            // 输出
            outputStream = new FileOutputStream(path);
            prop.store(outputStream, "update message");
        }
        catch (Exception e) {}
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateKeyValue(Properties prop) {
        //        prop.setProperty(key, value)
    }
}
