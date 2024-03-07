package i2f.stream.timed;

import i2f.stream.Streaming;
import i2f.stream.window.TimeWindowInfo;
import i2f.stream.window.ViewTimeWindowInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/6 16:26
 * @desc
 */
public interface TimedStreaming<E> extends Streaming<Map.Entry<Long,E>> {

    TimedStreaming<E> timeOrdered(long maxDelayMillSeconds);

    default Streaming<Map.Entry<List<Map.Entry<Long,E>>, TimeWindowInfo>> timeWindow(long windowMillSeconds){
        return slideTimeWindow(windowMillSeconds,windowMillSeconds);
    }

    Streaming<Map.Entry<List<Map.Entry<Long,E>>, TimeWindowInfo>> slideTimeWindow(long windowMillSeconds, long slideMillSeconds);

    Streaming<Map.Entry<List<Map.Entry<Long,E>>,TimeWindowInfo>> sessionTimeWindow(long sessionTimeoutMillSeconds);

    Streaming<Map.Entry<List<Map.Entry<Long,E>>, ViewTimeWindowInfo>> viewTimeWindow(long beforeMillSeconds, long afterMillSeconds);
}
