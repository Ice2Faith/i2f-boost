package i2f.core.database.jdbc.sql.consts;

/**
 * @author ltb
 * @date 2022/5/23 13:59
 * @desc
 */
public interface Sql {
    String SELECT="select";
    String SELECT_1="select 1";

    String AS="as";
    String FROM="from";
    String WHERE="where";
    String DISTINCT="distinct";

    String IF="if";
    String IF_EXISTS="if exists";

    String DUAL="dual";
    String FROM_DUAL="from dual";

    String JOIN="join";
    String LEFT="left";
    String RIGHT="right";
    String INNER="inner";
    String OUTER="outer";
    String ON="on";

    String LEFT_JOIN="left join";
    String RIGHT_JOIN="right join";
    String INNER_JOIN="inner join";
    String OUTER_JOIN="outer join";

    String AND="and";
    String OR="or";
    String IN="in";
    String LIKE="like";
    String GROUP="group";
    String BY="by";
    String GROUP_BY="group by";
    String HAVING="having";
    String ORDER="order";
    String ORDER_BY="order by";
    String ASC="asc";
    String DESC="desc";

    String EXISTS="exists";
    String NOT="not";
    String NOT_EXIST="not exists";

    String INSERT="insert";
    String INTO="into";
    String INSERT_INTO="insert into";
    String VALUES="values";

    String UPDATE="update";
    String SET="set";

    String DELETE="delete";
    String DELETE_FORM="delete from";

    String CREATE="create";
    String TABLE="table";
    String CREATE_TABLE="create table";

    String VIEW="view";
    String CREATE_VIEW="create view";
    String PRIMARY="primary";
    String KEY="key";
    String PRIMARY_KEY="primary key";
    String UNIQUE="unique";
    String NULL="null";
    String NOT_NULL="not null";
    String DEFAULT="default";

    String COMMENT="comment";
    String INDEX="index";
    String FOREIGN="foreign";
    String FOREIGN_KEY="foreign key";
    String REFERENCES ="references";
    String USE="use";
    String DATABASE="database";
    String DROP="drop";
    String DROP_TABLE="drop table";
    String DROP_VIEW="drop view";
    String DROP_DATABASE="drop database";

    String CASCADE="cascade";
    String ALTER="alter";
    String ALTER_TABLE="alter table";
    String COLUMN="column";
    String ADD="add";
    String CONSTRAINT="constraint";

    String CALL="call";

    String EQ="=";
    String NEQ="!=";
    String GT=">";
    String LT="<";
    String GTE=">=";
    String LTE="<=";

    String AUTO_INCREMENT="auto_increment";

    String COUNT="count";
    String COUNT_ALL="count(*)";
    String COUNT_1="count(1)";

}
