package studyhub.studyhub.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studyhub.studyhub.domain.auth.dto.LoginRequest;
import studyhub.studyhub.domain.auth.dto.LoginResponse;
import studyhub.studyhub.global.security.JwtUserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", loginResponse.accessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(loginResponse.expiresInMs() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie clearCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthMeResponse> me(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ResponseEntity.ok(new AuthMeResponse(principal.id(), principal.email()));
    }

    private record AuthMeResponse(Long userId, String email) {
    }
}
