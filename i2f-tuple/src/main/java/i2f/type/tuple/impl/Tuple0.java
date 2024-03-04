package i2f.type.tuple.impl;

import i2f.type.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/4 9:01
 * @desc
 */
public class Tuple0 implements Tuple {
    public static final int SIZE = 0;

    public static final Tuple0 INSTANCE = new Tuple0();

    public Tuple0() {
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
        return null;
    }

    @Override
    public void set(int index, Object value) {
        if (index >= SIZE || index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Tuple0)) {
            return false;
        }

        Tuple0 other = (Tuple0) obj;
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s()",
                this.getClass().getSimpleName());
    }
}
