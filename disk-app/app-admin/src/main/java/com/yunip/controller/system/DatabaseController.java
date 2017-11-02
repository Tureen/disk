package com.yunip.controller.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminException;
import com.yunip.model.sys.Backup;
import com.yunip.model.sys.query.BackupQuery;
import com.yunip.service.IBackupService;
import com.yunip.utils.date.DateUtil;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 数据库备份与还原
 */
@Controller
@RequestMapping("/database")
public class DatabaseController extends BaseController {
    //mysql工具集所在目录路径
    private final static String path = DatabaseController.class.getResource(SystemContant.MYSQL_BACKUP_TOOLS).getPath();
    
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
    @RequestMapping("/index")
    public String index(HttpServletRequest request,BackupQuery query) {
        query.setPageSize(10);
        query = backupService.queryBackup(query);
        request.setAttribute("query", query);
        return "database/index";
    }
    
    /**
     * 导出
     * @param response
     * @return
     * @throws IOException  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/export")
    @ResponseBody
    public JsonData<String> emportData(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        JsonData<String> data = new JsonData<String>();
        String upath = SysConfigHelper.getValue(SystemContant.OPENCODE, SystemContant.FILEROOTPATH)+"/database";//本地存放路径
        InputStream is = DatabaseController.class.getClassLoader().getResourceAsStream(
                "yunipconfig.properties");
        Properties properties = new Properties();
        properties.load(is);
        Runtime runtime = Runtime.getRuntime();
        StringBuffer command = new StringBuffer();
        String username = properties.getProperty("db.jdbc.username");//用户名  
        String password = properties.getProperty("db.jdbc.password");//用户密码  
        String url = properties.getProperty("db.jdbc.url");//url,通过截取获得host port 等
        String host = url.split(":")[2].split("//")[1];//从哪个主机导出数据库，如果没有指定这个值，则默认取localhost
        String port = url.split(":")[3].split("/")[0];
        String exportDatabaseName = url.split(":")[3].split("/")[1].split("\\?")[0];//需要导出的数据库名  
        String fileName = exportDatabaseName
                + "_" + DateUtil.getDateFormat(new Date(), DateUtil.YMDSTRING_DATA)
                + ".sql";
        String exportPath = upath + "/"+fileName;//导出路径  
        //命令拼接(yun_sys_backup是备份数据库记录表，导出时忽略此表)
        command.append(path).append("mysqldump -u").append(username).append(
                " -p").append(password)//密码是用的小p，而端口是用的大P。  
        .append(" -h").append(host).append(" -P").append(port).append(" ").append(
                exportDatabaseName).append(" --ignore-table=").append(exportDatabaseName).append(".yun_sys_backup").append(" -r ").append(exportPath);
        //存放文件夹判断
        File file = new File(upath);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            Process proc = runtime.exec(command.toString());
            proc.waitFor(); // 阻塞当前线程，并等待外部程序中止后获取结果码
            //加密参数
            String params = exportPath + "|" + fileName;
            Des des = new Des();
            params = des.strEnc(params, SystemContant.DATABASEKEY, null, null);
            //添加数据库
            Backup backup = new Backup();
            backup.setSqlName(fileName);
            backup.setSqlPath(exportPath);
            backup.setCreateAdmin(getMyinfo(request).getEmployeeName());
            backupService.addBackup(backup);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MyException(AdminException.DATABASEEXPOERTERROR);
        }
        return data;
    }

    /**
     * 导入
     * @param request
     * @param response
     * @return
     * @throws Exception  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/import")
    @ResponseBody
    public JsonData<String> importData(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        JsonData<String> data = new JsonData<String>();
        String importPath = null;
        InputStream is = DatabaseController.class.getClassLoader().getResourceAsStream(
                "yunipconfig.properties");
        Properties properties = new Properties();
        properties.load(is);
        Runtime runtime = Runtime.getRuntime();
        //因为在命令窗口进行mysql数据库的导入一般分三步走，所以所执行的命令将以字符串数组的形式出现  
        String username = properties.getProperty("db.jdbc.username");//用户名  
        String password = properties.getProperty("db.jdbc.password");//密码  
        String url = properties.getProperty("db.jdbc.url");//url,通过截取获得host port 等
        String host = url.split(":")[2].split("//")[1];//导入的目标数据库所在的主机  
        String port = url.split(":")[3].split("/")[0];//使用的端口号  
        String importDatabaseName = url.split(":")[3].split("/")[1].split("\\?")[0];//导入的目标数据库的名称  
        importPath = request.getSession().getServletContext().getRealPath("/database");//本地存放路径
        //将上传文件保存至本地
        String backupId = ""+request.getParameter("backupId");
        String topath = null;
        if(backupId.trim().equals("null") || StringUtil.nullOrBlank(backupId.trim())){
            topath = fileDownLoad(request,response,importPath);
        } 
        //将数据库中文件地址保存至本地
        else{
            Backup backup = backupService.getBackupById(Integer.valueOf(backupId.trim()));
            topath = backup.getSqlPath();
//            fileReplace(backup.getSqlPath(),importPath+"/import.sql");
        }
        //第一步，获取登录命令语句  
        String loginCommand = new StringBuffer().append(path).append("mysql -u").append(username).append(" -p").append(password).append(" -h").append(host)  
        .append(" -P").append(port).toString();  
        //第二步，获取切换数据库到目标数据库的命令语句  
        String switchCommand = new StringBuffer("use ").append(importDatabaseName).toString();  
        //第三步，获取导入的命令语句  
        String importCommand = new StringBuffer("source ").append(topath).toString();  
        //需要返回的命令语句数组  
        String[] commands = new String[] {loginCommand, switchCommand, importCommand};  
        //runtime.exec(cmdarray);//这里也是简单的直接抛出异常  
        Process process = runtime.exec(commands[0]);
        //执行了第一条命令以后已经登录到mysql了，所以之后就是利用mysql的命令窗口  
        //进程执行后面的代码  
        OutputStream os = process.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        //命令1和命令2要放在一起执行  
        writer.write(commands[1] + "\r\n" + commands[2]);
        writer.flush();
        writer.close();
        os.close();
        return data;
    }
    
    /**
     * 上传文件获取
     * @param request
     * @param response
     * @param upath
     * @throws Exception  
     * void 
     * @exception
     */
    public String fileDownLoad(HttpServletRequest request,  
            HttpServletResponse response,String upath) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        File folder = new File(upath);
        if (!folder.exists()) {//判断该文件夹是否存在，不存在则创建
            folder.mkdirs();
        }
        File localFile = new File(upath+"/import.sql");
        if (!localFile.exists()) {//判断该文件夹是否存在，不存在则创建
            localFile.mkdirs();
        }
        file.transferTo(localFile);
        return upath+"/import.sql";
    }
    
    /**
     * 导入文件替换
     * @param file
     * @param upath  
     * void 
     * @exception
     *//*
    @SuppressWarnings("unused")
	public void fileReplace(String oldPath,String newPath){
        int bytesum = 0;
        int byteread = 0;
        FileInputStream inPutStream = null;
        FileOutputStream outPutStream = null;
        try {
            //判断import.sql文件是否存在,不存在则创建
            File oldFile = new File(oldPath);
            if(!oldFile.exists()){
                oldFile.createNewFile();
            }
            // oldPath的文件copy到新的路径下，如果在新路径下有同名文件，则覆盖源文件
            inPutStream = new FileInputStream(oldPath);
            outPutStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];
            while ((byteread = inPutStream.read(buffer)) != -1) {
                // byte
                bytesum += byteread;
                outPutStream.write(buffer, 0, byteread);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inPutStream != null) {
                    inPutStream.close();
                    inPutStream = null;
                }
                if (outPutStream != null) {
                    outPutStream.close();
                    outPutStream = null;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 文件下载
     * @param request
     * @param response  
     * void 
     * @exception
     */
    @RequestMapping("download")
    public void download(HttpServletRequest request,
            HttpServletResponse response) {
        //下载文件加密参数
        String params = request.getParameter("params");
        Des desObj = new Des();
        //解密后需要下载的具体文件
        String decParams = desObj.strDec(params, SystemContant.DATABASEKEY,
                null, null);
        String[] paramsArr = decParams.split("\\|");
        if (paramsArr != null && paramsArr.length == 2) {
            singleFileDownload(paramsArr[0], paramsArr[1], false, request,
                    response);
        }
    }

    /**
     * 单个文件下载
     * @param downFilePath
     * @param isDelete 下载完成后是否删除原文件
     */
    public void singleFileDownload(String downFilePath, String downFileName,
            boolean isDelete, HttpServletRequest request,
            HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        File downFile = new File(downFilePath);
        if (!downFile.exists() || !downFile.isFile()) {
            return;
        }
        try {
            InputStream fis = new FileInputStream(downFile);
            setFileDownloadHeader(request, response, downFileName);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) != -1) {
                os.write(b, 0, length);
            }
            os.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //若下载后删除文件
            if (isDelete) {
                FileUtil.deleteFile(downFile);
            }
        }
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     * 根据浏览器的不同设置不同的编码格式  防止中文乱码
     * @param fileName 下载后的文件名.
     */
    public void setFileDownloadHeader(HttpServletRequest request,
            HttpServletResponse response, String fileName) {
        try {
            String encodedfileName = null;
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE")) {//IE
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            }
            else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                encodedfileName = new String(fileName.getBytes("UTF-8"),
                        "iso-8859-1");
            }
            else {
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + encodedfileName + "\"");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
}
