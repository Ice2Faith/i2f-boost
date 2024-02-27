package i2f.stream.impl;

import i2f.stream.richable.BaseRichStreamProcessor;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/2/23 10:28
 * @desc
 */
public class ResourcesIterator<T,E> extends BaseRichStreamProcessor implements Iterator<E> {
    private T resource;
    private Iterator<E> iterator;
    private BiFunction<T,ResourcesIterator<T,E>,Iterator<E>> initializer;
    private BiConsumer<T,ResourcesIterator<T,E>> finisher;
    private boolean initialed=false;
    private boolean finished=false;

    public ResourcesIterator(T resource,
                             BiFunction<T, ResourcesIterator<T, E>, Iterator<E>> supplier,
                             BiConsumer<T,ResourcesIterator<T,E>> finisher) {
        this.resource = resource;
        this.initializer = supplier;
        this.finisher=finisher;
    }

    @Override
    public void onBefore() {
        if(initialed){
            return;
        }
        this.iterator = initializer.apply(this.resource, this);
        initialed=true;
    }

    @Override
    public void onAfter() {
        if(finished){
            return;
        }
        finisher.accept(resource,this);
        finished=true;
    }

    @Override
    public boolean hasNext() {
        onBefore();
        if(this.finished){
            return false;
        }
        boolean ret = iterator.hasNext();
        if(!ret){
            onAfter();
        }
        return ret;
    }

    @Override
    public E next() {
        onBefore();
        return iterator.next();
    }
}
