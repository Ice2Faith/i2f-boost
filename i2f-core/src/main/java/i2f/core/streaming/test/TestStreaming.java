package i2f.core.streaming.test;


import i2f.core.streaming.Streaming;
import i2f.core.streaming.support.sink.FileTextLineSinkStreaming;
import i2f.core.streaming.support.source.FileTextLineSourceStreaming;
import i2f.core.tuple.Tuples;

import java.io.File;
import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/11/22 9:49
 * @desc
 */
public class TestStreaming {
    public static void main(String[] args) {
        File file = new File("D:\\word.txt");
        File cp = new File("D:\\word.cnt.txt");

        Streaming.stream(new FileTextLineSourceStreaming(file))
                .<String>flatMap((e, c) -> {
                    String[] arr = e.split("\\s+");
                    for (String item : arr) {
                        c.add(item);
                    }
                    try {
                        Thread.sleep(90);
                    } catch (Exception ex) {

                    }
                }).parallel()
                .keyBy(e -> {
                    if (e.length() >= 3) {
                        return e.substring(0, 3);
                    }
                    return e;
                })
                .peek(System.out::println)
                .flatMap((e, c) -> {
                    c.addAll(e.t2);
                })
                .countBy()
                .peek(System.out::println).sequential()
                .sink(new FileTextLineSinkStreaming<>(cp, (e) -> e.t2 + " > " + e.t1));
    }

    public void test1() {

        ArrayList<String> collect = Streaming.stream(1, 5, 3, 7, 6, 2, 8, 4, 9)
                // 过滤大于2的元素
                .filter((e) -> e > 2)
                // 过滤小于8的元素
                .filter((e) -> e < 8)
                // 拼接为字符串
                .map((e) -> e + "(i)")
                // 排序
                .sort((e1, e2) -> -e1.compareTo(e2))
                // 将字符串n(i)拆分为两个元素：n,(i)
                .<String>flatMap((e, col) -> {
                    col.add(e.substring(0, 1));
                    col.add(e.substring(1));
                })
                // 跳过前2个元素
                .skip(2)
                // 最多保留6个元素
                .limit(6)
//                // 自定义聚合，直接统计每个单词出现的次数
//                .<Tuple2<String,Integer>>aggregate((iterator, col)->{
//                    Map<String,Integer> cntMap=new HashedMap();
//                    while(iterator.hasNext()){
//                        String str = iterator.next();
//                        if(!cntMap.containsKey(str)){
//                            cntMap.put(str,0);
//                        }
//                        cntMap.put(str,cntMap.get(str)+1);
//                    }
//                    for(Map.Entry<String,Integer> item : cntMap.entrySet()){
//                        col.add(Tuples.of(item.getKey(),item.getValue()));
//                    }
//                })
                // 按照字符本身分组，分组内统计次数
                .<String, Integer>keyedAggregate(e -> e, (col) -> {
                    int i = 0;
                    while (col.hasNext()) {
                        col.next();
                        i++;
                    }
                    return i;
                })
                // 根据二元组第一个元素分组，求分组内sum
                .keyedReduce(e -> e.t1, (e1, e2) -> {
                    int v1 = e1 == null ? 0 : e1.t2;
                    return Tuples.of(e2.t1, v1 + e2.t2);
                })
                // 转换为string
                .map(Object::toString)
                // 收集为ArrayList
                .collect(new ArrayList<>());
//                .each(System.out::println);
        collect.forEach(System.out::println);

        System.out.println("===============================================");

        Integer sum = Streaming.stream(1, 2, 3, 4, 5, 6)
                .reduce((e1, e2) -> {
                    if (e1 == null) {
                        e1 = 0;
                    }
                    return e1 + e2;
                });
        System.out.println(sum);

        System.out.println("===============================================");

        Streaming.stream(1, 2, 3, 4, 5)
                .peek(System.out::println)
                .log((pram) -> System.out.println("--------------------"))
                .join(Streaming.stream("3", "4", "5", "6", "7"),
                        (v1, v2) -> String.valueOf(v1).equals(String.valueOf(v2)),
                        (v1, v2) -> v1 + "=" + v2)
                .peek(System.out::println)
                .log((pram) -> System.out.println("--------------------"))
                .each(System.out::println);
    }
}
