package i2f.core.lang.spi.test;

import i2f.core.context.IAppContext;
import i2f.core.lang.spi.SpiComponentFactory;

public class TestSpiComponentFactory implements SpiComponentFactory<TestUser> {

    @Override
    public Class<?> type() {
        return TestUser.class;
    }

    @Override
    public TestUser build(IAppContext context) {
        return new TestUser("admin","123456");
    }
}
