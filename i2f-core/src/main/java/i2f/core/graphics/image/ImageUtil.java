package i2f.core.graphics.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ltb
 * @date 2022/6/18 0:37
 * @desc 图片操作
 */
public class ImageUtil {
    public static BufferedImage load(File file) throws IOException {
        return ImageIO.read(file);
    }
    public static BufferedImage load(InputStream is) throws IOException {
        return ImageIO.read(is);
    }
    public static void save(BufferedImage img,String formatName,File file) throws IOException {
        ImageIO.write(img,formatName,file);
    }
    public static void save(BufferedImage img,File file) throws IOException {
        String fname=file.getName();
        String formatName="png";
        int idx=fname.lastIndexOf(".");
        if(idx>=0){
            formatName=fname.substring(idx+1);
        }
        ImageIO.write(img,formatName,file);
    }
    public static void save(BufferedImage img, String formatName, OutputStream os) throws IOException {
        ImageIO.write(img,formatName,os);
    }
}
