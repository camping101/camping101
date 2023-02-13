package com.camping101.beta.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long recTagId;

    @ManyToOne
    @JoinColumn(name = "CAMP_LOG_ID")
    private CampLog campLog;

    private String name;
    private boolean useYn;

    public void changeCampLog(CampLog campLog) {
        this.campLog = campLog;
        if (!campLog.getRecTags().contains(this)) {
            campLog.getRecTags().add(this);
        }
    }

}
