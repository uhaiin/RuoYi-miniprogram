package com.ruoyi.web.core.aop;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 切面记录请求入参和返回报文以及接口响应时间
 *
 * @author Jiancai.zhong
 * @since 2024-10-31 04:29:05
 */
@Component
@Aspect
@Slf4j
public class ControllerLogAspect {
    /**
     * 默认构造器，创建一个 ExampleClass 的实例。
     */
    public ControllerLogAspect() {
        // 构造器的具体实现
    }

    /**
     * 切面构造器
     * 该方法定义了一个切面的切入点，用于匹配com.uhaiin.controller包下的所有公共方法
     * 此注解用于指定切面关注的操作范围，是AOP（面向切面编程）中的一个关键概念
     * 它不执行任何操作，但为其他通知（如前置、后置、环绕通知等）提供了一个可以绑定的点
     */
    @Pointcut("execution(public * com.ruoyi.web.controller.wechat.*.*(..))")
    public void pointCut() {
    }

    /**
     * 切面监控请求接口耗时，打印入参、请求方法、出参等
     *
     * @param point 切点
     * @return Object
     * @throws Throwable Throwable 异常
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        log.info("Request ==> {} {}", request.getMethod(), Optional.of(request.getRequestURI()).orElse(null));

        String data = null;
        Object obj = point.proceed();
        try {
            data = JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("format response error", e);
        } finally {
            log.info("Response <== {}", data);
            long costTime = System.currentTimeMillis() - startTime;
            log.info("Method: {} executed in {} ms", point.getSignature(), costTime);
            if (costTime > 100) {
                // 获取类名和方法名
                String className = point.getTarget().getClass().getName();
                String methodName = point.getSignature().getName();
                log.warn("接口响应缓慢 - 类: {}, 方法: {}, 耗时: {}ms", className, methodName, costTime);
            }
        }

        return obj;
    }

}