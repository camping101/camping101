package com.camping101.beta.member.repository;

import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignUpType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findBySignUpTypeAndGoogleId(SignUpType signUpType, String googleId);

    Page<Member> findMembersByMemberType(MemberType memberType, Pageable pageable);


}
