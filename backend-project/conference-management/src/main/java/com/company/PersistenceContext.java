package com.company;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.company.repository"})
public class PersistenceContext {

    // DATASOURCE CONFIGURATION (HIKARI)
    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("db.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("db.password"));

        return new HikariDataSource(dataSourceConfig);
    }

    // ENTITY MANAGER CONFIGURATION (HIBERNATE)
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            Environment env) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.company.domain");

        Properties jpaProperties = new Properties();

        jpaProperties.put("logging.level.org.hibernate.SQL",
                env.getRequiredProperty("logging.level.org.hibernate.SQL"));

        jpaProperties.put("logging.level.org.hibernate.type.descriptor.sql.BasicBinder",
                env.getRequiredProperty("logging.level.org.hibernate.type.descriptor.sql.BasicBinder"));

        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));

        jpaProperties.put("hibernate.hbm2ddl.auto",
                env.getRequiredProperty("hibernate.hbm2ddl.auto"));

        jpaProperties.put("hibernate.ejb.naming_strategy",
                env.getRequiredProperty("hibernate.ejb.naming_strategy"));

        jpaProperties.put("hibernate.show_sql",
                env.getRequiredProperty("hibernate.show_sql"));

        jpaProperties.put("hibernate.format_sql",
                env.getRequiredProperty("hibernate.format_sql"));

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }


    // TRANSACTION MANAGER CONFIGURATION
    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }


}
