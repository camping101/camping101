package com.camping101.beta.comment.service;

import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.campLog.repository.CampLogRepository;
import com.camping101.beta.comment.dto.*;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.comment.exception.CommentException;
import com.camping101.beta.comment.exception.ErrorCode;
import com.camping101.beta.comment.repository.CommentRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Member member = memberRepository.findByEmail(request.getWriterEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        CampLog campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        long commentCntOfCampLog = commentRepository.countAllByCampLog(campLog);
        validateIfCommentsCntOfCampLogAlreadyReachedFive(commentCntOfCampLog);

        List<Comment> commentsOfCampLogByMember = commentRepository.findAllByMemberAndCampLog(member, campLog);
        validateIfCommentAlreadyWrittenByMember(request, commentsOfCampLogByMember);

        Comment newComment = commentRepository.save(Comment.from(request));
        newComment.changeMember(member);
        newComment.changeCampLog(campLog);

        return CommentInfoResponse.fromEntity(newComment);
    }

    private static void validateIfCommentsCntOfCampLogAlreadyReachedFive(long commentCntOfCampLog) {
        // ????????? ?????? ????????? ?????? ?????? ??? 5???
        if (commentCntOfCampLog >= 5) {
            throw new CommentException(ErrorCode.COMMENT_MAX_LIMIT_REACHED);
        }
    }

    private static void validateIfCommentAlreadyWrittenByMember(CommentCreateRequest request,
                                                                List<Comment> commentsOfCampLogByMember) {
        if (!CollectionUtils.isEmpty(commentsOfCampLogByMember)){
            int mainCommentCnt = 0;

            // ?????? ?????? ???????????? ????????? ????????? ????????? ?????????
            for (Comment commentByMember : commentsOfCampLogByMember) {
                // ????????? 0??? ????????? ?????? ??????
                if (Objects.isNull(commentByMember.getParentId())
                        && commentByMember.isReCommentYn() == false) {
                    mainCommentCnt++;
                    if (mainCommentCnt >= 1) break;
                }
                // ????????? 1??? ??????(=?????????) ??????
                else {
                    // ?????? ?????? ????????? ???????????? ?????? ?????? ??? ?????? ?????? ??????
                    if (commentByMember.getParentId() == request.getParentId()) {
                        throw new CommentException(ErrorCode.RE_COMMENT_MAX_LIMIT_REACHED);
                    }
                }
            }

            // ????????? 0??? ????????? 1??? ????????? ???, ?????? 0??? ?????? ??? ?????? ?????? ??????
            if ((Objects.isNull(request.getParentId())
                    && request.isReCommentYn() == false
                    && mainCommentCnt >= 1) && request.getParentId() == -1){
                throw new CommentException(ErrorCode.COMMENT_MAX_LIMIT_REACHED);
            }
        }
    }

    public CommentListResponse getCommentListOfCampLog(CommentListRequest request) {

        CampLog campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        Pageable page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
                Sort.Direction.DESC, "createdAt");

        Page<Comment> commentList = commentRepository.findAllByCampLog(campLog, page);

        return CommentListResponse.fromEntity(commentList, campLog);
    }

    public List<CommentInfoResponse> getChildrenListOfParentComment(Long commentId) {

        List<Comment> commentList = commentRepository.findAllByParentId(commentId);

        if (CollectionUtils.isEmpty(commentList)) {
            throw new CommentException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return commentList.stream()
                .map(CommentInfoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentInfoResponse updateComment(CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getEmail().equals(request.getRequesterEmail())) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        comment.changeContent(request.getContent());

        return CommentInfoResponse.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String requesterEmail) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getEmail().equals(requesterEmail)) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        commentRepository.delete(comment);

    }

}
