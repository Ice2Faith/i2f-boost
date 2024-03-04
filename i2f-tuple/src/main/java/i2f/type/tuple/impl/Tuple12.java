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
public class Tuple12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> implements Tuple {
    public static final int SIZE = 12;

    public V1 v1;
    public V2 v2;
    public V3 v3;
    public V4 v4;
    public V5 v5;
    public V6 v6;
    public V7 v7;
    public V8 v8;
    public V9 v9;
    public V10 v10;
    public V11 v11;
    public V12 v12;

    public Tuple12() {
    }

    public Tuple12(V1 v1) {
        this.v1 = v1;
    }

    public Tuple12(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
        this.v10 = v10;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
        this.v10 = v10;
        this.v11 = v11;
    }

    public Tuple12(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
        this.v10 = v10;
        this.v11 = v11;
        this.v12 = v12;
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
            case 3:
                return v4;
            case 4:
                return v5;
            case 5:
                return v6;
            case 6:
                return v7;
            case 7:
                return v8;
            case 8:
                return v9;
            case 9:
                return v10;
            case 10:
                return v11;
            case 11:
                return v12;
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
            case 3:
                v4 = (V4) value;
                break;
            case 4:
                v5 = (V5) value;
                break;
            case 5:
                v6 = (V6) value;
                break;
            case 6:
                v7 = (V7) value;
                break;
            case 7:
                v8 = (V8) value;
                break;
            case 8:
                v9 = (V9) value;
                break;
            case 9:
                v10 = (V10) value;
                break;
            case 10:
                v11 = (V11) value;
                break;
            case 11:
                v12 = (V12) value;
                break;
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));
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

    public V4 getV4() {
        return v4;
    }

    public void setV4(V4 v4) {
        this.v4 = v4;
    }

    public V5 getV5() {
        return v5;
    }

    public void setV5(V5 v5) {
        this.v5 = v5;
    }

    public V6 getV6() {
        return v6;
    }

    public void setV6(V6 v6) {
        this.v6 = v6;
    }

    public V7 getV7() {
        return v7;
    }

    public void setV7(V7 v7) {
        this.v7 = v7;
    }

    public V8 getV8() {
        return v8;
    }

    public void setV8(V8 v8) {
        this.v8 = v8;
    }

    public V9 getV9() {
        return v9;
    }

    public void setV9(V9 v9) {
        this.v9 = v9;
    }

    public V10 getV10() {
        return v10;
    }

    public void setV10(V10 v10) {
        this.v10 = v10;
    }

    public V11 getV11() {
        return v11;
    }

    public void setV11(V11 v11) {
        this.v11 = v11;
    }

    public V12 getV12() {
        return v12;
    }

    public void setV12(V12 v12) {
        this.v12 = v12;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple12 = (Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(v1, tuple12.v1) &&
                Objects.equals(v2, tuple12.v2) &&
                Objects.equals(v3, tuple12.v3) &&
                Objects.equals(v4, tuple12.v4) &&
                Objects.equals(v5, tuple12.v5) &&
                Objects.equals(v6, tuple12.v6) &&
                Objects.equals(v7, tuple12.v7) &&
                Objects.equals(v8, tuple12.v8) &&
                Objects.equals(v9, tuple12.v9) &&
                Objects.equals(v10, tuple12.v10) &&
                Objects.equals(v11, tuple12.v11) &&
                Objects.equals(v12, tuple12.v12);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
    }

    @Override
    public String toString() {
        return String.format("%s(v1=%s, v2=%s, v3=%s, v4=%s, v5=%s, v6=%s, v7=%s, v8=%s, v9=%s, v10=%s, v11=%s, v12=%s)",
                this.getClass().getSimpleName(),
                String.valueOf(v1),
                String.valueOf(v2),
                String.valueOf(v3),
                String.valueOf(v4),
                String.valueOf(v5),
                String.valueOf(v6),
                String.valueOf(v7),
                String.valueOf(v8),
                String.valueOf(v9),
                String.valueOf(v10),
                String.valueOf(v11),
                String.valueOf(v12));
    }
}
