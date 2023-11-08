package ru.template.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Аспект для перехвата логов
 */
@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Перехват запросов к контроллерам и параметров запроса
     */
    @Pointcut("within(ru.template.example.documents.controller.*)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object logExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();
        logger.log(Level.WARNING, "Название метода " + methodName);
        logger.log(Level.INFO, "Аргументы метода " + List.of(args));
        logger.log(Level.INFO, "Возвращаемое значение " + result);
        return result;
    }
}
