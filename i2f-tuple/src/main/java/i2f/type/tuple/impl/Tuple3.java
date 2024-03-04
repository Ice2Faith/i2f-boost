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
public class Tuple3<V1, V2, V3> implements Tuple {
    public static final int SIZE = 3;

    public V1 v1;
    public V2 v2;
    public V3 v3;

    public Tuple3() {
    }

    public Tuple3(V1 v1) {
        this.v1 = v1;
    }

    public Tuple3(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Tuple3(V1 v1, V2 v2, V3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
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
            case 2:
                return v3;
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
            case 2:
                v3 = (V3) value;
                break;
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(v1, v2, v3));
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

    public V3 getV3() {
        return v3;
    }

    public void setV3(V3 v3) {
        this.v3 = v3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(v1, tuple3.v1) &&
                Objects.equals(v2, tuple3.v2) &&
                Objects.equals(v3, tuple3.v3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3);
    }

    @Override
    public String toString() {
        return String.format("%s(v1=%s, v2=%s, v3=%s)",
                this.getClass().getSimpleName(),
                String.valueOf(v1),
                String.valueOf(v2),
                String.valueOf(v3));
    }
}
