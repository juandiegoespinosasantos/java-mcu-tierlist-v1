package com.mcutierlist.model.dto;

import com.mcutierlist.model.enums.Scores;

/**
 * Data transfer object for {@link Scores} entity.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 03, 2026
 * @since 25
 */
public record ScoreDTO(Double score,
                       String description,
                       String displayName) {
}