package test.verifycode;

import i2f.core.verifycode.data.VerifyCodeDto;
import i2f.core.verifycode.impl.MultiMatrixMarkerVerifyCodeGenerator;
import i2f.core.verifycode.std.IVerifyCodeGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

/**
 * @author Ice2Faith
 * @date 2023/8/17 20:48
 * @desc
 */
public class TestVerifyCode {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        IVerifyCodeGenerator generator = new MultiMatrixMarkerVerifyCodeGenerator();

        VerifyCodeDto dto = generator.generate(0, 0, null);
        System.out.println("问题：" + dto.getQuestion());
        System.out.println("类型：" + dto.getType());
        System.out.println("次数：" + dto.getCount());

        BufferedImage img = dto.getImg();
        File file = new File("./output.png");
        ImageIO.write(img, "png", file);

        System.out.println("请输入你的答案：");

        String answer = scanner.nextLine().trim();

        boolean ok = generator.verify(dto.getResult(), answer);
        System.out.println("验证结果：" + ok);
        System.out.println("真实结果：" + dto.getResult());

    }
}
