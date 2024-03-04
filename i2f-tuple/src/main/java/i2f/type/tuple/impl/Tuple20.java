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
public class Tuple20<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, V17, V18, V19, V20> implements Tuple {
    public static final int SIZE = 20;

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
    public V13 v13;
    public V14 v14;
    public V15 v15;
    public V16 v16;
    public V17 v17;
    public V18 v18;
    public V19 v19;
    public V20 v20;

    public Tuple20() {
    }

    public Tuple20(V1 v1) {
        this.v1 = v1;
    }

    public Tuple20(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) {
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

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10) {
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

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11) {
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

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12) {
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

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13) {
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
        this.v13 = v13;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14) {
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
        this.v13 = v13;
        this.v14 = v14;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
        this.v16 = v16;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
        this.v16 = v16;
        this.v17 = v17;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
        this.v16 = v16;
        this.v17 = v17;
        this.v18 = v18;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18, V19 v19) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
        this.v16 = v16;
        this.v17 = v17;
        this.v18 = v18;
        this.v19 = v19;
    }

    public Tuple20(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16, V17 v17, V18 v18, V19 v19, V20 v20) {
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
        this.v13 = v13;
        this.v14 = v14;
        this.v15 = v15;
        this.v16 = v16;
        this.v17 = v17;
        this.v18 = v18;
        this.v19 = v19;
        this.v20 = v20;
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
            case 12:
                return v13;
            case 13:
                return v14;
            case 14:
                return v15;
            case 15:
                return v16;
            case 16:
                return v17;
            case 17:
                return v18;
            case 18:
                return v19;
            case 19:
                return v20;
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
            case 12:
                v13 = (V13) value;
                break;
            case 13:
                v14 = (V14) value;
                break;
            case 14:
                v15 = (V15) value;
                break;
            case 15:
                v16 = (V16) value;
                break;
            case 16:
                v17 = (V17) value;
                break;
            case 17:
                v18 = (V18) value;
                break;
            case 18:
                v19 = (V19) value;
                break;
            case 19:
                v20 = (V20) value;
                break;
            default:
                throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + SIZE);
        }
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20));
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

    public V13 getV13() {
        return v13;
    }

    public void setV13(V13 v13) {
        this.v13 = v13;
    }

    public V14 getV14() {
        return v14;
    }

    public void setV14(V14 v14) {
        this.v14 = v14;
    }

    public V15 getV15() {
        return v15;
    }

    public void setV15(V15 v15) {
        this.v15 = v15;
    }

    public V16 getV16() {
        return v16;
    }

    public void setV16(V16 v16) {
        this.v16 = v16;
    }

    public V17 getV17() {
        return v17;
    }

    public void setV17(V17 v17) {
        this.v17 = v17;
    }

    public V18 getV18() {
        return v18;
    }

    public void setV18(V18 v18) {
        this.v18 = v18;
    }

    public V19 getV19() {
        return v19;
    }

    public void setV19(V19 v19) {
        this.v19 = v19;
    }

    public V20 getV20() {
        return v20;
    }

    public void setV20(V20 v20) {
        this.v20 = v20;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple20 = (Tuple20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(v1, tuple20.v1) &&
                Objects.equals(v2, tuple20.v2) &&
                Objects.equals(v3, tuple20.v3) &&
                Objects.equals(v4, tuple20.v4) &&
                Objects.equals(v5, tuple20.v5) &&
                Objects.equals(v6, tuple20.v6) &&
                Objects.equals(v7, tuple20.v7) &&
                Objects.equals(v8, tuple20.v8) &&
                Objects.equals(v9, tuple20.v9) &&
                Objects.equals(v10, tuple20.v10) &&
                Objects.equals(v11, tuple20.v11) &&
                Objects.equals(v12, tuple20.v12) &&
                Objects.equals(v13, tuple20.v13) &&
                Objects.equals(v14, tuple20.v14) &&
                Objects.equals(v15, tuple20.v15) &&
                Objects.equals(v16, tuple20.v16) &&
                Objects.equals(v17, tuple20.v17) &&
                Objects.equals(v18, tuple20.v18) &&
                Objects.equals(v19, tuple20.v19) &&
                Objects.equals(v20, tuple20.v20);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20);
    }

    @Override
    public String toString() {
        return String.format("%s(v1=%s, v2=%s, v3=%s, v4=%s, v5=%s, v6=%s, v7=%s, v8=%s, v9=%s, v10=%s, v11=%s, v12=%s, v13=%s, v14=%s, v15=%s, v16=%s, v17=%s, v18=%s, v19=%s, v20=%s)",
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
                String.valueOf(v12),
                String.valueOf(v13),
                String.valueOf(v14),
                String.valueOf(v15),
                String.valueOf(v16),
                String.valueOf(v17),
                String.valueOf(v18),
                String.valueOf(v19),
                String.valueOf(v20));
    }
}
