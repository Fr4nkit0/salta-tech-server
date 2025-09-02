package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken,Long> {
	@Query("SELECT j FROM RefreshToken  j WHERE j.token=?1")
	Optional<RefreshToken> findByToken(String jwt);

	@Modifying
	@Query("""
    DELETE FROM RefreshToken rt
    WHERE rt.user.email = ?1
    """)
	void deleteAllByEmail(String email);
}
