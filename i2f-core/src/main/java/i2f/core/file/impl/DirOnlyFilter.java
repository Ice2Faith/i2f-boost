package i2f.core.file.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.file.IFileFilter;

import java.io.File;

@Author("i2f")
public class DirOnlyFilter implements IFileFilter {

    @Override
    public boolean save(File item) {
        return item.isDirectory();
    }
}
