package i2f.springboot.zplugin.config.data;

import i2f.core.std.api.ApiPage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/06/06 16:17
 * @desc
 */
@Data
@NoArgsConstructor
public class ServiceExportBaseVo {
    protected ApiPage page;
    protected String exportMode; // 1 仅分页数据，2 全量数据
}
