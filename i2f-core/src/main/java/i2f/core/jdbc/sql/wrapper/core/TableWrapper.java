package i2f.core.jdbc.sql.wrapper.core;

/**
 * @author ltb
 * @date 2022/5/23 15:41
 * @desc
 */
public class TableWrapper<N> implements INextWrapper<N> {
    protected N next;
    public String table;
    public String alias;
    public TableWrapper(N next){
        this.next=next;
    }
    public TableWrapper<N> table(String name){
        this.table=table;
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
}
