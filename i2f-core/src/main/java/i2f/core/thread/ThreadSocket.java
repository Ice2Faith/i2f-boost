package i2f.core.thread;

import i2f.core.annotations.remark.Author;

import java.io.*;

/**
 * @author ltb
 * @date 2022/3/2 10:11
 * @desc 多线程之间通信
 */
@Author("i2f")
public class ThreadSocket {
    private PipedInputStream is;
    private PipedOutputStream os;
    public ThreadSocket(){
        is=new PipedInputStream();
        os=new PipedOutputStream();
    }
    public ThreadSocket(PipedInputStream is, PipedOutputStream os){
        this.is=is;
        this.os=os;
    }
    public InputStream getInputStream(){
        return is;
    }
    public OutputStream getOutputStream(){
        return os;
    }
    public void connect(ThreadSocket sock) throws IOException {
        is.connect(sock.os);
        os.connect(sock.is);
    }
}
