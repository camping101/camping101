package com.camping101.beta.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {

    private String writerEmail;
    private Long campLogId;
    private Long parentId;
    private boolean reCommentYn;
    private String content;

}
