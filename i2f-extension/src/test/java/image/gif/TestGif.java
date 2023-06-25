package image.gif;

import i2f.core.graphics.image.ImageUtil;
import i2f.core.os.command.CmdLineExecutor;
import i2f.extension.image.gif.Gif;
import i2f.extension.image.gif.GifFrame;
import i2f.extension.image.gif.GifUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/6/26 16:03
 * @desc
 */
public class TestGif {
    public static void main(String[] args) throws IOException, InterruptedException {
        File file=new File("E:\\MySystemDefaultFiles\\Desktop\\testj.gif");
        BufferedImage img1= ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\l01.jpg"));
        BufferedImage img2=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\l05.jpg"));
        BufferedImage img3=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\k09.jpg"));
        FileOutputStream fos=new FileOutputStream(file);
        Gif gif=new Gif();
        gif.width=480;
        gif.height=720;
        gif.quality=10;
        gif.repeat=0;
        gif.frames=new ArrayList<>();
        gif.frames.add(new GifFrame(500,img1));
        gif.frames.add(new GifFrame(3000,img2));
        gif.frames.add(new GifFrame(1000,img3));
        GifUtil.save(fos,gif);
        fos.close();
        CmdLineExecutor.runLine("explorer \""+file.getAbsolutePath()+"\"");

        FileInputStream fis=new FileInputStream(file);
        Gif rgif = GifUtil.load(fis);
        System.out.println(rgif.frames.size());
    }
}
