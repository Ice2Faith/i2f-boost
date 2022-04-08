package i2f.core.xml;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/24 17:29
 * @desc
 */
@Author("i2f")
public class Xml2Processor implements IXmlProcessor {
    @Override
    public String toText(Object obj) {
        return Xml2.toXml(obj);
    }

    @Override
    public <T> T parseText(String json, Class<T> clazz) {
        throw new UnsupportedOperationException("Xml2 un-support parseText.");
    }

    @Override
    public <T> T parseTextRef(String json, Object typeToken) {
        throw new UnsupportedOperationException("Xml2 un-support parseTextRef.");
    }
}
