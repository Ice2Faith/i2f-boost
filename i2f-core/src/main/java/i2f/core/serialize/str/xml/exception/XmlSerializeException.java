package i2f.core.serialize.str.xml.exception;

import i2f.core.serialize.exception.SerializeException;

/**
 * @author ltb
 * @date 2022/4/2 14:13
 * @desc
 */
public class XmlSerializeException extends SerializeException {
    public XmlSerializeException() {
    }

    public XmlSerializeException(String message) {
        super(message);
    }

    public XmlSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlSerializeException(Throwable cause) {
        super(cause);
    }
}
