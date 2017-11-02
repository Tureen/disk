/*
 * 描述：文件打水印工具类
 * 创建人：jian.xiong
 * 创建时间：2016-8-3
 */
package com.yunip.utils.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


/**
 * 文件打水印工具类
 */
public class FileWaterMarkUtil {
    
    /**
     * PDF文件添加水印
     * @param fileName 源文件路径
     * @param savePath 目标路径
     * @param waterMarkText 水印文字
     * @param color 水印文字的颜色
     * @param align 对齐方式(1:顶部、2:垂直居中、3:底部)
     * @param fontSize 字体大小(0:自适应，大于0按照设置字体大小显示)
     * @param rotation 水印文字倾斜（旋转）角度(1-360的整数型)
     * @return 返回pdf文件的页数
     * @throws Exception 
     */
    public static int pdfAddWaterMark(String fileName, String savePath, String waterMarkText, BaseColor color, int align, int fontSize, int rotation) throws Exception{
        // 文档总页数
        int pdfTotalPageNum = 0;
        //Document document = new Document();
        PdfReader reader = new PdfReader(fileName);
        Field f = PdfReader.class.getDeclaredField("ownerPasswordUsed");
        f.setAccessible(true); 
        f.set(reader, Boolean.TRUE); 
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(savePath));
        try {
            pdfTotalPageNum = reader.getNumberOfPages();
            //设置字体
            //BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            BaseFont base = BaseFont.createFont("/fonts/STKAITI.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            PdfContentByte under;
            for(int i = 1;i <= pdfTotalPageNum; i++){
                //under = stamper.getUnderContent(i);//显示在文字下
                under = stamper.getOverContent(i);//显示在文字上
                //设置文字水印开始
                under.beginText();
                //设置颜色
                under.setColorFill(color);
                
                //字符越长，字体越小，设置字体大小
                int textFontSize = getFontSize(fontSize, waterMarkText);
                under.setFontAndSize(base, textFontSize);

                //设置水印文字字体倾斜 开始
                float pageWidth = reader.getPageSize(i).getWidth();
                float pageHeight = reader.getPageSize(i).getHeight();
                //水印位置 参数一：对齐（左中右都行） 参数二：水印内容 参数三四：水印开始坐标 参数五：水印旋转角度
                under.showTextAligned(Element.ALIGN_CENTER, waterMarkText, pageWidth/2, getWaterMarkYPoint(pageHeight, textFontSize, align), rotation);//水印文字成60度角倾斜,且页面居中展示
                //字体设置结束
                under.endText();
                //设置文字水印结束
            }
            //stamper.close();
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(reader != null){
                reader.close();
            }
            if(stamper != null){
                stamper.close();
            }
            /*if (null != document){
                document.close();
            }*/
        }
        return pdfTotalPageNum;
    }

    
    /**
     * 根据水印文字长度计算获取字体大小
     * @param fontSize 字体大小(0:自适应(计算得出),其他就是根据设置的字体大小来)
     * @param waterMarkText 字体内容
     * @return 返回字体大小
     */
    private static int getFontSize(int fontSize, String waterMarkText){
        int size = 50;
        if(null != waterMarkText && !"".equals(waterMarkText)){
            if(fontSize > 0){
                return fontSize;
            }else{
                int length = waterMarkText.length();
                if(length <=26 && length >= 18){
                    size = 26;
                }else if(length <18 && length >= 8){
                    size = 40;
                }else if(length <8 && length >= 1){
                    size = 60;
                }else{
                    size = 16;
                }
            }
        }       
        return size;
    }
    
    /**
     * 根据页面的高度获得水印Y坐标
     * @param height 文档高度
     * @param fontSize 字体大小（用于顶部对齐时矫正显示垂直位置）
     * @param position 显示位置(1:顶部、2:垂直居中、3:底部)
     * @return 返回水印所在Y坐标的坐标点
     */
    private static float getWaterMarkYPoint(float height, int fontSize, int position){
        float yPoint = 0f;
        switch (position) {
            case 1://顶部
                yPoint = height - 5 - fontSize;
                break;
            case 2:
                yPoint = height / 2;
                break;
            case 3:
                yPoint = 15;
                break;
            default:
                yPoint = height / 2;
                break;
        }
        return yPoint;
    }
    
    /**
     * 根据外部字体文件创建字体
     * @param path 外部字体文件路径
     * @param size 字体大小
     */
    public static Font createFont(String path, float size){
        Font font = null;
        String fontPath = FileWaterMarkUtil.class.getClassLoader().getResource(path).getPath();
        try {
            Font fontTemp = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            font = fontTemp.deriveFont(size);
        }catch (Exception e) {
            e.printStackTrace();
            font = new Font(null, Font.BOLD, 12);
        }
        return font;
    }
    
    /**
     * 根据页面的高度获得水印Y坐标
     * @param font 字体
     * @param width 图片宽度
     * @param height 图片高度
     * @param fontSize 字体大小
     * @param waterMarkText 水印文字内容
     * @param position 显示位置(1:顶部、2:垂直居中、3:底部)
     * @return 返回水印所在的坐标点
     */
    public static Position getImageWaterMarkPoint(Font font, int width, int height, int fontSize, String waterMarkText, int position){
        Position p = new Position(0, 0);
        FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
        int textWidth = fm.stringWidth(waterMarkText);
        int textHeight = fm.getHeight();
        switch (position) {
            case 1://顶部
                p.setX((width - textWidth) / 2);
                p.setY(textHeight - fontSize/2);
                break;
            case 2:
                p.setX((width - textWidth) / 2);
                p.setY(height/ 2);
                break;
            case 3:
                p.setX((width - textWidth) / 2);
                p.setY(height - textHeight + fontSize);
                break;
            default:
                p.setX((width - textWidth) / 2);
                p.setY(height/ 2);
                break;
        }
        return p;
    }
    
    /**
     * 图片文件添加文字水印
     * @param fileName 源文件路径
     * @param savePath 目标路径
     * @param waterMarkText 水印文字
     * @param color 水印文字的颜色
     * @param align 对齐方式(1:顶部、2:垂直居中、3:底部)
     * @param fontSize 字体大小(0:自适应，大于0按照设置字体大小显示)
     * @throws Exception 
     */
    public static void imageAddWaterMark(String fileName, String savePath, String waterMarkText, Color color, int align, int fontSize, int rotation) throws IOException{
        File srcFile = new File(fileName);
        BufferedImage imgBuff = ImageIO.read(srcFile);
        int width = imgBuff.getWidth();
        int height = imgBuff.getHeight();
        Font font = createFont("fonts/STKAITI.TTF", fontSize);
        Graphics2D g = imgBuff.createGraphics();
        Position position = getImageWaterMarkPoint(font, width, height, fontSize, waterMarkText, align);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.rotate(Math.toRadians(-rotation), width/2, height/2);
        g2d.drawString(waterMarkText, position.getX(), position.getY());
        g.dispose();
        ImageIO.write(imgBuff, "jpeg", new File(savePath));
    }
    
    public static void main(String[] args) throws IOException {
          //pdfAddWaterMark("C:\\test\\pdf1\\10f38ba1-e5f6-4d7f-873c-2d78d0df7fd7.pdf", "C:\\test\\pdf1\\1.pdf", "内部资料，禁止外传！", Color.RED, 2, 0, 30);
          //imageAddWaterMark("C:\\test\\thumb_IMG_5138.jpg", "C:\\test\\2-1.jpg", "内部资料，禁止外传！", Color.RED, 1, 24, 0);
    }
    
    /**
     * 水印位置
     */
    public static class Position {
        /**
         * x坐标位置
         */
        private int x;
        /**
         * y坐标位置
         */
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}