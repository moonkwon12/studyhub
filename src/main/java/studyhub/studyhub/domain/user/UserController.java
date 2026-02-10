package studyhub.studyhub.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studyhub.studyhub.domain.user.dto.UserCreateRequest;
import studyhub.studyhub.domain.user.dto.UserResponse;

import java.util.List;

@Tag(name = "User", description = "사용자(User) 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원 가입",
            description = "이메일과 비밀번호, 이름을 입력받아 새로운 사용자를 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(schema = @Schema(implementation = Long.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 존재하는 이메일"
            )
    })
    @PostMapping
    public ResponseEntity<Long> createUser(
            @RequestBody @Valid UserCreateRequest request
    ) {
        Long userId = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @Operation(
            summary = "회원 단건 조회",
            description = "사용자 ID를 통해 회원 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "조회할 사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @Operation(
            summary = "회원 목록 조회",
            description = "전체 사용자 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(
            summary = "회원 삭제",
            description = "사용자 ID를 통해 회원을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "삭제할 사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
