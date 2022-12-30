package i2f.core.serialize;

import i2f.core.serialize.adapter.BytesStringSerializer;

public interface IBytesSerializer extends ISerializer<byte[]> {
    default IStringSerializer asStringSerializer() {
        return new BytesStringSerializer(this);
    }

    default IStringSerializer asStringSerializer(String charset) {
        return new BytesStringSerializer(this, charset);
    }
}
