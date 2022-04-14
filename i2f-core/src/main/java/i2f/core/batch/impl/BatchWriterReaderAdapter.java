package i2f.core.batch.impl;

import i2f.core.batch.IBatchReader;
import i2f.core.batch.IBatchWriter;
import i2f.core.batch.exception.BatchException;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ltb
 * @date 2022/4/14 14:31
 * @desc
 */
public class BatchWriterReaderAdapter<T> implements IBatchReader<T>, IBatchWriter<T> {
    private volatile LinkedBlockingQueue<Collection<T>> queue=new LinkedBlockingQueue<>();
    private volatile boolean ending=false;
    @Override
    public Collection<T> read() {
        if(queue.isEmpty() && ending){
            return null;
        }
        try{
            return queue.take();
        }catch(Exception e){
            throw new BatchException(e.getMessage(),e);
        }
    }

    @Override
    public void write(Collection<T> col) {
        if(col==null){
            ending=true;
            return;
        }
        try{
            queue.put(col);
        }catch(Exception e){
            throw new BatchException(e.getMessage(),e);
        }
    }

}
