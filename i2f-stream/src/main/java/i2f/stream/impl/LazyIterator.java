package i2f.stream.impl;

import i2f.stream.richable.BaseRichStreamProcessor;
import i2f.stream.richable.RichStreamProcessor;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/2/23 10:28
 * @desc
 */
public class LazyIterator<E> extends BaseRichStreamProcessor implements Iterator<E> {

    protected Iterator<E> iterator;

    protected Supplier<Iterator<E>> supplier;

    public LazyIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public LazyIterator(Supplier<Iterator<E>> supplier) {
        this.supplier = supplier;
    }

    protected void prepare(){
        if(this.iterator!=null){
            return;
        }
        if(this.supplier==null){
            throw new NullPointerException("lazy supplier cannot be null");
        }
        onBefore();
        this.iterator=this.supplier.get();
    }

    @Override
    public boolean hasNext() {
        prepare();
        boolean ret= this.iterator.hasNext();
        if(!ret){
            onAfter();
        }
        return ret;
    }

    @Override
    public E next() {
        prepare();
        return this.iterator.next();
    }
}
