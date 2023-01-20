package i2f.extension.netty.tcp.protocol.codec;

import i2f.extension.netty.tcp.protocol.consts.NettyConst;
import i2f.extension.netty.tcp.protocol.consts.NettyPkgType;
import i2f.extension.netty.tcp.protocol.pkg.NettyMessage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 协议包
 * 编解码器
 * 负责将数据包编解码为协议包
 */
public class NettyMessageCodec extends MessageToMessageCodec<NettyPackage, NettyMessage> {
    private int partSize=NettyConst.DEFAULT_MAX_FRAME_LENGTH/2;
    private int memSize=Integer.MAX_VALUE/2;

    public NettyMessageCodec() {
    }

    public NettyMessageCodec(int partSize, int memSize) {
        this.partSize = partSize;
        this.memSize = memSize;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage pkg, List<Object> list) throws Exception {
        int partCnt=0;
        NettyPackage lastPkg=null;

        NettyPackage template=pkg.template;
        InputStream is = pkg.is;
        byte[] buf=new byte[partSize];
        int len=0;
        while ((len=is.read(buf))>0){
            NettyPackage part=new NettyPackage();
            part.magicNumber= template.magicNumber;
            part.version= template.version;
            part.serializeType= template.serializeType;
            part.pkgType= (byte)NettyPkgType.NONE.code();
            part.flag= template.flag;
            part.seqId= template.seqId;
            part.length=len;
            byte[] content = Arrays.copyOf(buf, len);
            part.content=content;

            if(lastPkg!=null){
                if(partCnt==0){
                    lastPkg.pkgType=(byte)NettyPkgType.BEGIN.code();
                }else{
                    lastPkg.pkgType=(byte)NettyPkgType.PART.code();
                }
                list.add(lastPkg);
            }

            lastPkg=part;
            partCnt++;
        }

        if(lastPkg!=null){
            if(partCnt==1){
                lastPkg.pkgType=(byte)NettyPkgType.NONE.code();
            }else{
                lastPkg.pkgType=(byte)NettyPkgType.END.code();
            }
            list.add(lastPkg);
        }
    }

    private OutputStream os;
    private File tmpFile;
    private long osSize;
    private NettyPackage template;

    @Override
    protected void decode(ChannelHandlerContext ctx, NettyPackage buf, List<Object> list) throws Exception {
        byte pkgType = buf.pkgType;
        if(pkgType==NettyPkgType.NONE.code()){
            NettyMessage msg=new NettyMessage();
            msg.template=buf;
            msg.is=new ByteArrayInputStream(buf.content);
            list.add(msg);
        }else if(pkgType==NettyPkgType.BEGIN.code()){
            os=new ByteArrayOutputStream();
            os.write(buf.content);
            os.flush();
            osSize=buf.content.length;
            template=buf;
        }else if(pkgType==NettyPkgType.PART.code()){
            long nextSize=osSize+buf.content.length;
            if(nextSize>=memSize){
                File tmpFile=File.createTempFile(UUID.randomUUID().toString(),".data",new File("netty-message"));
                OutputStream fos=new FileOutputStream(tmpFile);
                os.close();
                fos.write(((ByteArrayOutputStream)os).toByteArray());
                fos.flush();
                os=fos;
            }else{
                os.write(buf.content);
                os.flush();
            }
            osSize=nextSize;
        }else if(pkgType==NettyPkgType.END.code()){
            long nextSize=osSize+buf.content.length;
            if(nextSize>=memSize){
                tmpFile=File.createTempFile(UUID.randomUUID().toString(),".data",new File("netty-message"));
                OutputStream fos=new FileOutputStream(tmpFile);
                os.close();
                fos.write(((ByteArrayOutputStream)os).toByteArray());
                fos.flush();
                os=fos;
            }else{
                os.write(buf.content);
                os.flush();
            }
            osSize=nextSize;

            os.close();

            NettyMessage msg=new NettyMessage();
            msg.template=template;
            if(os instanceof ByteArrayOutputStream){
                msg.is=new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
            }else{
                msg.is=new FileInputStream(tmpFile);
            }
            list.add(msg);
        }
    }
}
