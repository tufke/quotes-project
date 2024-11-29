package nl.kabisa.service.quotes.config.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.config.properties.ApplicationProperties;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.dummyjson.client.api.GetQuotesApi;
import nl.kabisa.service.quotes.dummyjson.client.application.ApiClient;
import nl.kabisa.service.quotes.interceptor.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * The configuration for the java clients.
 *
 * @author Mark Spreksel
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class DummyjsonServiceClientConfig {

    private final ApplicationProperties properties;

    @Bean
    public GetQuotesApi getQuotesApi(RestTemplateBuilder restTemplateBuilder) {
        return new GetQuotesApi(dummyjsonServiceApiClient(restTemplateBuilder));
    }

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ApiClient dummyjsonServiceApiClient(RestTemplateBuilder restTemplateBuilder) {
        //Configure the connection settings when using resttemplate client
        var restTemplate =
                restTemplateBuilder
                        .requestFactory(() ->
                        {
                            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                            requestFactory.setConnectTimeout(properties.getDummyjsonServiceConnectionTimeout());

                            if (properties.getDummyjsonServiceLogging()) {
                                // This allows us to read the response more than once - Necessary for debugging.
                                return new BufferingClientHttpRequestFactory(requestFactory);
                            }
                            return requestFactory;
                        })
                        .build();

        //Configure the apiclient, for resttemplate or webflux client
        var apiClient = new ApiClient(restTemplate);

        //configure the basepath of the client
        if (StringUtils.isNotBlank(properties.getDummyjsonServiceGatewayUrl())) {
            apiClient.setBasePath(properties.getDummyjsonServiceGatewayUrl());
        }

        //configure if logging of request and response should be done when the client is called
        apiClient.setDebugging(properties.getDummyjsonServiceLogging());

        //Set request headers when callin another internal service
/*        String securityToken = ApplicationContextManager.getApplicationContext().getSecuritytoken();
        String userId = ApplicationContextManager.getApplicationContext().getUserid();

        var securityTokenAuth = (ApiKeyAuth) apiClient.getAuthentication(Headers.SECURITY_TOKEN_HEADER_NAME);
        securityTokenAuth.setApiKey(securityToken);

        var userIdAuth = (ApiKeyAuth) apiClient.getAuthentication(Headers.USER_ID_HEADER_NAME);
        userIdAuth.setApiKey(userId);
*/

        return apiClient;
    }

}

