package i2f.core.verifycode.data;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:29
 * @desc
 */
@Data
public class VerifyCodeDto {
    private BufferedImage img;
    private String result;
}
