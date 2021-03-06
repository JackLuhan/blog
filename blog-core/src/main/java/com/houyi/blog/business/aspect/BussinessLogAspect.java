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
package com.houyi.blog.business.aspect;

import com.houyi.blog.business.annotation.BussinessLog;
import com.houyi.blog.business.entity.Log;
import com.houyi.blog.business.entity.User;
import com.houyi.blog.business.enums.LogLevelEnum;
import com.houyi.blog.business.enums.LogTypeEnum;
import com.houyi.blog.business.enums.PlatformEnum;
import com.houyi.blog.business.service.SysLogService;
import com.houyi.blog.business.util.WebSpiderUtils;
import com.houyi.blog.util.AspectUtil;
import com.houyi.blog.util.RegexUtils;
import com.houyi.blog.util.RequestUtil;
import com.houyi.blog.util.SessionUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AOP切面记录日志
 *
 * @author houyi
 * @version 1.0

 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class BussinessLogAspect {

    @Autowired
    private SysLogService logService;

    @Pointcut(value = "@annotation(com.houyi.blog.business.annotation.BussinessLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object writeLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handle(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point) throws Exception {
        //获取拦截方法的参数
        String className = AspectUtil.getClassName(point);
        Method currentMethod = AspectUtil.getMethod(point);
        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
        boolean save = annotation.save();
        PlatformEnum platform = annotation.platform();
        String bussinessName = parseContent(point.getArgs(), annotation.value());
        String ua = RequestUtil.getUa();

        log.info("{}-{}.{}", bussinessName, className, currentMethod.getName());
        log.info("IP: {}, Method: {}, Request URL: {}", RequestUtil.getIp(), RequestUtil.getMethod(), RequestUtil.getRequestUrl());
        log.info("User-Agent: " + ua);
        if (!save) {
            return;
        }
        Log sysLog = new Log();
        sysLog.setLogLevel(LogLevelEnum.INFO);
        sysLog.setType(platform.equals(PlatformEnum.WEB) ? LogTypeEnum.VISIT : LogTypeEnum.SYSTEM);
        sysLog.setIp(RequestUtil.getIp());
        sysLog.setReferer(RequestUtil.getReferer());
        sysLog.setRequestUrl(RequestUtil.getRequestUrl());
        sysLog.setUa(ua);
        sysLog.setSpiderType(WebSpiderUtils.parseUa(ua));

        User user = SessionUtil.getUser();
        if(user != null) {
            sysLog.setUserId(user.getId());
            sysLog.setContent(String.format("用户: [%s] | 操作: %s | 参数: %s", user.getUsername(), bussinessName, RequestUtil.getParameters()));
        } else {
            sysLog.setContent(String.format("访客: [%s] | 操作: %s | 参数: %s", sysLog.getIp(), bussinessName, RequestUtil.getParameters()));
        }

        try {
            UserAgent agent = UserAgent.parseUserAgentString(ua);
            sysLog.setBrowser(agent.getBrowser().getName());
            sysLog.setOs(agent.getOperatingSystem().getName());
            logService.insert(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseContent(Object[] params, String bussinessName) {
        if (bussinessName.contains("{") && bussinessName.contains("}")) {
            List<String> result = RegexUtils.match(bussinessName, "(?<=\\{)(\\d+)");
            for (String s : result) {
                int index = Integer.parseInt(s);
                bussinessName = bussinessName.replaceAll("\\{" + index + "\\}", String.valueOf(params[index - 1]));
            }
        }
        return bussinessName;
    }
}
