package i2f.packet.test;

import i2f.packet.rule.StreamPacketResolver;
import i2f.packet.data.StreamPacket;
import i2f.packet.io.LocalOutputStreamInputAdapter;
import i2f.packet.rule.PacketRule;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2024/3/8 16:46
 * @desc
 */
public class TestStreamPacket {

    public static void main(String[] args) throws Exception {
        testBasic();
        testStream();

    }

    public static void testStream() throws Exception {
        File outDir=new File("./test");
        if(!outDir.exists()){
            outDir.mkdirs();
        }

        Runtime.getRuntime().exec("explorer \""+outDir.getAbsolutePath()+"\"");

        File outFile=new File(outDir,"test.spkt");
        OutputStream os=new FileOutputStream(outFile);

        StreamPacketResolver.write(StreamPacket.of("这是报文头",
                "第一块数据",
                "the second data",
                "第 third 块 data"),os);

        StreamPacketResolver.write(StreamPacket.of(new byte[]{0x00,0x01,0x02,0x03},
                new byte[]{0x10,0x11,0x12,0x13},
                new byte[]{(byte)0xef,(byte)0xee,(byte)0xed,(byte)0xec,(byte)0xeb,(byte)0xea},
                new byte[]{0x20,0x45,0x46,0x47,0x48}),os);

        os.close();


        InputStream is = new FileInputStream(outFile);
        while(true){
            try{
                StreamPacket read = StreamPacketResolver.read( is);
                if(read==null){
                    System.out.println("none more");
                    break;
                }

                System.out.println("------------------------------");
                if(!read.isEqualsTail()){
                    System.out.println("check fail!");
                }else{
                    System.out.println("check ok.");
                }
                byte[][] head = read.getHead();
                if(head!=null){
                    for (byte[] hd : head) {
                        try{
                            System.out.println("head:"+StreamPacket.ofCharset(hd,"UTF-8"));
                        }catch(Exception e){

                        }
                        System.out.println("head:"+printByte(hd));
                    }
                }
                for (InputStream item : read.getBody()) {
                    ByteArrayOutputStream tos=new ByteArrayOutputStream();
                    LocalOutputStreamInputAdapter.copy(item,tos);
                    item.close();
                    try{
                        System.out.println("body:"+StreamPacket.ofCharset(tos.toByteArray(),"UTF-8"));
                    }catch(Exception e){

                    }
                    System.out.println("body:"+printByte(tos.toByteArray()));
                }
                System.out.println("tail:"+printByte(read.getTail()));
                System.out.println("rule:"+printByte(read.getRuleTail()));
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
        is.close();
        System.out.println("ok");
    }

    public static void testBasic() throws Exception {
        PacketRule<Long> rule = PacketRule.defaultHashRule();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] headData = {(byte)0xea, (byte)0xeb, 0x02, 0x03, 0x00};
        byte[] bodyData = {0x10,  (byte) 0xef, (byte) 0xee, (byte)0xea, (byte)0xeb};
        byte[][] head = new byte[][]{headData,null,headData};

        InputStream[] body=new InputStream[]{
                new ByteArrayInputStream(bodyData),
                null,
                new ByteArrayInputStream(bodyData)
        };
        StreamPacket src = new StreamPacket(head, body);
        // 在包之前添加一些杂乱内容，测试包辨别能力
        bos.write("xxx".getBytes());
        StreamPacketResolver.write(rule, src, bos);

        head=new byte[0][];
        body = new InputStream[0];
        src = new StreamPacket(head, body);
        // 在包之前添加一些杂乱内容，测试包辨别能力
        bos.write("aaa".getBytes());
        StreamPacketResolver.write(rule, src, bos);


        byte[] recv = bos.toByteArray();
        System.out.println("all:"+printByte(recv));

        ByteArrayInputStream bis = new ByteArrayInputStream(recv);
        while(true){
            try{
                StreamPacket read = StreamPacketResolver.read(rule, bis);
                if(read==null){
                    System.out.println("none more");
                    break;
                }

                System.out.println("------------------------------");
                if(!read.isEqualsTail()){
                    System.out.println("check fail!");
                }else{
                    System.out.println("check ok.");
                }
                byte[][] rhd = read.getHead();
                if(rhd!=null){
                    for (byte[] hd : rhd) {
                        System.out.println("head:"+printByte(hd));
                    }
                }
                for (InputStream item : read.getBody()) {
                    ByteArrayOutputStream tos=new ByteArrayOutputStream();
                    LocalOutputStreamInputAdapter.copy(item,tos);
                    item.close();
                    System.out.println("body:"+printByte(tos.toByteArray()));
                }
                System.out.println("tail:"+printByte(read.getTail()));
                System.out.println("rule:"+printByte(read.getRuleTail()));
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
        System.out.println("ok");
    }


    public static String printByte(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte bt : bytes) {

            builder.append(String.format("[0x%02x]", (int) (bt & 0x0ff)));

        }
        return builder.toString();
    }
}
