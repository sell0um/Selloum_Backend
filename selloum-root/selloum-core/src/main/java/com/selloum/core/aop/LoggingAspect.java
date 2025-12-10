package com.selloum.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.selloum.api..controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
    	
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        log.info("[Controller : Start] {}", method);
        try {
            Object result = joinPoint.proceed();
            log.info("[Controller : End] {} ({}ms)", method, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("[Controller : Error] {} - {}", method, e.getMessage());
            throw e;
        }
    }
    
    @Around("execution(* com.selloum.api..service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {

    	String method = joinPoint.getSignature().toShortString();
        
    	long start = System.currentTimeMillis();
        
        log.info("[Service : Start] {} ", method);
        try {
            Object result = joinPoint.proceed();
            log.info("[Service : End] {} ({}ms)", method, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("[Service : Error] {} - {}", method, e.getMessage());
            throw e;
        }
    }
    
    @Around("execution(* com.selloum.batch.job.*.step.*.*(..))")
    public Object logBatchStep(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        log.info("▶ [Batch Step - Start] {}", method);
        try {
            Object result = joinPoint.proceed();
            log.info("[Batch Step - End] {} ({}ms)", method, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("[Batch Step - Error] {} - {}", method, e.getMessage());
            throw e;
        }
    }
}
