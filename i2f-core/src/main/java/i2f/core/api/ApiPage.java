package i2f.core.api;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.annotations.remark.Usage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/19 14:54
 * @desc
 */
@Author("i2f")
@Remark("defined page request model")
@Usage({"ApiPage.beginPage();","ApiPage.data(list);"})
@Data
@NoArgsConstructor
public class ApiPage<T> {
    //从0开始，表示页索引
    @Remark("begin form 0")
    private Integer index;
    //页大小
    private Integer size;
    //取数据的下标
    private Long offset;
    //总数据条数
    private Long total;
    //数据列表
    private List<T> list;
    public ApiPage(@Name("index") Integer index, @Name("size") Integer size){
        page(index,size);
    }
    public ApiPage<T> page(@Name("index") Integer index,@Name("size") Integer size){
        this.index=index;
        this.size=size;
        prepare();
        return this;
    }
    public ApiPage<T> prepare(){
        if(this.index!=null && this.index<0){
            this.index=0;
        }
        if(this.index!=null && this.size<0){
            this.size=20;
        }
        if(this.index!=null && this.size!=null){
            long lidx=this.index;
            long lsz=this.size;
            this.offset=lidx*lsz;
        }
        return this;
    }
    public ApiPage<T> data(@Name("total") Long total,@Name("list") List<T> list){
        this.total=total;
        this.list=list;
        return this;
    }
    public boolean valid(){
        prepare();
        return this.index!=null && this.size!=null;
    }
    public void beginPage(){
        if(this.index==null){
            this.index=0;
        }
        if(this.size==null){
            this.size=20;
        }
        prepare();
    }
    public ApiPage<T> data(@Name("list") List<T> list){
        this.list=list;
        return this;
    }
}
