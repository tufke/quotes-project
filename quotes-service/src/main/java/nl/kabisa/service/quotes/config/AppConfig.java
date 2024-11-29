package nl.kabisa.service.quotes.config;

import lombok.RequiredArgsConstructor;
import nl.kabisa.service.quotes.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * Configuration for this application
 */
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class AppConfig {

    private final RequestInterceptor requestInterceptor;

    /**
     * Configures and returns a {@link MappedInterceptor} which intercepts
     * requests to the REST endpoints of this application.
     */
    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/api/**"}, requestInterceptor);
    }

}

