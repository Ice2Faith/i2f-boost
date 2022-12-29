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

## 兼容java-stream的互相转换使用
```java
// 注意，这里使用的Collectors类是本包下的，不是java中的
Stream.of(1,2,3,4) // 一开始，是java-stream
    .map(e->e*2) // java-stream的map方法
    .collect(Collectors.toStreaming()) // 经过toStreaming转换为streaming
    .peek(System.out::println) // streaming的peek方法
    .map(e->e*2) // streaming的map方法
    .stream() // 经过stream转换为java-stream
    .map(e->e*2) // java-stream的map方法
    .collect(Collectors.toArrayList()) // java-stream 的collect为ArrayList
    .stream() // 通过list的stream方法得到java-stream
    .collect(Collectors.toStreaming()) // 经过toStreaming转为streaming
    .collect(Collectors.toArrayList()) // streaming的collect方法，转为ArrayList
    .forEach(System.out::println); // list 的foreach方法

// 下面使用streaming构造
// 可以发现，使用时一样的
Streaming.stream(5,6,7,8)
    .map(e->e*2)
    .collect(Collectors.toStreaming()) // 这里，原本就是streaming了，这里多重复包装了一次
    .peek(System.out::println)
    .map(e->e*2)
    .stream()
    .map(e->e*2)
    .collect(Collectors.toArrayList())
    .stream()
    .collect(Collectors.toStreaming())
    .collect(Collectors.toArrayList())
    .forEach(System.out::println);
```