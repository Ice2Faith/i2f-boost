package i2f.core.serialize.std;

public interface ISerializer<TYPE> {
    TYPE serialize(Object obj);

    default TYPE serialize(Object obj, boolean formatted) {
        return serialize(obj);
    }

    <T> T deserialize(TYPE text, Class<T> clazz);

    <T> T deserialize(TYPE text, Object typeToken);
}
