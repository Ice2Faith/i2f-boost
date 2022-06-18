package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 阶化RGB
 */
public class StepRgbImageFilter extends AbstractImageFilter {
    protected int steps=2;
    public StepRgbImageFilter(int steps){
        this.steps=steps;
    }
    @Override
    public Rgba pixel(Rgba color) {
        int r=color.r;
        int g=color.g;
        int b=color.b;
        // 计算每一阶多大
        double eve=255.0/steps;

        // 计算此像素使用了多少阶（四舍五入）
        double rcnt=(int)(r/eve+0.5);
        double gcnt=(int)(g/eve+0.5);
        double bcnt=(int)(b/eve+0.5);

        // 计算这一阶对应的像素值
        r=(int)(rcnt*eve);
        g=(int)(gcnt*eve);
        b=(int)(bcnt*eve);

        return Rgba.rgba(r,g,b,color.a);
    }
}
