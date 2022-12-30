package i2f.extension.json.gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public class GsonUtil {

    private static volatile GsonJsonProcessor processor=new GsonJsonProcessor();
    /**
     * 将对象转换为Json串
     * 用法：
     * String js=toJson(new Admin());
     * @param obj 对象
     * @param <T> 对象类型
     * @return Json串
     */
    public static<T>String toJson(T obj) throws IOException {
        return processor.serialize(obj);
    }

    /**
     * 将一个Json串解析为对象
     * 用法：
     * Admin admin=fromJson(js,Admin.class);
     * @param json Json串
     * @param clazz 类类型
     * @param <T> 类型
     * @return 类对象
     */
    public static<T>T fromJson(String json,Class<T>clazz) throws IOException {
        return processor.deserialize(json, clazz);
    }


    /**
     * 将一个Json串解析为对象集合
     * 用法：
     * Lis<Admin> list=fromJsonArray(js);
     * @param json Json串
     * @param <T> 类型
     * @return 对象集合
     */
    public static<T>T fromJsonTypeToken(String json) throws IOException {
        return processor.deserialize(json, new TypeToken<T>() {
        });
    }

    public static JsonObject formJson(String json){
        JsonParser parser=new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }
}
