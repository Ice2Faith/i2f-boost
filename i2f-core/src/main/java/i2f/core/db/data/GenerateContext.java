package i2f.core.db.data;

import i2f.core.annotations.remark.Author;
import i2f.core.str.Strings;

import java.io.File;

/**
 * @author ltb
 * @date 2021/9/28
 */
@Author("i2f")
public class GenerateContext {
    public String basePackage="com.xxx";
    public String author="xxx";
    public int genType=0;
    public String savePath;
    public TableMeta meta;
    public boolean save2File=false;
    public static String castTableName(String tableName){
        return Strings.toPascal(tableName);
    }
    public static String castColumnName(String columnName){
        return Strings.toCamel(columnName);
    }

    public static Builder build(){
        return new Builder();
    }

    public static class Builder{
        private GenerateContext ctx;
        public Builder(){
            ctx=new GenerateContext();
        }
        public GenerateContext done(){
            return ctx;
        }
        public Builder basePackage(String pack){
            ctx.basePackage=pack;
            return this;
        }
        public Builder author(String author){
            ctx.author=author;
            return this;
        }
        public Builder savePath(String path){
            ctx.savePath=path;
            return this;
        }
        public Builder savePath(File path){
            ctx.savePath=path.getAbsolutePath();
            return this;
        }
        public Builder table(TableMeta meta){
            ctx.meta=meta;
            return this;
        }
        public Builder save2File(boolean state){
            ctx.save2File=state;
            return this;
        }
    }
}
