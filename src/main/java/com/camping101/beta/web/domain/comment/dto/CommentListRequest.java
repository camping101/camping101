package com.camping101.beta.web.domain.comment.dto;

import com.camping101.beta.global.paging.CustomPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentListRequest extends CustomPage {

    private Long campLogId;

    CommentListRequest(Long campLogId, int pageNumber, int recordSize, String orderBy, String orderDir) {
        super(pageNumber, recordSize, orderBy, orderDir);
        this.campLogId = campLogId;
    }

}
