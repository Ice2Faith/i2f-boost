package i2f.core.dict.type;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:01
 * @desc
 */
public interface ITypeConverter {
    boolean support(Object obj, Class<?> tarType);

    Object convert(Object obj, Class<?> tarType);
}
