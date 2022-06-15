package i2f.core.xml.parser;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/13 15:05
 * @desc
 */
@Data
@NoArgsConstructor
public class XmlCtx {
    private String name;

    private String tag;
    private String attr;
    private String content;

    private String xml;

    private int form;
    private int to;
}
