/*
 * 描述：Office文档转换工具类
 * 创建人：jian.xiong
 * 创建时间：2016-6-3
 */
package com.yunip.web.common;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.BaseColor;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.FileWaterMarkUtil;
import com.yunip.utils.util.StringUtil;

/**
 * Office文档转换工具类
 * @remark 需要服务端安装OpenOffice
 */
public class OfficeConverter {
    private Logger log = Logger.getLogger(this.getClass());
    
    /**
     * OpenOffice的安装目录
     */
    //private final static String OPENOFFICE_HOME = "C:\\Program Files (x86)\\OpenOffice 4";
    
    /**
     * OpenOffice服务端口号
     */
    //private final static int OPENOFFICE_SERVICE_PORT = 8100;
    
    /**
     * OpenOffice启动命令
     */
    //private final static String START_OPENOFFICE_COMMAND = OPENOFFICE_HOME + File.separator + "program" + File.separator + "soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=" + OPENOFFICE_SERVICE_PORT + ";urp;\" -nofirststartwizard";
    
    /**
     * SWFTools安装目录
     */
    //private final static String SWF_HOME = "C:\\Program Files (x86)\\SWFTools\\pdf2swf.exe";
    
    /**
     * swf转换命令
     */
    //private static String SWF_COMMAND = SWF_HOME + " -t {pdfpath} -o {swfpath} -T 9";
    
    /**
     * 转换文件生成目录(C:\\test\\pdf)
     */
    //private final static String OUTPUT_PATH = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR;
    
    /**
     * 是否copy被转换的副本，在其基础上进行转换
     */
    private final static boolean isCopyOfficeFile = true;
    
    /**
     * pdf文件后缀名
     */
    private final static String PDF_SUFFIX = Constant.PDF_SUFFIX;
    
    /**
     * swf文件后缀名
     */
    private final static String SWF_SUFFIX = Constant.SWF_SUFFIX;
    
    /**
     * office文件(转换前)
     */
    private File officeFile;
    
    /**
     * pdf文件(转换后)
     */
    private File pdfFile;
    
    /**
     * swf文件(flexpaper用于预览)
     */
    private File swfFile;
    
    /**
     * 文件扩展名(带.)
     */
    private String suffix;
    
    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getSwfFile() {
        return swfFile;
    }

    public void setSwfFile(File swfFile) {
        this.swfFile = swfFile;
    }
    
    /**
     * 文件路径
     */
    //private String filePath;

    /**
     * 文件名称(不带后缀名)
     */
    //private String fileName;
    
    
    
    public OfficeConverter(String filePath){
        init(filePath);
    }

    /**
     * 启动OpenOffice服务
     * @throws IOException 
     */
    private static void startOpenOfficeServices() throws IOException{
        //Process pro = Runtime.getRuntime().exec(START_OPENOFFICE_COMMAND);
    }
    
    /**
     * 关闭OpenOffice服务
     * @throws IOException 
     */
    private static void stopOpenOfficeServices(){
        
    }
    
    /** 
     * 初始化
     */
    private void init(String filePath){
        try {
            String OUTPUT_PATH = Constant.ROOTPATH + File.separator + Constant.FILE_PREVIEW_DIR;
            File file = new File(filePath);
            if(file.exists() && file.isFile()){
                String shortFileNameWithoutExt = FileUtil.getShortFileNameWithoutExt(filePath);//文件名(不带扩展名)
                suffix = FileUtil.getFileNameExt(filePath);//文件扩展名(带.)
                //String shortFileName = FileUtil.getShortFileName(filePath);//文件名(带扩展名)
                String shortFileName = shortFileNameWithoutExt + suffix;//文件名(带扩展名)
                
                //创建PDF文件
                this.pdfFile = new File(OUTPUT_PATH + File.separator + shortFileNameWithoutExt + PDF_SUFFIX);
                //创建SWF文件
                this.swfFile = new File(OUTPUT_PATH + File.separator + shortFileNameWithoutExt + SWF_SUFFIX);
                
                //需要转换生成的文件已经存在时，直接返回(swf文件在pdf基础上转换的，转swf之前pdf文件必须存在)
                if(this.pdfFile.exists() || (this.swfFile.exists() && this.pdfFile.exists())){
                    return;
                }
                
                if(isCopyOfficeFile){//判断是否copy一份需要转换的文件的副本，然后在副本的基础上进行文件转换(应对同一份文件同时被多次操作)
                    File copyFile = new File(OUTPUT_PATH + File.separator + shortFileName);
                    if(suffix.equalsIgnoreCase(PDF_SUFFIX)){//是PDF格式时，直接复制创建，后面就无须再转换
                        this.copyFile(file, copyFile);
                    }else{
                        this.officeFile = copyFile;
                        if(!copyFile.exists()){//文件存在则不创建副本（生成了转换文件的也无须创建副本）
                            this.copyFile(file, this.officeFile);
                        }
                    }
                    //this.filePath = this.officeFile.getPath();
                }else{
                    this.officeFile = file;
                    //this.filePath = filePath;
                    if(suffix.equalsIgnoreCase(PDF_SUFFIX)){//是PDF格式时，直接复制，后面就无须再转换
                        this.copyFile(file, this.pdfFile);
                    }
                }
            }else{
                log.error("转换文件不存在！");
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("文件转换初始化异常：" + e.getMessage());
        }
    }
    
    /**
     * 获得源文档转换时使用的格式
     * @param fileExtName 文件扩展名
     */
    private DocumentFormat getDocumentFormat(String fileExtName){
        DocumentFormat sourceFileFormat = null;
        if(fileExtName.equals("wps")){
            sourceFileFormat = new DocumentFormat("Wps Word", DocumentFamily.TEXT, "application/msword", "wps");
        }else if(fileExtName.equals("et")){
            sourceFileFormat = new DocumentFormat("Wps Table", DocumentFamily.TEXT, "application/kset", "et");
        }else if(fileExtName.equals("dps")){
            sourceFileFormat = new DocumentFormat("Wps PowerPoint", DocumentFamily.TEXT, "application/ksdps", "dps");
        }else if(fileExtName.equals("jpg")){
            sourceFileFormat = new DocumentFormat("image/jpeg", DocumentFamily.TEXT, "application/ksdps", "jpg");
        }else if(fileExtName.equals("txt")){
            sourceFileFormat = new DocumentFormat("Plain Text", DocumentFamily.TEXT, "application/vnd.oasis.opendocument.text", "txt");
        }else{
            sourceFileFormat = new DefaultDocumentFormatRegistry().getFormatByFileExtension(fileExtName);
        }
        return sourceFileFormat;
    }
    
    /**
     * office文件转换为pdf文件
     */
    public void officeToPdf() throws Exception{
        if(!pdfFile.exists()){
            if(officeFile.exists()){
                //连接
                OpenOfficeConnection connection = null;
                try {
                    //创建连接
                    int OPENOFFICE_SERVICE_PORT = Integer.parseInt(SysConfigHelper.getValue(SystemContant.OPENCODE, "OPENOFFICE_SERVICE_PORT"));
                    connection = new SocketOpenOfficeConnection(OPENOFFICE_SERVICE_PORT);
                    //连接
                    connection.connect();
                    //开始转换
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                    
                    //获得文件扩展名(去除了.)
                    String fileExtName = suffix;
                    if(!StringUtil.nullOrBlank(fileExtName) && fileExtName.indexOf(".") != -1){
                        fileExtName = fileExtName.substring(1, fileExtName.length()).toLowerCase();
                    }
                    DocumentFormat sourceFileFormat = getDocumentFormat(fileExtName);
                    DocumentFormat pdfFileFormat = new DefaultDocumentFormatRegistry().getFormatByFileExtension("pdf");
                    converter.convert(officeFile, sourceFileFormat, pdfFile, pdfFileFormat);
                    //关闭连接
                    connection.disconnect();
                    //converter.convert(officeFile, pdfFile);
                }catch (ConnectException e) {
                    e.printStackTrace();
                    log.error("***pdf转换异常，openoffice服务未启动！***");
                    throw e;
                }catch (OpenOfficeException e) {
                    e.printStackTrace();
                    log.error("***pdf转换异常，读取转换文件失败***");
                    log.error("***文件：***" + officeFile.getName() + "，路径" + officeFile.getPath());
                    throw e;
                }catch (Exception e) {
                    e.printStackTrace();
                    log.error("***pdf转换异常：***" + e.getMessage());
                    log.error("***文件：***" + officeFile.getName() + "，路径" + officeFile.getPath());
                    throw e;
                }finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }else{
                log.info("****pdf转换：需要转换的office文件不存在，无法转换****");
            }
        }else{
            log.info("****pdf转换：已转换为pdf，无须转换****");
        }
    }
    
    static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        //把InputStream字节流 替换为BufferedReader字符流 2013-07-17修改
        //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder buffer = new StringBuilder();
        while ((ptr = reader.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
    
    /**
     * pdf文件转换为swf文件
     */
    public void pdfToSwf() throws Exception{
        if(!swfFile.exists()){
            if(pdfFile.exists()){
                pdfAddWaterMark();//转换前添加给pdf增加水印
                System.out.println("转换源文件：" + pdfFile.getPath());
                System.out.println("转换目标文件：" + swfFile.getPath());
                String SWF_COMMAND = SysConfigHelper.getValue(SystemContant.OPENCODE, "SWF_HOME") + " -t " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9 -qq";
                //Process p = Runtime.getRuntime().exec(SWF_COMMAND.replace("{pdfpath}", pdfFile.getPath()).replace("{swfpath}", swfFile.getPath()));
                Process p = Runtime.getRuntime().exec(SWF_COMMAND);
                if(p.waitFor() != 0){//普通转换出现错误采用第二种转换命令(原因：第一种转换命令速度稍快，到对于文件大太或者文件图形过多会出现异常。第二种转换命令可以解决，但处理效率稍低)
                    SWF_COMMAND += " -G -s poly2bitmap ";
                    Process p1 = Runtime.getRuntime().exec(SWF_COMMAND);
                    p1.waitFor();
                }
                System.out.print(loadStream(p.getInputStream()));
                if (isCopyOfficeFile && officeFile != null && officeFile.exists()) {
                    officeFile.delete();
                }
                System.out.println("==============");
            }else{
                log.info("****swf转换：需要转换pdf的文件不存在，无法转换****");
            }
        }else{
            log.info("****swf转换：已转换为swf，无须转换****");
        }
    }
    
    /**
     * 复制文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    private void copyFile(File sourceFile,File targetFile)throws Exception{
        if(!targetFile.getParentFile().exists()){//目录不存在则创建
            new File(targetFile.getParentFile().getPath()).mkdirs();
        }
        //新建文件输入流并对它进行缓冲 
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff  = new BufferedOutputStream(output);
        // 缓冲数组 
        byte[] b = new byte[1024 * 5];
        int len;
        while((len = inBuff.read(b)) != -1){
            outBuff.write(b,0,len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();
        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
    
    /**
     * pdf文件增加水印
     */
    public void pdfAddWaterMark() throws Exception{
        if(pdfFile.exists()){
            log.info("***pdf文件增加水印：pdf添加水印开始工作***");
            boolean isOpenWaterMark = false;
            String isOpenWaterMarkStr = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.IS_WATER_MARK.getKey());
            if(!StringUtil.nullOrBlank(isOpenWaterMarkStr)){
                isOpenWaterMark = Boolean.valueOf(isOpenWaterMarkStr);
            }
            //给文件增加水印
            if(isOpenWaterMark){
                String newPdfFilePath = pdfFile.getParentFile().getPath() + File.separator + "waterMark-" + pdfFile.getName();
                try {
                    String waterMarkText = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.WATER_MARK_TEXT.getKey());//水印文字内容
                    String textColorStr = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.TEXT_COLOR.getKey());//水印文字颜色
                    BaseColor textColor = BaseColor.RED;//水印文字颜色
                    if(!StringUtil.nullOrBlank(textColorStr)){
                        textColor = new BaseColor(Color.decode(textColorStr).getAlpha());
                    }
                    int align = 0, fontSize = 0, rotation = 0;
                    String alignStr = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ALIGN.getKey());//对齐方式
                    if(!StringUtil.nullOrBlank(alignStr)){
                        align = Integer.parseInt(alignStr);
                    }
                    String fontSizeStr = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.FONT_SIZE.getKey());//水印文字大小
                    if(!StringUtil.nullOrBlank(fontSizeStr)){
                        fontSize = Integer.parseInt(fontSizeStr);
                    }
                    String rotationStr = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.ROTATION.getKey());//水印文字倾斜（旋转）角度
                    if(!StringUtil.nullOrBlank(rotationStr)){
                        rotation = Integer.parseInt(rotationStr);
                    }
                    FileWaterMarkUtil.pdfAddWaterMark(pdfFile.getPath(), newPdfFilePath, waterMarkText, textColor, align, fontSize, rotation);
                    pdfFile.delete();
                    File newPdfFile = new File(newPdfFilePath);
                    newPdfFile.renameTo(pdfFile);
                    log.info("***pdf文件增加水印：pdf添加水印结束***");
                }catch (Exception e) {
                    log.error("***pdf文件增加水印：pdf文件加水印失败***");
                    //增加水印失败，则删除该文件
                    File newPdfFile = new File(newPdfFilePath);
                    if(newPdfFile.exists()){
                        newPdfFile.delete();
                    }
                    throw e;
                }
            }
        }else{
            log.info("***pdf文件增加水印：原pdf文件不存在，无法添加水印***");
        }
    }
    
    /*
     * 转换主方法
     */
    public boolean converter() throws Exception{
        if (swfFile.exists()) {
            log.info("***swf转换器开始工作，该文件已经转换为swf***");
            return true;
        }

        try {
            officeToPdf();
            pdfToSwf();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("***预览转换异常：***" + officeFile.getName() + "，路径" + officeFile.getPath() + "，错误：" + e.getMessage());
            //转换失败，则删除原文件
            if (isCopyOfficeFile && officeFile.exists()) {
                officeFile.delete();
            }
            if(pdfFile.exists()){
                pdfFile.delete();
            }
            if(swfFile.exists()){
                swfFile.delete();
            }
            throw e;
        }

        if (swfFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /** 
     * main(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param args  
     * void 
     * @exception  
    */
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        /*list.add("C:\\test\\doc格式.doc");
        list.add("C:\\test\\docx格式.docx");
        list.add("C:\\test\\xls格式.xls");
        list.add("C:\\test\\xlsx格式.xlsx");*/
        //list.add("C:\\test\\ppt格式.ppt");
        /*list.add("C:\\test\\pptx格式.pptx");
        list.add("C:\\test\\TXT文本格式.txt");
        list.add("C:\\test\\wps_word格式.wps");
        list.add("C:\\test\\wps_excel格式.et");
        list.add("C:\\test\\wps_ppt格式.dps");
        list.add("C:\\test\\pdf图片格式.pdf");*/
        try {
            for(String str : list){
                OfficeConverter off = new OfficeConverter(str);
                off.converter();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
