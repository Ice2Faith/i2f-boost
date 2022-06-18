package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Hsl;
import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 增强色相
 */
public class EnhanceHueImageFilter extends AbstractImageFilter {
    protected double rate=0;

    public EnhanceHueImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.h=Hsl.stdHsl(hsl.h*rate);

        return hsl.rgba();
    }
}
