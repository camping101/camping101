package com.camping101.beta.web.domain.comment.controller;

import com.camping101.beta.web.domain.comment.dto.CommentCreateRequest;
import com.camping101.beta.web.domain.comment.dto.CommentInfoResponse;
import com.camping101.beta.web.domain.comment.dto.CommentListRequest;
import com.camping101.beta.web.domain.comment.dto.CommentListResponse;
import com.camping101.beta.web.domain.comment.dto.CommentUpdateRequest;
import com.camping101.beta.web.domain.comment.service.CommentService;
import io.swagger.annotations.Api;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camplog/comment")
@Api(tags = {"캠핑 101 - 댓글 API"})
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentInfoResponse> createComment(
        @RequestBody CommentCreateRequest request,
        @ApiIgnore Principal principal) {

        request.setWriterEmail(principal.getName());
        CommentInfoResponse createdComment = commentService.createComment(request);

        return ResponseEntity.ok(createdComment);
    }

    @GetMapping
    public ResponseEntity<CommentListResponse> getCommentListOfCampLog(CommentListRequest request) {

        CommentListResponse commentList = commentService.getCommentListOfCampLog(request);

        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentInfoResponse>> getChildrenListOfParentComment(
        @PathVariable Long commentId) {

        List<CommentInfoResponse> reCommentList = commentService.getChildrenListOfParentComment(
            commentId);

        return ResponseEntity.ok(reCommentList);
    }

    @PutMapping
    public ResponseEntity<CommentInfoResponse> updateComment(
        @RequestBody CommentUpdateRequest request,
        @ApiIgnore Principal principal) {

        request.setRequesterEmail(principal.getName());
        CommentInfoResponse updatedComment = commentService.updateComment(request);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId,
        @ApiIgnore Principal principal) {

        commentService.deleteComment(commentId, principal.getName());

        return ResponseEntity.ok().build();
    }


}
