package test.filter;

import i2f.core.container.collection.Collections;
import i2f.core.data.IDataReader;
import i2f.core.data.IDataWriter;
import i2f.core.data.impl.StreamReaderDataReader;
import i2f.core.data.impl.StreamWriterDataWriter;
import i2f.core.filter.BloomFilter;
import i2f.core.filter.bigdata.bloom.HashBloomRepeatFilter;
import i2f.core.filter.bigdata.hash.HashGroupRepeatFilter;
import i2f.core.filter.bigdata.hash.IRepeatDecider;
import i2f.core.filter.bigdata.hash.impl.StreamHashGroupProvider;
import i2f.core.hash.ObjectHashcodeHashProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/27 11:10
 * @desc
 */
public class TestGroupRepeatFilter {
    public static void main(String[] args) throws IOException {
        String basePath = "E:\\01test\\filter";
        IDataWriter<String> writer = new StreamWriterDataWriter(new File(basePath, "result-hash-group.txt"));
        List<IDataReader<String>> readers = getReaders(basePath);
        HashGroupRepeatFilter<String> filter = new HashGroupRepeatFilter<>();
        filter.setGroupCount(10)
                .setHasher(new ObjectHashcodeHashProvider<String>())
                .setProvider(new StreamHashGroupProvider(new File(basePath)))
                .setDecider(new IRepeatDecider<String>() {
                    @Override
                    public boolean save(String data, long cnt) {
                        return cnt == 1;
                    }
                });
        filter.filter(writer, readers);

        HashBloomRepeatFilter<String> bloomRepeatFilter=new HashBloomRepeatFilter<>();
        bloomRepeatFilter.setBloomFilter(new BloomFilter<>());

        writer=new StreamWriterDataWriter(new File(basePath, "result-bloom.txt"));
        readers = getReaders(basePath);
        bloomRepeatFilter.filter(writer,readers);


        bloomRepeatFilter.setBloomFilter(new BloomFilter<>());
        readers = getReaders(basePath);
        readers.remove(0);
        readers.remove(0);
        bloomRepeatFilter.trainBloomFilter(readers);

        writer=new StreamWriterDataWriter(new File(basePath, "result-bloom-train-not-repeat.txt"));
        readers=getReaders(basePath);
        bloomRepeatFilter.filterAfterTrain(false,writer,readers);

        writer=new StreamWriterDataWriter(new File(basePath, "result-bloom-train-possible-repeat.txt"));
        readers=getReaders(basePath);
        bloomRepeatFilter.filterAfterTrain(true,writer,readers);
    }

    public static List<IDataReader<String>> getReaders(String basePath){
        return Collections.arrayList(
                new StreamReaderDataReader(new File(basePath, "part1.txt")),
                new StreamReaderDataReader(new File(basePath, "part2.txt")),
                new StreamReaderDataReader(new File(basePath, "part3.txt")),
                new StreamReaderDataReader(new File(basePath, "part4.txt"))
        );
    }


}
