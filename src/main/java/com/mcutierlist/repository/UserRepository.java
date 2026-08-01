package com.mcutierlist.repository;

import com.mcutierlist.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manually-seeded {@link User} accounts.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public interface UserRepository extends JpaRepository<User, String> {
}
