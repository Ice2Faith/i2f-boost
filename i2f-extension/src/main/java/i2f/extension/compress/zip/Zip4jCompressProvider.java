package i2f.extension.compress.zip;

import i2f.core.compress.ICompressProvider;
import i2f.core.compress.data.CompressBindData;
import i2f.core.compress.data.CompressBindFile;
import i2f.core.file.FileUtil;
import i2f.core.stream.StreamUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.*;

/**
 * @author ltb
 * @date 2022/3/31 16:50
 * @desc
 */
public class Zip4jCompressProvider implements ICompressProvider {
    private ZipParameters zipParameters;
    private String password;
    public Zip4jCompressProvider(){
        this(null);
    }
    public Zip4jCompressProvider(String password){
        this.password=password;
        ZipParameters parameters = new ZipParameters();
        // 压缩级别
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);

        if(password!=null && !"".equals(password)){
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.AES);
            parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_128);
        }

        this.zipParameters=parameters;
    }
    public Zip4jCompressProvider(ZipParameters zipParameters,String password){
        this.zipParameters=zipParameters;
        this.password=password;
    }

    @Override
    public void compress(File output, File... inputs) throws IOException {
        if(!output.getParentFile().exists()){
            output.getParentFile().mkdirs();
        }
        ZipFile zipFile=new ZipFile(output);
        if(password!=null && !"".equals(password)){
            zipFile.setPassword(password.toCharArray());
        }
        for(File item : inputs){
            if(!item.exists()){
                continue;
            }
            if(item.isDirectory()){
                zipFile.addFolder(item,zipParameters);
            }else{
                zipFile.addFile(item,zipParameters);
            }
        }
    }

    @Override
    public void compress(File output, CompressBindFile... inputs) throws IOException {
        File file=FileUtil.getTempFile();
        file.mkdirs();
        File[] files=new File[inputs.length];
        for(int i=0;i<files.length;i++){
            CompressBindFile item=inputs[i];
            File dir=new File(file.getAbsolutePath());
            if(item.getDirectory()!=null && !"".equals(item.getDirectory())){
                dir=new File(file,item.getDirectory());
            }
            dir.mkdirs();
            File tpFile=new File(dir,item.getFile().getName());
            FileUtil.copy(tpFile,item.getFile(),null);
        }
        compress(output,files);
    }

    @Override
    public void compress(File output, CompressBindData... inputs) throws IOException {
        File file=FileUtil.getTempFile();
        file.mkdirs();
        File[] files=new File[inputs.length];
        for(int i=0;i<files.length;i++){
            CompressBindData item=inputs[i];
            File dir=new File(file.getAbsolutePath());
            if(item.getDirectory()!=null && !"".equals(item.getDirectory())){
                dir=new File(file,item.getDirectory());
            }
            dir.mkdirs();
            File tpFile=new File(dir,item.getFileName());
            OutputStream fos=new FileOutputStream(tpFile);
            StreamUtil.streamCopy(item.getInputStream(),fos,false);
            fos.close();
            item.getInputStream().close();
        }
        compress(output,files);
    }

    @Override
    public void release(InputStream input, File output) throws IOException {
        File file= FileUtil.getTempFile();
        FileOutputStream fos=new FileOutputStream(file);
        StreamUtil.streamCopy(input,fos,false);
        fos.close();
        release(file,output);
    }

    @Override
    public void release(File input, File output) throws IOException {
        ZipFile zipFile=new ZipFile(input);
        if(zipFile.isEncrypted()){
            zipFile.setPassword(password.toCharArray());
        }
        zipFile.extractAll(output.getAbsolutePath());
    }
}
