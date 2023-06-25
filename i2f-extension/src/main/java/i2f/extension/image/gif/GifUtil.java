package i2f.extension.image.gif;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/6/24 17:29
 * @desc
 */
public class GifUtil {

    public static void save(OutputStream os, Gif gif){
        AnimatedGifEncoder encoder=new AnimatedGifEncoder();
        encoder.start(os);
        encoder.setQuality(gif.quality);
        encoder.setSize(gif.width,gif.height);
        encoder.setRepeat(gif.repeat);
        encoder.setTransparent(new Color(gif.transparentColor.argb(),true));
        for(GifFrame item : gif.frames){
            encoder.setDelay(item.delayMs);

            int pwid=item.image.getWidth();
            int phei=item.image.getHeight();

            double rateWid=pwid*1.0/gif.width;
            double rateHei=phei*1.0/gif.height;

            if(pwid/rateHei>gif.width){ // 按照高度缩放，大于目标宽度
                pwid=(int)(pwid/rateWid);
                phei=(int)(phei/rateWid);
            }else{
                pwid=(int)(pwid/rateHei);
                phei=(int)(phei/rateHei);
            }

            Image smg = item.image.getScaledInstance(pwid,phei, Image.SCALE_SMOOTH);

            BufferedImage fmg=new BufferedImage(gif.width,gif.height,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics gra=fmg.getGraphics();
            gra.setColor(new Color(gif.transparentColor.argb(),true));
            gra.fillRect(0,0,fmg.getWidth(),fmg.getHeight());
            gra.drawImage(smg,(gif.width-pwid)/2,(gif.height-phei)/2,null);

            encoder.addFrame(fmg);
        }
        encoder.finish();

    }

    public static Gif load(InputStream is){
        GifDecoder decoder=new GifDecoder();
        int status = decoder.read(is);
        if(status!=GifDecoder.STATUS_OK){
            return null;
        }
        Gif ret=new Gif();
        ret.repeat= decoder.getLoopCount();
        int cnt= decoder.getFrameCount();
        ret.frames=new ArrayList<>();
        Dimension dim= decoder.getFrameSize();
        ret.width= dim.width;
        ret.height=dim.height;
        for(int i=0;i<cnt;i++){
            int delayMs= decoder.getDelay(i);
            BufferedImage img= decoder.getFrame(i);
            GifFrame frame=new GifFrame(delayMs,img);
            ret.frames.add(frame);
        }

        return ret;
    }
}
