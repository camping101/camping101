package com.camping101.beta.comment.controller;

import com.camping101.beta.comment.dto.*;
import com.camping101.beta.comment.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                                                             Principal principal){

        request.setWriterEmail(principal.getName());
        var createdComment = commentService.createComment(request);

        return ResponseEntity.ok(createdComment);
    }

    @GetMapping
    public ResponseEntity<CommentListResponse> getCommentListOfCampLog(@RequestBody CommentListRequest request){

        var commentList = commentService.getCommentListOfCampLog(request);

        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentInfoResponse>> getChildrenListOfParentComment(@PathVariable Long commentId){

        var reCommentList = commentService.getChildrenListOfParentComment(commentId);

        return ResponseEntity.ok(reCommentList);
    }

    @PutMapping
    public ResponseEntity<CommentInfoResponse> updateComment(@RequestBody CommentUpdateRequest request,
                                                             Principal principal){

        request.setRequesterEmail(principal.getName());
        var updatedComment = commentService.updateComment(request);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId,
                                        Principal principal){

        commentService.deleteComment(commentId, principal.getName());

        return ResponseEntity.ok().build();
    }


}
