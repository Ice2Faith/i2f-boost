package i2f.core.builder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/20 16:07
 * @desc
 */
public class $sql<T extends $<BindSql, ArrayList<BindSql>, BindSql, T>> extends $<BindSql, ArrayList<BindSql>, BindSql, T> {

    public $sql() {
        super(BindSql.class,
                h->new ArrayList<>(),
                (c, e) -> {
                    c.add(e);
                    return c;
                },
                (h,c) -> {
                    StringBuilder builder = new StringBuilder();
                    List<Object> args = new ArrayList<>();
                    if(c.isEmpty()){
                        return new BindSql("");
                    }
                    if(c.size()==1){
                        return c.get(0);
                    }
                    for (BindSql item : c) {
                        builder.append(item.getSql());
                        args.addAll(item.getArgs());
                    }


                    return new BindSql(builder.toString(), args);
                });
        $sep(" ");
    }

    public static <T extends $sql<T>> $sql<T> $_() {
        return new $sql();
    }

    public T $(String sql,Object ... args){
        return $(new BindSql(sql, Arrays.asList(args)));
    }

    public T $sep(String separator) {
        if (separator != null) {
            this.separator = () -> new BindSql(separator);
        }
        return (T) this;
    }

    public T $sep(BindSql separator) {
        if (separator != null) {
            this.separator = () -> separator;
        }
        return (T) this;
    }

    public <V> T $trim(V val, Predicate<V> condition,
                       Function<V, BindSql> prefix,
                       Function<V, BindSql> suffix,
                       Function<V, List<String>> trimPrefix,
                       Function<V, List<String>> trimSuffix,
                       Function<V, BindSql> innerMapper) {
        if (condition != null && !condition.test(val)) {
            return (T) this;
        }
        BindSql sql = innerMapper.apply(val);
        if (this.ignoreNull && sql == null) {
            return (T) this;
        }
        if (sql != null && trimPrefix != null) {
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
        if (sql != null && trimSuffix != null) {
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
        if (sql != null) {
            sql.setSql(sql.getSql().trim());
        }
        if (this.ignoreNull && "".equals(sql)) {
            return (T) this;
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
        return (T) this;
    }

    public <V> T $where(V val, Predicate<V> condition,
                        Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("where"),
                null,
                e->Arrays.asList("and","or","AND","OR"),
                e->Arrays.asList("and","or","AND","OR"),
                innerMapper);
    }

    public <V> T $and(V val, Predicate<V> condition,
                        Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("and ("),
                e->new BindSql(")"),
                e->Arrays.asList("and","or","AND","OR"),
                e->Arrays.asList("and","or","AND","OR"),
                innerMapper);
    }

    public <V> T $or(V val, Predicate<V> condition,
                      Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("or ("),
                e->new BindSql(")"),
                e->Arrays.asList("and","or","AND","OR"),
                e->Arrays.asList("and","or","AND","OR"),
                innerMapper);
    }

    public <V> T $exists(V val, Predicate<V> condition,
                     Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("exists ("),
                e->new BindSql(")"),
                e->Arrays.asList("and","or","AND","OR"),
                e->Arrays.asList("and","or","AND","OR"),
                innerMapper);
    }

    public <V> T $set(V val, Predicate<V> condition,
                         Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("set"),
                null,
                e->Arrays.asList(","),
                e->Arrays.asList(","),
                innerMapper);
    }

    public <V> T $into(V val, Predicate<V> condition,
                       Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("into ("),
                e->new BindSql(")"),
                e->Arrays.asList(","),
                e->Arrays.asList(","),
                innerMapper);
    }

    public <V> T $values(V val, Predicate<V> condition,
                      Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("values ("),
                e->new BindSql(")"),
                e->Arrays.asList(","),
                e->Arrays.asList(","),
                innerMapper);
    }

    public <V> T $select(V val, Predicate<V> condition,
                         Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("select"),
                null,
                e->Arrays.asList(","),
                e->Arrays.asList(","),
                innerMapper);
    }

    public <V,C extends Iterable<V>> T $in(C val, Predicate<C> condition,
                         Function<V, BindSql> innerMapper){
        return $trim(val,condition,
                e->new BindSql("in ("),
                e->new BindSql(")"),
                e->Arrays.asList(","),
                e->Arrays.asList(","),
                e->$sql.$_().$for(val,condition,
                        null,(v)->new BindSql(","),
                        null,(v,i)->innerMapper.apply(v))
                .$$()
        );
    }
}
