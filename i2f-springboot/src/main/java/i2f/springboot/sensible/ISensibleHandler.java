package i2f.springboot.sensible;

import java.util.Set;

public interface ISensibleHandler {
    Set<String> accept();

    Set<Class<?>> type();

    Object handle(Object obj, Sensible ann);
}
