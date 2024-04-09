package i2f.sql;


import org.apache.poi.hssf.record.formula.functions.T;
import i2f.sql.bean.BeanResolver;
import i2f.sql.lambda.IClassTableNameResolver;
import i2f.sql.lambda.IFieldColumnNameResolver;
import i2f.sql.lambda.LambdaInflater;
import i2f.sql.lambda.impl.DefaultClassTableNameResolver;
import i2f.sql.lambda.impl.DefaultFieldColumnNameResolver;
import i2f.sql.lambda.interfaces.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:44
 * @desc
 */
public class $ {
    protected LinkedList<BindSql> builder = new LinkedList<>();
    protected String link = "and";
    protected String separator = " ";
    protected String alias = null;
    protected String placeholder = "?";
    protected IFieldColumnNameResolver fieldNameResolver = DefaultFieldColumnNameResolver.DEFAULT;
    protected IClassTableNameResolver tableNameResolver = DefaultClassTableNameResolver.DEFAULT;

    public static final Predicate<?> DEFAULT_FILTER = (val) -> {
        if (val == null) {
            return false;
        }
        if (val instanceof String) {
            return !"".equals(val);
        }
        if (val instanceof Collection) {
            return !((Collection) val).isEmpty();
        }
        if (val instanceof Map) {
            return !((Map) val).isEmpty();
        }
        Class<?> clazz = val.getClass();
        if (clazz.isArray()) {
            return Array.getLength(val) > 0;
        }
        return val != null;
    };

    public static <T> Predicate<T> defaultFilter() {
        return (Predicate<T>) DEFAULT_FILTER;
    }


    public $() {

    }

    public static $ $_() {
        return new $();
    }

    public static $ $_(BindSql bql) {
        return new $().$(bql);
    }

    public static $ $_($ val) {
        return new $().$(val);
    }

    public static $ $_(Supplier<$> caller) {
        return new $().$(caller);
    }

    public static $ $_(String sql, Object args) {
        return new $().$(sql, args);
    }

    public BindSql $$() {
        if (builder.isEmpty()) {
            return new BindSql("");
        }
        if (builder.size() == 1) {
            return builder.get(0);
        }
        StringBuilder sql = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (BindSql bql : builder) {
            if (bql == null) {
                continue;
            }
            if (bql.sql != null) {
                sql.append(bql.sql);
            }
            if (bql.args != null) {
                args.addAll(bql.args);
            }
        }
        return new BindSql(sql.toString(), args);
    }

    public $ addSeparator() {
        if (separator == null) {
            return this;
        }
        if (builder.isEmpty()) {
            return this;
        }
        BindSql last = builder.getLast();
        if (last.args.isEmpty()) {
            if (separator.equals(last.sql)) {
                return this;
            }
            if (last.sql.endsWith(separator)) {
                return this;
            }
            if (last.sql.trim().endsWith(separator)) {
                return this;
            }
        }
        builder.add(new BindSql(separator));
        return this;
    }

    public $ $($ val) {
        if (val != null) {
            addSeparator();
            builder.addAll(val.builder);
        }
        return this;
    }

    public $ $(Supplier<$> caller) {
        if (caller != null) {
            return $(caller.get());
        }
        return this;
    }

    public $ $(BindSql bql) {
        if (bql == null) {
            return this;
        }
        addSeparator();
        builder.add(bql);
        return this;
    }

    public $ $(String sql, Object... args) {
        return $(new BindSql(sql, new ArrayList<>(Arrays.asList(args))));
    }

    public <CT, CR> $ $(IGetter<CT, CR> capture) {
        return $(lambdaFieldName(capture));
    }

    public <CT, CV> $ $(ISetter<CT, CV> capture) {
        return $(lambdaFieldName(capture));
    }

    public <CT, CV, CR> $ $(IFunction<CT, CV, CR> capture) {
        return $(lambdaFieldName(capture));
    }

    public <CT> $ $(IExecute<CT> capture) {
        return $(lambdaFieldName(capture));
    }

    public $ $(Class<?> clazz) {
        return $(classTableName(clazz));
    }


    public $ $clear() {
        this.builder.clear();
        return this;
    }

    public BindSql $getAndClear() {
        BindSql bql = this.$$();
        this.$clear();
        return bql;
    }

    public $ $ensureBefore(String str) {
        if (this.builder.isEmpty()) {
            return $(str);
        }
        if (!this.builder.isEmpty()) {
            BindSql last = this.builder.getLast();
            String sql = last.sql;
            if (sql.endsWith(str)) {
                return this;
            }
            sql = sql.trim();
            if (sql.endsWith(str)) {
                return this;
            }
        }

        BindSql bql = this.$getAndClear();
        String sql = bql.sql;
        if (sql.endsWith(str)) {
            return this;
        }

        sql = sql.trim();
        if (sql.endsWith(str)) {
            return this;
        }

        bql.sql = sql;
        $(bql);
        $(str);
        return this;
    }

    public $ $trimBefore(List<String> strs) {
        if (this.builder.isEmpty()) {
            return this;
        }
        if (!this.builder.isEmpty()) {
            BindSql last = this.builder.getLast();
            String sql = last.sql;
            for (String str : strs) {
                if (sql.endsWith(str)) {
                    sql = sql.substring(0, sql.length() - str.length());
                    last.sql = sql;
                    return this;
                }
            }
            sql = sql.trim();
            for (String str : strs) {
                if (sql.endsWith(str)) {
                    sql = sql.substring(0, sql.length() - str.length());
                    last.sql = sql;
                    return this;
                }
            }
        }

        BindSql bql = this.$getAndClear();
        String sql = bql.sql;
        for (String str : strs) {
            if (sql.endsWith(str)) {
                sql = sql.substring(0, sql.length() - str.length());
                bql.sql = sql;
                return $(bql);
            }
        }
        sql = sql.trim();
        for (String str : strs) {
            if (sql.endsWith(str)) {
                sql = sql.substring(0, sql.length() - str.length());
                bql.sql = sql;
                return $(bql);
            }
        }
        return $(bql);
    }

    public <T> $ $if(T val, Predicate<T> filter, Function<T, $> caller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        return $(caller.apply(val));
    }

    public <T> $ $if(T val, Function<T, $> caller) {
        return $if(val, defaultFilter(), caller);
    }

    public <T> $ $trim(T val, Predicate<T> filter,
                       List<String> trimPrefixes, List<String> trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, $> caller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        BindSql bql = caller.apply(val).$$();
        String sql = bql.sql;
        if (trimPrefixes != null) {
            for (String item : trimPrefixes) {
                sql = sql.trim();
                if (sql.startsWith(item)) {
                    sql = sql.substring(item.length());
                }
            }
        }
        if (trimSuffixes != null) {
            for (String item : trimSuffixes) {
                sql = sql.trim();
                if (sql.startsWith(item)) {
                    sql = sql.substring(0, sql.length() - item.length());
                }
            }
        }
        if (appendPrefix != null) {
            sql = sql.trim();
            if (!"".equals(sql)) {
                sql = appendPrefix + " " + sql;
            }
        }
        if (appendSuffix != null) {
            sql = sql.trim();
            if (!"".equals(sql)) {
                sql = sql + " " + appendSuffix;
            }
        }
        bql.sql = sql;
        return $(bql);
    }

    public <T> $ $trim(T val,
                       List<String> trimPrefixes, List<String> trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Function<T, $> caller) {
        return $trim(val, defaultFilter(),
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                caller);
    }

    public <T> $ $trim(List<String> trimPrefixes, List<String> trimSuffixes,
                       String appendPrefix, String appendSuffix,
                       Supplier<$> caller) {
        return $trim(null, null,
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                v -> caller.get());
    }

    public <T, C extends Collection<T>> $ $for(C val, Predicate<C> filter,
                                               String separator,
                                               Predicate<T> itemFilter,
                                               BiFunction<Integer, T, $> itemCaller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        $ next = $.$_().$sep(separator);
        int idx = 0;
        for (T item : val) {
            if (itemFilter != null && !itemFilter.test(item)) {
                idx++;
                continue;
            }
            $ one = itemCaller.apply(idx, item);
            next.$(one);
            idx++;
        }
        return $(next);
    }

    public <T, C extends Collection<T>> $ $for(C val,
                                               String separator,
                                               Predicate<T> itemFilter,
                                               BiFunction<Integer, T, $> itemCaller) {
        return $for(val, defaultFilter(), separator, itemFilter, itemCaller);
    }

    public <T, C extends Collection<T>> $ $forUnion(C val,
                                                    Predicate<T> itemFilter,
                                                    BiFunction<Integer, T, $> itemCaller) {
        return $trim(val, Arrays.asList("union all", "union"),
                Arrays.asList("union all", "union"),
                null, null,
                (list) -> $.$_()
                        .$for(list, " union ", itemFilter,
                                itemCaller
                        )
        );
    }

    public <T, C extends Collection<T>> $ $forUnionAll(C val,
                                                       Predicate<T> itemFilter,
                                                       BiFunction<Integer, T, $> itemCaller) {
        return $trim(val, Arrays.asList("union all", "union"),
                Arrays.asList("union all", "union"),
                null, null,
                (list) -> $.$_()
                        .$for(list, " union all ", itemFilter,
                                itemCaller
                        )
        );
    }


    public $ $each(Supplier<$>... suppliers) {
        for (Supplier<$> supplier : suppliers) {
            $ next = supplier.get();
            $(next);
        }
        return this;
    }

    public $ $var(Object val) {
        return $(placeholder, val);
    }

    public static <T, R> ILambda $lm(IGetter<T, R> capture) {
        return capture;
    }

    public static <T, V> ILambda $lm(ISetter<T, V> capture) {
        return capture;
    }

    public static <T, V, R> ILambda $lm(IFunction<T, V, R> capture) {
        return capture;
    }

    public static <T> ILambda $lm(IExecute<T> capture) {
        return capture;
    }

    public static <T> List<T> $ls(T... arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public List<String> lm2names(List<ILambda> list) {
        if (list == null) {
            return null;
        }
        List<String> ret = new ArrayList<>();
        for (ILambda lambda : list) {
            ret.add(lambdaFieldName(lambda));
        }
        return ret;
    }

    public List<String> lm2names(ILambda... lambdas) {
        return lm2names(Arrays.asList(lambdas));
    }

    public <T> $ $beanDelete(T bean) {
        return $beanDelete(bean, null, null);
    }

    public <T> $ $beanDelete(T bean,
                             List<ILambda> whereIsNullCols) {
        return $beanDelete(bean, whereIsNullCols, null);
    }

    public <T> $ $beanDelete(T bean,
                             List<ILambda> whereIsNullCols,
                             List<ILambda> whereIsNotNullCols) {
        if (bean == null) {
            return this;
        }
        Class<?> clazz = bean.getClass();
        Map<String, Object> whereMap = new LinkedHashMap<>();

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        for (Field field : fields) {
            String colName = fieldNameResolver.getName(field);

            if (bean != null) {
                try {
                    field.setAccessible(true);
                    Object val = field.get(bean);
                    whereMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }

        return $mapDelete(classTableName(clazz),
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public $ $mapDelete(String table,
                        Map<String, Object> whereMap,
                        List<String> whereIsNullCols,
                        List<String> whereIsNotNullCols) {
        return $deleteFrom(table)
                .$mapWhere(null, whereMap, whereIsNullCols, whereIsNotNullCols);
    }

    public <T> $ $beanUpdate(T update, T cond) {
        return $beanUpdate(update, cond, null, null, null);
    }

    public <T> $ $beanUpdate(T update, T cond, List<ILambda> updateNullCols) {
        return $beanUpdate(update, cond, updateNullCols, null, null);
    }

    public <T> $ $beanUpdate(T update, T cond,
                             List<ILambda> updateNullCols,
                             List<ILambda> whereIsNullCols,
                             List<ILambda> whereIsNotNullCols) {
        if (update == null) {
            return this;
        }
        Class<?> clazz = update.getClass();
        Map<String, Object> updateMap = new LinkedHashMap<>();
        Map<String, Object> whereMap = new LinkedHashMap<>();

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        for (Field field : fields) {
            String colName = fieldNameResolver.getName(field);
            if (update != null) {
                try {
                    field.setAccessible(true);
                    Object val = field.get(update);
                    updateMap.put(colName, val);
                } catch (Exception e) {

                }
            }

            if (cond != null) {
                try {
                    field.setAccessible(true);
                    Object val = field.get(cond);
                    whereMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }

        return $mapUpdate(classTableName(clazz),
                updateMap,
                lm2names(updateNullCols),
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }


    public $ $mapUpdate(String table, Map<String, Object> updateMap,
                        List<String> updateNullCols,
                        Map<String, Object> whereMap,
                        List<String> whereIsNullCols,
                        List<String> whereIsNotNullCols) {
        return $update(table)
                .$mapSet(updateMap, updateNullCols)
                .$mapWhere(null, whereMap, whereIsNullCols, whereIsNotNullCols);
    }

    public $ $mapSet(Map<String, Object> updateMap,
                     List<String> updateNullCols) {
        return $set(updateMap, v -> {
            return (updateMap != null && !updateMap.isEmpty())
                    || (updateNullCols != null && !updateNullCols.isEmpty());
        }, v -> {
            $ ret = $.$_().$sepComma().$link();
            if (updateMap != null) {
                for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    ret.$eq(col, val);
                }
            }
            if (updateNullCols != null) {
                for (String col : new LinkedHashSet<>(updateNullCols)) {
                    ret.$eqNull(col);
                }
            }
            return ret;
        });
    }

    public <T> $ $beanQuery(T bean) {
        return $beanQuery(bean, null, null);
    }

    public <T> $ $beanQuery(T bean, List<ILambda> selectCols) {
        return $beanQuery(bean, null, selectCols);
    }

    public <T> $ $beanQuery(T bean, String alias) {
        return $beanQuery(bean, alias, null);
    }

    public <T> $ $beanQuery(T bean, String alias, List<ILambda> selectCols) {
        return $beanQuery(bean, alias, selectCols, null);
    }

    public <T> $ $beanQuery(T bean, String alias, List<ILambda> selectCols, List<ILambda> selectExcludeCols) {
        return $beanQuery(bean, alias, selectCols, selectExcludeCols, null, null, null);
    }

    public <T> $ $beanQuery(T bean, String alias,
                            List<ILambda> selectCols,
                            List<ILambda> selectExcludeCols,
                            List<ILambda> whereIsNullCols,
                            List<ILambda> whereIsNotNullCols,
                            List<ILambda> orderCols) {
        if (bean == null) {
            return this;
        }
        Class<?> clazz = bean.getClass();
        Set<Field> fields = BeanResolver.getDbFields(clazz);
        Map<String, String> colMap = new LinkedHashMap<>();
        if (selectCols != null && !selectCols.isEmpty()) {
            for (ILambda lambda : selectCols) {
                Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
                String colName = fieldNameResolver.getName(field);
                if (field.getName().equals(colName)) {
                    colMap.put(colName, null);
                } else {
                    colMap.put(colName, field.getName());
                }
            }
        }
        Set<String> excludeNames = new HashSet<>();
        if (selectExcludeCols != null) {
            for (ILambda lambda : selectExcludeCols) {
                Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
                excludeNames.add(field.getName());
            }
        }
        Map<String, Object> whereMap = new LinkedHashMap<>();
        for (Field field : fields) {
            String colName = fieldNameResolver.getName(field);
            if (selectCols == null || selectCols.isEmpty()) {
                if (!excludeNames.contains(field.getName())) {
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                }
            }

            try {
                field.setAccessible(true);
                Object val = field.get(bean);
                whereMap.put(colName, val);
            } catch (Exception e) {

            }
        }
        List<String> orderList = lm2names(orderCols);
        List<String> orderNames = new ArrayList<>();
        if (orderList != null) {
            for (String name : orderList) {
                String str = columnName(alias, name);
                orderNames.add(str);
            }
        }
        return $mapQuery(classTableName(clazz),
                alias,
                colMap,
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols),
                null,
                orderNames);
    }


    public $ $mapQuery(String table,
                       String alias,
                       Map<String, String> colMap,
                       Map<String, Object> whereMap,
                       List<String> whereIsNullCols,
                       List<String> whereIsNotNullCols,
                       List<String> groupCols,
                       List<String> orderCols) {

        return $mapSelect(alias, colMap)
                .$from(table, alias)
                .$mapWhere(alias, whereMap, whereIsNullCols, whereIsNotNullCols)
                .$mapGroupBy(groupCols)
                .$mapOrderBy(orderCols);
    }


    public $ $mapSelect(String alias,
                        Map<String, String> colMap) {
        return $select(() -> {
            $ ret = $.$_().$sepComma().$alias(alias);
            for (Map.Entry<String, String> entry : colMap.entrySet()) {
                String col = entry.getKey();
                String as = entry.getValue();
                ret.$col(col, as);
            }
            return ret;
        });
    }

    public $ $mapWhere(String alias,
                       Map<String, Object> whereMap,
                       List<String> whereIsNullCols,
                       List<String> whereIsNotNullCols) {
        return $where(whereMap, v -> {
            return (whereMap != null && !whereMap.isEmpty())
                    || (whereIsNullCols != null && !whereIsNullCols.isEmpty())
                    || (whereIsNotNullCols != null && !whereIsNotNullCols.isEmpty());
        }, (v) -> {
            $ ret = $.$_().$alias(alias);
            if (whereMap != null) {
                for (Map.Entry<String, Object> entry : whereMap.entrySet()) {
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    ret.$eq(col, val);
                }
            }
            if (whereIsNullCols != null) {
                for (String col : new LinkedHashSet<>(whereIsNullCols)) {
                    ret.$isNull(col);
                }
            }
            if (whereIsNotNullCols != null) {
                for (String col : new LinkedHashSet<>(whereIsNotNullCols)) {
                    ret.$isNotNull(col);
                }
            }
            return ret;
        });
    }

    public $ $mapGroupBy(List<String> groupCols) {
        return $groupBy(groupCols, (cols) -> {
            $ ret = $.$_().$sepComma().$alias(alias);
            for (String col : cols) {
                ret.$col(col);
            }
            return ret;
        });
    }

    public $ $mapOrderBy(List<String> orderCols) {
        return $orderBy(orderCols, (cols) -> {
            $ ret = $.$_().$sepComma().$alias(alias);
            for (String col : cols) {
                ret.$col(col);
            }
            return ret;
        });
    }

    public $ $from(String table, String tableAlias) {
        if (tableAlias == null || "".equals(tableAlias)) {
            return $("from " + table);
        }
        return $("from " + table + " " + tableAlias);
    }

    public $ $from(Class<?> table, String tableAlias) {
        return $from(classTableName(table), tableAlias);
    }

    public $ $from(String table) {
        return $from(table, null);
    }

    public $ $fromDual() {
        return $from("dual");
    }

    public $ $from(Class<?> clazz) {
        return $from(classTableName(clazz));
    }

    public $ $from(Supplier<$> caller, String tableAlias) {
        return $trim(null, null,
                null, null,
                "from (",
                ") " + tableAlias,
                v -> caller.get()
        );
    }

    public $ left() {
        return $("left");
    }

    public $ right() {
        return $("right");
    }

    public $ inner() {
        return $("inner");
    }

    public $ outer() {
        return $("outer");
    }

    public $ join() {
        return $("join");
    }

    public $ leftJoin() {
        return left().join();
    }

    public $ rightJoin() {
        return right().join();
    }

    public $ innerJoin() {
        return inner().join();
    }

    public $ outerJoin() {
        return outer().join();
    }

    public $ $join(String table, String tableAlias) {
        if (tableAlias == null || "".equals(tableAlias)) {
            return join().$(table);
        }
        return join().$(table).$(tableAlias);
    }

    public $ $join(Class<?> table, String tableAlias) {
        return $join(classTableName(table), tableAlias);
    }

    public $ $join(String table) {
        return join().$(table);
    }

    public $ $join(Class<?> table) {
        return $join(classTableName(table));
    }

    public $ $join(Supplier<$> caller, String tableAlias) {
        return $trim(null, null,
                null, null,
                "join (",
                ") " + tableAlias,
                v -> caller.get()
        );
    }


    public <T> $ $on(T val, Predicate<T> filter,
                     Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                "on", null,
                caller);
    }

    public <T> $ $on(T val,
                     Function<T, $> caller) {
        return $on(val, defaultFilter(), caller);
    }

    public <T> $ $on(Supplier<$> caller) {
        return $on(null, null, v -> caller.get());
    }


    public <T> $ $where(T val, Predicate<T> filter,
                        Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                "where", null,
                caller);
    }

    public <T> $ $where(T val,
                        Function<T, $> caller) {
        return $where(val, defaultFilter(), caller);
    }

    public <T> $ $where(Supplier<$> caller) {
        return $where(null, null, v -> caller.get());
    }

    public <T> $ $set(T val, Predicate<T> filter,
                      Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                "set", null,
                caller);
    }

    public <T> $ $set(T val,
                      Function<T, $> caller) {
        return $set(val, defaultFilter(), caller);
    }

    public <T> $ $set(Supplier<$> caller) {
        return $set(null, null, v -> caller.get());
    }

    public <T> $ $select(T val, Predicate<T> filter,
                         Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                "select", null,
                caller);
    }

    public <T> $ $select(T val,
                         Function<T, $> caller) {
        return $select(val, defaultFilter(), caller);
    }

    public $ $select(Supplier<$> caller) {
        return $select(null, null, v -> caller.get());
    }

    public $ $select(List<ILambda> list) {
        return $select(() -> {
            $ ret = i2f.sql.$.$_().$sepComma();
            for (ILambda lambda : list) {
                ret.$col(lambdaFieldName(lambda));
            }
            return ret;
        });
    }

    public <T> $ $groupBy(T val, Predicate<T> filter,
                          Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                "group by", null,
                caller);
    }

    public <T> $ $groupBy(T val,
                          Function<T, $> caller) {
        return $groupBy(val, defaultFilter(), caller);
    }

    public <T> $ $groupBy(Supplier<$> caller) {
        return $groupBy(null, null, v -> caller.get());
    }

    public <T> $ $orderBy(T val, Predicate<T> filter,
                          Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                "order by", null,
                caller);
    }

    public <T> $ $orderBy(T val,
                          Function<T, $> caller) {
        return $orderBy(val, defaultFilter(), caller);
    }

    public <T> $ $orderBy(Supplier<$> caller) {
        return $orderBy(null, null, v -> caller.get());
    }


    public <T> $ $and(T val, Predicate<T> filter,
                      Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                "and (", ")",
                caller);
    }

    public <T> $ $and(T val,
                      Function<T, $> caller) {
        return $and(val, defaultFilter(), caller);
    }

    public <T> $ $and(Supplier<$> caller) {
        return $and(null, null, v -> caller.get());
    }


    public <T> $ $or(T val, Predicate<T> filter,
                     Function<T, $> caller) {
        return $trim(val, filter,
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                "or (", ")",
                caller);
    }

    public <T> $ $or(T val,
                     Function<T, $> caller) {
        return $or(val, defaultFilter(), caller);
    }

    public <T> $ $or(Supplier<$> caller) {
        return $or(null, null, v -> caller.get());
    }

    public $ $reset() {
        this.link = "and";
        this.alias = null;
        this.separator = " ";
        this.placeholder = "?";
        return this;
    }

    public $ $and() {
        this.link = "and";
        return this;
    }

    public $ $or() {
        this.link = "or";
        return this;
    }

    public $ $link() {
        this.link = null;
        return this;
    }

    public $ $alias() {
        this.alias = null;
        return this;
    }

    public $ $alias(String alias) {
        this.alias = alias;
        return this;
    }

    public $ $sep() {
        this.separator = " ";
        return this;
    }

    public $ $sepComma() {
        this.separator = ",";
        return this;
    }

    public $ $sep(String separator) {
        this.separator = separator;
        return this;
    }

    public $ $sepNone() {
        this.separator = null;
        return this;
    }

    public $ placeholder() {
        this.placeholder = "?";
        return this;
    }

    public $ placeholder(String holder) {
        this.placeholder = holder;
        return this;
    }

    public static String columnName(String tableAlias, String column) {
        if (tableAlias != null && !"".equals(tableAlias)) {
            return tableAlias + "." + column;
        }
        return column;
    }

    public static String condColumnName(String link, String tableAlias, String column) {
        String name = columnName(tableAlias, column);
        if (link != null && !"".equals(link)) {
            return link + " " + name;
        }
        return name;
    }

    public static String selectColumnName(String tableAlias, String column, String asAlias) {
        String name = columnName(tableAlias, column);
        if (asAlias != null && !"".equals(asAlias)) {
            return name + " as " + asAlias;
        }
        return name;
    }

    public $ $colAs(String column, String asAlias, Object... args) {
        return $(selectColumnName(null, column, asAlias), args);
    }

    public <CT, CV, CR> $ $colAs(String column, IGetter<CT, CR> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT, CV> $ $colAs(String column, ISetter<CT, CV> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT, CV, CR> $ $colAs(String column, IFunction<CT, CV, CR> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT> $ $colAs(String column, IExecute<CT> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public $ $col(String tableAlias, String column, String asAlias) {
        return $(selectColumnName(tableAlias, column, asAlias));
    }

    public <T, R> $ $col(String tableAlias, IGetter<T, R> capture, String asAlias) {
        return $col(tableAlias, lambdaFieldName(capture), asAlias);
    }

    public <T, V> $ $col(String tableAlias, ISetter<T, V> capture, String asAlias) {
        return $col(tableAlias, lambdaFieldName(capture), asAlias);
    }

    public <T, V, R> $ $col(String tableAlias, IFunction<T, V, R> capture, String asAlias) {
        return $col(tableAlias, lambdaFieldName(capture), asAlias);
    }

    public <T> $ $col(String tableAlias, IExecute<T> capture, String asAlias) {
        return $col(tableAlias, lambdaFieldName(capture), asAlias);
    }

    public $ $col(String column, String asAlias) {
        return $(selectColumnName(alias, column, asAlias));
    }

    public <T, R> $ $col(IGetter<T, R> capture, String asAlias) {
        return $col(lambdaFieldName(capture), asAlias);
    }

    public <T, V> $ $col(ISetter<T, V> capture, String asAlias) {
        return $col(lambdaFieldName(capture), asAlias);
    }

    public <T, V, R> $ $col(IFunction<T, V, R> capture, String asAlias) {
        return $col(lambdaFieldName(capture), asAlias);
    }

    public $ $col(IExecute<T> capture, String asAlias) {
        return $col(lambdaFieldName(capture), asAlias);
    }

    public $ $col(String column) {
        return $(selectColumnName(alias, column, null));
    }

    public <T, R> $ $col(IGetter<T, R> capture) {
        return $(lambdaFieldName(capture));
    }

    public <T, V> $ $col(ISetter<T, V> capture) {
        return $(lambdaFieldName(capture));
    }

    public <T, V, R> $ $col(IFunction<T, V, R> capture) {
        return $(lambdaFieldName(capture));
    }

    public $ $col(IExecute<T> capture) {
        return $(lambdaFieldName(capture));
    }

    public String lambdaFieldName(ILambda lambda) {
        Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
        return fieldNameResolver.getName(field);
    }

    public String classTableName(Class<?> clazz) {
        return tableNameResolver.getName(clazz);
    }


    public $ $col(Supplier<$> caller, String asAlias) {
        return $trim(null, null,
                null, null,
                "(",
                ") as " + asAlias,
                v -> caller.get()
        );
    }

    public <T> $ $cond(T val, Predicate<T> filter, BiFunction<T, String, $> caller) {
        return $if(val, filter, v -> $.$_().$(link).$(caller.apply(v, alias)));
    }

    public <T> $ $cond(T val, BiFunction<T, String, $> caller) {
        return $cond(val, defaultFilter(), caller);
    }

    public <T> $ $cond(Supplier<$> caller) {
        return $cond(null, null, (v, a) -> caller.get());
    }

    public <T> $ $cond(String sql, Object... args) {
        return $cond(null, null, (v, a) -> $.$_(new BindSql(sql, new ArrayList<>(Arrays.asList(args)))));
    }


    public <T> $ $eq(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " = " + placeholder, val));
    }

    public <T, CT, CR> $ $eq(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $eq(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $eq(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $eq(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $eq(String column, T val) {
        return $eq(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $eq(IGetter<CT, CR> capture, T val) {
        return $eq(lambdaFieldName(capture), val, defaultFilter());
    }

    public <T, CT, CV> $ $eq(ISetter<CT, CV> capture, T val) {
        return $eq(lambdaFieldName(capture), val, defaultFilter());
    }

    public <T, CT, CV, CR> $ $eq(IFunction<CT, CV, CR> capture, T val) {
        return $eq(lambdaFieldName(capture), val, defaultFilter());
    }

    public <T, CT> $ $eq(IExecute<CT> capture, T val) {
        return $eq(lambdaFieldName(capture), val, defaultFilter());
    }

    public $ $eqNull(String column) {
        return $(condColumnName(link, alias, column) + " = null");
    }

    public <CT, CR> $ $eqNull(IGetter<CT, CR> capture) {
        return $eqNull(lambdaFieldName(capture));
    }

    public <CT, CV> $ $eqNull(ISetter<CT, CV> capture) {
        return $eqNull(lambdaFieldName(capture));
    }

    public <CT, CV, CR> $ $eqNull(IFunction<CT, CV, CR> capture) {
        return $eqNull(lambdaFieldName(capture));
    }

    public <CT> $ $eqNull(IExecute<CT> capture) {
        return $eqNull(lambdaFieldName(capture));
    }

    public <T> $ $like(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " like " + placeholder, val));
    }

    public <T, CT, CR> $ $like(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $like(ISetter<CT, CT> capture, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CR, CV> $ $like(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $like(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $like(String column, T val) {
        return $like(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $like(IGetter<CT, CR> capture, T val) {
        return $like(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $like(ISetter<CT, CV> capture, T val) {
        return $like(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $like(IFunction<CT, CV, CR> capture, T val) {
        return $like(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $like(IExecute<CT> capture, T val) {
        return $like(lambdaFieldName(capture), val);
    }

    public <T> $ $instr(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, null, "instr(" + columnName(alias, column) + "," + placeholder + ")") + " > 0", val));
    }

    public <T, CT, CR> $ $instr(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $instr(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $instr(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $instr(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $instr(String column, T val) {
        return $instr(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $instr(IGetter<CT, CR> capture, T val) {
        return $instr(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $instr(ISetter<CT, CV> capture, T val) {
        return $instr(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $instr(IFunction<CT, CV, CR> capture, T val) {
        return $instr(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $instr(IExecute<CT> capture, T val) {
        return $instr(lambdaFieldName(capture), val);
    }

    public <T> $ $gt(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " > " + placeholder, val));
    }

    public <T, CT, CR> $ $gt(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $gt(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $gt(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $gt(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $gt(String column, T val) {
        return $gt(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $gt(IGetter<CT, CR> capture, T val) {
        return $gt(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $gt(ISetter<CT, CV> capture, T val) {
        return $gt(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $gt(IFunction<CT, CV, CR> capture, T val) {
        return $gt(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $gt(IExecute<CT> capture, T val) {
        return $gt(lambdaFieldName(capture), val);
    }

    public <T> $ $lt(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " < " + placeholder, val));
    }

    public <T, CT, CR> $ $lt(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $lt(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $lt(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $lt(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $lt(String column, T val) {
        return $lt(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $lt(IGetter<CT, CR> capture, T val) {
        return $lt(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $lt(ISetter<CT, CV> capture, T val) {
        return $lt(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $lt(IFunction<CT, CV, CR> capture, T val) {
        return $lt(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $lt(IExecute<CT> capture, T val) {
        return $lt(lambdaFieldName(capture), val);
    }

    public <T> $ $gte(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " >= " + placeholder, val));
    }

    public <T, CT, CR> $ $gte(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $gte(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $gte(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $gte(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $gte(String column, T val) {
        return $gte(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $gte(IGetter<CT, CR> capture, T val) {
        return $gte(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $gte(ISetter<CT, CV> capture, T val) {
        return $gte(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $gte(IFunction<CT, CV, CR> capture, T val) {
        return $gte(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $gte(IExecute<CT> capture, T val) {
        return $gte(lambdaFieldName(capture), val);
    }

    public <T> $ $lte(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " <= " + placeholder, val));
    }

    public <T, CT, CR> $ $lte(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $lte(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $lte(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $lte(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(capture), val, filter);
    }


    public <T> $ $lte(String column, T val) {
        return $lte(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $lte(IGetter<CT, CR> capture, T val) {
        return $lte(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $lte(ISetter<CT, CV> capture, T val) {
        return $lte(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $lte(IFunction<CT, CV, CR> capture, T val) {
        return $lte(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $lte(IExecute<CT> capture, T val) {
        return $lte(lambdaFieldName(capture), val);
    }

    public <T> $ $neq(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " != " + placeholder, val));
    }

    public <T, CT, CR> $ $neq(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $neq(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $neq(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $neq(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $neq(String column, T val) {
        return $neq(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $neq(IGetter<CT, CR> capture, T val) {
        return $neq(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $neq(ISetter<CT, CV> capture, T val) {
        return $neq(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $neq(IFunction<CT, CV, CR> capture, T val) {
        return $neq(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $neq(IExecute<CT> capture, T val) {
        return $neq(lambdaFieldName(capture), val);
    }

    public <T> $ $ne(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(condColumnName(link, alias, column) + " <> " + placeholder, val));
    }

    public <T, CT, CR> $ $ne(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $ne(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $ne(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $ne(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $ne(String column, T val) {
        return $ne(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $ne(IGetter<CT, CR> capture, T val) {
        return $ne(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $ne(ISetter<CT, CV> capture, T val) {
        return $ne(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $ne(IFunction<CT, CV, CR> capture, T val) {
        return $ne(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $ne(IExecute<CT> capture, T val) {
        return $ne(lambdaFieldName(capture), val);
    }

    public <T> $ $isNull(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(new BindSql(condColumnName(link, alias, column) + " is null")));
    }

    public <T, CT, CR> $ $isNull(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $isNull(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $isNull(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $isNull(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $isNull(String column, T val) {
        return $isNull(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $isNull(IGetter<CT, CR> capture, T val) {
        return $isNull(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $isNull(ISetter<CT, CV> capture, T val) {
        return $isNull(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $isNull(IFunction<CT, CV, CR> capture, T val) {
        return $isNull(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $isNull(IExecute<CT> capture, T val) {
        return $isNull(lambdaFieldName(capture), val);
    }


    public $ $isNull(String column) {
        return $isNull(column, null, null);
    }

    public <CT, CR> $ $isNull(IGetter<CT, CR> capture) {
        return $isNull(lambdaFieldName(capture));
    }

    public <CT, CV> $ $isNull(ISetter<CT, CV> capture) {
        return $isNull(lambdaFieldName(capture));
    }

    public <CT, CV, CR> $ $isNull(IFunction<CT, CV, CR> capture) {
        return $isNull(lambdaFieldName(capture));
    }

    public <CT> $ $isNull(IExecute<CT> capture) {
        return $isNull(lambdaFieldName(capture));
    }

    public <T> $ $isNotNull(String column, T val, Predicate<T> filter) {
        return $if(val, filter, v -> $.$_(new BindSql(condColumnName(link, alias, column) + " is not null")));
    }

    public <T, CT, CR> $ $isNotNull(IGetter<CT, CR> capture, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV> $ $isNotNull(ISetter<CT, CV> capture, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT, CV, CR> $ $isNotNull(IFunction<CT, CV, CR> capture, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(capture), val, filter);
    }

    public <T, CT> $ $isNotNull(IExecute<CT> capture, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(capture), val, filter);
    }

    public <T> $ $isNotNull(String column, T val) {
        return $isNotNull(column, val, defaultFilter());
    }

    public <T, CT, CR> $ $isNotNull(IGetter<CT, CR> capture, T val) {
        return $isNotNull(lambdaFieldName(capture), val);
    }

    public <T, CT, CV> $ $isNotNull(ISetter<CT, CV> capture, T val) {
        return $isNotNull(lambdaFieldName(capture), val);
    }

    public <T, CT, CV, CR> $ $isNotNull(IFunction<CT, CV, CR> capture, T val) {
        return $isNotNull(lambdaFieldName(capture), val);
    }

    public <T, CT> $ $isNotNull(IExecute<CT> capture, T val) {
        return $isNotNull(lambdaFieldName(capture), val);
    }

    public $ $isNotNull(String column) {
        return $isNotNull(column, null, null);
    }

    public <CT, CR> $ $isNotNull(IGetter<CT, CR> capture) {
        return $isNotNull(lambdaFieldName(capture));
    }

    public <CT, CV> $ $isNotNull(ISetter<CT, CV> capture) {
        return $isNotNull(lambdaFieldName(capture));
    }

    public <CT, CV, CR> $ $isNotNull(IFunction<CT, CV, CR> capture) {
        return $isNotNull(lambdaFieldName(capture));
    }

    public <CT> $ $isNotNull(IExecute<CT> capture) {
        return $isNotNull(lambdaFieldName(capture));
    }

    public <T, C extends Collection<T>> $ $in(String column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                condColumnName(link, alias, column) + " in (",
                ")",
                col -> $.$_().
                        $for(col, null, ",", itemFilter,
                                (i, v) -> $.$_(placeholder, v)
                        )
        );
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $in(IGetter<CT, CR> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> $ $in(ISetter<CT, CV> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $in(IFunction<CT, CV, CR> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT> $ $in(IExecute<CT> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>> $ $in(String column, C val, Predicate<T> itemFilter) {
        return $in(column, val, defaultFilter(), itemFilter);
    }

    public <T, C extends Collection<T>, CT, CR> $ $in(IGetter<CT, CR> capture, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> $ $in(ISetter<CT, CV> capture, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $in(IFunction<CT, CV, CR> capture, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT> $ $in(IExecute<CT> capture, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>> $ $in(String column, C val) {
        return $in(column, val, defaultFilter(), null);
    }

    public <T, C extends Collection<T>, CT, CR> $ $in(IGetter<CT, CR> capture, C val) {
        return $in(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT, CV> $ $in(ISetter<CT, CV> capture, C val) {
        return $in(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $in(IFunction<CT, CV, CR> capture, C val) {
        return $in(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT> $ $in(IExecute<CT> capture, C val) {
        return $in(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>> $ $notIn(String column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $trim(val, filter,
                Arrays.asList(","),
                Arrays.asList(","),
                condColumnName(link, alias, column) + " not in (",
                ")",
                col -> $.$_().
                        $for(col, null, ",", itemFilter,
                                (i, v) -> $.$_(placeholder, v)
                        )
        );
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $notIn(IGetter<CT, CR> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> $ $notIn(ISetter<CT, CV> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $notIn(IFunction<CT, CV, CR> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT> $ $notIn(IExecute<CT> capture, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>> $ $notIn(String column, C val, Predicate<T> itemFilter) {
        return $notIn(column, val, defaultFilter(), itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $notIn(IGetter<CT, CR> capture, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> $ $notIn(ISetter<CT, CV> capture, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $notIn(IFunction<CT, CV, CR> capture, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT> $ $notIn(IExecute<CT> capture, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(capture), val, itemFilter);
    }

    public <T, C extends Collection<T>> $ $notIn(String column, C val) {
        return $notIn(column, val, defaultFilter(), null);
    }

    public <T, C extends Collection<T>, CT, CR> $ $notIn(IGetter<CT, CR> capture, C val) {
        return $notIn(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT, CV> $ $notIn(ISetter<CT, CV> capture, C val) {
        return $notIn(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT, CV, CR> $ $notIn(IFunction<CT, CV, CR> capture, C val) {
        return $notIn(lambdaFieldName(capture), val);
    }

    public <T, C extends Collection<T>, CT> $ $notIn(IExecute<CT> capture, C val) {
        return $notIn(lambdaFieldName(capture), val);
    }

    public <T> $ $between(String column, T min, T max, Predicate<T> filter) {
        boolean testMin = filter != null && filter.test(min);
        boolean testMax = filter != null && filter.test(max);
        if (testMin && testMax) {
            return $(condColumnName(link, alias, column)).between().$(placeholder, min).and().$(placeholder, max);
        } else if (testMin) {
            return $gte(column, min);
        } else if (testMax) {
            return $lt(column, max);
        }
        return this;
    }

    public <T, CT, CR> $ $between(IGetter<CT, CR> capture, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(capture), min, max, filter);
    }

    public <T, CT, CV> $ $between(ISetter<CT, CV> capture, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(capture), min, max, filter);
    }

    public <T, CT, CV, CR> $ $between(IFunction<CT, CV, CR> capture, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(capture), min, max, filter);
    }

    public <T, CT> $ $between(IExecute<CT> capture, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(capture), min, max, filter);
    }

    public <T> $ $between(String column, T min, T max) {
        return $between(column, min, max, defaultFilter());
    }

    public <T, CT, CR> $ $between(IGetter<CT, CR> capture, T min, T max) {
        return $between(lambdaFieldName(capture), min, max);
    }

    public <T, CT, CV> $ $between(ISetter<CT, CV> capture, T min, T max) {
        return $between(lambdaFieldName(capture), min, max);
    }

    public <T, CT, CV, CR> $ $between(IFunction<CT, CV, CR> capture, T min, T max) {
        return $between(lambdaFieldName(capture), min, max);
    }

    public <T, CT> $ $between(IExecute<CT> capture, T min, T max) {
        return $between(lambdaFieldName(capture), min, max);
    }


    public <T> $ $exists(T val, Predicate<T> filter, Function<T, $> caller) {
        return $trim(val, filter,
                null, null,
                condColumnName(link, null, " exists ("),
                ")", caller
        );
    }

    public <T> $ $exists(T val, Function<T, $> caller) {
        return $exists(val, defaultFilter(), caller);
    }

    public <T> $ $exists(Supplier<$> caller) {
        return $exists(null, null, v -> caller.get());
    }

    public <T> $ $notExists(T val, Predicate<T> filter, Function<T, $> caller) {
        return $trim(val, filter,
                null, null,
                condColumnName(link, null, " not exists ("),
                ")", caller
        );
    }

    public <T> $ $notExists(T val, Function<T, $> caller) {
        return $notExists(val, defaultFilter(), caller);
    }

    public <T> $ $notExists(Supplier<$> caller) {
        return $notExists(null, null, v -> caller.get());
    }


    public $ insert() {
        return $("insert");
    }

    public $ into() {
        return $("into");
    }

    public $ $into(String table) {
        return into().$(table);
    }

    public $ $into(Class<?> table) {
        return $into(classTableName(table));
    }


    public $ $into(String table, Supplier<$> caller) {
        return into().$(table).$bracket(caller);
    }

    public $ $into(Class<?> table, Supplier<$> caller) {
        return $into(classTableName(table), caller);
    }

    public $ $bracket(Supplier<$> caller) {
        return $trim(null, null,
                Arrays.asList(","),
                Arrays.asList(","),
                "(", ")",
                v -> caller.get());
    }

    public $ $beginBlock(Supplier<$> caller) {
        return $trim(null, null,
                Arrays.asList(",", ";"),
                Arrays.asList(","),
                "begin\n", "\nend",
                v -> caller.get());
    }

    public $ values() {
        return $("values");
    }

    public $ $values(Supplier<$> caller) {
        return values().$bracket(caller);
    }


    public $ update() {
        return $("update");
    }

    public $ $update(String table) {
        return update().$(table);
    }

    public $ $update(Class<?> table) {
        return $update(classTableName(table));
    }


    public $ delete() {
        return $("delete");
    }

    public $ deleteFrom() {
        return delete().from();
    }

    public $ $deleteFrom(String table) {
        return deleteFrom().$(table);
    }

    public $ $deleteFrom(Class<?> table) {
        return $deleteFrom(classTableName(table));
    }

    public $ create() {
        return $("create");
    }

    public $ table() {
        return $("table");
    }

    public $ $table(String table) {
        return table().$(table);
    }

    public $ $table(Class<?> table) {
        return $table(classTableName(table));
    }


    public $ primary() {
        return $("primary");
    }

    public $ key() {
        return $("key");
    }

    public $ primaryKey() {
        return primary().key();
    }

    public $ $default() {
        return $("default");
    }

    public $ $null() {
        return $("null");
    }

    public $ not() {
        return $("not");
    }

    public $ notNull() {
        return not().$null();
    }

    public $ comment() {
        return $("comment");
    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("'", "''");
    }

    public $ $comment(String comment) {
        return comment().$("'" + escapeSql(comment) + "'");
    }

    public $ foreign() {
        return $("foreign");
    }

    public $ references() {
        return $("references");
    }

    public $ foreignKeyReferences() {
        return foreign().key().references();
    }

    public $ $foreignKeyReferences(String table, String column) {
        return foreign().key().references().$(table + "(" + column + ")");
    }

    public <CT, CR> $ $foreignKeyReferences(Class<?> table, IGetter<CT, CR> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT, CV> $ $foreignKeyReferences(Class<?> table, ISetter<CT, CV> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT, CV, CR> $ $foreignKeyReferences(Class<?> table, IFunction<CT, CV, CR> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT> $ $foreignKeyReferences(Class<?> table, IExecute<CT> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public $ unique() {
        return $("unique");
    }

    public $ index() {
        return $("index");
    }

    public $ on() {
        return $("on");
    }

    public $ drop() {
        return $("drop");
    }

    public $ select() {
        return $("select");
    }

    public $ as() {
        return $("as");
    }

    public $ $case() {
        return $("case");
    }

    public $ when() {
        return $("when");
    }

    public $ $else() {
        return $("else");
    }

    public $ end() {
        return $("end");
    }

    public $ from() {
        return $("from");
    }

    public $ and() {
        return $("and");
    }

    public $ or() {
        return $("or");
    }

    public $ like() {
        return $("like");
    }

    public $ sp() {
        return $(" ");
    }

    public $ ln() {
        return $("\n");
    }

    public $ tb() {
        return $("\t");
    }

    public $ cm() {
        return $(",");
    }

    public $ sm() {
        return $(";");
    }


    public $ qt() {
        return $("'");
    }

    public $ qd() {
        return $("\"");
    }

    public $ eq() {
        return $("=");
    }

    public $ gt() {
        return $(">");
    }

    public $ lt() {
        return $("<");
    }

    public $ gte() {
        return $(">=");
    }

    public $ lte() {
        return $("<=");
    }

    public $ neq() {
        return $("!=");
    }

    public $ ne() {
        return $("<>");
    }

    public $ between() {
        return $("between");
    }

    public $ where() {
        return $("where");
    }

    public $ group() {
        return $("group");
    }

    public $ by() {
        return $("by");
    }

    public $ groupBy() {
        return group().by();
    }

    public $ having() {
        return $("having");
    }

    public $ order() {
        return $("order");
    }

    public $ orderBy() {
        return order().by();
    }

    public $ $if() {
        return $("if");
    }

    public $ exists() {
        return $("exists");
    }

    public $ replace() {
        return $("replace");
    }

    public $ ignore() {
        return $("ignore");
    }

    public $ truncate() {
        return $("truncate");
    }

    public $ truncateTable() {
        return truncate().table();
    }

    public $ $truncateTable(String table) {
        return truncate().table().$(table);
    }

    public $ $truncateTable(Class<?> table) {
        return $truncateTable(classTableName(table));
    }

    public $ set() {
        return $("set");
    }

    public $ distinct() {
        return $("distinct");
    }

    public $ top() {
        return $("top");
    }

    public $ limit() {
        return $("limit");
    }

    public $ in() {
        return $("in");
    }

    public $ with() {
        return $("with");
    }

    public $ union() {
        return $("union");
    }

    public $ all() {
        return $("all");
    }

    public $ unionAll() {
        return union().all();
    }

    public $ over() {
        return $("over");
    }

    public $ partition() {
        return $("partition");
    }

    public $ partitionBy() {
        return partition().by();
    }

    public $ database() {
        return $("database");
    }

    public $ check() {
        return $("check");
    }

    public $ show() {
        return $("show");
    }

    public $ databases() {
        return $("databases");
    }

    public $ tables() {
        return $("tables");
    }

    public $ procedure() {
        return $("procedure");
    }

    public $ declare() {
        return $("declare");
    }

    public $ temporary() {
        return $("temporary");
    }

    public $ alter() {
        return $("alter");
    }

    public $ view() {
        return $("view");
    }

    public $ trigger() {
        return $("trigger");
    }

    public $ schema() {
        return $("schema");
    }

    public $ domain() {
        return $("domain");
    }

    public $ catalog() {
        return $("catalog");
    }

    public $ then() {
        return $("then");
    }

    public $ $while() {
        return $("while");
    }

    public $ $do() {
        return $("do");
    }

    public $ repeat() {
        return $("repeat");
    }

    public $ endRepeat() {
        return end().repeat();
    }

    public $ until() {
        return $("until");
    }


    public $ loop() {
        return $("loop");
    }

    public $ delimiter() {
        return $("delimiter");
    }

    public $ delimiter(String delimiter) {
        return delimiter().$(delimiter).ln();
    }

    public $ out() {
        return $("out");
    }

    public $ begin() {
        return $("begin");
    }

    public $ commit() {
        return $("commit");
    }

    public $ rollback() {
        return $("rollback");
    }

    public $ call() {
        return $("call");
    }

    public $ before() {
        return $("before");
    }

    public $ after() {
        return $("after");
    }

    public $ $for() {
        return $("for");
    }

    public $ each() {
        return $("each");
    }

    public $ row() {
        return $("row");
    }

    public $ forEachRow() {
        return $for().each().row();
    }

    public $ forEeachRow(Supplier<$> caller) {
        return forEachRow().$beginBlock(caller);
    }

    public $ cursor() {
        return $("cursor");
    }

    public $ declareCursor(String name, Supplier<$> caller) {
        return declare().cursor().$(name).$for().$(caller.get());
    }

    public $ open() {
        return $("open");
    }

    public $ openCursor(String name) {
        return open().$(name);
    }

    public $ close() {
        return $("close");
    }

    public $ closeCursor(String name) {
        return close().$(name);
    }

    public $ fetch() {
        return $("fetch");
    }

    public $ fetchCursorInto(String name, Supplier<$> caller) {
        return fetch().$(name).into()
                .$trim(Arrays.asList(","),
                        Arrays.asList(","),
                        null, null,
                        caller);
    }


    public $ $int() {
        return $("int");
    }

    public $ bigint() {
        return $("bigint");
    }

    public $ varchar() {
        return $("varchar");
    }

    public $ varchar(int len) {
        return $("varchar(" + len + ")");
    }

    public $ datetime() {
        return $("datetime");
    }

    public $ date() {
        return $("date");
    }

    public $ timestamp() {
        return $("timestamp");
    }

    public $ tinyint() {
        return $("tinyint");
    }

    public $ $char() {
        return $("char");
    }

    public $ $char(int len) {
        return $("char(" + len + ")");
    }

    public $ decimal() {
        return $("decimal");
    }

    public $ decimal(int precision, int scale) {
        return $("decimal(" + precision + "," + scale + ")");
    }

    public $ $double() {
        return $("double");
    }

    public $ $double(int precision, int scale) {
        return $("double(" + precision + "," + scale + ")");
    }

    public $ count() {
        return $("count");
    }

    public $ countAll() {
        return $("count(*)");
    }

    public $ count1() {
        return $("count(1)");
    }

    public $ count(String inner) {
        return $("count(" + inner + ")");
    }

    public $ count(Supplier<$> caller) {
        return $("count(").$(caller.get()).$(")");
    }


}
