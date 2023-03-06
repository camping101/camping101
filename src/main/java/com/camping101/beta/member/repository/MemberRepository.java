package com.camping101.beta.member.repository;

import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.MemberType;
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
