package com.mcutierlist.model.repositories;

import com.mcutierlist.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link User} entity.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}