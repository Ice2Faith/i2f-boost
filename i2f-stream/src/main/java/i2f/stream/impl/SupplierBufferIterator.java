package i2f.stream.impl;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/2/23 10:35
 * @desc
 */
public class SupplierBufferIterator<E> implements Iterator<E> {
    protected LinkedList<E> buffer = new LinkedList<>();
    protected Supplier<Reference<Collection<E>>> supplier;

    public SupplierBufferIterator(Supplier<Reference<Collection<E>>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        if (buffer.isEmpty()) {
            while(true){
                Reference<Collection<E>> reference = supplier.get();
                if (reference != null) {
                    if(reference.isFinish()){
                        break;
                    }
                    if(reference.isNop()){
                        continue;
                    }
                    if (reference.isPresent()) {
                        buffer.addAll(reference.get());
                    }
                }
            }

        }
        return !buffer.isEmpty();
    }

    @Override
    public E next() {
        return buffer.removeFirst();
    }
}
