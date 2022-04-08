package i2f.core.compress.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/3/31 15:31
 * @desc
 */
@Data
@NoArgsConstructor
public class CompressBindData {
    private String fileName;
    private String directory;
    private InputStream inputStream;
    public static CompressBindData instance(){
        return new CompressBindData();
    }
}
