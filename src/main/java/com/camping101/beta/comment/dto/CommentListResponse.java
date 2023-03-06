package com.camping101.beta.comment.dto;

import com.camping101.beta.comment.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentListResponse {

    long total;
    int pageNumber = 1;
    int recordSize = 5;
    List<CommentInfoResponse> comments = new ArrayList<>();

    public static CommentListResponse fromEntity(Page<Comment> comments) {
        return CommentListResponse.builder()
                .total(comments.getTotalElements())
                .pageNumber(comments.getNumber())
                .recordSize(comments.getNumberOfElements())
                .comments(comments.getContent().stream()
                        .map(CommentInfoResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

}
