package i2f.type.tuple.impl;

import i2f.type.tuple.Tuple;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/4 9:01
 * @desc
 */
public class Tuple2<V1, V2> implements Tuple, Map.Entry<V1, V2> {
    public static final int SIZE = 2;

    public V1 v1;
    public V2 v2;

    public Tuple2() {
    }

    public Tuple2(V1 v1) {
        this.v1 = v1;
    }

    public Tuple2(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public Object get(int index) {
        if (index >= SIZE || index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
        switch (index) {
            case 0:
                return v1;
            case 1:
                return v2;
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public void set(int index, Object value) {
        if (index >= SIZE || index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
        switch (index) {
            case 0:
                v1 = (V1) value;
                break;
            case 1:
                v2 = (V2) value;
                break;
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(v1, v2));
    }

    public V1 getV1() {
        return v1;
    }

    public void setV1(V1 v1) {
        this.v1 = v1;
    }

    public V2 getV2() {
        return v2;
    }

    public void setV2(V2 v2) {
        this.v2 = v2;
    }

    public V1 setKey(V1 key) {
        V1 ret = this.v1;
        this.v1 = key;
        return ret;
    }

    @Override
    public V1 getKey() {
        return this.v1;
    }

    @Override
    public V2 getValue() {
        return this.v2;
    }

    @Override
    public V2 setValue(V2 value) {
        V2 ret = this.v2;
        this.v2 = value;
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(v1, tuple2.v1) &&
                Objects.equals(v2, tuple2.v2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2);
    }

    @Override
    public String toString() {
        return String.format("%s(v1=%s, v2=%s)",
                this.getClass().getSimpleName(),
                String.valueOf(v1),
                String.valueOf(v2));
    }
}
