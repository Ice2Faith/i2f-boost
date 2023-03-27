package i2f.spring.wrapper;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2023/3/17 8:47
 * @desc
 */
public class MultipartFileWrapper implements MultipartFile {
    private MultipartFile file;

    private File tmpFile;

    public MultipartFileWrapper(MultipartFile file) throws IOException {
        wrap(file);
    }

    public void wrap(MultipartFile file) throws IOException {
        this.file = file;
        this.tmpFile = File.createTempFile("multipart-file-wrapper-" + UUID.randomUUID().toString(), ".tmp");
        this.file.transferTo(this.tmpFile);
        this.tmpFile.deleteOnExit();
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        return new FileOutputStream(tmpFile);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public String getContentType() {
        return file.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return file.isEmpty();
    }

    @Override
    public long getSize() {
        return tmpFile.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        InputStream is = getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(is, bos);
        return bos.toByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(tmpFile);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        copy(getInputStream(), new FileOutputStream(file));
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
