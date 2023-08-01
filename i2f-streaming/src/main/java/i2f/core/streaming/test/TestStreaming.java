package i2f.core.streaming.test;

import i2f.core.streaming.Streaming;
import i2f.core.streaming.data.KeyedData;
import i2f.core.streaming.iterator.SyncQueueIterator;
import i2f.core.streaming.keyed.functional.KeyedPredicate;
import i2f.core.streaming.process.StreamingProcessCollectWrapper;
import i2f.core.streaming.process.StreamingProcessWrapper;
import i2f.core.streaming.rich.adapter.RichBiFunctionWrapper;
import i2f.core.streaming.sink.StreamingSinkWrapper;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:02
 * @desc
 */
public class TestStreaming {
    public static void main(String[] args) {
        test7();
    }


    public static void test() throws Exception {
        String str = Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 2000 * 10000;
            private SecureRandom random = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                return random.nextInt(curr);
            }
        }).parallel()
                .sort()
                .resample(0.5)
                .shuffle()
                .sort()
                .reverse()
                .keyBy(e -> e % 10)
                .map((Function<Integer, Integer>) Math::abs)
                .sort()
                .shuffle()
                .sort()
                .reverse()
                .count()
                .stringify((e) -> e.key + ":" + e.val, "[", ",", "]");
        System.out.println(str);
    }

    public static void test1() throws Exception {
        Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 1000 * 10000;

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                return curr++;
            }
        })
                .process(new StreamingProcessWrapper<Integer, Integer>() {
                    @Override
                    public Iterator<Integer> process(Iterator<Integer> iterator) {
                        return new SyncQueueIterator<Integer>() {
                            @Override
                            public void require(boolean first) {
                                while (iterator.hasNext()) {
                                    Integer val = iterator.next();
                                    if (val % 2 == 1) {
                                        put(val);
                                        break;
                                    }
                                }
                                if (!iterator.hasNext()) {
                                    finish();
                                }
                            }
                        };
                    }
                })
                .process(new StreamingProcessWrapper<Integer, Integer>() {
                    @Override
                    public Iterator<Integer> process(Iterator<Integer> iterator) {
                        return new SyncQueueIterator<Integer>() {
                            @Override
                            public void require(boolean first) {
                                if (first) {
                                    getContext().getPool().submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            while (iterator.hasNext()) {
                                                Integer val = iterator.next();
                                                if (val < 1000) {
                                                    put(val);
                                                }
                                            }
                                            finish();
                                        }
                                    });
                                }
                            }
                        };
                    }
                })
                .process(new StreamingProcessCollectWrapper<Integer, Integer>() {
                    @Override
                    public void collect(Iterator<Integer> iterator, Consumer<Integer> consumer) {
                        while (iterator.hasNext()) {
                            Integer val = iterator.next();
                            if (val % 3 == 0) {
                                consumer.accept(val);
                            }
                        }
                    }
                })
                .filter((val) -> val < 50)
                .parallel()
                .map((val) -> val * 2)
                .sequential()
                .<Integer>flatMap((val, col) -> {
                    col.accept(val % 60);
                    col.accept(val % 70);
                })
                .keyBy((val) -> val % 10)
                .filter((val) -> val > 3)
                .map((val) -> (int) (val * 3.14))
                .<Integer>flatMap((val, col) -> {
                    col.accept((val) * 2);
                })
                .flat()
                .sink(new StreamingSinkWrapper<Integer, Object>() {
                    @Override
                    public Object sink(Iterator<Integer> iterator) {
                        while (iterator.hasNext()) {
                            System.out.println(iterator.next());
                        }
                        return null;
                    }
                });
    }

    public static void test2() throws Exception {

        System.out.println("----------------------------------");
        Double avg = Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 100 * 10000;
            private SecureRandom rand = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                if (rand.nextDouble() < 0.05) {
                    return null;
                }
                return curr;
            }
        })
                .parallel()
//                .sync((list)->System.out.println("init-------------------------"))
//                .peek((e)->System.out.println("init:"+e))
                .map(e -> e == null ? -1 : e * 2)
//                .sync((list)->System.out.println("map-------------------------"))
//                .peek((e)->System.out.println("map:"+e))
                .resample(0.5)
//                .sync((list)->System.out.println("resample-------------------------"))
//                .peek((e)->System.out.println("resample:"+e))
                .shuffle()
//                .sync((list)->System.out.println("shuffle-------------------------"))
//                .peek((e)->System.out.println("shuffle:"+e))
                .sort()
//                .sync((list)->System.out.println("sort-------------------------"))
//                .peek((e)->System.out.println("sort:"+e))
                .reverse()
//                .sync((list)->System.out.println("reverse-------------------------"))
//                .peek((e)->System.out.println("reverse:"+e))
                .keyBy(e -> e % 5)
                .fork((ss) -> {
                    try {
                        Integer sum = ss.count().peek((data) -> {
                            System.out.println("fork-count:key=" + data.key + ",count=" + data.val);
                        }).map(e -> e.key)
                                .peek(e -> System.out.println("map-fork----------"))
                                .number(e -> e)
                                .sum();
                        System.out.println("fork-sum:" + sum);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                })
                .count()
                .peek((data) -> {
                    System.out.println("count:key=" + data.key + ",count=" + data.val);
                })
                .map(e -> e.val)
                .keyBy(e -> e)
                .filter(new KeyedPredicate<Integer, Integer>() {
                    @Override
                    public boolean test(Integer integer, Integer key) {
                        System.out.println("key:" + key + ",val:" + integer);
                        return true;
                    }
                })
                .peek((val, key) -> System.out.println("peek:key:" + key + ",val:" + val))
                .flat()
                .number(e -> e * 1.0)
//                .avg();
                .fork((ss) -> {
                    ss.after((e) -> e > 15)
                            .before(e -> e < 100)
                            .join(Streaming.source(101, 102, 103).number(e -> e * 1.0))
                            .sync((col) -> System.out.println("after----------------"))
                            .peek((e) -> System.out.println("after:" + e))
                            .batch(30, (col) -> {
                                System.out.println("size:" + col.size());
                            });
                })
                .reduce(new RichBiFunctionWrapper<Double, Double, Double>() {
                    @Override
                    public Double apply(Double aDouble, Double aDouble2) {
                        ConcurrentHashMap<String, Optional<Object>> pmp = getContext().getProcessMap();
                        if (!pmp.containsKey("cnt")) {
                            pmp.put("cnt", Optional.ofNullable(1));
                        }
                        Integer cnt = (Integer) pmp.get("cnt").orElse(0);
                        cnt++;
                        pmp.put("cnt", Optional.ofNullable(cnt));
                        double sum = aDouble + aDouble2;
                        System.out.println("avg:" + (sum / cnt));
                        return sum;
                    }
                });
        System.out.println("sum:" + avg);

    }

    public static void test3() throws Exception {


        System.out.println("----------------------------------");
        Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 1 * 10000;
            private SecureRandom rand = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                if (rand.nextDouble() < 0.05) {
                    return null;
                }
                return rand.nextInt() % curr;
            }
        })
                .parallel()
                .measures(
                        Streaming.Measure.FIRST, // 第一个，返回E
                        Streaming.Measure.LAST, // 最后一个，返回E
                        Streaming.Measure.COUNT,  // 总数，返回Integer
                        Streaming.Measure.NULL_COUNT, // 空值的数量，返回Integer
                        Streaming.Measure.NON_NULL_COUNT, // 非空值的数量，返回Integer
                        Streaming.Measure.DISTINCT_COUNT, // 去重的数量，返回Integer
                        Streaming.Measure.MOST, // 最多出现的元素，返回E
                        Streaming.Measure.LEAST, // 最少出现的元素，返回E
                        Streaming.Measure.MIN, // 最小元素，返回E
                        Streaming.Measure.MAX, // 最大元素，返回E
                        Streaming.Measure.SUM, // 求和，返回E
                        Streaming.Measure.AVG, // 求品均值，返回E
                        Streaming.Measure.MEDIAN // 求中位数，返回E
                ).forEach((key, val) -> {
            System.out.println("measure:" + key + ":" + val);
        });

    }

    public static void test4() throws Exception {


        System.out.println("----------------------------------");
        Object aggr = Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 1 * 10000;
            private SecureRandom rand = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                if (rand.nextDouble() < 0.05) {
                    return null;
                }
                return rand.nextInt() % curr;
            }
        })
                .parallel()
                .filter(Objects::nonNull)
                .aggregate(1, new Function<List<Integer>, Integer>() {
                    @Override
                    public Integer apply(List<Integer> integers) {
                        int sum = 0;
                        for (Integer item : integers) {
                            sum += item;
                        }
                        return sum;
                    }
                }, Integer::sum);

        System.out.println("sum:" + aggr);
    }

    public static void test5() throws Exception {


        System.out.println("----------------------------------");
        Object aggr = Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 1 * 10000;
            private SecureRandom rand = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                if (rand.nextDouble() < 0.05) {
                    return null;
                }
                return rand.nextInt() % curr;
            }
        })
                .parallel()
                .filter(Objects::nonNull)
                .aggregate(() -> new KeyedData<>(0, 0),
                        (elem, acc) -> new KeyedData<>(acc.key + elem, acc.val + 1),
                        (acc1, acc2) -> new KeyedData<>(acc1.key + acc2.key, acc1.val + acc2.val),
                        acc -> acc.key * 1.0 / acc.val);
        System.out.println("avg:" + aggr);
    }

    public static void test6() throws InterruptedException {


        System.out.println("----------------------------------");
        Streaming.source(new Iterator<Integer>() {
            private int curr = 0;
            private int max = 1 * 10000;
            private SecureRandom rand = new SecureRandom();

            @Override
            public boolean hasNext() {
                return curr < max;
            }

            @Override
            public Integer next() {
                curr++;
                if (rand.nextDouble() < 0.05) {
                    return null;
                }
                return rand.nextInt() % curr;
            }
        }).sort()
                .tail(3)
                .each((Consumer<Integer>) System.out::println);

    }

    public static void test7(){
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));

                test();
                System.out.println("\n\n\n\n\n\n*********************************************\n\n\n\n\n\n");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
