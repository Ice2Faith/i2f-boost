package i2f.database.sql.test;

import i2f.database.jdbc.data.BindSql;
import i2f.database.sql.$;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/3/18 9:10
 * @desc
 */
public class TestSql {
    public static void main(String[] args) {
//        testSelect();

//        testUpdate();

//        testInsert();

        testWithQuery();
    }


    public static void testWithQuery() {
        BindSql sql = $.$_()
                .with().$("dim").$ln()
                .as().$bracket$(s -> s.$select$(s1 -> s1.$("item_value")
                                .$("item_text")
                        )
                                .$from("sys_dict")
                                .$where$(s2 -> s2.$eq("dict_type", "user_status"))
                ).$cm().$ln()
                .$("ods").$ln()
                .as().$bracket$(s -> s.$select$(s1 -> s1.$("status")
                        )
                                .$from("sys_user")
                ).$ln()
                .$select$(s -> s.$col("a.", "status")
                        .$col("b", "item_text", "status_desc")
                        .$col(null, "count(*)", "cnt")
                ).$ln()
                .$from("ods", "a").$ln()
                .left().$join("dim", "b").on().$("a.status=b.item_value").$ln()
                .group().by().$arr$(s -> s.$("a.status")
                        .$("b.item_text")).$ln()
                .order().by().$("cnt").desc().$ln()
                .$$();

        printBindSql(sql);

        /**
         *  with dim
         *  as ( select item_value,item_text from sys_dict where dict_type = ? ),
         *  ods
         *  as ( select status from sys_user )
         *  select a.status,b.item_text as status_desc,count(*) as cnt
         *  from ods a
         *  left join dim b on a.status=b.item_value
         *  group by a.status,b.item_text
         *  order by cnt desc
         */
    }

    public static void testInsert() {
        BindSql sql = $.$_()
                .insert().into().$("sys_user").$ln()
                .$vals$(e -> e
                        .$("username")
                        .$("password")
                        .$("status")
                )
                .$ln()
                .$values$(e -> e
                        .$val("zhangsan")
                        .$val("123456")
                        .$val(1)
                )
                .$$();

        printBindSql(sql);

        /**
         *  insert into sys_user
         *  ( username,password,status )
         *  values
         *  ( ?,?,? )
         */
    }

    public static void testUpdate() {
        BindSql sql = $.$_()
                .update().$("sys_user").$ln()
                .$set$(e -> e
                        .$eq("nick_name", "root")
                        .$eq("age", 12)
                        .$eq(null, "role_id", null, v -> false)
                        .$eq(null, "update_by", null, v -> true)
                ).$ln()
                .$where$(e -> e
                        .$and$(s -> s.$eq("user_id", 1))
                        .$and$(0, v -> v > 0, (v, s) -> s.$eq("status", v))
                ).$$();

        printBindSql(sql);

        /**
         * update sys_user
         *  set nick_name = ?,age = ?,update_by = ?
         *  where ( user_id = ? )
         */
    }

    public static void testSelect() {
        BindSql sql = $.$_()
                .$select$(e -> e
                        .$col("a", "user_name", "username")
                        .$col("a", "password")
                        .$col("a", "status")
                        .$col("b", "item_value", "statusDesc")
                ).$ln()
                .$from("sys_user", "a").$ln()
                .left().join().$bracket$(e ->
                        e.$select(v -> v
                                .$("item_text")
                                .$("item_value")
                                .$$()
                        ).$ln()
                                .$from("sys_dict").$ln()
                                .where().$eq("dict_type", "user_status").$ln()
                ).$ln().$("b").on().$("a.status=b.item_value").$ln()
                .$where$(e -> e
                        .$sepLine()
                        .$and$(1, v -> true, (v, s) -> s.$eq("a", "status", v))
                        .$and$(Arrays.asList(2, 3, 4, 5), v -> false, (v, s) -> s.$in("a", "level", v))
                        .$and$("zhang", v -> true, (v, s) -> s.$like("a", "nick_name", v))
                        .$if$("li", v -> true, (v, s) -> s.and().$like("a", "first_name", v))
                        .$and$(1, null, (v, s) -> s.$eq("b", "status", 1))
                )
                .$$();

        printBindSql(sql);

        /**
         *   select a.user_name as username a.password a.status b.item_value as statusDesc
         *  from sys_user a
         *  left join ( select item_text,item_value
         *  from sys_dict
         *  where dict_type = ? )
         *  b on a.status=b.item_value
         *  where (
         * a.status = ?
         * )
         * and (
         * a.nick_name like ?
         * )
         *  and a.first_name like ?
         * and (
         * b.status = ?
         * )
         */
    }

    public static void printBindSql(BindSql sql) {
        System.out.println(sql.getSql());
        System.out.println("====> " + sql.getArgs());
    }
}
