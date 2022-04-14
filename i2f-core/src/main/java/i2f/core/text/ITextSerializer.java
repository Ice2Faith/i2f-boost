package i2f.core.text;

/**
 * @author ltb
 * @date 2022/4/14 10:11
 * @desc
 */
public interface ITextSerializer {
    String serialize(Object obj);
    Object deserialize(String str);
}
