package studyhub.studyhub.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.auth.dto.LoginRequest;
import studyhub.studyhub.domain.auth.dto.LoginResponse;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.InvalidCredentialsException;
import studyhub.studyhub.global.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        // Backward compatibility for users created before password hashing.
        boolean matched = passwordEncoder.matches(request.password(), user.getPassword())
                || user.getPassword().equals(request.password());

        if (!matched) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        return new LoginResponse(
                "Bearer",
                accessToken,
                jwtTokenProvider.getAccessTokenExpirationMs(),
                user.getId(),
                user.getEmail()
        );
    }
}

