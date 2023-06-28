package i2f.core.serialize.str;

import i2f.core.serialize.adapter.StringBytesSerializerAdapter;
import i2f.core.serialize.bytes.IBytesObjectSerializer;

public interface IStringObjectSerializer extends IStringTypeSerializer<Object> {
    default IBytesObjectSerializer asBytesSerializer() {
        return new StringBytesSerializerAdapter(this);
    }

    default IBytesObjectSerializer asBytesSerializer(String charset) {
        return new StringBytesSerializerAdapter(this, charset);
    }
}
