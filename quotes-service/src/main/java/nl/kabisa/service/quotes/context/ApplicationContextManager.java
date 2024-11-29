package nl.kabisa.service.quotes.context;

/**
 * Manages the context of the request on a local thread
 */
public class ApplicationContextManager {

    private ApplicationContextManager() {
        //No instances allowed!
    }

    private static final ThreadLocal<ApplicationContext> applicationContextThreadLocal = new ThreadLocal<>();

    public static ApplicationContext getApplicationContext() {
        return applicationContextThreadLocal.get();
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        applicationContextThreadLocal.set(applicationContext);
    }

    public static void clear() {
        applicationContextThreadLocal.remove();
    }
}
