package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.MailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MailAuthRepository extends JpaRepository<MailAuth, String> {

    Optional<MailAuth> findByEmail(String email);

}
