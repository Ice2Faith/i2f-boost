package i2f.core.safe.token.impl;

import i2f.core.safe.token.ITokenGenerator;

import java.util.UUID;

/**
 * @author ltb
 * @date 2022/5/25 8:47
 * @desc 定义一个基于UUID的token生成器
 */
public class UuidTokenGenerator implements ITokenGenerator {
    @Override
    public String token(Object... args) {
        return UUID.randomUUID().toString().replace("-","").toLowerCase();
    }
}
