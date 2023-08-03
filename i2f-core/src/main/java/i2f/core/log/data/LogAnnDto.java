package i2f.core.log.data;

import i2f.core.log.consts.LogLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/8/2 9:46
 * @desc
 */
@Data
@NoArgsConstructor
public class LogAnnDto {
    private boolean value=true;
    private boolean before=true;
    private boolean after=false;
    private boolean throwing=true;

    private LogLevel throwLevel=LogLevel.ERROR;

    private String module="";
    private String label="";
}
