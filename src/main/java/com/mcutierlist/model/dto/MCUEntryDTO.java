package com.mcutierlist.model.dto;

import com.mcutierlist.model.entities.MCUEntry;

import java.time.ZonedDateTime;

/**
 * Data transfer object for {@link MCUEntry} entity.
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
                          ZonedDateTime createdAt, // TODO: Set as millis
                          ZonedDateTime updatedAt) {
}