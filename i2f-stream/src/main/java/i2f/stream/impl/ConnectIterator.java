package i2f.stream.impl;


import java.util.Iterator;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/2/23 17:02
 * @desc
 */
public class ConnectIterator<T,U> implements Iterator<Map.Entry<T,U>> {

    private Iterator<T> left;
    private Iterator<U> right;

    public ConnectIterator(Iterator<T> left, Iterator<U> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean hasNext() {
        return left.hasNext() || right.hasNext();
    }

    @Override
    public Map.Entry<T, U> next() {
        SimpleEntry<T, U> ret = new SimpleEntry<>();
        if(left.hasNext()){
            ret.setKey(left.next());
        }
        if(right.hasNext()){
            ret.setValue(right.next());
        }
        return ret;
    }
}
