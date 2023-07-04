package i2f.springboot.security.def;

import i2f.springboot.security.impl.AbstractAuthorizeExceptionHandler;
import i2f.springboot.security.impl.AuthorizeExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/7 10:28
 * @desc
 */
@ConditionalOnMissingBean(AuthorizeExceptionHandler.class)
@Slf4j
@Component
public class DefaultAuthorizeExceptionHandler extends AbstractAuthorizeExceptionHandler {

}
