package com.yunip.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public class PptReader {

    private static XSLFPowerPointExtractor ppt;

    public static String getTextFromPPT(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.equalsIgnoreCase(".pptx", FileUtil.getFileNameExt(file.getName()))) {// 2007 2010
            XMLSlideShow xmlslideshow = new XMLSlideShow(new FileInputStream(file));
            ppt = new XSLFPowerPointExtractor(xmlslideshow);
            sb.append(ppt.getText());
        }  else {
            // 2003 HSSFWorkbook
            FileInputStream is = null;  
            PowerPointExtractor extractor = null;  
            try {  
                is = new FileInputStream(file);  
                extractor = new PowerPointExtractor(is);  
                sb.append(extractor.getText());  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {
                extractor.close();
                is.close();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException,
            IOException {
        String ppt = getTextFromPPT(new File(
                "C:\\Users\\Administrator\\Desktop\\文件格式\\wps_ppt格式.dps"));
        System.out.println(ppt);
    }
}
