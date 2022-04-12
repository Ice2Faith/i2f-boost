package i2f.springboot.security.def;

import i2f.springboot.security.impl.AbstractUnAuthorizedHandler;
import i2f.springboot.security.impl.UnAuthorizedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/7 10:28
 * @desc
 */
@ConditionalOnMissingBean(UnAuthorizedHandler.class)
@Slf4j
@Component
public class DefaultUnAuthorizedHandler extends AbstractUnAuthorizedHandler {

}
