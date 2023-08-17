package i2f.core.verifycode.std;

import i2f.core.graphics.d2.Point;
import i2f.core.verifycode.consts.VerifyCodeType;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.data.VerifyCodeStdDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/17 15:54
 * @desc 适用于一维相似数值比较，默认按照百分比进行
 * 因此要求result的只隶属区间[0,100]
 * 为什么使用百分比？
 * 因为，图片一旦经过缩放
 * 则使用坐标会产生很大的问题
 * 适用类型：
 * 点击符号的位置（仅X轴）
 */
public abstract class AbsPositionD2MultiVerifyCodeGenerator implements IVerifyCodeGenerator {

    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<List<Point>> dto = generateInner(width, height, params);
        VerifyCodeDto ret = new VerifyCodeDto();
        ret.setType(VerifyCodeType.D1.code());
        ret.setImg(dto.getImg());
        ret.setQuestion(dto.getQuestion());
        StringBuilder result = new StringBuilder();
        for (Point num : dto.getResult()) {
            result.append(";").append(String.format("%.2f,%.2f", num.getX(), num.getY()));
        }
        String str = result.toString();
        if (!"".equals(str)) {
            str = str.substring(1);
        }
        ret.setResult(str);
        return ret;
    }

    @Override
    public boolean verify(String result, String answer) {
        if (result == null || answer == null) {
            return false;
        }
        try {
            List<Point> resultNum = parsePoints(result);
            List<Point> answerNum = parsePoints(answer);
            return verifyInner(resultNum, answerNum);
        } catch (Exception e) {

        }
        return false;
    }

    public List<Point> parsePoints(String str) {
        List<Point> ret = new ArrayList<>();
        if (str == null) {
            return ret;
        }
        String[] arr = str.split(";");
        for (String item : arr) {
            try {
                Point point = parsePoint(item);
                if (point != null) {
                    ret.add(point);
                }
            } catch (Exception e) {

            }
        }
        return ret;
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

    public boolean verifyInner(List<Point> resultNum, List<Point> answerNum) {
        int rs = resultNum.size();
        int as = answerNum.size();
        if (as < rs) {
            return false;
        }
        for (int i = 0; i < rs; i++) {
            Point rnum = resultNum.get(i);
            Point anum = answerNum.get(i);
            double diffDis = MathUtil.distance(rnum.getX(), rnum.getY(),
                    anum.getX(), anum.getY());
            boolean ok = diffDis < 8;
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    public abstract VerifyCodeStdDto<List<Point>> generateInner(int width, int height, Map<String, Object> params);
}
