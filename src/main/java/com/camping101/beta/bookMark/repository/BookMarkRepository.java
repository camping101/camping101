package com.camping101.beta.bookMark.repository;

import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {


    List<BookMark> findBookMarkByMember(Member member);

}
