package i2f.core.streaming.base.sink;

import i2f.core.streaming.Streaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class StreamSinkStreaming<E> extends AbsSinkStreaming<Stream<E>, E, E> {

    public StreamSinkStreaming() {

    }

    @Override
    protected Stream<E> sink(Iterator<E> iterator) {
        LinkedList<E> list = Streaming.stream(iterator).collect(new LinkedList<E>());
        return list.stream();
    }
}
