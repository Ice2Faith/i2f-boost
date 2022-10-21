package i2f.core.secret.util;


import i2f.core.secret.api.key.IKeyPair;
import i2f.core.secret.data.SecretKeyPair;
import i2f.core.secret.exception.SecretException;

import java.io.*;
import java.math.BigInteger;
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

    public static int randIntMax(int max) {
        return random.nextInt(max);
    }

    public static int randIntRange(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double randDouble() {
        return random.nextDouble();
    }

    public static boolean randBool() {
        return random.nextBoolean();
    }

    public static char randAlphabet() {
        int num = randIntMax(26 + 26 + 10);
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
            return str2utf8(bytes2hex(md.digest()).toLowerCase());
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public static String bytes2hex(byte[] bts) {
        String md5code = new BigInteger(1, bts).toString(16).toLowerCase();
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
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

    public static String readText(InputStream is) throws IOException {
        return readText(is, DEFAULT_CHARSET);
    }

    public static String readText(InputStream is, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            is.close();
        }
        return sb.toString();
    }

    public static void writeText(OutputStream os, String text) throws IOException {
        writeText(os, text, DEFAULT_CHARSET);
    }

    public static void writeText(OutputStream os, String text, String charset) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(os, charset));
            writer.write(text);
            writer.flush();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
            os.close();
        }
    }

    public static void saveKeyPair(OutputStream os, IKeyPair key) throws IOException {
        String pubKey = "";
        if (key.publicKey() != null) {
            pubKey = escapeBase64(toBase64(key.publicKey()));
        }
        String priKey = "";
        if (key.privateKey() != null) {
            priKey = escapeBase64(toBase64(key.privateKey()));
        }
        String content = pubKey + "\n" + priKey;
        writeText(os, content);
    }

    public static IKeyPair loadKeyPair(InputStream is) throws IOException {
        String lines = readText(is);
        String[] arr = lines.split("\n", 2);
        String pubKey = arr[0].trim();
        String priKey = arr[1].trim();
        byte[] pubKeyBytes = null;
        if (pubKey.length() != 0) {
            pubKeyBytes = parseBase64(descapeBase64(pubKey));
        }
        byte[] priKeyBytes = null;
        if (priKey.length() != 0) {
            priKeyBytes = parseBase64(descapeBase64(priKey));
        }
        return new SecretKeyPair(pubKeyBytes, priKeyBytes);
    }

    public static String escapeBase64(String bs4) {
        if (bs4 == null) {
            return bs4;
        }
        char[] arr = bs4.toCharArray();
        int elen = 0;
        while (arr[arr.length - 1 - elen] == '=') {
            elen++;
        }
        int logicLen = arr.length - elen;
        for (int i = 0; i < logicLen / 2; i++) {
            char tmp = arr[i];
            arr[i] = arr[logicLen - 1 - i];
            arr[logicLen - 1 - i] = tmp;
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                arr[i] = (char) (arr[i] - 'A' + 'a');
            } else if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] = (char) (arr[i] - 'a' + 'A');
            }
            if (arr[logicLen - 1 - i] >= 'A' && arr[logicLen - 1 - i] <= 'Z') {
                arr[logicLen - 1 - i] = (char) (arr[logicLen - 1 - i] - 'A' + 'a');
            } else if (arr[logicLen - 1 - i] >= 'a' && arr[logicLen - 1 - i] <= 'z') {
                arr[logicLen - 1 - i] = (char) (arr[logicLen - 1 - i] - 'a' + 'A');
            }
        }
        return new String(arr);
    }

    public static String descapeBase64(String bs4) {
        if (bs4 == null) {
            return bs4;
        }
        char[] arr = bs4.toCharArray();
        int elen = 0;
        while (arr[arr.length - 1 - elen] == '=') {
            elen++;
        }
        int logicLen = arr.length - elen;
        for (int i = 0; i < logicLen / 2; i++) {
            char tmp = arr[i];
            arr[i] = arr[logicLen - 1 - i];
            arr[logicLen - 1 - i] = tmp;
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                arr[i] = (char) (arr[i] - 'A' + 'a');
            } else if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] = (char) (arr[i] - 'a' + 'A');
            }
            if (arr[logicLen - 1 - i] >= 'A' && arr[logicLen - 1 - i] <= 'Z') {
                arr[logicLen - 1 - i] = (char) (arr[logicLen - 1 - i] - 'A' + 'a');
            } else if (arr[logicLen - 1 - i] >= 'a' && arr[logicLen - 1 - i] <= 'z') {
                arr[logicLen - 1 - i] = (char) (arr[logicLen - 1 - i] - 'a' + 'A');
            }
        }
        return new String(arr);
    }
}
