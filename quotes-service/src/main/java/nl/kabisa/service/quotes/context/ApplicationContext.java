package nl.kabisa.service.quotes.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Context variables for the application
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationContext {

    private String userid;
    private String securitytoken;
}
