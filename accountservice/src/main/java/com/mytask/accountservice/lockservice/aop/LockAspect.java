package com.mytask.accountservice.lockservice.aop;

import com.mytask.accountservice.accountservice.annotations.LockByParameter;
import com.mytask.accountservice.accountservice.annotations.LockKey;
import com.mytask.accountservice.lockservice.LockService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Method;

/**
 * Aspect for locking handling
 * <p>
 * works only using only first {@link LockKey} annotation
 */
@Aspect
@Component
public class LockAspect {
    private final LockService lockService;

    @Autowired
    public LockAspect(LockService lockService) {
        this.lockService = lockService;
    }

    @Around(value = "@annotation(lockByParameter)")
    public Object handle(ProceedingJoinPoint joinPoint, LockByParameter lockByParameter) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod(); // get method
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        //  Can be changed to handle multiple LockKey (e.g. for locking multiple Accounts). Don't forget to sort keys before locking
        int argNumber = getArgNumberOfLockKey(parameterAnnotations);

        if (argNumber < 0) {
            throw new IncompleteAnnotationException(LockByParameter.class, "LockKey parameter annotation not found");
        }

        String lockKey = String.valueOf(joinPoint.getArgs()[argNumber]);
        lockService.lock(lockKey);
        try {
            return joinPoint.proceed();
        } finally {
            lockService.unlock(lockKey);
        }

    }

    private int getArgNumberOfLockKey(Annotation[][] parameterAnnotations) {
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().equals(LockKey.class)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
