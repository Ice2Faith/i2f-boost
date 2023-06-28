package com.i2f.demo.modules.mybatis.controller;

import com.i2f.demo.modules.mybatis.domain.ConfigDomain;
import com.i2f.demo.modules.mybatis.mapper.ConfigMapper;
import i2f.core.std.api.ApiResp;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ltb
 * @date 2022/4/7 15:22
 * @desc
 */
@RestController
@RequestMapping("config")
public class ConfigController {
    @Autowired
    private ConfigMapper configMapper;

    @RequiresRoles("admin")
    @PreAuthorize("hasAuthority('ROLE_admin')")
    @RequestMapping("all")
    public ApiResp getAllConfig(ConfigDomain req){
        return ApiResp.success(configMapper.qryConfig(req));
    }

    @RequestMapping("std")
    public Object getStd(ConfigDomain req){
        return configMapper.qryConfig(req);
    }
}
