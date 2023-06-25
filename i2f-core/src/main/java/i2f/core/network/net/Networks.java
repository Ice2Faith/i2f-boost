package i2f.core.network.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/3/14 8:59
 * @desc
 */
public class Networks {

    public static final String NET_IP_URL = "https://www.taobao.com/help/getip.php";

    public static String getNetIp() throws IOException {

        byte[] data = httpGet(NET_IP_URL);
        // ipCallback({ip:"x.x.x.x"})
        String resp = new String(data, "UTF-8");

        int bidx = resp.indexOf("\"");
        int eidx = resp.lastIndexOf("\"");
        if (bidx >= eidx) {
            return null;
        }
        String ip = resp.substring(bidx + 1, eidx);

        return ip;
    }

    public static byte[] httpGet(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(3000);
        connection.connect();
        int code = connection.getResponseCode();
        byte[] data = new byte[0];

        if (code == 200) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len = 0;
            InputStream is = connection.getInputStream();
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            is.close();
            bos.close();
            data = bos.toByteArray();
        } else {
            String msg = connection.getResponseMessage();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len = 0;
            try {
                InputStream is = connection.getInputStream();
                while ((len = is.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                is.close();
            } catch (Exception e) {
                msg = e.getMessage();
            }
            try {
                InputStream is = connection.getErrorStream();
                while ((len = is.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                is.close();
            } catch (Exception e) {

            }
            bos.close();


            BadHttpResponseException ex = new BadHttpResponseException(String.format("HTTP %d %s", code, msg));
            ex.code = code;

            ex.msg = msg;
            ex.body = bos.toByteArray();

            Map<String, List<String>> headers = connection.getHeaderFields();
            ex.headers = headers;
            throw ex;
        }
        // 6. 断开连接，释放资源
        connection.disconnect();
        return data;
    }

    public static class BadHttpResponseException extends IOException {
        public byte[] body;
        public int code;
        public String msg;
        public Map<String, List<String>> headers;

        public BadHttpResponseException() {
        }

        public BadHttpResponseException(String message) {
            super(message);
        }

        public BadHttpResponseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static List<NetworkCard> getNetworkCards() throws IOException {
        List<NetworkCard> ret = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface element = interfaces.nextElement();
            NetworkCard item = parseNetworkCard(element);

            ret.add(item);
        }

        return ret;
    }

    public static NetworkCard parseNetworkCard(NetworkInterface element) throws IOException {
        NetworkCard item = new NetworkCard();
        item.setNetworkInterface(element);
        item.setName(element.getName());
        item.setDisplayName(element.getDisplayName());
        item.setIndex(element.getIndex());
        item.setMtu(element.getMTU());
        item.setParent(element.getParent());
        item.setLoopback(element.isLoopback());
        item.setUp(element.isUp());
        item.setVirtual(element.isVirtual());
        item.setMulticast(element.supportsMulticast());
        item.setP2p(element.isPointToPoint());


        byte[] macBytes = element.getHardwareAddress();
        if (macBytes != null) {
            StringBuilder builder = new StringBuilder();
            for (byte bt : macBytes) {
                builder.append(":");
                builder.append(String.format("%02X", (int) (bt & 0x0ff)));
            }
            String mac = builder.toString();
            if (!"".equals(mac)) {
                mac = mac.substring(1);
            }
            item.setMac(mac);
        }

        List<NetAddress> list = new ArrayList<>();

        Enumeration<InetAddress> addresses = element.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress addr = addresses.nextElement();

            NetAddress netAddress = parseNetAddress(addr);

            list.add(netAddress);
        }

        item.setAddresses(list);

        return item;
    }

    public static NetAddress parseNetAddress(InetAddress addr) throws IOException {
        NetAddress item = new NetAddress();
        item.setInetAddress(addr);
        item.setIpv4(addr instanceof Inet4Address);
        item.setAddr(addr.getHostAddress());
        item.setLoopback(addr.isLoopbackAddress());
        item.setMulticast(addr.isMulticastAddress());
        item.setReachable(addr.isReachable(3000));
        return item;
    }


}
