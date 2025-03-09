package com.bank.config;

import com.bank.infra.postgres.AccountRepositoryImpl;
import com.bank.infra.postgres.TransactionRepositoryImpl;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryBeanConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeHandlersPackage("com.bank.infra.postgres.handler"); // Scans for @MappedTypes
        return factoryBean.getObject();
    }
}
