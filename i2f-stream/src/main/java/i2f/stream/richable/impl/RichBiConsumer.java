package i2f.stream.richable.impl;

import i2f.stream.richable.BaseRichStreamProcessor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:37
 * @desc
 */
public abstract class RichBiConsumer<T,U> extends BaseRichStreamProcessor implements BiConsumer<T,U> {
}
