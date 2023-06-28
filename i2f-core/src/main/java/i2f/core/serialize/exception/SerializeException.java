package i2f.core.serialize.exception;

import i2f.core.codec.exception.CodecException;

public class SerializeException extends CodecException {
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

}
