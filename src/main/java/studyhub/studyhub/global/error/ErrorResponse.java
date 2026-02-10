package studyhub.studyhub.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(
        description = """
    공통 에러 응답 형식입니다.
    
    모든 에러 응답은 HTTP 상태 코드와 관계없이
    아래 JSON 구조를 따릅니다.
    """
)
public class ErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "404")
    private final int status;

    @Schema(description = "HTTP 상태 이름", example = "Not Found")
    private final String error;

    @Schema(description = "에러 상세 메시지", example = "사용자를 찾을 수 없습니다.")
    private final String message;

    private ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public static ErrorResponse from(HttpStatus status, String message) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }
}
