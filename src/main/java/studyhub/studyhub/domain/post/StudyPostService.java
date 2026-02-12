package studyhub.studyhub.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.post.dto.StudyPostCreateRequest;
import studyhub.studyhub.domain.post.dto.StudyPostCreateResponse;
import studyhub.studyhub.domain.post.dto.StudyPostListItem;
import studyhub.studyhub.domain.study.Study;
import studyhub.studyhub.domain.study.StudyRepository;
import studyhub.studyhub.domain.studymember.StudyMemberRepository;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository postRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Transactional
    public StudyPostCreateResponse createPost(Long studyId, Long loginUserId, StudyPostCreateRequest req) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);

        User user = userRepository.findById(loginUserId)
                .orElseThrow(UserNotFoundException::new);

        studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        StudyPost post = StudyPost.create(study, user, req.title(), req.content());
        StudyPost saved = postRepository.save(post);

        return new StudyPostCreateResponse(saved.getId(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<StudyPostListItem> getPostList(Long studyId, Long loginUserId) {
        // 조회도 “멤버만 볼 수 있다” 정책이면 여기서 멤버 검증
        studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        // DTO Projection
        return postRepository.findListByStudyId(studyId);
    }

    @Transactional
    public void deletePost(Long postId, Long loginUserId) {

        StudyPost post = postRepository.findDetailForAuth(postId)
                .orElseThrow(PostNotFoundException::new);

        Long studyId = post.getStudy().getId();

        var member = studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        boolean isAuthor = post.getAuthor().getId().equals(loginUserId);
        boolean isLeader = member.getRole().name().equals("LEADER");

        if (!isAuthor && !isLeader) {
            throw new PostPermissionDeniedException();
        }

        postRepository.delete(post);
    }
}