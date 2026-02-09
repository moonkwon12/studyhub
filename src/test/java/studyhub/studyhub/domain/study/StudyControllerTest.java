package studyhub.studyhub.domain.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 스터디_생성_성공() throws Exception {
        CreateStudyRequest request = new CreateStudyRequest();
        request.userId = 1L;
        request.title = "JPA 스터디";
        request.description = "MockMvc 테스트";

        mockMvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("study-create",
                        requestFields(
                                fieldWithPath("userId").description("스터디 생성자 사용자 ID"),
                                fieldWithPath("title").description("스터디 제목"),
                                fieldWithPath("description").description("스터디 설명")
                        )
                ));
    }

    @Test
    void 스터디_생성_실패_사용자없음() throws Exception {
        CreateStudyRequest request = new CreateStudyRequest();
        request.userId = 999L;
        request.title = "실패 테스트";
        request.description = "에러";

        mockMvc.perform(post("/api/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("사용자를 찾을 수 없습니다."));
    }

}

class CreateStudyRequest {
    public Long userId;
    public String title;
    public String description;
}