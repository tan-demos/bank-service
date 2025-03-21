package com.bank.util.aspect;

import com.bank.util.annotation.AutoLogging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AutoLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(AutoLoggingAspect.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public AutoLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.bank.util.annotation.AutoLogging)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            logger.warn("{} is not MethodSignature", signature);
            return joinPoint.proceed();
        }

        var annotation = methodSignature.getMethod().getAnnotation(AutoLogging.class);
        String methodDescription = null;
        if (annotation.input() || annotation.output()) {
            methodDescription = getMethodDescription(methodSignature);
        }
        if (annotation.input()) {
            logger.info("{} input: {}", methodDescription, joinPoint.getArgs());
        }

        try {
            Object output = joinPoint.proceed();
            if (annotation.output()) {
                logger.info("{} output: {}", methodDescription, objectToText(output));
            }
            return output;
        } catch (Exception e) {
            logger.error(methodDescription, e);
            throw e;
        }
    }

    private String getMethodDescription(MethodSignature signature) {
        var parameterNames = signature.getParameterNames();
        var builder = new StringBuilder();
        builder.append(signature.getDeclaringTypeName());
        builder.append('.');
        builder.append(signature.getName());
        builder.append('(');
        for (int i = 0; i < parameterNames.length; i++) {
            builder.append(parameterNames[i]);
            if (i != parameterNames.length-1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private String objectToText(Object o) {
        if (o instanceof String) {
            return (String) o;
        }

        if (o instanceof Long || o instanceof Short || o instanceof Integer || o instanceof Boolean
        || o instanceof Double || o instanceof Float || o instanceof Character) {
            return String.valueOf(o);
        }

        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(String.format("Cannot write object %s into string", o.getClass().getName()), e);
            return o.getClass().getName();
        }
    }
}
