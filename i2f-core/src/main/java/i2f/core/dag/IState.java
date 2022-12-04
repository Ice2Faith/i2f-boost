package i2f.core.dag;

import i2f.core.tuple.impl.Tuple2;

public interface IState<EVENT, STATE> {
    // 获取所有的事件和事件之后到达的状态
    Tuple2<EVENT, STATE>[] conversions();

    // 根据事件，进行转换到新状态
    default STATE transition(EVENT event) {
        Tuple2<EVENT, STATE>[] arr = conversions();
        for (Tuple2<EVENT, STATE> item : arr) {
            if (item.t1.equals(event)) {
                return item.t2;
            }
        }
        throw new IllegalStateException("current state(" + this + ") cannot transition event(" + event + ")");
    }
}
