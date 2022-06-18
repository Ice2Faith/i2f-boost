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
public class AsciiStyleImageFilter implements IImageFilter {
    public static final char[] DEFAULT_ASCII_CHARS={ ' ', '`', '.', '^', ',', ':', '~', '"', '<', '!', 'c', 't', '+',
            '{', 'i', '7', '?', 'u', '3', '0', 'p', 'w', '4', 'A', '8', 'D', 'X', '%', '#', 'H', 'W', 'M' };

    protected char[] chars;
    protected int fontSize;
    protected boolean keepRgb;
    protected String fontFamily="黑体";
    protected Rgba backgroundColor=Rgba.transparent();


    public AsciiStyleImageFilter(char[] chars, int fontSize, boolean keepRgb, String fontFamily, Rgba backgroundColor) {
        this.chars = chars;
        this.fontSize = fontSize;
        this.keepRgb = keepRgb;
        this.fontFamily = fontFamily;
        this.backgroundColor = backgroundColor;
    }

    public AsciiStyleImageFilter(int fontSize, boolean keepRgb) {
        this.chars=DEFAULT_ASCII_CHARS;
        this.fontSize = fontSize;
        this.keepRgb = keepRgb;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(img.getWidth()*fontSize,img.getHeight()*fontSize,BufferedImage.TYPE_4BYTE_ABGR);

        Graphics draw = dimg.getGraphics();
        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
        draw.setFont(font);

        draw.setColor(new Color(backgroundColor.argb(),true));
        draw.fillRect(0, 0, dimg.getWidth(), dimg.getHeight());

        for (int x = 0; x < simg.getWidth(); x++) {
            for (int y = 0; y < simg.getHeight(); y++) {
                int c=simg.getRGB(x,y);
                Rgba color= Rgba.argb(c);
                if(keepRgb){
                    draw.setColor(new Color(color.argb(),true));
                }else{
                    draw.setColor(Color.black);
                }
                int gray=color.gray();
                int idx=(int)(gray*1.0/255*chars.length);
                char ch=chars[idx];
                draw.drawString(""+ch,x*fontSize,y*fontSize);
            }
        }

        return dimg;
    }

}
