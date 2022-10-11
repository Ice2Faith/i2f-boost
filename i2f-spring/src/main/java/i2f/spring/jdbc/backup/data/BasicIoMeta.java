package i2f.spring.jdbc.backup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/4 9:30
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicIoMeta {
    public Boolean compress;
    public String type;
    public List<String> packages;
    public Map<String, Object> meta;
}
