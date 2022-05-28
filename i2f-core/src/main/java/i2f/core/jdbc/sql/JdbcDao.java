package i2f.core.jdbc.sql;

import i2f.core.annotations.remark.Author;
import i2f.core.jdbc.core.IJdbcMeta;
import i2f.core.jdbc.core.JdbcProvider;
import i2f.core.jdbc.core.TransactionManager;
import i2f.core.jdbc.data.DBResultData;
import i2f.core.jdbc.data.PageContextData;
import i2f.core.jdbc.data.PageMeta;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.wrapper.*;
import i2f.core.jdbc.sql.wrapper.core.BindSql;
import i2f.core.jdbc.sql.wrapper.core.PageBindSql;
import i2f.core.str.Appender;
import lombok.Data;

import java.sql.SQLException;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
@Data
public class JdbcDao {
    private IJdbcMeta meta;
    private JdbcProvider provider;
    private TransactionManager transactionManager;


    public JdbcDao(IJdbcMeta meta) throws SQLException {
        this.meta = meta;
        this.transactionManager=new TransactionManager(this.meta);
        this.provider = new JdbcProvider(this.transactionManager);
    }

    public JdbcDao(TransactionManager transactionManager) {
        this.transactionManager=transactionManager;
        this.provider = new JdbcProvider(this.transactionManager);
        this.meta = this.transactionManager.getMeta();
    }

    public DBResultData queryNative(String sql) throws SQLException {
        return provider.query(sql);
    }

    public int insertNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int updateNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int deleteNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public int executeNative(String sql) throws SQLException {
        return provider.update(sql);
    }

    public DBResultData queryAll(String tableName) throws SQLException {
        String sql = Appender.builder()
                .addsSep(" ", Sql.SELECT,"*",Sql.FROM,tableName)
                .get();

        return provider.query(sql);
    }

    public int emptyTable(String tableName) throws SQLException {
        String sql = Appender.builder()
                .addsSep(" ",Sql.DELETE_FORM,tableName)
                .get();
        return provider.update(sql);
    }

    public int dropTable(String tableName) throws SQLException {
        String sql = Appender.builder()
                .addsSep(" ",Sql.DROP_TABLE,tableName)
                .get();
        return provider.update(sql);
    }

    public int dropTableIfExists(String tableName) throws SQLException {
        String sql = Appender.builder()
                .addsSep(" ",Sql.DROP_TABLE,Sql.IF_EXISTS,tableName)
                .get();
        return provider.update(sql);
    }

    public long countTable(String tableName) throws SQLException {
        String sql = Appender.builder()
                .addsSep(" ",Sql.SELECT,"count(*)",Sql.FROM,tableName)
                .get();
        DBResultData rs = provider.query(sql);
        return (Long) rs.getData(0, 0);
    }

    public DBResultData queryCommon(QueryWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        return provider.queryRaw(bindSql.sql, bindSql.params);
    }

    public long queryCountCommon(QueryWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        String sql = Appender.builder()
                .addsSep("\n",
                        " select count(*) cnt ",
                        " from ( ",
                        bindSql.sql,
                        " ) tmp ")
                .get();

        DBResultData rs = provider.queryRaw(sql, bindSql.params);
        return (Long) rs.getData(0, 0);
    }

    public int deleteCommon(DeleteWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        int effecLine = provider.updateRaw(bindSql.sql, bindSql.params);
        return effecLine;
    }

    public int updateCommon(UpdateWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        int effecLine = provider.updateRaw(bindSql.sql, bindSql.params);
        return effecLine;
    }

    public int insertCommon(InsertWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        int effecLine = provider.updateRaw(bindSql.sql, bindSql.params);
        return effecLine;
    }

    public long insertCommonReturnId(InsertWrapper wrapper) throws SQLException {
        int effecLine = insertCommon(wrapper);
        String sql = "select @@IDENTITY";
        DBResultData rs = provider.query(sql);
        Long id = rs.getData(0, 0);
        return id;
    }

    public int insertCommonBatch(InsertBatchWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        int effecLine = provider.updateRaw(bindSql.sql, bindSql.params);
        return effecLine;
    }

    public PageContextData queryPage(QueryWrapper wrapper) throws SQLException {
        BindSql bindSql=wrapper.prepare();
        Long index=null;
        Long size=null;
        if(bindSql instanceof PageBindSql){
            PageBindSql pageBindSql=(PageBindSql)bindSql;

            index=pageBindSql.index;
            size=pageBindSql.size;
        }

        PageMeta page = new PageMeta(index, size);
        PageContextData ctx = provider.pageRaw(page, bindSql.sql, bindSql.params);
        return ctx;
    }
}
