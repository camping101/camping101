package com.camping101.beta.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookMarkId;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "bookMark")
    private List<CampLog> campLogs = new ArrayList<>();

    public void addCampLog(CampLog campLog) {
        this.campLogs.add(campLog);
        if (campLog.getBookMark() != this) {
            campLog.changeBookMark(this);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
        if (member.getBookMark() != this) {
            member.changeBookMark(this);
        }
    }

}
