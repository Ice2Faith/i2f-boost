package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.math.Calc;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 增强对比度
 */
public class EnhanceComparableImageFilter extends AbstractImageFilter {
    protected int half=255/2;
    protected double rate=0;

    public EnhanceComparableImageFilter(double rate) {
        this.rate = rate;
    }

    @Override
    public Rgba pixel(Rgba color) {
        double upRate=1.0+rate;
        double downRate=1.0-rate;

        int r=color.r;
        int g=color.g;
        int b=color.b;
        int a=color.a;

        r=(int) Calc.between((r>half)?(r*upRate):(r*downRate),0,255);
        g=(int) Calc.between((g>half)?(g*upRate):(g*downRate),0,255);
        b=(int) Calc.between((b>half)?(b*upRate):(b*downRate),0,255);
        a=(int) Calc.between((a>half)?(a*upRate):(a*downRate),0,255);

        return Rgba.rgba(r,g,b,a);
    }
}
