package com.camping101.beta.web.domain.comment.dto;

import com.camping101.beta.db.entity.comment.ReComment;
import java.time.LocalDateTime;
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
public class ReCommentInfoResponse {

   private Long reCommentId;
    private String writerEmail;
    private String writerNickName;
    private String profileImagePath;
    private String content;
    private long like;
    private LocalDateTime createdAt;

    public static ReCommentInfoResponse fromEntity(ReComment reComment){
        return ReCommentInfoResponse.builder()
                .reCommentId(reComment.getReCommentId())
                .writerEmail(reComment.getMember().getEmail())
                .writerNickName(reComment.getMember().getNickname())
                .profileImagePath(reComment.getMember().getProfileImagePath())
                .content(reComment.getContent())
                .createdAt(reComment.getCreatedAt())
                .build();
    }

}
