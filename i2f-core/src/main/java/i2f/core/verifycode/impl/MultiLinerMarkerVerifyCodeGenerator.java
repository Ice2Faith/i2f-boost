package i2f.core.verifycode.impl;

import i2f.core.verifycode.core.GraphicsUtil;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeStdDto;
import i2f.core.verifycode.std.AbsPositionD1MultiVerifyCodeGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:35
 * @desc
 */
public class MultiLinerMarkerVerifyCodeGenerator extends AbsPositionD1MultiVerifyCodeGenerator {

    public static final String PARAM_SPLIT_COUNT = "splitCount";
    public static final String PARAM_VERIFY_COUNT = "verifyCount";

    public static final int DEFAULT_SPLIT_COUNT = 10;
    public static final int DEFAULT_VERIFY_COUNT = 3;

    public static final int DEFAULT_WIDTH = 480;
    public static final int DEFAULT_HEIGHT = 120;

    private Supplier<String> markerSupplier = () -> String.valueOf(MathUtil.RANDOM.nextInt(100));

    public MultiLinerMarkerVerifyCodeGenerator() {
    }

    public MultiLinerMarkerVerifyCodeGenerator(Supplier<String> markerSupplier) {
        this.markerSupplier = markerSupplier;
    }

    @Override
    public VerifyCodeStdDto<List<Double>> generateInner(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<List<Double>> ret = new VerifyCodeStdDto<>();

        if (width <= 0) {
            width = DEFAULT_WIDTH;
        }
        if (height <= 0) {
            height = DEFAULT_HEIGHT;
        }

        int splitCount = DEFAULT_SPLIT_COUNT;
        int verifyCount = DEFAULT_VERIFY_COUNT;
        if (params != null) {
            if (params.containsKey(PARAM_SPLIT_COUNT)) {
                Integer val = (Integer) params.get(PARAM_SPLIT_COUNT);
                if (val != null && val > 0) {
                    splitCount = val;
                }
            }
            if (params.containsKey(PARAM_VERIFY_COUNT)) {
                Integer val = (Integer) params.get(PARAM_VERIFY_COUNT);
                if (val != null && val > 0) {
                    verifyCount = val;
                }
            }
        }

        int dynamicCount = (int) ((MathUtil.RANDOM.nextDouble() * 0.5) * splitCount);
        dynamicCount = Math.min(dynamicCount, 20);
        splitCount = (int) (splitCount + dynamicCount);
        Set<String> numbersSet = new LinkedHashSet<>(splitCount);
        for (int i = 0; i < splitCount; i++) {
            String val = markerSupplier.get();
            numbersSet.add(val);
        }

        List<String> numbers = new ArrayList<>(numbersSet);
        splitCount = numbers.size();

        verifyCount = verifyCount + MathUtil.RANDOM.nextInt(2);
        Set<Integer> targetIndexSet = new LinkedHashSet<>();
        for (int i = 0; i < verifyCount; i++) {
            targetIndexSet.add(MathUtil.RANDOM.nextInt(splitCount));
        }

        List<Integer> targetIndexes = new ArrayList<>(targetIndexSet);
        verifyCount = targetIndexes.size();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);


        int fontWidth = width / 15;
        Font font = new Font(null, Font.ITALIC, fontWidth);
        g.setFont(font);


        List<Integer> targetPoints = new ArrayList<>();
        int stepWidth = width / splitCount;
        for (int i = 0; i < splitCount; i++) {
            String text = numbers.get(i) + "";

            int posX = stepWidth * i;
            int posY = height / 2;

            g.setColor(new Color(MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200), MathUtil.RANDOM.nextInt(200)));
            GraphicsUtil.drawCenterString(g, text, posX, posY, (trans, gdi) -> {
                trans.rotate((MathUtil.RANDOM.nextDouble() - 0.5) * (Math.PI / 4));
                trans.scale(MathUtil.RANDOM.nextDouble() + 0.5, MathUtil.RANDOM.nextDouble() + 0.5);
                trans.shear(MathUtil.RANDOM.nextDouble() * 0.5, MathUtil.RANDOM.nextDouble() * 0.5);
                int fh = (int) (gdi.getFontMetrics().getHeight());
                trans.translate(0, (MathUtil.RANDOM.nextDouble() - 0.5) * fh);
            });

            if (targetIndexes.contains(i)) {
                targetPoints.add(posX);
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

        StringBuilder builder = new StringBuilder();
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < verifyCount; i++) {
            Integer px = targetPoints.get(i);
            result.add(px * 100.0 / width);
            builder.append(",").append(numbers.get(targetIndexes.get(i)));
        }
        String str = builder.toString();
        if (!"".equals(str)) {
            str = str.substring(1);
        }
        ret.setCount(verifyCount);
        ret.setQuestion("请按顺序点击[" + str + "]所在的位置");
        ret.setImg(img);
        ret.setResult(result);
        return ret;
    }
}
