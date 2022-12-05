package i2f.core.streaming.support.sink;

import i2f.core.functional.common.IMapper;
import i2f.core.streaming.api.sink.ISinkStreaming;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

public class FileTextLineSinkStreaming<E> implements ISinkStreaming<Integer, E> {
    public File file;
    private IMapper<String, E> mapper;
    public String charset = "UTF-8";
    public BufferedWriter writer;


    public FileTextLineSinkStreaming(File file, IMapper<String, E> mapper) {
        this.file = file;
        this.mapper = mapper;
    }

    public FileTextLineSinkStreaming(File file, IMapper<String, E> mapper, String charset) {
        this.file = file;
        this.mapper = mapper;
        this.charset = charset;
    }

    @Override
    public Integer sink(Iterator<E> iterator, ExecutorService pool) {
        int cnt = 0;
        try {
            while (iterator.hasNext()) {
                E item = iterator.next();
                String str = mapper.get(item);
                writer.write(str);
                writer.newLine();
                cnt++;
            }
            writer.flush();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return cnt;
    }

    @Override
    public void create() {
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        System.out.println(this.getClass().getSimpleName() + " create");
    }

    @Override
    public void destroy() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        System.out.println(this.getClass().getSimpleName() + " destroy");

    }
}
