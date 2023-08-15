package i2f.springboot.verifycode.core;

import i2f.core.std.api.ApiResp;
import i2f.core.verifycode.data.VerifyCodeQuestionDto;
import i2f.springboot.limit.annotations.Limit;
import i2f.springboot.limit.consts.LimitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/15 18:58
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.verifycode.api.enable:true}")
@RestController
@RequestMapping("verifycode")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeContext verifyCodeContext;

    @Limit(type = LimitType.IP, window = 1, unit = TimeUnit.MINUTES, count = 10)
    @RequestMapping("refresh")
    public ApiResp<?> refresh() throws Exception {
        VerifyCodeQuestionDto ret = verifyCodeContext.make();
        return ApiResp.success(ret);
    }
}
