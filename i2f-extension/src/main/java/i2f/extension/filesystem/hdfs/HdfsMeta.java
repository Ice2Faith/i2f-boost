package i2f.extension.filesystem.hdfs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.conf.Configuration;

@Data
@NoArgsConstructor
public class HdfsMeta {
    public String defaultFs;
    public String uri;
    public String user;
    public Configuration config;
}
