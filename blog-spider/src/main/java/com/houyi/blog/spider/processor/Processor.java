package com.houyi.blog.spider.processor;

import com.houyi.blog.spider.model.BaseModel;
import us.codecraft.webmagic.Page;

/**
 * 页面解析接口
 *
 * @author houyi
 * @version 1.0

 * @date 2018/7/31 17:37
 */
public interface Processor {
    void process(Page page, BaseModel model);
}
