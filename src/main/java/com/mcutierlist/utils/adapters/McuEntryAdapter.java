package com.mcutierlist.utils.adapters;

import com.mcutierlist.model.dto.MCUEntryDTO;
import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Adapter class for McuEntry domain.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 03, 2026
 * @since 17
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class McuEntryAdapter {

    public static McuEntryScoreDTO transform(final MCUEntry movie,
                                             final Map<Long, MovieScoreXUser> scoredMoviesById,
                                             final Map<Double, Score> labelMap) {
        MovieScoreXUser ums = scoredMoviesById.get(movie.getId());
        Double score = ums != null ? ums.getScore() : null;
        Score label = score != null ? labelMap.get(score) : null;
        MCUEntryDTO dto = new MCUEntryDTO(
                movie.getId(),
                movie.getOriginalTitle(),
                movie.getAlternativeTitle(),
                movie.getPhase(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getCreatedAt(),
                movie.getUpdatedAt());

        return new McuEntryScoreDTO(
                dto,
                score,
                label.getDisplayName(),
                score != null ? resolveTier(score) : null,
                ums != null ? ums.getRanking() : null
        );
    }

    public static String resolveTier(Double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 4.0) return "Very Good";
        if (score >= 3.0) return "Good";
        if (score >= 2.0) return "Weak";

        return "Bad";
    }
}