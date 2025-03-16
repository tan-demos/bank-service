package com.bank.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class TraceIdFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader("X-Trace-Id"); // Check for incoming traceId
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString(); // Generate new if not present
        }

        // Set traceId in MDC
        MDC.put("traceId", traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear MDC after request completes
            MDC.clear();
        }
    }
}
