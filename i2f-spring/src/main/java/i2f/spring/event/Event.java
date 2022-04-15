package i2f.spring.event;

import org.springframework.context.ApplicationEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/15 9:23
 * @desc
 */
public class Event extends ApplicationEvent {
    protected int code;
    protected String msg;
    protected Object data;
    protected Map<String,Object> kvs;

    protected Object source;
    protected Date date;

    public Event(Object source) {
        super(source);
        source=source;
        date=new Date();
    }

    public Event() {
        super(null);
        source=null;
        date=new Date();
    }

    public Event(int code,String msg,Object data){
        this();
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    public Event(Object source,int code,String msg,Object data){
        this(source);
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public Event addKvs(String key,Object val){
        if(kvs==null){
            kvs=new HashMap<>();
        }
        kvs.put(key,val);
        return this;
    }

    public static Event builder(int code,Object data){
        return new Event(code,null,data);
    }

    public static Event builder(int code,String msg){
        return new Event(code,msg,null);
    }

    public static Event builder(int code,String msg,Object data){
        return new Event(code,msg,data);
    }

    public int getCode() {
        return code;
    }

    public Event setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Event setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Event setData(Object data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getKvs() {
        return kvs;
    }

    public Event setKvs(Map<String, Object> kvs) {
        this.kvs = kvs;
        return this;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public Event setSource(Object source) {
        this.source = source;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Event setDate(Date date) {
        this.date = date;
        return this;
    }
}
