package i2f.springboot.verifycode.adapter;

import i2f.core.exception.BoostException;
import i2f.core.verifycode.data.VerifyCodeAnswerDto;
import i2f.springboot.security.impl.BeforeLoginChecker;
import i2f.springboot.verifycode.core.VerifyCodeContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/15 19:21
 * @desc
 */
@Data
@ConditionalOnExpression("${i2f.springboot.config.verifycode.adapt.security-login-check.enable:true}")
@ConfigurationProperties(prefix = "i2f.springboot.config.verifycode.adapt.security-login-check")
@Component
public class VerifyCodeBeforeLoginChecker implements BeforeLoginChecker {
    private String codeName = "code";
    private String resultName = "result";

    @Autowired
    private VerifyCodeContext verifyCodeContext;

    @Override
    public void onJsonLogin(String username, String password, Map<String, Object> params, HttpServletRequest request) throws Exception {
        Object code = params.get(codeName);
        Object result = params.get(resultName);
        verifyVerifyCode(code, result);
    }

    @Override
    public void onFormLogin(String username, String password, HttpServletRequest request) throws Exception {
        String code = request.getParameter(codeName);
        String result = request.getParameter(resultName);
        verifyVerifyCode(code, result);
    }

    public void verifyVerifyCode(Object code, Object result) {
        if (code == null) {
            throw new BoostException("无效的验证码请求");
        }
        if (result == null) {
            throw new BoostException("请填写验证码");
        }
        VerifyCodeAnswerDto dto = new VerifyCodeAnswerDto();
        dto.setCode(String.valueOf(code));
        dto.setResult(String.valueOf(result));
        boolean ok = verifyCodeContext.verify(dto);
        if (!ok) {
            throw new BoostException("验证码验证失败，请刷新验证码后重试");
        }
    }
}
