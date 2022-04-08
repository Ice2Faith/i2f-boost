package i2f.core.jdbc.wrapper.base;


import i2f.core.annotations.remark.Author;
import i2f.core.str.AppendUtil;

@Author("i2f")
public class SqlBaseWrapperBuilder extends SqlBase{
    private SqlBaseWrapper wrapper;
    public SqlBaseWrapperBuilder(){

    }
    public SqlBaseWrapperBuilder(SqlBaseWrapper wrapper){
        this.wrapper=wrapper;
    }
    public SqlBaseWrapperBuilder setWrapper(SqlBaseWrapper wrapper){
        this.wrapper=wrapper;
        return this;
    }
    public SqlBaseWrapperBuilder table(String tableName){
        wrapper.tableName=tableName;
        return this;
    }
    public SqlBaseWrapperBuilder table(Class clazz, String prefix, String suffix){
        wrapper.tableName= AppendUtil.buffer()
                .appends(true,false,null,prefix,suffix,clazz.getSimpleName())
                .done();
        return this;
    }
}
