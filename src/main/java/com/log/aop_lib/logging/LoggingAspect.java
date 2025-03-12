package com.log.aop_lib.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoggingAspect() {
        logger.info(">>> LoggingAspect carregado pelo Spring!");
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("Entrando no método: {}.{}() com argumentos: {}", className, methodName, objectMapper.writeValueAsString(args));

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exceção no método: {}.{}() - {}", className, methodName, e.getMessage(), e);
            throw e;
        }

        long timeTaken = System.currentTimeMillis() - startTime;

        logger.info("Saída do método: {}.{}() com retorno: {} (Executado em {} ms)", className, methodName, objectMapper.writeValueAsString(result), timeTaken);

        return result;
    }
}
