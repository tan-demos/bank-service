package com.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    private final JdbcTemplate jdbcTemplate;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate, RedisTemplate<String, String> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/deep-ping")
    public ResponseEntity<Object> deepPing() {
        if (!checkDatabase()) {
            return new ResponseEntity<>("Database is unavailable", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Database is available");

        if (!checkRedis()) {
            return new ResponseEntity<>("Redis is unavailable", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Redis is available");

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    private boolean checkDatabase() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception exception) {
            logger.error("Database is not available", exception);
            return false;
        }
    }

    private boolean checkRedis() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            return true;
        } catch (Exception exception) {
            logger.error("Redis is not available", exception);
            return false;
        }
    }
}
