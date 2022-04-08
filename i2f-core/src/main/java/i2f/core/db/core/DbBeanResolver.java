package i2f.core.db.core;

import i2f.core.annotations.remark.Author;
import i2f.core.db.annotations.*;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/3/24 18:02
 * @desc
 */
@Author("i2f")
public class DbBeanResolver {
    public static TableMeta getTableMeta(Class clazz){
        if(clazz==null){
            return null;
        }
        TableMeta meta=new TableMeta();
        Set<Annotation> classAnns=ReflectResolver.getAllAnnotations(clazz);
        for(Annotation item : classAnns){
            if(item instanceof DbCatalog){
                DbCatalog dban=(DbCatalog)item;
                meta.setCatalog(dban.value());
            }
            if(item instanceof DbSchema){
                DbSchema dban=(DbSchema)item;
                meta.setSchema(dban.value());
            }
            if(item instanceof DbName){
                DbName dban=(DbName)item;
                meta.setTable(dban.value());
            }
            if(item instanceof DbType){
                DbType dban=(DbType)item;
                meta.setTypeName(dban.value());
            }
            if(item instanceof DbComment){
                DbComment dban=(DbComment)item;
                meta.setRemark(dban.value());
            }
        }
        List<TableColumnMeta> columns=new ArrayList<>();
        meta.setColumns(columns);
        List<PropertyAccessor> fields= ReflectResolver.getLogicalReadWriteFields(clazz);

        for(PropertyAccessor item : fields){
            Field field=item.getField();
            if(field==null){
                continue;
            }
            Set<Annotation> fieldAnns=ReflectResolver.getAllAnnotations(field);
            boolean isDbField=false;
            TableColumnMeta col=new TableColumnMeta();
            for(Annotation ann : fieldAnns){
                if(ann instanceof DbName){
                    DbName dban=(DbName)ann;
                    col.setName(dban.value());
                    isDbField=true;
                }
                if(ann instanceof DbComment){
                    DbComment dban=(DbComment)ann;
                    col.setRemark(dban.value());
                }
                if(ann instanceof DbType){
                    DbType dban=(DbType)ann;
                    col.setTypeName(dban.value());
                    col.setPrecision(dban.precision());
                    col.setScale(dban.scale());
                }
                if(ann instanceof DbNullable){
                    DbNullable dban=(DbNullable)ann;
                    col.setIsNullable(dban.value()?"YES":"NO");
                }
                if(ann instanceof DbAutoIncrement){
                    DbAutoIncrement dban=(DbAutoIncrement)ann;
                    col.setIsAutoincrement(dban.value()?"YES":"NO");
                }
                if(ann instanceof DbPrimaryKey){
                    DbPrimaryKey dban=(DbPrimaryKey)ann;
                    col.setIsPrimaryKey(dban.value()?"YES":"NO");
                    col.setPrimaryKeyName(dban.key());
                }
                if(ann instanceof DbUnique){
                    DbUnique dban=(DbUnique)ann;
                    col.setIsUnique(dban.value()?"YES":"NO");
                    col.setUniqueKeyName(dban.key());
                }
                if(ann instanceof DbDefault){
                    DbDefault dban=(DbDefault)ann;
                    col.setColumnDef(dban.value());
                }
                if(ann instanceof DbIndex){
                    DbIndex dban=(DbIndex)ann;
                    col.setIsIndex(dban.value()?"YES":"NO");
                    col.setIndexKeyName(dban.key());
                    col.setIndexType(dban.type());
                    col.setIndexUsing(dban.using());
                }
            }
            if(!isDbField){
                continue;
            }
            col.setCatalogName(meta.getCatalog());
            col.setSchemaName(meta.getSchema());
            col.setTableName(meta.getTable());
            col.setJavaType(field.getType());
            col.setJavaTypeString(field.getType().getName());
            columns.add(col);
        }

        columns.sort(new Comparator<TableColumnMeta>() {
            @Override
            public int compare(TableColumnMeta o1, TableColumnMeta o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return meta;
    }
}
