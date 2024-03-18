package i2f.database.sql;


import i2f.database.jdbc.data.BindSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/3/18 8:56
 * @desc
 */
public class $ {
    protected List<BindSql> sqls = new ArrayList<>();
    protected String separator = " ";

    public $() {

    }

    public $(BindSql sql) {
        this.sqls.add(sql);
    }

    public static $ $_() {
        return new $();
    }

    public static $ $_(BindSql sql) {
        if (sql == null) {
            return new $();
        }
        return new $(sql);
    }

    public static <T> T[] array(T... arr) {
        return arr;
    }

    public static <T> List<T> array2list(T[] args) {
        List<T> ret = new ArrayList<>();
        if (args == null) {
            return ret;
        }
        for (T arg : args) {
            ret.add(arg);
        }
        return ret;
    }

    public static $ $_(String sql, Object... args) {
        return new $(new BindSql(sql, array2list(args)));
    }

    public $ $sep(String str) {
        this.separator = str;
        return this;
    }

    public $ $sepSpace() {
        this.separator = " ";
        return this;
    }

    public $ $sepNone() {
        this.separator = null;
        return this;
    }

    public $ $sepComma() {
        this.separator = ",";
        return this;
    }

    public $ $sepLine() {
        this.separator = "\n";
        return this;
    }

    public $ $($ sql) {
        if (sql == null) {
            return this;
        }
        BindSql bsql = sql.$$();
        return $(bsql);
    }

    public $ $(BindSql sql) {
        if (sql == null) {
            return this;
        }
        if (this.separator != null) {
            this.sqls.add(new BindSql(this.separator));
        }
        this.sqls.add(sql);
        return this;
    }

    public $ $(String sql, Object... args) {
        return $(new BindSql(sql, array2list(args)));
    }

    public static BindSql $$(String sql, Object... args) {
        return $.$_(sql, args).$$();
    }

    public BindSql $$() {
        if (this.sqls.isEmpty()) {
            return new BindSql("");
        }
        if (this.sqls.size() == 1) {
            return this.sqls.get(0);
        }
        StringBuilder builder = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (BindSql sql : this.sqls) {
            builder.append(sql.getSql());
            args.addAll(sql.getArgs());
        }
        return new BindSql(builder.toString(), args);
    }

    public <T> $ $if(T val, Predicate<T> condition, String sql, Object... args) {
        return $if(val, condition, e -> new BindSql(sql, array2list(args)));
    }

    public <T> $ $if(T val, Predicate<T> condition, BindSql sql) {
        return $if(val, condition, e -> sql);
    }

    public <T> $ $if(T val, Predicate<T> condition, $ sql) {
        return $if(val, condition, e -> sql.$$());
    }

    public <T> $ $if(T val, Predicate<T> condition, Function<T, BindSql> processor) {
        if (condition == null || condition.test(val)) {
            return $(processor.apply(val));
        }
        return this;
    }

    public <T> $ $if$(T val, Predicate<T> condition, BiFunction<T, $, $> processor) {
        return $if(val, condition, e -> processor.apply(e, $.$_()).$$());
    }

    public <E, C extends Iterable<E>> $ $for(C val,
                                             String separator,
                                             BiFunction<E, Integer, BindSql> itemMapper) {
        return $for(val, null, e -> e, separator, itemMapper);
    }

    public <E, C extends Iterable<E>> $ $for$(C val,
                                              String separator,
                                              BiFunction<E, $, $> itemMapper) {
        return $for(val, separator, (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <E, C extends Iterable<E>> $ $for(C val, Predicate<C> condition,
                                             String separator,
                                             BiFunction<E, Integer, BindSql> itemMapper) {
        return $for(val, condition, e -> e, separator, itemMapper);
    }

    public <E, C extends Iterable<E>> $ $for$(C val, Predicate<C> condition,
                                              String separator,
                                              BiFunction<E, $, $> itemMapper) {
        return $for(val, condition, separator, (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <E, C extends Iterable<E>> $ $for(C val, Predicate<C> condition,
                                             Function<C, BindSql> separator,
                                             BiFunction<E, Integer, BindSql> itemMapper) {
        return $for(val, condition, e -> e, separator, itemMapper);
    }

    public <E, C extends Iterable<E>> $ $for$(C val, Predicate<C> condition,
                                              BiFunction<C, $, $> separator,
                                              BiFunction<E, $, $> itemMapper) {
        return $for(val, condition, (separator == null ? null : e -> separator.apply(e, $.$_()).$$()),
                (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <T, E, C extends Iterable<E>> $ $for(T val, Predicate<T> condition,
                                                Function<T, C> iterableMapper,
                                                String separator,
                                                BiFunction<E, Integer, BindSql> itemMapper) {
        return $for(val, condition, iterableMapper,
                e -> (separator == null ? null : new BindSql(separator)),
                itemMapper);
    }

    public <T, E, C extends Iterable<E>> $ $for$(T val, Predicate<T> condition,
                                                 Function<T, C> iterableMapper,
                                                 String separator,
                                                 BiFunction<E, $, $> itemMapper) {
        return $for(val, condition, iterableMapper, separator, (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <T, E, C extends Iterable<E>> $ $for(T val, Predicate<T> condition,
                                                Function<T, C> iterableMapper,
                                                Function<T, BindSql> separator,
                                                BiFunction<E, Integer, BindSql> itemMapper) {
        if (condition != null && !condition.test(val)) {
            return this;
        }
        C col = iterableMapper.apply(val);
        int i = 0;
        for (E item : col) {
            BindSql sql = itemMapper.apply(item, i);
            if (sql == null) {
                continue;
            }
            if (i > 0) {
                if (separator != null) {
                    BindSql sep = separator.apply(val);
                    if (sep != null) {
                        this.$(sep);
                    }
                }
            }
            this.$(sql);
            i++;
        }
        return this;
    }

    public <T, E, C extends Iterable<E>> $ $for$(T val, Predicate<T> condition,
                                                 Function<T, C> iterableMapper,
                                                 BiFunction<T, $, $> separator,
                                                 BiFunction<E, $, $> itemMapper) {
        return $for(val, condition, iterableMapper,
                (separator == null ? null : e -> separator.apply(e, $.$_()).$$()),
                (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public $ $trim(String prefix,
                   String suffix,
                   String[] trimPrefix,
                   String[] trimSuffix,
                   Supplier<BindSql> innerMapper) {
        return $trim(null, null,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> innerMapper.get());
    }

    public $ $trim$(String prefix,
                    String suffix,
                    String[] trimPrefix,
                    String[] trimSuffix,
                    Function<$, $> innerMapper) {
        return $trim(prefix, suffix,
                trimPrefix, trimSuffix,
                () -> innerMapper.apply($.$_()).$$());
    }

    public $ $trim(String prefix,
                   String suffix,
                   List<String> trimPrefix,
                   List<String> trimSuffix,
                   Supplier<BindSql> innerMapper) {
        return $trim(null, null,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> innerMapper.get());
    }

    public $ $trim$(String prefix,
                    String suffix,
                    List<String> trimPrefix,
                    List<String> trimSuffix,
                    Function<$, $> innerMapper) {
        return $trim(prefix, suffix,
                trimPrefix, trimSuffix,
                () -> innerMapper.apply($.$_()).$$());
    }

    public $ $trim(Supplier<BindSql> prefix,
                   Supplier<BindSql> suffix,
                   Supplier<List<String>> trimPrefix,
                   Supplier<List<String>> trimSuffix,
                   Supplier<BindSql> innerMapper) {
        return $trim(null, null,
                e -> prefix.get(), e -> suffix.get(),
                e -> trimPrefix.get(), e -> trimSuffix.get(),
                e -> innerMapper.get());
    }

    public $ $trim$(Function<$, $> prefix,
                    Function<$, $> suffix,
                    Supplier<List<String>> trimPrefix,
                    Supplier<List<String>> trimSuffix,
                    Function<$, $> innerMapper) {
        return $trim((prefix == null ? null : () -> prefix.apply($.$_()).$$()),
                (suffix == null ? null : () -> suffix.apply($.$_()).$$()),
                trimPrefix, trimSuffix,
                () -> innerMapper.apply($.$_()).$$());
    }

    public <T> $ $trim(T val, Predicate<T> condition,
                       String prefix,
                       String suffix,
                       String[] trimPrefix,
                       String[] trimSuffix,
                       Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                e -> (prefix == null ? null : new BindSql(prefix)),
                e -> (suffix == null ? null : new BindSql(suffix)),
                e -> (trimPrefix == null || trimPrefix.length == 0 ? null : array2list(trimPrefix)),
                e -> (trimSuffix == null || trimSuffix.length == 0 ? null : array2list(trimSuffix)),
                innerMapper);
    }

    public <T> $ $trim$(T val, Predicate<T> condition,
                        String prefix,
                        String suffix,
                        String[] trimPrefix,
                        String[] trimSuffix,
                        BiFunction<T, $, $> innerMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> innerMapper.apply(e, $.$_()).$$());
    }

    public <T> $ $trim(T val, Predicate<T> condition,
                       String prefix,
                       String suffix,
                       List<String> trimPrefix,
                       List<String> trimSuffix,
                       Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                e -> (prefix == null ? null : new BindSql(prefix)),
                e -> (suffix == null ? null : new BindSql(suffix)),
                e -> (trimPrefix),
                e -> (trimSuffix),
                innerMapper);
    }

    public <T> $ $trim$(T val, Predicate<T> condition,
                        String prefix,
                        String suffix,
                        List<String> trimPrefix,
                        List<String> trimSuffix,
                        BiFunction<T, $, $> innerMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> innerMapper.apply(e, $.$_()).$$());
    }

    public <T> $ $trim(T val, Predicate<T> condition,
                       Function<T, BindSql> prefix,
                       Function<T, BindSql> suffix,
                       Function<T, List<String>> trimPrefix,
                       Function<T, List<String>> trimSuffix,
                       Function<T, BindSql> innerMapper) {
        if (condition != null && !condition.test(val)) {
            return this;
        }
        BindSql sql = innerMapper.apply(val);
        if (sql == null) {
            return this;
        }
        if (trimPrefix != null) {
            List<String> list = trimPrefix.apply(val);
            if (list != null) {
                String str = sql.getSql();
                for (String item : list) {
                    str = str.trim();
                    if (str.startsWith(item)) {
                        str = str.substring(item.length());
                        break;
                    }
                }
                sql.setSql(str);
            }
        }
        if (trimSuffix != null) {
            List<String> list = trimSuffix.apply(val);
            if (list != null) {
                String str = sql.getSql();
                for (String item : list) {
                    str = str.trim();
                    if (str.endsWith(item)) {
                        str = str.substring(0, str.length() - item.length());
                        break;
                    }
                }
                sql.setSql(str);
            }
        }
        sql.setSql(sql.getSql().trim());
        if ("".equals(sql.getSql())) {
            return this;
        }
        if (prefix != null) {
            BindSql pre = prefix.apply(val);
            if (pre != null) {
                this.$(pre);
            }
        }
        this.$(sql);
        if (suffix != null) {
            BindSql suf = suffix.apply(val);
            if (suf != null) {
                this.$(suf);
            }
        }
        return this;
    }

    public <T> $ $trim$(T val, Predicate<T> condition,
                        BiFunction<T, $, $> prefix,
                        BiFunction<T, $, $> suffix,
                        Function<T, List<String>> trimPrefix,
                        Function<T, List<String>> trimSuffix,
                        BiFunction<T, $, $> innerMapper) {
        return $trim(val, condition,
                (prefix == null ? null : e -> prefix.apply(e, $.$_()).$$()),
                (suffix == null ? null : e -> suffix.apply(e, $.$_()).$$()),
                trimPrefix, trimSuffix,
                e -> innerMapper.apply(e, $.$_()).$$());
    }

    public <E, C extends Iterable<E>> $ $foreach(C val, Predicate<C> condition,
                                                 String prefix,
                                                 String suffix,
                                                 String[] trimPrefix,
                                                 String[] trimSuffix,
                                                 String separator,
                                                 BiFunction<E, Integer, BindSql> itemMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> $.$_().$for(val, condition,
                        separator,
                        itemMapper).$$()
        );
    }

    public <E, C extends Iterable<E>> $ $foreach$(C val, Predicate<C> condition,
                                                  String prefix,
                                                  String suffix,
                                                  String[] trimPrefix,
                                                  String[] trimSuffix,
                                                  String separator,
                                                  BiFunction<E, $, $> itemMapper) {
        return $foreach(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                separator, (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <E, C extends Iterable<E>> $ $foreach(C val, Predicate<C> condition,
                                                 String prefix,
                                                 String suffix,
                                                 List<String> trimPrefix,
                                                 List<String> trimSuffix,
                                                 String separator,
                                                 BiFunction<E, Integer, BindSql> itemMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> $.$_().$for(val, condition,
                        separator,
                        itemMapper).$$()
        );
    }

    public <E, C extends Iterable<E>> $ $foreach$(C val, Predicate<C> condition,
                                                  String prefix,
                                                  String suffix,
                                                  List<String> trimPrefix,
                                                  List<String> trimSuffix,
                                                  String separator,
                                                  BiFunction<E, $, $> itemMapper) {
        return $foreach(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                separator, (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <T, E, C extends Iterable<E>> $ $foreach(T val, Predicate<T> condition,
                                                    String prefix,
                                                    String suffix,
                                                    List<String> trimPrefix,
                                                    List<String> trimSuffix,
                                                    Function<T, C> iterableMapper,
                                                    String separator,
                                                    BiFunction<E, Integer, BindSql> itemMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> $.$_().$for(val, condition,
                        iterableMapper,
                        separator,
                        itemMapper).$$()
        );
    }

    public <T, E, C extends Iterable<E>> $ $foreach$(T val, Predicate<T> condition,
                                                     String prefix,
                                                     String suffix,
                                                     List<String> trimPrefix,
                                                     List<String> trimSuffix,
                                                     Function<T, C> iterableMapper,
                                                     String separator,
                                                     BiFunction<E, $, $> itemMapper) {
        return $foreach(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                iterableMapper, separator,
                (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public <T, E, C extends Iterable<E>> $ $foreach(T val, Predicate<T> condition,
                                                    Function<T, BindSql> prefix,
                                                    Function<T, BindSql> suffix,
                                                    Function<T, List<String>> trimPrefix,
                                                    Function<T, List<String>> trimSuffix,
                                                    Function<T, C> iterableMapper,
                                                    Function<T, BindSql> separator,
                                                    BiFunction<E, Integer, BindSql> itemMapper) {
        return $trim(val, condition,
                prefix, suffix,
                trimPrefix, trimSuffix,
                e -> $.$_()
                        .$for(val, condition,
                                iterableMapper,
                                separator,
                                itemMapper)
                        .$$());
    }

    public <T, E, C extends Iterable<E>> $ $foreach$(T val, Predicate<T> condition,
                                                     BiFunction<T, $, $> prefix,
                                                     BiFunction<T, $, $> suffix,
                                                     Function<T, List<String>> trimPrefix,
                                                     Function<T, List<String>> trimSuffix,
                                                     Function<T, C> iterableMapper,
                                                     BiFunction<T, $, $> separator,
                                                     BiFunction<E, $, $> itemMapper) {
        return $foreach(val, condition,
                (prefix == null ? null : e -> prefix.apply(e, $.$_()).$$()),
                (suffix == null ? null : e -> suffix.apply(e, $.$_()).$$()),
                trimPrefix, trimSuffix,
                iterableMapper,
                (separator == null ? null : e -> separator.apply(e, $.$_()).$$()),
                (e, i) -> itemMapper.apply(e, $.$_()).$$());
    }

    public $ $sp() {
        this.sqls.add(new BindSql(" "));
        return this;
    }

    public $ $ln() {
        this.sqls.add(new BindSql("\n"));
        return this;
    }

    public $ $tb() {
        this.sqls.add(new BindSql("\t"));
        return this;
    }

    public $ $cm(){
        this.sqls.add(new BindSql(","));
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public $ $val(Object val) {
        return $("?", val);
    }

    public $ $select(Function<$, BindSql> innerMapper) {
        return select()
                .$comma(e -> innerMapper.apply($.$_().$sepComma()));
    }

    public $ $select$(Function<$, $> innerMapper) {
        return $select(e -> innerMapper.apply(e).$$());
    }

    public $ $from(String name) {
        return $from(name, null);
    }

    public $ $from(String name, String alias) {
        if (alias != null && !"".equals(alias)) {
            name = name + " " + alias;
        }
        return from().$(name);
    }

    public <T> $ $from(T val, Function<T, BindSql> tableMapper, Function<T, String> aliasMapper) {
        return $trim(val, null,
                e -> new BindSql("from ("),
                e -> {
                    String suf = ")";
                    if (aliasMapper != null) {
                        String alias = aliasMapper.apply(e);
                        if (alias != null && !"".equals(alias)) {
                            suf += " " + alias;
                        }
                    }
                    return new BindSql(suf);
                },
                null,
                null,
                tableMapper);
    }

    public <T> $ $from$(T val, BiFunction<T, $, $> tableMapper, Function<T, String> aliasMapper) {
        return $from(val, e -> tableMapper.apply(e, $.$_()).$$(), aliasMapper);
    }

    public $ $join(String name) {
        return $join(name, null);
    }

    public $ $join(String name, String alias) {
        if (alias != null && !"".equals(alias)) {
            name = name + " " + alias;
        }
        return join().$(name);
    }

    public <T> $ $join(T val, Function<T, BindSql> tableMapper, Function<T, String> aliasMapper) {
        return $trim(val, null,
                e -> new BindSql("join ("),
                e -> {
                    String suf = ")";
                    if (aliasMapper != null) {
                        String alias = aliasMapper.apply(e);
                        if (alias != null && !"".equals(alias)) {
                            suf += " " + alias;
                        }
                    }
                    return new BindSql(suf);
                },
                null,
                null,
                tableMapper);
    }

    public <T> $ $join$(T val, BiFunction<T, $, $> tableMapper, Function<T, String> aliasMapper) {
        return $join(val, e -> tableMapper.apply(e, $.$_()).$$(), aliasMapper);
    }

    public $ $where(Function<$, BindSql> innerMapper) {
        return $where(null, null, v -> innerMapper.apply($.$_()));
    }

    public $ $where(Supplier<BindSql> innerMapper) {
        return $where(null, null, v -> innerMapper.get());
    }

    public $ $where$(Function<$, $> innerMapper) {
        return $where(null, null, e -> innerMapper.apply($.$_()).$$());
    }

    public <T> $ $where(T val, Predicate<T> condition,
                        Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                "where", null,
                Arrays.asList("and", "or", "AND", "OR"), null,
                innerMapper);
    }

    public <T> $ $where$(T val, Predicate<T> condition,
                         BiFunction<T, $, $> innerMapper) {
        return $where(val, condition, e -> innerMapper.apply(e, $.$_()).$$());
    }

    public $ $arr$(Function<$, $> innerMapper) {
        return $comma$(e -> innerMapper.apply($.$_().$sepComma()));
    }

    public $ $vals$(Function<$, $> innerMapper) {
        return $bracket$(s -> s.$comma$(e -> innerMapper.apply($.$_().$sepComma())));
    }

    public $ $values$(Function<$, $> innerMapper) {
        return values().$bracket$(s -> s.$comma$(e -> innerMapper.apply($.$_().$sepComma())));
    }

    public <T> $ $update(String name) {
        return $update(name, v -> v);
    }

    public <T> $ $update(T val, Function<T, String> nameMapper) {
        return update().$(nameMapper.apply(val));
    }

    public $ $set(Function<$, BindSql> innerMapper) {
        return $set(null, null, e -> innerMapper.apply($.$_().$sepComma()));
    }

    public $ $set(Supplier<BindSql> innerMapper) {
        return $set(null, null, e -> innerMapper.get());
    }

    public $ $set$(Function<$, $> innerMapper) {
        return $set(null, null, e -> innerMapper.apply($.$_().$sepComma()).$$());
    }

    public <T> $ $set(T val, Predicate<T> condition,
                      Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                "set", null,
                Arrays.asList(","),
                Arrays.asList(","),
                innerMapper);
    }

    public <T> $ $set$(T val, Predicate<T> condition,
                       BiFunction<T, $, $> innerMapper) {
        return $set(val, condition, e -> innerMapper.apply(e, $.$_().$sepComma()).$$());
    }

    public $ $comma(Function<$, BindSql> innerMapper) {
        return $comma(null, null, e -> innerMapper.apply($.$_()));
    }

    public $ $comma$(Function<$, $> innerMapper) {
        return $comma(null, null, e -> innerMapper.apply($.$_()).$$());
    }

    public $ $comma(Supplier<BindSql> innerMapper) {
        return $comma(null, null, e -> innerMapper.get());
    }

    public <T> $ $comma(T val, Predicate<T> condition,
                        Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                null, null,
                Arrays.asList(","),
                Arrays.asList(","),
                innerMapper);
    }

    public <T> $ $comma$(T val, Predicate<T> condition,
                         BiFunction<T, $, $> innerMapper) {
        return $comma(val, condition, e -> innerMapper.apply(e, $.$_()).$$());
    }

    public $ $bracket(Function<$, BindSql> innerMapper) {
        return $bracket(null, null, e -> innerMapper.apply($.$_()));
    }

    public $ $bracket$(Function<$, $> innerMapper) {
        return $bracket(null, null, e -> innerMapper.apply($.$_()).$$());
    }

    public $ $bracket(Supplier<BindSql> innerMapper) {
        return $bracket(null, null, e -> innerMapper.get());
    }

    public <T> $ $bracket(T val, Predicate<T> condition,
                          Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                "(", ")",
                new String[0],
                new String[0],
                innerMapper);
    }

    public <T> $ $bracket$(T val, Predicate<T> condition,
                           BiFunction<T, $, $> innerMapper) {
        return $bracket(val, condition, e -> innerMapper.apply(e, $.$_()).$$());
    }

    public $ $and(Function<$, BindSql> innerMapper) {
        return $and(null, null, e -> innerMapper.apply($.$_()));
    }

    public $ $and$(Function<$, $> innerMapper) {
        return $and(null, null, e -> innerMapper.apply($.$_()).$$());
    }

    public $ $and(Supplier<BindSql> innerMapper) {
        return $and(null, null, e -> innerMapper.get());
    }

    public <T> $ $and(T val, Predicate<T> condition,
                      Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                "and (", ")",
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                innerMapper);
    }

    public <T> $ $and$(T val, Predicate<T> condition,
                       BiFunction<T, $, $> innerMapper) {
        return $and(val, condition,
                e -> innerMapper.apply(e, $.$_()).$$()
        );
    }

    public $ $or(Function<$, BindSql> innerMapper) {
        return $and(null, null, e -> innerMapper.apply($.$_()));
    }

    public $ $or$(Function<$, $> innerMapper) {
        return $and(null, null, e -> innerMapper.apply($.$_()).$$());
    }

    public $ $or(Supplier<BindSql> innerMapper) {
        return $and(null, null, e -> innerMapper.get());
    }

    public <T> $ $or(T val, Predicate<T> condition,
                     Function<T, BindSql> innerMapper) {
        return $trim(val, condition,
                "or (", ")",
                Arrays.asList("and", "or", "AND", "OR"),
                Arrays.asList("and", "or", "AND", "OR"),
                innerMapper);
    }

    public <T> $ $or$(T val, Predicate<T> condition,
                      BiFunction<T, $, $> innerMapper) {
        return $or(val, condition, e -> innerMapper.apply(e, $.$_()).$$());
    }

    public static String prefixColumnName(String prefix, String column) {
        if (prefix != null && !"".equals(prefix)) {
            if (prefix.endsWith(".")) {
                column = prefix + column;
            } else {
                column = prefix + "." + column;
            }
        }
        return column;
    }

    public $ $col(String prefix, String column) {
        return this.$col(prefix, column, null);
    }

    public $ $col(String prefix, String column, String asName) {
        column = prefixColumnName(prefix, column);
        if (asName != null && !"".equals(asName)) {
            column += " as " + asName;
        }
        return this.$(column);
    }

    public <T> $ $eq(String column, T val) {
        return $eq(null, column, val);
    }

    public <T> $ $eq(String prefix, String column, T val) {
        return $eq(prefix, column, val, null);
    }

    public <T> $ $eq(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " = ?", val);
    }

    public <T> $ $gt(String column, T val) {
        return $gt(null, column, val);
    }

    public <T> $ $gt(String prefix, String column, T val) {
        return $gt(prefix, column, val, null);
    }

    public <T> $ $gt(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " > ?", val);
    }

    public <T> $ $lt(String column, T val) {
        return $lt(null, column, val);
    }

    public <T> $ $lt(String prefix, String column, T val) {
        return $lt(prefix, column, val, null);
    }

    public <T> $ $lt(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " < ?", val);
    }

    public <T> $ $gte(String column, T val) {
        return $gte(null, column, val);
    }

    public <T> $ $gte(String prefix, String column, T val) {
        return $gte(prefix, column, val, null);
    }

    public <T> $ $gte(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " >= ?", val);
    }

    public <T> $ $lte(String column, T val) {
        return $lte(null, column, val);
    }

    public <T> $ $lte(String prefix, String column, T val) {
        return $lte(prefix, column, val, null);
    }

    public <T> $ $lte(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " <= ?", val);
    }

    public <T> $ $neq(String column, T val) {
        return $neq(null, column, val);
    }

    public <T> $ $neq(String prefix, String column, T val) {
        return $neq(prefix, column, val, null);
    }

    public <T> $ $neq(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " != ?", val);
    }

    public <T> $ $like(String column, T val) {
        return $like(null, column, val);
    }

    public <T> $ $like(String prefix, String column, T val) {
        return $like(prefix, column, val, null);
    }

    public <T> $ $like(String prefix, String column, T val, Predicate<T> condition) {
        return $if(val, condition, prefixColumnName(prefix, column) + " like ?", "?" + val + "?");
    }

    public <E, C extends Iterable<E>> $ $in(String column, C val) {
        return $in(null, column, val);
    }

    public <E, C extends Iterable<E>> $ $in(String prefix, String column, C val) {
        return $in(prefix, column, val, e -> e, null);
    }

    public <E, C extends Iterable<E>> $ $in(String prefix, String column, C val,
                                            Predicate<C> condition) {
        return $in(prefix, column, val, e -> e, condition);
    }

    public <T, E, C extends Iterable<E>> $ $in(String prefix, String column, T val,
                                               Function<T, C> iterableMapper,
                                               Predicate<T> condition) {
        return $foreach(val, condition,
                prefixColumnName(prefix, column) + " in (",
                ")",
                null,
                null,
                iterableMapper,
                ",",
                (e, i) -> new BindSql("?", Arrays.asList(e))
        );
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final BindSql WITH = new BindSql("with");
    public static final BindSql SELECT = new BindSql("select");
    public static final BindSql AS = new BindSql("as");
    public static final BindSql CASE = new BindSql("case");
    public static final BindSql WHEN = new BindSql("when");
    public static final BindSql THEN = new BindSql("then");
    public static final BindSql ELSE = new BindSql("else");
    public static final BindSql END = new BindSql("end");
    public static final BindSql FROM = new BindSql("from");
    public static final BindSql JOIN = new BindSql("join");
    public static final BindSql INNER = new BindSql("inner");
    public static final BindSql LEFT = new BindSql("left");
    public static final BindSql RIGHT = new BindSql("right");
    public static final BindSql ON = new BindSql("on");
    public static final BindSql WHERE = new BindSql("where");
    public static final BindSql AND = new BindSql("and");
    public static final BindSql OR = new BindSql("or");
    public static final BindSql LIKE = new BindSql("like");
    public static final BindSql IN = new BindSql("in");
    public static final BindSql EQ = new BindSql("=");
    public static final BindSql GT = new BindSql(">");
    public static final BindSql LT = new BindSql("<");
    public static final BindSql GTE = new BindSql(">=");
    public static final BindSql LTE = new BindSql("<=");
    public static final BindSql NEQ = new BindSql("!=");
    public static final BindSql GROUP = new BindSql("group");
    public static final BindSql BY = new BindSql("by");
    public static final BindSql HAVING = new BindSql("having");
    public static final BindSql ORDER = new BindSql("order");
    public static final BindSql ASC = new BindSql("asc");
    public static final BindSql DESC = new BindSql("desc");
    public static final BindSql LIMIT = new BindSql("limit");
    public static final BindSql INSERT = new BindSql("insert");
    public static final BindSql INTO = new BindSql("into");
    public static final BindSql VALUES = new BindSql("values");
    public static final BindSql UNION = new BindSql("union");
    public static final BindSql ALL = new BindSql("all");
    public static final BindSql UPDATE = new BindSql("update");
    public static final BindSql SET = new BindSql("set");
    public static final BindSql DELETE = new BindSql("delete");
    public static final BindSql CREATE = new BindSql("create");
    public static final BindSql TABLE = new BindSql("table");
    public static final BindSql VIEW = new BindSql("view");
    public static final BindSql INDEX = new BindSql("index");
    public static final BindSql DROP = new BindSql("drop");

    public $ with() {
        return $(WITH);
    }

    public $ select() {
        return $(SELECT);
    }

    public $ as() {
        return $(AS);
    }

    public $ $case() {
        return $(CASE);
    }

    public $ when() {
        return $(WHEN);
    }

    public $ then() {
        return $(THEN);
    }

    public $ $else() {
        return $(ELSE);
    }

    public $ end() {
        return $(END);
    }

    public $ from() {
        return $(FROM);
    }

    public $ join() {
        return $(JOIN);
    }

    public $ inner() {
        return $(INNER);
    }

    public $ left() {
        return $(LEFT);
    }

    public $ right() {
        return $(RIGHT);
    }

    public $ on() {
        return $(ON);
    }

    public $ where() {
        return $(WHERE);
    }

    public $ and() {
        return $(AND);
    }

    public $ or() {
        return $(OR);
    }

    public $ like() {
        return $(LIKE);
    }

    public $ in() {
        return $(IN);
    }

    public $ eq() {
        return $(EQ);
    }

    public $ gt() {
        return $(GT);
    }

    public $ lt() {
        return $(LT);
    }

    public $ gte() {
        return $(GTE);
    }

    public $ lte() {
        return $(LTE);
    }

    public $ neq() {
        return $(NEQ);
    }

    public $ group() {
        return $(GROUP);
    }

    public $ by() {
        return $(BY);
    }

    public $ having() {
        return $(HAVING);
    }

    public $ order() {
        return $(ORDER);
    }

    public $ asc() {
        return $(ASC);
    }

    public $ desc() {
        return $(DESC);
    }

    public $ limit() {
        return $(LIMIT);
    }

    public $ insert() {
        return $(INSERT);
    }

    public $ into() {
        return $(INTO);
    }

    public $ values() {
        return $(VALUES);
    }

    public $ union() {
        return $(UNION);
    }

    public $ all() {
        return $(ALL);
    }

    public $ update() {
        return $(UPDATE);
    }

    public $ set() {
        return $(SET);
    }

    public $ delete() {
        return $(DELETE);
    }

    public $ create() {
        return $(CREATE);
    }

    public $ table() {
        return $(TABLE);
    }

    public $ view() {
        return $(VIEW);
    }

    public $ index() {
        return $(INDEX);
    }

    public $ drop() {
        return $(DROP);
    }
}
