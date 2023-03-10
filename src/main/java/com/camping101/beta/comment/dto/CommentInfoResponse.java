package com.camping101.beta.comment.dto;

import com.camping101.beta.campLog.dto.CampLogInfoResponse;
import com.camping101.beta.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentInfoResponse {

    private Long commentId;
    private String writerEmail;
    private String writerNickName;
    private String profileImagePath;
    private long parentId;
    private boolean reCommentYn;
    private String content;
    private long like;

    private LocalDateTime createdAt;

    public static CommentInfoResponse fromEntity(Comment comment){
        return CommentInfoResponse.builder()
                .commentId(comment.getCommentId())
                .writerEmail(comment.getMember().getEmail())
                .writerNickName(comment.getMember().getNickname())
                .profileImagePath(comment.getMember().getImage())
                .reCommentYn(comment.isReCommentYn())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
