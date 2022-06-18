package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 图像裁剪
 */
public  class ClipImageFilter implements IImageFilter {
    protected int sx;
    protected int sy;
    protected int wid;
    protected int hei;

    public ClipImageFilter(int sx, int sy, int wid, int hei) {
        this.sx = sx;
        this.sy = sy;
        this.wid = wid;
        this.hei = hei;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(wid, hei,BufferedImage.TYPE_4BYTE_ABGR);

        for (int i = 0; i < hei; i++)
        {
            for (int j = 0; j < wid; j++)
            {
                int color=simg.getRGB(j+sx,i+sy);

                dimg.setRGB(j, i, color);
            }
        }

        return dimg;
    }

}
