package com.bank.controller.api;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@RestController
public class HealthCheckController {
    private final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    private final JdbcTemplate jdbcTemplate;

    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaAdmin kafkaAdmin;


    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate,
                                 RedisTemplate<String, String> redisTemplate,
                                 KafkaAdmin kafkaAdmin) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
        this.kafkaAdmin = kafkaAdmin;
    }

    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/deep-ping")
    public ResponseEntity<Object> deepPing() {
        if (!checkKafka()) {
            return new ResponseEntity<>("Kafka is unavailable", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Kafka is available");

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

    private boolean checkKafka() {
        Properties props = new Properties();
        props.putAll(kafkaAdmin.getConfigurationProperties());

        try (AdminClient adminClient = AdminClient.create(props)) {
            ListTopicsResult topicsResult = adminClient.listTopics();
            logger.info("Topics: {}", topicsResult.names().get());

            DescribeClusterResult clusterResult = adminClient.describeCluster();
            Collection<Node> nodes = clusterResult.nodes().get();
            logger.info("Node count: {}", nodes.size());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Kafka is unavailable", e);
            return false;
        }
    }
}
