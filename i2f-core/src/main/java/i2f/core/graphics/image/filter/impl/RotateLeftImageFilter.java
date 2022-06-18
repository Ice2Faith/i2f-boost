package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 左转图片
 */
public class RotateLeftImageFilter implements IImageFilter {

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(simg.getHeight(), simg.getWidth(), BufferedImage.TYPE_4BYTE_ABGR);


        for (int x = 0; x < simg.getWidth(); x++) {
            for (int y = 0; y < simg.getHeight(); y++) {
                int sc=simg.getRGB(x,y);
                int rx = (simg.getHeight() + y) % simg.getHeight();
                int ry = (simg.getWidth() - x) % simg.getWidth();
                dimg.setRGB(rx,ry,sc);
            }
        }

        return dimg;
    }

}
