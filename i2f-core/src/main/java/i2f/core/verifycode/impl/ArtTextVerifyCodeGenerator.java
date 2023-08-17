package i2f.core.verifycode.impl;

import i2f.core.verifycode.core.GraphicsUtil;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeStdDto;
import i2f.core.verifycode.std.AbsTextVerifyCodeGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:35
 * @desc
 */
public class ArtTextVerifyCodeGenerator extends AbsTextVerifyCodeGenerator {

    public static final String PARAM_CHAR_LENGTH = "charLength";
    public static final String PARAM_NUMBER_ONLY = "numberOnly";

    public static final int DEFAULT_CHAR_LENGTH = 4;
    public static final boolean DEFAULT_NUMBER_ONLY = false;
    public static final int DEFAULT_WIDTH = 360;
    public static final int DEFAULT_HEIGHT = 160;

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
    public VerifyCodeStdDto<String> generateInner(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<String> ret = new VerifyCodeStdDto<>();

        if (width <= 0) {
            width = DEFAULT_WIDTH;
        }
        if (height <= 0) {
            height = DEFAULT_HEIGHT;
        }
        int charLen = DEFAULT_CHAR_LENGTH;
        boolean numberOnly = DEFAULT_NUMBER_ONLY;
        if (params != null) {
            if (params.containsKey(PARAM_CHAR_LENGTH)) {
                Integer val = (Integer) params.get(PARAM_CHAR_LENGTH);
                if (val != null && val > 0) {
                    charLen = val;
                }
            }
            if (params.containsKey(PARAM_NUMBER_ONLY)) {
                Boolean val = (Boolean) params.get(PARAM_NUMBER_ONLY);
                if (val != null) {
                    numberOnly = val;
                }
            }
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        String result = makeCode(charLen, numberOnly);

        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int fontWidth = (int) (width / result.length());
        Font font = new Font(null, Font.ITALIC, fontWidth);
        g.setFont(font);
        GraphicsUtil.drawArtString(g, result, width / 2, height / 2, true,
                GraphicsUtil::defaultArtStringGdiConsumer,
                (trans, gdi) -> {
                    trans.rotate((MathUtil.RANDOM.nextDouble() - 0.5) * (Math.PI / 4));
                    trans.scale(MathUtil.RANDOM.nextDouble() * 0.8 + 0.5, MathUtil.RANDOM.nextDouble() * 0.8 + 0.5);
                    trans.shear(MathUtil.RANDOM.nextDouble() * 0.8, MathUtil.RANDOM.nextDouble() * 0.8);

                    int fh = (int) (gdi.getFontMetrics().getHeight() * 0.2);
                    trans.translate(0, (MathUtil.RANDOM.nextDouble() - 0.5) * fh);
                },
                (wid) -> fontWidth * 1.0);

        // draw shuffle line
        int shuffleCount = MathUtil.RANDOM.nextInt(10) + 20;
        for (int i = 0; i < shuffleCount; i++) {
            g.setColor(new Color(MathUtil.RANDOM.nextInt(180), MathUtil.RANDOM.nextInt(180), MathUtil.RANDOM.nextInt(180)));

            if (MathUtil.RANDOM.nextDouble() < 0.7) {
                g.drawLine(MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2,
                        MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2);
            } else {
                g.drawOval(MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2,
                        MathUtil.RANDOM.nextInt(width * 2) - width / 2, MathUtil.RANDOM.nextInt(height * 2) - height / 2);
            }
        }

        ret.setQuestion("请输入图片中的" + charLen + "位字符");
        ret.setImg(img);
        ret.setResult(result);
        return ret;
    }
}
