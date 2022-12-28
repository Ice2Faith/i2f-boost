package i2f.extension.filesystem.minio;

import i2f.core.filesystem.IFile;
import i2f.core.filesystem.abs.AbsFileSystem;
import i2f.core.filesystem.abs.FileSystemUtil;
import i2f.core.tuple.impl.Tuple2;
import io.minio.*;
import io.minio.messages.Item;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MinioFileSystem extends AbsFileSystem {
    private MinioMeta meta;
    private MinioClient client;

    public MinioFileSystem(MinioMeta meta) {
        this.meta = meta;
        getClient();
    }

    public MinioClient getClient() {
        if (this.client == null) {
            this.client = MinioClient.builder()
                    .endpoint(meta.getUrl())
                    .credentials(meta.getAccessKey(), meta.getSecretKey())
                    .build();
        }
        return client;
    }

    @Override
    public String pathSeparator() {
        return super.pathSeparator();
    }

    public Tuple2<String, String> parseMinioPath(String path) {
        String bucketName = null;
        String objectName = null;
        if (path == null) {
            path = "";
        }
        if (path.startsWith(pathSeparator())) {
            path = path.substring(pathSeparator().length());
        }

        int idx = path.indexOf(pathSeparator());
        if (idx >= 0) {
            bucketName = path.substring(0, idx);
            objectName = path.substring(idx + 1);
        } else {
            bucketName = path;
        }

        return new Tuple2<>(bucketName, objectName);
    }

    public String minioPath(String path) {
        if (path == null) {
            return null;
        }
        if (path.endsWith(pathSeparator())) {
            return path;
        }
        return path + pathSeparator();
    }

    @Override
    public IFile getFile(String path) {
        return new MinioFile(this, path);
    }

    @Override
    public String getAbsolutePath(String path) {
        return path;
    }

    @Override
    public boolean isDirectory(String path) {
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        Tuple2<String, String> pair = parseMinioPath(path);
        if (pair.t2 == null) {
            try {
                return getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.t1).build());
            } catch (Throwable e) {

            }
        } else {
            Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                    .bucket(pair.t1)
                    .prefix(pair.t2)
                    .recursive(false)
                    .build()
            );
            for (Result<Item> res : iter) {
                try {
                    Item item = res.get();
                    if (item.isDir()) {
                        String name = item.objectName();
                        if (name.endsWith(pathSeparator())) {
                            name = name.substring(0, name.length() - pathSeparator().length());
                        }
                        if (name.equals(pair.t2)) {
                            return true;
                        }
                    }
                } catch (Throwable e) {
                }
            }
        }

        return false;
    }

    @Override
    public boolean isFile(String path) {
        Tuple2<String, String> pair = parseMinioPath(path);
        if (pair.t2 == null) {
            return false;
        }
        try {
            ObjectStat stat = getClient().statObject(StatObjectArgs.builder()
                    .bucket(pair.t1)
                    .object(pair.t2)
                    .build()
            );
            return true;
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public boolean isExists(String path) {
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        Tuple2<String, String> pair = parseMinioPath(path);
        if (pair.t2 == null) {
            try {
                return getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.t1).build());
            } catch (Throwable e) {

            }
        } else {
            Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                    .bucket(pair.t1)
                    .prefix(pair.t2)
                    .recursive(false)
                    .build()
            );
            for (Result<Item> res : iter) {
                try {
                    Item item = res.get();
                    String name = item.objectName();
                    if (name.endsWith(pathSeparator())) {
                        name = name.substring(0, name.length() - pathSeparator().length());
                    }
                    if (name.equals(pair.t2)) {
                        return true;
                    }
                } catch (Throwable e) {
                }
            }
        }

        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        Tuple2<String, String> pair = parseMinioPath(path);
        Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                .bucket(pair.t1)
                .prefix(minioPath(pair.t2))
                .recursive(false)
                .build()
        );
        List<IFile> ret = new LinkedList<>();
        for (Result<Item> res : iter) {
            try {
                Item item = res.get();
                String name = item.objectName();
                ret.add(getFile(pair.t1, name));
            } catch (Throwable e) {
            }
        }
        return ret;
    }

    @Override
    public void delete(String path) {
        Tuple2<String, String> pair = parseMinioPath(path);
        if (isFile(path)) {
            try {
                getClient().removeObject(RemoveObjectArgs.builder()
                        .bucket(pair.t1)
                        .object(pair.t2)
                        .build());
            } catch (Throwable e) {
                throw new IllegalStateException("delete failure:" + e.getMessage(), e);
            }
        } else {
            if (pair.t1 != null && !"".equals(pair.t1)) {
                if (pair.t2 == null || "".equals(pair.t2)) {
                    try {
                        getClient().removeBucket(RemoveBucketArgs.builder()
                                .bucket(pair.t1)
                                .build());
                    } catch (Exception e) {
                        throw new IllegalStateException("delete failure:" + e.getMessage(), e);
                    }
                }
            }
        }

    }

    @Override
    public boolean isAppendable(String path) {
        return false;
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        Tuple2<String, String> pair = parseMinioPath(path);
        try {
            return getClient().getObject(GetObjectArgs.builder()
                    .bucket(pair.t1)
                    .object(pair.t2)
                    .build());
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        Tuple2<String, String> pair = parseMinioPath(path);
        File tmpFile = File.createTempFile("minio-" + UUID.randomUUID().toString(), ".tmp");
        FileOutputStream fos = new FileOutputStream(tmpFile);
        return new FilterOutputStream(fos) {
            @Override
            public void close() throws IOException {
                super.close();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tmpFile);
                    getClient().putObject(PutObjectArgs.builder()
                            .bucket(pair.t1)
                            .object(pair.t2)
                            .stream(fis, tmpFile.length(), -1)
                            .build());
                } catch (Throwable e) {
                    throw new IOException(e.getMessage(), e);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                    tmpFile.delete();
                }
            }
        };
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        throw new UnsupportedOperationException("minio not support appendable stream");
    }

    @Override
    public void mkdir(String path) {
        Tuple2<String, String> pair = parseMinioPath(path);
        try {
            boolean exBkt = getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.t1).build());
            if (!exBkt) {
                getClient().makeBucket(MakeBucketArgs.builder().bucket(pair.t1).build());
            }
            if (pair.t1 != null) {
                String holdName = combinePath(pair.t2, ".ignore");
                getClient().putObject(PutObjectArgs.builder()
                        .bucket(pair.t1)
                        .object(holdName)
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build());
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void store(String path, InputStream is) throws IOException {
        Tuple2<String, String> pair = parseMinioPath(path);
        try {
            getClient().putObject(PutObjectArgs.builder()
                    .bucket(pair.t1)
                    .object(pair.t2)
                    .stream(is, -1, -1)
                    .build());
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public void load(String path, OutputStream os) throws IOException {
        Tuple2<String, String> pair = parseMinioPath(path);
        InputStream is = null;
        try {
            is = getClient().getObject(GetObjectArgs.builder()
                    .bucket(pair.t1)
                    .object(pair.t2)
                    .build());
            FileSystemUtil.streamCopy(is, os);
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    public long length(String path) {
        Tuple2<String, String> pair = parseMinioPath(path);
        try {
            ObjectStat stat = getClient().statObject(StatObjectArgs.builder()
                    .bucket(pair.t1)
                    .object(pair.t2)
                    .build()
            );
            return stat.length();
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
