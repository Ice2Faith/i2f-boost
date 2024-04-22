package i2f.container.iterator.impl.array;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 8:45
 * @desc
 */
public class IntArrayIterator implements Iterator<Integer> {
    private int[] arr;
    private int startIndex = 0;
    private int endIndex;

    public IntArrayIterator(int[] arr) {
        this.arr = arr;
        this.startIndex = 0;
        this.endIndex = arr.length;
    }

    public IntArrayIterator(int[] arr, int startIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = arr.length;
    }

    public IntArrayIterator(int[] arr, int startIndex, int endIndex) {
        this.arr = arr;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public boolean hasNext() {
        return this.startIndex < this.endIndex;
    }

    @Override
    public Integer next() {
        int ret = arr[startIndex];
        startIndex++;
        return ret;
    }
}
