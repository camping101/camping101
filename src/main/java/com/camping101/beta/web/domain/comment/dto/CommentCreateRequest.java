package com.camping101.beta.web.domain.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {

    @ApiModelProperty(hidden = true)
    private String writerEmail;
    private Long campLogId;
    private long parentId = -1;
    private boolean reCommentYn;
    private String content;

}
