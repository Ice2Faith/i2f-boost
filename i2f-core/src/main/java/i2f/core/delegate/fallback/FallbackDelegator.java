package i2f.core.delegate.fallback;

import i2f.core.functional.supplier.ISupplier0;
import i2f.core.functional.supplier.ISupplier1;
import i2f.core.functional.supplier.ISupplier2;
import i2f.core.functional.supplier.ISupplier3;

public class FallbackDelegator {
    public static <R> R fallback(ISupplier0<R> supplier, ISupplier0<R> fallbacker) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return fallbacker.get();
        }
    }

    public static <R, V1> R fallback(ISupplier1<R, V1> supplier, ISupplier1<R, V1> fallbacker, V1 v1) {
        try {
            return supplier.get(v1);
        } catch (Throwable e) {
            return fallbacker.get(v1);
        }
    }

    public static <R, V1, V2> R fallback(ISupplier2<R, V1, V2> supplier, ISupplier2<R, V1, V2> fallbacker, V1 v1, V2 v2) {
        try {
            return supplier.get(v1, v2);
        } catch (Throwable e) {
            return fallbacker.get(v1, v2);
        }
    }

    public static <R, V1, V2, V3> R fallback(ISupplier3<R, V1, V2, V3> supplier, ISupplier3<R, V1, V2, V3> fallbacker, V1 v1, V2 v2, V3 v3) {
        try {
            return supplier.get(v1, v2, v3);
        } catch (Throwable e) {
            return fallbacker.get(v1, v2, v3);
        }
    }
}
