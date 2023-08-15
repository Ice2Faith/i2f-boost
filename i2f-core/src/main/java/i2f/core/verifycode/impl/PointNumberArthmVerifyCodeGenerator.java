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
public class PointNumberArthmVerifyCodeGenerator implements IVerifyCodeGenerator {

    public static final String PARAM_SPLIT_COUNT = "splitCount";
    public static final String PARAM_BOUND_NUMBER = "boundNumber";

    public static final int DEFAULT_SPLIT_COUNT = 15;
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
        int[] targetIndexArr = new int[2];
        for (int i = 0; i < splitCount; i++) {
            int num = MathUtil.RANDOM.nextInt(boundNumber);
            numbers.add(num);
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        targetIndexArr[0] = MathUtil.RANDOM.nextInt(splitCount);
        targetIndexArr[1] = MathUtil.RANDOM.nextInt(splitCount);

        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);


        int fontWidth = Math.min(width, height) / 15;
        Font font = new Font(null, Font.ITALIC, fontWidth);
        g.setFont(font);


        for (int i = 0; i < splitCount; i++) {
            String text = numbers.get(i) + "";

            int posX = MathUtil.RANDOM.nextInt(width);
            int posY = MathUtil.RANDOM.nextInt(height);

            g.setColor(new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200)));
            GraphicsUtil.drawCenterString(g, text, posX, posY, (trans, gdi) -> {
                trans.rotate((MathUtil.RANDOM.nextDouble() - 0.5) * (Math.PI / 4));
                trans.scale(MathUtil.RANDOM.nextDouble() + 0.5, MathUtil.RANDOM.nextDouble() + 0.5);
                trans.shear(MathUtil.RANDOM.nextDouble() * 0.5, MathUtil.RANDOM.nextDouble() * 0.5);

            });

            int fh = g.getFontMetrics().getHeight();
            for (int j = 0; j < targetIndexArr.length; j++) {
                if (targetIndexArr[j] != i) {
                    continue;
                }
                int bx = MathUtil.RANDOM.nextInt(width);
                int by = MathUtil.RANDOM.nextInt(height);

                double radian = GraphicsUtil.awtRadian(bx, by, posX, posY);
                double dis = MathUtil.distance(posX, posY, bx, by);

                dis -= fh;

                double ex = bx + dis * Math.sin(radian);
                double ey = by + dis * Math.cos(radian);

                g.setColor(new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200)));
                GraphicsUtil.drawArrow(g, bx, by, (int) ex, (int) ey);
            }
        }


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

        int result = numbers.get(targetIndexArr[0]) + numbers.get(targetIndexArr[1]);
        ret.setQuestion("请计算两个指针指向数字相加的结果");
        ret.setImg(img);
        ret.setResult(result + "");
        return ret;
    }
}
