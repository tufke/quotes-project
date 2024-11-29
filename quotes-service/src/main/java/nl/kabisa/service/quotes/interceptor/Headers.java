package nl.kabisa.service.quotes.interceptor;

/**
 * Represents the custom headers in api
 */
public class Headers {

    private Headers() {
        //No instances allowed!
    }

    public static final String SECURITY_TOKEN_HEADER_NAME = "x-security-token";
    public static final String USER_ID_HEADER_NAME = "x-user-id";

}
