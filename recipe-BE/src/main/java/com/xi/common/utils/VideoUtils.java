package com.xi.common.utils;



import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 描述：截取视频中图片作为封面
 * @author zwh 2021.5.18 10:25
 */

@Slf4j
public class VideoUtils {
    private final byte[] imgContent;
    private final String header;

    public static void main(String[] args) throws IOException {
        String s = VideoUtils.fetchFrame("C:\\Users\\86138\\Videos\\snake.mp4");

        VideoUtils.base64ToMultipart(s).transferTo(new File("E:\\work\\graduate\\IMG\\zan\\testWW.jpg"));

    }

    public VideoUtils(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header;
    }

    public static String fetchFrame(String videoPath) {
        FFmpegFrameGrabber ff = null;
        byte[] data = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            //创建FFmpegFrameGrabber对象并设置要捕获的视频源 URL
            ff = new FFmpegFrameGrabber(videoPath);
            ff.start(); //打开视频流
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame f = null;
            while (i < length) {
                // 过滤前5帧，避免出现全黑的图片 这个根据自己的情况来定，这里就以5秒作为测试
                f = ff.grabFrame(); //使用grabber对象的grab方法来读取视频帧
                if ((i > 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            //将图像数据解码为Java BufferedImage对象以进行处理
            BufferedImage bi =  new Java2DFrameConverter().getBufferedImage(f);
            String rotate = ff.getVideoMetadata("rotate");
            if (rotate != null) {
                bi = rotate(bi, Integer.parseInt(rotate));
            }
            ImageIO.write(bi, "jpg", os); //将视频帧保存为本地文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ff != null) {
                    ff.stop(); //释放资源
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/jpg;base64,"+encoder.encode(os.toByteArray());
    }

    public static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }
    /**
     * 将base64转换成MultipartFile
     * @param base64 将base64格式的图片字符串转换成MultipartFile
     * @return VideoUtils
     */
    public static VideoUtils base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b;
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new VideoUtils(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if(angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }


   
    public String getOriginalFilename() {
        return null;
    }
   
    public String getName() {
        return null;
    }



   
    public String getContentType() {
        return header.split(":")[1];
    }

   
    public long getSize() {
        return imgContent.length;
    }
   
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }


   
    public byte[] getBytes() throws IOException {
        return imgContent;
    }
   
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

   
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

}


