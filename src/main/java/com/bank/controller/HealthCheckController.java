package com.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/deep-ping")
    public ResponseEntity<Object> deepPing() {
        // TODO: check database, redis
        if (!checkDatabase()) {
            return new ResponseEntity<>("Unexpected database response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Database is healthy");
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    private boolean checkDatabase() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return result != null && result == 1;
        }
        catch (Exception exception) {
            logger.error("Failed to check db connection", exception);
            return false;
        }
    }
}
