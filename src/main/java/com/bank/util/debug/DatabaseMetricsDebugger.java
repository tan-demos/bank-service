package com.bank.util.debug;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
class DatabaseMetricsDebugger {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMetricsDebugger.class);
    @Autowired
    private MeterRegistry meterRegistry;
    @Scheduled(fixedRate = 60000) // Report every minute
    void reportMetrics() {
        var activeConnections = meterRegistry.get("hikaricp.connections.active")
                .tag("pool", "MyHikariPool")
                .gauge()
                .value();
        var idleConnections = meterRegistry.get("hikaricp.connections.idle")
                .tag("pool", "MyHikariPool")
                .gauge()
                .value();
        var totalConnections = meterRegistry.get("hikaricp.connections")
                .tag("pool", "MyHikariPool")
                .gauge()
                .value();
        logger.debug("Active Connections: {}, Idle Connections: {}, Total Connections: {}", activeConnections, idleConnections, totalConnections);

    }
}