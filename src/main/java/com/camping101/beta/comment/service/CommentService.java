package com.camping101.beta.comment.service;

import com.camping101.beta.campLog.CampLogRepository;
import com.camping101.beta.comment.dto.*;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.comment.repository.CommentRepository;
import com.camping101.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final CampLogRepository campLogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentInfoResponse createComment(CommentCreateRequest request) {

        var member = memberRepository.findByEmail(request.getWriterEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        var campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new RuntimeException(""));

        var commentCntOfCampLog = commentRepository.countAllByCampLog(campLog);
        validateIfCommentsCntOfCampLogAlreadyReachedFive(commentCntOfCampLog);

        var commentsOfCampLogByMember = commentRepository.findAllByMemberAndCampLog(member, campLog);
        validateIfCommentAlreadyWrittenByMember(request, commentsOfCampLogByMember);

        Comment newComment = commentRepository.save(Comment.from(request));
        newComment.setMember(member);
        newComment.setCampLog(campLog);

        return CommentInfoResponse.fromEntity(newComment);
    }

    private static void validateIfCommentsCntOfCampLogAlreadyReachedFive(long commentCntOfCampLog) {

        if (commentCntOfCampLog >= 5) {
            throw new RuntimeException("하나의 캠프 로그에는 최대 5개의 댓글이 달릴 수 있습니다.");
        }

    }

    private static void validateIfCommentAlreadyWrittenByMember(CommentCreateRequest request,
                                                                List<Comment> commentsOfCampLogByMember) {
        if (!CollectionUtils.isEmpty(commentsOfCampLogByMember)){
            int mainCommentCnt = 0;
            int reCommentCnt = 0;
            for (Comment commentByMember : commentsOfCampLogByMember) {
                if (Objects.isNull(commentByMember.getParentId())
                        && commentByMember.isReCommentYn() == false) {
                    mainCommentCnt++;
                } else {
                    reCommentCnt++;
                }
            }

            if ((Objects.isNull(request.getParentId())
                    && request.isReCommentYn() == false
                    && mainCommentCnt >= 1)){
                throw new RuntimeException("하나의 캠프 로그에 하나의 댓글만 작성 가능합니다.");
            }

            if (Objects.nonNull(request.getParentId())
                    && request.isReCommentYn()
                    && reCommentCnt >= 1){
                throw new RuntimeException("하나의 댓글에 하나의 대댓글만 작성 가능합니다.");
            }
        }
    }

    public CommentListResponse getCommentListOfCampLog(CommentListRequest request) {

        var campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new RuntimeException(""));

        var page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
                Sort.Direction.DESC, "created_at");

        var commentList = commentRepository.findAllByCampLog(campLog, page);

        return CommentListResponse.fromEntity(commentList);
    }

    public List<CommentInfoResponse> getChildrenListOfParentComment(Long commentId) {

        var commentList = commentRepository.findAllByParentId(commentId);

        if (CollectionUtils.isEmpty(commentList)) {
            throw new RuntimeException("");
        }

        return commentList.stream()
                .map(CommentInfoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentInfoResponse updateComment(CommentUpdateRequest request) {

        var comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getMember().getEmail().equals(request.getRequesterEmail())) {
            throw new RuntimeException("댓글을 쓴 작성자만 수정이 가능합니다.");
        }

        comment.setContent(request.getContent());

        return CommentInfoResponse.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String requesterEmail) {

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getMember().getEmail().equals(requesterEmail)) {
            throw new RuntimeException("댓글을 쓴 작성자만 삭제가 가능합니다.");
        }

        commentRepository.delete(comment);

    }

}
