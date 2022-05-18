package test;

import i2f.core.zplugin.inject.annotations.Injects;
import i2f.core.zplugin.log.annotations.Log;
import i2f.core.zplugin.log.enums.LogLevel;
import i2f.core.zplugin.validate.impl.VBean;
import i2f.core.zplugin.validate.impl.VNotType;
import test.model.TestBean;

/**
 * @author ltb
 * @date 2022/3/25 21:00
 * @desc
 */
@Log(system = "test",module = "log",label = "unit",level = LogLevel.INFO)
public interface ITest {
    String say(@VNotType(String.class) Object str);

    @Log(system = "test",module="log",label = "bean",level = LogLevel.WARN)
    String bean(@VBean @Injects(fields = {"sex","age","account"},providerNames = "testBean") TestBean obj);
}
