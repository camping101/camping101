package com.camping101.beta.regtag.entity;

import com.camping101.beta.admin.recTag.dto.AdminRecTagCreateRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class RecTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_tag_id")
    private Long recTagId;
    private String name;
    private boolean useYn;

    @CreatedDate
    private LocalDateTime createdAt;

    public static RecTag from(AdminRecTagCreateRequest request) {
        return RecTag.builder()
                .name(request.getName())
                .useYn(false)
                .build();
    }

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }

}
