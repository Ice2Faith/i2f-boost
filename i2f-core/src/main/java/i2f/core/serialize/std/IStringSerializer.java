package i2f.core.serialize.std;

import i2f.core.serialize.std.adapter.StringBytesSerializer;

public interface IStringSerializer extends ISerializer<String> {
    default IBytesSerializer asBytesSerializer() {
        return new StringBytesSerializer(this);
    }

    default IBytesSerializer asBytesSerializer(String charset) {
        return new StringBytesSerializer(this, charset);
    }
}
