package i2f.core.verifycode.std;

import i2f.core.verifycode.consts.VerifyCodeType;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.data.VerifyCodeStdDto;

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
public abstract class AbsPositionD1VerifyCodeGenerator implements IVerifyCodeGenerator {

    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<Double> dto = generateInner(width, height, params);
        VerifyCodeDto ret = new VerifyCodeDto();
        ret.setType(VerifyCodeType.D1.code());
        ret.setCount(dto.getCount());
        ret.setImg(dto.getImg());
        ret.setQuestion(dto.getQuestion());
        ret.setResult(String.format("%.2f", dto.getResult()));
        return ret;
    }

    @Override
    public boolean verify(String result, String answer) {
        if (result == null || answer == null) {
            return false;
        }
        try {
            Double resultNum = Double.valueOf(result);
            Double answerNum = Double.valueOf(answer);
            return verifyInner(resultNum, answerNum);
        } catch (Exception e) {

        }
        return false;
    }

    public boolean verifyInner(Double resultNum, Double answerNum) {
        double diffRate = Math.abs(resultNum - answerNum);
        return diffRate < 5;
    }

    public abstract VerifyCodeStdDto<Double> generateInner(int width, int height, Map<String, Object> params);
}
