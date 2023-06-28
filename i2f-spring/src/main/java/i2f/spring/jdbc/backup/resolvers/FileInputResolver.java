package i2f.spring.jdbc.backup.resolvers;

import com.fasterxml.jackson.core.type.TypeReference;
import i2f.spring.jdbc.backup.apis.IInputResolver;
import i2f.spring.jdbc.backup.data.BasicIoMeta;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author ltb
 * @date 2022/10/4 14:08
 * @desc
 */
@Slf4j
public class FileInputResolver implements IInputResolver {
    private BasicIoMeta meta;
    private String path;
    private List<String> pattens;
    private LinkedList<File> files;
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd-HHmmss");

    @Override
    public void begin(BasicIoMeta meta) {
        this.meta = meta;
        this.path = (String) this.meta.meta.get("path");
        this.pattens = (List<String>) this.meta.meta.get("pattens");
        AntPathMatcher matcher = new AntPathMatcher();
        this.files = new LinkedList<>();
        File dir = new File(this.path);
        if (!dir.exists()) {
            return;
        }
        // 筛选每个前缀对应的最新的一次备份
        Map<String, Date> newestMap = new HashMap<>();
        Map<String, String> newestPrefixMap = new HashMap<>();
        List<File> fileList = new ArrayList<>();

        File[] arr = dir.listFiles();
        for (File item : arr) {
            String name = item.getName();
            for (String patten : this.pattens) {
                if (matcher.match(patten, name)) {
                    fileList.add(item);

                    String[] parts = name.split("\\+");
                    String prefix = parts[0];
                    String datetime = parts[1];
                    if (!newestMap.containsKey(prefix)) {
                        newestMap.put(prefix, new Date(1));
                        newestPrefixMap.put(prefix, prefix + "+" + datetime);
                    }
                    try {

                        Date date = fmt.parse(datetime);
                        Date oldDate = newestMap.get(prefix);
                        if (date.after(oldDate)) {
                            newestMap.put(prefix, date);
                            newestPrefixMap.put(prefix, prefix + "+" + datetime);
                        }
                    } catch (Exception e) {

                    }
                    break;
                }
            }
        }

        for (File item : fileList) {
            for (Map.Entry<String, String> entry : newestPrefixMap.entrySet()) {
                if (item.getName().startsWith(entry.getValue())) {
                    this.files.add(item);
                    break;
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> resolve() throws Exception {
        if (this.files.isEmpty()) {
            return null;
        }
        File file = this.files.getFirst();
        this.files.removeFirst();
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        byte[] buf = new byte[2048];
        InputStream is = new FileInputStream(file);
        if (this.meta.compress) {
            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = zis.getNextEntry();
            if ("data.x.json".equals(entry.getName())) {
                InputStream pis = zipFile.getInputStream(entry);
                int ilen = 0;
                while ((ilen = pis.read(buf)) >= 0) {
                    bos.write(buf, 0, ilen);
                }
                pis.close();
            }
        } else {
            int ilen = 0;
            while ((ilen = is.read(buf)) >= 0) {
                bos.write(buf, 0, ilen);
            }
        }
        is.close();
        bos.close();
        byte[] bytes = bos.toByteArray();
        String text = new String(bytes, "UTF-8");
        return JacksonJsonSerializer.INSTANCE.deserialize(text, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    @Override
    public void end() {

    }
}
