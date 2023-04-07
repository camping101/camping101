package com.camping101.beta.web.domain.comment.controller;

import com.camping101.beta.web.domain.comment.dto.*;
import com.camping101.beta.web.domain.comment.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camplog/comment")
@Api(tags = {"댓글 API"})
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentInfoResponse> createComment(@RequestBody CommentCreateRequest request,
                                                             @ApiIgnore Principal principal){

        request.setWriterEmail(principal.getName());
        CommentInfoResponse createdComment = commentService.createComment(request);

        return ResponseEntity.ok(createdComment);
    }

    @GetMapping
    public ResponseEntity<CommentListResponse> getCommentListOfCampLog(CommentListRequest request){

        CommentListResponse commentList = commentService.getCommentListOfCampLog(request);

        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentInfoResponse>> getChildrenListOfParentComment(@PathVariable Long commentId){

        List<CommentInfoResponse> reCommentList = commentService.getChildrenListOfParentComment(commentId);

        return ResponseEntity.ok(reCommentList);
    }

    @PutMapping
    public ResponseEntity<CommentInfoResponse> updateComment(@RequestBody CommentUpdateRequest request,
                                                             @ApiIgnore Principal principal){

        request.setRequesterEmail(principal.getName());
        CommentInfoResponse updatedComment = commentService.updateComment(request);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId,
                                        @ApiIgnore Principal principal){

        commentService.deleteComment(commentId, principal.getName());

        return ResponseEntity.ok().build();
    }


}
