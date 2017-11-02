/*
 * 描述：与文件服务器进行交互的操作工具类
 * 创建人：jian.xiong
 * 创建时间：2016-5-27
 */
package com.yunip.manage;

import org.apache.log4j.Logger;

import com.yunip.constant.SystemContant;
import com.yunip.enums.fileservers.EnumFileServiceOperateType;
import com.yunip.model.fileserver.DownloadFileEntity;
import com.yunip.model.fileserver.FileEntity;
import com.yunip.model.fileserver.FileOperateRequest;
import com.yunip.model.fileserver.PreviewFileEntity;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.StringUtil;

/**
 * 与文件服务器进行交互的操作工具类
 */
public class FileServiceOperateManagerUtil {
    private static Logger logger = Logger.getLogger(FileServiceOperateManagerUtil.class);

    /**
     * 调用接口时，生成加密校验值
     */
    public static String getChkValue(FileOperateRequest<?> bean) {
        String chkValue = "";
        if(bean.getCmdId().equals(EnumFileServiceOperateType.DELETE_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof FileEntity){
                        FileEntity file = (FileEntity) obj;
                        chkValue += file.getFileId();
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.DOWNLOAD_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof DownloadFileEntity){
                        DownloadFileEntity file = (DownloadFileEntity) obj;
                        if(file.getFiles() != null && file.getFiles().size() > 0){
                            chkValue += StringUtil.list2series(file.getFiles(), ",");
                        }
                        if(file.getFolders() != null && file.getFolders().size() > 0){
                            chkValue += StringUtil.list2series(file.getFolders(), ",");
                        }
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.TAKECODE_DOWNLOAD_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof DownloadFileEntity){
                        DownloadFileEntity file = (DownloadFileEntity) obj;
                        if(file.getFiles() != null && file.getFiles().size() > 0){
                            chkValue += StringUtil.list2series(file.getFiles(), ",");
                        }
                        if(file.getFolders() != null && file.getFolders().size() > 0){
                            chkValue += StringUtil.list2series(file.getFolders(), ",");
                        }
                        if(!StringUtil.nullOrBlank(file.getTakeCode())){
                            chkValue += file.getTakeCode();
                        }
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.TAKECODE_PREVIEW_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof PreviewFileEntity){
                        PreviewFileEntity file = (PreviewFileEntity) obj;
                        if(StringUtil.nullOrBlank(file.getFileId())){
                            chkValue += file.getFileId();
                        }
                        if(!StringUtil.nullOrBlank(file.getTakeCode())){
                            chkValue += file.getTakeCode();
                        }
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.ONLINE_EDIT_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof String){
                        String fileId = (String) obj;
                        chkValue += fileId;
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.BATCH_READER_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof String){
                        String fileId = (String) obj;
                        chkValue += fileId;
                    }
                }
            }
        }else if(bean.getCmdId().equals(EnumFileServiceOperateType.BATCH_READER_TEAMWORK_FILE.getCmdId())){
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
            if(bean.getParams() != null){
                for(Object obj : bean.getParams()){
                    if(obj instanceof String){
                        String fileId = (String) obj;
                        chkValue += fileId;
                    }
                }
            }
        }else{
            chkValue = bean.getCmdId() + bean.getOperateId() + bean.getOperateTime();
        }
        try {
            logger.info("加密前：" + chkValue);
            chkValue = Md5.encodeByMd5(chkValue + SystemContant.md5Key);
            logger.info("加密后：" + chkValue);
        }catch (Exception e) {
            chkValue = "";
            e.printStackTrace();
            logger.error("调用文件接口时，生成加密校验值时错误：" + e.getMessage());
        }
        return chkValue;
    }

}
