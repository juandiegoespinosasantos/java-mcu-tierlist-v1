package com.mcutierlist.model.repositories;

import com.mcutierlist.model.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for the shared, pre-seeded {@link Score} lookup table.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Double> {

    List<Score> findAllByOrderByScoreDesc();
}