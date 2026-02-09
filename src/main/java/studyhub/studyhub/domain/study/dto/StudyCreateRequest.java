package studyhub.studyhub.domain.study.dto;

import lombok.Getter;

@Getter
public class StudyCreateRequest {

    private Long userId;
    private String title;
    private String description;
}
