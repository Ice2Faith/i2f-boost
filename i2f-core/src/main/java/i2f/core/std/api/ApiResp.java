package i2f.core.std.api;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.annotations.remark.Usage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/19 14:44
 * @desc
 */
@Author("i2f")
@Remark("defined server standard response format")
@Usage({"ApiResp.success(data);","ApiResp.error(\"bad request\");"})
@Data
@NoArgsConstructor
public class ApiResp<T> {
    @Remark("code value form ApiCode, suggest")
    private int code;
    private String msg;
    private T data;
    @Remark("multiply data response cloud store in here by add()")
    private Map<String,Object> kvs;

    public ApiResp(@Name("code") int code, @Name("msg") String msg, @Name("data") T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public ApiResp<T> add(@Name("key") String key,@Name("val")Object val){
        if(kvs==null){
            kvs=new HashMap<>();
        }
        kvs.put(key,val);
        return this;
    }

    public ApiResp<T> code(@Name("code")int code){
        this.code=code;
        return this;
    }

    public ApiResp<T> msg(@Name("msg")String msg){
        this.msg=msg;
        return this;
    }

    public ApiResp<T> data(@Name("data")T data){
        this.data=data;
        return this;
    }

    public static<T> ApiResp<T> success(@Name("data")T data){
        return new ApiResp<>(ApiCode.SUCCESS,"success",data);
    }

    public static<T> ApiResp<T> success(@Name("data")T data,@Name("msg")String msg){
        return new ApiResp<>(ApiCode.SUCCESS,msg,data);
    }

    public static<T> ApiResp<T> error(@Name("msg")String msg){
        return new ApiResp<>(ApiCode.ERROR,msg,null);
    }

    public static<T> ApiResp<T> error(@Name("code")int code,@Name("msg")String msg){
        return new ApiResp<>(code,msg,null);
    }

    public static<T> ApiResp<T> resp(@Name("code")int code,@Name("msg")String msg,@Name("data")T data){
        return new ApiResp<>(code,msg,data);
    }
}
