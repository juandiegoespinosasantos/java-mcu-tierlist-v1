package com.mcutierlist.model.dto;

/**
 * DTO joining a movie with the current user's score, label, tier and ranking.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public record McuEntryScoreDTO(MCUEntryDTO mcuEntry,
                               ScoreDTO score,
                               Integer ranking) {

    public Long mcuEntryId() {
        return (mcuEntry == null) ? null : mcuEntry.id();
    }

    public Integer mcuEntryPhase() {
        return (mcuEntry == null) ? null : mcuEntry.phase();
    }
}