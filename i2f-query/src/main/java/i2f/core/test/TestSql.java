package i2f.core.test;


import i2f.core.builder.$sql;
import i2f.core.builder.$str;
import i2f.core.builder.BindSql;
import i2f.core.str.$;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:06
 * @desc
 */
public class TestSql {
    public static void main(String[] args) {
        String st = $.str()
                .str(1)
                .str(2)
                .str(3)
                .$$();
        System.out.println(st);

        String sq = i2f.core.sql.$.sql()
                .str(1)
                .str(2)
                .sql(3)
                .str(4)
                .sql(5)
                .sql(6)
                .str(7)
                .$$();
        System.out.println(sq);

        String reg = i2f.core.regex.$.regex()
                .sql(1)
                .sql(2)
                .regex(3)
                .regex(4)
                .str(5)
                .str(6)
                .sql(7)
                .regex(8)
                .str(9)
                .regex(10)
                .sql(11)
                .$$();
        System.out.println(reg);

        String pat = i2f.core.patten.$.patten()
                .patten(0)
                .regex(1)
                .sql(2)
                .regex(3)
                .str(4)
                .regex(5)
                .patten(9)
                .regex(6)
                .sql(7)
                .str(8)
                .str(8)
                .patten(11)
                .patten(12)
                .sql(9)
                .sql(10)
                .regex(11)
                .$$();

        System.out.println(pat);

        String bnum = i2f.core.builder.$.$_(Number.class,
                h -> new StringBuilder(),
                (c, e) -> c.append(e),
                (h, c) -> c.toString())
                .$sep(() -> -1)
                .$if(1, e -> true, e -> e)
                .$(2)
                .$(3.5)
                .$$();
        System.out.println(bnum);

        String bapp = i2f.core.builder.$.$_(Object.class,
                h -> new StringBuilder(),
                StringBuilder::append,
                (h, c) -> c.toString())
                .$sep(() -> ",")
                .$if(1, e -> false, e -> "num:1")
                .$(2)
                .$for(Arrays.asList(9, 8, 7, 6),
                        e -> true,
                        Integer.class,
                        e -> e,
                        e -> "[",
                        e -> "-",
                        e -> "]",
                        (e, i) -> e)
                .$$();
        System.out.println(bapp);

        String bst = $str.$_()
                .$sep("1")
                .$(2)
                .$format("%02x", 1)
                .$trim(1, e -> true,
                        e -> "^",
                        e -> "$",
                        e -> Arrays.asList("["),
                        e -> Arrays.asList("]"),
                        v -> $str.$_().$for(Arrays.asList(9, 8, 7, 6),
                                e -> true,
                                Integer.class,
                                e -> e,
                                e -> "[",
                                e -> "-",
                                e -> "]",
                                (e, i) -> e).$$()
                )
                .$$();
        System.out.println(bst);

        BindSql bsql = $sql.$_()
                .$("select")
                .$("*")
                .$("from sys_user a")
                .$where(null, null,
                        e -> $sql.$_()
                                .$and(1, v -> true, v -> $sql.$_()
                                        .$("a.status=?", v)
                                        .$$()
                                )
                                .$and("admin", v -> true, v -> $sql.$_()
                                        .$("a.username=?", v)
                                        .$$()
                                )
                                .$and(Arrays.asList(9, 8, 7), v -> true, v -> $sql.$_()
                                        .$in(v, c -> true, t -> $sql.$_()
                                                .$("?", t)
                                                .$$()
                                        )
                                        .$$()
                                )
                                .$$()
                )
                .$$();
        System.out.println(bsql);
        System.out.println(bsql.toSql());

    }
}
