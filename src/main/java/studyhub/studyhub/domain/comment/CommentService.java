package studyhub.studyhub.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.comment.dto.CommentCreateRequest;
import studyhub.studyhub.domain.comment.dto.CommentCreateResponse;
import studyhub.studyhub.domain.comment.dto.CommentListItem;
import studyhub.studyhub.domain.comment.dto.CommentUpdateRequest;
import studyhub.studyhub.domain.comment.dto.CommentUpdateResponse;
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

        var member = studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);
        boolean isLeader = member.getRole().name().equals("LEADER");

        return commentRepository.findWithAuthorByPostId(postId)
                .stream()
                .map(c -> {
                    boolean mine = c.getAuthor().getId().equals(loginUserId);
                    return new CommentListItem(
                            c.getId(),
                            c.getAuthor().getId(),
                            c.getAuthor().getName(),
                            c.getContent(),
                            c.getCreatedAt(),
                            mine,
                            mine,
                            mine || isLeader
                    );
                })
                .toList();
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

    @Transactional
    public CommentUpdateResponse updateComment(Long studyId, Long postId, Long commentId, Long loginUserId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findDetailForAuth(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getPost().getId().equals(postId) || !comment.getPost().getStudy().getId().equals(studyId)) {
            throw new CommentNotFoundException();
        }

        // Must be a study member and the comment author.
        studyMemberRepository.findByStudyIdAndUserId(studyId, loginUserId)
                .orElseThrow(StudyMemberNotFoundException::new);

        if (!comment.getAuthor().getId().equals(loginUserId)) {
            throw new CommentPermissionDeniedException();
        }

        comment.updateContent(request.content());
        return new CommentUpdateResponse(comment.getId(), comment.getContent(), comment.getCreatedAt());
    }

    private void validatePostInStudy(StudyPost post, Long studyId) {
        if (!post.getStudy().getId().equals(studyId)) {
            throw new PostNotFoundException();
        }
    }
}
