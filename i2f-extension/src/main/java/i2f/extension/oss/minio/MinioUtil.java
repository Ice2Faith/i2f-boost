package i2f.extension.oss.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.Data;

import java.io.*;

/**
 * @author ltb
 * @date 2022/7/8 9:09
 * @desc
 */
@Data
public class MinioUtil {
    protected MinioClient client;
    public MinioUtil(MinioClient client){
        this.client=client;
    }
    public MinioUtil(MinioMeta meta){
        this.client=getClient(meta);
    }

    public static MinioClient getClient(MinioMeta meta){
        return MinioClient.builder()
                .endpoint(meta.getUrl())
                .credentials(meta.getAccessKey(), meta.getSecretKey())
                .build();
    }

    public boolean bucketExists(String bucketName) throws IOException {
        try{
            return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public void bucketCreate(String bucketName) throws IOException {
        if(bucketExists(bucketName)){
            return;
        }
        try{
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public void bucketRemove(String bucketName) throws IOException {
        try{
            client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }



    public ObjectStat getObjectInfo(String bucketName,String objectName) throws IOException {
        try{
            return client.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public ObjectWriteResponse upload(String bucketName, String objectName, InputStream is,Long fileSize,Long partSize,String type) throws IOException {
        try{
            return client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(is,fileSize,partSize)
                    .contentType(type).build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public ObjectWriteResponse upload(String bucketName,String objectName,InputStream is,Long fileSize,String contentType) throws IOException {
        return upload(bucketName,objectName,is,fileSize,-1L,contentType);
    }

    public ObjectWriteResponse upload(String bucketName,String objectName,InputStream is,String contentType) throws IOException {
        return upload(bucketName,objectName,is,-1L,-1L,contentType);
    }

    public ObjectWriteResponse upload(String bucketName, String objectName, File file, String contentType) throws IOException {
        InputStream is=null;
        try{
            is=new FileInputStream(file);
            return upload(bucketName,objectName,is,file.length(),-1L,contentType);
        }finally {
            if(is!=null){
                is.close();
            }
        }
    }

    public InputStream download(String bucketName,String objectName) throws IOException {
        try{
            return client.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public Iterable<Result<Item>> list(String bucketName){
        return client.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .recursive(false)
                .build());
    }

    public Iterable<Result<Item>> list(String bucketName,String prefix){
        return client.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(false)
                .build());
    }

    public boolean prefixExists(String bucketName,String prefix) throws IOException {
        try{
            Iterable<Result<Item>> iterable=list(bucketName, prefix);
            for(Result<Item> result : iterable){
                Item item= result.get();
                if(item.isDir()){
                    return true;
                }
            }
            return false;
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public void prefixCreate(String bucketName,String prefix) throws IOException {
        try{
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(prefix)
                    .stream(new ByteArrayInputStream(new byte[0]),0,-1)
                    .build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public String urlOf(String bucketName,String objectName) throws IOException {
        try{
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        }catch(Exception e){
            throw new IOException(e.getMessage(),e);
        }
    }

    public String urlOf(ObjectWriteResponse resp) throws IOException {
        return urlOf(resp.bucket(),resp.object());
    }

}
