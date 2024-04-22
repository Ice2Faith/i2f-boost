package i2f.container.enumeration.impl;

import java.util.Enumeration;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:54
 * @desc
 */
public class WrapEnumeration<E> implements Enumeration<E> {
    private Enumeration<E> enumeration;

    public WrapEnumeration(Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasMoreElements() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E nextElement() {
        return enumeration.nextElement();
    }
}
