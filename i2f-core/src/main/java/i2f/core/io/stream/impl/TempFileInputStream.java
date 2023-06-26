package i2f.core.io.stream.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/6/26 14:02
 * @desc
 */
public class TempFileInputStream extends FileInputStream {
    protected String fileName;

    public TempFileInputStream(String name) throws FileNotFoundException {
        super(name);
        this.fileName = name;
    }

    public TempFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.fileName = file.getAbsolutePath();
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
