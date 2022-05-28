package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.str.Appender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:34
 * @desc
 */
@Data
@NoArgsConstructor
public class PageBindSql extends BindSql{
    public Long index;
    public Long size;
    public PageBindSql(String sql){
        this.sql=sql;
    }
    public PageBindSql(String sql, List<Object> params){
        this.sql=sql;
        this.params=params;
    }
    public PageBindSql page(Long index,Long size){
        this.index=index;
        this.size=size;
        return this;
    }

    @Override
    public String show() {
        String pstr=super.show();
        return Appender.builder()
                .add(pstr)
                .line()
                .add("page:")
                .adds(index,",",size)
                .line()
                .addRepeat("-",20)
                .get();
    }
}
