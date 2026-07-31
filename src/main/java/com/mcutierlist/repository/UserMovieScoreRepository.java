package com.mcutierlist.repository;

import com.mcutierlist.entity.UserMovieScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserMovieScoreRepository extends JpaRepository<UserMovieScore, Long> {
    List<UserMovieScore> findByUserUsername(String username);
    Optional<UserMovieScore> findByUserUsernameAndMovieId(String username, Long movieId);
    List<UserMovieScore> findByUserUsernameAndScoreBetweenOrderByRankingAsc(String username, BigDecimal min, BigDecimal max);
    int countByUserUsernameAndScoreBetween(String username, BigDecimal min, BigDecimal max);
}
