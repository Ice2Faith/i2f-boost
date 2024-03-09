package i2f.packet.io;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/3/8 14:12
 * @desc
 */
public class LocalOutputStreamInputAdapter extends OutputStream {
    public static final int DEFAULT_MEMORY_SIZE=512*1024; // 512kb
    protected OutputStream os;
    protected File file;
    protected int memorySize;
    protected int currentSize;
    protected boolean hasClose;
    protected boolean hasRead;

    public LocalOutputStreamInputAdapter(){
        this.memorySize=DEFAULT_MEMORY_SIZE;
        prepare();
    }

    public LocalOutputStreamInputAdapter(int memorySize) {
        this.memorySize = memorySize;
        prepare();
    }

    public void prepare() {
        this.currentSize=0;
        this.file=null;
        this.os=null;
        this.hasClose=false;
        this.hasRead=false;
        if(this.memorySize<=0){
            try{
                this.file=getTempFile();
                this.os=new FileOutputStream(this.file);
            }catch(Exception e){
                throw new IllegalStateException("create local template file stream error",e);
            }
        }else{
           this.os = new ByteArrayOutputStream();
        }
    }

    public static File getTempFile(){
        String fileName="local-" + Thread.currentThread().getId() + "-" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        String fileSuffix=".data";
        String name=fileName+fileSuffix;
        try{
            return File.createTempFile(fileName, fileSuffix);
        }catch(Exception e){

        }
        File dir=new File("./tmp");
        if(!dir.exists()){
            dir.mkdirs();
        }
        return new File(dir,name);
    }

    public static void copy(InputStream is,OutputStream os) throws IOException {
        byte[] buff=new byte[64*1024];
        int len=0;
        while((len=is.read(buff,0,buff.length))>0){
            os.write(buff,0,len);
        }
        os.flush();
        is.close();
    }

    public boolean isReadyInput(){
        return this.hasClose && !this.hasRead;
    }

    public InputStream getInputStream() {
        if(!this.hasClose){
            throw new IllegalStateException("adapter output stream not close, cannot provide input stream.");
        }
        if(this.hasRead){
            throw new IllegalStateException("input stream has read, cannot provide again");
        }
        this.hasRead=true;
        if(this.file!=null){
            try{
                return new TempFileInputStream(new FileInputStream(this.file),this.file);
            }catch(Exception e){
                throw new IllegalStateException("local file stream error",e);
            }
        }else{
            return new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
        }
    }

    public byte[] toByteArray() throws IOException {
        if(os instanceof ByteArrayOutputStream){
            return ((ByteArrayOutputStream) os).toByteArray();
        }else{
            InputStream is=getInputStream();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            copy(is,bos);
            bos.close();
            return bos.toByteArray();
        }
    }

    private void copyToTmpFile() throws IOException {
        if(this.currentSize<this.memorySize){
            return;
        }
        if(this.file!=null){
            return;
        }

        this.os.flush();
        this.os.close();

        this.file=getTempFile();
        OutputStream fos=new FileOutputStream(this.file);
        ByteArrayInputStream bis=new ByteArrayInputStream(((ByteArrayOutputStream)this.os).toByteArray());
        copy(bis,fos);

        this.os=fos;
    }

    @Override
    public void write(int b) throws IOException {
        copyToTmpFile();

        os.write(b);

        currentSize++;
    }

    @Override
    public void flush() throws IOException {
        this.os.flush();
    }

    @Override
    public void close() throws IOException {
        this.os.close();
        this.hasClose=true;
    }

    @Override
    public void write(byte[] b) throws IOException {
        copyToTmpFile();

        this.os.write(b);

        this.currentSize+=b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        copyToTmpFile();

        this.os.write(b, off, len);

        this.currentSize+=len;
    }

    public OutputStream getOs() {
        return os;
    }

    public File getFile() {
        return file;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public boolean isHasClose() {
        return hasClose;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalOutputStreamInputAdapter that = (LocalOutputStreamInputAdapter) o;
        return memorySize == that.memorySize &&
                currentSize == that.currentSize &&
                hasClose == that.hasClose &&
                hasRead == that.hasRead &&
                Objects.equals(os, that.os) &&
                Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(os, file, memorySize, currentSize, hasClose, hasRead);
    }

    @Override
    public String toString() {
        return "LocalOutputStreamInputAdapter{" +
                "os=" + os +
                ", file=" + file +
                ", memorySize=" + memorySize +
                ", currentSize=" + currentSize +
                ", hasClose=" + hasClose +
                ", hasRead=" + hasRead +
                '}';
    }
}
