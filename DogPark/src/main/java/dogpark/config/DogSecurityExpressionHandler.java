package dogpark.config;

import dogpark.service.DogService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class DogSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final DogService dogService;

    public DogSecurityExpressionHandler(DogService dogService) {
        this.dogService = dogService;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {

        OwnerSecurityExpressionRoot root = new OwnerSecurityExpressionRoot(authentication, dogService);

        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        root.setRoleHierarchy(getRoleHierarchy());

        return root;
    }
}
