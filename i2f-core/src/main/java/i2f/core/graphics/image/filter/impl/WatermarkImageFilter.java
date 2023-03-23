package i2f.core.graphics.image.filter.impl;


import i2f.core.graphics.image.filter.IImageFilter;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2023/3/23 10:49
 * @desc
 */
@Data
public class WatermarkImageFilter implements IImageFilter {
    private String text;
    private boolean gridMode = true;
    private String fontFamily;
    private Color color = Color.WHITE;
    private float alpha = 0.7f;
    private double rotate = -30;
    private double spacing = 0.1;
    private int fontCount = 35;

    @Override
    public BufferedImage filter(BufferedImage img) {
        return addWatermark(img, text, gridMode, fontFamily, color, alpha, rotate, spacing, fontCount);
    }

    /**
     * 为图片添加水印，返回添加水印之后的图片
     * 两种模式，也就是gridMode
     * 如果gridMode=true
     * 则是网格形式的水印，也就是重复的水印覆盖整个图片
     * 如果gridMode=false
     * 则仅在右下角显示水印
     *
     * @param img        图片对象
     * @param text       水印文字,支持多行
     * @param gridMode   是否网格覆盖的水印
     * @param fontFamily 水印的字体，可以为null
     * @param color      水印的颜色
     * @param alpha      水印的透明度，[0.0-1.0]，0 全透明，1 不透明
     * @param rotate     旋转角度，-180~180度
     * @param spacing    间隔距离，按照整张图片的宽高的百分比，[0.0-1.0],值越大，水印的间隔越大
     * @param fontCount  字体数量，指按照图片的最长边计算情况下，每条边做多显示多少个字
     * @return
     */
    public static BufferedImage addWatermark(Image img, String text, boolean gridMode, String fontFamily, Color color, float alpha, double rotate, double spacing, int fontCount) {
        int wid = img.getWidth(null);
        int hei = img.getHeight(null);

        BufferedImage mdc = new BufferedImage(wid, hei, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = mdc.createGraphics();

        g.drawImage(img, 0, 0, wid, hei, null);

        Font font = new Font(fontFamily, Font.BOLD, Math.max(wid, hei) / fontCount);
        g.setFont(font);
        g.setColor(color);


        String[] lines = text.split("\n");
        int bhei = lines.length * g.getFontMetrics().getHeight();
        int bwid = 0;
        for (String line : lines) {
            int w = g.getFontMetrics().stringWidth(line);
            if (w > bwid) {
                bwid = w;
            }
        }

        double radian = rotate / 180 * Math.PI;
        if (gridMode) {
            g.rotate(radian, wid / 2, hei / 2);
        } else {
            g.rotate(radian, wid - (bwid + wid * spacing), hei - (bhei + hei * spacing / 2));
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        if (gridMode) {
            int cwid = (int) (wid / (bwid + wid * spacing)) + 1;
            int chei = (int) (hei / (bhei + hei * spacing / 2)) + 1;

            for (int x = 0; x < cwid; x++) {
                for (int y = 0; y < chei; y++) {
                    if ((x + y) % 2 == 0) {
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            g.drawString(line, (int) (x * (bwid + wid * spacing)) - bwid, (int) (y * (bhei + hei * spacing / 2)) - bhei + (i * g.getFontMetrics().getHeight()));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                g.drawString(line, wid - (int) (bwid + wid * spacing), hei - (int) (bhei + hei * spacing / 2) + (i * g.getFontMetrics().getHeight()));
            }
        }

        return mdc;
    }


}
