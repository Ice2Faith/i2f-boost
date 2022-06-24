package test.graphics;

import i2f.core.command.CmdLineExecutor;
import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.image.ImageUtil;
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
//        testFilter();
//        testEmbedImage();
        BufferedImage img1=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\l01.jpg"));

        String data="算法实现 \n" +
                "\n" +
                "　　基本的算法思路上面已经提过了，可以说是一个相当简单的算法了。不过具体有几点需要注意：\n" +
                "\n" +
                "由其原理可知，隐藏图只能是黑白的，不能是彩色的，因此当遇到彩色像素时，需要将其转换成灰度。对于彩色转灰度，心理学中有一个公式：Gray = R*0.299 + G*0.587 + B*0.114，我们需要做的是根据算出来的灰度设定像素透明度。在白色背景下，黑色像素的灰度会随着像透明度增高而降低，在黑色背景下，白色像素的灰度会随着透明度增高而增高。\n" +
                "考虑到需要合成的两张图片尺寸不一致，为了保证生成的隐藏图能够完成保留两张图片信息并且不发生变形，我们需要将最终图片的长和宽设定为两张图片尺寸中最大的长和最大的宽。";

        HideData2Image.write2ImageString(img1,data);

        File file=new File("E:\\MySystemDefaultFiles\\Desktop\\data.png");
        ImageUtil.save(img1,file);

        img1=ImageUtil.load(file);
        String rdata=HideData2Image.read4ImageString(img1);

        System.out.println(data.equals(rdata));
        System.out.println(rdata);

        CmdLineExecutor.runLine("explorer \""+file.getAbsolutePath()+"\"");
    }

    private static void testEmbedImage() throws IOException, InterruptedException {
        BufferedImage img1=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\l01.jpg"));
        BufferedImage img2=ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\17临时文档\\图像边缘查找\\图片测试\\l05.jpg"));

        EmbedDoubleImageFilter filter=new EmbedDoubleImageFilter(img2);
        BufferedImage dimg=filter.filter(img1);

        File file=new File("E:\\MySystemDefaultFiles\\Desktop\\hide.png");
        ImageUtil.save(dimg,file);

        CmdLineExecutor.runLine("explorer \""+file.getAbsolutePath()+"\"");
    }

    public static void testFilter() throws IOException, InterruptedException {

        BufferedImage simg= ImageUtil.load(new File("E:\\MySystemDefaultFiles\\Desktop\\疫情大屏_20220309162306.png"));
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
