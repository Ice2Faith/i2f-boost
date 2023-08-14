package i2f.core.verifycode.impl;

import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.std.IVerifyCodeGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:35
 * @desc
 */
public class ArtTextVerifyCodeGenerator implements IVerifyCodeGenerator {
    public static final  SecureRandom RANDOM=new SecureRandom();

    public static final String PARAM_CHAR_LENGTH="charLength";
    public static final String PARAM_NUMBER_ONLY="numberOnly";

    public static final int DEFAULT_CHAR_LENGTH=4;
    public static final boolean DEFAULT_NUMBER_ONLY=false;
    public static final int DEFAULT_WIDTH=240;
    public static final int DEFAULT_HEIGHT=120;

    public static void main(String[] args) throws Exception {
        ArtTextVerifyCodeGenerator generator = new ArtTextVerifyCodeGenerator();

        VerifyCodeDto dto=generator.generate(240,120,null);

        File file=new File("./output.png");
        ImageIO.write(dto.getImg(),"PNG",file);

        System.out.println(dto.getResult());

        Runtime.getRuntime().exec("explorer \""+file.getAbsolutePath()+"\"");
    }

    @Override
    public VerifyCodeDto generate(int width, int height, Map<String, Object> params) {
        VerifyCodeDto ret=new VerifyCodeDto();

        if(width<=0){
            width=DEFAULT_WIDTH;
        }
        if(height<=0){
            height=DEFAULT_HEIGHT;
        }
        int charLen=6;
        boolean numberOnly=DEFAULT_NUMBER_ONLY;
        if(params!=null){
            if(params.containsKey(PARAM_CHAR_LENGTH)){
                Integer val=(Integer)params.get(PARAM_CHAR_LENGTH);
                if(val!=null && val>0){
                    charLen=val;
                }
            }
            if(params.containsKey(PARAM_NUMBER_ONLY)){
                Boolean val=(Boolean)params.get(PARAM_NUMBER_ONLY);
                if(val!=null){
                    numberOnly=val;
                }
            }
        }

        BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        String result=makeCode(charLen,numberOnly);

        Graphics2D g = img.createGraphics();
        // fill background
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


        int fontWidth=width/result.length();
        Font font=new Font(null,Font.ITALIC,fontWidth);
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();

        for (int i = 0; i < result.length(); i++) {

            BufferedImage tmp=new BufferedImage(fontHeight,fontHeight,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D tmpG = tmp.createGraphics();

            AffineTransform trans=new AffineTransform();
            trans.shear(RANDOM.nextDouble()*0.5,RANDOM.nextDouble()*0.5);
            trans.scale(RANDOM.nextDouble()*0.8+0.5,RANDOM.nextDouble()*0.8+0.5);

            tmpG.setTransform(trans);

            tmpG.setFont(font);
            String str = result.substring(i, i + 1);
            Rectangle2D bounds = tmpG.getFontMetrics().getStringBounds(str, tmpG);
            tmpG.translate(fontHeight/2,fontHeight/2);
            tmpG.rotate((RANDOM.nextDouble()-0.5)*(Math.PI/2));
            tmpG.setColor(new Color(RANDOM.nextInt(180),RANDOM.nextInt(180),RANDOM.nextInt(180)));



            tmpG.drawString(str,(int)-bounds.getWidth()/2,(int)bounds.getHeight()/4);


            g.drawImage(tmp,i*fontWidth,RANDOM.nextInt(height-fontHeight),null);
        }

        // draw shuffle line
        int shuffleCount=RANDOM.nextInt(30)+20;
        for(int i=0;i<shuffleCount;i++){
            g.setColor(new Color(RANDOM.nextInt(180),RANDOM.nextInt(180),RANDOM.nextInt(180)));
            g.drawLine(RANDOM.nextInt(width*2)-width/2,RANDOM.nextInt(height*2)-height/2,
                    RANDOM.nextInt(width*2)-width/2,RANDOM.nextInt(height*2)-height/2);
        }

        ret.setImg(img);
        ret.setResult(result);
        return ret;
    }

    public static String makeCode(int len,boolean numberOnly){

        String ret="";
        for (int i = 0; i < len; i++) {
            int num=RANDOM.nextInt(10);
            if(!numberOnly){
                num=RANDOM.nextInt(62);
            }
            char ch='A';
            if(num<10){
                ch=(char)('0'+num);
            }else if(num<36){
                ch=(char)('A'+(num-10));
            }else{
                ch=(char)('a'+(num-36));
            }
            ret+=ch;
        }
        return ret;
    }
}
