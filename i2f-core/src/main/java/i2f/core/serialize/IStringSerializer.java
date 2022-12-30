package i2f.core.serialize;

import i2f.core.serialize.adapter.StringBytesSerializer;

public interface IStringSerializer extends ISerializer<String> {
    default IBytesSerializer asBytesSerializer() {
        return new StringBytesSerializer(this);
    }

    default IBytesSerializer asBytesSerializer(String charset) {
        return new StringBytesSerializer(this, charset);
    }
}
