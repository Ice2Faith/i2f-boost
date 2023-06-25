package i2f.core.serialize.xml.exception;

import i2f.core.text.exception.TextFormatException;

/**
 * @author ltb
 * @date 2022/4/2 14:13
 * @desc
 */
public class XmlProcessException extends TextFormatException {
    public XmlProcessException() {
    }

    public XmlProcessException(String message) {
        super(message);
    }

    public XmlProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlProcessException(Throwable cause) {
        super(cause);
    }
}
