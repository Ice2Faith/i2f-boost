package i2f.database.sql;


import i2f.database.jdbc.data.BindSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/15 22:25
 * @desc
 */
public class $ {
    protected List<BindSql> list;

    public $() {
        this.list = new ArrayList<>();
    }

    public static $ $() {
        return new $();
    }

    public static BindSql $$(String sql, Object... args) {
        return $.$().$(sql, args).$$();
    }

    public BindSql $$() {
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder builder = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (BindSql item : list) {
            if (item.getSql() != null) {
                builder.append(" ");
                builder.append(item.getSql());
                if (item.getArgs() != null) {
                    args.addAll(item.getArgs());
                }
            }
        }
        String sql = builder.toString();
        return new BindSql(sql, args);
    }

    public $ $(String sql, Object... args) {
        this.list.add(new BindSql(sql, new ArrayList<>(Arrays.asList(args))));
        return this;
    }

    public $ $(BindSql sql) {
        this.list.add(sql);
        return this;
    }

    public <T> $ $(T val, Predicate<T> filter, String sql, Object... args) {
        return $(val, filter, e -> new BindSql(sql, new ArrayList<>(Arrays.asList(args))));
    }

    public <T> $ $(T val, Predicate<T> filter, BindSql sql) {
        return $(val, filter, e -> sql);
    }

    public <T> $ $(T val, Predicate<T> filter, Function<T, BindSql> mapper) {
        if (filter == null || filter.test(val)) {
            BindSql sql = mapper.apply(val);
            if (sql != null) {
                this.list.add(sql);
            }
        }
        return this;
    }

    public $ $val(Object val) {
        this.list.add(new BindSql("?", new ArrayList<>(Arrays.asList(val))));
        return this;
    }

    public <E, C extends Collection<E>> $ $in(String name, C col, Predicate<C> filter) {
        return $trim(col, filter,
                e -> new BindSql(name + " in ("),
                e -> new BindSql(")"),
                null, null,
                v -> $.$().$foreach(v, filter,
                        e -> e,
                        e -> COMMA,
                        (e, i) -> $.$$("?", e)).$$());
    }

    public <T> $ $like(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " like ?", val);
    }

    public <T> $ $eq(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " = ?", val);
    }

    public <T> $ $gt(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " > ?", val);
    }

    public <T> $ $lt(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " < ?", val);
    }

    public <T> $ $gte(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " >= ?", val);
    }

    public <T> $ $lte(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " <= ?", val);
    }

    public <T> $ $neq(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " != ?", val);
    }

    public <T> $ $isNull(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " is null");
    }

    public <T> $ $isNotNull(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " is not null");
    }

    public <T> $ $eqNull(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " = null");
    }

    public <T> $ $neqNull(String name, T val, Predicate<T> filter) {
        return $(val, filter, name + " != null");
    }

    public <T> $ $where(T val, Predicate<T> filter,
                        Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> WHERE, null,
                e -> Arrays.asList("and", "or", "AND", "OR"),
                null, inner);
    }

    public <T> $ $exists(T val, Predicate<T> filter,
                         Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> new BindSql("exists ("),
                e -> new BindSql(")"),
                e -> Arrays.asList("and", "or", "AND", "OR"),
                null, inner);
    }

    public <T> $ $set(T val, Predicate<T> filter,
                      Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> SET, null,
                e -> Arrays.asList(","),
                e -> Arrays.asList(","),
                inner);
    }

    public <T> $ $cols(T val, Predicate<T> filter,
                       Function<T, BindSql> inner) {
        return $trim(val, filter,
                null, null,
                e -> Arrays.asList(","),
                e -> Arrays.asList(","),
                inner);
    }

    public <T> $ $bracket(T val, Predicate<T> filter,
                          Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> new BindSql("("),
                e -> new BindSql(")"),
                null,
                null,
                inner);
    }

    public <T> $ $and(T val, Predicate<T> filter,
                      Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> new BindSql("and ("),
                e -> new BindSql(")"),
                e -> Arrays.asList("and", "or", "AND", "OR"),
                null, inner);
    }

    public <T> $ $or(T val, Predicate<T> filter,
                     Function<T, BindSql> inner) {
        return $trim(val, filter,
                e -> new BindSql("or ("),
                e -> new BindSql(")"),
                e -> Arrays.asList("and", "or", "AND", "OR"),
                null, inner);
    }

    public <T> $ $trim(T val, Predicate<T> filter,
                       Function<T, BindSql> prefix,
                       Function<T, BindSql> suffix,
                       Function<T, List<String>> trimPrefix,
                       Function<T, List<String>> trimSuffix,
                       Function<T, BindSql> inner
    ) {
        if (filter == null || filter.test(val)) {
            BindSql sql = inner.apply(val);
            if (sql == null) {
                return this;
            }
            if (trimPrefix != null) {
                String str = sql.getSql().trim();
                List<String> trims = trimPrefix.apply(val);
                if (trims != null) {
                    while (true) {
                        boolean replace = false;
                        str = str.trim();
                        for (String trim : trims) {
                            if (str.startsWith(trim)) {
                                str = str.substring(trim.length());
                                replace = true;
                                break;
                            }
                        }
                        if (!replace) {
                            break;
                        }
                    }

                }
                sql.setSql(str);
            }
            if (trimSuffix != null) {
                String str = sql.getSql().trim();
                List<String> trims = trimSuffix.apply(val);
                if (trims != null) {
                    while (true) {
                        boolean replace = false;
                        str = str.trim();
                        for (String trim : trims) {
                            if (str.endsWith(trim)) {
                                str = str.substring(0, str.length() - trim.length());
                                replace = false;
                                break;
                            }
                        }
                        if (!replace) {
                            break;
                        }
                    }
                }
                sql.setSql(str);
            }
            if ("".equals(sql.getSql().trim())) {
                return this;
            }
            if (prefix != null) {
                BindSql pre = prefix.apply(val);
                if (pre != null) {
                    this.list.add(pre);
                }
            }
            this.list.add(sql);
            if (suffix != null) {
                BindSql suf = suffix.apply(val);
                if (suf != null) {
                    this.list.add(suf);
                }
            }
        }
        return this;
    }


    public <T, E, C extends Iterable<E>> $ $foreach(T val, Predicate<T> filter,
                                                    Function<T, C> iterable,
                                                    Function<T, BindSql> separator,
                                                    BiFunction<E, Integer, BindSql> item) {
        if (filter == null || filter.test(val)) {

            int i = 0;
            boolean isFirst = true;
            C col = iterable.apply(val);
            List<BindSql> tmp = new ArrayList<>();
            for (E iter : col) {
                BindSql sql = item.apply(iter, i);
                if (sql == null) {
                    i++;
                    continue;
                }
                if (!isFirst) {
                    if (separator != null) {
                        BindSql sep = separator.apply(val);
                        tmp.add(sep);
                    }
                }
                tmp.add(sql);
                isFirst = false;
                i++;
            }
            if (!tmp.isEmpty()) {
                $ begin = $.$();
                for (BindSql iter : tmp) {
                    begin.$(iter);
                }
                BindSql sql = begin.$$();
                this.list.add(sql);
            }
        }
        return this;
    }

    public static final BindSql SELECT = new BindSql("select");
    public static final BindSql FROM = new BindSql("from");
    public static final BindSql JOIN = new BindSql("join");
    public static final BindSql ON = new BindSql("on");
    public static final BindSql LEFT = new BindSql("left");
    public static final BindSql RIGHT = new BindSql("right");
    public static final BindSql INNER = new BindSql("inner");
    public static final BindSql WHERE = new BindSql("where");
    public static final BindSql AND = new BindSql("and");
    public static final BindSql OR = new BindSql("or");
    public static final BindSql GROUP = new BindSql("group");
    public static final BindSql BY = new BindSql("by");
    public static final BindSql HAVING = new BindSql("having");
    public static final BindSql ORDER = new BindSql("order");
    public static final BindSql LIMIT = new BindSql("limit");
    public static final BindSql AS = new BindSql("as");
    public static final BindSql IS = new BindSql("is");
    public static final BindSql NULL = new BindSql("null");
    public static final BindSql NOT = new BindSql("not");
    public static final BindSql LIKE = new BindSql("like");
    public static final BindSql IN = new BindSql("in");
    public static final BindSql EXISTS = new BindSql("exists");

    public static final BindSql CREATE = new BindSql("create");
    public static final BindSql TABLE = new BindSql("table");
    public static final BindSql PRIMARY = new BindSql("primary");
    public static final BindSql KEY = new BindSql("key");
    public static final BindSql FOREIGN = new BindSql("foreign");
    public static final BindSql REFERENCES = new BindSql("references");
    public static final BindSql UNIQUE = new BindSql("unique");
    public static final BindSql INDEX = new BindSql("index");
    public static final BindSql VIEW = new BindSql("view");

    public static final BindSql UPDATE = new BindSql("update");
    public static final BindSql SET = new BindSql("set");
    public static final BindSql DELETE = new BindSql("delete");
    public static final BindSql INSERT = new BindSql("insert");
    public static final BindSql INTO = new BindSql("into");
    public static final BindSql VALUES = new BindSql("values");
    public static final BindSql UNION = new BindSql("union");
    public static final BindSql ALL = new BindSql("all");
    public static final BindSql DISTINCT = new BindSql("distinct");
    public static final BindSql TOP = new BindSql("top");
    public static final BindSql ASC = new BindSql("asc");
    public static final BindSql DESC = new BindSql("desc");

    public static final BindSql CASE = new BindSql("case");
    public static final BindSql WHEN = new BindSql("when");
    public static final BindSql THEN = new BindSql("then");
    public static final BindSql ELSE = new BindSql("else");
    public static final BindSql END = new BindSql("end");


    public static final BindSql EQ = new BindSql("=");
    public static final BindSql GT = new BindSql(">");
    public static final BindSql LT = new BindSql("<");
    public static final BindSql GTE = new BindSql(">=");
    public static final BindSql LTE = new BindSql("<=");
    public static final BindSql NEQ = new BindSql("!=");

    public static final BindSql IS_NULL = new BindSql("is null");
    public static final BindSql IS_NOT_NULL = new BindSql("is not null");

    public static final BindSql EQ_NULL = new BindSql("= null");
    public static final BindSql NEQ_NULL = new BindSql("!= null");

    public static final BindSql DOT = new BindSql(".");
    public static final BindSql COMMA = new BindSql(",");
    public static final BindSql LINE = new BindSql("\n");
    public static final BindSql SPACE = new BindSql(" ");

    public $ select() {
        this.list.add(SELECT);
        return this;
    }

    public $ from() {
        this.list.add(FROM);
        return this;
    }

    public $ join() {
        this.list.add(JOIN);
        return this;
    }

    public $ on() {
        this.list.add(ON);
        return this;
    }

    public $ left() {
        this.list.add(LEFT);
        return this;
    }

    public $ right() {
        this.list.add(RIGHT);
        return this;
    }

    public $ inner() {
        this.list.add(INNER);
        return this;
    }

    public $ where() {
        this.list.add(WHERE);
        return this;
    }

    public $ and() {
        this.list.add(AND);
        return this;
    }

    public $ or() {
        this.list.add(OR);
        return this;
    }

    public $ group() {
        this.list.add(GROUP);
        return this;
    }

    public $ by() {
        this.list.add(BY);
        return this;
    }

    public $ having() {
        this.list.add(HAVING);
        return this;
    }

    public $ order() {
        this.list.add(ORDER);
        return this;
    }

    public $ limit() {
        this.list.add(LIMIT);
        return this;
    }

    public $ as() {
        this.list.add(AS);
        return this;
    }

    public $ is() {
        this.list.add(IS);
        return this;
    }

    public $ $null() {
        this.list.add(NULL);
        return this;
    }

    public $ not() {
        this.list.add(NOT);
        return this;
    }

    public $ like() {
        this.list.add(LIKE);
        return this;
    }

    public $ in() {
        this.list.add(IN);
        return this;
    }

    public $ exists() {
        this.list.add(EXISTS);
        return this;
    }

    public $ create() {
        this.list.add(CREATE);
        return this;
    }

    public $ table() {
        this.list.add(TABLE);
        return this;
    }

    public $ primary() {
        this.list.add(PRIMARY);
        return this;
    }

    public $ key() {
        this.list.add(KEY);
        return this;
    }

    public $ foreign() {
        this.list.add(FOREIGN);
        return this;
    }

    public $ references() {
        this.list.add(REFERENCES);
        return this;
    }

    public $ unique() {
        this.list.add(UNIQUE);
        return this;
    }

    public $ index() {
        this.list.add(INDEX);
        return this;
    }

    public $ view() {
        this.list.add(VIEW);
        return this;
    }

    public $ update() {
        this.list.add(UPDATE);
        return this;
    }

    public $ set() {
        this.list.add(SET);
        return this;
    }

    public $ delete() {
        this.list.add(DELETE);
        return this;
    }

    public $ insert() {
        this.list.add(INSERT);
        return this;
    }

    public $ into() {
        this.list.add(INTO);
        return this;
    }

    public $ values() {
        this.list.add(VALUES);
        return this;
    }

    public $ union() {
        this.list.add(UNION);
        return this;
    }

    public $ all() {
        this.list.add(ALL);
        return this;
    }

    public $ distinct() {
        this.list.add(DISTINCT);
        return this;
    }

    public $ top() {
        this.list.add(TOP);
        return this;
    }

    public $ asc() {
        this.list.add(ASC);
        return this;
    }

    public $ desc() {
        this.list.add(DESC);
        return this;
    }

    public $ $case() {
        this.list.add(CASE);
        return this;
    }

    public $ when() {
        this.list.add(WHEN);
        return this;
    }

    public $ then() {
        this.list.add(THEN);
        return this;
    }

    public $ $else() {
        this.list.add(ELSE);
        return this;
    }

    public $ eq() {
        this.list.add(EQ);
        return this;
    }

    public $ gt() {
        this.list.add(GT);
        return this;
    }

    public $ lt() {
        this.list.add(LT);
        return this;
    }

    public $ gte() {
        this.list.add(GTE);
        return this;
    }

    public $ lte() {
        this.list.add(LTE);
        return this;
    }

    public $ neq() {
        this.list.add(NEQ);
        return this;
    }

    public $ isNull() {
        this.list.add(IS_NULL);
        return this;
    }

    public $ isNotNull() {
        this.list.add(IS_NOT_NULL);
        return this;
    }

    public $ eqNull() {
        this.list.add(EQ_NULL);
        return this;
    }

    public $ neqNull() {
        this.list.add(NEQ_NULL);
        return this;
    }

    public $ end() {
        this.list.add(END);
        return this;
    }

    public $ dot() {
        this.list.add(DOT);
        return this;
    }

    public $ comma() {
        this.list.add(COMMA);
        return this;
    }

    public $ line() {
        this.list.add(LINE);
        return this;
    }

    public $ space() {
        this.list.add(SPACE);
        return this;
    }


}
