package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/24 14:28
 * @desc 图片隐写，将数据写入图片中，或从图片中读取数据
 * 需要注意，保存图片请使用无损格式，bmp或者png等无损格式
 * jpg这种有损格式，将无法再解析内容
 */
public class HideData2Image {
    /**
     * 计算这张图片最多存储的数据大小(byte)
     * 每个像素存放一个bit,8个像素存放一个字节
     * 前4个字节也就是32像素，存放数据长度
     * 构成：
     *
     * @param img
     * @return
     */
    public static int maxbytes(BufferedImage img) {
        return img.getWidth() / 8 * img.getHeight() - 4;
    }

    public static String read4ImageString(BufferedImage img){
        byte[] data=read4image(img);
        try{
            return new String(data,"UTF-8");
        }catch(Exception e){
            return new String(data);
        }
    }

    public static void write2ImageString(BufferedImage img,String str){
        try{
            byte[] data=str.getBytes("UTF-8");
            write2image(data,img);
        }catch(Exception e){

        }
    }

    public static byte[] read4image(BufferedImage img){
        int maxlen=img.getWidth()*img.getHeight();
        byte[] bytes=new byte[4];
        int idx=0;
        for(int i=0;i<bytes.length && (idx+8)<maxlen;i++){
            bytes[i]=readByte(img,idx);
            idx+=8;
        }
        if(idx>=maxlen){
            return new byte[0];
        }
        int size=((bytes[0]&0x0ff)<<24)|((bytes[1]&0x0ff)<<16)|((bytes[2]&0x0ff)<<8)|(bytes[3]&0x0ff);
        if(size<0){
            return new byte[0];
        }
        byte[] data=new byte[size];
        int vlen=0;
        for(int i=0;i<data.length && (idx+8)<maxlen;i++,vlen++){
            data[i]=readByte(img,idx);
            idx+=8;
        }
        if(vlen==data.length){
            return data;
        }
        byte[] rdata=new byte[vlen];
        for(int i=0;i<rdata.length;i++){
            rdata[i]=data[i];
        }
        return rdata;
    }
    public static void write2image(byte[] data, BufferedImage img) {
        int maxlen=img.getWidth()*img.getHeight();
        int len=data.length;
        //int2byte 高字节在低地址，大端
        byte[] bytes=new byte[4];
        bytes[0]=(byte)((len>>>24)&0x0ff);
        bytes[1]=(byte)((len>>>16)&0x0ff);
        bytes[2]=(byte)((len>>>8)&0x0ff);
        bytes[3]=(byte)((len)&0x0ff);
        int idx=0;
        for(int i=0;i<bytes.length && (idx+8)<maxlen;i++){
            writeByte(img,idx,bytes[i]);
            idx+=8;
        }
        for(int i=0;i<data.length && (idx+8)<maxlen;i++){
            writeByte(img,idx,data[i]);
            idx+=8;
        }
    }


    public static void writeByte(BufferedImage img,int idx,byte bt){
        // 第三位作为数据保存bit
        // 原因是，使用小bit，容易被图像压缩给压缩掉
        // 使用大bit,导致原图像失真严重
        // 11110111=0xf7
        // 00001000=0x08
        // 高bit在低索引，大端
        for(int i=7;i>=0;i--){
            int v=((bt&0x0ff)>>i)&0x01;
            Rgba c=pixelAt(img,idx+(7-i));
            int r=c.r;
            int g=c.g;
            int b=c.b;
            // 对RGB分量都设置，做数据冗余，只要存在两个都有效的情况下，丢失其中一个分量，也正正确保存数据
            r=(r&0x0f7)|(v<<3);
            g=(g&0x0f7)|(v<<3);
            b=(b&0x0f7)|(v<<3);


            c=Rgba.rgba(r,g,b,c.a);
            pixelAt(img,idx+(7-i),c);

        }
    }
    public static byte readByte(BufferedImage img,int idx){
        byte bt=0;
        for(int i=0;i<8;i++){
            Rgba c=pixelAt(img,idx+i);
            int r=c.r;
            int g=c.g;
            int b=c.b;

            r=(r>>>3)&0x01;
            g=(g>>>3)&0x01;
            b=(b>>>3)&0x01;

            int s=r+g+b;

            int v=0;
            if(s>=2){
                v=1;
            }

            bt<<=1;
            bt|=v;
        }
        return bt;
    }

    public static Rgba pixelAt(BufferedImage img, int idx){
        int y=idx/img.getWidth();
        int x=idx%img.getWidth();
        return Rgba.argb(img.getRGB(x,y));
    }
    public static void pixelAt(BufferedImage img,int idx,Rgba c){
        int y=idx/img.getWidth();
        int x=idx%img.getWidth();
        img.setRGB(x,y,c.argb());
    }
}
