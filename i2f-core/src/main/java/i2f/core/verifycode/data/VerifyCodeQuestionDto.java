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

    public static VerifyCodeQuestionDto make(VerifyCodeDto dto, String code) throws IOException {
        VerifyCodeQuestionDto ret = new VerifyCodeQuestionDto();
        ret.setQuestion(dto.getQuestion());
        BufferedImage img = dto.getImg();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        byte[] bytes = bos.toByteArray();
        String bs64 = Base64.getEncoder().encodeToString(bytes);
        ret.setBase64(bs64);
        ret.setCode(code);
        return ret;
    }
}
