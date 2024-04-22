package i2f.container.iterator.impl;

import i2f.container.reference.Reference;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2024/4/19 9:31
 * @desc
 */
public class ReaderLineIterator extends ResourceIterator<BufferedReader, BufferedReader, String> {

    public ReaderLineIterator(BufferedReader bufferedReader) {
        super(bufferedReader,
                (e) -> e,
                e -> {
                    String line = e.readLine();
                    if (line == null) {
                        return Reference.finish();
                    }
                    return Reference.of(line);
                }, (holder, resource) -> resource.close());
    }

    public ReaderLineIterator(Reader reader) {
        this(new BufferedReader(reader));
    }

    public ReaderLineIterator(InputStream is, String charset) throws IOException {
        this(new BufferedReader(new InputStreamReader(is, charset)));
    }

    public ReaderLineIterator(File file, String charset) throws IOException {
        this(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)));
    }
}
