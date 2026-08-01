package com.mcutierlist.repository;

import com.mcutierlist.model.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for the pre-seeded {@link Movie} catalog.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByReleaseDateAsc();
}
