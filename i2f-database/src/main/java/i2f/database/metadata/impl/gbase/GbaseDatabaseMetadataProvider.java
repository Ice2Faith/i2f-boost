package i2f.database.metadata.impl.gbase;

import i2f.database.jdbc.data.StdType;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.impl.mysql.MysqlDatabaseMetadataProvider;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:56
 * @desc
 */
public class GbaseDatabaseMetadataProvider extends MysqlDatabaseMetadataProvider {

    public static final String DRIVER_NAME="com.gbase.jdbc.Driver";

    public static final String MAVEN_DEPENDENCY="<dependency>\n" +
            "            <groupId>com.gbase</groupId>\n" +
            "            <artifactId>gbase-jdbc</artifactId>\n" +
            "            <version>8.3.81.53-build55.2.1-bin</version>\n" +
            "        </dependency>";

    @Override
    public ColumnMeta parseColumn(Map<String,Object> row){
        ColumnMeta col=new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"),0));
        col.setName(asString(row.get("COLUMN_NAME"),null));
        col.setType(asString(row.get("TYPE_NAME"),null));
        if(col.getType()!=null){
            col.setType(col.getType().split("\\(",2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"),null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"),0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"),0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"),null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"),null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"),null))));
        col.setDefaultValue(asString(row.get("COLUMN_DEF"),null));

        StdType type=StdType.VARCHAR;
        GbaseType gbaseType= null;
        for (GbaseType item : GbaseType.values()) {
            if(item.text().equalsIgnoreCase(col.getType())){
                type=item.stdType();
                gbaseType=item;
                break;
            }
        }

        String columnType=col.getType();
        if(gbaseType!=null){
            if(gbaseType.precision()){
                columnType+="("+col.getPrecision();
                if(gbaseType.scale()){
                    columnType+=","+col.getScale();
                }
                columnType+=")";
            }
        }else{
            if(type.precision()){
                columnType+="("+col.getPrecision();
                if(type.scale()){
                    columnType+=","+col.getScale();
                }
                columnType+=")";
            }
        }
        col.setColumnType(columnType);

        col.setJavaType(type.javaType().getSimpleName());
        col.setJdbcType(type.jdbcType().getName());
        col.setStdType(type.text());
        col.setLooseJavaType(type.looseJavaType().getSimpleName());
        col.setLooseJdbcType(type.looseJdbcType().getName());
        return col;
    }
}
