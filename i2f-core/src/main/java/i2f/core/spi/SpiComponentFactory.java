package i2f.core.spi;

import i2f.core.context.IAppContext;

import java.util.Set;

public interface SpiComponentFactory<T> {
    default Set<Class<?>> requires(){
        return null;
    }
    Class<?> type();
    T build(IAppContext context);
}
