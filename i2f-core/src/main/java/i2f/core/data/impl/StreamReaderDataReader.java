package i2f.core.data.impl;

import i2f.core.cycle.exception.LifeCycleException;
import i2f.core.data.IDataReader;

import java.io.*;

/**
 * @author ltb
 * @date 2022/4/27 11:11
 * @desc
 */
public class StreamReaderDataReader implements IDataReader<String> {
    protected File file;
    protected InputStream inputStream;
    protected InputStreamReader streamReader;
    protected BufferedReader reader;
    protected String line;
    protected boolean deleteOnDestroy=false;
    public StreamReaderDataReader(File file){
        this.file=file;
    }
    public StreamReaderDataReader(InputStream inputStream){
        this.inputStream=inputStream;
    }
    public StreamReaderDataReader(InputStreamReader streamReader){
        this.streamReader=streamReader;
    }
    public StreamReaderDataReader(BufferedReader reader){
        this.reader=reader;
    }

    public boolean isDeleteOnDestroy() {
        return deleteOnDestroy;
    }

    public StreamReaderDataReader setDeleteOnDestroy(boolean deleteOnDestroy) {
        this.deleteOnDestroy = deleteOnDestroy;
        return this;
    }

    @Override
    public String read() throws IOException{
        return line;
    }

    @Override
    public boolean hasMore() throws IOException{
        line=reader.readLine();
        if(line==null){
            return false;
        }
        return true;
    }

    @Override
    public void create() {
        try{
            if(reader==null){
                if(streamReader==null){
                    if(inputStream==null){
                        inputStream=new FileInputStream(file);
                    }
                    streamReader=new InputStreamReader(inputStream);
                }
                reader=new BufferedReader(streamReader);
            }
        }catch(IOException e){
            throw new LifeCycleException(e.getMessage(),e);
        }
    }

    @Override
    public void destroy() {
        try{
            if(reader!=null){
                reader.close();
            }
            if(deleteOnDestroy){
                if(file!=null){
                    file.delete();
                }
            }
        }catch(IOException e){
            throw new LifeCycleException(e.getMessage(),e);
        }
    }
}
