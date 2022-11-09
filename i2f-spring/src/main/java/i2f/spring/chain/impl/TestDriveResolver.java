package i2f.spring.chain.impl;

import i2f.spring.chain.AbsChainResolver;
import i2f.spring.chain.ChainAction;
import i2f.spring.chain.ChainContext;
import i2f.spring.chain.IChainResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/9/20 15:46
 * @desc 测试
 * A->B|C
 * B-D
 */
@Slf4j
@Component
public class TestDriveResolver extends AbsChainResolver implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private TestAChainResolver aChainResolver;

    @Autowired
    private TestBtAChainResolver btAChainResolver;

    @Override
    public void task(IChainResolver parent, Object action, Object params, ChainContext chainContext) {
        log.info(this.getClass().getSimpleName() + ":task:" + params);
        next(ChainAction.AFTER, "drive", true, true, null);
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        task(this, ChainAction.RUN, "self", null);
    }

}
