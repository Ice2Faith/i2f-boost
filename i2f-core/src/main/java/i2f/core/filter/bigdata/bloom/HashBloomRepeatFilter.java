package i2f.core.filter.bigdata.bloom;

import i2f.core.data.IDataReader;
import i2f.core.data.IDataWriter;
import i2f.core.filter.BloomFilter;
import i2f.core.filter.bigdata.IRepeatFilter;

import java.io.IOException;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/27 14:20
 * @desc 使用布隆过滤器，过滤重复项
 * 属于模糊的匹配，只能说可能重复，不精确
 */
public class HashBloomRepeatFilter<T> implements IRepeatFilter<T> {
    protected BloomFilter<T> bloomFilter;

    public HashBloomRepeatFilter() {
    }

    public BloomFilter<T> getBloomFilter() {
        return bloomFilter;
    }

    public HashBloomRepeatFilter<T> setBloomFilter(BloomFilter<T> bloomFilter) {
        this.bloomFilter = bloomFilter;
        return this;
    }

    @Override
    public void filter(IDataWriter<T> writer, List<IDataReader<T>> readers) throws IOException {
        boolean firstWrite=true;
        for(IDataReader<T> item : readers){
            item.create();
            while(item.hasMore()){
              T data=item.read();
              if(bloomFilter.exists(data)){
                  if(firstWrite){
                      writer.create();
                      firstWrite=false;
                  }
                  writer.write(data);
              }else{
                  bloomFilter.mark(data);
              }
            }
            item.destroy();
        }
        writer.destroy();
    }

    public void trainBloomFilter(List<IDataReader<T>> readers) throws IOException {
        for(IDataReader<T> item : readers){
            item.create();
            while(item.hasMore()){
                T data=item.read();
                bloomFilter.mark(data);
            }
            item.destroy();
        }
    }

    public void filterAfterTrain(boolean repeat,IDataWriter<T> writer,List<IDataReader<T>> readers) throws IOException {
        boolean firstWrite=true;
        for(IDataReader<T> item : readers){
            item.create();
            while(item.hasMore()){
                T data=item.read();
                if(bloomFilter.exists(data)==repeat){
                    if(firstWrite){
                        writer.create();
                        firstWrite=false;
                    }
                    writer.write(data);
                }
            }
            item.destroy();
        }
        writer.destroy();
    }

}
