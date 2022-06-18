package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 阶化灰度
 */
public class StepGrayImageFilter extends AbstractImageFilter {
    protected int steps=2;
    public StepGrayImageFilter(int steps){
        this.steps=steps;
    }
    @Override
    public Rgba pixel(Rgba color) {
        int gy=color.gray();
        // 计算每一阶多大
        double eve=255.0/steps;

        // 计算此像素使用了多少阶（四舍五入）
        double cnt=(int)(gy/eve+0.5);

        // 计算这一阶对应的像素值
        gy=(int)(cnt*eve);

        return Rgba.rgba(gy,gy,gy,color.a);
    }
}
