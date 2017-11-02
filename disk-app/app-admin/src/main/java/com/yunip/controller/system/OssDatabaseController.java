package com.yunip.controller.system;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.model.sys.Backup;
import com.yunip.model.sys.query.BackupQuery;
import com.yunip.oss.operate.OssUploadFile;
import com.yunip.service.IBackupService;
import com.yunip.utils.util.StringUtil;
import com.yunip.web.common.Authority;

/**
 * 数据库文件备份至阿里云OSS对象存储
 */
@Controller
@RequestMapping("/ossdatabase")
public class OssDatabaseController extends BaseController {
    @Resource(name = "iBackupService")
    private IBackupService backupService;

    /**
     * 备份与还原页面
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @Authority("P42")
    @RequestMapping("/index")
    public String index(HttpServletRequest request,BackupQuery query) {
        query = backupService.queryBackup(query);
        request.setAttribute("query", query);
        return "ossdatabase/index";
    }
    
    /**
     * 数据库备份文件上传至阿里云OSS
     */
    @RequestMapping("/uploadOss")
    @ResponseBody
    public JsonData<String> emportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonData<String> data = new JsonData<String>();
        String backupId = request.getParameter("backupId");
        if(StringUtil.nullOrBlank(backupId)){
            throw new MyException(AdminException.PARAMERROR);
        }
        Backup backup = backupService.getBackupById(Integer.valueOf(backupId.trim()));
        if(backup == null){
            throw new MyException(AdminException.DATABASEBACKUPFILENOTEXISTS);
        }
        String path = backup.getSqlPath();
        if(StringUtil.nullOrBlank(backupId)){
            throw new MyException(AdminException.DATABASEBACKUPFILENOTEXISTS);
        }
        File file = new File(path);
        if(!file.exists()){
            throw new MyException(AdminException.DATABASEBACKUPFILENOTEXISTS);
        }
        try {
            //操作帐号bucket名称
            String bucketName = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ALIYUN_OSS_BUCKET_NAME.getKey());
            //访问地址
            String endpoint = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ALIYUN_OSS_DOMAIN_ENDPOINT.getKey());
            //操作账户key ID
            String accessKeyId = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_ID.getKey());
            //操作账户密钥
            String accessKeySecret = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ALIYUN_OSS_ACCESS_KEY_SECRET.getKey());
            
            OssUploadFile.uploadLocalFile(endpoint, accessKeyId, accessKeySecret, bucketName, SystemContant.OSS_DATABASE_BACKUP_FOLDEDR_NAME, path);
        }catch (Exception e) {
            throw new MyException(AdminException.OPERATEALIYUNOSSERROR);
        }
        return data;
    }
}
