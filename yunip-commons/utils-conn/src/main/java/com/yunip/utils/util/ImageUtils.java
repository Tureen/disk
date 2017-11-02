/*
 * 描述：图片处理工具类
 * 创建人：jian.xiong
 * 创建时间：2016-8-3
 */
package com.yunip.utils.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类
 */
public class ImageUtils {
    private static String DEFAULT_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;
    
    /**
     * 根据图片路径生成缩略图 
     * @param imagePath    原图片路径
     * @param toPath       输出路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     * @throws Exception 
     */
    public static void thumbnailImage(File imgFile, File toPath, int w, int h, String prevfix, boolean force) throws Exception{
        if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                    throw new Exception("文件类型不支持压缩");
                }
                Image img = ImageIO.read(imgFile);
                if(!force){
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, toPath);
            } catch (IOException e) {
               e.printStackTrace();
               throw e;
            }
        }else{
            throw new Exception("文件不存在");
        }
    }
    
    /**
     * 根据设定的压缩基数，自适应生成图片路径生成预览图 
     * @param imagePath    原图片路径
     * @param toPath       输出路径
     * @param minSize      预览图最小大小(单位字节)，图片不小于此大小时才进行压缩处理。1024*100
     * @param radix        图片压缩大小基数(根据图片本身的宽、高比例*此基数，计算出压缩图片后的实际宽、高)
     * @param prevfix      生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     * @throws Exception 
     */
    public static void autoadaptationThumbImage(File imgFile, File toFile, long minSize, int radix, String prevfix) throws Exception{
        if(imgFile.exists()){
            try {
                if(!toFile.getParentFile().exists()){//判断目录是否存在，不存在则创建
                    toFile.getParentFile().mkdirs();
                }
                if(imgFile.length() <= minSize){//当图片大小小于100K时，直接复制不进行压缩
                    FileUtil.copyFile(imgFile.getPath(), toFile.getPath());
                }else{
                    // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                    String types = Arrays.toString(ImageIO.getReaderFormatNames());
                    String suffix = null;
                    // 获取图片后缀
                    if(imgFile.getName().indexOf(".") > -1) {
                        suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                    }
                    // 类型和图片后缀全部小写，然后判断后缀是否合法
                    if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                        throw new Exception("文件类型不支持压缩");
                    }
                    Image img = ImageIO.read(imgFile);
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    //计算图片的宽*高比例
                    BigDecimal result = new BigDecimal(String.valueOf(width)).divide(new BigDecimal(String.valueOf(height)), 2, BigDecimal.ROUND_HALF_UP);
                    double ratio = result.doubleValue();
                    int w = (int) (ratio * radix);//计算得出压缩后的实际宽度
                    int h = radix;//计算得出压缩后的实际高度
                    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.getGraphics();
                    g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                    g.dispose();
                    // 将图片保存在原目录并加上前缀
                    ImageIO.write(bi, suffix, toFile);
                }
            } catch (IOException e) {
               e.printStackTrace();
               throw e;
            }
        }else{
            throw new Exception("文件不存在");
        }
    }
    
    public static void thumbnailImage(String imagePath, String toPath, int w, int h, String prevfix, boolean force) throws Exception{
        File imgFile = new File(imagePath);
        File toFile = new File(toPath);
        thumbnailImage(imgFile, toFile, w, h, prevfix, force);
    }
    
    public static void thumbnailImage(String imagePath, String toPath, int w, int h, boolean force) throws Exception{
        thumbnailImage(imagePath, toPath, w, h, DEFAULT_PREVFIX, force);
    }
    
    public static void thumbnailImage(String imagePath, String toPath, int w, int h) throws Exception{
        thumbnailImage(imagePath, toPath, w, h, DEFAULT_FORCE);
    }
    
    public static void autoadaptationThumbImage(String imagePath, String toPath, long minSize, int radix) throws Exception{
        File imgFile = new File(imagePath);
        File toFile = new File(toPath);
        autoadaptationThumbImage(imgFile, toFile, minSize, radix, DEFAULT_PREVFIX);
    }
    
    public static void autoadaptationThumbImage(String imagePath, String toPath, long minSize, int radix, String prevfix) throws Exception{
        File imgFile = new File(imagePath);
        File toFile = new File(toPath);
        autoadaptationThumbImage(imgFile, toFile, minSize, radix, prevfix);
    }
    
    public static void main(String[] args) throws Exception {
        //new ImageUtil1().thumbnailImage("C:\\test\\用飘柔就是自信.jpeg", 300, 450);
        //ImageUtils.thumbnailImage("C:\\test\\IMG_5138.jpg", "C:\\test\\IMG_5138-1.jpg", 300, 450);
        //ImageUtils.autoadaptationThumbImage("C:\\test\\IMG_5138.jpg", "C:\\test\\IMG_5138-1.jpg", 1024 * 100, 200);
    }
}