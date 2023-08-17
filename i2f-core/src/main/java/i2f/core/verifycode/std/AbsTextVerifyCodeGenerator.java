package i2f.core.verifycode.std;

import i2f.core.verifycode.consts.VerifyCodeType;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.data.VerifyCodeStdDto;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/17 15:52
 * @desc 适用于进行文本比较类型
 * 适用类型：
 * 输入验证码类型
 * 看图答题类型
 * 看图计算类型
 */
public abstract class AbsTextVerifyCodeGenerator implements IVerifyCodeGenerator {
    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeStdDto<String> dto = generateInner(width, height, params);
        VerifyCodeDto ret = new VerifyCodeDto();
        ret.setType(VerifyCodeType.INPUT.code());
        ret.setImg(dto.getImg());
        ret.setQuestion(dto.getQuestion());
        ret.setResult(dto.getResult());
        return ret;
    }

    protected abstract VerifyCodeStdDto<String> generateInner(int width, int height, Map<String, Object> params);

    @Override
    public boolean verify(String result, String answer) {
        if (result == null || answer == null) {
            return false;
        }
        return result.equalsIgnoreCase(answer);
    }
}
