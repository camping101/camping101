package com.camping101.beta.regtag.entity;

import com.camping101.beta.admin.recTag.dto.AdminRecTagCreateRequest;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class RecTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reg_tag_id")
    private Long recTagId;

    private Long campLogId;

    private String name;
    private boolean useYn;

    public static RecTag from(AdminRecTagCreateRequest request) {
        return RecTag.builder()
                .campLogId(request.getCampLogId())
                .name(request.getName())
                .useYn(false)
                .build();
    }

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }

}
