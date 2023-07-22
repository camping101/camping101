package com.camping101.beta.web.domain.comment.controller;

import com.camping101.beta.web.domain.comment.dto.ReCommentCreateRequest;
import com.camping101.beta.web.domain.comment.dto.ReCommentInfoResponse;
import com.camping101.beta.web.domain.comment.dto.ReCommentUpdateRequest;
import com.camping101.beta.web.domain.comment.service.ReCommentService;
import io.swagger.annotations.Api;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camplog/recomment")
@Api(tags = {"캠핑 101 - 대댓글 API"})
public class ReCommentController {

    private final ReCommentService reCommentService;

    @PostMapping
    public ResponseEntity<ReCommentInfoResponse> createComment(@RequestBody ReCommentCreateRequest request,
                                                               @ApiIgnore Principal principal){

        request.setWriterEmail(principal.getName());
        ReCommentInfoResponse createdComment = reCommentService.createReComment(request);

        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/{reCommentId}")
    public ResponseEntity<ReCommentInfoResponse> updateComment(@PathVariable Long reCommentId,
                                                               @RequestBody ReCommentUpdateRequest request,
                                                               @ApiIgnore Principal principal){

        request.setRequesterEmail(principal.getName());
        ReCommentInfoResponse updatedComment = reCommentService.updateReComment(reCommentId, request);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{reCommentId}")
    public ResponseEntity deleteComment(@PathVariable Long reCommentId,
                                        @ApiIgnore Principal principal){

        reCommentService.deleteReComment(reCommentId, principal.getName());

        return ResponseEntity.ok().build();
    }


}
