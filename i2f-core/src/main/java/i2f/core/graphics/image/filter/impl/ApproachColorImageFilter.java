package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 颜色趋近，适用于实现冷暖色调
 */
public class ApproachColorImageFilter extends AbstractImageFilter {
    protected Rgba target;
    protected double rate=0;

    public ApproachColorImageFilter(Rgba target,double rate) {
        this.target = target;
        this.rate=rate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        double srate=1.0-rate;
        double nrate=rate;

        int a = (int)(color.a * srate + target.a * nrate);
        int r = (int)(color.r * srate + target.r * nrate);
        int g = (int)(color.g * srate + target.g * nrate);
        int b = (int)(color.b * srate + target.b * nrate);

        return Rgba.rgba(r,g,b,a);
    }
}
