package i2f.stream.patten;

import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/7 8:38
 * @desc
 */
public class StreamingPatten<E> {

    protected Predicate<E> filter;
    protected int count;
    protected int maxCount;
    protected StreamingPatten<E> next;
    protected StreamingPatten<E> prev;

    public StreamingPatten(Predicate<E> filter, int count,StreamingPatten<E> prev) {
        this.filter = filter;
        this.count = count;
        this.maxCount=0;
        this.prev=prev;
        if(this.prev!=null){
            this.prev.next=this;
        }
    }

    public StreamingPatten(Predicate<E> filter, int count, int maxCount, StreamingPatten<E> prev) {
        this.filter = filter;
        this.count = count;
        this.maxCount = maxCount;
        this.prev = prev;
        if(this.prev!=null){
            this.prev.next=this;
        }
    }

    public static<T> StreamingPatten<T> begin(){
        return new StreamingPatten<>(null,1,null);
    }

    public static<T> StreamingPatten<T> begin(Predicate<T> filter){
        return new StreamingPatten<>(filter,1,null);
    }

    public StreamingPatten<E> next(Predicate<E> filter){
        return new StreamingPatten<>(filter,1,this);
    }

    public StreamingPatten<E> any(){
        return next(e->true);
    }

    public StreamingPatten<E> repeat(int count){
        this.count=count;
        return this;
    }

    public StreamingPatten<E> repeats(){
        this.count=-1;
        return this;
    }

    public StreamingPatten<E> follow(int maxCount,Predicate<E> filter){
        return new StreamingPatten<>(filter,1,maxCount,this);
    }

    public StreamingPatten<E> end(){
        StreamingPatten<E> curr=this;
        while(curr.prev!=null){
            curr=curr.prev;
        }
        if(curr.filter==null){
            curr=curr.next;
            if(curr!=null){
                curr.prev.next=null;
                curr.prev=null;
            }
        }
        return curr;
    }

    public Predicate<E> getFilter() {
        return filter;
    }

    public int getCount() {
        return count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public StreamingPatten<E> getNext() {
        return next;
    }

    public StreamingPatten<E> getPrev() {
        return prev;
    }
}
