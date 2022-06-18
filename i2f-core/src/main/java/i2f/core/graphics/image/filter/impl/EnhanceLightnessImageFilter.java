package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Hsl;
import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 增强亮度
 */
public class EnhanceLightnessImageFilter extends AbstractImageFilter {
    protected double rate=0;

    public EnhanceLightnessImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {

        Hsl hsl = color.hsl();
        hsl.l=Hsl.stdHsl(hsl.l*rate);

        return hsl.rgba();
    }
}
