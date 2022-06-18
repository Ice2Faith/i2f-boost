package i2f.core.graphics.image;

import i2f.core.command.CmdLineExecutor;
import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.image.filter.IImageFilter;
import i2f.core.graphics.image.filter.impl.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/6/18 1:04
 * @desc
 */
public class TestImg {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage simg=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\疫情大屏_20220309162306.png"));
        IImageFilter filter=new GrayImageFilter();

        filter=new DoubleValueImageFilter();
        filter=new StepGrayImageFilter(1);
        filter=new StepRgbImageFilter(3);
        filter=new EnhanceComparableImageFilter(0.3);
        filter=new EnhanceHueImageFilter(1.2);
        filter=new EnhanceSaturationImageFilter(1.5);
        filter=new EnhanceLightnessImageFilter(1.3);
        filter=new LigtherDarkImageFilter(0.5);
        filter=new DarkerLightImageFilter(0.5);
        filter=new CoolTonesImageFilter(0.2);
        filter=new WarmTonesImageFilter(0.2);
        filter=new TeaTonesImageFilter(0.2);
        filter=new PinkTonesImageFilter(0.2);
        filter=new ReplaceColorImageFilter(Rgba.rgba(205,131,53,255),Rgba.rgba(0,0,0,0),0.05);
        filter=new RotateLeftImageFilter();
        filter=new RotateRightImageFilter();
        filter=new Rotate180ImageFilter();
        filter=new MirrorHorizontalImageFilter();
        filter=new MirrorVerticalImageFilter();
        filter=new ResizeImageFilter(720,500);
        filter=new ScaleImageFilter(0.5);
        filter=new BorderImageFilter(Rgba.white(),0.05,true,null);
        filter=new BlurImageFilter(12);
        filter=new SeedReplaceColorImageFilter(100,100,0.08,Rgba.transparent());
        filter=new RectangleNormalizeImageFilter(new Point[]{
                new Point(100,100),
                new Point(200,100),
                new Point(0,500),
                new Point(700,800)
        });
        filter=new ClipImageFilter(100,100,200,100);

//        filter=new ScaleImageFilter(0.1);
        BufferedImage dimg = filter.filter(simg);
//        filter=new AsciiStyleImageFilter(22,false);
//        filter=new TextStyleImageFilter("我是你的小baby",22);
//        dimg=filter.filter(dimg);


        File file=new File("E:\\MySystemDefaultFiles\\Desktop\\疫情大屏_20220309162306-gray.png");
        ImageUtil.save(dimg,file);

        CmdLineExecutor.runLine("explorer \""+file.getAbsolutePath()+"\"");
    }
}
