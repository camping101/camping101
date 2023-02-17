package com.camping101.beta.campLog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @ManyToOne
    @JoinColumn(name = "camp_log_id")
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
