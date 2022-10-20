package i2f.core.secret.util;


import i2f.core.secret.exception.SecretException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author ltb
 * @date 2022/10/19 16:42
 * @desc
 */
public class SecretUtil {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static SecureRandom random = new SecureRandom();
    public static byte[] str2utf8(String str) {
        try {
            return str.getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {

        }
        return new byte[0];
    }

    public static String utf82str(byte[] bts) {
        try {
            return new String(bts, DEFAULT_CHARSET);
        } catch (Exception e) {

        }
        return null;
    }

    public static int randInt() {
        return random.nextInt();
    }

    public static int randInt(int max) {
        return random.nextInt(max);
    }

    public static int randInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double randDouble() {
        return random.nextDouble();
    }

    public static boolean randBool() {
        return random.nextBoolean();
    }

    public static char randAlphabet() {
        int num = randInt(26 + 26 + 10);
        if (num < 10) {
            return (char) (num + '0');
        } else if (num < 10 + 26) {
            return (char) (num - 10 + 'a');
        } else {
            return (char) (num - 10 - 26 + 'A');
        }
    }

    public static byte[] mergeByteArray(byte[]... arrs) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] item : arrs) {
            out.write(item);
        }
        out.flush();
        return out.toByteArray();
    }

    public static boolean bytesCompare(byte[] bts1, byte[] bts2) {
        if (bts1 == bts2) {
            return true;
        }
        if (bts1 == null || bts2 == null) {
            return false;
        }
        int i = 0;
        while (i < bts1.length && i < bts2.length) {
            if (bts1[i] != bts2[i]) {
                return false;
            }
            i++;
        }
        if (i < bts1.length || i < bts2.length) {
            return false;
        }
        return true;
    }

    public static byte[] messageDigest(byte[] data, String algoName) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoName);
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public static byte[] md5(byte[] data) {
        return messageDigest(data, "MD5");
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public static String toBase64(byte[] bts) {
        return Base64.getEncoder().encodeToString(bts);
    }

    public static byte[] parseBase64(String bs4) {
        return Base64.getDecoder().decode(bs4);
    }

    public static Class getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {

        }
        return null;
    }

    public static byte[] gzipZipBytes(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(baos);
        zos.write(data);
        zos.finish();
        zos.flush();
        baos.close();
        return baos.toByteArray();
    }
    public static byte[] gzipUnzipBytes(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream zis = new GZIPInputStream(bis);
        byte[] buf = new byte[2048];
        int len = 0;
        while ((len = zis.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }
        zis.close();
        bos.close();
        return bos.toByteArray();
    }
}
