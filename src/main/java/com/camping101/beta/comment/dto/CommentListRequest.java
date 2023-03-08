package com.camping101.beta.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentListRequest {

    private Long campLogId;
    int pageNumber = 0;
    int recordSize = 5;

}
