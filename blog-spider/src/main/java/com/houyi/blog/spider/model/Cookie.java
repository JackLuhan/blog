package com.houyi.blog.spider.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houyi

 * @version 1.0
 * @date 2018/7/31 17:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Cookie {

    String domain;
    String name;
    String value;

    public Cookie(String domain, String name, String value) {
        this.domain = domain;
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
