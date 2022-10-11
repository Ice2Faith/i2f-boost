package i2f.core.jdbc.data;

import i2f.core.annotations.remark.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
public class PageContextData {
    public String prepareSql;
    public String pageSql;
    public List<Object> pageParams=new ArrayList<>();
    public String countSql;
    public List<Object> countParams=new ArrayList<>();
    public List<Object> params;
    public DBResultList data;
    public Long count;
    public PageMeta page;
    public PageContextData(String prepareSql, List<Object> params){
        this.prepareSql=prepareSql;
        this.params=params;
    }
    public PageContextData(PageMeta page,String prepareSql,List<Object> params){
        this.page=page;
        this.prepareSql=prepareSql;
        this.params=params;
    }

}
