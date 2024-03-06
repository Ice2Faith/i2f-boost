package i2f.stream.timed;

import i2f.stream.Streaming;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/6 16:26
 * @desc
 */
public interface TimedStreaming<E> extends Streaming<Map.Entry<Long,E>> {

    TimedStreaming<E> timeOrdered(long maxDelayMillSeconds);

    default Streaming<Map.Entry<List<Map.Entry<Long,E>>,Map.Entry<Long,Long>>> timeWindow(long windowMillSeconds){
        return slideTimeWindow(windowMillSeconds,windowMillSeconds);
    }

    Streaming<Map.Entry<List<Map.Entry<Long,E>>,Map.Entry<Long,Long>>> slideTimeWindow(long windowMillSeconds,long slideMillSeconds);

    Streaming<Map.Entry<List<Map.Entry<Long,E>>,Map.Entry<Long,Long>>> sessionTimeWindow(long sessionTimeoutMillSeconds);
}
