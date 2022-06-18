package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 颜色替换
 */
public class ReplaceColorImageFilter extends AbstractImageFilter {
    protected Rgba src;
    protected Rgba dst;
    protected double wrongRate=-1;

    public ReplaceColorImageFilter(Rgba src, Rgba dst, double wrongRate) {
        this.src = src;
        this.dst = dst;
        this.wrongRate = wrongRate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        boolean isTarget = false;
        if (wrongRate <= 0)
        {
            if (src.rgba() == color.rgba())
            {
                isTarget = true;

            }
        }
        else
        {
            double rate = Rgba.diff(src, color);
            if (rate <= wrongRate)
            {
                isTarget = true;

            }
        }

        Rgba dc=color;
        if (isTarget)
        {
            dc = dst;
        }

        return dc;
    }
}
