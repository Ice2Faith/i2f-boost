package i2f.functional.comparator.except.impl;

import i2f.functional.comparator.except.IExComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IExComparator4<V1, V2, V3, V4> extends IExComparator {
    int compare(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;
}
