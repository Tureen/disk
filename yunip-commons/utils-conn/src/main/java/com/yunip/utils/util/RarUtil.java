/*
 * 描述：zip压缩工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-14
 */
package com.yunip.utils.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

/**
 * zip压缩工具类
 */
public class RarUtil {

    private static Logger             logger    = Logger.getLogger(RarUtil.class);

    /**任务名称**/
    public static String              TASKNAME  = "";

    /**取消任务的结果查询链表**/
    public static Map<String, Object> CANCELMAP = new LinkedHashMap<String, Object>();

    /** 
     * 禁止实例化
     */
    private RarUtil() {}

    /** 压缩文件后缀 **/
    public static final String EXT = ".rar";

    private static Archive a;

    /** 
     * 根据原始rar路径，解压到指定文件夹下.      
     * @param srcRarPath 原始rar路径 
     * @param dstDirectoryPath 解压到的文件夹      
     */
    @SuppressWarnings("resource")
    public static void unRarFile(String srcRarPath, String dstDirectoryPath)
            throws Exception {
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        int index = 0;
        a = new Archive(new File(srcRarPath));
        if (a != null) {
            a.getMainHeader().print(); // 打印文件信息.
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                //进行取消任务
                if (CANCELMAP.containsKey(TASKNAME)) {
                    logger.info("cancel the decopress task，the task name is "
                            + TASKNAME);
                    throw new Exception(TASKNAME);
                }
                //防止文件名中文乱码问题的处理
                String fileName = fh.getFileNameW().isEmpty() ? fh.getFileNameString() : fh.getFileNameW();
                if (fh.isDirectory()) {
                    // 文件夹 
                    File fol = new File(dstDirectoryPath + File.separator+ fileName);
                    fol.mkdirs();
                } else {
                    // 文件
                    File out = new File(dstDirectoryPath + File.separator
                            + fileName.trim());
                    if (!out.exists()) {
                        if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录. 
                            out.getParentFile().mkdirs();
                        }
                        out.createNewFile();
                    }
                    System.out.println(fileName.trim());
                    System.out.println(index++);
                    System.out.println(out.length());
                    FileOutputStream os = new FileOutputStream(out);
                    a.extractFile(fh, os);
                    os.close();
                }
                fh = a.nextFileHeader();
            }
            a.close();
        }
    }
    
    /** 
     * 获取压缩文件内部文件数量   
     * @param srcRarPath 原始rar路径 
     */
    public static int getRarSize(String srcRarPath) throws Exception {
        a = new Archive(new File(srcRarPath));
        return a.getFileHeaders().size();
    }
    
}
