package studyhub.studyhub.global.security;

import org.springframework.stereotype.Component;
import studyhub.studyhub.global.exception.UnauthorizedException;

@Component
public class AuthUserIdResolver {

    public Long resolve(JwtUserPrincipal principal) {
        if (principal != null) {
            return principal.id();
        }
        throw new UnauthorizedException();
    }
}

