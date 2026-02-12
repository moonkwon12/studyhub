package studyhub.studyhub.domain.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import studyhub.studyhub.domain.post.dto.*;
import studyhub.studyhub.global.error.GlobalExceptionHandler;
import studyhub.studyhub.global.exception.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudyPostController.class)
class StudyPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyPostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 생성 성공")
    void createPost_success() throws Exception {

        StudyPostCreateRequest request =
                new StudyPostCreateRequest("제목", "내용");

        StudyPostCreateResponse response =
                new StudyPostCreateResponse(1L, LocalDateTime.now());

        Mockito.when(postService.createPost(eq(1L), eq(10L), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/studies/1/posts")
                        .header("X-USER-ID", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L));
    }

    @Test
    @DisplayName("스터디 없음 예외")
    void createPost_studyNotFound() throws Exception {

        Mockito.when(postService.createPost(eq(1L), eq(10L), any()))
                .thenThrow(new StudyNotFoundException());

        StudyPostCreateRequest request =
                new StudyPostCreateRequest("제목", "내용");

        mockMvc.perform(post("/api/studies/1/posts")
                        .header("X-USER-ID", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("스터디를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("스터디 멤버 아님 예외")
    void createPost_notMember() throws Exception {

        Mockito.when(postService.createPost(eq(1L), eq(10L), any()))
                .thenThrow(new StudyMemberNotFoundException());

        StudyPostCreateRequest request =
                new StudyPostCreateRequest("제목", "내용");

        mockMvc.perform(post("/api/studies/1/posts")
                        .header("X-USER-ID", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("스터디 멤버가 아닙니다."));
    }


    @Test
    @DisplayName("게시글 삭제 권한 없음")
    void deletePost_permissionDenied() throws Exception {

        Mockito.doThrow(new PostPermissionDeniedException())
                .when(postService)
                .deletePost(1L, 10L);

        mockMvc.perform(delete("/api/studies/1/posts/1")
                        .header("X-USER-ID", 10L))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("게시글에 대한 권한이 없습니다."));
    }

}