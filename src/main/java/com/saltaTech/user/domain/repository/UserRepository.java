package com.saltaTech.user.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User,Long> {
	@Transactional(readOnly = true)
	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.enabled = true ")
	Optional<User> findActiveByEmail (String email);
	@Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM User u
        WHERE u.email = ?1
          AND u.isSuperUser = true
          AND u.enabled = true
    """)
	boolean existsSuperUserByEmailAndEnabledTrue(String email);

}
