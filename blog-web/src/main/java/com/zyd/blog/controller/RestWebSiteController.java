/**
 * MIT License
 * Copyright (c) 2018 houyi
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zyd.blog.controller;

import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.Template;
import com.zyd.blog.business.enums.PlatformEnum;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.*;
import com.zyd.blog.util.FreeMarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点相关的接口类，主要为sitemap和robots的生成
 *
 * @author houyi
 * @version 1.0

 * @date 2018/4/18 11:48
 * @since 1.0
 */
@RestController
public class RestWebSiteController {
    @Autowired
    private BizArticleService articleService;
    @Autowired
    private BizTypeService typeService;
    @Autowired
    private BizTagsService tagsService;
    @Autowired
    private SysTemplateService templateService;
    @Autowired
    private SysConfigService configService;

    @GetMapping(value = "/sitemap.xml", produces = {"application/xml"})
    @BussinessLog(value = "查看sitemap.xml", platform = PlatformEnum.WEB)
    public String sitemapXml() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_XML);
    }

    @GetMapping(value = "/sitemap.txt", produces = {"text/plain"})
    @BussinessLog(value = "查看sitemap.txt", platform = PlatformEnum.WEB)
    public String sitemapTxt() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_TXT);
    }

    @GetMapping(value = "/sitemap.html", produces = {"text/html"})
    @BussinessLog(value = "查看sitemap.html", platform = PlatformEnum.WEB)
    public String sitemapHtml() {
        return getSitemap(TemplateKeyEnum.TM_SITEMAP_HTML);
    }

    @GetMapping(value = "/robots.txt", produces = {"text/plain"})
    @BussinessLog(value = "查看robots", platform = PlatformEnum.WEB)
    public String robots() {
        Template template = templateService.getTemplate(TemplateKeyEnum.TM_ROBOTS);
        return template.getRefValue();
    }

    private String getSitemap(TemplateKeyEnum key) {
        Template template = templateService.getTemplate(key);
        Map<String, Object> map = new HashMap<>();
        map.put("articleTypeList", typeService.listAll());
        map.put("articleTagsList", tagsService.listAll());
        map.put("articleList", articleService.listAll());
        map.put("config", configService.get());
        return FreeMarkerUtil.template2String(template.getRefValue(), map, true);
    }
}
