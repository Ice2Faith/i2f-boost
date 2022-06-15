package i2f.generator.er;

import lombok.Data;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 20:44
 * @desc
 */
@Data
public class ErContext {
    private List<ErEntity> entities;
    private List<ErAttribute> attrs;
    private List<ErLine> lines;

}
