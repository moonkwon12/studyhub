package studyhub.studyhub.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.UnauthorizedException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getOrCreateGoogleUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new UnauthorizedException();
        }

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String name = oAuth2User.getAttribute("name");
                    if (name == null || name.isBlank()) {
                        name = "google-user";
                    }

                    User user = new User(
                            email,
                            passwordEncoder.encode(UUID.randomUUID().toString()),
                            name
                    );
                    return userRepository.save(user);
                });
    }
}

