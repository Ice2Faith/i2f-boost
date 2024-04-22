package i2f.container.iterator.impl;

import i2f.container.reference.Reference;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:31
 * @desc
 */
public class ResourceIterator<HOLDER, RESOURCE, E> implements Iterator<E> {
    @FunctionalInterface
    public static interface ExFunction<T, R> {
        R apply(T obj) throws Throwable;
    }

    @FunctionalInterface
    public static interface ExBiConsumer<V1, V2> {
        void accept(V1 v1, V2 v2) throws Throwable;
    }

    private HOLDER holder;
    private ExFunction<HOLDER, RESOURCE> resourceInitializer;
    private ExFunction<RESOURCE, Reference<E>> elementReader;
    private ExBiConsumer<HOLDER, RESOURCE> resourceReleaser;


    private RESOURCE res;
    private Reference<E> ref = Reference.nop();
    private AtomicBoolean isRelease = new AtomicBoolean(false);

    public ResourceIterator(HOLDER holder,
                            ExFunction<HOLDER, RESOURCE> resourceInitializer,
                            ExFunction<RESOURCE, Reference<E>> elementReader,
                            ExBiConsumer<HOLDER, RESOURCE> resourceReleaser) {
        this.holder = holder;
        this.resourceInitializer = resourceInitializer;
        this.elementReader = elementReader;
        this.resourceReleaser = resourceReleaser;
    }

    @Override
    public boolean hasNext() {
        try {
            if (res == null) {
                res = resourceInitializer.apply(holder);
            }
            if (ref.isValue()) {
                return true;
            }
            ref.toFinish();
            Reference<E> elem = Reference.nop();
            while (true) {
                elem = elementReader.apply(res);
                if (!elem.isNop()) {
                    break;
                }
            }
            ref.of(elem);
            boolean ret = ref.isValue();
            if (!ret) {
                onceRelease();
            }
            return ret;
        } catch (Throwable e) {
            throw new IllegalStateException("iterator exception : " + e.getMessage(), e);
        } finally {
            onceRelease();
        }
    }

    @Override
    public E next() {
        try {
            if (res == null) {
                res = resourceInitializer.apply(holder);
            }
            E ret = ref.get();
            ref.toNop();
            return ret;
        } catch (Throwable e) {
            throw new IllegalStateException("iterator exception : " + e.getMessage(), e);
        } finally {
            onceRelease();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        onceRelease();
    }

    protected void onceRelease() {
        if (!isRelease.get()) {
            try {
                release();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            isRelease.set(true);
        }
    }

    public void release() throws Throwable {
        if (resourceReleaser != null) {
            resourceReleaser.accept(holder, res);
        }
    }
}
