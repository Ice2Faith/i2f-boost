package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 冷色调
 */
public class CoolTonesImageFilter extends ApproachColorImageFilter {
    public CoolTonesImageFilter(double rate) {
        super(Rgba.rgba(0,128,255,255), rate);
    }
}
