package i2f.core.database.jdbc.sql.wrapper.core;

/**
 * @author ltb
 * @date 2022/5/23 15:41
 * @desc
 */
public class TableWrapper<N>
        implements INextWrapper<N>{
    protected N next;
    public String schema;
    public String table;
    public String alias;
    public TableWrapper(N next){
        this.next=next;
    }
    public TableWrapper<N> schema(String name){
        this.schema=name;
        return this;
    }
    public TableWrapper<N> table(String name){
        this.table=name;
        return this;
    }
    public TableWrapper<N> table(String name,String alias){
        this.table=name;
        this.alias=alias;
        return this;
    }

    @Override
    public N next() {
        return next;
    }

    public String fullTable(){
        String ret=table;
        if(schema!=null && !"".equals(schema)){
            ret=schema+"."+table;
        }
        return ret;
    }

    public String aliasTable(){
        String ret=fullTable();
        if(alias!=null && !"".equals(alias)){
            ret+=" "+alias;
        }
        return ret;
    }
}
