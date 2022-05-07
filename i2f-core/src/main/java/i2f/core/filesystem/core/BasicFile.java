package i2f.core.filesystem.core;

import i2f.core.filesystem.IFile;
import i2f.core.filesystem.IFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/7 9:52
 * @desc
 */
public class BasicFile implements IFile {
    protected IFileSystem fileSystem;
    protected String path;
    public BasicFile(IFileSystem fileSystem){
        this.fileSystem=fileSystem;
    }
    public BasicFile(IFileSystem fileSystem, String path){
        this.fileSystem=fileSystem;
        this.path=path;
    }

    public BasicFile setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public IFile setFileSystem(IFileSystem fileSystem) {
        this.fileSystem=fileSystem;
        return this;
    }

    @Override
    public IFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public IFile ofPath(String path) {
        return new BasicFile(getFileSystem(),path);
    }

    @Override
    public String getName() {
        return getFileSystem().getName(path);
    }

    @Override
    public String getExtension() {
        return getFileSystem().getExtension(path);
    }

    @Override
    public String getPath() {
        return getFileSystem().getPath(path);
    }

    @Override
    public String getParentPath() {
        return getFileSystem().getParentPath(path);
    }

    @Override
    public String getPathOperator() {
        return getFileSystem().getPathOperator();
    }

    @Override
    public String getExtensionOperator() {
        return getFileSystem().getExtensionOperator();
    }

    @Override
    public String getAbsolutePath() throws IOException {
        return getFileSystem().getAbsolutePath(path);
    }

    @Override
    public boolean isFile() throws IOException {
        return getFileSystem().isFile(path);
    }

    @Override
    public boolean isDirectory() throws IOException {
        return getFileSystem().isDirectory(path);
    }

    @Override
    public boolean create() throws IOException {
        return getFileSystem().create(path);
    }

    @Override
    public boolean delete() throws IOException {
        return getFileSystem().delete(path);
    }

    @Override
    public boolean deleteAll() throws IOException {
        IFileSystem fileSystem=getFileSystem();
        if(!fileSystem.exists(path)){
            return true;
        }
        if(fileSystem.isFile(path)){
            return fileSystem.delete(path);
        }
        if(fileSystem.isDirectory(path)){
            List<IFile> dirs=listDirectories();
            for(IFile item : dirs){
                item.deleteAll();
            }
            List<IFile> files=listFiles();
            for(IFile item : files){
                item.deleteAll();
            }
            return fileSystem.delete(path);
        }
        return false;
    }

    @Override
    public boolean exists() throws IOException {
        return getFileSystem().exists(path);
    }

    @Override
    public boolean renameTo(String dstPath, boolean overwrite) throws IOException {
        return getFileSystem().renameTo(path,dstPath,overwrite);
    }

    @Override
    public boolean renameTo(IFile dstFile, boolean overwrite) throws IOException {
        return getFileSystem().renameTo(path, dstFile.getPath(), overwrite);
    }

    @Override
    public boolean mkdir() throws IOException {
        return getFileSystem().mkdir(path);
    }

    @Override
    public boolean mkdirs() throws IOException {
        return getFileSystem().mkdirs(path);
    }

    @Override
    public boolean copyTo(String dstPath, boolean overwrite) throws IOException {
        return getFileSystem().copyTo(path,dstPath,overwrite);
    }

    @Override
    public boolean moveTo(String dstPath, boolean overwrite) throws IOException {
        return getFileSystem().moveTo(path,dstPath,overwrite);
    }

    @Override
    public boolean copyAllTo(String dstPath, boolean overwrite) throws IOException {
        IFileSystem fileSystem=getFileSystem();
        if(!fileSystem.exists(path)){
            return true;
        }
        if(fileSystem.isFile(path)){
            return fileSystem.copyTo(path,dstPath,overwrite);
        }
        if(fileSystem.isDirectory(path)){
            dstPath=dstPath+fileSystem.getPathOperator()+fileSystem.getName(path);
            if(!fileSystem.exists(dstPath)){
                fileSystem.mkdirs(dstPath);
            }
            List<IFile> dirs=listDirectories();
            for(IFile item : dirs){
                item.copyAllTo(dstPath, overwrite);
            }
            List<IFile> files=listFiles();
            for(IFile item : files){
                dstPath=dstPath+fileSystem.getPathOperator()+item.getName();
                item.copyAllTo(dstPath,overwrite);
            }
        }
        return true;
    }

    @Override
    public boolean moveAllTo(String dstPath, boolean overwrite) throws IOException {
        IFileSystem fileSystem=getFileSystem();
        if(!fileSystem.exists(path)){
            return true;
        }
        if(fileSystem.isFile(path)){
            return fileSystem.moveTo(path,dstPath,overwrite);
        }
        if(fileSystem.isDirectory(path)){
            dstPath=dstPath+fileSystem.getPathOperator()+fileSystem.getName(path);
            if(!fileSystem.exists(dstPath)){
                fileSystem.mkdirs(dstPath);
            }
            List<IFile> dirs=listDirectories();
            for(IFile item : dirs){
                item.moveAllTo(dstPath, overwrite);
            }
            List<IFile> files=listFiles();
            for(IFile item : files){
                dstPath=dstPath+fileSystem.getPathOperator()+item.getName();
                item.moveAllTo(dstPath,overwrite);
            }
            fileSystem.delete(path);
        }
        return true;
    }

    @Override
    public boolean copyTo(IFile dstFile, boolean overwrite) throws IOException {
        return getFileSystem().copyTo(path, dstFile.getPath(), overwrite);
    }

    @Override
    public boolean moveTo(IFile dstFile, boolean overwrite) throws IOException {
        return getFileSystem().moveTo(path, dstFile.getPath(), overwrite);
    }

    @Override
    public InputStream readOpen() throws IOException {
        return getFileSystem().readOpen(path);
    }

    @Override
    public OutputStream writeOpen() throws IOException {
        return getFileSystem().writeOpen(path);
    }

    @Override
    public List<IFile> list() throws IOException {
        List<String> list=getFileSystem().list(path);
        List<IFile> ret=new ArrayList<>();
        for(String item : list){
            ret.add(ofPath(item));
        }
        return ret;
    }

    @Override
    public List<IFile> listFiles() throws IOException {
        List<String> list=getFileSystem().listFiles(path);
        List<IFile> ret=new ArrayList<>();
        for(String item : list){
            ret.add(ofPath(item));
        }
        return ret;
    }

    @Override
    public List<IFile> listDirectories() throws IOException {
        List<String> list=getFileSystem().listDirectories(path);
        List<IFile> ret=new ArrayList<>();
        for(String item : list){
            ret.add(ofPath(item));
        }
        return ret;
    }
}
