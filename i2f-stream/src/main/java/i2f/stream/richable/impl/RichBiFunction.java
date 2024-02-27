package i2f.stream.richable.impl;

import i2f.stream.richable.BaseRichStreamProcessor;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:37
 * @desc
 */
public abstract class RichBiFunction<T,U,R> extends BaseRichStreamProcessor implements BiFunction<T,U,R> {
}
