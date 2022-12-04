package i2f.core.functions;

import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.notice.NullableReturn;
import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IExecutor;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.common.IMapper;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Author("i2f")
public class Algorithm {

    /**
     * 对可迭代对象Iterable进行迭代，针对IFilter过滤通过的对象T使用IMap为T类型对象给IExecute执行，同时如果Collection<E>不为null，则放入此容器R
     * Collection<E>可以为null,表示不需要保存
     * IExecute可以为null,表示不需要执行
     * IMap可以为null,表示直接强转，因此需要考虑是否类型可强转
     * IFilter可以为null,表示不进行过滤
     * @param collection
     * @param ite
     * @param filter
     * @param mapper
     * @param executor
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    @NullableReturn
    public static <T, E, R extends Collection<E>> R eachProxy(@Nullable R collection, Iterable<T> ite,
                                                              @Nullable IFilter<T> filter, @Nullable IMapper<E, T> mapper,
                                                              @Nullable IExecutor<E> executor) {
        Iterator<T> itr = ite.iterator();
        while (itr.hasNext()) {
            T cur = itr.next();
            if (filter == null || filter.test(cur)) {
                E val = (mapper == null) ? ((E) cur) : (mapper.get(cur));
                if (collection != null) {
                    collection.add(val);
                }
                if (executor != null) {
                    executor.accept(val);
                }
            }
        }
        return collection;
    }

    /**
     * 同上的针对可迭代对象的，
     * 这个是针对可枚举类型的
     * @param collection
     * @param ite
     * @param filter
     * @param mapper
     * @param executor
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    @NullableReturn
    public static <T, E, R extends Collection<E>> R eachProxy(@Nullable R collection, Enumeration<T> ite,
                                                              @Nullable IFilter<T> filter, @Nullable IMapper<E, T> mapper,
                                                              @Nullable IExecutor<E> executor) {
        while (ite.hasMoreElements()) {
            T cur = ite.nextElement();
            if (filter == null || filter.test(cur)) {
                E val = (mapper == null) ? ((E) cur) : (mapper.get(cur));
                if (collection != null) {
                    collection.add(val);
                }
                if (executor != null) {
                    executor.accept(val);
                }
            }
        }
        return collection;
    }

    /**
     * 过滤并映射
     * @param collection
     * @param ite
     * @param filter
     * @param mapper
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    @NullableReturn
    public static <T, E, R extends Collection<E>> R filterMap(@Nullable R collection, Iterable<T> ite, @Nullable IFilter<T> filter, @Nullable IMapper<E, T> mapper) {
        return eachProxy(collection, ite, filter, mapper, null);
    }

    @NullableReturn
    public static <T, E, R extends Collection<E>> R filterMap(@Nullable R collection, Enumeration<T> ite, @Nullable IFilter<T> filter, @Nullable IMapper<E, T> mapper) {
        return eachProxy(collection, ite, filter, mapper, null);
    }

    /**
     * 仅过滤
     * @param collection
     * @param ite
     * @param filter
     * @param <T>
     * @param <R>
     * @return
     */
    @NullableReturn
    public static<T,R extends Collection<T>> R filter(@Nullable R collection,Iterable<T> ite,@Nullable IFilter<T> filter){
        return eachProxy(collection,ite,filter,null,null);
    }
    @NullableReturn
    public static<T,R extends Collection<T>> R filter(@Nullable R collection,Enumeration<T> ite,@Nullable IFilter<T> filter){
        return eachProxy(collection,ite,filter,null,null);
    }

    /**
     * 仅映射
     * @param collection
     * @param ite
     * @param mapper
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    @NullableReturn
    public static <T, E, R extends Collection<E>> R map(@Nullable R collection, Iterable<T> ite, @Nullable IMapper<E, T> mapper) {
        return eachProxy(collection, ite, null, mapper, null);
    }

    @NullableReturn
    public static <T, E, R extends Collection<E>> R map(@Nullable R collection, Enumeration<T> ite, @Nullable IMapper<E, T> mapper) {
        return eachProxy(collection, ite, null, mapper, null);
    }

    /**
     * 仅执行
     *
     * @param ite
     * @param executor
     * @param <T>
     */
    public static <T> void execute(Iterable<T> ite, @Nullable IExecutor<T> executor) {
        eachProxy(null, ite, null, null, executor);
    }

    public static <T> void execute(Enumeration<T> ite, @Nullable IExecutor<T> executor) {
        eachProxy(null, ite, null, null, executor);
    }
}
