package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 桃红色调
 */
public class PinkTonesImageFilter extends ApproachColorImageFilter {
    public PinkTonesImageFilter(double rate) {
        super(Rgba.rgba(255,0,128,255), rate);
    }
}
