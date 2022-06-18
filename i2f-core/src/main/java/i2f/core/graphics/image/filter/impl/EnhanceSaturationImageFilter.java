package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Hsl;
import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 增强饱和度
 */
public class EnhanceSaturationImageFilter extends AbstractImageFilter {
    protected double rate=0;

    public EnhanceSaturationImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.s=Hsl.stdHsl(hsl.s*rate);

        return hsl.rgba();
    }
}
