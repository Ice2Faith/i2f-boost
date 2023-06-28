package i2f.core.serialize.bytes;

import i2f.core.serialize.adapter.BytesStringSerializerAdapter;
import i2f.core.serialize.str.IStringObjectSerializer;

public interface IBytesObjectSerializer extends IBytesTypeSerializer<Object> {
    default IStringObjectSerializer asStringSerializer() {
        return new BytesStringSerializerAdapter(this);
    }

    default IStringObjectSerializer asStringSerializer(String charset) {
        return new BytesStringSerializerAdapter(this, charset);
    }
}
