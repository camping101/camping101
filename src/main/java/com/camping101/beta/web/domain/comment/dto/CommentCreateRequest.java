package com.camping101.beta.web.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {

    private String writerEmail;
    private Long campLogId;
    private long parentId = -1;
    private boolean reCommentYn;
    private String content;

}
