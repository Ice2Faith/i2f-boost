package i2f.container.iterator.impl;

import i2f.container.reference.Reference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:31
 * @desc
 */
public class FileLineIterator extends ResourceIterator<File, BufferedReader, String> {

    public FileLineIterator(File file, String charset) {
        super(file,
                (e) -> new BufferedReader(new InputStreamReader(new FileInputStream(e), charset)),
                e -> {
                    String line = e.readLine();
                    if (line == null) {
                        return Reference.finish();
                    }
                    return Reference.of(line);
                }, (holder, resource) -> resource.close());
    }

}
