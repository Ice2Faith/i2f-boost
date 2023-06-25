package i2f.core.compress.impl;

import i2f.core.compress.ICompressProvider;
import i2f.core.compress.data.CompressBindData;
import i2f.core.compress.data.CompressBindFile;
import i2f.core.io.stream.StreamUtil;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author ltb
 * @date 2022/3/31 15:38
 * @desc
 */
public class JdkZipCompressProvider implements ICompressProvider {

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
        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(output));
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
        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(output));
        for(CompressBindData item : inputs){
            compress(zos,item);
        }
        zos.flush();
        zos.close();
    }

    private void compress(ZipOutputStream zos,CompressBindData item) throws IOException{
        String dir= item.getDirectory();
        if(dir==null){
            dir="";
        }
        String path=dir+"/"+item.getFileName();
        if("".equals(dir)){
            path= item.getFileName();
        }
        ZipEntry entry=new ZipEntry(path);
        zos.putNextEntry(entry);
        StreamUtil.streamCopy(item.getInputStream(),zos,false);
        item.getInputStream().close();
        zos.flush();
        zos.closeEntry();
    }

    private void compressNext(ZipOutputStream zos,CompressBindFile bind) throws IOException{
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
        ZipInputStream zis=new ZipInputStream(input);
        ZipEntry entry=null;
        while((entry=zis.getNextEntry())!=null){
            File saveFile=new File(output,entry.getName());
            if(!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            FileOutputStream fos=new FileOutputStream(saveFile);
            StreamUtil.streamCopy(zis,fos,false);
            fos.flush();
            fos.close();
            zis.closeEntry();
        }
    }

    @Override
    public void release(File input, File output) throws IOException {
        InputStream fis=new FileInputStream(input);
        release(fis,output);
        fis.close();
    }
}
