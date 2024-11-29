package nl.kabisa.service.quotes.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.context.ApplicationContext;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.spring.boot.starter.service.exception.ServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;

/**
 * Interceptor which is responsible for updating/clearing the {@link ApplicationContext}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * Clears the {@link ApplicationContextManager} after the HTTP request has been executed.
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler  handler of the request
     * @param ex       {@link Exception}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ApplicationContextManager.clear();
    }

    /**
     * Intercepts the incoming request to validate if the required headers are set
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler  handler of the request
     * @return true if request is accepted
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        var securityToken = request.getHeader(Headers.SECURITY_TOKEN_HEADER_NAME);
        var userId = request.getHeader(Headers.USER_ID_HEADER_NAME);

        if (securityToken == null) {
            // securityToken should always be present for this application
            log.warn("Received request with missing {} header!", Headers.SECURITY_TOKEN_HEADER_NAME);
            throw new ServiceException("The x-security-token property should be present on the header!");
        }

        if (userId == null) {
            // OC-Code should always be present for this application
            log.warn("Received request with missing {} header!", Headers.USER_ID_HEADER_NAME);
            throw new ServiceException("The x-user-id property should be present on the header!");
        }

        if (log.isDebugEnabled()) {
            log.debug("Received request by user={} with token={}", userId, securityToken);
        }

        ApplicationContextManager.setApplicationContext(new ApplicationContext(userId, securityToken));
        return true;
    }
}
