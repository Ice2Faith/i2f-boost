package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 暖色调
 */
public class WarmTonesImageFilter extends ApproachColorImageFilter {
    public WarmTonesImageFilter(double rate) {
        super(Rgba.rgba(255,128,0,255), rate);
    }
}
