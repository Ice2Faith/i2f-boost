package i2f.core.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/20 16:41
 * @desc
 */
public class BindSql {
    protected String sql;
    protected List<Object> args=new ArrayList<>();

    public BindSql() {
    }

    public BindSql(String sql) {
        this.sql = sql;
    }

    public BindSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public String toSql(){
        StringBuilder builder=new StringBuilder();
        String[] arr = (" "+this.sql+" ").split("\\?");
        int i=0;
        for (String item : arr) {
            if(i>0){
                Object val = this.args.get(i - 1);
                if(val !=null){
                    if(!(val instanceof Number)){
                        String str=val.toString();
                        val="'"+str.replaceAll("'","''")+"'";
                    }
                }
                builder.append(val);
            }
            builder.append(item);
            i++;
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "BindSql{\n" +
                "\tsql:\t" + sql + "\n" +
                "\targs:\t" + args +"\n" +
                "}";
    }
}
