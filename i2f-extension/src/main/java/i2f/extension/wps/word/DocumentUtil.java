package i2f.extension.wps.word;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/5/25 14:24
 * @desc
 */
public class DocumentUtil {
    public static String[] fontsPaths = {
            "fonts",
            "conf/fonts",
            "/usr/share/fonts/chinese"
    };
    private static AtomicBoolean hasLicence = new AtomicBoolean(false);

    public static void licence() {
        if (hasLicence.get()) {
            return;
        }
        try {
            for (String path : fontsPaths) {
                File dir = new File(path);
                if (!dir.exists()) {
                    continue;
                }
                if (dir.isDirectory()) {
                    File[] items = dir.listFiles();
                    if (items.length != 0) {
                        FontSettings.setFontsFolder(dir.getAbsolutePath(), false);
                        break;
                    }
                }
            }

            License license = new License();
            InputStream is = new ByteArrayInputStream(LICENSE_TEXT.getBytes());
            license.setLicense(is);
            hasLicence.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convert(File wordFile, int saveFormat, File outFile) throws Exception {
        licence();
        Document doc = new Document(wordFile.getAbsolutePath());
        doc.save(outFile.getAbsolutePath(), saveFormat);
    }

    public static File getTempFile(String suffix) throws IOException {
        String prefix = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        return File.createTempFile(prefix, suffix);
    }

    public static void streamCopy(InputStream is, OutputStream os) throws IOException {
        streamCopy(is, os, true, true);
    }

    public static void streamCopy(InputStream is, OutputStream os, boolean closeIs, boolean closeOs) throws IOException {
        byte[] buf = new byte[1024 * 16];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        if (closeIs) {
            is.close();
        }
        if (closeOs) {
            os.close();
        }
    }

    public static void saveAsFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        streamCopy(is, fos);
    }

    public static void convert(InputStream wordIs, int saveFormat, OutputStream outOs) throws Exception {
        File wordFile = getTempFile(".tmp");
        saveAsFile(wordIs, wordFile);

        File outFile = getTempFile(".tmp");
        convert(wordFile, saveFormat, outFile);

        FileInputStream fis = new FileInputStream(outFile);
        streamCopy(fis, outOs, true, false);

        wordFile.delete();
        outFile.delete();
    }

    public static void word2pdf(File wordFile, File outFile) throws Exception {
        convert(wordFile, SaveFormat.PDF, outFile);
    }

    public static void word2png(File wordFile, File outFile) throws Exception {
        convert(wordFile, SaveFormat.PNG, outFile);
    }

    public static void word2pdf(InputStream wordIs, OutputStream outOs) throws Exception {
        convert(wordIs, SaveFormat.PDF, outOs);
    }

    public static void word2png(InputStream wordIs, OutputStream outOs) throws Exception {
        convert(wordIs, SaveFormat.PNG, outOs);
    }

    public static final String LICENSE_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<License>\n" +
            "    <Data>\n" +
            "        <Products>\n" +
            "            <Product>Aspose.Total for Java</Product>\n" +
            "            <Product>Aspose.Words for Java</Product>\n" +
            "        </Products>\n" +
            "        <EditionType>Enterprise</EditionType>\n" +
            "        <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
            "        <LicenseExpiry>20991231</LicenseExpiry>\n" +
            "        <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
            "    </Data>\n" +
            "    <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
            "</License>";
}
