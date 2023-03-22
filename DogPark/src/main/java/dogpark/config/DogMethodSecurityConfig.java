package dogpark.config;

import dogpark.service.DogService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DogMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final DogService dogService;

    public DogMethodSecurityConfig(DogService dogService) {
        this.dogService = dogService;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new DogSecurityExpressionHandler(dogService);
    }
}
