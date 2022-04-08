package i2f.extension.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import i2f.core.json.impl.AbstractJsonProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 21:21
 * @desc
 */
public class GsonJsonProcessor extends AbstractJsonProcessor {
    public static String dateFormatPatten="yyyy-MM-dd HH:mm:ss SSS";

    private Gson gson;
    public GsonJsonProcessor(){
        gson=new GsonBuilder()
                .setDateFormat(dateFormatPatten)
                .create();
    }
    public GsonJsonProcessor(Gson gson){
        this.gson=gson;
    }
    public Gson getGson(){
        return gson;
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json=toText(obj);
        return parseTextRef(json,new TypeToken<Map<String,Object>>(){});
    }

    @Override
    public String toText(Object obj) {
        return getGson().toJson(obj);
    }

    @Override
    public <T> T parseText(String text, Class<T> clazz) {
        return getGson().fromJson(text,clazz);
    }

    @Override
    public <T> T parseTextRef(String text, Object typeToken) {
        TypeToken<T> token=(TypeToken<T>)typeToken;
        return getGson().fromJson(text,token.getType());
    }
}
