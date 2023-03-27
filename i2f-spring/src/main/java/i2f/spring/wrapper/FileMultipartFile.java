package i2f.spring.wrapper;

import lombok.Data;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2023/3/22 10:19
 * @desc
 */
@Data
public class FileMultipartFile implements MultipartFile {
    private String name = "file";
    private String contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
    private File file;

    public FileMultipartFile(File file) {
        this.file = file;
    }

    public FileMultipartFile(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public FileMultipartFile(String name, String contentType, File file) {
        this.name = name;
        this.contentType = contentType;
        this.file = file;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return file.exists();
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(getInputStream(), bos);
        return bos.toByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        FileOutputStream fos = new FileOutputStream(file);
        copy(getInputStream(), fos);
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[4086];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        is.close();
        os.close();
    }
}
