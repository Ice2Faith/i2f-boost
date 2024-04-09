package i2f.sql.test;

import i2f.sql.$;
import i2f.sql.BindSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:46
 * @desc
 */
public class Test {

    public static void main(String[] args) {

        testSelect();

        testInsert();

        testBatchInsert();

        testBatchSelectInsert();

        testUpdate();

        testDelete();

        testCreateTable();

        testCreateIndex();

        testLambda();

        testBeanQuery();

        testBeanUpdate();

        testBeanDelete();

        System.out.println("ok");
    }


    public static String location(StackTraceElement[] trace) {
        return trace[1].getClassName() + "." + trace[1].getMethodName() + ":" + trace[1].getLineNumber();
    }

    public static void testBeanDelete() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        BindSql bql = $.$_()
                .$beanDelete(user)
                .$$();
        System.out.println(bql);

        bql = $.$_()
                .$beanDelete(user, $.$ls(
                        $.$lm(SysUser::getRoleId)
                ))
                .$$();
        System.out.println(bql);
    }

    public static void testBeanUpdate() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        SysUser cond = new SysUser();
        cond.setId(101L);

        BindSql bql = $.$_()
                .$beanUpdate(user, cond)
                .$$();
        System.out.println(bql);

        bql = $.$_()
                .$beanUpdate(user, cond,
                        $.$ls($.$lm(SysUser::getAge)))
                .$$();
        System.out.println(bql);
    }

    public static void testBeanQuery() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        SysUser user = new SysUser();
        user.setAge(24);
        user.setDelFlag(0);
        user.setStatus(1);

        BindSql bql = $.$_()
                .$beanQuery(user)
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$beanQuery(user, "su")
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$beanQuery(user, $.$ls(
                        $.$lm(SysUser::getId),
                        $.$lm(SysUser::getUserName),
                        $.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$beanQuery(user, null, null, $.$ls(
                        $.$lm(SysUser::getId),
                        $.$lm(SysUser::getUserName),
                        $.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$beanQuery(user, "su", $.$ls(
                        $.$lm(SysUser::getId),
                        $.$lm(SysUser::getUserName),
                        $.$lm(SysUser::getNickName)
                ))
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$beanQuery(user, "su",
                        null, null,
                        $.$ls($.$lm(SysUser::getUpdateTime)),
                        $.$ls($.$lm(SysUser::getCreateTime)),
                        $.$ls($.$lm(SysUser::getUserName))
                )
                .$$();

        System.out.println(bql);
    }

    public static void testLambda() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .$select(() -> $.$_().$sepComma()
                        .$col(SysUser::getUserName)
                        .$col(SysUser::getAge)
                )
                .$from(SysUser.class)
                .$where(() -> $.$_()
                        .$eq(SysUser::getUserName, "admin")
                        .$eq(SysUser::getStatus, 1)
                        .$gte(SysUser::getAge, 18)
                )
                .$$();
        System.out.println(bql);
    }

    public static void testCreateIndex() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .create().unique().index().$("idx_sys_user_status_del")
                .on().$("sys_user").$bracket(() -> $.$_().$sepComma()
                        .$col("status")
                        .$col("del_flag")
                )
                .$$();
        System.out.println(bql);

        bql = $.$_()
                .create().unique().index().$("idx_sys_user_status_del")
                .on().$(SysUser.class).$bracket(() -> $.$_().$sepComma()
                        .$col(SysUser::getStatus)
                        .$col(SysUser::getDelFlag)
                )
                .$$();
        System.out.println(bql);
    }

    public static void testCreateTable() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .create().$table("sys_user")
                .$bracket(() -> $.$_().$sepComma()
                        .$each(() -> $.$_().$("id").bigint().primary().key().$comment("ID"),
                                () -> $.$_().$("role_id").bigint().$foreignKeyReferences("sys_role", "id").$comment("角色ID"),
                                () -> $.$_().$("username").varchar(300).not().$null().$comment("用户名"),
                                () -> $.$_().$("password").varchar(300).not().$null().$comment("密码"),
                                () -> $.$_().$("status").tinyint().$default().$("1").$comment("状态：'0' 禁用，1 正常"),
                                () -> $.$_().$("create_time").datetime().$default().$("now()").$comment("创建时间"),
                                () -> $.$_().$("update_time").datetime().$comment("更新时间")
                        )

                ).$comment("用户表")
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .create().$table(SysUser.class)
                .$bracket(() -> $.$_().$sepComma()
                        .$each(() -> $.$_().$(SysUser::getId).bigint().primary().key().$comment("ID"),
                                () -> $.$_().$(SysUser::getRoleId).bigint().$foreignKeyReferences(SysRole.class, SysRole::getId).$comment("角色ID"),
                                () -> $.$_().$(SysUser::getUserName).varchar(300).not().$null().$comment("用户名"),
                                () -> $.$_().$(SysUser::getPassword).varchar(300).not().$null().$comment("密码"),
                                () -> $.$_().$(SysUser::getStatus).tinyint().$default().$("1").$comment("状态：'0' 禁用，1 正常"),
                                () -> $.$_().$(SysUser::getCreateTime).datetime().$default().$("now()").$comment("创建时间"),
                                () -> $.$_().$(SysUser::getUpdateTime).datetime().$comment("更新时间")
                        )

                ).$comment("用户表")
                .$$();

        System.out.println(bql);
    }

    public static void testDelete() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .delete().$from("sys_user")
                .$where(() -> $.$_()
                        .$eq("del_flag", 1)
                        .$and(() -> $.$_()
                                .$or()
                                .$lt("create_time", 10)
                                .$lt("update_time", 10)
                        )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .delete().$from(SysUser.class)
                .$where(() -> $.$_()
                        .$eq(SysUser::getDelFlag, 1)
                        .$and(() -> $.$_()
                                .$or()
                                .$lt(SysUser::getCreateTime, 10)
                                .$lt(SysUser::setUpdateTime, 10)
                        )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testUpdate() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .$update("sys_user")
                .$set(() -> $.$_().$sepComma()
                        .$link()
                        .$eq("username", "zhang")
                        .$eq("nickname", "zhang")
                        .$eq("del_flag", 0)
                        .$eqNull("role_id")
                )
                .$where(() -> $.$_()
                        .$eq("id", 1)
                        .$eq("status", 1)
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$update(SysUser.class)
                .$set(() -> $.$_().$sepComma()
                        .$link()
                        .$eq(SysUser::getUserName, "zhang")
                        .$eq(SysUser::getNickName, "zhang")
                        .$eq(SysUser::getDelFlag, 0)
                        .$eqNull(SysUser::getRoleId)
                )
                .$where(() -> $.$_()
                        .$eq(SysUser::getId, 1)
                        .$eq(SysUser::getStatus, 1)
                )
                .$$();

        System.out.println(bql);
    }

    public static void testBatchSelectInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        List<String> names = Arrays.asList("zhang", "li", "wang");
        BindSql bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList("union all", "union"),
                        Arrays.asList("union all", "union"),
                        null, null,
                        (list) -> $.$_()
                                .$for(list, " union all ", null,
                                        (i, v) -> $.$_()
                                                .$select(() -> $.$_().$sepComma()
                                                        .$("? as username", v)
                                                        .$("? as nickname", v)
                                                        .$("? as age", v + i)
                                                        .$("1 as status")
                                                        .$("now() as create_time")
                                                ).$from("dual")
                                )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into(SysUser.class,
                        () -> $.$_().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::setCreateTime)
                )
                .$trim(names, Arrays.asList("union all", "union"),
                        Arrays.asList("union all", "union"),
                        null, null,
                        (list) -> $.$_()
                                .$for(list, " union all ", null,
                                        (i, v) -> $.$_()
                                                .$select(() -> $.$_().$sepComma()
                                                        .$colAs("?", SysUser::getUserName, v)
                                                        .$colAs("?", SysUser::getNickName, v)
                                                        .$colAs("?", SysUser::getAge, v + i)
                                                        .$colAs("1", SysUser::getStatus)
                                                        .$colAs("now()", SysUser::getCreateTime)
                                                ).$fromDual()
                                )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$forUnionAll(names, null, (i, v) -> $.$_()
                        .$select(() -> $.$_().$sepComma()
                                .$("? as username", v)
                                .$("? as nickname", v)
                                .$("? as age", v + i)
                                .$("1 as status")
                                .$("now() as create_time")
                        ).$from("dual")
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into(SysUser.class,
                        () -> $.$_().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::setCreateTime)
                )
                .$forUnionAll(names, null, (i, v) -> $.$_()
                        .$select(() -> $.$_().$sepComma()
                                .$colAs("?", SysUser::getUserName, v)
                                .$colAs("?", SysUser::getNickName, v)
                                .$colAs("?", SysUser::getAge, v + i)
                                .$colAs("1", SysUser::getStatus)
                                .$colAs("now()", SysUser::getCreateTime)
                        ).$fromDual()
                )
                .$$();

        System.out.println(bql);
    }

    public static void testBatchInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        List<String> names = Arrays.asList("zhang", "li", "wang");
        BindSql bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> $.$_().
                                $for(list, ",", null, (i, v) -> $.$_()
                                        .$bracket(() -> $.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> $.$_().
                                $for(list, ",", null, (i, v) -> $.$_()
                                        .$bracket(() -> $.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into(SysUser.class,
                        () -> $.$_().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::getCreateTime)
                )
                .$trim(names, Arrays.asList(","),
                        Arrays.asList(","),
                        "values", null,
                        (list) -> $.$_().
                                $for(list, ",", null, (i, v) -> $.$_()
                                        .$bracket(() -> $.$_().$sepComma()
                                                .$("?", v)
                                                .$("?", v)
                                                .$("?", 24 + i)
                                                .$("?", 1)
                                                .$("now()")
                                        )
                                )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testInsert() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        BindSql bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$values(() -> $.$_().$sepComma()
                        .$("?", "zhang")
                        .$("?", "zhang")
                        .$("?", 24)
                        .$("?", 1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into("sys_user",
                        () -> $.$_().$sepComma()
                                .$col("username")
                                .$col("nickname")
                                .$col("age")
                                .$col("status")
                                .$col("create_time")
                )
                .$values(() -> $.$_().$sepComma()
                        .$var("zhang")
                        .$var("zhang")
                        .$var(24)
                        .$var(1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .insert().$into(SysUser.class,
                        () -> $.$_().$sepComma()
                                .$col(SysUser::getUserName)
                                .$col(SysUser::getNickName)
                                .$col(SysUser::getAge)
                                .$col(SysUser::getStatus)
                                .$col(SysUser::getCreateTime)
                )
                .$values(() -> $.$_().$sepComma()
                        .$var("zhang")
                        .$var("zhang")
                        .$var(24)
                        .$var(1)
                        .$("now()")
                )
                .$$();

        System.out.println(bql);
    }

    public static void testSelect() {
        System.out.println("----------------------------------------------------");
        System.out.println(location(Thread.currentThread().getStackTrace()));
        Map<String, Object> query = new HashMap<>();
        query.put("username", "zhang");
        query.put("age", 24);

        BindSql bql = $.$_()
                .$select(() -> $.$_().$sepComma()
                        .$alias("su")
                        .$col("*")
                        .$alias("d1")
                        .$col("dict_text", "status_desc")
                        .$col(() -> $.$_()
                                        .$select(() -> $.$_().$col("name"))
                                        .$from("sys_role", "r")
                                        .$where(() -> $.$_()
                                                .$cond("r.id=su.role_id")
                                        )
                                , "role_name")
                        .$col(() -> $.$_()
                                        .$case().when().$("su.del_flag=1").then().$var("删除")
                                        .when().$("su.del_flag=0").then().$var("正常")
                                        .end()
                                , "del_flag_desc")
                )
                .$from("sys_user", "su")
                .left().$join(() -> $.$_()
                                .$select(() -> $.$_().$sepComma()
                                        .$col("dict_value")
                                        .$col("dict_text")
                                )
                                .$from("sys_dict")
                                .$where(() -> $.$_()
                                        .$eq("dict_type", "user_status")
                                )
                        , "d1").$on(() -> $.$_()
                        .$cond("su.status=d1.dict_value")
                )
                .$where(query, post -> post != null && post.size() > 0,
                        post -> $.$_()
                                .$and((String) post.get("username"), v -> v != null && !"".equals(v), v -> $.$_("su.username=?", v))
                                .$and((Integer) post.get("age"), v -> v != null && v > 0, v -> $.$_("su.age>=?", v))
                                .$alias("su").$and()
                                .$eq("age", (Integer) post.get("age"))
                                .$like("username", "wang")
                                .$neq("age", 12)
                                .$ne("age", 14)
                                .$instr("nickname", "admin")
                                .$and(1, v -> $.$_().$or().$alias("su")
                                        .$lt("age", 35)
                                        .$gte("age", 18)
                                        .$or(() -> $.$_()
                                                .$alias("su")
                                                .$isNotNull("status")
                                                .$isNull("del_flag"))
                                )

                                .$in("grade", Arrays.asList(6, 4, 8, 9, 10))
                                .$exists(() -> $.$_()
                                        .$select(() -> $.$_().$sepComma()
                                                .$alias("p")
                                                .$col("dict_value")
                                                .$col("dict_text")

                                        ).$("from sys_dict p")
                                        .$where(() -> $.$_()
                                                .$and()
                                                .$eq("dict_type", "user_status")
                                                .$cond(() -> $.$_().$alias("p").$col("dict_type").$("= su.status"))
                                        )

                                )
                )
                .$$();

        System.out.println(bql);

        bql = $.$_()
                .$select(() -> $.$_().$sepComma()
                        .$alias("su")
                        .$col("*")
                        .$alias("d1")
                        .$col(SysDict::getDictText, "status_desc")
                        .$col(() -> $.$_()
                                        .$select(() -> $.$_().$col(SysRole::getName))
                                        .$from(SysRole.class, "r")
                                        .$where(() -> $.$_()
                                                .$cond("r.id=su.role_id")
                                        )
                                , "role_name")
                        .$col(() -> $.$_()
                                        .$case().when().$("su.del_flag=1").then().$var("删除")
                                        .when().$("su.del_flag=0").then().$var("正常")
                                        .end()
                                , "del_flag_desc")
                )
                .$from(SysUser.class, "su")
                .left().$join(() -> $.$_()
                                .$select(() -> $.$_().$sepComma()
                                        .$col(SysDict::getDictValue)
                                        .$col(SysDict::getDictText)
                                )
                                .$from(SysDict.class)
                                .$where(() -> $.$_()
                                        .$eq(SysDict::getDictType, "user_status")
                                )
                        , "d1").$on(() -> $.$_()
                        .$cond("su.status=d1.dict_value")
                )
                .$where(query, post -> post != null && post.size() > 0,
                        post -> $.$_()
                                .$and((String) post.get("username"), v -> v != null && !"".equals(v), v -> $.$_("su.username=?", v))
                                .$and((Integer) post.get("age"), v -> v != null && v > 0, v -> $.$_("su.age>=?", v))
                                .$alias("su").$and()
                                .$eq(SysUser::getAge, (Integer) post.get("age"))
                                .$like(SysUser::getUserName, "wang")
                                .$neq(SysUser::getAge, 12)
                                .$ne(SysUser::getAge, 14)
                                .$instr(SysUser::getNickName, "admin")
                                .$and(1, v -> $.$_().$or().$alias("su")
                                        .$lt(SysUser::getAge, 35)
                                        .$gte(SysUser::getAge, 18)
                                        .$or(() -> $.$_()
                                                .$alias("su")
                                                .$isNotNull(SysUser::getStatus)
                                                .$isNull(SysUser::getDelFlag))
                                )

                                .$in(SysUser::getGrade, Arrays.asList(6, 4, 8, 9, 10))
                                .$exists(() -> $.$_()
                                        .$select(() -> $.$_().$sepComma()
                                                .$alias("p")
                                                .$col(SysDict::getDictValue)
                                                .$col(SysDict::getDictText)

                                        ).$("from sys_dict p")
                                        .$where(() -> $.$_()
                                                .$and()
                                                .$eq(SysDict::getDictType, "user_status")
                                                .$cond(() -> $.$_().$alias("p").$col(SysDict::getDictType).$("= su.status"))
                                        )

                                )
                )
                .$$();

        System.out.println(bql);
    }

    public static void testExecute(Connection conn, BindSql bql) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(bql.sql);
        List<Object> args = bql.args;
        int idx = 1;
        for (Object arg : args) {
            stat.setObject(idx, arg);
            idx++;
        }
//        int num = stat.executeUpdate();
        ResultSet rs = stat.executeQuery();

        rs.close();
        stat.close();
    }
}
