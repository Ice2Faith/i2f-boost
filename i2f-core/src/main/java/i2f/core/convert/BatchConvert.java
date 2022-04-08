package i2f.core.convert;

import java.util.*;

/**
 * @author ltb
 * @date 2022/3/27 18:43
 * @desc
 */
public class BatchConvert {
    public static<T> void batch(Iterator<T> iterator,long batchSize,IBatchProcessor<T> processor){
        List<T> list=new LinkedList<>();
        long curSize=0;
        while(iterator.hasNext()){
            T val= iterator.next();
            list.add(val);
            curSize++;
            if(curSize==batchSize){
                processor.process(list);
                curSize=0;
                list=new LinkedList<>();
            }
        }
        if(curSize>0){
            processor.process(list);
        }
    }
    public static<T> void batch(Iterable<T> data,long batchSize,IBatchProcessor<T> processor){
        Iterator<T> iterator=data.iterator();
        batch(iterator,batchSize,processor);
    }
    public static<T> void batch(Enumeration<T> data, long batchSize, IBatchProcessor<T> processor){
        List<T> list=new LinkedList<>();
        long curSize=0;
        while(data.hasMoreElements()){
            T val= data.nextElement();
            list.add(val);
            curSize++;
            if(curSize==batchSize){
                processor.process(list);
                curSize=0;
                list=new LinkedList<>();
            }
        }
        if(curSize>0){
            processor.process(list);
        }
    }
    public static<T> void batch(Collection<T> data, long batchSize, IBatchProcessor<T> processor){
        if(data.size()<=batchSize){
            processor.process(data);
            return;
        }
        List<T> list=new LinkedList<>();
        Iterator<T> iterator=data.iterator();
        batch(iterator,batchSize,processor);
    }
}
