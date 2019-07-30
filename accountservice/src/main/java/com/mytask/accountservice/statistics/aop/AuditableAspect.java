package com.mytask.accountservice.statistics.aop;

import com.mytask.accountservice.accountservice.annotations.Auditable;
import com.mytask.accountservice.statistics.StatisticsManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect for method invocation audit
 */
@Aspect
@Component
public class AuditableAspect {

    private final StatisticsManager statisticsManager;

    @Autowired
    public AuditableAspect(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    @Around(value = "@annotation(auditable)")
    public Object handle(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod(); // get method
        statisticsManager.registerInvoke(method.getName());
        return joinPoint.proceed();
    }

}
