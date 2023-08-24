package i2f.core.verifycode.impl;

import i2f.core.math.calculator.FormulaCalculator;
import i2f.core.verifycode.core.GraphicsUtil;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeStdDto;
import i2f.core.verifycode.std.AbsTextVerifyCodeGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:35
 * @desc
 */
public class MathCalcVerifyCodeGenerator extends AbsTextVerifyCodeGenerator {

    public static final String PARAM_CALC_COUNT = "calcCount";
    public static final String PARAM_BOUND_NUMBER = "boundNumber";

    public static final int DEFAULT_CALC_COUNT = 1;
    public static final int DEFAULT_BOUND_NUMBER = 10;

    public static final int DEFAULT_WIDTH = 240;
    public static final int DEFAULT_HEIGHT = 120;

    public MathCalcVerifyCodeGenerator() {
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

        int boundNumber = DEFAULT_BOUND_NUMBER;
        int calcCount = DEFAULT_CALC_COUNT;
        if (params != null) {
            if (params.containsKey(PARAM_BOUND_NUMBER)) {
                Integer val = (Integer) params.get(PARAM_BOUND_NUMBER);
                if (val != null && val > 0) {
                    boundNumber = val;
                }
            }
            if (params.containsKey(PARAM_CALC_COUNT)) {
                Integer val = (Integer) params.get(PARAM_CALC_COUNT);
                if (val != null && val > 0) {
                    calcCount = val;
                }
            }
        }

        calcCount += MathUtil.RANDOM.nextInt(2);


        StringBuilder formula = new StringBuilder();
        formula.append(MathUtil.RANDOM.nextInt(boundNumber));
        String[] opes = {"+", "-", "*", "/"};

        for (int i = 0; i < calcCount; i++) {
            String ope = opes[MathUtil.RANDOM.nextInt(opes.length)];
            int num = MathUtil.RANDOM.nextInt(boundNumber);
            if ("/".equals(ope)) {
                int cnt = 10;
                while (num == 0 && cnt > 0) {
                    num = MathUtil.RANDOM.nextInt(boundNumber);
                    cnt--;
                }
            }
            formula.append(ope).append(num);
        }

        String formulaText = formula.toString();
        BigDecimal result = new FormulaCalculator().calculate(formulaText);


        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);


        int fontWidth = width / 6;
        Font font = new Font(null, Font.ITALIC, fontWidth);
        g.setFont(font);

        int posX = width / 2;
        int posY = height / 2;

        g.setColor(new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200)));
        GraphicsUtil.drawCenterString(g, formulaText, posX, posY, (trans, gdi) -> {
            trans.rotate((MathUtil.RANDOM.nextDouble() - 0.5) * (Math.PI / 4));
            trans.scale(MathUtil.RANDOM.nextDouble() + 0.5, MathUtil.RANDOM.nextDouble() + 0.5);
            trans.shear(MathUtil.RANDOM.nextDouble() * 0.5, MathUtil.RANDOM.nextDouble() * 0.5);
            int fh = (int) (gdi.getFontMetrics().getHeight());
            trans.translate(0, (MathUtil.RANDOM.nextDouble() - 0.5) * fh);
        });


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

        ret.setQuestion("请计算表达式的值，结果最多两位小数");
        ret.setImg(img);
        ret.setResult(String.format("%.2f", result.doubleValue()));
        return ret;
    }

    @Override
    public boolean verify(String result, String answer) {
        if (result == null || answer == null) {
            return false;
        }
        try {
            double drs = Double.parseDouble(result);
            double das = Double.parseDouble(answer);
            return String.format("%.2f", drs).equals(String.format("%.2f", das));
        } catch (Exception e) {

        }
        return false;
    }
}
