package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 图像边缘提取
 */
public class BorderImageFilter implements IImageFilter {

    protected Rgba whiteBolor;
    protected double wrongRate;
    protected boolean keepRgbBorder;
    protected Rgba borderColor;

    public BorderImageFilter(Rgba whiteBolor, double wrongRate, boolean keepRgbBorder, Rgba borderColor) {
        this.whiteBolor = whiteBolor;
        this.wrongRate = wrongRate;
        this.keepRgbBorder = keepRgbBorder;
        this.borderColor = borderColor;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(simg.getWidth(), simg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < simg.getWidth() - 1; x++)
        {
            for (int y = 0; y < simg.getHeight() - 1; y++)
            {
                Rgba rightBottomColor = Rgba.argb(simg.getRGB(x + 1, y + 1));
                Rgba rightColor = Rgba.argb(simg.getRGB(x + 1, y));
                Rgba bottomColor = Rgba.argb(simg.getRGB(x, y + 1));

                int diffCount = 3;
                Rgba[] diffArr = { rightBottomColor, rightColor, bottomColor };

                Rgba currentColor = Rgba.argb(simg.getRGB(x, y));

                boolean isBorder = false;
                for (int i = 0; i < diffCount; i++)
                {
                    if (wrongRate <= 0)
                    {
                        if (currentColor.argb() != diffArr[i].argb())
                        {
                            isBorder = true;
                            break;
                        }
                    }
                    else
                    {
                        double rate = Rgba.diff(currentColor, diffArr[i]);
                        if (rate > wrongRate)
                        {
                            isBorder = true;
                            break;
                        }
                    }
                }
                if (isBorder)
                {
                    if (!keepRgbBorder)
                    {
                        dimg.setRGB(x, y, borderColor.argb());
                    }
                    else
                    {
                        dimg.setRGB(x, y, currentColor.argb());
                    }
                }
                else
                {
                    dimg.setRGB(x, y, whiteBolor.argb());
                    if (x == 1 || y == 1)
                    {
                        dimg.setRGB(x, y, whiteBolor.argb());
                    }
                }
            }
        }

        return dimg;
    }

}
