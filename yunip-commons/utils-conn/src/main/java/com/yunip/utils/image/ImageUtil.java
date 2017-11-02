package com.yunip.utils.image;

import java.awt.Rectangle; 
import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException;  
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
//import com.mortennobel.imagescaling.ResampleOp;

/**
 * 图片处理工具类
 *
 */
public class ImageUtil {
    /**
     * 
     * @param oldPath  原路径
     * @param newPath   现在路径
     */
/*  public static void copyFile(String oldPath, String newPath) {
          try {
           int bytesum = 0;
           int byteread = 0;
           File oldfile = new File(oldPath);
           if (oldfile.exists()) { // 文件存在时
            InputStream inStream = new FileInputStream(oldPath); // 读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
             bytesum += byteread; // 字节数 文件大小
             fs.write(buffer, 0, byteread);
            }
            inStream.close();
            System.out.println("上传成功");
           }
           else {
            System.out.println("aaa");
        }
          } catch (Exception e) {
           System.out.println("复制单个文件操作出错");
           e.printStackTrace();

          }

         }*/
    
    /**
     * 根据传入的图片坐标进行图片截取
     * 
     * @param x1 X起点坐标
     * @param x2 X终点坐标
     * @param y1 Y起点坐标
     * @param y2 Y终点坐标
     * @param originPath 原始图片的存放路径
     * @param savePath 截取后图片的存储路径
     * @throws IOException
     */
    public static void scissor(int x1, int x2, int y1, int y2,
            String originPath, String savePath) throws IOException {
        
        FileInputStream is = null;
        ImageInputStream iis = null;
        
        try {
            
            // 读取图片文件
            is = new FileInputStream(originPath);

            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，
             * 这些 ImageReader 声称能够解码指定格式。
             * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO
                    .getImageReadersByFormatName(getExtention(originPath)
                            .toLowerCase());
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);

            /*
             * iis:读取源.true:只向前搜索，将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 
             * reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /*
             * 描述如何对流进行解码的类，用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 
             * 将从其 ImageReader 实现的
             * getDefaultReadParam方法中返回 ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);

            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
             * BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);

            // 保存新图片
            ImageIO.write(bi, getExtention(originPath).toLowerCase(), new File(
                    savePath));
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    /**
     * 
     * 缩放图片
     * 
     * @param width 宽
     * @param height 高
     * @param originPath 原始路径
     * @param savePath 保存路径
     * @throws IOException
     */
    /*public static void scaleImage(int width, int height, String originPath,
            String savePath) throws IOException {
        
        BufferedImage sourceImage = readImage(originPath);
        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage rescaledTomato = resampleOp.filter(sourceImage, null);
        ImageIO.write(rescaledTomato, getExtention(originPath).toLowerCase(),
                new File(savePath));
    }*/
    
/*  public static BufferedImage readImage(String imagePath) throws IOException {
        return readImage(new File(imagePath));
    }
    
    public static BufferedImage readImage(File image) throws IOException {
        return ImageIO.read(image);
    }*/
    
    /**
     * 功能：提取文件名的后缀
     * 
     * @param fileName
     * @return
     */
    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }
    /**
     * 功能：图片变灰
     * @param originPath  图片原路径
     * @param savePath    图片保存后的路径
     * @throws IOException  
     */
    /*public static  void grayImage(String originPath,String savePath) throws IOException
    {
        BufferedImage sourceImage = readImage(originPath);
        int imageWidth = sourceImage.getWidth();
          int imageHeight = sourceImage.getHeight();

          BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_3BYTE_BGR);

          ColorConvertOp cco = new ColorConvertOp(ColorSpace
            .getInstance(ColorSpace.CS_GRAY), null);
          cco.filter(sourceImage, newPic);
          ImageIO.write(newPic, getExtention(originPath).toLowerCase(),
                    new File(savePath));    
    }*/

}
