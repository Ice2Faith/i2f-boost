package i2f.spring.chain.impl;

import i2f.spring.chain.AbsChainResolver;
import i2f.spring.chain.ChainAction;
import i2f.spring.chain.ChainContext;
import i2f.spring.chain.IChainResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/9/20 15:28
 * @desc
 */
@Slf4j
@Component
public class TestCtAChainResolver extends AbsChainResolver {
    @Override
    public Set<Class<? extends IChainResolver>> attach() {
        return new HashSet<>(Arrays.asList(TestAChainResolver.class));
    }

    @Override
    public void task(IChainResolver parent, Object action, Object params, ChainContext chainContext) {
        log.info(this.getClass().getSimpleName() + ":task:" + params);
        next(ChainAction.AFTER, "[" + this.getClass().getSimpleName() + ":ret]", true, false, null);
    }
}
