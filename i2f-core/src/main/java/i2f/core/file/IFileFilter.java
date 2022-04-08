package i2f.core.file;

import i2f.core.annotations.remark.Author;

import java.io.File;

@Author("i2f")
public interface IFileFilter {
    boolean save(File item);
}
