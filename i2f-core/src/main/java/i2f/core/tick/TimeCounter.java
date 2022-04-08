package i2f.core.tick;

import i2f.core.annotations.remark.Author;

@Author("i2f")
public class TimeCounter {
    private volatile long start;
    private volatile long end;
    public TimeCounter(){
        start=System.currentTimeMillis();
    }
    public TimeCounter begin(){
        start=System.currentTimeMillis();
        return this;
    }
    public TimeCounter end(){
        end=System.currentTimeMillis();
        return this;
    }
    public long duration(){
        return this.end-this.start;
    }
}
