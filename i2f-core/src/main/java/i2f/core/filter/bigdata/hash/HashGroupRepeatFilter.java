package i2f.core.filter.bigdata.hash;

import i2f.core.annotations.notice.Nullable;
import i2f.core.data.IDataReader;
import i2f.core.data.IDataWriter;
import i2f.core.filter.bigdata.IRepeatFilter;
import i2f.core.hash.IHashProvider;

import java.io.IOException;
import java.util.*;

/**
 * @author ltb
 * @date 2022/4/27 10:06
 * @desc 适用于，多个大数据量文件，要求找出这些文件中重复的项
 * 使用hash分组切片小数据进行重复性校验，精确，但是需要IO次数高，需要两倍空间
 * 如果你指定了 IRepeatDecider ，那么将按照你指定的决策器进行保留
 * 他接受2个参数，分别是什么数据，这个数据出现的次数
 * 所以，如果你要找到不重复的，那么==1即可，
 * 重复的，那么大于1即可
 */
public class HashGroupRepeatFilter<T> implements IRepeatFilter<T> {
    protected int groupCount=32;
    protected IHashProvider<T> hasher;
    protected IHashGroupProvider<T> provider;

    @Nullable
    protected IRepeatDecider<T> decider;

    public HashGroupRepeatFilter(){

    }

    public int getGroupCount() {
        return groupCount;
    }

    public HashGroupRepeatFilter<T> setGroupCount(int groupCount) {
        this.groupCount = groupCount;
        return this;
    }

    public IHashProvider<T> getHasher() {
        return hasher;
    }

    public HashGroupRepeatFilter<T> setHasher(IHashProvider<T> hasher) {
        this.hasher = hasher;
        return this;
    }

    public IHashGroupProvider<T> getProvider() {
        return provider;
    }

    public HashGroupRepeatFilter<T> setProvider(IHashGroupProvider<T> provider) {
        this.provider = provider;
        return this;
    }

    public IRepeatDecider<T> getDecider() {
        return decider;
    }

    public HashGroupRepeatFilter<T> setDecider(IRepeatDecider<T> decider) {
        this.decider = decider;
        return this;
    }

    /**
     * 大数据下，对多个数据来源readers的数据进行获取重复数据写入到writer中
     * 对reader中的数据使用hasher切片为groupCount份
     * 每一份被切分的数据的读取使用provider进行读写
     * 原理实现：
     * 对每一个reader中的数据使用hasher进行切片，相同hash值得项，写入provider提供的writer中
     * 因此，只要hash值相同，对于每一个reader来说，hash值相同，则说明可能存在相同的数据
     * 因此，再进行比较每一个reader切分出来的每一个小数据集，进行获取重复项，写入writer中即可
     * 举例：
     * 第一步，数据分别按照hash切分，这里两个reader都被切分为3个数据集
     * reader1 且分为了hash值分别为 hash1(reader1hash1),hash2(reader1hash2),hash3(reader1hash3)的数据集
     * reader2 且分为了hash值分别为 hash1(reader2hash1),hash2(reader2hash2),hash3(reader2hash3)的数据集
     * 第二步，对相同hash的不同数据集，进行获取重复项
     * hash1的数据集，对应reader1,reader2,也就是对reader1hash1,reader2hash2进行获取重复项hash1repeat
     * 第三步，对上面得到的hash1repeat写入到writer中
     * 依次类推，重复第二、第三步，直到所有hash分组被处理完毕，则在writer中得到最终结果
     */
    @Override
    public void filter(IDataWriter<T> writer, List<IDataReader<T>> readers) throws IOException {
        provider.create();
        Map<Long, Set<String>> hashMap=new HashMap<>();
        Map<String,IDataWriter<T>> groupWriters=new HashMap<>();
        for(int i=0;i<readers.size();i++){
            IDataReader<T> item = readers.get(i);
            item.create();
            while(item.hasMore()){
                T data=item.read();
                long hash=hasher.hash(data);
                if(hash<0){
                    hash=0-hash;
                }
                long idx=hash%groupCount;
                String gpHash="h"+idx+"r"+i;
                if(!hashMap.containsKey(idx)){
                    hashMap.put(idx,new HashSet<>());
                }
                hashMap.get(idx).add(gpHash);
                if(!groupWriters.containsKey(gpHash)){
                    IDataWriter<T> gwr=provider.getWriter(gpHash);
                    gwr.create();
                    groupWriters.put(gpHash,gwr);
                }
                IDataWriter<T> gwr=groupWriters.get(gpHash);
                gwr.write(data);
            }
            item.destroy();
        }

        for(Map.Entry<String,IDataWriter<T>> item : groupWriters.entrySet()){
            item.getValue().destroy();
        }
        groupWriters=null;

        boolean firstWrite=true;
        for(Map.Entry<Long,Set<String>> item : hashMap.entrySet()){
            Map<T,Long> rptMap=new HashMap<>();

            Set<String> set=item.getValue();
            Map<String,IDataReader<T>> groupReaders=new HashMap<>();
            for(String gpHash : set){
                if(!groupReaders.containsKey(gpHash)){
                    IDataReader<T> grd=provider.getReader(gpHash);
                    grd.create();
                    groupReaders.put(gpHash,grd);
                }
                IDataReader<T> grd=groupReaders.get(gpHash);
                while(grd.hasMore()){
                    T data=grd.read();
                    if(!rptMap.containsKey(data)){
                        rptMap.put(data,1L);
                    }else{
                        Long afCnt=rptMap.get(data);
                        long nxCnt=afCnt+1;
                        rptMap.put(data,nxCnt);
                    }
                }
            }
            for(Map.Entry<String,IDataReader<T>> rdItem : groupReaders.entrySet()){
                rdItem.getValue().destroy();
            }
            groupReaders=null;

            if(firstWrite){
                writer.create();
                firstWrite=false;
            }

            for(Map.Entry<T,Long> rptItem : rptMap.entrySet()){
                if(decider!=null){
                    if(decider.save(rptItem.getKey(),rptItem.getValue())){
                        writer.write(rptItem.getKey());
                    }
                }else{
                    if(rptItem.getValue()>1){
                        writer.write(rptItem.getKey());
                    }
                }
            }

        }

        writer.destroy();

        provider.destroy();
    }


}
