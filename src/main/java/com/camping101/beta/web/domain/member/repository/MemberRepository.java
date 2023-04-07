package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Page<Member> findMembersByMemberType(MemberType memberType, Pageable pageable);


}
