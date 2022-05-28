package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Triple;
import i2f.core.jdbc.sql.enums.SqlJoin;
import i2f.core.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:19
 * @desc
 */
public class JoinWrapper<N> implements INextWrapper<N> {
    protected N next;
    public List<Triple<String,String,Object>> kvs=new ArrayList<>();
    protected String type=SqlJoin.INNER.type();
    public JoinWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }

    public JoinWrapper<N> type(String type){
        this.type=type;
        return this;
    }
    public JoinWrapper<N> type(SqlJoin join){
        this.type=join.type();
        return this;
    }
    public JoinWrapper<N> inner(){
        return type(SqlJoin.INNER);
    }
    public JoinWrapper<N> left(){
        return type(SqlJoin.LEFT);
    }
    public JoinWrapper<N> right(){
        return type(SqlJoin.RIGHT);
    }
    public JoinWrapper<N> outer(){
        return type(SqlJoin.OUTER);
    }
    //////////////////////////////////////
    public JoinWrapper<N> join(String table, String ... conditions){
        String cond= Appender.builder().addsFull(" ",null,null,conditions).get();
        kvs.add(new Triple<>(type,table,cond));
        return this;
    }

    public String joinTables(){
        return Appender.builder()
                .addCollection(kvs,"\n",null,null,(Object val)->{
                    Triple<String,String,Object> item=(Triple<String,String,Object>)val;
                    return Appender.builder().addsSep(" ",item.fst,item.sec,item.trd).get();
                }).get();
    }
}
