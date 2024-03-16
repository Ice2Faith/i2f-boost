package i2f.database.sql.test;


import i2f.database.jdbc.data.BindSql;
import i2f.database.sql.$;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author Ice2Faith
 * @date 2024/3/15 22:32
 * @desc
 */
public class TestSql {
    public static void main(String[] args) {
//        testBasic();

        testLang();

        System.out.println("ok");
    }

    public static void testLang() {
        BindSql sql = $.$()
                .select().line()
                .$cols(null, null, e ->
                        $.$().
                                $("?", 1).as().$("year").comma().line()
                                .$("a.username").comma().line()
                                .$("a.age").as().$("user_age").comma().line()
                                .$("a.status").comma().line()
                                .$("b.item_text").as().$("status_desc").line()
                                .$$()
                ).line()
                .from().$("sys_user").$("a").line()
                .left().join().$bracket(null, null, e ->
                        $.$().
                                select().$("item_value,item_text").line()
                                .from().$("sys_dict").line()
                                .where().$("dict_type=?", "user_status").$$()
                ).$("b").on().$("a.status=b.item_value").line()
                .$where(1, null,
                        e -> $.$()
                                .$("admin", v -> true, v -> $.$().and().$eq("a.username", v, null).line().$$())
                                .$(1, v -> false, v -> $.$().and().$eq("a.status", v, null).line().$$())
                                .$(Arrays.asList(6, 7, 8), v -> true, v -> $.$().and().$in("a.id", v, null).line().$$())
                                .$$()
                ).$$();


        System.out.println(sql.getSql());
        System.out.println("=====>>> " + sql.getArgs());
    }

    public static void testBasic() {
        BindSql end = $.$()
                .select()
                .$(new BindSql("select ? as year", Arrays.asList(2024)))
                .$(new BindSql(","))
                .$(1, e -> e > 0, e -> $.$()
                        .$(new BindSql("? as level", Arrays.asList(e)))
                        .$$())
                .$trim(1, e -> true, e -> new BindSql("where"), null,
                        e -> Arrays.asList("and"), null,
                        e -> {
                            return $.$().$("and 1=1").$$();
                        })
                .$trim(1, e -> true,
                        e -> new BindSql("and ("),
                        e -> new BindSql(")"),
                        e -> Arrays.asList("or"),
                        e -> Arrays.asList("or"),
                        v -> $.$().$foreach(
                                Arrays.asList(4, 2, 3, 1, 5, 0),
                                e -> true,
                                e -> new TreeSet<>(e),
                                e -> new BindSql("\n"),
                                (e, i) -> $.$().$(new BindSql("or a.id = ?", Arrays.asList(e))).$$()
                        ).$$())
                .$$();

        System.out.println(end.getSql());
        System.out.println("=====>>> " + end.getArgs());
    }
}
