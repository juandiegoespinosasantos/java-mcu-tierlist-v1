package com.mcutierlist.model.dto;

/**
 * DTO joining a movie with the current user's score, label, tier and ranking.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */

public record McuEntryScoreDTO(MCUEntryDTO mcuEntry,
                               Double score,
                               String scoreLabel,
                               String tier,
                               Integer ranking) {
}