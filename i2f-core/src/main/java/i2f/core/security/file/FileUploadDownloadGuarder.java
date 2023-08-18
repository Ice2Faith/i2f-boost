package i2f.core.security.file;

import java.io.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/8/18 11:21
 * @desc
 */
public class FileUploadDownloadGuarder {

    public static final long DEFAULT_ENC_COUNT = 16 * 1024;
    public static final Supplier<BiFunction<Byte, Long, Byte>> DEFAULT_XOR_BYTE_CONVERTER_SUPPLIER = XorFactorByteConverter::new;
    public static final FileUploadDownloadGuarder DEFAULT_INSTANCE = new FileUploadDownloadGuarder(DEFAULT_ENC_COUNT, DEFAULT_XOR_BYTE_CONVERTER_SUPPLIER, DEFAULT_XOR_BYTE_CONVERTER_SUPPLIER);
    private long encCount;
    private Supplier<BiFunction<Byte, Long, Byte>> uploadByteConverterSupplier;
    private Supplier<BiFunction<Byte, Long, Byte>> downloadByteConverterSupplier;

    public FileUploadDownloadGuarder() {
    }

    public FileUploadDownloadGuarder(long encCount, Supplier<BiFunction<Byte, Long, Byte>> uploadByteConverterSupplier, Supplier<BiFunction<Byte, Long, Byte>> downloadByteConverterSupplier) {
        this.encCount = encCount;
        this.uploadByteConverterSupplier = uploadByteConverterSupplier;
        this.downloadByteConverterSupplier = downloadByteConverterSupplier;
    }

    public boolean upload(InputStream is, File rootDir, File storeFile) throws IOException {
        return upload(is, rootDir.getAbsolutePath(), storeFile.getAbsolutePath());
    }

    public boolean upload(InputStream is, String rootPath, String storePath) throws IOException {
        boolean ok = FileAccessFirewall.isSafePath(rootPath, storePath);
        if (!ok) {
            return false;
        }
        File file = new File(rootPath, storePath);
        if (FileAccessFirewall.isAbsPath(storePath)) {
            file = new File(storePath);
        }
        FileOutputStream fos = new FileOutputStream(file);
        return uploadStream(is, fos);
    }

    public boolean uploadStream(InputStream is, OutputStream fos) throws IOException {
        BiFunction<Byte, Long, Byte> byteConverter = null;
        if (uploadByteConverterSupplier != null) {
            byteConverter = uploadByteConverterSupplier.get();
        }
        long currCount = 0;
        byte[] buf = new byte[16 * 1024];
        int len = 0;
        try {
            while ((len = is.read(buf)) > 0) {
                if (byteConverter != null && encCount > 0) {
                    for (int i = 0; i < len; i++) {
                        if (currCount < encCount) {
                            byte bt = byteConverter.apply(buf[i], currCount);
                            buf[i] = bt;
                            currCount++;
                        }
                    }
                }
                fos.write(buf, 0, len);
            }
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return true;
    }

    public boolean download(File rootDir, File storeFile, OutputStream os) throws IOException {
        return download(rootDir, storeFile, -1, -1, os);
    }

    public boolean download(String rootPath, String storePath, OutputStream os) throws IOException {
        return download(rootPath, storePath, -1, -1, os);
    }

    public boolean download(File rootDir, File storeFile, long offset, long size, OutputStream os) throws IOException {
        return download(rootDir.getAbsolutePath(), storeFile.getAbsolutePath(), offset, size, os);
    }

    public boolean download(String rootPath, String storePath, long offset, long size, OutputStream os) throws IOException {
        boolean ok = FileAccessFirewall.isSafePath(rootPath, storePath);
        if (!ok) {
            return false;
        }
        File file = new File(rootPath, storePath);
        if (FileAccessFirewall.isAbsPath(storePath)) {
            file = new File(storePath);
        }
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        return downloadStream(fis, offset, size, os);
    }

    public boolean downloadStream(InputStream fis, long offset, long size, OutputStream os) throws IOException {
        BiFunction<Byte, Long, Byte> byteConverter = null;
        if (downloadByteConverterSupplier != null) {
            byteConverter = downloadByteConverterSupplier.get();
        }
        long currCount = 0;
        long writeCount = 0;
        int bt = 0;
        try {
            if (!(fis instanceof BufferedInputStream)) {
                fis = new BufferedInputStream(fis);
            }
            while ((bt = fis.read()) >= 0) {
                if (byteConverter != null && encCount > 0) {
                    if (currCount < encCount) {
                        bt = byteConverter.apply((byte) bt, currCount);
                    }
                }
                if (offset >= 0 && currCount < offset) {
                    currCount++;
                    continue;
                }
                if (size >= 0 && writeCount >= size) {
                    break;
                }
                os.write(bt);
                currCount++;
                writeCount++;
            }
            os.flush();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return true;
    }

    public static class XorFactorByteConverter implements BiFunction<Byte, Long, Byte> {
        private int factorNumber = 31;
        private int magicNumber = 0xcaffe;
        private int state = factorNumber;

        public XorFactorByteConverter() {

        }

        public XorFactorByteConverter(int factorNumber, int magicNumber) {
            this.factorNumber = factorNumber;
            this.magicNumber = magicNumber;
            this.state = this.factorNumber;
        }

        @Override
        public Byte apply(Byte bt, Long len) {
            byte b = bt;
            long l = len;
            state = (int) ((state * factorNumber + l) ^ magicNumber);
            int ret = b ^ state;
            return (byte) ret;
        }
    }
}
