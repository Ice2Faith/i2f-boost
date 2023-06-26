package i2f.core.io.stream.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author Ice2Faith
 * @date 2023/6/26 11:14
 * @desc
 */
public class WhiteHoleInputStream extends InputStream {
    private Random random = new Random();

    public WhiteHoleInputStream(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public int read() throws IOException {
        return random.nextInt(256);
    }
}
