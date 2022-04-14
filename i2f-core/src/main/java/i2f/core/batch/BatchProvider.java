package i2f.core.batch;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.annotations.notice.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/14 11:34
 * @desc
 */
public class BatchProvider {
    public static<T,E> long batch(IBatchReader<T> reader, @CloudBe("-1") long batchSize, @Nullable IBatchProcessor<T,E> processor,@Nullable IBatchWriter<E> writer){
        List<E> once=new LinkedList<E>();
        Collection<T> rd=null;
        long times=0;
        long curSize=0;
        boolean firstProcess=true;
        boolean firstWrite=true;
        reader.create();
        while((rd=reader.read())!=null){
            for(T item : rd){
                E val=null;
                if(processor!=null){
                    if(firstProcess){
                        processor.create();
                        firstProcess=false;
                    }
                    if(!processor.beforeFilter(item)){
                        continue;
                    }
                    val= processor.process(item);
                    if(!processor.afterFilter(val)){
                        continue;
                    }
                }else{
                    val=(E)item;
                }
                once.add(val);
                curSize++;
                if(curSize==batchSize){
                    if(writer!=null){
                        if(firstWrite){
                            writer.create();
                        }
                        writer.write(once);
                    }
                    once=new LinkedList<>();
                    curSize=0;
                    times++;
                }
            }
        }
        reader.destroy();
        if(processor!=null){
            processor.destroy();
        }
        if(!once.isEmpty()){
            if(writer!=null){
                if(firstWrite){
                    writer.create();
                }
                writer.write(once);
            }
            times++;
        }
        writer.write(null);
        writer.destroy();
        return times;
    }

}
