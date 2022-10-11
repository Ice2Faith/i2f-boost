package test;

import i2f.core.db.reverse.DbReverseEngineer;
import i2f.core.db.reverse.impl.TableBuildReverseProcessor;
import i2f.core.db.core.DbBeanResolver;
import i2f.core.db.core.DbResolver;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.jdbc.core.JdbcMetaAdapter;
import i2f.core.jdbc.core.JdbcProvider;
import i2f.core.jdbc.core.TransactionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/24 21:39
 * @desc
 */
public class TestDbResolver {
    public static void main(String[] args) throws SQLException {
        testDbResolver(true);
        testDbBeanResolver();
    }
    public static void testDbResolver(boolean simple) throws SQLException {
        JdbcProvider provider = new JdbcProvider(new TransactionManager(
                new JdbcMetaAdapter()
                        .setDriver("com.mysql.cj.jdbc.Driver")
                        .setUrl("jdbc:mysql://39.105.33.11:3306/car_loc_db?useUnicode=true&characterEncoding=UTF-8")
                        .setUsername("root")
                        .setPassword("ltb12315")));

        Connection conn = provider.getConnection();
        TableMeta vehicleInfo= DbResolver.getTableMeta(conn,"rg_config");
        String code= DbReverseEngineer.makeBeanCodeByTable(vehicleInfo,"i2f.core.bean",true);
        System.out.println(code);
        if(simple){
            provider.getTransactionManager().close();
            return;
        }

        List<TableMeta> tables = DbResolver.getAllTables(conn,true, null);
        provider.getTransactionManager().close();

        List<String> codes=DbReverseEngineer.makeBeansByTables(tables,"i2f.core.bean",true);
        for(String item : codes){
            System.out.println(item);
        }

        for(TableMeta item : tables){
            System.out.println("create table "+item.getCatalog()+"."+item.getTable());
            System.out.println("(");
            List<TableColumnMeta> cols=item.getColumns();
            for(int i=0; i<cols.size();i++){
                TableColumnMeta col= cols.get(i);
                System.out.print("\t"+col.getName()
                        +"\t"+col.getTypeName()+"("+col.getColumnSize()+")"
                        +("YES".equals(col.getIsAutoincrement())?" auto_increment":"")
                        +("YES".equals(col.getIsNullable())?" default null":" not null")
                        +((col.getRemark()==null || "".equals(col.getRemark()))?"":" comment '"+col.getRemark()+"'"));
                if(i!=cols.size()-1){
                    System.out.print(",");
                }
                System.out.println();
            }
            System.out.println(") "+((item.getRemark()==null || "".equals(item.getRemark()))?"":" comment '"+item.getRemark()+"'")+" ;");
            System.out.println();
        }
    }

    public static void testDbBeanResolver() throws SQLException {
        TableMeta meta= DbBeanResolver.getTableMeta(RgConfig.class);
        System.out.println(meta);
        String str=DbReverseEngineer.makeBeanCodeByTable(meta,"i2f.core.bean",true);
        System.out.println(str);

        String sql=DbReverseEngineer.reverse(meta, new TableBuildReverseProcessor(true,true,true));

        System.out.println(sql);
    }
}
