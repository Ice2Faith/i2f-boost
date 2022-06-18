package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 重新设置大小
 */
public class ResizeImageFilter implements IImageFilter {
    protected int wid;
    protected int hei;

    public ResizeImageFilter(int wid, int hei) {
        this.wid = wid;
        this.hei = hei;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(wid, hei, BufferedImage.TYPE_4BYTE_ABGR);

        for (int y = 0; y < dimg.getHeight(); y++)
        {
            double dy = (y * 1.0 / dimg.getHeight()) * simg.getHeight();
            int iy = (int)dy;
            double yrate = dy - iy;
            for (int x = 0; x < dimg.getWidth(); x++)
            {
                double dx = (x * 1.0 / dimg.getWidth()) * simg.getWidth();
                int ix = (int)dx;
                double xrate = dx - ix;

                Rgba pc = Rgba.argb(simg.getRGB(ix, iy));
                int dr = 0;
                int dg = 0;
                int db = 0;
                int da = 0;


                if (ix + 1 < simg.getWidth() && iy + 1 < simg.getHeight())
                {
                    Rgba rc =  Rgba.argb(simg.getRGB(ix + 1, iy));
                    Rgba bc =  Rgba.argb(simg.getRGB(ix, iy + 1));
                    Rgba rbc = Rgba.argb(simg.getRGB(ix + 1, iy + 1));

                    double arate = (xrate + yrate) / 2;

                    double dr1 = (pc.r * xrate + rc.r * (1.0 - xrate));
                    double dg1 = (pc.g * xrate + rc.g * (1.0 - xrate));
                    double db1 = (pc.b * xrate + rc.b * (1.0 - xrate));
                    double da1 = (pc.a * xrate + rc.a * (1.0 - xrate));

                    double dr2 = (pc.r * yrate + bc.r * (1.0 - yrate));
                    double dg2 = (pc.g * yrate + bc.g * (1.0 - yrate));
                    double db2 = (pc.b * yrate + bc.b * (1.0 - yrate));
                    double da2 = (pc.a * yrate + bc.a * (1.0 - yrate));

                    double dr3 = (pc.r * arate + rbc.r * (1.0 - arate));
                    double dg3 = (pc.g * arate + rbc.g * (1.0 - arate));
                    double db3 = (pc.b * arate + rbc.b * (1.0 - arate));
                    double da3 = (pc.a * arate + rbc.a * (1.0 - arate));

                    dr = (int)((dr1 + dr2 + dr3) / 3);
                    dg = (int)((dg1 + dg2 + dg3) / 3);
                    db = (int)((db1 + db2 + db3) / 3);
                    da = (int)((da1 + da2 + da3) / 3);

                }
                else if (ix + 1 < simg.getWidth())
                {
                    Rgba rc = Rgba.argb(simg.getRGB(ix + 1, iy));
                    dr = (int)(pc.r * xrate + rc.r * (1.0 - xrate));
                    dg = (int)(pc.g * xrate + rc.g * (1.0 - xrate));
                    db = (int)(pc.b * xrate + rc.b * (1.0 - xrate));
                    da = (int)(pc.a * xrate + rc.a * (1.0 - xrate));
                }
                else if (iy + 1 < simg.getHeight())
                {
                    Rgba bc = Rgba.argb(simg.getRGB(ix, iy + 1));
                    dr = (int)(pc.r * xrate + bc.r * (1.0 - xrate));
                    dg = (int)(pc.g * xrate + bc.g * (1.0 - xrate));
                    db = (int)(pc.b * xrate + bc.b * (1.0 - xrate));
                    da = (int)(pc.a * xrate + bc.a * (1.0 - xrate));

                }
                else
                {
                    dr = pc.r;
                    dg = pc.g;
                    db = pc.b;
                    da = pc.a;
                }


                dimg.setRGB(x, y, Rgba.rgba(dr, dg, db,da).argb());

            }
        }

        return dimg;
    }

}
