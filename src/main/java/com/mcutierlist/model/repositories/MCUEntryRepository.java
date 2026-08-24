package com.mcutierlist.model.repositories;

import com.mcutierlist.model.entities.MCUEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link MCUEntry} entity.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Repository
public interface MCUEntryRepository extends JpaRepository<MCUEntry, Long> {

    List<MCUEntry> findAllByOrderByReleaseDateAsc();
}