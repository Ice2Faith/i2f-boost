package i2f.extension.serialize.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import i2f.core.serialize.str.json.IJsonSerializer;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 21:21
 * @desc
 */
public class GsonJsonSerializer implements IJsonSerializer {
    public static String dateFormatPatten = "yyyy-MM-dd HH:mm:ss SSS";
    public static GsonJsonSerializer INSTANCE = new GsonJsonSerializer();

    private Gson gson;

    public GsonJsonSerializer() {
        gson = new GsonBuilder()
                .setDateFormat(dateFormatPatten)
                .create();
    }

    public GsonJsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return (Map<String, Object>) deserialize(json, new TypeToken<Map<String, Object>>() {
        });
    }

    @Override
    public String serialize(Object obj) {
        return getGson().toJson(obj);
    }

    @Override
    public Object deserialize(String enc) {
        return getGson().fromJson(enc, Object.class);
    }

    @Override
    public Object deserialize(String text, Class<?> clazz) {
        return getGson().fromJson(text, clazz);
    }

    @Override
    public Object deserialize(String text, Object typeToken) {
        return deserialize(text, (TypeToken) typeToken);
    }

    public <T> T deserialize(String text, TypeToken<T> token) {
        return getGson().fromJson(text, token.getType());
    }
}
