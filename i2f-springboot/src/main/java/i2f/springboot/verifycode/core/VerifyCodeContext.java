package i2f.springboot.verifycode.core;

import i2f.core.cache.ICache;
import i2f.core.verifycode.core.MathUtil;
import i2f.core.verifycode.data.VerifyCodeAnswerDto;
import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.data.VerifyCodeQuestionDto;
import i2f.core.verifycode.std.IVerifyCodeGenerator;
import i2f.springboot.verifycode.VerifyCodeConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/15 17:19
 * @desc
 */
@Component
public class VerifyCodeContext implements ApplicationContextAware {

    @Autowired
    private VerifyCodeConfig config;

    @Autowired
    private ICache<String, Object> cache;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public VerifyCodeQuestionDto make() throws IOException {
        Map<String, IVerifyCodeGenerator> beans = getBeans(IVerifyCodeGenerator.class);
        List<IVerifyCodeGenerator> generatorList = new ArrayList<>(beans.values());
        int idx = MathUtil.RANDOM.nextInt(generatorList.size());
        IVerifyCodeGenerator generator = generatorList.get(idx);
        VerifyCodeDto dto = generator.generate(0, 0, null);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        String code = config.getCacheKeyPrefix() + uuid;

        cache.set(code, dto.getResult(), config.getExpireSeconds(), TimeUnit.SECONDS);

        return VerifyCodeQuestionDto.make(dto, code);
    }

    public boolean verify(VerifyCodeAnswerDto dto) {
        if (dto.getCode() == null) {
            return false;
        }
        if (dto.getResult() == null) {
            return false;
        }
        Object result = cache.get(dto.getCode());
        cache.remove(dto.getCode());
        if (result == null) {
            return false;
        }
        String rs = String.valueOf(result);
        if (config.isIgnoreCase()) {
            if (rs.equalsIgnoreCase(dto.getResult())) {
                return true;
            }
        }
        if (rs.equals(dto.getResult())) {
            return true;
        }

        return false;
    }

    public <T> Map<String, T> getBeans(Class<T> clazz) {
        Map<String, T> ret = new LinkedHashMap<>();
        String[] names = context.getBeanNamesForType(clazz);
        for (String name : names) {
            ret.put(name, (T) context.getBean(name));
        }
        return ret;
    }
}
