package i2f.springboot.zplugin.config.service;


import i2f.core.std.api.ApiPage;
import i2f.springboot.zplugin.config.dto.ConfigDto;
import i2f.springboot.zplugin.config.vo.ConfigVo;

/**
 * @author ltb
 * @date 2022/06/06 15:48
 * @desc 配置项 Service
 */
public interface ConfigService {
    /**
     * 查询配置列表项
     * @param configVo
     * @return
     */
    ApiPage<ConfigDto> qryConfig(ConfigVo configVo);

}
