/*
 * 描述：〈授权处理类〉
 * 创建人：Administrator
 * 创建时间：2016-7-5
 */
package com.yunip.web.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.disk.DiskException;
import com.yunip.mapper.config.ISysConfigDao;
import com.yunip.model.config.SysConfig;
import com.yunip.util.SpringContextUtil;
import com.yunip.utils.http.HttpUtil;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.SystemUtil;

/**
 * 授权处理类
 */
public class License {
    
    /***可以用小时数***/
    public static int hour;
    
    /***系统的网卡信息***/
    public static String macCode;
    
    public static Des des = new Des();

    
  //4C641C025442FBD07FEC10C051A8CD6F830CED4CE133A38F12E80820ED493E64
    
    /**
     * @throws Exception **
     * 根据用户提供的序列号生成授权文件
     * @param serCode  
     * void 
     * @exception
     */
    public static void generateCode(HttpServletRequest request ,String serCode) throws Exception{
        //serCode现为“授权时间码#允许使用员工数秘钥”，需划分
        if(serCode != null && serCode.contains("#")){
            String[] strArr = serCode.split("#");
            serCode = strArr[0];//授权码
            String numKey = strArr[1];//员工数秘钥
            //秘钥添加
            ISysConfigDao mapper = SpringContextUtil.getBean("iSysConfigDao");
            SysConfig sysConfig = new SysConfig();
            sysConfig.setConfigCode(SystemContant.BASICSCODE);
            sysConfig.setConfigKey(SystemContant.REGISTER_RESTRICTIONS_KEY);
            sysConfig.setConfigValue(numKey);
            mapper.update(sysConfig);
            //通知全局
            SysConfigHelper.reload();//disk云盘
            HttpUtil.post(SystemContant.ADMIN_SERVICE_URL + SystemContant.RELOAD_PATH, "", "", "UTF-8");//admin后台
           /* //文件服务器
            HttpUtil.post(
                    SysConfigHelper.getValue(SystemContant.SYSTEMCODE,
                            SystemContant.SYSTEM_FILE_DOMAIN)
                            + SystemContant.RELOAD_PATH, "", "", "UTF-8");//file云盘
*/        }
        // ----------------------------原流程------------------------------
        macCode = SystemUtil.getConfig();
        String filePath = request.getSession().getServletContext().getRealPath("/") + File.separator + SystemContant.LICENSEFILENAME;
        //整理流程 
        //1.序列号客户端生成mac的MD5加密 生成字符串Z1
        //2.序列号服务端用 Z1 + 密钥KEY 组成新的 密钥字符串 Z2
        //2.序列号服务端用YYYYMMDD+HOURS(系统有效小时数) 并且用Z2为密钥进行DES加密  生成Z3
        //3.客户端输入Z3
        //String sercode = "4C641C025442FBD07FEC10C051A8CD6F830CED4CE133A38F12E80820ED493E64";
        String clientSign = Md5.encodeByMd5(macCode) + SystemContant.LICENSECODEKEY;
        String hours = des.strDec(serCode, clientSign, null, null);
        if(StringUtils.isBlank(hours) || !StringUtils.isNumeric(hours)){
           //非法授权
           throw new MyException(DiskException.FFSQ);
        }
        //用新的签名对该数据进行加密
        String signHours = des.strEnc(hours, Md5.encodeByMd5(macCode), null, null);
        //先读取
        String linceseCode = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String str;
            while(null != (str=br.readLine())){
                linceseCode += str;
            }
            br.close();
            linceseCode = des.strDec(linceseCode, SystemContant.LICENSECODEKEY2, null, null);
        } catch (Exception e) {
            //系统非法操作(用户擅自改动配置)
            throw new MyException(DiskException.FFSQ);
        }
        File file = new File(filePath);
        if(!linceseCode.equals(SystemContant.LICENSECODEKEY1)){
            //证明已经使用过授权码
            //1.验证授权码是否合法
            if(StringUtils.isBlank(hours) || !StringUtils.isNumeric(hours)){
                //非法授权
                throw new MyException(DiskException.FFSQ);
            }
            if(linceseCode.contains(signHours)){
                //非法授权
                throw new MyException(DiskException.SQMYJSYG);
            }
        }
        hour = Integer.parseInt(hours.substring(8));
        StringBuffer signBuf = new StringBuffer();
        //加两次
        signBuf.append(linceseCode);
        signBuf.append("|");
        signBuf.append(signHours);
        signBuf.append("|");
        signBuf.append(signHours);
        //再次加密
        String desc = des.strEnc(signBuf.toString(), SystemContant.LICENSECODEKEY2, null, null);
        //合法通过的情况下，替换掉以前的授权码使用新授权时间计算
        FileWriter fileWritter = new FileWriter(file);
        fileWritter.write(desc);
        fileWritter.close();
        //开始设置值
        //SyncLicense.minusLicenseHour(request);
        // -----------------------------结束-----------------------------
    }
    
    /***
     * 验证是否授权码是否正常
     * @throws Exception  
     * void 
     * @exception
     */
    public static DiskException checkLicenseCode() throws Exception{
        if(hour <= 0){
            return DiskException.SQCS;
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        //System.out.println("2016071832222".substring(0,8));
        
        
    }
}
