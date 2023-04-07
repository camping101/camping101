package com.camping101.beta.web.domain.bookMark.repository;

import com.camping101.beta.db.entity.bookMark.BookMark;
import com.camping101.beta.db.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {


    Page<BookMark> findBookMarkByMember(Member member, Pageable pageable);

}
