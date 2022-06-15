package i2f.generator.data;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/15 16:49
 * @desc
 */
@Data
public class JavaCodeContext {
    private String pkg;
    private String author;
    private boolean useLombok;
    private boolean useSwagger;
}
