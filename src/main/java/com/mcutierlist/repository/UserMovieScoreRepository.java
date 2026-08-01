package com.mcutierlist.repository;

import com.mcutierlist.model.entities.UserMovieScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for per-user {@link UserMovieScore} scores and rankings.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public interface UserMovieScoreRepository extends JpaRepository<UserMovieScore, Long> {
    List<UserMovieScore> findByUserUsername(String username);
    Optional<UserMovieScore> findByUserUsernameAndMovieId(String username, Long movieId);
    List<UserMovieScore> findByUserUsernameAndScoreBetweenOrderByRankingAsc(String username, BigDecimal min, BigDecimal max);
    int countByUserUsernameAndScoreBetween(String username, BigDecimal min, BigDecimal max);
}
