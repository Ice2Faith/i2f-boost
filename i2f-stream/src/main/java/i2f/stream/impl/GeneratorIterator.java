package i2f.stream.impl;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/2/26 17:03
 * @desc
 */
public class GeneratorIterator<E> implements Iterator<E> {
    private Reference<E> ref;
    private LinkedBlockingQueue<Reference<E>> queue=new LinkedBlockingQueue<>();
    private Consumer<Consumer<Reference<E>>> generator;
    private AtomicBoolean isTrigger=new AtomicBoolean(false);

    public GeneratorIterator(Consumer<Consumer<Reference<E>>> generator) {
        this.generator = generator;
    }

    private void triggerGenerator(){
        if(isTrigger.get()){
            return;
        }
        synchronized (this){
            Thread thread = new Thread(() -> {
                generator.accept((elem) -> {
                    try {
                        queue.put(elem);
                    } catch (Exception e) {
                        throw new IllegalStateException("generator put queue exception", e);
                    }
                });
            }, this.getClass().getName() + "@" + this.hashCode());
            thread.start();
            isTrigger.set(true);
        }
    }

    @Override
    public boolean hasNext() {
        synchronized (this){
            while(ref==null){
                triggerGenerator();
                try{
                    ref=queue.take();
                }catch(Exception e){
                    throw new IllegalStateException("generator take queue exception",e);
                }
                if(ref.isNop()){
                    ref=null;
                }
            }
            return !ref.isFinish();
        }
    }

    @Override
    public E next() {
        synchronized (this){
            E ret = ref.get();
            ref=null;
            return ret;
        }
    }
}
