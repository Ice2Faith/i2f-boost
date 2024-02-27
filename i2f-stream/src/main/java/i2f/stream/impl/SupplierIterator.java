package i2f.stream.impl;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/2/23 10:35
 * @desc
 */
public class SupplierIterator<E> implements Iterator<E> {
    protected Reference<E> ref;
    protected Supplier<Reference<E>> supplier;

    public SupplierIterator(Supplier<Reference<E>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        if(ref==null){
            while(true){
                ref=supplier.get();
                if(!ref.isNop()){
                    break;
                }
            }
        }
        return !ref.isFinish();
    }

    @Override
    public E next() {
        E ret=ref.get();
        ref=null;
        return ret;
    }
}
