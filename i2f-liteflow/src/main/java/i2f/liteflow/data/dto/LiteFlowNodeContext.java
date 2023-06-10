package i2f.liteflow.data.dto;

import i2f.liteflow.data.vo.LiteFlowInstanceVo;
import i2f.liteflow.data.vo.LiteFlowLogVo;
import i2f.liteflow.spel.SpelEnhancer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/10 15:22
 * @desc
 */
@Data
@NoArgsConstructor
public class LiteFlowNodeContext {
    private LiteFlowLogVo log;
    private LiteFlowInstanceVo instance;
    private Map<String,Object> params;
    private SpelEnhancer enhancer;
    private LiteFlowDagDto dag;
}
