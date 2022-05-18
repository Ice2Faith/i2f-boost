package test.filesys;

import i2f.core.filesystem.impl.JdkFile;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/5/7 10:40
 * @desc
 */
public class TestFileSystem {
    public static void main(String[] args) throws IOException, InterruptedException {
        JdkFile file=new JdkFile();
        file.setPath("D:/01test/compress-move");

        file.parallelCopyAllTo("D:/01test/compress",true);
    }
}
