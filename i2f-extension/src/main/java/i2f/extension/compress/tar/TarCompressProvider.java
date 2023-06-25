package i2f.extension.compress.tar;

import i2f.core.compress.ICompressProvider;
import i2f.core.compress.data.CompressBindData;
import i2f.core.compress.data.CompressBindFile;
import i2f.core.io.file.FileUtil;
import i2f.core.io.stream.StreamUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

/**
 * @author ltb
 * @date 2022/3/31 17:40
 * @desc
 */
public class TarCompressProvider implements ICompressProvider {
    @Override
    public void compress(File output, File... inputs) throws IOException {
        CompressBindFile[] binds=new CompressBindFile[inputs.length];
        for(int i=0;i< inputs.length;i++){
            binds[i]=new CompressBindFile();
            binds[i].setFile(inputs[i]);
        }
        compress(output,binds);
    }

    @Override
    public void compress(File output, CompressBindFile... inputs) throws IOException {
        if(!output.getParentFile().exists()){
            output.getParentFile().mkdirs();
        }
        TarArchiveOutputStream zos=new TarArchiveOutputStream(new FileOutputStream(output));
        for(CompressBindFile item : inputs){
            compressNext(zos,item);
        }
        zos.flush();
        zos.close();
    }

    @Override
    public void compress(File output, CompressBindData... inputs) throws IOException {
        if(!output.getParentFile().exists()){
            output.getParentFile().mkdirs();
        }
        TarArchiveOutputStream zos=new TarArchiveOutputStream(new FileOutputStream(output));
        for(CompressBindData item : inputs){
            compress(zos,item);
        }
        zos.flush();
        zos.close();
    }

    private void compress(TarArchiveOutputStream zos, CompressBindData item) throws IOException{
        String dir= item.getDirectory();
        if(dir==null){
            dir="";
        }
        String path=dir+"/"+item.getFileName();
        if("".equals(dir)){
            path= item.getFileName();
        }
        File tpFile= FileUtil.getTempFile();
        FileUtil.save(item.getInputStream(),tpFile);
        item.getInputStream().close();
        InputStream fis=new FileInputStream(tpFile);

        TarArchiveEntry entry=new TarArchiveEntry(item.getFileName());
        entry.setSize(tpFile.length());
        entry.setName(path);
        zos.putArchiveEntry(entry);
        StreamUtil.streamCopy(fis,zos,false);
        fis.close();
        zos.flush();
        zos.closeArchiveEntry();
    }

    private void compressNext(TarArchiveOutputStream zos,CompressBindFile bind) throws IOException{
        if(zos==null || bind==null){
            return;
        }
        File file=bind.getFile();
        String dir= bind.getDirectory();
        if(file==null || !file.exists()){
            return;
        }
        if(dir==null){
            dir="";
        }
        String path=dir+"/"+file.getName();
        if("".equals(dir)){
            path= file.getName();
        }
        if(file.isFile()){
            CompressBindData item=new CompressBindData();
            item.setDirectory(dir);
            item.setFileName(file.getName());
            item.setInputStream(new FileInputStream(file));
            compress(zos,item);
        }else{
            File[] files=file.listFiles();
            for(File item : files){
                CompressBindFile next=new CompressBindFile();
                next.setDirectory(path);
                next.setFile(item);
                compressNext(zos,next);
            }
        }
    }

    @Override
    public void release(InputStream input, File output) throws IOException {
        TarArchiveInputStream zis=new TarArchiveInputStream (input);
        TarArchiveEntry  entry=null;
        while((entry=zis.getNextTarEntry())!=null){
            File saveFile=new File(output,entry.getName());
            if(!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            FileOutputStream fos=new FileOutputStream(saveFile);
            StreamUtil.streamCopy(zis,fos,false);
            fos.flush();
            fos.close();
        }
    }

    @Override
    public void release(File input, File output) throws IOException {
        InputStream fis=new FileInputStream(input);
        release(fis,output);
        fis.close();
    }
}
