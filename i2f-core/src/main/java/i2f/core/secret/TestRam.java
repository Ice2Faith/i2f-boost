package i2f.core.secret;

import i2f.core.secret.core.SecretProvider;
import i2f.core.secret.data.Base64SecretMsg;
import i2f.core.secret.data.SecretMsg;
import i2f.core.secret.impl.ram.RamSecretProvider;
import i2f.core.secret.util.SecretUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/10/19 18:15
 * @desc
 */
public class TestRam {
    public static void main(String[] args) throws Exception {
        SecretProvider sender = RamSecretProvider.getInstance();

        SecretProvider recver = RamSecretProvider.getInstance();
        String rsaKeyPath = "../rsa.key";
        File file = new File(rsaKeyPath);
        try {
            if (file.exists()) {
                InputStream is = new FileInputStream(file);
                recver.mineKey = SecretUtil.loadKeyPair(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String msg = "{\n" +
                "  \"username\": \"aaa\",\n" +
                "  \"password\": \"123\"\n" +
                "}";

        msg = msg + msg + msg + msg;
        byte[] data = SecretUtil.str2utf8(msg);

        byte[] zdata = SecretUtil.gzipZipBytes(data);

        byte[] ldata = SecretUtil.gzipUnzipBytes(zdata);

        boolean eq = SecretUtil.bytesCompare(data, ldata);

        SecretMsg send = sender.send(data, recver.mineKey);

        Base64SecretMsg sendWeb = send.convert();
        System.out.println(sendWeb);

        SecretMsg recvWeb = sendWeb.convert();
        System.out.println(recvWeb);

        byte[] rdata = recver.recv(recvWeb);

        String str = SecretUtil.utf82str(rdata);

        System.out.println(str);
    }
}
