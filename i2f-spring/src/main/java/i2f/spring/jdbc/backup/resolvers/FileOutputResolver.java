package i2f.spring.jdbc.backup.resolvers;


import i2f.core.database.jdbc.data.PageMeta;
import i2f.spring.jackson.JacksonSerializer;
import i2f.spring.jdbc.backup.apis.IOutputResolver;
import i2f.spring.jdbc.backup.data.BasicIoMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ltb
 * @date 2022/10/4 9:43
 * @desc
 */
public class FileOutputResolver implements IOutputResolver {
    public BasicIoMeta meta;
    public String path;
    public String prefix;
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd-HHmmss");
    private SimpleDateFormat fmtDay = new SimpleDateFormat("yyyy-MM-dd");
    private String dayFlag;
    private String timeFlag;
    private Integer expireDays;

    @Override
    public void begin(BasicIoMeta meta) {
        this.meta = meta;
        this.path = (String) this.meta.meta.get("path");
        this.prefix = (String) this.meta.meta.get("prefix");
        this.timeFlag = fmt.format(new Date());
        this.dayFlag = fmtDay.format(new Date());
        this.expireDays = (Integer) this.meta.meta.get("expireDays");
    }

    public void removeExpireDaysFiles() {
        if (this.expireDays == null || this.expireDays < 0) {
            return;
        }
        File dir = new File(this.path);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        Date expireDate = new Date(0L);
        synchronized (Calendar.class) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DAY_OF_MONTH, -expireDays);
            expireDate = cl.getTime();
        }
        for (File item : files) {
            String name = item.getName();
            if (!name.startsWith(this.prefix + "+")) {
                continue;
            }
            String[] arr = name.split("\\+");
            String dateStr = arr[1];
            try {
                Date date = fmt.parse(dateStr);
                if (date.before(expireDate)) {
                    item.delete();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void resolve(List<Map<String, Object>> list, PageMeta page) throws Exception {
        long offset = page == null ? 0 : page.offset;
        long end = page == null ? list.size() : page.offsetEnd;
        File file = new File(this.path + "/" + this.prefix + "+" + timeFlag + "+" + offset + "+" + end + ".x.json");
        if (meta.compress != null && meta.compress) {
            file = new File(file.getAbsolutePath() + ".zip");
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream os = new FileOutputStream(file);
        String text = JacksonSerializer.toJson(list);
        if (meta.compress != null && meta.compress) {
            ZipOutputStream zos = new ZipOutputStream(os);
            os = zos;
            zos.putNextEntry(new ZipEntry("data.x.json"));
            zos.write(text.getBytes("UTF-8"));
        } else {
            os.write(text.getBytes("UTF-8"));
        }

        os.flush();
        os.close();
    }

    @Override
    public void end() {
        removeExpireDaysFiles();
    }
}
