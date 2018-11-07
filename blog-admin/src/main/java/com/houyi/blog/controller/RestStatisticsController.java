package com.houyi.blog.controller;

import com.houyi.blog.business.service.BizStatisticsService;
import com.houyi.blog.business.service.SysConfigService;
import com.houyi.blog.framework.object.ResponseVO;
import com.houyi.blog.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author houyi
 * @version 1.0
 * @date 2018/5/22 16:47
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
public class RestStatisticsController {

    @Autowired
    private SysConfigService configService;
    @Autowired
    private BizStatisticsService statisticsService;

    @RequestMapping("/siteInfo")
    public ResponseVO getSiteInfo(){
        return ResultUtil.success("", configService.getSiteInfo());
    }

    @RequestMapping("/listSpider")
    public ResponseVO listSpider(){
        return ResultUtil.success("", statisticsService.listSpider(10));
    }

    @RequestMapping("/listType")
    public ResponseVO listType(){
        return ResultUtil.success("", statisticsService.listType(10));
    }

}
