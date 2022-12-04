# 流式计算包
- 提供了类似java8中的Stream-Api的能力

## 详情
- Streaming
    - 提供了直接暴露的流接口
- AbsStreaming
    - 提供了每个流操作可以进行的操作的抽象包装
```java
ArrayList<String> collect = Streams.stream(1, 5, 3, 7, 6, 2, 8, 4, 9)
                // 过滤大于2的元素
                .filter((e) -> e > 2)
                // 过滤小于8的元素
                .filter((e) -> e < 8)
                // 拼接为字符串
                .map((e) -> e + "(i)")
                // 排序
                .sort((e1, e2) -> -e1.compareTo(e2))
                // 将字符串n(i)拆分为两个元素：n,(i)
                .<String>flatMap((e,col)->{
                    col.add(e.substring(0,1));
                    col.add(e.substring(1));
                })
                // 跳过前2个元素
                .skip(2)
                // 最多保留6个元素
                .limit(6)
                // 按照字符本身分组，分组内统计次数
                .<String,Integer>keyedAggregate(e->e,(col)->{
                    int i=0;
                    while(col.hasNext()){
                        col.next();
                        i++;
                    }
                    return i;
                })
                // 根据二元组第一个元素分组，求分组内sum
                .keyedReduce(e->e.t1,(e1,e2)->{
                    int v1=e1==null?0:e1.t2;
                    return Tuples.of(e2.t1,v1+e2.t2);
                })
                // 转换为string
                .map(Object::toString)
                .each(System.out::println);
```