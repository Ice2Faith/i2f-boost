package i2f.extension.image.gif;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/24 17:34
 * @desc
 */
public class GifFrame {
    public int delayMs =300;
    public BufferedImage image;

    public GifFrame(BufferedImage image) {
        this.image = image;
    }

    public GifFrame(int delayMs, BufferedImage image) {
        this.delayMs = delayMs;
        this.image = image;
    }
}
