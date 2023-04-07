package com.camping101.beta.web.domain.token.repository;

import com.camping101.beta.web.domain.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

}
