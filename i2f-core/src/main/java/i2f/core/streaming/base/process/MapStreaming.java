package i2f.core.streaming.base.process;

import i2f.core.functional.common.IMapper;
import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class MapStreaming<E, R> extends AbsStreaming<R, E> {
    private IMapper<R, E> mapper;

    public MapStreaming(IMapper<R, E> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> apply(Iterator<E> iterator) {
        List<R> ret = new LinkedList<R>();
        while (iterator.hasNext()) {
            E item = iterator.next();
            R cvt = mapper.get(item);
            ret.add(cvt);
        }
        return ret.iterator();
    }
}
