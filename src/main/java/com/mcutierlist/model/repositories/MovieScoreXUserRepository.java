package com.mcutierlist.model.repositories;

import com.mcutierlist.model.entities.MovieScoreXUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for per-user {@link MovieScoreXUser} scores and rankings.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Repository
public interface MovieScoreXUserRepository extends JpaRepository<MovieScoreXUser, Long> {

    List<MovieScoreXUser> findByUserUsername(String username);

    Optional<MovieScoreXUser> findByUserUsernameAndMcuEntryId(String username, Long movieId);

    List<MovieScoreXUser> findByUserUsernameAndScoreBetweenOrderByRankingAsc(String username, Double min, Double max);

    int countByUserUsernameAndScoreBetween(String username, Double min, Double max);
}
