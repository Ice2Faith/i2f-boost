package i2f.core.delegate.retry;

import i2f.core.lang.functional.supplier.ISupplier0;
import i2f.core.lang.functional.supplier.ISupplier1;
import i2f.core.lang.functional.supplier.ISupplier2;
import i2f.core.lang.functional.supplier.ISupplier3;

public class RetryDelegator {

    public static <R> R retry(int maxCount, int delayMillSec, ISupplier0<R> supplier) throws Throwable {
        Throwable thr = null;
        if (maxCount < 1) {
            maxCount = 1;
        }
        int i = 0;
        while (i < maxCount) {
            try {
                return supplier.get();
            } catch (Throwable e) {
                thr = e;
                if (delayMillSec > 0) {
                    try {
                        Thread.sleep(delayMillSec);
                    } catch (Exception ex) {

                    }
                }
            }
            i++;
        }
        throw thr;
    }

    public static <R, V1> R retry(int maxCount, int delayMillSec, ISupplier1<R, V1> supplier, V1 v1) throws Throwable {
        Throwable thr = null;
        if (maxCount < 1) {
            maxCount = 1;
        }
        int i = 0;
        while (i < maxCount) {
            try {
                return supplier.get(v1);
            } catch (Throwable e) {
                thr = e;
                if (delayMillSec > 0) {
                    try {
                        Thread.sleep(delayMillSec);
                    } catch (Exception ex) {

                    }
                }
            }
            i++;
        }
        throw thr;
    }

    public static <R, V1, V2> R retry(int maxCount, int delayMillSec, ISupplier2<R, V1, V2> supplier, V1 v1, V2 v2) throws Throwable {
        Throwable thr = null;
        if (maxCount < 1) {
            maxCount = 1;
        }
        int i = 0;
        while (i < maxCount) {
            try {
                return supplier.get(v1, v2);
            } catch (Throwable e) {
                thr = e;
                if (delayMillSec > 0) {
                    try {
                        Thread.sleep(delayMillSec);
                    } catch (Exception ex) {

                    }
                }
            }
            i++;
        }
        throw thr;
    }

    public static <R, V1, V2, V3> R retry(int maxCount, int delayMillSec, ISupplier3<R, V1, V2, V3> supplier, V1 v1, V2 v2, V3 v3) throws Throwable {
        Throwable thr = null;
        if (maxCount < 1) {
            maxCount = 1;
        }
        int i = 0;
        while (i < maxCount) {
            try {
                return supplier.get(v1, v2, v3);
            } catch (Throwable e) {
                thr = e;
                if (delayMillSec > 0) {
                    try {
                        Thread.sleep(delayMillSec);
                    } catch (Exception ex) {

                    }
                }
            }
            i++;
        }
        throw thr;
    }

}
