package i2f.core.filesystem.core;

import i2f.core.filesystem.IFileSystem;
import i2f.core.stream.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/7 9:31
 * @desc
 */
public abstract class BasicFileSystem implements IFileSystem {
    public static final String PATH_OPERATOR="/";
    public static final String EXTENSION_OPERATOR=".";

    public String getLastSepAfter(String path,String sep,boolean keepSep){
        if(path==null){
            return null;
        }
        int idx=path.lastIndexOf(sep);
        if(idx>=0){
            if(keepSep){
                return path.substring(idx);
            }else{
                return path.substring(idx+sep.length());
            }
        }
        return "";
    }

    public String getLastSepBefore(String path,String sep,boolean keepSep){
        if(path==null){
            return null;
        }
        int idx=path.lastIndexOf(sep);
        if(idx>=0){
            if(keepSep){
                return path.substring(0,idx+sep.length());
            }else{
                return path.substring(0,idx);
            }
        }
        return path;
    }


    @Override
    public String getName(String path) {
        return getLastSepAfter(path,getPathOperator(),false);
    }

    @Override
    public String getExtension(String path) {
        return getLastSepAfter(path,getExtensionOperator(),true);
    }

    @Override
    public String getPath(String path) {
        return path;
    }

    @Override
    public String getParentPath(String path) {
        return getLastSepBefore(path,getPathOperator(),false);
    }

    @Override
    public String getPathOperator() {
        return PATH_OPERATOR;
    }

    @Override
    public String getExtensionOperator() {
        return EXTENSION_OPERATOR;
    }

    @Override
    public boolean create(String path) throws IOException {
        OutputStream os=null;
        try{
            os=writeOpen(path);
        }catch(IOException e){
            throw e;
        }finally {
            os.close();
        }
        return true;
    }

    @Override
    public boolean renameTo(String srcPath, String dstPath, boolean overwrite) throws IOException {
        return moveTo(srcPath, dstPath, overwrite);
    }


    @Override
    public boolean mkdirs(String path) throws IOException {
        String[] arr=path.split(getPathOperator());
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if(i!=0){
                builder.append(getPathOperator());
            }
            builder.append(arr[i]);
            String ppath=builder.toString();
            if(!exists(ppath)){
                mkdir(ppath);
            }
        }
        return true;
    }

    @Override
    public boolean copyTo(String srcPath, String dstPath, boolean overwrite) throws IOException {
        if(exists(dstPath) && !overwrite){
            return false;
        }
        if(exists(dstPath) && !isFile(dstPath)){
            return false;
        }
        if(overwrite){
            delete(dstPath);
        }
        InputStream is=null;
        OutputStream os=null;
        try{
            is=readOpen(srcPath);
            os=writeOpen(dstPath);
            StreamUtil.streamCopy(is,os,true,true);
        }catch(IOException e){
            throw  e;
        }finally {
            if(is!=null){
                is.close();
            }
            if(os!=null){
                os.close();
            }
        }
        return true;
    }

    @Override
    public boolean moveTo(String srcPath, String dstPath, boolean overwrite) throws IOException {
        boolean rs=copyTo(srcPath, dstPath, overwrite);
        if(rs){
            delete(srcPath);
        }
        return rs;
    }

    @Override
    public List<String> listFiles(String path) throws IOException {
        List<String> list=list(path);
        List<String> ret=new ArrayList<>();
        for(String item : list){
            if(isFile(item)){
                ret.add(item);
            }
        }
        return ret;
    }

    @Override
    public List<String> listDirectories(String path) throws IOException {
        List<String> list=list(path);
        List<String> ret=new ArrayList<>();
        for(String item : list){
            if(isDirectory(item)){
                ret.add(item);
            }
        }
        return ret;
    }
}
