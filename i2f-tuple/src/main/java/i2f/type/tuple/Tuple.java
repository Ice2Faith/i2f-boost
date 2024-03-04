package i2f.type.tuple;

import i2f.type.tuple.impl.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/4 8:57
 * @desc
 */
public interface Tuple extends Iterable<Object> {

    int size();

    Object get(int index);

    void set(int index, Object value);

    List<Object> toList();

    default boolean isEmpty() {
        return size() == 0;
    }

    default Object[] toArray() {
        return toList().toArray();
    }

    @Override
    default Iterator<Object> iterator() {
        return toList().iterator();
    }

    static Tuple0 of() {
        return Tuple0.INSTANCE;
    }

    static <V1> Tuple1<V1> of(V1 v1) {
        return new Tuple1<>(v1);
    }

    static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
        return new Tuple2<>(v1, v2);
    }

    static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
        return new Tuple3<>(v1, v2, v3);
    }

    static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> of(V1 v1, V2 v2, V3 v3, V4 v4) {
        return new Tuple4<>(v1, v2, v3, v4);
    }

    static <V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
        return new Tuple5<>(v1, v2, v3, v4, v5);
    }

    static <V1, V2, V3, V4, V5, V6> Tuple6<V1, V2, V3, V4, V5, V6> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) {
        return new Tuple6<>(v1, v2, v3, v4, v5, v6);
    }

    static <V1, V2, V3, V4, V5, V6, V7> Tuple7<V1, V2, V3, V4, V5, V6, V7> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        return new Tuple7<>(v1, v2, v3, v4, v5, v6, v7);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8> Tuple8<V1, V2, V3, V4, V5, V6, V7, V8> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) {
        return new Tuple8<>(v1, v2, v3, v4, v5, v6, v7, v8);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Tuple9<V1, V2, V3, V4, V5, V6, V7, V8, V9> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) {
        return new Tuple9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Tuple11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11) {
        return new Tuple11<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Tuple12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12) {
        return new Tuple12<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Tuple13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13) {
        return new Tuple13<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Tuple14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14) {
        return new Tuple14<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Tuple15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15) {
        return new Tuple15<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16> Tuple16<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16) {
        return new Tuple16<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17> Tuple17<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17) {
        return new Tuple17<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18> Tuple18<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18) {
        return new Tuple18<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18, V19> Tuple19<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18, V19> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18, V19 v19) {
        return new Tuple19<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19);
    }

    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18, V19, V20> Tuple20<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18, V19, V20> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18, V19 v19, V20 v20) {
        return new Tuple20<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20);
    }
}
