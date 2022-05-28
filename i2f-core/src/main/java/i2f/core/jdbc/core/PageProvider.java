package i2f.core.jdbc.core;


import i2f.core.annotations.remark.Author;
import i2f.core.jdbc.data.DBResultData;
import i2f.core.jdbc.data.PageContextData;
import i2f.core.jdbc.data.PageMeta;
import i2f.core.jdbc.type.DbType;
import i2f.core.str.Appender;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
public class PageProvider {
    public static PageContextData queryPage(PageMeta page, Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PageContextData ctx=new PageContextData(page,prepareSql,params);
        DbType type= DbType.typeOfConnection(conn);
        switch (type){
            case MYSQL:
                prepareMySqlPage(ctx);
                break;
            case ORACLE:
                prepareOraclePage(ctx);
                break;
            case MARIADB:
                prepareMariaDBPage(ctx);
                break;
            case GBASE:
                prepareGbasePage(ctx);
                break;
            case OSCAR:
                prepareOscarPage(ctx);
                break;
            case XU_GU:
                prepareXuGuPage(ctx);
                break;
            case CLICK_HOUSE:
                prepareClickHousePage(ctx);
                break;
            case OCEAN_BASE:
                prepareOceanBasePage(ctx);
                break;
            case POSTGRE_SQL:
                preparePostgreSqlPage(ctx);
                break;
            case H2:
                prepareH2Page(ctx);
                break;
            case SQLITE:
                prepareSqlLitePage(ctx);
                break;
            case HSQL:
                prepareHSqlPage(ctx);
                break;
            case KINGBASE_ES:
                prepareKingBaseEsPage(ctx);
                break;
            case PHOENIX:
                preparePhoenixPage(ctx);
                break;
            case DM:
                prepareDmPage(ctx);
                break;
            case GAUSS:
                prepareGaussPage(ctx);
                break;
            case ORACLE_12C:
                prepareOracle12cPage(ctx);
                break;
            case DB2:
                prepareDb2Page(ctx);
                break;
            case SQL_SERVER:
                prepareSQLServerPage(ctx);
                break;
            default:
                throw new SQLException("unsupport auto page route db type");
        }
        prepareCountSql(ctx);
        if(ctx.countSql!=null){
            DBResultData rsCount= JdbcProvider.queryRaw(conn,ctx.countSql,ctx.countParams);
            Long count=rsCount.getData(0,0);
            ctx.count=count;
        }
        if(ctx.count!=null){
            if(ctx.count>0L){
                DBResultData rsData= JdbcProvider.queryRaw(conn,ctx.pageSql,ctx.pageParams);
                ctx.data=rsData;
            }
        }
        return ctx;
    }

    private static PageContextData prepareCountSql(PageContextData ctx){
        String sql= Appender.builder()
                .addLine(" select count(*) cnt ")
                .addLine(" from ( ")
                .addLine(ctx.prepareSql)
                .addLine(" ) tmp ")
                .get();
        ctx.countSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.countParams.add(item);
            }
        }
        return ctx;
    }

    private static PageContextData prepareMySqlPage(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(ctx.prepareSql)
                .addLine(" limit ?,?")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.index);
            ctx.pageParams.add(ctx.page.size);
        }
        return ctx;
    }

    private static PageContextData prepareOraclePage(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(" SELECT * ")
                .addLine(" FROM (SELECT TMP.*, ROWNUM ROW_ID ")
                .addLine(" FROM ( ")
                .addLine(ctx.prepareSql)
                .addLine(" ) TMP ")
                .addLine(" WHERE ROWNUM <= ?) ")
                .addLine(" WHERE ROW_ID > ? ")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.offsetEnd);
            ctx.pageParams.add(ctx.page.offset);
        }
        return ctx;
    }

    private static PageContextData prepareMariaDBPage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData prepareGbasePage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData prepareOscarPage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData prepareXuGuPage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData prepareClickHousePage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData prepareOceanBasePage(PageContextData ctx) {
        return prepareMySqlPage(ctx);
    }
    private static PageContextData preparePostgreSqlPage(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(ctx.prepareSql)
                .addLine(" limit ? ")
                .addLine(" offset ? ")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.size);
            ctx.pageParams.add(ctx.page.offset);
        }
        return ctx;
    }
    private static PageContextData prepareH2Page(PageContextData ctx) {
        return preparePostgreSqlPage(ctx);
    }
    private static PageContextData prepareSqlLitePage(PageContextData ctx) {
        return preparePostgreSqlPage(ctx);
    }
    private static PageContextData prepareHSqlPage(PageContextData ctx) {
        return preparePostgreSqlPage(ctx);
    }
    private static PageContextData prepareKingBaseEsPage(PageContextData ctx) {
        return preparePostgreSqlPage(ctx);
    }
    private static PageContextData preparePhoenixPage(PageContextData ctx) {
        return preparePostgreSqlPage(ctx);
    }
    private static PageContextData prepareDmPage(PageContextData ctx) {
        return prepareOraclePage(ctx);
    }
    private static PageContextData prepareGaussPage(PageContextData ctx) {
        return prepareOraclePage(ctx);
    }
    private static PageContextData prepareOracle12cPage(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(ctx.prepareSql)
                .addLine(" offset ? ")
                .addLine(" rows fetch next ? rows only ")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.offset);
            ctx.pageParams.add(ctx.page.size);
        }
        return ctx;
    }
    private static PageContextData prepareDb2Page(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(" SELECT * FROM ( ")
                .addLine(" SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                .addLine(ctx.prepareSql)
                .addLine(" ) AS TMP_PAGE ")
                .addLine(" ) TMP_PAGE ")
                .addLine(" WHERE ROW_ID BETWEEN ? AND ? ")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.offset);
            ctx.pageParams.add(ctx.page.offsetEnd);
        }
        return ctx;
    }
    private static PageContextData prepareSQLServerPage(PageContextData ctx) {
        String sql=Appender.builder()
                .addLine(ctx.prepareSql)
                .addLine(" offset ? ")
                .addLine(" rows fetch next ? rows only ")
                .get();
        ctx.pageSql=sql;
        if(ctx.params!=null){
            for(Object item : ctx.params){
                ctx.pageParams.add(item);
            }
        }
        if(ctx.page!=null){
            ctx.pageParams.add(ctx.page.offset);
            ctx.pageParams.add(ctx.page.size);
        }
        return ctx;
    }
}
