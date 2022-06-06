package i2f.springboot.zplugin.config.mapper;

import i2f.springboot.zplugin.config.dto.ConfigDto;
import i2f.springboot.zplugin.config.vo.ConfigVo;

import java.util.List;

/**
 * @author ltb
 * @date 2022/06/06 15:47
 * @desc
 */
public interface ConfigMapper {
    /**
     * 查询配置项列表
     * @param configVo
     * @return
     */
    List<ConfigDto> qryConfig(ConfigVo configVo);

}
