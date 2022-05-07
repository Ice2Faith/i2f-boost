package i2f.core.filesystem.impl;

import i2f.core.filesystem.core.BasicFileSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/7 10:13
 * @desc
 */
public class JdkFileSystem extends BasicFileSystem {
    public File getFile(String path){
        return new File(path);
    }

    @Override
    public String getName(String path) {
        return getFile(path).getName();
    }

    @Override
    public String getPath(String path) {
        return getFile(path).getPath();
    }

    @Override
    public String getAbsolutePath(String path) throws IOException {
        return getFile(path).getAbsolutePath();
    }

    @Override
    public boolean isFile(String path) throws IOException {
        return getFile(path).isFile();
    }

    @Override
    public boolean isDirectory(String path) throws IOException {
        return getFile(path).isDirectory();
    }

    @Override
    public boolean delete(String path) throws IOException {
        return getFile(path).delete();
    }

    @Override
    public boolean exists(String path) throws IOException {
        return getFile(path).exists();
    }

    @Override
    public boolean mkdir(String path) throws IOException {
        return getFile(path).mkdir();
    }

    @Override
    public boolean mkdirs(String path) throws IOException {
        return getFile(path).mkdirs();
    }

    @Override
    public InputStream readOpen(String path) throws IOException {
        return new FileInputStream(path);
    }

    @Override
    public OutputStream writeOpen(String path) throws IOException {
        return new FileOutputStream(path);
    }

    @Override
    public List<String> list(String path) throws IOException {
        File[] files=getFile(path).listFiles();
        List<String> ret=new ArrayList<>();
        for(File item : files){
            ret.add(item.getAbsolutePath());
        }
        return ret;
    }

    @Override
    public List<String> listFiles(String path) throws IOException {
        File[] files=getFile(path).listFiles();
        List<String> ret=new ArrayList<>();
        for(File item : files){
            if(item.isFile()){
                ret.add(item.getAbsolutePath());
            }
        }
        return ret;
    }

    @Override
    public List<String> listDirectories(String path) throws IOException {
        File[] files=getFile(path).listFiles();
        List<String> ret=new ArrayList<>();
        for(File item : files){
            if(item.isDirectory()){
                ret.add(item.getAbsolutePath());
            }
        }
        return ret;
    }
}
