package test.sql.wrapper;

import i2f.core.collection.CollectionUtil;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.wrapper.DeleteWrapper;
import i2f.core.jdbc.sql.wrapper.InsertWrapper;
import i2f.core.jdbc.sql.wrapper.QueryWrapper;
import i2f.core.jdbc.sql.wrapper.UpdateWrapper;
import i2f.core.jdbc.sql.wrapper.core.BindSql;
import i2f.core.jdbc.sql.wrapper.ddl.CreateTableWrapper;
import i2f.core.jdbc.sql.wrapper.ddl.DropTableWrapper;
import test.lambda.SysUser;

/**
 * @author ltb
 * @date 2022/5/23 15:23
 * @desc
 */
public class TestConditionWrapper {
    public static void main(String[] args){
        QueryWrapper queryWrapper = QueryWrapper.builder()
                .select()
                .distinct()
                .prefix("a.")
                .col("user_name","userName").col(SysUser::getAvatar).col("id")
                .next()

                .table().table("tb_user","a")
                .next()

                .joins()
                .inner().join("tb_role r","a.role_id=r.role_id")
                .next()

                .where()
                .and()
                .prefix("a.").eq(SysUser::getUserName,"Zhang")
                .like(SysUser::getRealName,"Li")
                .likes(SysUser::getAvatar, CollectionUtil.arrayList("1","2","3").iterator())
                .multiLike(CollectionUtil.arrayList("userName","realName").iterator(),"wang")
                .condFree("r.role_name like '%'||?||'%'","admin")
                .next()

                .group()
                .prefix("a.").col(SysUser::getState)
                .next()

                .having().and().gt("count(*)",3)
                .next()

                .order()
                .asc().prefix("a.").col(SysUser::getUserName)
                .next()

                .page()
                .page(0L,20L)
                .next()
                .done();

        BindSql queryBindSql=queryWrapper.prepare();
        System.out.println(queryBindSql.show());

        UpdateWrapper updateWrapper=UpdateWrapper.builder()
                .table().table("tb_user")
                .next()
                .sets()
                .set(SysUser::getRealName,"Wang")
                .set(SysUser::getState,"0")
                .next()
                .where()
                .and()
                .eq(SysUser::getId,1)
                .next()
                .done();

        BindSql updateBindSql=updateWrapper.prepare();
        System.out.println(updateBindSql.show());

        DeleteWrapper deleteWrapper=DeleteWrapper.builder()
                .table().table("tb_user")
                .next()
                .where()
                .and()
                .eq(SysUser::getRealName,"Zhang")
                .next()
                .done();

        BindSql deleteBindSql=deleteWrapper.prepare();
        System.out.println(deleteBindSql.show());

        InsertWrapper insertWrapper=InsertWrapper.builder()
                .table().table("tb_user")
                .next()
                .values()
                .set(SysUser::getUserName,"Zhao")
                .set(SysUser::getRealName,"找死")
                .next()
                .done();
        BindSql insertBindSql=insertWrapper.prepare();
        System.out.println(insertBindSql.show());

        CreateTableWrapper createTableWrapper=CreateTableWrapper.builder()
                .table().schema("test").table("tb_user")
                .next()
                .columns()
                .col("id","BIGINT", Sql.PRIMARY_KEY,Sql.AUTO_INCREMENT)
                .col(SysUser::getUserName,"VARCHAR(40)",Sql.UNIQUE)
                .col(SysUser::getState,"TINYINT",Sql.DEFAULT,"(","0",")")
                .next()
                .done();

        BindSql createTableBindSql = createTableWrapper.prepare();
        System.out.println(createTableBindSql.sql);

        DropTableWrapper dropTableWrapper=DropTableWrapper.builder()
                .table().schema("test").table("tb_user")
                .next()
                .done();

        BindSql dropTableBindSql=dropTableWrapper.prepare();
        System.out.println(dropTableBindSql.sql);

    }
}
