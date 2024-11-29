package nl.kabisa.service.quotes.config.database;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "quotesEntityManagerFactory",
        transactionManagerRef = "quotesTransactionManager",
        basePackages = {
                "nl.kabisa.service.quotes.database"})

public class QuotesJpaConfig {
    @Primary
    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties("quotes.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "quotesDataSource")
    @ConfigurationProperties("lookup.configuration")
    public HikariDataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "quotesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("quotesDataSource") HikariDataSource hikariDataSource) {
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.generate_statistics", false);

        hikariDataSource.setMaximumPoolSize(30);

        return builder
                .dataSource(hikariDataSource)
                .packages("nl.kabisa.service.quotes.database")
                .persistenceUnit("quotes")
                .properties(jpaProperties)
                .build();
    }

    @Primary
    @Bean(name = "quotesTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("quotesEntityManagerFactory") EntityManagerFactory quotesEntityManagerFactory) {
        return new JpaTransactionManager(quotesEntityManagerFactory);
    }

    @Bean(name = "quotesJDBCTemplate")
    public JdbcTemplate quotesJDBCTemplate(@Qualifier("quotesDataSource") DataSource datasource) {
        return new JdbcTemplate(datasource);
    }

}
