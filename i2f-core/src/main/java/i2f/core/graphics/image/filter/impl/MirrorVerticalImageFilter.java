package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 垂直镜像图片
 */
public class MirrorVerticalImageFilter implements IImageFilter {

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(simg.getWidth(), simg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);


        for (int x = 0; x < simg.getWidth(); x++) {
            for (int y = 0; y < simg.getHeight(); y++) {
                int sc=simg.getRGB(x,y);
                int ry = (simg.getHeight() - y) % simg.getHeight();
                int rx = (simg.getWidth() + x) % simg.getWidth();
                dimg.setRGB(rx,ry,sc);
            }
        }

        return dimg;
    }

}
