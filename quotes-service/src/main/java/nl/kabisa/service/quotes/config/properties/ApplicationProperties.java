package nl.kabisa.service.quotes.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
public class ApplicationProperties {

    @Value("${service.dummyjson-service.gateway-url:https://dummyjson.com}")
    private String dummyjsonServiceGatewayUrl;
    @Value("${service.dummyjson-service.connection-timeout-millis:30000}")
    private Integer dummyjsonServiceConnectionTimeout;
    @Value("${service.dummyjson-service.read-timeout-millis:15000}")
    private Integer dummyjsonServiceReadTimeout;
    @Value("${service.dummyjson-service.logging:false}")
    private Boolean dummyjsonServiceLogging;
}
