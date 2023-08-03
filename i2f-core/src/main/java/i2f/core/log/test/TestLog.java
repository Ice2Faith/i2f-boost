package i2f.core.log.test;

import i2f.core.log.annotations.Log;
import i2f.core.log.core.LogContext;
import i2f.core.log.logger.Lg;
import i2f.core.log.logger.Logger;
import i2f.core.log.logger.LoggerFactory;

/**
 * @author Ice2Faith
 * @date 2023/8/2 18:26
 * @desc
 */
@Log(module = "测试日志")
public class TestLog {
    public static Logger logger= LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) throws InterruptedException {
        LogContext.initialed.set(true);

        logger.info("主方法","日志输出");

        Lg.info("静态调用","没有内容");
        System.out.println("================");
    }
}
