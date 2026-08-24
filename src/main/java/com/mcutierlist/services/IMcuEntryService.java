package com.mcutierlist.services;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;

import java.util.List;
import java.util.Map;

/**
 * Interface for {@link MCUEntry} business logic service.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 03, 2026
 * @since 25
 */
public interface IMcuEntryService {

    Map<Integer, List<McuEntryScoreDTO>> getMoviesByPhase(String username);

    Map<String, String> getScoreLabelsForDisplay();
}