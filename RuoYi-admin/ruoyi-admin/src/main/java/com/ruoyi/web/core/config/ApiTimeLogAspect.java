package com.ruoyi.web.core.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 接口耗时记录切面
 */
@Aspect
@Component
public class ApiTimeLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ApiTimeLogAspect.class);

    // 定义切入点：匹配所有Controller中的方法
    @Pointcut("execution(* com.ruoyi.web.controller.wechat..*.*(..))")
    public void apiPointcut() {
    }

    // 环绕通知，用于计算方法执行时间
    @Around("apiPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        try {
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            // 计算并记录耗时
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 获取类名和方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            // 打印耗时日志
            logger.info("接口调用 - 类: {}, 方法: {}, 耗时: {}ms",
                    className, methodName, costTime);

            // 可以在这里添加耗时过长的预警逻辑
            if (costTime > 500) {
                logger.warn("接口响应缓慢 - 类: {}, 方法: {}, 耗时: {}ms",
                        className, methodName, costTime);
            }
        }
    }
}
