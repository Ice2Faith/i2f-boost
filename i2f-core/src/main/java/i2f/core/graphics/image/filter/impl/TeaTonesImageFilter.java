package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 茶色调
 */
public class TeaTonesImageFilter extends ApproachColorImageFilter {
    public TeaTonesImageFilter(double rate) {
        super(Rgba.rgba(0,255,128,255), rate);
    }
}
