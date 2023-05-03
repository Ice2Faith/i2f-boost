package i2f.core.streaming.iterator;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author Ice2Faith
 * @date 2023/5/2 23:47
 * @desc
 */
public class OptionalDecodeIterator<E> extends DecodeIterator<Optional<E>, E> {

    public OptionalDecodeIterator(Iterator<Optional<E>> iterator) {
        super((opt) -> opt.orElse(null), iterator);
    }

}
