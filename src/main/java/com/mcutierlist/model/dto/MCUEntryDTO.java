package com.mcutierlist.model.dto;

import java.time.ZonedDateTime;

/**
 * DTO for MCU entry.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
public record MCUEntryDTO(Long id,
                          String originalTitle,
                          String alternativeTitle,
                          Integer phase,
                          ZonedDateTime releaseDate,
                          String posterUrl,
                          ZonedDateTime createdAt,
                          ZonedDateTime updatedAt) {
}