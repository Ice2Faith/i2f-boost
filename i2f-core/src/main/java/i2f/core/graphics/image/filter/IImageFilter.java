package i2f.core.graphics.image.filter;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:46
 * @desc
 */
public interface IImageFilter {
    BufferedImage filter(BufferedImage img);
}
