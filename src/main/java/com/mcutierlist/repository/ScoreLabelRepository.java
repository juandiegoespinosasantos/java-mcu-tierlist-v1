package com.mcutierlist.repository;

import com.mcutierlist.model.entities.ScoreLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for the shared, pre-seeded {@link ScoreLabel} lookup table.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public interface ScoreLabelRepository extends JpaRepository<ScoreLabel, BigDecimal> {
    List<ScoreLabel> findAllByOrderByScoreDesc();
}
