package i2f.core.iterator.impl;

import java.util.Iterator;

/**
 * 对String类型提供的迭代器
 * 迭代为char
 */
public class StringIterator implements Iterator<Character> {
    private String str;
    private int idx = 0;

    public StringIterator(String str) {
        this.str = str;
    }

    @Override
    public boolean hasNext() {
        return str != null && idx < str.length();
    }

    @Override
    public Character next() {
        char ch = str.charAt(idx);
        idx++;
        return ch;
    }
}
