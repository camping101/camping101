package com.camping101.beta.db.entity.bookmark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.camping101.beta.db.entity.camplog.CampLog;
import com.camping101.beta.db.entity.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookMarkTest {

    @Test
    @DisplayName("특정 캠프로그에 북마크를 할 수 있다.")
    void changeCampLogTest(){
        // given
        BookMark bookMark = new BookMark();
        CampLog campLog = new CampLog();
        // when
        bookMark.changeCampLog(campLog);
        // then
        assertThat(bookMark.getCampLog()).isEqualTo(campLog);
        assertThat(campLog.getBookMarks().get(0)).isEqualTo(bookMark);
    }

    @Test
    @DisplayName("북마크 엔티티에 작성자를 등록할 수 있다.")
    void test(){
        // given
        Member member = new Member();
        BookMark bookMark = new BookMark();
        // when
        bookMark.changeMember(member);
        // then
        assertThat(bookMark.getMember()).isEqualTo(member);
    }
}