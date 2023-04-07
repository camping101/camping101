package com.camping101.beta.web.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateRequest {

    private Long commentId;
    private String requesterEmail;
    private Long campLogId;
    private String content;

}
