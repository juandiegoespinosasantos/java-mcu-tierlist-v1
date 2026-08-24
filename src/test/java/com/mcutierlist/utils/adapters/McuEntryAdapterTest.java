package com.mcutierlist.utils.adapters;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.enums.Scores;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Unit tests for {@link McuEntryAdapter}.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 23, 2026
 * @since 25
 */
class McuEntryAdapterTest {

    @Test
    @DisplayName("given a movie without a recorded user score, when transform, then return a DTO with null score and ranking")
    void givenMovieWithoutRecordedScore_whenTransform_thenReturnDtoWithNullScoreAndRanking() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        Map<Long, MovieScoreXUser> scoredMoviesById = Collections.emptyMap();

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.mcuEntry());
        Assertions.assertEquals(movie.getId(), actual.mcuEntry().id());
        Assertions.assertEquals(movie.getOriginalTitle(), actual.mcuEntry().originalTitle());
        Assertions.assertEquals(movie.getPhase(), actual.mcuEntry().phase());
        Assertions.assertNull(actual.score());
        Assertions.assertNull(actual.ranking());
    }

    @ParameterizedTest(name = "given a recorded score in the {0} tier, when transform, then return a DTO mapped to that tier")
    @EnumSource(Scores.class)
    @DisplayName("transform maps a recorded score to its tier")
    void givenMovieWithRecordedScore_whenTransform_thenReturnDtoWithMappedScoreAndRanking(final Scores tier) {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        Double recordedScore = tier.getMaxRange();
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(recordedScore)
                .ranking(2)
                .build();
        Map<Long, MovieScoreXUser> scoredMoviesById = Map.of(movie.getId(), userScore);

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie.getId(), actual.mcuEntry().id());
        Assertions.assertNotNull(actual.score());
        Assertions.assertEquals(recordedScore, actual.score().score());
        Assertions.assertEquals(tier.name(), actual.score().description());
        Assertions.assertEquals(tier.getDisplayName(), actual.score().displayName());
        Assertions.assertEquals(2, actual.ranking());
    }

    @Test
    @DisplayName("given a movie with a recorded entry but no score, when transform, then return a DTO with null score")
    void givenMovieWithRecordedEntryButNoScore_whenTransform_thenReturnDtoWithNullScore() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(null)
                .ranking(null)
                .build();
        Map<Long, MovieScoreXUser> scoredMoviesById = Map.of(movie.getId(), userScore);

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertNull(actual.score());
        Assertions.assertNull(actual.ranking());
    }

    @Test
    @DisplayName("given a movie with a recorded score outside every defined tier, when transform, then throw IllegalArgumentException")
    void givenMovieWithRecordedScoreOutsideEveryTier_whenTransform_thenThrowIllegalArgumentException() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(-1.0)
                .ranking(1)
                .build();
        Map<Long, MovieScoreXUser> scoredMoviesById = Map.of(movie.getId(), userScore);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> McuEntryAdapter.transform(movie, scoredMoviesById));
    }

    private MCUEntry buildMovie(Long id, String title, Integer phase) {
        return MCUEntry.builder()
                .id(id)
                .originalTitle(title)
                .phase(phase)
                .releaseDate(ZonedDateTime.now())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }
}
