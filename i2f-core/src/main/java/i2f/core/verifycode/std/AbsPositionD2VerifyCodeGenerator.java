package i2f.core.verifycode.std;

import i2f.core.graphics.d2.Point;
import i2f.core.verifycode.consts.VerifyCodeType;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.data.VerifyCodeStdDto;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/17 15:54
 * @desc 适用于二维相似数值比较，默认按照百分比进行
 * 因此要求result的只隶属区间[0,100]
 * 因为是二维，也就需要两个维度X,Y
 * 为什么使用百分比？
 * 因为，图片一旦经过缩放
 * 则使用坐标会产生很大的问题
 * 适用类型：
 * 点击符号的位置（二维，X,Y）
 */
public abstract class AbsPositionD2VerifyCodeGenerator implements IVerifyCodeGenerator {

    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<Point> dto = generateInner(width, height, params);
        VerifyCodeDto ret = new VerifyCodeDto();
        ret.setType(VerifyCodeType.D2.code());
        ret.setImg(dto.getImg());
        ret.setQuestion(dto.getQuestion());
        ret.setResult(String.format("%.2f,%.2f", dto.getResult().getX(), dto.getResult().getY()));
        return ret;
    }

    @Override
    public boolean verify(String result, String answer) {
        if (result == null || answer == null) {
            return false;
        }
        try {
            Point resultPoint = parsePoint(result);
            Point answerPoint = parsePoint(answer);
            return verifyInner(resultPoint, answerPoint);
        } catch (Exception e) {

        }
        return false;
    }

    public Point parsePoint(String str) {
        String[] arr = str.split(",");
        if (arr.length != 2) {
            return null;
        }
        double x = Double.parseDouble(arr[0]);
        double y = Double.parseDouble(arr[1]);
        return new Point(x, y);
    }

    public boolean verifyInner(Point resultPoint, Point answerPoint) {
        double diffDis = MathUtil.distance(answerPoint.getX(), answerPoint.getY(),
                resultPoint.getX(), resultPoint.getY());
        return diffDis < 8;
    }

    public abstract VerifyCodeStdDto<Point> generateInner(int width, int height, Map<String, Object> params);
}
