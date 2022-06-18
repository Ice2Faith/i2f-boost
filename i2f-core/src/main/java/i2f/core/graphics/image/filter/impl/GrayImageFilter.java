package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 灰度
 */
public class GrayImageFilter extends AbstractImageFilter {
    @Override
    public Rgba pixel(Rgba color) {
        int gy=color.gray();
        return Rgba.rgba(gy,gy,gy,color.a);
    }
}
