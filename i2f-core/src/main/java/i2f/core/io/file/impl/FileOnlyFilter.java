package i2f.core.io.file.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.io.file.IFileFilter;

import java.io.File;

@Author("i2f")
public class FileOnlyFilter implements IFileFilter {

    @Override
    public boolean save(File item) {
        return item.isFile();
    }
}
