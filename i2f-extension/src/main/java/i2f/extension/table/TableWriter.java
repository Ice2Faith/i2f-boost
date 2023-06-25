package i2f.extension.table;


import i2f.core.type.tuple.impl.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/30 8:50
 * @desc
 */
public interface TableWriter {
    void write(List<Tuple2<String, String>> heads,
               List<?> data,
               File file) throws IOException;
}
