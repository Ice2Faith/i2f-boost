package com.i2f.demo.modules.feign.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ltb
 * @date 2022/3/27 15:56
 * @desc
 */
@FeignClient(name = "baidu-api",url = "https://www.baidu.com",path = "/")
public interface BaiduApi {
    @GetMapping(path = "s",consumes = "text/*")
    String search(@RequestParam("wd") String wd);
}
