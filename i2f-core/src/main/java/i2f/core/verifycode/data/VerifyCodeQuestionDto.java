package i2f.core.verifycode.data;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/8/15 17:11
 * @desc
 */
@Data
public class VerifyCodeQuestionDto {
    private String question;
    private String base64;
    private String code;
    public static final String URL_IMG_PREFIX = "data:image/*;base64,";
    private int width;
    private int height;
    private int type;
    private int count;

    public static VerifyCodeQuestionDto make(VerifyCodeDto dto, String code) throws IOException {
        return make(dto, code, true);
    }

    public static VerifyCodeQuestionDto make(VerifyCodeDto dto, String code, boolean withPrefix) throws IOException {
        VerifyCodeQuestionDto ret = new VerifyCodeQuestionDto();
        ret.setQuestion(dto.getQuestion());
        BufferedImage img = dto.getImg();
        ret.setWidth(img.getWidth());
        ret.setHeight(img.getHeight());
        ret.setType(dto.getType());
        ret.setCount(dto.getCount());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        byte[] bytes = bos.toByteArray();
        String bs64 = Base64.getEncoder().encodeToString(bytes);
        if (withPrefix) {
            bs64 = URL_IMG_PREFIX + bs64;
        }
        ret.setBase64(bs64);
        ret.setCode(code);
        return ret;
    }
}
