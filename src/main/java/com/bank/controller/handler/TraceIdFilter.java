package com.bank.controller.handler;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String traceId = httpRequest.getHeader("X-Trace-Id"); // Check for incoming traceId
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString(); // Generate new if not present
        }

        // Set traceId in MDC
        MDC.put("traceId", traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            // Clear MDC after request completes
            MDC.clear();
        }
    }
}
