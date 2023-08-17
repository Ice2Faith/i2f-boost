package i2f.core.verifycode.data;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:29
 * @desc
 */
@Data
public class VerifyCodeStdDto<T> {
    private String question;
    private BufferedImage img;
    private T result;
    private int type;
}
