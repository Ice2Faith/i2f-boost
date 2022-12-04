package i2f.core.streaming.support.source;

import i2f.core.streaming.api.source.ISourceStreaming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class FileTextLineSourceStreaming implements ISourceStreaming<String> {
    public File file;
    public String charset = "UTF-8";
    public BufferedReader reader;

    public FileTextLineSourceStreaming(File file) {
        this.file = file;
    }

    public FileTextLineSourceStreaming(File file, String charset) {
        this.file = file;
        this.charset = charset;
    }

    @Override
    public Iterator<String> iterator() {
        return new BufferedReaderIterator(reader);
    }

    public static class BufferedReaderIterator implements Iterator<String> {
        private BufferedReader reader;
        private String line = "";

        public BufferedReaderIterator(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            if (reader != null) {
                try {
                    line = reader.readLine();
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
            return reader != null && line != null;
        }

        @Override
        public String next() {
            return line;
        }
    }

    @Override
    public void create() {
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        System.out.println(this.getClass().getSimpleName() + " create");
    }

    @Override
    public void destroy() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        System.out.println(this.getClass().getSimpleName() + " destroy");

    }
}
