package i2f.packet.protocol;


import i2f.packet.data.StreamPacket;

import java.io.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/11 10:08
 * @desc 定义协议
 * 参考HTTP协议
 * 这里给出对照
 * head格式：
 * ${name}:${value}
 * name和value都是文本
 * 其中内置的head：
 * action 表示 url, 因为封包，所以主机和端口没什么必要，退化为请求路径，或者要触发的动作 action
 * x-body-${bodyIndex}-${bodyHeadName} 用于表示索引为bodyIndex的请求体的附加请求头bodyHeadName，比如用来表示文件名，字段名，类型等
 * name 表示一个名称，用于表示文件名，字段名等的名称
 * content-type 内容类型，用以表示请求体的类型
 * -------------------------------------------
 * 注意，由于底层的StreamPacket的请求头限制了最大个数为 128
 * 因此，合理使用附加请求头 x-body-
 * 如有必要，使用一个body来保存复合数据类型，比如 JSON/xml等结构化数据
 * 在默认的情况下，如果每个body都使用拓展头 name 和 content-type
 * 则必要的请求头可能包含 action，token，timestamp，messageId
 * (如果要实现响应指定的消息，实现HTTP过程，则需要一个messageId进行确定消息ID)
 * 则，最多可以有 (128-4)/2=62 个body
 * 换句话说，可以有62个字段，或者62个文件
 * 这个数量，大多数情况下，基本都是满足需求的
 * -------------------------------------------
 * 示例头参考：
 * action: /sys/upload
 * token: xxx
 * timestamp: xxx
 * x-body-0-name: username
 * x-body-0-content-type: text
 * x-body-1-name: test.txt
 * x-body-1-content-type: file
 * x-body-1-name: test.pdf
 * x-body-1-content-type: file
 * 含义解析：
 * 这个请求，表示上传了两个文件，并携带了一个username的参数，交由 /sys/upload 进行处理
 * action: /sys/upload
 * 说明了触发的动作为 /sys/upload
 * token: xxx
 * timestamp: xxx
 * 这两个是一般的请求头
 * x-body-0-name: username
 * x-body-0-content-type: text
 * 说明了索引为0的请求体是一个text文本类型的字段名为username的字段值
 * x-body-1-name: test.txt
 * x-body-1-content-type: file
 * 说明了索引为1的请求体是一个file文件类型的名为test.txt的文件
 */
public class PacketProtocol {
    public static final String HEAD_ACTION = "action";

    public static final String HEAD_CONTENT_TYPE = "content-type";
    public static final String HEAD_NAME = "name";

    public static final String BODY_HEAD_PREFIX = "x-body-";

    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_NUMBER = "number";
    public static final String CONTENT_TYPE_JSON = "json";
    public static final String CONTENT_TYPE_XML = "xml";
    public static final String CONTENT_TYPE_FILE = "file";
    public static final String CONTENT_TYPE_BINARY = "binary";


    public static String makeBodyHeadName(String name, int index) {
        return BODY_HEAD_PREFIX + index + "-" + name;
    }


    private Map<String, String> head;
    private List<InputStream> body;

    public PacketProtocol() {
        this.head = new LinkedHashMap<>();
        this.body = new ArrayList<>();
    }

    public PacketProtocol(Map<String, String> head, List<InputStream> body) {
        this.head = head;
        this.body = body;
    }

    public static PacketProtocol begin() {
        return new PacketProtocol();
    }

    public static PacketProtocol begin(String action) {
        return new PacketProtocol()
                .action(action);
    }

    public PacketProtocol action(String action) {
        this.head.put(HEAD_ACTION, action);
        return this;
    }

    public PacketProtocol addHead(String name, String value) {
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("head name cannot be empty");
        }
        this.head.put(name, value);
        return this;
    }

    public PacketProtocol addBody(String name, String contentType, InputStream is) {
        Map<String, String> head = new LinkedHashMap<>();
        head.put(HEAD_NAME, name);
        head.put(HEAD_CONTENT_TYPE, contentType);
        return addBody(head, is);
    }

    public PacketProtocol addBody(File file) throws IOException {
        return addBody(file.getName(), CONTENT_TYPE_FILE, new FileInputStream(file));
    }

    public PacketProtocol addText(String str) {
        return addBody(null, CONTENT_TYPE_TEXT, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addText(String name, String str) {
        return addBody(name, CONTENT_TYPE_TEXT, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addJson(String str) {
        return addBody(null, CONTENT_TYPE_JSON, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addJson(String name, String str) {
        return addBody(name, CONTENT_TYPE_JSON, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addXml(String str) {
        return addBody(null, CONTENT_TYPE_XML, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addXml(String name, String str) {
        return addBody(name, CONTENT_TYPE_XML, new ByteArrayInputStream(StreamPacket.toCharset(str, "UTF-8")));
    }

    public PacketProtocol addBinary(byte[] data) {
        return addBody(null, CONTENT_TYPE_BINARY, new ByteArrayInputStream(data));
    }

    public PacketProtocol addBinary(String name, byte[] data) {
        return addBody(name, CONTENT_TYPE_BINARY, new ByteArrayInputStream(data));
    }

    public PacketProtocol addFile(String name, InputStream is) {
        Map<String, String> head = new LinkedHashMap<>();
        head.put(HEAD_NAME, name);
        head.put(HEAD_CONTENT_TYPE, CONTENT_TYPE_FILE);
        return addBody(head, is);
    }

    public PacketProtocol addBody(InputStream is) {
        return addBody(null, is);
    }

    public PacketProtocol addBody(Map<String, String> bodyHead, InputStream is) {
        if (bodyHead != null) {
            int size = this.body.size();
            for (Map.Entry<String, String> entry : bodyHead.entrySet()) {
                String name = entry.getKey();
                String bodyHeadName = makeBodyHeadName(name, size);
                this.head.put(bodyHeadName, entry.getValue());
            }
        }
        this.body.add(is);
        return this;
    }

    public String getHead(String name) {
        return this.head.get(name);
    }

    public Map<String, String> getBodyHead(int index) {
        String indexStr = "" + index;
        Map<String, String> ret = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : this.head.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(BODY_HEAD_PREFIX)) {
                continue;
            }
            key = key.substring(BODY_HEAD_PREFIX.length());
            String[] arr = key.split("-", 2);
            if (indexStr.equals(arr[0])) {
                ret.put(arr[1], entry.getValue());
            }
        }
        return ret;
    }


    public StreamPacket toPacket() {
        return toPacket(this);
    }

    public static PacketProtocol ofPacket(StreamPacket packet) {
        PacketProtocol ret = PacketProtocol.begin();
        byte[][] packHead = packet.getHead();
        for (byte[] item : packHead) {
            String line = StreamPacket.ofCharset(item, "UTF-8");
            String[] arr = line.split(":", 2);
            if (arr.length != 2) {
                continue;
            }
            ret.addHead(arr[0], arr[1]);
        }
        InputStream[] packBody = packet.getBody();
        for (InputStream item : packBody) {
            ret.addBody(item);
        }
        return ret;
    }

    public static StreamPacket toPacket(PacketProtocol protocol) {
        byte[][] packHead = new byte[protocol.head.size()][];
        InputStream[] packBody = new InputStream[protocol.body.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : protocol.head.entrySet()) {
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            packHead[i] = StreamPacket.toCharset(entry.getKey() + ":" + value, "UTF-8");
            i++;
        }
        i = 0;
        for (InputStream is : protocol.body) {
            packBody[i] = is;
            i++;
        }
        return new StreamPacket(packHead, packBody);
    }

    public Map<String, String> getHead() {
        return head;
    }

    public void setHead(Map<String, String> head) {
        this.head = head;
    }

    public List<InputStream> getBody() {
        return body;
    }

    public void setBody(List<InputStream> body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PacketProtocol protocol = (PacketProtocol) o;
        return Objects.equals(head, protocol.head) &&
                Objects.equals(body, protocol.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, body);
    }

    @Override
    public String toString() {
        return "PacketProtocol{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
