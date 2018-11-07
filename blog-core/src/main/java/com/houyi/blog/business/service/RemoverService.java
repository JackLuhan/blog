package com.houyi.blog.business.service;

import com.houyi.blog.spider.model.BaseModel;

import java.io.PrintWriter;

/**
 * @author houyi
 * @version 1.0

 * @date 2018/8/21 15:35
 * @since 1.8
 */
public interface RemoverService {

    void run(Long typeId, BaseModel model, PrintWriter writer);

    void stop();
}
