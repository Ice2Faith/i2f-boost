package i2f.core.database.jdbc.sql.wrapper.core;

/**
 * @author ltb
 * @date 2022/5/23 15:41
 * @desc
 */
public class PageWrapper<N>
        implements INextWrapper<N>{
    protected N next;
    public Long index;
    public Long size;
    public PageWrapper(N next){
        this.next=next;
    }

    public PageWrapper<N> page(Long index,Long size){
        this.index=index;
        this.size=size;
        return this;
    }

    @Override
    public N next() {
        return next;
    }

}
