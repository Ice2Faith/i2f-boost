package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 图像字符化
 */
public class TextStyleImageFilter implements IImageFilter {

    protected String str;
    protected int fontSize;
    protected String fontFamily="黑体";
    protected Rgba backgroundColor=Rgba.transparent();

    public TextStyleImageFilter(String str, int fontSize, String fontFamily, Rgba backgroundColor) {
        this.str = str;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.backgroundColor = backgroundColor;
    }

    public TextStyleImageFilter(String str, int fontSize) {
        this.str = str;
        this.fontSize = fontSize;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(img.getWidth() * fontSize, img.getHeight() * fontSize, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics draw = dimg.getGraphics();
        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
        draw.setFont(font);

        draw.setColor(new Color(backgroundColor.argb(),true));
        draw.fillRect(0, 0, dimg.getWidth(), dimg.getHeight());

        int idx = 0;

        for (int y = 0; y < simg.getHeight(); y++) {
            for (int x = 0; x < simg.getWidth(); x++) {
                int c = simg.getRGB(x, y);
                Rgba color = Rgba.argb(c);

                draw.setColor(new Color(color.argb(), true));

                char ch = str.charAt(idx);
                idx = (idx + 1) % str.length();
                draw.drawString("" + ch, x * fontSize, y * fontSize);
            }
        }

        return dimg;
    }

}
