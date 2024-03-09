package i2f.packet.data;

import java.io.*;
import java.util.Arrays;


/**
 * @author Ice2Faith
 * @date 2024/3/8 8:49
 * @desc 封包
 * 提供一个具有多个head+多个body分开封包的能力
 * 以覆盖大多数的场景，
 * 例如
 * 1.使用head区分body的类型，比如：文件、文本、图片等等
 * 2.需要使用多个head，类比HTTP，例如：URL，时间戳，请求方法，文件名等等
 * 3.需要使用多个body，例如：上传多个文件，多个消息载荷等等
 * ------------------------------------------------
 * 包构成
 * lhead+head ... head+lbody+body...body+tail
 * lhead表示head的个数
 * 0-n个head,n的最大值为127，即一个字节整形
 * lbody表示body的个数
 * 0-n个body,n的最大值为127，即一个字节整形
 * 可以有一个后置的tail，可以用于在传输时，最后发送一些数据
 * 比如校验位
 * tail默认根据rule来聚合得到，聚合的对象是head+body
 * 因此，常见的tail就是计算head+body的校验值
 * 用以确认包的完整性
 */
public class StreamPacket {

    private byte[][] head;
    private InputStream[] body;
    private byte[] tail;
    private byte[] ruleTail;

    public static<T> T[] array(T ... arr){
        return arr;
    }

    public static StreamPacket of(byte[] head,InputStream ... body){
        return new StreamPacket(array(head),body);
    }

    public static StreamPacket of(String head,String ... body){
        InputStream[] arr=new InputStream[body.length];
        for (int i = 0; i < body.length; i++) {
            arr[i]=new ByteArrayInputStream(toCharset(body[i],"UTF-8"));
        }
        return new StreamPacket(array(toCharset(head,"UTF-8")),arr);
    }

    public static StreamPacket of(byte[] head,byte[] ... body){
        InputStream[] arr=new InputStream[body.length];
        for (int i = 0; i < body.length; i++) {
            arr[i]=new ByteArrayInputStream(body[i]);
        }
        return new StreamPacket(array(head),arr);
    }

    public static byte[] toCharset(String str,String charset){
        try{
            return str.getBytes(charset);
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    public static String ofCharset(byte[] data,String charset){
        try{
            return new String(data,charset);
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }


    public StreamPacket() {
    }


    public StreamPacket(byte[][] head, InputStream[] body) {
        this.head = head;
        this.body = body;
    }


    public boolean isEqualsTail(){
        if(tail==ruleTail){
            return true;
        }
        if(tail==null || ruleTail==null){
            return false;
        }
        if(tail.length!=ruleTail.length){
            return false;
        }
        for (int i = 0; i < tail.length; i++) {
            if(tail[i]!=ruleTail[i]){
                return false;
            }
        }
        return true;
    }

    public byte[][] getHead() {
        return head;
    }

    public void setHead(byte[][] head) {
        this.head = head;
    }

    public InputStream[] getBody() {
        return body;
    }

    public void setBody(InputStream[] body) {
        this.body = body;
    }

    public byte[] getTail() {
        return tail;
    }

    public void setTail(byte[] tail) {
        this.tail = tail;
    }

    public byte[] getRuleTail() {
        return ruleTail;
    }

    public void setRuleTail(byte[] ruleTail) {
        this.ruleTail = ruleTail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StreamPacket that = (StreamPacket) o;
        return Arrays.equals(head, that.head) &&
                Arrays.equals(body, that.body) &&
                Arrays.equals(tail, that.tail) &&
                Arrays.equals(ruleTail, that.ruleTail);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(head);
        result = 31 * result + Arrays.hashCode(body);
        result = 31 * result + Arrays.hashCode(tail);
        result = 31 * result + Arrays.hashCode(ruleTail);
        return result;
    }

    @Override
    public String toString() {
        return "StreamPacket{" +
                "head=" + Arrays.toString(head) +
                ", body=" + Arrays.toString(body) +
                ", tail=" + Arrays.toString(tail) +
                ", ruleTail=" + Arrays.toString(ruleTail) +
                '}';
    }
}
