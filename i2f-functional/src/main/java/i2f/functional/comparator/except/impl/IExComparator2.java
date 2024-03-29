package i2f.functional.comparator.except.impl;

import i2f.functional.comparator.except.IExComparator;

/**
 * @author Ice2Faith
 * @date 2024/3/29 14:00
 * @desc
 */
@FunctionalInterface
public interface IExComparator2<V1, V2> extends IExComparator {
    int compare(V1 v1, V2 v2) throws Throwable;
}
