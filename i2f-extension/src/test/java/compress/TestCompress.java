package compress;

import i2f.core.compress.ICompressProvider;
import i2f.extension.compress.tar.TarCompressProvider;

import java.io.File;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/1 8:49
 * @desc
 */
public class TestCompress {

    public static void main(String[] args) throws IOException {
        ICompressProvider provider=null;
        File src=new File("D:\\01test\\compress\\src");
        File output=new File("D:\\01test\\compress\\output\\cmp.zip");
        File release=new File("D:\\01test\\compress\\release");

//        provider=new JdkZipCompressProvider();
//        testCompress(provider,src,output,release);
//
//        provider=new Zip4jCompressProvider();
//        testCompress(provider,src,output,release);

        provider=new TarCompressProvider();
        testCompress(provider,src,output,release);
    }

    public static void testCompress(ICompressProvider provider,File src, File output, File release) throws IOException {
        output.delete();
        release.delete();
        provider.compress(output,src);
        provider.release(output,release);
    }
}
