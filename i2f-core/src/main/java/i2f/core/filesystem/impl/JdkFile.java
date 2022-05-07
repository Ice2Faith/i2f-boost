package i2f.core.filesystem.impl;

import i2f.core.filesystem.core.BasicFile;

/**
 * @author ltb
 * @date 2022/5/7 10:31
 * @desc
 */
public class JdkFile extends BasicFile {
    public JdkFile(){
        super(new JdkFileSystem());
    }
    public JdkFile(String path){
        super(new JdkFileSystem(),path);
    }
}
