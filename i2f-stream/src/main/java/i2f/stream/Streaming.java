package i2f.stream;

import i2f.stream.impl.*;
import i2f.stream.patten.StreamingPatten;
import i2f.stream.timed.TimedStreaming;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:30
 * @desc
 */
public interface Streaming<E> {

    static Streaming<Integer> ofInt(int begin,int step,int end){
        return new StreamingImpl<>(new LazyIterator<>(()->{
            AtomicInteger curr=new AtomicInteger(begin);
            return new SupplierIterator<>(()->{
                int ret=curr.getAndAdd(step);
                if(step>0){
                    if(ret>end){
                        return Reference.finish();
                    }
                }else{
                    if(ret<end){
                        return Reference.finish();
                    }
                }
                return Reference.of(ret);
            });
        }));
    }

    static Streaming<Integer> ofRandomInt(int count,int bound){
        return new StreamingImpl<>(new LazyIterator<>(()->{
            AtomicInteger curr=new AtomicInteger(0);
            SecureRandom rand=new SecureRandom();
            return new SupplierIterator<>(()->{
                int cnt = curr.getAndIncrement();
                if(cnt>=count){
                    return Reference.finish();
                }
                return Reference.of(rand.nextInt(bound));
            });
        }));
    }

    static Streaming<Double> ofRandom(int count){
        return new StreamingImpl<>(new LazyIterator<>(()->{
            AtomicInteger curr=new AtomicInteger(0);
            SecureRandom rand=new SecureRandom();
            return new SupplierIterator<>(()->{
                int cnt = curr.getAndIncrement();
                if(cnt>=count){
                    return Reference.finish();
                }
                return Reference.of(rand.nextDouble());
            });
        }));
    }

    static Streaming<File> ofDir(File dir) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            File[] arr = dir.listFiles();
            AtomicInteger idx=new AtomicInteger(0);
            return new SupplierIterator<>(() -> {
                if (idx.get()>=arr.length) {
                    return Reference.finish();
                }
                int i = idx.getAndIncrement();
                return Reference.of(arr[i]);
            });
        }));
    }

    static Streaming<Field> ofField(Class<?> clazz) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            Set<Field> fields=new LinkedHashSet<>();
            for (Field field : clazz.getFields()) {
                fields.add(field);
            }
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            return fields.iterator();
        }));
    }

    static Streaming<Method> ofMethod(Class<?> clazz) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            Set<Method> methods=new LinkedHashSet<>();
            for (Method method : clazz.getMethods()) {
                methods.add(method);
            }
            for (Method method : clazz.getDeclaredMethods()) {
                methods.add(method);
            }
            return methods.iterator();
        }));
    }

    static <T> Streaming<T> ofGenerator(Consumer<Consumer<Reference<T>>> collector){
        return new StreamingImpl<>(new GeneratorIterator<>(collector));
    }

    static <T> Streaming<T> of(Supplier<Reference<T>> supplier) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            return new SupplierIterator<>(supplier);
        }));
    }

    static <T> Streaming<T> of(Iterable<T> iterable) {
        return new StreamingImpl<>(new LazyIterator<T>(iterable::iterator));
    }

    static <K,V> Streaming<Map.Entry<K,V>> of(Map<K,V> map){
        return new StreamingImpl<>(new LazyIterator<Map.Entry<K, V>>(map.entrySet().iterator()));
    }

    static <T> Streaming<T> of(Iterator<T> iterator) {
        return new StreamingImpl<>(iterator);
    }

    static <T> Streaming<T> of(Enumeration<T> enumeration) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            return new SupplierIterator<>(() -> {
                if (!enumeration.hasMoreElements()) {
                    return Reference.finish();
                }
                return Reference.of(enumeration.nextElement());
            });
        }));
    }

    static <T> Streaming<T> of(T... arr) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            AtomicInteger idx=new AtomicInteger(0);
            return new SupplierIterator<>(() -> {
                if (idx.get()>=arr.length) {
                    return Reference.finish();
                }
                int i = idx.getAndIncrement();
                return Reference.of(arr[i]);
            });
        }));
    }

    static <T> Streaming<T> of(Stream<T> stream) {
        return new StreamingImpl<>(new LazyIterator<T>(()->{
            LinkedList<T> col = stream.collect(Collectors.toCollection(LinkedList::new));
            return col.iterator();
        }));
    }

    static Streaming<String> of(File file,String charset){
        try{
            return of(new FileInputStream(file),charset);
        }catch(Exception e){
            throw new IllegalStateException("open io exception",e);
        }
    }

    static Streaming<String> of(InputStream is,String charset) {
        try{
            return of(new InputStreamReader(is,charset));
        }catch(Exception e){
            throw new IllegalStateException("convert io exception",e);
        }
    }

    static Streaming<String> of(Reader reader){
        return new StreamingImpl<>(new ResourcesIterator<>(reader,(res,iter)->{
            try{
                BufferedReader buffer = new BufferedReader(reader);
                return new SupplierIterator<>(()->{
                    String line = null;
                    try{
                        line=buffer.readLine();
                    }catch(Exception e){
                        throw new IllegalStateException("read io exception",e);
                    }
                    if(line==null){
                        return Reference.finish();
                    }
                    return Reference.of(line);
                });
            }catch(Exception e){
                throw new IllegalStateException("initial io exception",e);
            }
        },(res,iter)->{
            try{
                res.close();
            }catch(Exception e){
                throw new IllegalStateException("close io exception",e);
            }
        }));
    }

    Streaming<E> parallel();

    Streaming<E> sequence();

    Streaming<E> pool(ExecutorService pool);

    Streaming<E> defaultPool();

    Streaming<E> pool(int size);

    Streaming<E> parallelism(int count);

    <R> Streaming<R> process(BiConsumer<E,Consumer<R>> mapper);

    <R> Streaming<R> process(Function<Iterator<E>, Iterator<R>> process);

    Streaming<Map.Entry<E,Long>> indexed();

    Streaming<E> filter(Predicate<E> filter);

    Streaming<E> afterAll(Predicate<E> filter);

    Streaming<E> beforeAll(Predicate<E> filter);

    default Streaming<E> rangeAll(Predicate<E> beginFilter,Predicate<E> endFilter){
        return rangeAll(beginFilter, endFilter,true,false);
    }

    Streaming<E> rangeAll(Predicate<E> beginFilter,Predicate<E> endFilter,boolean includeBegin,boolean includeEnd);

    default Streaming<E> dropRange(Predicate<E> beginFilter,Predicate<E> endFilter){
        return dropRange(beginFilter, endFilter,true,false);
    }

    Streaming<E> dropRange(Predicate<E> beginFilter,Predicate<E> endFilter,boolean includeBegin,boolean includeEnd);

    <R> Streaming<R> map(Function<E, R> mapper);

    <R> Streaming<R> flatMap(BiConsumer<E, Consumer<R>> collector);

    Streaming<E> recursive(BiConsumer<E,Consumer<E>> collector);

    <R> Streaming<R> recursiveMap(BiConsumer<E, Consumer<R>> initCollector,BiConsumer<R,Consumer<R>> recursiveCollector);

    Streaming<E> skip(long count);

    Streaming<E> limit(long count);

    Streaming<E> tail(int count);

    default Streaming<E> sort(){
        return sort(true);
    }

    Streaming<E> sort(boolean asc);

    Streaming<E> sort(Comparator<E> comparator);

    Streaming<E> reverse();

    Streaming<E> shuffle();

    Streaming<E> distinct();

    Streaming<E> sample(double rate);

    Streaming<E> sampleCount(int count);

    Streaming<E> peek(Consumer<E> consumer);

    default Streaming<E> print(){
        return print(null);
    }

    Streaming<E> print(String prefix);

    Streaming<E> merge(Streaming<E> streaming);

    <T> Streaming<E> merge(Streaming<T> streaming, Function<T, E> mapper);

    Streaming<E> mixed(Streaming<E> streaming);

    Streaming<E> include(Streaming<E> streaming);

    Streaming<E> exclude(Streaming<E> streaming);

    <K> Streaming<Map.Entry<K, Streaming<E>>> keyBy(Function<E, K> keySupplier);

    <K,R> Streaming<Map.Entry<K,R>> keyBy(Function<E,K> keySupplier,Function<Streaming<E>,R> finishMapper);

    <K, V extends Collection<E>> Streaming<Map.Entry<K, V>> keyBy(Supplier<V> containerSupplier,
                                                                  Function<E, K> keySupplier);

    Streaming<Map.Entry<E,Long>> countBy();

    <K> Streaming<Map.Entry<K,Long>> countBy(Function<E,K> keySupplier);

    Streaming<Map.Entry<List<E>,Map.Entry<Integer,Integer>>> viewWindow(int beforeCount, int afterCount);

    Streaming<Map.Entry<List<E>,Map.Entry<Long,Long>>> slideWindow(int windowSize,int slideCount);

    default Streaming<Map.Entry<List<E>,Map.Entry<Long,Long>>> countWindow(int windowSize){
        return slideWindow(windowSize,windowSize);
    }

    <R> Streaming<Map.Entry<List<E>,R>> conditionWindow(Supplier<R> initConditionSupplier,
                                                        Function<E,R> currentConditionMapper,
                                                        BiPredicate<R,R> conditionChangePredicater);

    Streaming<Map.Entry<List<E>,Map.Entry<Long,Long>>> pattenWindow(StreamingPatten<E> patten);

    default Streaming<Map.Entry<List<E>,Map.Entry<Long,Long>>> pattenWindow(Function<StreamingPatten<E>,StreamingPatten<E>> consumer){
        StreamingPatten<E> patten=StreamingPatten.begin();
        patten=consumer.apply(patten);
        return pattenWindow(patten.end());
    }

    <T> Streaming<Map.Entry<E,T>> connect(Streaming<T> other);

    <T> Streaming<Map.Entry<E,T>> join(Streaming<T> other,BiPredicate<E,T> conditional);

    <K,T> Streaming<Map.Entry<E,T>> join(Streaming<T> other,Function<E,K> leftKeySupplier,Function<T,K> rightKeySupplier);

    <R> R collect(Function<Iterator<E>, R> mapper);

    Iterator<E> iterator();

    <R extends Collection<E>> R toCollection(R collection);

    default <R extends Collection<E>> R toCollection(Supplier<R> collectionSupplier){
        return toCollection(collectionSupplier.get());
    }

    <K, V, R extends Map<K, V>> R toMap(R map, Function<E, K> keySupplier,
                                        Function<E, V> valueSupplier,
                                        BiFunction<V, V, V> valueSelector);

    default <K, V, R extends Map<K, V>> R toMap(Supplier<R> mapSupplier,
                                                Function<E, K> keySupplier,
                                        Function<E, V> valueSupplier,
                                        BiFunction<V, V, V> valueSelector){
        return toMap(mapSupplier.get(), keySupplier, valueSupplier, valueSelector);
    }

    <K, V extends Collection<E>, R extends Map<K, V>> Map.Entry<V,R> toGroup(R map,
                                                                Supplier<V> containerSupplier,
                                                                Function<E, K> keySupplier);

    default <K, V extends Collection<E>, R extends Map<K, V>> Map.Entry<V,R> toGroup(Supplier<R> mapSupplier,
                                                                             Supplier<V> containerSupplier,
                                                                             Function<E, K> keySupplier){
        return toGroup(mapSupplier.get(), containerSupplier, keySupplier);
    }

    Reference<E> first();

    Reference<E> last();

    Reference<E> first(Predicate<E> filter);

    Reference<E> last(Predicate<E> filter);

    boolean anyMatch(Predicate<E> filter);

    boolean allMatch(Predicate<E> filter);

    Reference<E> min(Comparator<E> comparator);

    Reference<E> max(Comparator<E> comparator);

    Reference<E> most();

    <K> Reference<Map.Entry<K,E>> most(Function<E,K> keySupplier);

    Reference<E> least();

    <K> Reference<Map.Entry<K,E>> least(Function<E,K> keySupplier);

    Reference<E> reduce(Supplier<E> firstSupplier,
             BiFunction<E,E,E> accumulator);

    <T,R> Reference<R> aggregate(Supplier<T> firstSupplier,
                    BiFunction<T,E,T> accumulator,
                    Function<T,R> finisher);

    long count();

    void forEach(Consumer<E> consumer);

    void forEach(BiConsumer<E,Long> consumer);

    default void sysout(){
        sysout(null);
    }

    void sysout(String prefix);

    default void broadcast(BiConsumer<E,Long> ... consumers){
        broadcast(Arrays.asList(consumers));
    }

    void broadcast(Collection<BiConsumer<E,Long>> consumers);

    default void ring(BiConsumer<E,Long> ... consumers){
        ring(Arrays.asList(consumers));
    }

    void ring(Collection<BiConsumer<E,Long>> consumers);

    default void random(BiConsumer<E,Long> ... consumers){
        random(Arrays.asList(consumers));
    }

    void random(Collection<BiConsumer<E,Long>> consumers);

    <C extends Collection<E>>void batch(int batchSize,Supplier<C> containerSupplier, Consumer<C> consumer);

    default String stringify(String separator){
        return stringify(null,separator,null);
    }

    String stringify(String prefix,String separator,String suffix);

    default void toFile(File file,String charset) throws IOException {
        toFile(file,charset,null,"\n",null);
    }

    default void toFile(File file,String charset,String prefix,String separator,String suffix) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        toStream(fos,charset,prefix,separator,suffix);
        fos.flush();
        fos.close();
    }

    default void toStream(OutputStream os,String charset) throws IOException {
        toStream(os,charset,null,"\n",null);
    }

    default void toStream(OutputStream os,String charset,String prefix,String separator,String suffix) throws IOException {
        OutputStreamWriter ow = new OutputStreamWriter(os, charset);
        toWriter(ow,prefix,separator,suffix);
        ow.flush();
        ow.close();
    }

    default void toWriter(Writer writer) throws IOException {
        toWriter(writer,null,"\n",null);
    }

    void toWriter(Writer writer,String prefix,String separator,String suffix) throws IOException;

    TimedStreaming<E> timed(Function<E,Long> timestampMapper);
}
