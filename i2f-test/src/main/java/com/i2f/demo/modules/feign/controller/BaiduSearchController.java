package com.i2f.demo.modules.feign.controller;

import com.i2f.demo.modules.feign.api.BaiduApi;
import i2f.core.exception.BoostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ltb
 * @date 2022/3/27 15:59
 * @desc
 */
@RestController
@RequestMapping("baidu")
public class BaiduSearchController {

    @Autowired
    private BaiduApi baiduApi;

    @RequestMapping("search")
    public String search(String wd){
        if(wd==null || "".equals(wd)){
            throw new BoostException("bad search content.");
        }
        return baiduApi.search(wd);
    }
}
