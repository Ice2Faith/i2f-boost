package i2f.springboot.zplugin.config.controller;


import i2f.core.api.ApiPage;
import i2f.core.api.ApiResp;
import i2f.springboot.zplugin.config.dto.ConfigDto;
import i2f.springboot.zplugin.config.service.ConfigService;
import i2f.springboot.zplugin.config.vo.ConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ltb
 * @date 2022/06/06 15:46
 * @desc 配置项 Controller
 */
@RestController
@RequestMapping("config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 查询配置列表项
     * @param configVo
     * @return
     */
    @RequestMapping("find")
    public ApiResp<ApiPage<ConfigDto>> qryConfig(@RequestBody ConfigVo configVo){
        ApiPage<ConfigDto> page=configService.qryConfig(configVo);
        return ApiResp.success(page);
    }

}
