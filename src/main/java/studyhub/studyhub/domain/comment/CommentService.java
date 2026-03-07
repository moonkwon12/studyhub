package studyhub.studyhub.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.comment.dto.CommentCreateRequest;
import studyhub.studyhub.domain.comment.dto.CommentCreateResponse;
import studyhub.studyhub.domain.comment.dto.CommentListItem;
import studyhub.studyhub.domain.post.StudyPost;
import studyhub.studyhub.domain.post.StudyPostRepository;
import studyhub.studyhub.domain.studymember.StudyMemberRepository;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;
import studyhub.studyhub.global.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudyPostRepository postRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long studyId, Long postId, Long loginUserId, CommentCreateRequest request) {
        StudyPost post = postRepository.findDetailForAuth(postId)
                .orElseThrow(PostNotFoundException::new);
        validatePostInStudy(post, studyId);

        studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        User user = userRepository.findById(loginUserId)
                .orElseThrow(UserNotFoundException::new);

        Comment comment = Comment.create(post, user, request.content());
        Comment saved = commentRepository.save(comment);

        return new CommentCreateResponse(saved.getId(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<CommentListItem> getComments(Long studyId, Long postId, Long loginUserId) {
        StudyPost post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        validatePostInStudy(post, studyId);

        studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        return commentRepository.findListByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long studyId, Long postId, Long commentId, Long loginUserId) {
        Comment comment = commentRepository.findDetailForAuth(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getPost().getId().equals(postId) || !comment.getPost().getStudy().getId().equals(studyId)) {
            throw new CommentNotFoundException();
        }

        var member = studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        boolean isAuthor = comment.getAuthor().getId().equals(loginUserId);
        boolean isLeader = member.getRole().name().equals("LEADER");
        if (!isAuthor && !isLeader) {
            throw new CommentPermissionDeniedException();
        }

        commentRepository.delete(comment);
    }

    private void validatePostInStudy(StudyPost post, Long studyId) {
        if (!post.getStudy().getId().equals(studyId)) {
            throw new PostNotFoundException();
        }
    }
}

