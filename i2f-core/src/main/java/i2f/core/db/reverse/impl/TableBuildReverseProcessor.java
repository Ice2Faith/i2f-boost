package i2f.core.db.reverse.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.collection.CollectionUtil;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.db.reverse.DbReverseEngineer;
import i2f.core.db.reverse.IReverseProcessor;

import java.util.*;

/**
 * @author ltb
 * @date 2022/3/23 23:36
 * @desc
 */
@Author("i2f")
public class TableBuildReverseProcessor implements IReverseProcessor<String> {
    private StringBuilder builder;
    private boolean withDrop;
    private boolean withCatalog;
    private boolean withSchema;
    private String fullTableName;
    private Map<String, Set<String>> primaryKeys=new HashMap<>();
    private Map<String, Set<String>> uniqueKeys=new HashMap<>();
    private Map<String,Set<String>> indexKeys=new HashMap<>();
    private Map<String,String> indexKeyUsings =new HashMap<>();
    private Set<String> defaultKeywords= CollectionUtil.hashSet("CURRENT_TIMESTAMP","NOW()");
    public TableBuildReverseProcessor(boolean withDrop,boolean withCatalog,boolean withSchema){
        this.withDrop=withDrop;
        this.withCatalog=withCatalog;
        this.withSchema=withSchema;
    }

    @Override
    public void onBegin(TableMeta meta) {
        builder=new StringBuilder();
        fullTableName="";
        if(withCatalog){
            fullTableName+=".";
            fullTableName+=meta.getCatalog();
        }
        if(withSchema){
            fullTableName+=".";
            fullTableName+=meta.getSchema();
        }
        fullTableName+=meta.getTable();
        if(fullTableName.startsWith(".")){
            fullTableName=fullTableName.substring(1);
        }
    }

    @Override
    public void onBeginColumns(TableMeta meta, List<TableColumnMeta> columns) {
        if(withDrop){
            builder.append("drop table if exists "+fullTableName+" ;\n");
        }
        builder.append("create table "+fullTableName+"\n");
        builder.append("(\n");
        for(TableColumnMeta item : columns){
            if("YES".equals(item.getIsPrimaryKey())){
                String keyName=item.getPrimaryKeyName();
                if(!primaryKeys.containsKey(keyName)){
                    primaryKeys.put(keyName,new HashSet<>());
                }
                primaryKeys.get(keyName).add(item.getName());
            }
            if("YES".equals(item.getIsUnique())){
                String keyName=item.getUniqueKeyName();
                if(!uniqueKeys.containsKey(keyName)){
                    uniqueKeys.put(keyName,new HashSet<>());
                }
                uniqueKeys.get(keyName).add(item.getName());
            }
            if("YES".equals(item.getIsIndex())){
                String keyName=item.getIndexKeyName();
                if(keyName.toUpperCase().contains("PRIMARY")){
                    continue;
                }
                if(!indexKeys.containsKey(keyName)){
                    indexKeys.put(keyName,new HashSet<>());
                }
                indexKeys.get(keyName).add(item.getName());
                if(item.getIndexUsing()!=null && !"".equals(item.getIndexUsing())) {
                    indexKeyUsings.put(keyName,item.getIndexUsing() );
                }
            }
        }
    }

    @Override
    public boolean onColumn(TableColumnMeta column, TableMeta meta, int index, int size, boolean isFirst, boolean isEnd, boolean isRenderFirst, int renderIndex) {
        String typeSize="";
        String autoIncrement="";
        String primaryKey="";
        String unique="";
        String nullable="";
        String defaultVal="";
        String comment="";
        if(column.getPrecision()>0){
            typeSize="("+column.getPrecision();
            if(column.getScale()>0){
                typeSize+=","+column.getScale();
            }else{
                typeSize+=")";
            }
        }
        if("YES".equals(column.getIsPrimaryKey())){
            if(primaryKeys.get(column.getPrimaryKeyName()).size()==1){
                primaryKey=" primary key";
            }
        }
        if("YES".equals(column.getIsUnique())){
            if(uniqueKeys.get(column.getUniqueKeyName()).size()==1){
                unique=" unique";
            }
        }
        if("NO".equals(column.getIsNullable())){
            nullable=" not null";
        }
        if("YES".equals(column.getIsAutoincrement())){
            autoIncrement=" auto_increment";
        }
        if(column.getColumnDef()!=null && !"".equals(column.getColumnDef())){
            String def=column.getColumnDef().toUpperCase();
            if(column.getColumnDef().matches("^\\d+$") || defaultKeywords.contains(column.getColumnDef())){
                defaultVal= " default "+column.getColumnDef();
            }else{
                defaultVal=" default '"+DbReverseEngineer.checkReplaceAll(column.getColumnDef(),"'","''")+"'";
            }
        }
        if(column.getRemark()!=null && !"".equals(column.getRemark())){
            comment=" comment '"+DbReverseEngineer.checkReplaceAll(column.getRemark(),"'","''")+"'";
        }
        builder.append("\t"+column.getName()+"\t\t"+column.getTypeName()+typeSize+autoIncrement+primaryKey+unique+nullable+defaultVal+comment);
        if(!isEnd){
            builder.append(",");
        }
        builder.append("\n");
        return true;
    }

    @Override
    public void onEndColumns(TableMeta meta, List<TableColumnMeta> columns) {
        for(String item : primaryKeys.keySet()){
            if(primaryKeys.get(item).size()>1){
                builder.append("\t,primary key "+item+" (");
                boolean isFirst=true;
                for(String col : primaryKeys.get(item)){
                    if(!isFirst){
                        builder.append(",");
                    }
                    builder.append(col);
                    isFirst=false;
                }
                builder.append(")\n");
            }
        }
        for(String item : uniqueKeys.keySet()){
            if(uniqueKeys.get(item).size()>1){
                builder.append("\t,unique "+item+" (");
                boolean isFirst=true;
                for(String col : uniqueKeys.get(item)){
                    if(!isFirst){
                        builder.append(",");
                    }
                    builder.append(col);
                    isFirst=false;
                }
                builder.append(")\n");
            }
        }
        for(String item : indexKeys.keySet()){
            if(indexKeys.get(item).size()>0){
                builder.append("\t,key "+item+" (");
                boolean isFirst=true;
                for(String col : indexKeys.get(item)){
                    if(!isFirst){
                        builder.append(",");
                    }
                    builder.append(col);
                    isFirst=false;
                }
                builder.append(")");
                if(indexKeyUsings.containsKey(item)){
                    builder.append(" using "+ indexKeyUsings.get(item));
                }
                builder.append("\n");
            }
        }
        String remark=meta.getRemark();
        if(remark!=null && !"".equals(remark)){
            builder.append(") comment '"+ DbReverseEngineer.checkReplaceAll(remark,"'","''")+"' ;\n");
        }else{
            builder.append(") ;\n");
        }
        builder.append("\n");
    }

    @Override
    public void onEnd(TableMeta meta) {

    }

    @Override
    public String onResult() {
        return builder.toString();
    }
}
