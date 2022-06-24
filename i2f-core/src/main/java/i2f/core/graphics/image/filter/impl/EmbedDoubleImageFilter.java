package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 两张图片嵌合，黑白背景下显示不同的图片
 */
public  class EmbedDoubleImageFilter implements IImageFilter {
    protected BufferedImage otherImg;

    public EmbedDoubleImageFilter(BufferedImage otherImg) {
        this.otherImg = otherImg;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {

        BufferedImage simg1=img;
        BufferedImage simg2=otherImg;
        int dwid=Math.max(simg1.getWidth(),simg2.getWidth());
        int dhei=Math.max(simg1.getHeight(),simg2.getHeight());
        BufferedImage dimg = new BufferedImage(dwid,dhei, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < dwid; x++)
        {
            double rx=x*1.0/dwid;
            for (int y = 0; y < dhei; y++)
            {
                double ry=y*1.0/dhei;
                Rgba sc=null;
                if((x+y)%2==0){
                    int dx=(int)(rx*simg1.getWidth());
                    int dy=(int)(ry*simg1.getHeight());
                    sc=Rgba.argb(simg1.getRGB(dx,dy));
                    int gy = Rgba.stdRgba((int)(sc.gray()*1.2)); // 强化灰度，这样两证图片聚合度降低，效果更好
                    sc=Rgba.rgba(0,0,0,255-gy);
                }else{
                    int dx=(int)(rx*simg2.getWidth());
                    int dy=(int)(ry*simg2.getHeight());
                    sc=Rgba.argb(simg2.getRGB(dx,dy));
                    int gy=Rgba.stdRgba((int)(sc.gray()*1.2));
                    sc=Rgba.rgba(255,255,255,gy);
                }
                dimg.setRGB(x, y, sc.argb());
            }
        }

        return dimg;
    }

}
