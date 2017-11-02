/*
 * 描述：文件操作工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

/**
 * 文件操作工具类
 */
public final class FileUtil {

    private static String ENCOD_CODE = "GBK";
    private static WordExtractor extractor;
    private static XWPFWordExtractor docx;

    /**
     * 构造方法，禁止实例化
     */
    private FileUtil() {}

    /**
     * 复制文件. <br>
     * <br>
     * <b>示例: </b>
     * 
     * <pre>
     * FileUtils.copyFile(&quot;/home/app/config.xml&quot;, &quot;/home/appbak/config_bak.xml&quot;)
     * </pre>
     * 
     * @param fromFile 源文件，包括完整的绝对路径和文件名
     * @param toFile 目标文件，包括完整的绝对路径和文件名，目标路径必须已经存在，该方法不负责创建新的目录
     * @throws IOException 抛出IOException
     */
    public static void copyFile(String fromFile, String toFile)
            throws IOException {
        FileInputStream in = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int len;
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
        in.close();
    }

    /**
     * 得到短文件名. <br>
     * <br>
     * <b>示例: </b> <br>
     * FileUtils.getShortFileName(&quot;/home/app/config.xml&quot;) 返回
     * &quot;config.xml&quot;
     * FileUtils.getShortFileName(&quot;C:\\test\\config.xml&quot;) 返回
     * &quot;config.xml&quot;</br>
     * 
     * @param fileName 文件名
     * @return 短文件名
     */
    public static String getShortFileName(String fileName) {
        String shortFileName = "";
        int pos = fileName.lastIndexOf('\\');
        if (pos == -1) {
            pos = fileName.lastIndexOf('/');
        }
        if (pos > -1) {
            shortFileName = fileName.substring(pos + 1);
        }
        else {
            shortFileName = fileName;
        }
        return shortFileName;
    }

    /**
     * 得到不带扩展名的短文件名. <br>
     * <br>
     * <b>示例: </b> <br>
     * FileUtils.getShortFileNameWithoutExt(&quot;/home/app/config.xml&quot;) 返回
     * &quot;config&quot;<br>
     * FileUtils.getShortFileNameWithoutExt(&quot;C:\\test\\config.xml&quot;) 返回
     * &quot;config&quot;</br>
     * 
     * @param fileName 文件名
     * @return 短文件名
     */
    public static String getShortFileNameWithoutExt(String fileName) {
        String shortFileName = getShortFileName(fileName);
        shortFileName = getFileNameWithoutExt(shortFileName);
        return shortFileName;
    }

    /**
     * 得到文件内容
     * 
     * @param fileName 文件名称
     * @return 文件内容
     * @throws Exception
     */
    public static String read(String fileName) throws Exception {
        String fileContent = "";
        fileContent = read(new FileInputStream(fileName));
        return fileContent;
    }

    /**
     * 得到文件内容
     * 
     * @param fileName 文件名称
     * @return 文件内容
     * @throws Exception
     */
    public static String readTxt(String fileName) throws Exception {
        String fileContent = "";
        File sourceFile = new File(fileName);
        String encode = getFilecharset(sourceFile);
        fileContent = readTxt(new FileInputStream(fileName), encode);
        return fileContent;
    }

    /**
     * 得到文件内容
     * 
     * @param file 文件
     * @return 文件内容
     * @throws Exception
     */
    public static String read(File file) throws Exception {
        String fileContent = "";
        fileContent = read(new FileInputStream(file));
        return fileContent;
    }

    /**
     * 得到输入流的内容
     * 
     * @param is 输入流
     * @return 字符串
     * @throws Exception
     */
    public static String read(InputStream is) throws Exception {
        byte[] result = readBytes(is);
        return new String(result);
    }

    /**
     * 得到输入流的内容
     * 
     * @param is 输入流
     * @param encode 编码格式
     * @return 字符串
     * @throws Exception
     */
    public static String readTxt(InputStream is, String encode)
            throws Exception {
        byte[] result = readBytes(is);
        return new String(result, encode);
    }

    /**
     * 以byte数组方式得到输入流的内容
     * 
     * @param fileName 文件名称
     * @return byte数组
     * @throws Exception
     */
    public static byte[] readBytes(String fileName) throws Exception {
        return readBytes(new FileInputStream(fileName));
    }

    /**
     * 以byte数组方式得到输入流的内容
     * 
     * @param file 文件
     * @return byte数组
     * @throws Exception
     */
    public static byte[] readBytes(File file) throws Exception {
        return readBytes(new FileInputStream(file));
    }

    /**
     * 以byte数组方式得到输入流的内容
     * 
     * @param is 输入流
     * @return byte数组
     * @throws Exception
     */
    public static byte[] readBytes(InputStream is) throws Exception {
        if (is == null || is.available() < 1) {
            return new byte[0];
        }
        byte[] buff = new byte[8192];
        byte[] result = new byte[is.available()];
        int nch;
        BufferedInputStream in = new BufferedInputStream(is);
        int pos = 0;
        while ((nch = in.read(buff, 0, buff.length)) != -1) {
            System.arraycopy(buff, 0, result, pos, nch);
            pos += nch;
        }
        in.close();
        return result;
    }

    /**
     * 写文件
     * 
     * @param content 文件内容
     * @param file 文件对象
     * @throws Exception 
     * @throws IOException
     */
    public static void write(String content, File file) throws Exception {
        write(content.getBytes(ENCOD_CODE), file);
    }

    /**
     * 写文件
     * 
     * @param content 文件内容
     * @param file 文件名
     * @throws Exception 
     * @throws IOException
     */
    public static void write(String content, String file) throws Exception {
        write(content, new File(file));
    }

    /**
     * 写文件
     * 
     * @param bytes 文件内容
     * @param file 文件名
     * @throws IOException
     */
    public static void write(byte[] bytes, String file) {
        write(bytes, new File(file));
    }

    /**
     * 写文件
     * 
     * @param bytes 文件内容
     * @param file 文件
     * @throws IOException
     */
    public static void write(byte[] bytes, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回不带扩展名的文件名
     * 
     * @param fileName 原始文件名
     * @return 不带扩展名的文件名
     */
    public static String getFileNameWithoutExt(String fileName) {
        String shortFileName = fileName;
        if (fileName.indexOf('.') > -1) {
            shortFileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return shortFileName;
    }

    /**
     * 返回文件扩展名,带“.”
     * 
     * @param fileName 原始文件名
     * @return 文件扩展名
     */
    public static String getFileNameExt(String fileName) {
        String fileExt = "";
        if (fileName.indexOf('.') > -1) {
            fileExt = fileName.substring(fileName.lastIndexOf('.'));
        }
        return fileExt;
    }
    
    /**
     * 返回文件扩展名,不带带“.”
     * 
     * @param fileName 原始文件名
     * @return 文件扩展名
     */
    public static String getFileNameSuffix(String fileName) {
        String fileExt = "";
        if (fileName.indexOf('.') > -1) {
            fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);
        }
        return fileExt;
    }

    /**
     * 得到唯一文件
     * 
     * @param fileName 原始文件名
     * @return
     */
    public static synchronized File getUniqueFile(File repository,
            String fileName) {
        String shortFileName = getShortFileName(fileName);
        String tempFileName = getFileNameWithoutExt(shortFileName);
        File file = new File(repository, shortFileName);
        String fileExt = getFileNameExt(shortFileName);
        while (file.exists()) {
            file = new File(repository, tempFileName + "-"
                    + Math.abs(new Random().nextInt()) + fileExt);
        }
        return file;
    }

    /**
     * 删除文件方法，如果删除不掉，将该文件加入删除池，下次进行调用时将尝试删除池中的文件
     * @param fileName fileName
     */
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    /**
     * 删除文件方法，如果删除不掉，将该文件加入删除池，下次进行调用时将尝试删除池中的文件
     * @param file file
     */
    public static void deleteFile(File file) {
        // 尝试删除文件
        file.delete();
    }

    /**
     * 检查池，删除池中文件，如果删除成功则同时从池中移除。
     */
    /*
    private static void checkDeletePool() {
        File file;
        for (int i = deleteFilesPool.size() - 1; i >= 0; i--) {
            file = (File) deleteFilesPool.get(i);
            file.delete();
            if (file.exists() == false) {
                deleteFilesPool.remove(i);
            }
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(POOL_FILE));
            out.writeObject(deleteFilesPool);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * 得到唯一文件。一个类处在某个位置的class或jar包中，根据此位置得到此类对应的文件。<br>
     * 不同位置的类得到的文件是不一样的。
     * @param cl 类
     * @param extension 带点的文件扩展名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File getUniqueFile(Class cl, String extension) {
        int key = 0;
        //"cl.getResource"取得类的classpath
        URL url = cl.getResource(getClassNameWithoutPackage(cl) + ".class");
        if (url != null) {
            key = url.getPath().hashCode();
        }
        /**
         * 生成新文件，“System.getProperty("java.io.tmpdir")”为路径
         * “getClassNameWithoutPackage(cl) + key + extension”为文件名
         */
        File propFile = new File(System.getProperty("java.io.tmpdir"),
                getClassNameWithoutPackage(cl) + key + extension);
        return propFile;
    }

    @SuppressWarnings("rawtypes")
    private static String getClassNameWithoutPackage(Class cl) {
        String className = cl.getName();
        int pos = className.lastIndexOf('.') + 1;
        if (pos == -1) {
            pos = 0;
        }
        String name = className.substring(pos);
        return name;
    }

    /**
     * 删除文件夹（不管是否文件夹为空）<br>
     * 注意：非原子操作，删除文件夹失败时，并不能保证没有文件被删除。 * <br>
     * <b>示例: </b> <br>
     * FileUtils.deleteFolder(&quot;/home/tmp&quot;) 删除成功返回true.<br>
     * FileUtils.deleteFolder(&quot;C:\\test&quot;) 删除成功返回true.</br>
     * @param delFolder 待删除的文件夹
     * @return 如果删除成功则返回true，否则返回false
     */
    public static boolean deleteFolder(File delFolder) {
        // 目录是否已删除
        boolean hasDeleted = true;
        // 得到该文件夹下的所有文件夹和文件数组
        File[] allFiles = delFolder.listFiles();

        for (int i = 0; i < allFiles.length; i++) {
            // 为true时操作
            if (hasDeleted) {
                if (allFiles[i].isDirectory()) {
                    // 如果为文件夹,则递归调用删除文件夹的方法
                    hasDeleted = deleteFolder(allFiles[i]);
                }
                else if (allFiles[i].isFile()) {
                    try {// 删除文件
                        if (!allFiles[i].delete()) {
                            // 删除失败,返回false
                            hasDeleted = false;
                        }
                    }
                    catch (Exception e) {
                        // 异常,返回false
                        hasDeleted = false;
                    }
                }
            }
            else {
                // 为false,跳出循环
                break;
            }
        }
        if (hasDeleted) {
            // 该文件夹已为空文件夹,删除它
            delFolder.delete();
        }
        return hasDeleted;
    }

    /**
     * 得到Java类所在的实际位置。一个类处在某个位置的class或jar包中，根据此位置得到此类对应的文件。<br>
     * 不同位置的类得到的文件是不一样的。
     * @param cl 类
     * @return 类在系统中的实际文件名
     */
    @SuppressWarnings("rawtypes")
    public static String getRealPathName(Class cl) {
        URL url = cl.getResource(getClassNameWithoutPackage(cl) + ".class");
        if (url != null) {
            return url.getPath();
        }
        return null;
    }

    /***
     * 递归获取指定目录下的所有的文件（不包括文件夹）
     * @param dirPath 目录
     */
    public static ArrayList<File> getDirAllFiles(String dirPath) {
        File dir = new File(dirPath);
        ArrayList<File> files = new ArrayList<File>();
        if (dir.isDirectory()) {
            File[] fileArr = dir.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File f = fileArr[i];
                if (f.isFile()) {
                    files.add(f);
                }
                else {
                    files.addAll(getDirAllFiles(f.getPath()));
                }
            }
        }
        return files;
    }

    /**
     * 获取指定目录下的所有文件(不包括子文件夹)
     * @param dirPath 目录
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath) {
        File dir = new File(dirPath);
        ArrayList<File> files = new ArrayList<File>();
        if (dir.isDirectory()) {
            File[] fileArr = dir.listFiles();
            for (File f : fileArr) {
                if (f.isFile()) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    /**
     * 获取指定目录下的所有目录(不包括子文件夹)
     * @param dirPath 目录
     * @return
     */
    public static ArrayList<File> getDirSubDirs(String dirPath) {
        File dir = new File(dirPath);
        ArrayList<File> files = new ArrayList<File>();
        if (dir.isDirectory()) {
            File[] fileArr = dir.listFiles();
            for (File f : fileArr) {
                if (f.isDirectory()) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    /**
     * 获得文件的大小
     * @param filePath 文件路径
     * @return 返回当前文件的大小(单位：字节数)
     */
    public static long getFileSize(String filePath) {
        long size = 0L;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取指定目录下的文件数量(不包括子文件夹)
     * @param dirPath 目录
     * @return
     */
    public static int getDirFileCount(String dirPath) {
        int count = 0;
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            count = dir.listFiles().length;
        }
        return count;
    }

    /**
     * 复制文件和目录结构
     * @param fromFile 源文件，包括完整的绝对路径和文件名
     * @param toFile 目标文件，包括完整的绝对路径和文件名
     */
    public static void copyFileAndDir(String fromFile, String toFile)
            throws IOException {
        String fileName = getShortFileName(toFile);
        String fileDirPath = toFile.substring(0, toFile.indexOf(fileName));
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        copyFile(fromFile, toFile);
    }

    /**
     * 获得当前文件的父目录路径
     */
    public static String getFileParentPath(String fileName) {
        String path = "";
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            path = file.getParent();
        }
        return path;
    }

    @SuppressWarnings({ "unused", "resource" })
    private static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            }
            else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            }
            else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            }
            else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else break;
                    }
                    else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            }
                            else break;
                        }
                        else break;
                    }
                }
            }
            bis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    public static String readDoc(String fileName) throws Exception {
        // 创建输入流读取DOC文件
        FileInputStream in = new FileInputStream(new File(fileName));
        String text = null;
         if(fileName.endsWith(".doc") || fileName.endsWith(".wps")){
            extractor = new WordExtractor(in);
            //对DOC文件进行提取
            text = extractor.getText();
        }else if(fileName.endsWith(".docx")){
            docx = new XWPFWordExtractor(POIXMLDocument.openPackage(fileName));
            text =docx.getText();
        } else if(fileName.endsWith(".pdf")) {
            PDFParser pdf = new PDFParser(in);
            pdf.parse();
            PDDocument pdd = pdf.getPDDocument(); 
            PDFTextStripper ts = new PDFTextStripper();     
            text = ts.getText(pdd);     
            pdd.close();     
        } else if(fileName.endsWith(".ppt") || fileName.endsWith(".pptx") || fileName.endsWith(".dps")){
            text =PptReader.getTextFromPPT(new File(fileName));
        } else {
            text = readTxt(fileName);
        }
        in.close();
        return text;
    }
    
    /**
     * 删除父目录的父目录为temp的目录
     * @param filePath 文件路径
     */
    public static void deleteFileTempFolder(String filePath){
        if(!StringUtil.nullOrBlank(filePath)){
            File file = new File(filePath);
            String parentFolderName = file.getParentFile().getParentFile().getName();
            if(parentFolderName.equals("temp")){
                FileUtil.deleteFolder(file.getParentFile());
            }
        }
    }
    /** 
     * 处理excel2003 
     * @param path 
     * @return 
     * @throws IOException 
     */  
    @SuppressWarnings("resource")
    public static String readExcel(String path) throws IOException {  
		InputStream inputStream = null;
		String content = null;
		try {
			inputStream = new FileInputStream(path);
			HSSFWorkbook wb = new HSSFWorkbook(inputStream);
			ExcelExtractor extractor = new ExcelExtractor(wb);
			extractor.setFormulasNotResults(true);
			extractor.setIncludeSheetNames(false);
			content = extractor.getText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return content;
    }  
    /** 
     * 处理excel2007 
     * @param path 
     * @return 
     * @throws IOException 
     */  
    @SuppressWarnings("deprecation")
    public static String readExcel2007(String path) throws IOException {  
		StringBuffer content = new StringBuffer();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(path);
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < xwb.getNumberOfSheets(); numSheet++) {
			XSSFSheet xSheet = xwb.getSheetAt(numSheet);
			if (xSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 0; rowNum <= xSheet.getLastRowNum(); rowNum++) {
				XSSFRow xRow = xSheet.getRow(rowNum);
				if (xRow == null) {
					continue;
				}
				// 循环列Cell
				for (int cellNum = 0; cellNum <= xRow.getLastCellNum(); cellNum++) {
					XSSFCell xCell = xRow.getCell(cellNum);
					if (xCell == null) {
						continue;
					}
					if (xCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
						content.append(xCell.getBooleanCellValue());
					} else if (xCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						content.append(xCell.getNumericCellValue());
					} else {
						content.append(xCell.getStringCellValue());
					}
				}
			}
		}

		return content.toString();
    }  

    public static void main(String[] args) throws Exception {
        //System.out.println(FileUtil.getDirFileCount("C:\\uploadDir\\temp\\9b8bd717126e1a8f3b873d8fcf982052"));
//System.out.println(FileUtil.getShortFileName("C:\\C\\1.TXT"));
        //System.out.println(FileUtil.getShortFileNameWithoutExt("C:\\C\\1.TXT"));
        //System.out.println(FileUtil.getFileNameExt("C:\\C\\1.TXT"));
        //File sourceFile = new File("C:\\Users\\Administrator\\Desktop\\新建文本文档 (7).txt"); 
     //String a = FileUtil.readDoc("C:\\Users\\Administrator\\Desktop\\新建文件夹\\贵州凯里智能停车收费管理系统方案（2015-05-13）.docx");
     //String a = FileUtil.readDoc();
    // String text =docx.getText();
        //FileInputStream in = new FileInputStream(new File(""));
       // extractor = new WordExtractor(in);
       String text = readDoc("C:\\Users\\Administrator\\Desktop\\20160401.pdf");
        
        //对DOC文件进行提取
       /* ClassLoader classloader =   
                org.apache.poi.poifs.filesystem.POIFSFileSystem.class.getClassLoader();   
             URL res = classloader.getResource(   
            "org/apache/poi/poifs/filesystem/POIFSFileSystem.class");   
             String path = res.getPath();   
             System.out.println("Core POI came from " + path);*/
        System.out.println(text);
    }
    
}
