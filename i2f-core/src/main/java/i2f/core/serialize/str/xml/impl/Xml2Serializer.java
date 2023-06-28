package i2f.core.serialize.str.xml.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.serialize.str.xml.IXmlSerializer;

/**
 * @author ltb
 * @date 2022/3/24 17:29
 * @desc
 */
@Author("i2f")
public class Xml2Serializer implements IXmlSerializer {
    @Override
    public String serialize(Object obj) {
        return Xml2.toXml(obj);
    }


    @Override
    public Object deserialize(String enc) {
        throw new UnsupportedOperationException("Xml2 un-support parseText.");
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        throw new UnsupportedOperationException("Xml2 un-support parseText.");
    }

    @Override
    public Object deserialize(String enc, Object type) {
        throw new UnsupportedOperationException("Xml2 un-support parseText.");
    }

}
