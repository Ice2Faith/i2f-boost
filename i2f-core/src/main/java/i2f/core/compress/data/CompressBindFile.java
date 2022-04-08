package i2f.core.compress.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author ltb
 * @date 2022/3/31 15:41
 * @desc
 */
@Data
@NoArgsConstructor
public class CompressBindFile {
    private File file;
    private String directory;
    public static CompressBindData instance(){
        return new CompressBindData();
    }
}
