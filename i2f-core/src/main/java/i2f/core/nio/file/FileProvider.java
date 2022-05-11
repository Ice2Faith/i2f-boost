package i2f.core.nio.file;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ltb
 * @date 2022/5/11 9:24
 * @desc
 */
public class FileProvider {
    public Path getPath(String path,String ... subs){
        return Paths.get(path,subs);
    }
    public Path normal(Path path){
        return path.normalize();
    }
}
