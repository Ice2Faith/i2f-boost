package i2f.core.reflection.pkg.data;

import lombok.Data;

import java.io.File;
import java.net.URL;

/**
 * @author ltb
 * @date 2022/5/25 17:22
 * @desc
 */
@Data
public class ResourceMetaData {
    public URL url;
    public File file;
    public String name;
    public String fullName;
    public String location;
}
