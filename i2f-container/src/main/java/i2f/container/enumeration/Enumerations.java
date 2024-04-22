package i2f.container.enumeration;

import i2f.container.enumeration.impl.IteratorEnumeration;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 10:11
 * @desc
 * 提供了将可迭代对象转换为可枚举对象的方法
 * 其他的数据类型，通过先转换为Iterator再转换的方式即可
 */
public class Enumerations {
    public static <E> Enumeration<E> of(Iterator<E> iterator) {
        return new IteratorEnumeration<>(iterator);
    }

}
