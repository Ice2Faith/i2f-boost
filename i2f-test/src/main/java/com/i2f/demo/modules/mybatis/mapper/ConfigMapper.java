package com.i2f.demo.modules.mybatis.mapper;

import com.i2f.demo.modules.mybatis.domain.ConfigDomain;
import i2f.extension.mybatis.interceptor.annotations.MybatisCamel;
import i2f.extension.mybatis.interceptor.annotations.MybatisLog;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/4 16:21
 * @desc
 */
@MybatisLog
public interface ConfigMapper {

    List<Map<String,Object>> qryConfig(ConfigDomain req);

    @MybatisCamel
    List<ConfigDomain> qryDomain(ConfigDomain req);
}
