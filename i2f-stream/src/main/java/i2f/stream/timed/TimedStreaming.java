package i2f.stream.timed;

import i2f.stream.Streaming;
import i2f.stream.impl.SimpleEntry;
import i2f.stream.index.DecimalIndex;
import i2f.stream.window.TimeWindowInfo;
import i2f.stream.window.ViewTimeWindowInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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

    default Streaming<Map.Entry<Map.Entry<Long,E>, BigDecimal>> latelyAverageTime(long latelyMillSeconds,
                                                              int scale,
                                                              Function<E,BigDecimal> valueMapper){
        return latelyAggregateTime(latelyMillSeconds,
                ()->new SimpleEntry<BigDecimal,BigDecimal>(BigDecimal.ZERO,BigDecimal.ZERO),
                (t,e)->{
                    t.setKey(t.getKey().add(valueMapper.apply(e)));
                    t.setValue(t.getValue().add(BigDecimal.ONE));
                    return t;
                },(e)->e.getKey().divide(e.getValue(),scale, RoundingMode.HALF_UP));
    }

    default Streaming<Map.Entry<Map.Entry<Long,E>, DecimalIndex>> latelyIndexTime(long latelyMillSeconds,
                                                              int scale,
                                                              Function<E,BigDecimal> valueMapper){
        return latelyAggregateTime(latelyMillSeconds,
                ()->{
                    DecimalIndex index=new DecimalIndex();
                    index.setAvg(BigDecimal.ZERO);
                    index.setCount(BigDecimal.ZERO);
                    index.setSum(BigDecimal.ZERO);
                    return index;
                },
                (t,e)->{
                    BigDecimal val = valueMapper.apply(e);
                    t.setSum(t.getSum().add(val));
                    t.setCount(t.getCount().add(BigDecimal.ONE));
                    if(t.getMin()==null){
                        t.setMin(val);
                    }else{
                        t.setMin(t.getMin().min(val));
                    }
                    if(t.getMax()==null){
                        t.setMax(val);
                    }else{
                        t.setMax(t.getMax().max(val));
                    }
                    return t;
                },(e)->{
                    e.setAvg(e.getSum().divide(e.getCount(),scale, RoundingMode.HALF_UP));
                    return e;
                });
    }

    <T,R> Streaming<Map.Entry<Map.Entry<Long,E>,R>> latelyAggregateTime(long latelyMillSeconds,
                                                    Supplier<T> firstSupplier,
                                                    BiFunction<T,E,T> accumulator,
                                                    Function<T,R> finisher);
}
