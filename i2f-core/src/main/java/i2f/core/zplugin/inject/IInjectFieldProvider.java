package i2f.core.zplugin.inject;

/**
 * @author ltb
 * @date 2022/5/9 15:41
 * @desc
 */
public interface IInjectFieldProvider {
    Object getValue(String fieldName) throws Exception;
}
