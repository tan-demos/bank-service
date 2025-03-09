package com.bank.util.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class PropertiesDebugger implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesDebugger.class);
    @Autowired
    private ConfigurableEnvironment env;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        logger.debug("+++datasource url: {}", dataSourceUrl);
        logger.debug("+++datasource username: {}", username);
        logger.debug("+++datasource password: {}", password);

        for (PropertySource<?> propertySource : env.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>) propertySource;
                logger.debug("--- Source: {} ---", propertySource.getName());
                for (String key : enumerable.getPropertyNames()) {
                    logger.debug("{}={}", key, propertySource.getProperty(key));
                }
            }
        }
    }
}
