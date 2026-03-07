package studyhub.studyhub.domain.study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class StudyCreateRequest {

    @NotBlank(message = "Study title is required.")
    @Size(max = 50, message = "Study title must be <= 50 characters.")
    private String title;

    @NotBlank(message = "Study description is required.")
    @Size(max = 200, message = "Study description must be <= 200 characters.")
    private String description;
}

