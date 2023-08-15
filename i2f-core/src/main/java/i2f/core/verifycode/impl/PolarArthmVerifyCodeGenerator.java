package i2f.core.verifycode.impl;

import i2f.core.verifycode.core.GraphicsUtil;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.std.IVerifyCodeGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:35
 * @desc
 */
public class PolarArthmVerifyCodeGenerator implements IVerifyCodeGenerator {

    public static final String PARAM_SPLIT_COUNT = "splitCount";
    public static final String PARAM_BOUND_NUMBER = "boundNumber";

    public static final int DEFAULT_SPLIT_COUNT = 33;
    public static final int DEFAULT_BOUND_NUMBER = 100;
    public static final int DEFAULT_WIDTH = 320;
    public static final int DEFAULT_HEIGHT = 320;

    public static String makeCode(int len, boolean numberOnly) {

        String ret = "";
        for (int i = 0; i < len; i++) {
            int num = MathUtil.RANDOM.nextInt(10);
            if (!numberOnly) {
                num = MathUtil.RANDOM.nextInt(62);
            }
            char ch = 'A';
            if (num < 10) {
                ch = (char) ('0' + num);
            } else if (num < 36) {
                ch = (char) ('A' + (num - 10));
            } else {
                ch = (char) ('a' + (num - 36));
            }
            ret += ch;
        }
        return ret;
    }

    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeDto ret = new VerifyCodeDto();

        if (width <= 0) {
            width = DEFAULT_WIDTH;
        }
        if (height <= 0) {
            height = DEFAULT_HEIGHT;
        }

        int splitCount = DEFAULT_SPLIT_COUNT;
        int boundNumber = DEFAULT_BOUND_NUMBER;
        if (params != null) {
            if (params.containsKey(PARAM_SPLIT_COUNT)) {
                Integer val = (Integer) params.get(PARAM_SPLIT_COUNT);
                if (val != null && val > 0) {
                    splitCount = val;
                }
            }
            if (params.containsKey(PARAM_BOUND_NUMBER)) {
                Integer val = (Integer) params.get(PARAM_BOUND_NUMBER);
                if (val != null && val > 0) {
                    boundNumber = val;
                }
            }
        }


        int dynamicCount = (int) ((MathUtil.RANDOM.nextDouble() * 0.5) * splitCount);
        dynamicCount = Math.min(dynamicCount, 20);
        splitCount = (int) (splitCount + dynamicCount);
        List<Integer> numbers = new ArrayList<>(splitCount);
        for (int i = 0; i < splitCount; i++) {
            int num = MathUtil.RANDOM.nextInt(boundNumber);
            numbers.add(num);
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int targetIndex = MathUtil.RANDOM.nextInt(splitCount);
        int addNumber = MathUtil.RANDOM.nextInt(boundNumber);

        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);


        int posX = width / 2;
        int posY = height / 2;


        int fontWidth = Math.min(width, height) / 25;
        Font font = new Font(null, Font.ITALIC, fontWidth);
        g.setFont(font);

        Color primaryColor = new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200));
        Color secondaryColor = new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200));
        Color arrowColor = new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200));
        Color markColor = new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200));


        g.setColor(primaryColor);
        int radius = (int) (Math.min(img.getWidth(), img.getHeight()) * 0.75) / 2;
        GraphicsUtil.drawTransform(g, posX, posY, (gdi) -> {
            gdi.drawOval(-radius, -radius, radius * 2, radius * 2);
        });

        g.setColor(markColor);
        for (int i = 0; i < splitCount; i++) {
            String text = numbers.get(i) + "";
            double rate = i * 1.0 / splitCount;
            double angle = rate * 360;

            double radian = Math.toRadians(angle);
            double rotate = Math.toRadians(-angle);

            double bx = radius * 0.9 * Math.sin(radian);
            double by = radius * 0.9 * Math.cos(radian);

            double ex = radius * 1.1 * Math.sin(radian);
            double ey = radius * 1.1 * Math.cos(radian);

            boolean gap = i % 2 == 0;
            GraphicsUtil.drawTransform(g, posX, posY, (gdi) -> {
                if (gap) {
                    gdi.setColor(secondaryColor);
                } else {
                    gdi.setColor(primaryColor);
                }
                gdi.drawLine((int) bx, (int) by, (int) ex, (int) ey);
            });
            GraphicsUtil.drawCenterString(g, text, posX, posY, (trans, gdi) -> {
                double ox = radius * 1.1 * Math.sin(radian);
                double oy = radius * 1.1 * Math.cos(radian);
                if (gap) {
                    ox = radius * 1.2 * Math.sin(radian);
                    oy = radius * 1.2 * Math.cos(radian);
                }
                if (gap) {
                    gdi.setColor(secondaryColor);
                } else {
                    gdi.setColor(primaryColor);
                }
                trans.translate(ox, oy);
                trans.rotate(rotate);
            });
        }

        double angle = targetIndex * 1.0 / splitCount * 360;
        double radian = Math.toRadians(angle);
        double bx = radius * 0.8 * Math.sin(radian);
        double by = radius * 0.8 * Math.cos(radian);
        GraphicsUtil.drawTransform(g, posX, posY, (gdi) -> {
            gdi.setColor(arrowColor);
            GraphicsUtil.drawArrow(gdi, 0, 0, (int) bx, (int) by);
        });

        Font centerFont = new Font(null, Font.BOLD, fontWidth * 4);
        g.setFont(centerFont);
        GraphicsUtil.drawArtString(g, addNumber + "", posX, posY, true);


        // draw shuffle line
        int shuffleCount = MathUtil.RANDOM.nextInt(10) + 20;
        for (int i = 0; i < shuffleCount; i++) {
            g.setColor(new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200)));

            if (MathUtil.RANDOM.nextDouble() < 0.7) {
                g.drawLine(MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2,
                        MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2);
            } else {
                g.drawOval(MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2,
                        MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2);
            }
        }

        int result = numbers.get(targetIndex) + addNumber;
        ret.setQuestion("请计算指针指向的数值加上中心的数值的结果");
        ret.setImg(img);
        ret.setResult(result + "");
        return ret;
    }
}
