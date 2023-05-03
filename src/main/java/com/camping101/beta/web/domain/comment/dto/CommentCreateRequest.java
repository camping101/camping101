package com.camping101.beta.web.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
