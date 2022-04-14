package i2f.core.batch.impl;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.batch.IBatchReader;

import java.util.*;

/**
 * @author ltb
 * @date 2022/4/14 14:03
 * @desc
 */
public class IteratorBatchReaderAdapter<T> implements IBatchReader<T> {
    private Iterator<T> iterator;
    private int onceCount=10;
    public IteratorBatchReaderAdapter(Iterator<T> iterator){
        this.iterator=iterator;
    }
    public IteratorBatchReaderAdapter(Iterator<T> iterator,@CloudBe("-1") int onceCount){
        this.iterator=iterator;
        this.onceCount=onceCount;
    }
    @Override
    public Collection<T> read() {
        if(!iterator.hasNext()){
            return null;
        }
        List<T> ret=new LinkedList<>();
        int i=0;
        while(iterator.hasNext()){
            ret.add(iterator.next());
            i++;
            if(i==onceCount){
                break;
            }
        }
        return ret;
    }
}
