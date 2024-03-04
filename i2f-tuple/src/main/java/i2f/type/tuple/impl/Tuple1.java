package i2f.type.tuple.impl;

import i2f.type.tuple.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/4 9:01
 * @desc
 */
public class Tuple1<V1> implements Tuple {
    public static final int SIZE = 1;

    public V1 v1;

    public Tuple1() {
    }

    public Tuple1(V1 v1) {
        this.v1 = v1;
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
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(v1));
    }

    public V1 getV1() {
        return v1;
    }

    public void setV1(V1 v1) {
        this.v1 = v1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple1<?> tuple1 = (Tuple1<?>) o;
        return Objects.equals(v1, tuple1.v1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1);
    }

    @Override
    public String toString() {
        return String.format("%s(v1=%s)",
                this.getClass().getSimpleName(),
                String.valueOf(v1));
    }
}
