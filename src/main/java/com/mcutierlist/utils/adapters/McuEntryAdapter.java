package com.mcutierlist.utils.adapters;

import com.mcutierlist.model.dto.MCUEntryDTO;
import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.dto.ScoreDTO;
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
 * @since 25
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class McuEntryAdapter {

    /**
     * Transforms a {@link MCUEntry} entity and its associated user score into a {@link McuEntryScoreDTO}.
     *
     * @param movie            The {@link MCUEntry} entity to be transformed.
     * @param scoredMoviesById A map of movie IDs to their corresponding {@link MovieScoreXUser} entities, representing the user's scores for the movies.
     * @return A {@link McuEntryScoreDTO} containing the transformed movie and its associated user score.
     */
    public static McuEntryScoreDTO transform(final MCUEntry movie,
                                             final Map<Long, MovieScoreXUser> scoredMoviesById) {
        MCUEntryDTO mcuEntryDto = transform(movie);

        MovieScoreXUser userScore = scoredMoviesById.get(movie.getId());
        Score score = (userScore == null) ? null : userScore.getScore();
        ScoreDTO scoreDto = transform(score);

        Integer ranking = (userScore == null) ? null : userScore.getRanking();

        return new McuEntryScoreDTO(mcuEntryDto, scoreDto, ranking);
    }

    // TODO
    public static String resolveTier(Double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 4.0) return "Very Good";
        if (score >= 3.0) return "Good";
        if (score >= 2.0) return "Weak";

        return "Bad";
    }

    private static MCUEntryDTO transform(final MCUEntry entity) {
        return new MCUEntryDTO(
                entity.getId(),
                entity.getOriginalTitle(),
                entity.getAlternativeTitle(),
                entity.getPhase(),
                entity.getReleaseDate(),
                entity.getPosterUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private static ScoreDTO transform(final Score entity) {
        return (entity == null) ? null : new ScoreDTO(entity.getScore(), entity.getDescription(), entity.getDisplayName());

    }
}