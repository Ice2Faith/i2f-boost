package i2f.extension.wps.word;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2023/5/25 14:24
 * @desc 文档转换工具，此工具仅适用于Windows环境
 * 确切的说是依赖了office的com组件
 * 而com组件是windows特有的
 * 当然，documents4j本身支持另一种方式
 * LocalConverter 是使用本机的office
 * RemoteConverter 可以使用远程主机的office，详情见github
 */
public class WindowsDocumentUtil {
    public static void convert(InputStream is, DocumentType inType,
                               OutputStream os, DocumentType outType) throws IOException {
        IConverter converter = LocalConverter.builder().build();
        converter.convert(is)
                .as(inType)
                .to(os)
                .as(outType).execute();
        converter.shutDown();
        is.close();
        os.close();
    }

    public static void convert(File inFIle, DocumentType inType,
                               File outFile, DocumentType outType) throws IOException {
        InputStream is = new FileInputStream(inFIle);
        OutputStream os = new FileOutputStream(outFile);
        convert(is, inType, os, outType);
    }

    public static void word2pdf(InputStream wordIs, OutputStream pdfOs) throws IOException {
        convert(wordIs, DocumentType.DOCX, pdfOs, DocumentType.PDF);
    }

    public static void word2pdf(File wordFile, File pdfFile) throws IOException {
        convert(wordFile, DocumentType.DOCX, pdfFile, DocumentType.PDF);
    }
}
